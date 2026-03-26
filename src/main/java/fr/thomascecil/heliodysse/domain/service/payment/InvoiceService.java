package fr.thomascecil.heliodysse.domain.service.payment;

import fr.thomascecil.heliodysse.domain.model.entity.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class InvoiceService {

    public static Invoice generateFromBooking(
            Booking booking,
            Flight flight,
            User user,
            PaymentCard card,
            SpacePort departurePort,
            SpacePort arrivalPort,
            Planet departurePlanet,
            Planet arrivalPlanet
    ) {
        return Invoice.builder()
                .userId(user.getIdUser())
                .invoiceNumber(UUID.randomUUID().toString())
        // Infos passager
                .firstNamePassenger(booking.getFirstNamePassenger())
                .lastNamePassenger(booking.getLastNamePassenger())
                .passengerAge(booking.getPassengerAge())
                .numberPassenger(booking.getNumberPassenger())
                .gender(booking.getGender())

                // Infos utilisateur acheteur
                .userEmail(user.getEmail())

                // Infos vol
                .flightNumber(flight.getNumFlight())
                .departureTime(flight.getDateDeparture())
                .arrivalTime(flight.getDateArrival())
                .departurePort(departurePort.getName())
                .departurePlanet(departurePlanet.getPlanetName().name())
                .arrivalPort(arrivalPort.getName())
                .arrivalPlanet(arrivalPlanet.getPlanetName().name())

                // Paiement
                .price(booking.getPrice())
                .cardBrand(card.getBrand())
                .cardLastDigits(card.getLastDigit())
                .cardExpMonth(card.getExpMonth())
                .cardExpYear(card.getExpYear())

                // Meta
                .bookingDate(booking.getDateCreation())
                .invoiceDate(LocalDateTime.now())
                .bookingStatus(booking.getStatus())
                .currency("EUR")
                .termsAndConditionsVersion("v1.0")
                .build();

    }
}
