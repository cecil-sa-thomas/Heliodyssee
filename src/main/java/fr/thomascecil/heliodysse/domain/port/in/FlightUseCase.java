package fr.thomascecil.heliodysse.domain.port.in;

import fr.thomascecil.heliodysse.domain.model.entity.Flight;
import fr.thomascecil.heliodysse.domain.model.enums.flightEnum.FlightStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightUseCase {
    Optional<Flight> getFlightById(Long id);
    List<Flight> getAll();
    List<Flight> getByDepartureAndArrivalAndDateDepartureAfter(Short departureId, Short arrivalId, LocalDateTime fromDate );
    List<Flight> getFlightByStatus(List<FlightStatus> statuses );
    Flight createFlight(Flight flight);
    Flight updateFlight(Flight flight);
    void deleteById(Long id);
}
