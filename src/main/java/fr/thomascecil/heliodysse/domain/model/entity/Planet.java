package fr.thomascecil.heliodysse.domain.model.entity;

import fr.thomascecil.heliodysse.domain.model.enums.planetEnum.PlanetName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Planet {
    private Short idPlanet;
    private PlanetName planetName;
    private LocalDateTime dateCreation;
    private String createdBy;
    private LocalDateTime lastModificationDate;
    private String lastModificationBy;
    private Short version;
}