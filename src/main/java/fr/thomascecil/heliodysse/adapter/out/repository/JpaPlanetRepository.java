package fr.thomascecil.heliodysse.adapter.out.repository;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.PlanetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPlanetRepository extends JpaRepository<PlanetEntity, Short> {
}
