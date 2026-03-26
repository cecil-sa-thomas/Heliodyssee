package fr.thomascecil.heliodysse.adapter.out.mapper;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.PaymentCardEntity;
import fr.thomascecil.heliodysse.domain.model.entity.PaymentCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentCardMapper {
    @Mapping(target = "idUser", source = "user.idUser")
    PaymentCard toDomain(PaymentCardEntity paymentCard);

    @Mapping(target = "user", ignore = true)
    PaymentCardEntity toEntity(PaymentCard paymentCard);
}
