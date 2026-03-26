package fr.thomascecil.heliodysse.adapter.in.dto.request.flight;

import fr.thomascecil.heliodysse.domain.model.enums.flightEnum.FlightStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FlightUpdateDTO {

    private String numFlight;
    private LocalDateTime dateArrival;
    private LocalDateTime dateDeparture;
    private FlightStatus status;
    private BigDecimal price;

    private Integer idArrival;
    private Integer idDeparture;
}
