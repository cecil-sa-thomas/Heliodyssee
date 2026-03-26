package fr.thomascecil.heliodysse.adapter.out.jpaEntity;

import fr.thomascecil.heliodysse.domain.model.enums.planetEnum.PlanetName;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "planet")
public class PlanetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_planet")
    private Short idPlanet;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, length = 50 )
    private PlanetName planetName;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    @Column(name = "last_modification_date")
    private LocalDateTime lastModificationDate;

    @Column(name = "last_modification_by", length = 50)
    private String lastModificationBy;

    @Version
    @Column(name = "version", nullable = false)
    private Short version;
}