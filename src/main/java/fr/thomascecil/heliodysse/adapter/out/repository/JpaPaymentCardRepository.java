package fr.thomascecil.heliodysse.adapter.out.repository;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.PaymentCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPaymentCardRepository extends JpaRepository<PaymentCardEntity, Long> {
    public List<PaymentCardEntity> findByUserIdUser(Long idUser);
}
