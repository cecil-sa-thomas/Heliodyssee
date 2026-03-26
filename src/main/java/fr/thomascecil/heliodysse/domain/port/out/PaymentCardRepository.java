package fr.thomascecil.heliodysse.domain.port.out;

import fr.thomascecil.heliodysse.domain.model.entity.PaymentCard;

import java.util.List;
import java.util.Optional;

public interface PaymentCardRepository {
    public Optional<PaymentCard> findById(Long id);
    public List<PaymentCard> findByUserId(Long idUser);
    public List<PaymentCard> findAll();
    public PaymentCard save(PaymentCard paymentCard);
    public void deleteById(Long id);
}
