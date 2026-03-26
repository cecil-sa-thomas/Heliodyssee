package fr.thomascecil.heliodysse.domain.model.entity;

import fr.thomascecil.heliodysse.domain.model.enums.bookingEnum.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

    private String id;// ID MongoDB
    private Long userId;
    private String invoiceNumber = UUID.randomUUID().toString(); // UUID unique à chaque facture

    // Infos passager
    private String firstNamePassenger;
    private String lastNamePassenger;
    private Short passengerAge;
    private String numberPassenger;
    private Boolean gender;

    // Infos utilisateur acheteur
    private String userEmail;

    // Vol
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String departurePort;
    private String departurePlanet;
    private String arrivalPort;
    private String arrivalPlanet;

    // Paiement
    private BigDecimal price;
    private String cardBrand;
    private String cardLastDigits;
    private Integer cardExpMonth;
    private Integer cardExpYear;

    // Meta
    private LocalDateTime bookingDate;
    private LocalDateTime invoiceDate;
    private BookingStatus bookingStatus;
    private String currency;
    private String termsAndConditionsVersion;
}
