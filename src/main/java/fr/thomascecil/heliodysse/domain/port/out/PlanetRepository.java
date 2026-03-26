package fr.thomascecil.heliodysse.domain.port.out;

import fr.thomascecil.heliodysse.domain.model.entity.Planet;

import java.util.List;
import java.util.Optional;

public interface PlanetRepository {
    Optional<Planet> findById(Short id);
    List<Planet> findAll();
    Planet save(Planet planet);
    void deleteById(Short id);
}
