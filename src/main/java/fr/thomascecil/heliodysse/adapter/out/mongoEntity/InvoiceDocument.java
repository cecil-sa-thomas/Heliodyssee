package fr.thomascecil.heliodysse.adapter.out.mongoEntity;

import fr.thomascecil.heliodysse.domain.model.enums.bookingEnum.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("invoices") // <- Nom de la collection MongoDB
public class InvoiceDocument {

    @Id
    private String id;// ID MongoDB

    private Long userId;
    private String invoiceNumber;

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

