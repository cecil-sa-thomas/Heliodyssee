package fr.thomascecil.heliodysse.adapter.in.dto.response.flight;

import fr.thomascecil.heliodysse.domain.model.enums.flightEnum.FlightStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FlightResponseDTO {

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

    private Integer idArrival;
    private Integer idDeparture;
}
