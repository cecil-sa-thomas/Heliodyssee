package fr.thomascecil.heliodysse.domain.port.out;


import fr.thomascecil.heliodysse.domain.model.entity.Planet;
import fr.thomascecil.heliodysse.domain.model.entity.SpacePort;

import java.util.List;
import java.util.Optional;

public interface SpacePortRepository {
    Optional<SpacePort> findById(Integer id);
    List<SpacePort> findAll();
    List<SpacePort> findByPlanet(Planet planet);
    SpacePort save(SpacePort spacePort);
    void deleteById(Integer id);
}
