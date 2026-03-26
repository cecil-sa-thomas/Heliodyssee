package fr.thomascecil.heliodysse.application.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import fr.thomascecil.heliodysse.domain.model.entity.PaymentCard;
import fr.thomascecil.heliodysse.domain.model.entity.User;
import fr.thomascecil.heliodysse.domain.port.in.PaymentCardUseCase;
import fr.thomascecil.heliodysse.domain.port.out.PaymentCardRepository;
import fr.thomascecil.heliodysse.domain.port.out.UserRepository;
import fr.thomascecil.heliodysse.domain.service.payment.PaymentCardService;
import fr.thomascecil.heliodysse.domain.service.payment.StripeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentCardApplicationService implements PaymentCardUseCase {
    private final PaymentCardRepository paymentCardrepository;
    private final UserRepository userRepository;
    private final PaymentCardService paymentCardService;
    private final StripeService stripeService;

    public PaymentCardApplicationService(PaymentCardRepository paymentCardrepository, UserRepository userRepository, PaymentCardService paymentCardService, StripeService stripeService) {
        this.paymentCardrepository = paymentCardrepository;
        this.userRepository = userRepository;
        this.paymentCardService = paymentCardService;
        this.stripeService = stripeService;
    }


    //Ajouter une logique d'authentification pour empêcher l'user de recupérer des carte qui ne lui appartiennent pas
    @Override
    public Optional<PaymentCard> getPaymentCardById(Long id) {
        return paymentCardrepository.findById(id);
    }

    @Override
    public Optional<PaymentCard> getDefaultPaymentCardByClient(Long id) {
        List<PaymentCard> cards = paymentCardrepository.findByUserId(id);

        return cards.stream()
                .filter(card -> Boolean.TRUE.equals(card.getIsDefault()))
                .findFirst();
    }


    @Override
    public List<PaymentCard> getPaymentCardsByClient(Long id) {
        //logique de vérification si id correspond à l'user authentifié
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));

        return paymentCardrepository.findByUserId(id);
    }

    @Override
    public List<PaymentCard> getAll() {
        return paymentCardrepository.findAll();
    }

    @Override
    public PaymentCard createPaymentCard(Long userId,  String paymentMethodId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'ID : " + userId));

        try {
            // 1. Créer un Customer Stripe si nécessaire
            if (user.getStripeCustomerId() == null) {
                Customer customer = stripeService.createCustomer();
                user.setStripeCustomerId(customer.getId());
                userRepository.save(user);
            }

            // 2. Créer une carte dans Stripe
            PaymentMethod paymentMethod = stripeService.addCardToCustomer(
                    user.getStripeCustomerId(),
                    paymentMethodId
            );

            // 3. Créer la PaymentCard locale
            List<PaymentCard> existingCards = paymentCardrepository.findByUserId(user.getIdUser());
            boolean hasDefault = existingCards.stream().anyMatch(PaymentCard::getIsDefault);

            PaymentCard newCard = PaymentCard.builder()
                    .stripeId(paymentMethod.getId()) // ✅ L'ID du PaymentMethod, pas de la carte
                    .lastDigit(paymentMethod.getCard().getLast4()) // ✅ OK
                    .brand(paymentMethod.getCard().getBrand()) // ✅ OK
                    .expMonth(paymentMethod.getCard().getExpMonth().intValue()) // ✅ Via .getCard()
                    .expYear(paymentMethod.getCard().getExpYear().intValue()) // ✅ Via .getCard()
                    .isDefault(!hasDefault)
                    .dateCreation(LocalDateTime.now())
                    .idUser(user.getIdUser())
                    .build();

            // 4. Validation métier
            paymentCardService.validatePaymentCard(newCard);

            return paymentCardrepository.save(newCard);

        } catch (StripeException e) {
            throw new RuntimeException("Erreur Stripe : " + e.getMessage(), e);
        }
    }


    @Transactional
    @Override
    public void setDefaultCard(Long id) {
        PaymentCard newDefaultCard = paymentCardrepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carte introuvable avec l'ID : " + id));

        userRepository.findById(newDefaultCard.getIdUser())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'ID : " + newDefaultCard.getIdUser()));

        List<PaymentCard> cards = paymentCardrepository.findByUserId(newDefaultCard.getIdUser());

        for (PaymentCard card : cards) {
            if (!card.getIdPaymentCard().equals(id) && Boolean.TRUE.equals(card.getIsDefault())) {
                card.setIsDefault(false);
                paymentCardrepository.save(card);
            }
        }

        newDefaultCard.setIsDefault(true);
        paymentCardService.validatePaymentCard(newDefaultCard);
        paymentCardrepository.save(newDefaultCard);
    }

    //Supprime automatiquement les cartes de paiment dont la date est expiré
    @Scheduled(cron = "0 0 0 * * ?") // Chaque jour à minuit
    public void deleteExpiredCards() {
        YearMonth currentYearMonth = YearMonth.now();
        List<PaymentCard> allCards = paymentCardrepository.findAll();

        for (PaymentCard card : allCards) {
            YearMonth cardExpiry = YearMonth.of(card.getExpYear(), card.getExpMonth());
            if (cardExpiry.isBefore(currentYearMonth)) {
                paymentCardrepository.deleteById(card.getIdPaymentCard());
            }
        }

        /* Ajoutez plus tard une requête jpql dans le jpaRepo :
        @Modifying
        @Query(value = """
    DELETE FROM payment_card
    WHERE (exp_year < :currentYear)
       OR (exp_year = :currentYear AND exp_month < :currentMonth)
    """, nativeQuery = true)
        int deleteExpiredCards(@Param("currentYear") int currentYear,
        @Param("currentMonth") int currentMonth);

        Puis dans le Service :
        @Scheduled(cron = "0 0 0 * * ?")
@Transactional
public void deleteExpiredCards() {
    YearMonth current = YearMonth.now();
    int deletedCount = paymentCardrepository.deleteExpiredCards(
        current.getYear(),
        current.getMonthValue()
    );
    // Optionnel : log du nombre de cartes supprimées
}
        */
    }

    @Override
    public void deleteUserAuthCardById(Long userId, Long cardId) {
        PaymentCard card = paymentCardrepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with id " + cardId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with id " + userId));

        if (!card.getIdUser().equals(user.getIdUser())) {
            throw new AccessDeniedException("Cette carte n'appartient pas à l'utilisateur.");

        }

        paymentCardrepository.deleteById(cardId);
    }



}
