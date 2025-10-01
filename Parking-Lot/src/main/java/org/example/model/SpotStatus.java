package org.example.model;

/**
 * Enum representing the status of a parking spot.
 */
public enum SpotStatus {
    AVAILABLE("Available for parking"),
    OCCUPIED("Currently occupied by a vehicle"),
    OUT_OF_ORDER("Temporarily unavailable for maintenance"),
    RESERVED("Reserved for specific purposes");

    private final String description;

    SpotStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
