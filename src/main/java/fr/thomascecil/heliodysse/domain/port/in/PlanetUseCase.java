package fr.thomascecil.heliodysse.domain.port.in;

import fr.thomascecil.heliodysse.domain.model.entity.Planet;

import java.util.List;
import java.util.Optional;

public interface PlanetUseCase {
    Optional<Planet> getPlanetById(Short id);
    List<Planet> getAll();
    Planet createPlanet(Planet planet);
    Planet updatePlanet(Planet planet);
    void deleteById(Short id);
}
