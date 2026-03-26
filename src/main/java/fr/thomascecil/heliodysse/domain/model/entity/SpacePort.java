package fr.thomascecil.heliodysse.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpacePort {
    private Integer idSpacePort;
    private String name;
    private LocalDateTime dateCreation;
    private String createdBy;
    private LocalDateTime lastModificationDate;
    private String lastModificationBy;
    private Short version;
    private Short idPlanet;
}
