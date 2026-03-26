package fr.thomascecil.heliodysse.adapter.in.dto.response.booking;


import fr.thomascecil.heliodysse.domain.model.enums.bookingEnum.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingResponseDTO {
    private Long idBooking;
    private short seatNumber;
    private BigDecimal price;
    private String firstNamePassenger;
    private String lastNamePassenger;
    private short passengerAge;
    private boolean gender;
    private BookingStatus status;

}
