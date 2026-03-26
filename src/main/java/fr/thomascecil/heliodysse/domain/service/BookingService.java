package fr.thomascecil.heliodysse.domain.service;

import fr.thomascecil.heliodysse.domain.model.entity.*;
import fr.thomascecil.heliodysse.domain.model.enums.bookingEnum.BookingStatus;
import fr.thomascecil.heliodysse.domain.model.enums.flightEnum.FlightStatus;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class BookingService {
    public void validateBooking(Booking booking) {
        checkRequiredFields(booking);
        isPassengerAdult(booking);
        initalizedDefaultsSelf(booking);
        validateStatus(booking);
    }

    public void checkRequiredFields(Booking booking) {
        if (booking.getSeatNumber() == null){
            throw new IllegalArgumentException("Seat number mandatory.");
        }
        if (booking.getPrice() == null){
            throw new IllegalArgumentException("Price mandatory.");
        }
        if (booking.getFirstNamePassenger() == null || booking.getFirstNamePassenger().isBlank()) {
            throw new IllegalArgumentException("First name mandatory.");
        }
        if (booking.getLastNamePassenger() == null || booking.getLastNamePassenger().isBlank()) {
            throw new IllegalArgumentException("Last name mandatory.");
        }
        if (booking.getNumberPassenger() == null || booking.getNumberPassenger().isBlank()) {
            throw new IllegalArgumentException("Number passenger mandatory.");
        }
        if (booking.getPassengerAge() == null) {
            throw new IllegalArgumentException("Age mandatory.");
        }
        if (booking.getGender() == null) {
            throw new IllegalArgumentException("gender mandatory.");
        }
        if (booking.getIdUser() == null) {
            throw new IllegalArgumentException(("user mandatory"));
        }
        if (booking.getIdFlight() == null) {
            throw new IllegalArgumentException(("flight mandatory"));
        }
    }
    public void initalizedDefaultsSelf(Booking booking){

        if (booking.getStatus()== null){
            booking.setStatus(BookingStatus.CONFIRMED);
        }
        if (booking.getCreatedBy() == null) {
            booking.setCreatedBy("placeHolder");
        }
        if (booking.getVersion() == null) {
            booking.setVersion((short) 0);
        }
        if (booking.getDateCreation() == null) {
            booking.setDateCreation(LocalDateTime.now());
        }
    }

    public void isPassengerAdult(Booking booking) {
        if (booking.getPassengerAge() == null) {
            throw new IllegalArgumentException("Passenger Age mandatory.");
        }
        int age = booking.getPassengerAge();
        if (age < 18) {
            throw new IllegalArgumentException("Passenger Age must be at least 18 years old.");
        }
    }
    public void validateStatus(Booking booking){
        if(booking.getStatus() == null){
            throw new IllegalArgumentException("Status not found");
        }
        boolean isValid = switch (booking.getStatus()){
            case CONFIRMED, CANCELLED -> true;
            default -> false;
        };
        if(!isValid){
            throw new IllegalArgumentException("Status non authorized : " + booking.getStatus());
        }
    }

    public void updateInfoSystem(Booking booking){
        booking.setLastModificationDate(LocalDateTime.now());
        booking.setLastModificationBy("system");
    }

    public Short findFreeSeatNumber(List<Booking> bookings) {
        Set<Short> usedSeats = bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED) // Ne garder que les réservations confirmées
                .map(Booking::getSeatNumber)
                .collect(Collectors.toSet());
        for (short i = 1; i <= 100; i++) {
            if (!usedSeats.contains(i)) {
                return i;
            }
        }
        throw new IllegalStateException("No seat available on this flight!");
        /*
         * Remarque sur la gestion de l’unicité seatNumber + flightId :
         *
         * 1. Problème initial :
         *    - Une contrainte UNIQUE sur (seatNumber, flightId) avait été ajoutée en base de données.
         *    - Cette contrainte ne tient pas compte du statut de la réservation (CONFIRMED / CANCELLED).
         *    - Résultat : Impossible de réattribuer un siège libéré par une réservation annulée.
         *
         * 2. Solution métier :
         *    - L’attribution des sièges ne prend désormais en compte que les réservations avec le statut CONFIRMED.
         *    - Les sièges libérés par des réservations annulées sont à nouveau disponibles.
         *
         * 3. Limite technique (MySQL) :
         *    - MySQL ne permet pas de contrainte UNIQUE “conditionnelle” sur (seatNumber, flightId, status = 'CONFIRMED').
         *    - La contrainte a donc été supprimée côté base de données (cf. ALTER TABLE ... DROP INDEX ...).
         *    - Le contrôle d’unicité métier est assuré par la couche service (Java).
         *    - Pour une contrainte conditionnelle en base, il faudrait utiliser PostgreSQL (index partiel).
         */

    }

    public String setNumberPassenger(Long idFlight, String lastName, String firstName, short age, Long idUser, Boolean gender){
        if (gender == null) {
            throw new IllegalArgumentException("Gender can't be null");
        }
        short genderShort = gender ? (short) 1 : (short) 0; //a determiner qui est male/female

        // safe pour les initiales (anti null/empty)
        String ln = (lastName != null && !lastName.isEmpty()) ? lastName.substring(0, 1).toUpperCase() : "X";
        String fn = (firstName != null && !firstName.isEmpty()) ? firstName.substring(0, 1).toUpperCase() : "X";

        String numPassenger = "F" + idFlight + "-" + ln + fn + age + idUser + "-" + genderShort;
        return numPassenger;
    }

    public void updateBookingFromBooking(Booking source, Booking target) {
        if (source.getSeatNumber() != null) target.setSeatNumber(source.getSeatNumber());
        if (source.getFirstNamePassenger() != null) target.setFirstNamePassenger(source.getFirstNamePassenger());
        if (source.getLastNamePassenger() != null) target.setLastNamePassenger(source.getLastNamePassenger());
        if (source.getNumberPassenger() != null) target.setNumberPassenger(source.getNumberPassenger());
        if (source.getPassengerAge() != null) target.setPassengerAge(source.getPassengerAge());
        if (source.getGender() != null) target.setGender(source.getGender());
        if (source.getStatus() != null) target.setStatus(source.getStatus());

    }

}
