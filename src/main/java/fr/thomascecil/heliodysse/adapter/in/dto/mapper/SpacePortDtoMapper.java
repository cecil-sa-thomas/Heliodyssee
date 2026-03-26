package fr.thomascecil.heliodysse.adapter.in.dto.mapper;

import fr.thomascecil.heliodysse.adapter.in.dto.request.spacePort.SpacePortCreateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.request.spacePort.SpacePortUpdateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.response.spacePort.SpacePortResponseDTO;
import fr.thomascecil.heliodysse.domain.model.entity.SpacePort;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SpacePortDtoMapper {
    SpacePort toDomain(SpacePortCreateDTO dto);
    SpacePortResponseDTO toDto(SpacePort spacePort);

    @Mapping(target = "idSpacePort", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateSpacePortFromDto(SpacePortUpdateDTO dto, @MappingTarget SpacePort spacePort);
}
