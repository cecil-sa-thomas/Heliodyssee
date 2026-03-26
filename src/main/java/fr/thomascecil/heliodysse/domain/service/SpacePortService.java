package fr.thomascecil.heliodysse.domain.service;

import fr.thomascecil.heliodysse.domain.model.entity.SpacePort;
import java.time.LocalDateTime;

public class SpacePortService {
    public void validateSpacePort(SpacePort spacePort) {
        checkRequiredFields(spacePort);
        initalizedDefaultsSelf(spacePort);
    }

    public void checkRequiredFields(SpacePort spacePort) {
        if (spacePort.getName() == null || spacePort.getName().isBlank()) {
            throw new IllegalArgumentException("First name mandatory.");
        }
    }

    public void initalizedDefaultsSelf(SpacePort spacePort){
        if (spacePort.getCreatedBy() == null) {
            spacePort.setCreatedBy("placeHolder");
        }
        if (spacePort.getVersion() == null) {
            spacePort.setVersion((short) 0);
        }
        if (spacePort.getDateCreation() == null) {
            spacePort.setDateCreation(LocalDateTime.now());
        }
    }

    public void updateInfoSystem(SpacePort spacePort){
        spacePort.setLastModificationDate(LocalDateTime.now());
        spacePort.setLastModificationBy("system");
    }

}
