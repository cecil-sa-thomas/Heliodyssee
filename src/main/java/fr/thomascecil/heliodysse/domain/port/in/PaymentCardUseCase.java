package fr.thomascecil.heliodysse.domain.port.in;

import fr.thomascecil.heliodysse.domain.model.entity.PaymentCard;
import java.util.List;
import java.util.Optional;

public interface PaymentCardUseCase {

    Optional<PaymentCard> getPaymentCardById(Long id);
    Optional<PaymentCard> getDefaultPaymentCardByClient(Long id);
    List<PaymentCard> getPaymentCardsByClient(Long id);
    List<PaymentCard> getAll();
    PaymentCard createPaymentCard(Long idUser, String stripeToken);
    void setDefaultCard(Long id);
    void deleteUserAuthCardById(Long userId, Long cardId);
}
