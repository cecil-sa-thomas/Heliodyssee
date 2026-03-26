package fr.thomascecil.heliodysse.adapter.in.dto.request.flight;

import fr.thomascecil.heliodysse.domain.model.enums.flightEnum.FlightStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FlightCreateDTO {
    @NotNull(message = "arrival date can't be null")
    private LocalDateTime dateArrival;

    @NotNull(message = "departure date can't be null")
    private LocalDateTime dateDeparture;

    @NotNull(message = "seats can't be null")
    private Short seats;

    @NotNull(message = "status can't be null")
    private FlightStatus status;

    @NotNull(message = "price can't be null")
    private BigDecimal price;

    @NotNull(message = "arrival SpacePort can't be null")
    private Integer idArrival;

    @NotNull(message = "departure SpacePort can't be null")
    private Integer idDeparture;
}
