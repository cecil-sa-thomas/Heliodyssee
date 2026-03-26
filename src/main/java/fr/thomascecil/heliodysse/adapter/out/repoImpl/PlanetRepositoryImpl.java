package fr.thomascecil.heliodysse.adapter.out.repoImpl;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.PlanetEntity;
import fr.thomascecil.heliodysse.adapter.out.mapper.PlanetMapper;
import fr.thomascecil.heliodysse.adapter.out.repository.JpaPlanetRepository;
import fr.thomascecil.heliodysse.domain.model.entity.Planet;
import fr.thomascecil.heliodysse.domain.port.out.PlanetRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PlanetRepositoryImpl  implements PlanetRepository {

    private final JpaPlanetRepository jpaPlanetsRepository;
    private final PlanetMapper mapper;

    public PlanetRepositoryImpl(JpaPlanetRepository jpaPlanetsRepository, PlanetMapper mapper) {
        this.jpaPlanetsRepository = jpaPlanetsRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Planet> findById(Short id) {
        return jpaPlanetsRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Planet> findAll() {
        List<PlanetEntity> entities = jpaPlanetsRepository.findAll();
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Planet save(Planet planet) {
        PlanetEntity entity = mapper.toEntity(planet);
        PlanetEntity saved = jpaPlanetsRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Short id) {
        jpaPlanetsRepository.deleteById(id);
    }
}
