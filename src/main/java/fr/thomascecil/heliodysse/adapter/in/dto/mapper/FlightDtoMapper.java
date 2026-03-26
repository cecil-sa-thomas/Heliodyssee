package fr.thomascecil.heliodysse.adapter.in.dto.mapper;

import fr.thomascecil.heliodysse.adapter.in.dto.request.flight.FlightCreateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.request.flight.FlightUpdateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.response.flight.FlightResponseDTO;
import fr.thomascecil.heliodysse.domain.model.entity.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FlightDtoMapper {
    Flight toDomain(FlightCreateDTO dto);
    FlightResponseDTO toDto(Flight flight);


    @Mapping(target = "idFlight", ignore = true)
    @Mapping(target = "numFlight", ignore = true)
    @Mapping(target = "seats", ignore = true)
    @Mapping(target = "seatsAvailable", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateFlightFromDto(FlightUpdateDTO dto, @MappingTarget Flight flight);

    Flight toDomain(FlightUpdateDTO dto);//TEST//
}

