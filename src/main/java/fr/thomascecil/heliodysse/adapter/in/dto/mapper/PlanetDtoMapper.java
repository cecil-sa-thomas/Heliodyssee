package fr.thomascecil.heliodysse.adapter.in.dto.mapper;

import fr.thomascecil.heliodysse.adapter.in.dto.request.planet.PlanetCreateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.request.planet.PlanetUpdateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.response.planet.PlanetResponseDTO;
import fr.thomascecil.heliodysse.domain.model.entity.Planet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PlanetDtoMapper {
    Planet toDomain(PlanetCreateDTO dto);
    PlanetResponseDTO toDto(Planet planet);

    @Mapping(target = "idPlanet", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updatePlanetFromDto(PlanetUpdateDTO dto, @MappingTarget Planet planet);

}

