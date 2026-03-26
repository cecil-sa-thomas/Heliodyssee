package fr.thomascecil.heliodysse.adapter.out.repoImpl;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.*;
import fr.thomascecil.heliodysse.adapter.out.mapper.FlightMapper;
import fr.thomascecil.heliodysse.adapter.out.repository.JpaFlightRepository;
import fr.thomascecil.heliodysse.adapter.out.repository.JpaPlanetRepository;
import fr.thomascecil.heliodysse.adapter.out.repository.JpaSpacePortRepository;
import fr.thomascecil.heliodysse.domain.model.entity.Flight;
import fr.thomascecil.heliodysse.domain.model.entity.SpacePort;
import fr.thomascecil.heliodysse.domain.model.enums.flightEnum.FlightStatus;
import fr.thomascecil.heliodysse.domain.port.out.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FlightRepositoryImpl implements FlightRepository {

    private final JpaFlightRepository jpaFlightRepository;
    private final JpaSpacePortRepository jpaSpacePortRepository;
    private final FlightMapper mapper;

    public FlightRepositoryImpl(JpaFlightRepository jpaFlightRepository, FlightMapper mapper, JpaPlanetRepository jpaPlanetRepository, JpaSpacePortRepository jpaSpacePortRepository) {
        this.jpaFlightRepository = jpaFlightRepository;
        this.mapper = mapper;
        this.jpaSpacePortRepository = jpaSpacePortRepository;
    }

    @Override
    public Optional<Flight> findById(Long id) {
        return jpaFlightRepository.findById(id).map(mapper::toDomain);
    }


    @Override
    public List<Flight> findByDeparturesAndArrivalsAndDateDepartureAfter(
            List<SpacePort> departures,
            List<SpacePort> arrivals,
            LocalDateTime fromDate
    ) {
        List<SpacePortEntity> departureEntities = departures.stream()
                .map(sp -> jpaSpacePortRepository.findById(sp.getIdSpacePort())
                        .orElseThrow(() -> new EntityNotFoundException("Departure not found: " + sp.getIdSpacePort())))
                .collect(Collectors.toList());

        List<SpacePortEntity> arrivalEntities = arrivals.stream()
                .map(sp -> jpaSpacePortRepository.findById(sp.getIdSpacePort())
                        .orElseThrow(() -> new EntityNotFoundException("Arrival not found: " + sp.getIdSpacePort())))
                .collect(Collectors.toList());

        System.out.println("📦 Recherche des vols entre " + departureEntities.size() + " départs et " + arrivalEntities.size() + " arrivées après " + fromDate);

        List<FlightEntity> entities = jpaFlightRepository.findByDepartureInAndArrivalInAndDateDepartureAfter(
                departureEntities, arrivalEntities, fromDate
        );

        System.out.println("📥 Vols récupérés : " + entities.size());

        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }



    @Override
    public List<Flight> findByStatusIn(List<FlightStatus> statuses) {
        return jpaFlightRepository.findByStatusIn(statuses).stream()
                .map(mapper::toDomain) // ← mapping ici
                .collect(Collectors.toList());
    }

    @Override
    public List<Flight> findAll() {
        List<FlightEntity> entities = jpaFlightRepository.findAll();
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Flight save(Flight flight) {
        FlightEntity entity = mapper.toEntity(flight);

        entity.setDeparture(jpaSpacePortRepository.getReferenceById(flight.getIdDeparture()));
        entity.setArrival(jpaSpacePortRepository.getReferenceById(flight.getIdArrival()));

        FlightEntity savedEntity = jpaFlightRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        jpaFlightRepository.deleteById(id);
    }
}
