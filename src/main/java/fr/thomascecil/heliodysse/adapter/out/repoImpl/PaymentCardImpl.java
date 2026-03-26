package fr.thomascecil.heliodysse.adapter.out.repoImpl;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.BookingEntity;
import fr.thomascecil.heliodysse.adapter.out.jpaEntity.FlightEntity;
import fr.thomascecil.heliodysse.adapter.out.jpaEntity.PaymentCardEntity;
import fr.thomascecil.heliodysse.adapter.out.jpaEntity.UserEntity;
import fr.thomascecil.heliodysse.adapter.out.mapper.PaymentCardMapper;
import fr.thomascecil.heliodysse.adapter.out.mapper.UserMapper;
import fr.thomascecil.heliodysse.adapter.out.repository.JpaPaymentCardRepository;
import fr.thomascecil.heliodysse.adapter.out.repository.JpaUserRepository;
import fr.thomascecil.heliodysse.domain.model.entity.PaymentCard;
import fr.thomascecil.heliodysse.domain.port.out.PaymentCardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PaymentCardImpl implements PaymentCardRepository {

    private final JpaPaymentCardRepository jpaPaymentCardrepository;
    private final JpaUserRepository jpaUserRepository;
    private final PaymentCardMapper mapper;
    private final UserMapper userMapper;

    public PaymentCardImpl(JpaPaymentCardRepository repository, JpaUserRepository jpaUserRepository, PaymentCardMapper paymentCardMapper, UserMapper userMapper) {
        this.jpaPaymentCardrepository = repository;
        this.jpaUserRepository = jpaUserRepository;
        this.mapper = paymentCardMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<PaymentCard> findById(Long id){
        return jpaPaymentCardrepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<PaymentCard> findByUserId(Long id) {
        List<PaymentCardEntity> entities = jpaPaymentCardrepository.findByUserIdUser(id);
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }


    @Override
    public List<PaymentCard> findAll(){
        List<PaymentCardEntity> entities = jpaPaymentCardrepository.findAll();
        return  entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentCard save(PaymentCard paymentCard) {
        PaymentCardEntity entity = mapper.toEntity(paymentCard);

        // -- User --
        UserEntity userEntity = jpaUserRepository.findById(paymentCard.getIdUser())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        entity.setUser(userEntity);

        PaymentCardEntity saved = jpaPaymentCardrepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpaPaymentCardrepository.deleteById(id);
    }
}
