package fr.thomascecil.heliodysse.adapter.out.repository;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.BookingEntity;
import fr.thomascecil.heliodysse.domain.model.enums.bookingEnum.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaBookingRepository extends JpaRepository<BookingEntity, Long> {
    public List<BookingEntity> findByUser_IdUser(Long idUser);
    public List<BookingEntity> findByFlight_IdFlight(Long idFlight);
    public List<BookingEntity> findByStatus(BookingStatus status);
    public List<BookingEntity> findByDateCreation(LocalDateTime dateCreation);
    public List<BookingEntity> findByDateCreationAfter(LocalDateTime date);
    public List<BookingEntity> findByDateCreationBefore(LocalDateTime date);
    public List<BookingEntity> findByDateCreationBetween(LocalDateTime start, LocalDateTime end);

}
