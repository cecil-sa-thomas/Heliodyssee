package fr.thomascecil.heliodysse.domain.service;

import fr.thomascecil.heliodysse.domain.model.entity.Flight;
import fr.thomascecil.heliodysse.domain.model.enums.flightEnum.FlightStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class FlightService {
    public void validateFlight(Flight flight) {
        checkRequiredFields(flight);
        checkValidateDate(flight);
        initalizedDefaultsSelf(flight);
        validateStatus(flight);

    }

    public void checkRequiredFields(Flight flight) {
        if (flight.getSeats() == null) {
            throw new IllegalArgumentException("Number of seats is mandatory.");
        }
        if (flight.getSeatsAvailable() == null) {
            throw new IllegalArgumentException("Available seats are mandatory.");
        }
        if (flight.getSeats() < flight.getSeatsAvailable()) {
            throw new IllegalArgumentException("Available seats cannot exceed total seats.");
        }
        if (flight.getPrice() == null) {
            throw new IllegalArgumentException("Price is mandatory.");
        }
        if (flight.getNumFlight() == null || flight.getNumFlight().isBlank()) {
            throw new IllegalArgumentException("Flight number is mandatory.");
        }
        if (flight.getDateDeparture() == null) {
            throw new IllegalArgumentException("Departure date is mandatory.");
        }
        if (flight.getDateArrival() == null) {
            throw new IllegalArgumentException("Arrival date is mandatory.");
        }
        if (flight.getIdDeparture() == null) {
            throw new IllegalArgumentException("Departure location ID is mandatory.");
        }
        if (flight.getIdArrival() == null) {
            throw new IllegalArgumentException("Arrival location ID is mandatory.");
        }
    }


    public void initalizedDefaultsSelf(Flight flight) {

        if (flight.getStatus() == null) {
            flight.setStatus(FlightStatus.SCHEDULED);
        }
        if (flight.getCreatedBy() == null) {
            flight.setCreatedBy("placeHolder");
        }
        if (flight.getVersion() == null) {
            flight.setVersion((short) 0);
        }
        if (flight.getDateCreation() == null) {
            flight.setDateCreation(LocalDateTime.now());
        }
    }

    public void validateStatus(Flight flight) {
        if (flight.getStatus() == null) {
            throw new IllegalArgumentException("Status not found");
        }
        boolean isValid = switch (flight.getStatus()) {
            case SCHEDULED, DELAYED, BOARDING, IN_FLIGHT, LANDED, CANCELLED, FULL, CLOSED -> true;
            default -> false;

        };
        if (!isValid) {
            throw new IllegalArgumentException("Status non authorized : " + flight.getStatus());
        }
    }

    //update le status d'un flight pour une mise à jour dynamique
    public void updateStatusIfNeeded(Flight flight) {
        LocalDateTime now = LocalDateTime.now();

        // Si le vol est prévu (SCHEDULED) et qu'on est à moins de 30 minutes du départ → passer à BOARDING
        if (flight.getStatus() == FlightStatus.SCHEDULED &&
                now.isAfter(flight.getDateDeparture().minusMinutes(30))) {
            flight.setStatus(FlightStatus.BOARDING);

            // Sinon, si le vol est en BOARDING et que l'heure de départ est atteinte → passer à IN_FLIGHT
        } else if (flight.getStatus() == FlightStatus.BOARDING &&
                now.isAfter(flight.getDateDeparture())) {
            flight.setStatus(FlightStatus.IN_FLIGHT);

            // Sinon, si le vol est en vol (IN_FLIGHT) et que l'heure d'arrivée est dépassée → passer à LANDED
        } else if (flight.getStatus() == FlightStatus.IN_FLIGHT &&
                now.isAfter(flight.getDateArrival())) {
            flight.setStatus(FlightStatus.LANDED);

            // Sinon, si le vol est atterri (LANDED) et que 15 min sont passées → passer à CLOSED
        } else if (flight.getStatus() == FlightStatus.LANDED &&
                now.isAfter(flight.getDateArrival().plusMinutes(15))) {
            flight.setStatus(FlightStatus.CLOSED);
        }

    }

    // Empêche des date illogique
    public void checkValidateDate(Flight flight) {
        if (flight.getDateDeparture().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date de départ doit être dans le futur.");
        }
        if (flight.getDateArrival().isBefore(flight.getDateDeparture())) {
            throw new IllegalArgumentException("La date d’arrivée doit être postérieure à la date de départ.");
        }
        LocalDateTime departure = flight.getDateDeparture();
        LocalDateTime arrival = flight.getDateArrival();

        Duration duration = Duration.between(departure, arrival);

        if (duration.toMinutes() < 60) {
            throw new IllegalArgumentException("La durée du vol doit être d’au moins 60 minutes.");
        }
    }

    public void updateFlightFromFlight(Flight source, Flight target) {
        if (target.getStatus() == FlightStatus.CANCELLED) {
            throw new IllegalStateException("Un vol annulé ne peut pas être modifié.");
        }

        // Copie uniquement les champs autorisés et non-nuls
        if (source.getNumFlight() != null) target.setNumFlight(source.getNumFlight());
        if (source.getDateArrival() != null) target.setDateArrival(source.getDateArrival());
        if (source.getDateDeparture() != null) target.setDateDeparture(source.getDateDeparture());
        if (source.getStatus() != null) target.setStatus(source.getStatus());
        if (source.getPrice() != null) target.setPrice(source.getPrice());
        if (source.getLastModificationDate() != null) target.setLastModificationDate(source.getLastModificationDate());
        if (source.getLastModificationBy() != null) target.setLastModificationBy(source.getLastModificationBy());
        if (source.getIdArrival() != null) target.setIdArrival(source.getIdArrival());
        if (source.getIdDeparture() != null) target.setIdDeparture(source.getIdDeparture());

        // Champs volontairement ignorés :
        // - idFlight
        // - seats
        // - seatsAvailable
        // - dateCreation
        // - createdBy
        // - version
    }

    //Appeller dans BookingServiceApplication
    public void decrementSeatsAvailable(Flight flight) {
        // Vérifie si des sièges sont encore disponibles
        if (flight.getSeatsAvailable() <= 0) {
            throw new IllegalStateException("Aucune place disponible pour ce vol.");
        }

        // Vérifie que le statut du vol autorise la réservation
        if (flight.getStatus() != FlightStatus.SCHEDULED) {
            throw new IllegalStateException("Le vol n'est pas ouvert à la réservation (statut : " + flight.getStatus() + ").");
        }

        // Décrémente le nombre de sièges disponibles
        flight.setSeatsAvailable((short) (flight.getSeatsAvailable() - 1));

    }
    public void updateInfoSystem(Flight flight){
        flight.setLastModificationDate(LocalDateTime.now());
        flight.setLastModificationBy("system");
    }

    public String setNumberFlight(Flight flight){
        String numFlight = "N" + System.currentTimeMillis() + "-D" + flight.getIdDeparture()  + "-A" + flight.getIdArrival();
        return numFlight;
    }
}