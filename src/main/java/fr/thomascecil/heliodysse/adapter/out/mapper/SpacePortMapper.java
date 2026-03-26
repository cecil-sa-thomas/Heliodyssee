package fr.thomascecil.heliodysse.adapter.out.mapper;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.SpacePortEntity;
import fr.thomascecil.heliodysse.domain.model.entity.SpacePort;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpacePortMapper {
    @Mapping(target = "idPlanet", source = "planet.idPlanet")
    SpacePort toDomain(SpacePortEntity entity);

    @Mapping(target = "planet", ignore = true)
    SpacePortEntity toEntity(SpacePort spacePort);
}
