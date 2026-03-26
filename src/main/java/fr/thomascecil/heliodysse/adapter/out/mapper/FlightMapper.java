package fr.thomascecil.heliodysse.adapter.out.mapper;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.FlightEntity;
import fr.thomascecil.heliodysse.domain.model.entity.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    @Mapping(target = "idDeparture", source = "departure.idSpacePort")
    @Mapping(target = "idArrival", source = "arrival.idSpacePort")
    Flight toDomain(FlightEntity entity);

    @Mapping(target = "departure", ignore = true)
    @Mapping(target = "arrival", ignore = true)
    FlightEntity toEntity(Flight flight);
}
