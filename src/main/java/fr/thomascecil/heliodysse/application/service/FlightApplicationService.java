package fr.thomascecil.heliodysse.application.service;

import com.stripe.model.Plan;
import fr.thomascecil.heliodysse.domain.model.entity.Flight;
import fr.thomascecil.heliodysse.domain.model.entity.Planet;
import fr.thomascecil.heliodysse.domain.model.entity.SpacePort;
import fr.thomascecil.heliodysse.domain.model.enums.flightEnum.FlightStatus;
import fr.thomascecil.heliodysse.domain.port.in.FlightUseCase;
import fr.thomascecil.heliodysse.domain.port.out.FlightRepository;
import fr.thomascecil.heliodysse.domain.port.out.PlanetRepository;
import fr.thomascecil.heliodysse.domain.port.out.SpacePortRepository;
import fr.thomascecil.heliodysse.domain.service.FlightService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightApplicationService implements FlightUseCase {
    private final FlightRepository flightRepository;
    private final FlightService flightService;
    private final SpacePortRepository spacePortRepository;
    private final PlanetRepository planetRepository;

    public FlightApplicationService(FlightRepository flightRepository, SpacePortRepository spacePortRepository, PlanetRepository planetRepository) {
        this.flightRepository = flightRepository;
        this.planetRepository = planetRepository;
        this.flightService = new FlightService();
        this.spacePortRepository = spacePortRepository;
    }

    @Override
    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    @Override
    public List<Flight> getAll() {
        return flightRepository.findAll();
    }

    @Override
    public List<Flight> getByDepartureAndArrivalAndDateDepartureAfter(Short departurePlanetId, Short arrivalPlanetId, LocalDateTime fromDate) {
        // 1. Récupérer les planètes
        Planet departurePlanet = planetRepository.findById(departurePlanetId)
                .orElseThrow(() -> new EntityNotFoundException("Planète départ non trouvée : " + departurePlanetId));
        Planet arrivalPlanet = planetRepository.findById(arrivalPlanetId)
                .orElseThrow(() -> new EntityNotFoundException("Planète arrivée non trouvée : " + arrivalPlanetId));

        // 2. Récupérer les spaceports associés à chaque planète
        List<SpacePort> departurePorts = spacePortRepository.findByPlanet(departurePlanet);
        List<SpacePort> arrivalPorts = spacePortRepository.findByPlanet(arrivalPlanet);

        // 3. Récupérer les vols correspondants
        return flightRepository.findByDeparturesAndArrivalsAndDateDepartureAfter(departurePorts, arrivalPorts, fromDate);
    }


    @Override
    public List<Flight> getFlightByStatus(List<FlightStatus> statuses) {
        return flightRepository.findByStatusIn(statuses);
    }

    @Override
    public Flight createFlight(Flight flight) {
        // Vérifier que les SpacePort de départ et d’arrivée existent
        SpacePort departure = spacePortRepository.findById(flight.getIdDeparture())
                .orElseThrow(() -> new EntityNotFoundException("Departure SpacePort not found"));
        SpacePort arrival = spacePortRepository.findById(flight.getIdArrival())
                .orElseThrow(() -> new EntityNotFoundException("Arrival SpacePort not found"));
        if (flight.getSeats() <= 0) {
            throw new IllegalArgumentException("Un flight doit contenir au moins une place.");
        }
        flight.setSeatsAvailable(flight.getSeats());
        flight.setNumFlight(flightService.setNumberFlight(flight));
        flightService.validateFlight(flight);
        return flightRepository.save(flight);
    }

    @Override
    public Flight updateFlight(Flight flight) {
        Flight found = flightRepository.findById(flight.getIdFlight())
                .orElseThrow(() -> new EntityNotFoundException("flight introuvable"));
        flightService.updateFlightFromFlight(flight, found);
        flightService.updateInfoSystem(found);
        flightService.validateFlight(found);
        return flightRepository.save(found);
    }

    @Override
    public void deleteById(Long id) {
        if (!flightRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("No booking found with id : " + id);
        }
        flightRepository.deleteById(id);
    }

    /*
        méthode "spéciale" permettant de mettre à jours autimaiquement
        les status des flight suivant des conditions implémenter dans flightService
     */
    @Scheduled(fixedRate = 60000)
    public void updateFlightStatuses() {//Gestion des status délégué à une appli tiers dans le futur(opérateur tour de control par exemple)
        List<Flight> flights = flightRepository.findByStatusIn(List.of(
                FlightStatus.SCHEDULED,
                FlightStatus.BOARDING,
                FlightStatus.IN_FLIGHT,
                FlightStatus.LANDED,
                FlightStatus.CLOSED

        ));

        for (Flight flight : flights) {
            flightService.updateStatusIfNeeded(flight);
            flightRepository.save(flight);
        }
    }
}
