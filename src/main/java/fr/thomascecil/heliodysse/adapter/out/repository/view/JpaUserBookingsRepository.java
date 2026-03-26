package fr.thomascecil.heliodysse.adapter.out.repository.view;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.BookingEntity;
import fr.thomascecil.heliodysse.domain.model.entity.viewdto.BookingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaUserBookingsRepository extends JpaRepository<BookingEntity, Long> {

    @Query("""
    SELECT new fr.thomascecil.heliodysse.domain.model.entity.viewdto.BookingInfo(
        b.idBooking, 
        f.numFlight,
        f.dateDeparture, 
        f.dateArrival,
        dep.name, 
        depPlanet.planetName,
        arr.name, 
        arrPlanet.planetName
    )
    FROM BookingEntity b
    JOIN b.flight f
    JOIN f.departure dep
    JOIN dep.planet depPlanet
    JOIN f.arrival arr
    JOIN arr.planet arrPlanet
    WHERE b.user.idUser = :userId
    ORDER BY f.dateDeparture DESC
""")
    List<BookingInfo> findAllByUserIdWithFlightDetails(@Param("userId") Long userId);

}
