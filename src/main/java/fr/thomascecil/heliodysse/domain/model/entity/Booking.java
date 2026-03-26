package fr.thomascecil.heliodysse.domain.model.entity;

import fr.thomascecil.heliodysse.domain.model.enums.bookingEnum.BookingStatus;
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
public class Booking {

    private Long idBooking;
    private Short seatNumber;
    private BigDecimal price;
    private String firstNamePassenger;
    private String lastNamePassenger;
    private String numberPassenger;
    private Short passengerAge;
    private Boolean gender;
    private BookingStatus status;
    private LocalDateTime dateCreation;
    private String createdBy;
    private LocalDateTime lastModificationDate;
    private String lastModificationBy;
    private Short version;

    private Long idFlight;
    private Long idUser;

}
