package fr.thomascecil.heliodysse.domain.port.out;

import fr.thomascecil.heliodysse.domain.model.entity.Flight;
import fr.thomascecil.heliodysse.domain.model.entity.SpacePort;
import fr.thomascecil.heliodysse.domain.model.enums.flightEnum.FlightStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository {
    Optional<Flight> findById(Long id);
    List<Flight> findByDeparturesAndArrivalsAndDateDepartureAfter(List<SpacePort> departures, List<SpacePort> arrivals, LocalDateTime fromDate);
    List<Flight> findByStatusIn(List<FlightStatus> statuses);
    List<Flight> findAll();
    Flight save(Flight Flight);
    void deleteById(Long id);
}
