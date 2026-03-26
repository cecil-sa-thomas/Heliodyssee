package fr.thomascecil.heliodysse.domain.port.out;

import fr.thomascecil.heliodysse.domain.model.entity.Booking;
import fr.thomascecil.heliodysse.domain.model.enums.bookingEnum.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingRepository {

    Optional<Booking> findById(Long id);
    List<Booking>  findByUserId(Long idUser);
    List<Booking> findByFlightId(Long idFlight);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByDateCreation(LocalDateTime dateCreation);
    List<Booking> findByDateCreationAfter(LocalDateTime date);
    List<Booking> findByDateCreationBefore(LocalDateTime date);
    List<Booking> findByDateCreationBetween(LocalDateTime start, LocalDateTime end);
    // Trouver les réservations à un certain prix
    List<Booking> findByPrice(BigDecimal price);
    List<Booking> findAll();

    //*logique de paiement*//
    Booking save(Booking booking);

    void deleteById(Long id);
}
