package fr.thomascecil.heliodysse.adapter.out.mapper;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.BookingEntity;
import fr.thomascecil.heliodysse.domain.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "idUser", source = "user.idUser")
    @Mapping(target = "idFlight", source = "flight.idFlight")
    Booking toDomain(BookingEntity entity);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "flight", ignore = true)
    BookingEntity toEntity(Booking booking);
}
