package fr.thomascecil.heliodysse.application.service;

import fr.thomascecil.heliodysse.domain.model.entity.Planet;
import fr.thomascecil.heliodysse.domain.port.in.PlanetUseCase;
import fr.thomascecil.heliodysse.domain.port.out.PlanetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanetApplicationService implements PlanetUseCase {

    private final PlanetRepository planetRepository;

    public PlanetApplicationService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @Override
    public Optional<Planet> getPlanetById(Short id) {
        return planetRepository.findById(id);
    }

    @Override
    public List<Planet> getAll() {
        return planetRepository.findAll();
    }

    @Override
    public Planet createPlanet(Planet planet) {
        return planetRepository.save(planet);
    }

    // Route à supprimer, les planet sont fixe
    @Override
    public Planet updatePlanet(Planet planet) {
        return planetRepository.save(planet);
    }

    @Override
    public void deleteById(Short id) {
        if(!planetRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("No planet found with id : " + id);
        }
        planetRepository.deleteById(id);
    }
}
