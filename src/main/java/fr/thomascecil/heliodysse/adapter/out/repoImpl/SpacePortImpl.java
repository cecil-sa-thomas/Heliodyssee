package fr.thomascecil.heliodysse.adapter.out.repoImpl;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.FlightEntity;
import fr.thomascecil.heliodysse.adapter.out.jpaEntity.PlanetEntity;
import fr.thomascecil.heliodysse.adapter.out.jpaEntity.SpacePortEntity;
import fr.thomascecil.heliodysse.adapter.out.mapper.PlanetMapper;
import fr.thomascecil.heliodysse.adapter.out.mapper.SpacePortMapper;
import fr.thomascecil.heliodysse.adapter.out.repository.JpaPlanetRepository;
import fr.thomascecil.heliodysse.adapter.out.repository.JpaSpacePortRepository;
import fr.thomascecil.heliodysse.domain.model.entity.Planet;
import fr.thomascecil.heliodysse.domain.model.entity.SpacePort;
import fr.thomascecil.heliodysse.domain.port.out.SpacePortRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SpacePortImpl implements SpacePortRepository {

    private final JpaSpacePortRepository jpaSpacePortRepository;
    private final JpaPlanetRepository jpaPlanetRepository;
    private final SpacePortMapper mapper;
    private final PlanetMapper planetMapper;

    public SpacePortImpl(JpaSpacePortRepository jpaSpacePortRepository, JpaPlanetRepository jpaPlanetRepository, SpacePortMapper mapper, PlanetMapper planetMapper) {
        this.jpaSpacePortRepository = jpaSpacePortRepository;
        this.jpaPlanetRepository = jpaPlanetRepository;
        this.mapper = mapper;
        this.planetMapper = planetMapper;
    }

    @Override
    public Optional<SpacePort> findById(Integer id) {
        return jpaSpacePortRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<SpacePort> findAll() {
        List<SpacePortEntity> entities = jpaSpacePortRepository.findAll();
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SpacePort> findByPlanet(Planet planet) {

        PlanetEntity planetEntity = planetMapper.toEntity(planet);
        List<SpacePortEntity> spacePortEntities = jpaSpacePortRepository.findByPlanet(planetEntity);

        // Mapping des entités vers le modèle métier
        return spacePortEntities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());

    }

    @Override
    public SpacePort save(SpacePort spacePort) {
        SpacePortEntity entity = mapper.toEntity(spacePort);

        PlanetEntity planetEntity = jpaPlanetRepository.findById(spacePort.getIdPlanet())
                        .orElseThrow(() -> new EntityNotFoundException("Planet not found"));
        entity.setPlanet(planetEntity);

        SpacePortEntity saved = jpaSpacePortRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Integer id) {
        jpaSpacePortRepository.deleteById(id);
    }
}
