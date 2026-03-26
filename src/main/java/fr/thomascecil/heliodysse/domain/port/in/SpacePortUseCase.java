package fr.thomascecil.heliodysse.domain.port.in;


import fr.thomascecil.heliodysse.domain.model.entity.SpacePort;

import java.util.List;
import java.util.Optional;

public interface SpacePortUseCase {
    Optional<SpacePort> getSpacePortById(Integer id);
    List<SpacePort> getAll();
    List<SpacePort> getByPlanets(Short id);
    SpacePort createSpacePort(SpacePort spacePort);
    SpacePort updateSpacePort(SpacePort spacePort);
    void deleteById(Integer id);
}
