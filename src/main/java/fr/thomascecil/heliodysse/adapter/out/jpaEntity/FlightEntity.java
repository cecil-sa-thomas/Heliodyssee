package fr.thomascecil.heliodysse.adapter.out.jpaEntity;

import fr.thomascecil.heliodysse.domain.model.enums.flightEnum.FlightStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "flight")
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_flight")
    private Long idFlight;

    @Column(name = "num_flight", nullable = false, unique = true)
    private String numFlight;

    @Column(name = "date_departure", nullable = false)
    private LocalDateTime dateDeparture;

    @Column(name = "date_arrival", nullable = false)
    private LocalDateTime dateArrival;

    @Column(nullable = false)
    private Short seats;

    @Column(name = "seats_available", nullable = false)
    private Short seatsAvailable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlightStatus status;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "last_modification_date")
    private LocalDateTime lastModificationDate;

    @Column(name = "last_modification_by")
    private String lastModificationBy;

    @Version
    @Column(nullable = false)
    private Short version;

    @ManyToOne
    @JoinColumn(name = "departure_spaceport_id", nullable = false)
    private SpacePortEntity departure;

    @ManyToOne
    @JoinColumn(name = "arrival_spaceport_id", nullable = false)
    private SpacePortEntity arrival;
}