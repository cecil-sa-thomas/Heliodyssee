package fr.thomascecil.heliodysse.adapter.out.jpaEntity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "spaceport")
public class SpacePortEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_spaceport")
    private Integer idSpacePort;

    @Column(name = "name", length = 50)
    private String name;

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
    private Short version ;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_planet", nullable = false)
    private PlanetEntity planet;
}
