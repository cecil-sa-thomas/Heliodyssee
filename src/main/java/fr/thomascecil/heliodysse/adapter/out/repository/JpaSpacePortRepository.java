package fr.thomascecil.heliodysse.adapter.out.repository;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.PlanetEntity;
import fr.thomascecil.heliodysse.adapter.out.jpaEntity.SpacePortEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSpacePortRepository extends JpaRepository<SpacePortEntity, Integer> {
    public List <SpacePortEntity> findByPlanet(PlanetEntity planet);
}
