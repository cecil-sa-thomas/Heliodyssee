package fr.thomascecil.heliodysse.adapter.in.dto.mapper;

import fr.thomascecil.heliodysse.adapter.in.dto.request.paymentCard.PaymentCardCreateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.response.paymentCard.PaymentCardResponseDTO;
import fr.thomascecil.heliodysse.domain.model.entity.PaymentCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentCardDTOMapper {
    PaymentCard toDomain(PaymentCardCreateDTO dto);
    PaymentCardResponseDTO toDto(PaymentCard paymentCard);
}
