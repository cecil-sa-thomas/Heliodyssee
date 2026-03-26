package fr.thomascecil.heliodysse.adapter.in.controller.rest;

import fr.thomascecil.heliodysse.adapter.in.dto.mapper.FlightDtoMapper;
import fr.thomascecil.heliodysse.adapter.in.dto.request.flight.FlightCreateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.request.flight.FlightUpdateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.response.flight.FlightResponseDTO;
import fr.thomascecil.heliodysse.domain.model.entity.Flight;
import fr.thomascecil.heliodysse.domain.model.enums.flightEnum.FlightStatus;
import fr.thomascecil.heliodysse.domain.port.in.FlightUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flights")
public class FlightRestController {

    private final FlightUseCase flightUseCase;
    private final FlightDtoMapper mapper;

    public FlightRestController(FlightUseCase flightUseCase, FlightDtoMapper mapper) {
        this.flightUseCase = flightUseCase;
        this.mapper = mapper;
    }

    @GetMapping("{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        return flightUseCase.getFlightById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<FlightResponseDTO>> getAllFlights() {
        List<Flight> flights = flightUseCase.getAll();
        List<FlightResponseDTO> flightsResponseDTO = flights.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(flightsResponseDTO);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<FlightResponseDTO>> getAllFlightsByArrivalAndDestination(
            @RequestParam Short departurePlanetId,
            @RequestParam Short arrivalPlanetId
    ) {
        LocalDateTime now = LocalDateTime.now(); // ou OffsetDateTime.now().toLocalDateTime()
        List<Flight> flights = flightUseCase.getByDepartureAndArrivalAndDateDepartureAfter(
                departurePlanetId, arrivalPlanetId, now
        );

        List<FlightResponseDTO> flightsResponseDTO = flights.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(flightsResponseDTO);
    }

    @GetMapping("/status")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByStatus(@RequestParam List<FlightStatus> statuses ){
        List<Flight> flights = flightUseCase.getFlightByStatus(statuses);
        List<FlightResponseDTO> flightsResponseDTO = flights.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(flightsResponseDTO);
    }

    @PostMapping
    public ResponseEntity<FlightResponseDTO> createFlight (@RequestBody FlightCreateDTO flightCreateDTO){
        //ajouter la carte de payment dans les paramètres
        Flight flight = mapper.toDomain(flightCreateDTO);
        System.out.println("DEBUG --- flight.getIdFlight() = " + flight.getIdArrival());
        System.out.println("DEBUG --- flight.getIdFlight() = " + flight.getIdDeparture());
        System.out.println("DEBUG --- flight.getIdFlight() = " + flight.getDateArrival());
        System.out.println("DEBUG --- flight.getIdFlight() = " + flight.getDateDeparture());
        Flight createdFlight = flightUseCase.createFlight(flight);
        FlightResponseDTO responseDTO = mapper.toDto(createdFlight);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> updateFlight (@PathVariable Long id, @RequestBody FlightUpdateDTO flightUpdateDTO){
        Flight flight = mapper.toDomain(flightUpdateDTO);
        Flight updatedFlight = flightUseCase.updateFlight(flight);
        FlightResponseDTO responseDTO = mapper.toDto(updatedFlight);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlights(@PathVariable Long id) {
        flightUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
