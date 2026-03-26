package fr.thomascecil.heliodysse.adapter.out.repository;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.FlightEntity;
import fr.thomascecil.heliodysse.adapter.out.jpaEntity.SpacePortEntity;
import fr.thomascecil.heliodysse.domain.model.enums.flightEnum.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaFlightRepository extends JpaRepository<FlightEntity, Long> {
    List<FlightEntity> findByDepartureInAndArrivalInAndDateDepartureAfter(
            List<SpacePortEntity> departurePorts,
            List<SpacePortEntity> arrivalPorts,
            LocalDateTime fromDate
    );
    public List<FlightEntity> findByStatusIn(List<FlightStatus> statuses);
}
//Approche jpql (plus "facile" ...)
/*
@Query("""
    SELECT f FROM FlightEntity f
    WHERE f.departure.planet.idPlanet = :departurePlanetId
      AND f.arrival.planet.idPlanet = :arrivalPlanetId
      AND f.dateDeparture > :fromDate
""")
List<FlightEntity> findFlightsByPlanetIdsAndDate(
    @Param("departurePlanetId") Short departurePlanetId,
    @Param("arrivalPlanetId") Short arrivalPlanetId,
    @Param("fromDate") LocalDateTime fromDate
);
 */
