package fr.thomascecil.heliodysse.adapter.out.mapper;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.PlanetEntity;
import fr.thomascecil.heliodysse.domain.model.entity.Planet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlanetMapper {
    Planet toDomain(PlanetEntity entity);
    PlanetEntity toEntity(Planet planet);
}
