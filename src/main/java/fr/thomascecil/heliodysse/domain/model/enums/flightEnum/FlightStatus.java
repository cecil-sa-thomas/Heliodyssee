package fr.thomascecil.heliodysse.domain.model.enums.flightEnum;

public enum FlightStatus {
    SCHEDULED("scheduled"),
    DELAYED("delayed"),
    BOARDING("boarding"),
    IN_FLIGHT("in_flight"),
    LANDED("landed"),
    CANCELLED("cancelled"),
    FULL("full"),
    CLOSED("closed");

    private final String label;

    FlightStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
