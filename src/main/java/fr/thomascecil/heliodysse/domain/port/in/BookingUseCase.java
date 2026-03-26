package fr.thomascecil.heliodysse.domain.port.in;

import fr.thomascecil.heliodysse.domain.model.entity.Booking;
import fr.thomascecil.heliodysse.domain.model.enums.bookingEnum.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingUseCase {

    Optional<Booking> getBookingById(Long id);
    List<Booking> getByUserId(Long idUser);
    List<Booking> getByFlightId(Long idFlight);
    List<Booking> getByStatus(BookingStatus status);
    List<Booking> getByDateCreation(LocalDateTime dateCreation);
    List<Booking> getByDateCreationAfter(LocalDateTime date);
    List<Booking> getByDateCreationBefore(LocalDateTime date);
    List<Booking> getByDateCreationBetween(LocalDateTime start, LocalDateTime end);
    List<Booking> getByPrice(BigDecimal price);
    List<Booking> getAll();

    Booking createBookingWithPayment(Booking booking, String stripeId, Long clientId);
    Booking updateBooking(Booking booking);
    void deleteById(Long id);
}
