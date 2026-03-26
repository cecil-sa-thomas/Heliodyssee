package fr.thomascecil.heliodysse.adapter.in.dto.mapper;

import fr.thomascecil.heliodysse.adapter.in.dto.request.booking.BookingCreateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.request.booking.BookingUpdateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.response.booking.BookingResponseDTO;
import fr.thomascecil.heliodysse.domain.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingDtoMapper {
    @Mapping(target = "idFlight", source = "flightId")
    @Mapping(target = "idUser", source = "userId")
    Booking toDomain(BookingCreateDTO dto);

    BookingResponseDTO toDto(Booking booking);

    @Mapping(target = "idBooking", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "idFlight", ignore = true)
    @Mapping(target = "idUser", ignore = true)
    void updateBookingFromDto(BookingUpdateDTO dto, @MappingTarget Booking booking);
}
