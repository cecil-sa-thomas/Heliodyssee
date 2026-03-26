package fr.thomascecil.heliodysse.adapter.out.repoImpl;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.BookingEntity;
import fr.thomascecil.heliodysse.adapter.out.jpaEntity.FlightEntity;
import fr.thomascecil.heliodysse.adapter.out.jpaEntity.UserEntity;
import fr.thomascecil.heliodysse.adapter.out.mapper.BookingMapper;
import fr.thomascecil.heliodysse.adapter.out.repository.JpaBookingRepository;
import fr.thomascecil.heliodysse.adapter.out.repository.JpaFlightRepository;
import fr.thomascecil.heliodysse.adapter.out.repository.JpaUserRepository;
import fr.thomascecil.heliodysse.domain.model.entity.Booking;
import fr.thomascecil.heliodysse.domain.model.enums.bookingEnum.BookingStatus;
import fr.thomascecil.heliodysse.domain.port.out.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
    private final JpaBookingRepository jpaBookingRepository;
    private final JpaUserRepository jpaUserRepository;
    private final JpaFlightRepository jpaFlightRepository;
    private final BookingMapper mapper;

    public BookingRepositoryImpl(JpaBookingRepository jpaRepo, JpaUserRepository jpaUserRepository, JpaFlightRepository jpaFlightRepository, BookingMapper mapper) {
        this.jpaBookingRepository = jpaRepo;
        this.jpaUserRepository = jpaUserRepository;
        this.jpaFlightRepository = jpaFlightRepository;
        this.mapper = mapper;
        ;
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return jpaBookingRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Booking> findByUserId(Long idUser) {
        List<BookingEntity> entities = jpaBookingRepository.findByUser_IdUser(idUser);
        return  entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByFlightId(Long idFlight) {
        List<BookingEntity> entities = jpaBookingRepository.findByFlight_IdFlight(idFlight);
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByStatus(BookingStatus status) {
        List<BookingEntity> entities = jpaBookingRepository.findByStatus(status);
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByDateCreation(LocalDateTime dateCreation) {
        List<BookingEntity> entities = jpaBookingRepository.findByDateCreation(dateCreation);
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByDateCreationAfter(LocalDateTime date) {
        return List.of();
    }

    @Override
    public List<Booking> findByDateCreationBefore(LocalDateTime date) {
        return List.of();
    }

    @Override
    public List<Booking> findByDateCreationBetween(LocalDateTime start, LocalDateTime end) {
        return List.of();
    }

    @Override
    public List<Booking> findByPrice(BigDecimal price) {
        return List.of();
    }

    @Override
    public List<Booking> findAll() {
        List<BookingEntity> entities = jpaBookingRepository.findAll();
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Booking save(Booking booking) {
        BookingEntity entity = mapper.toEntity(booking);

        // -- User --
        UserEntity userEntity = jpaUserRepository.findById(booking.getIdUser())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        entity.setUser(userEntity);

        // -- Flight --
        FlightEntity flightEntity = jpaFlightRepository.findById(booking.getIdFlight())
                .orElseThrow(() -> new EntityNotFoundException("Flight not found"));
        entity.setFlight(flightEntity);

        BookingEntity saved = jpaBookingRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpaBookingRepository.deleteById(id);
    }

}
