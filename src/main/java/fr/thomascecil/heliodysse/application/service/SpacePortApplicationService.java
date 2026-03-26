package fr.thomascecil.heliodysse.application.service;

import fr.thomascecil.heliodysse.domain.model.entity.Planet;
import fr.thomascecil.heliodysse.domain.model.entity.SpacePort;
import fr.thomascecil.heliodysse.domain.port.in.SpacePortUseCase;
import fr.thomascecil.heliodysse.domain.port.out.PlanetRepository;
import fr.thomascecil.heliodysse.domain.port.out.SpacePortRepository;
import fr.thomascecil.heliodysse.domain.service.SpacePortService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpacePortApplicationService implements SpacePortUseCase {

    private final SpacePortRepository spacePortRepository;
    private final SpacePortService spacePortService;
    private final PlanetRepository planetRepository;

    public SpacePortApplicationService(SpacePortRepository spacePortRepository, PlanetRepository planetRepository) {
        this.spacePortRepository = spacePortRepository;
        this.planetRepository = planetRepository;
        this.spacePortService = new SpacePortService();
    }

    @Override
    public Optional<SpacePort> getSpacePortById(Integer id) {
        return spacePortRepository.findById(id);
    }

    @Override
    public List<SpacePort> getAll() {
        return spacePortRepository.findAll();
    }

    @Override
    public List<SpacePort> getByPlanets(Short id) {
        Planet planet = planetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Planète non trouvée avec l'id = " + id));
        return spacePortRepository.findByPlanet(planet);
    }

    @Override
    public SpacePort createSpacePort(SpacePort spacePort) {
        spacePortService.validateSpacePort(spacePort);
        return spacePortRepository.save(spacePort);
    }

    @Override
    public SpacePort updateSpacePort(SpacePort spacePort) {
        spacePortService.updateInfoSystem(spacePort);
        spacePortService.validateSpacePort(spacePort);
        return spacePortRepository.save(spacePort);
    }

    @Override
    public void deleteById(Integer id) {
        if(!spacePortRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("No space port found with id : " + id);
        }
        spacePortRepository.deleteById(id);
    }
}
