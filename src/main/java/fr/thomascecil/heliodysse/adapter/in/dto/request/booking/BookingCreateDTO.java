package fr.thomascecil.heliodysse.adapter.in.dto.request.booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class BookingCreateDTO {
    @NotNull(message = "flightId can't be null")
    private Long flightId;

    @NotNull(message = "userId can't be null")
    private Long userId;

    @NotBlank(message = "First name can't be blank")
    private String firstNamePassenger;

    @NotBlank(message = "Last name can't be blank")
    private String lastNamePassenger;

    @NotNull(message = "Passenger age can't be null")
    private Short passengerAge;

    @NotNull(message = "Gender can't be null")
    private Boolean gender;

    @NotNull(message = "Stripe id can't be null")
    private String stripeId;

}
