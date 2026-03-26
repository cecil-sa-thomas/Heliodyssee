package fr.thomascecil.heliodysse.domain.service;

import fr.thomascecil.heliodysse.domain.model.entity.Planet;

import java.time.LocalDateTime;

public class PlanetService {
    public void validatePlanet(Planet planet) {
        initalizedDefaultsSelf(planet);
    }


    public void initalizedDefaultsSelf(Planet planet){
        if (planet.getCreatedBy() == null) {
            planet.setCreatedBy("placeHolder");
        }
        if (planet.getVersion() == null) {
            planet.setVersion((short) 0);
        }
        if (planet.getDateCreation() == null) {
            planet.setDateCreation(LocalDateTime.now());
        }
    }

    public void updateInfoSystem(Planet planet){
        planet.setLastModificationDate(LocalDateTime.now());
        planet.setLastModificationBy("system");
    }
}
