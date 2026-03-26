package fr.thomascecil.heliodysse.domain.model.entity;

import fr.thomascecil.heliodysse.domain.model.enums.flightEnum.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
    private Long idFlight;
    private String numFlight;
    private LocalDateTime dateArrival;
    private LocalDateTime dateDeparture;
    private Short seats;
    private Short seatsAvailable;
    private FlightStatus status;
    private BigDecimal price;
    private LocalDateTime dateCreation;
    private String createdBy;
    private LocalDateTime lastModificationDate;
    private String lastModificationBy;
    private Short version;

    private Integer idArrival;
    private Integer idDeparture;
}
