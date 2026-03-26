package fr.thomascecil.heliodysse.domain.model.entity.viewdto;

import fr.thomascecil.heliodysse.domain.model.enums.planetEnum.PlanetName;
import java.time.LocalDateTime;

public record BookingInfo(
        Long idBooking,
        String numFlight,
        LocalDateTime dateDeparture,
        LocalDateTime dateArrival,
        String departureSpaceportName,
        PlanetName departurePlanetName,
        String arrivalSpaceportName,
        PlanetName arrivalPlanetName
) {
    // Constructeur explicite pour Hibernate (même si redondant avec le record)
    public BookingInfo(Long idBooking, String numFlight, LocalDateTime dateDeparture,
                       LocalDateTime dateArrival, String departureSpaceportName,
                       PlanetName departurePlanetName, String arrivalSpaceportName,
                       PlanetName arrivalPlanetName) {
        this.idBooking = idBooking;
        this.numFlight = numFlight;
        this.dateDeparture = dateDeparture;
        this.dateArrival = dateArrival;
        this.departureSpaceportName = departureSpaceportName;
        this.departurePlanetName = departurePlanetName;
        this.arrivalSpaceportName = arrivalSpaceportName;
        this.arrivalPlanetName = arrivalPlanetName;
    }
}