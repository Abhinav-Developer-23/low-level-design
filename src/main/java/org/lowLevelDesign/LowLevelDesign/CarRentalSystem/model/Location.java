package org.lowLevelDesign.LowLevelDesign.CarRentalSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Adding LocationStatus enum in the same file
enum LocationStatus {
    ACTIVE,
    INACTIVE,
    UNDER_MAINTENANCE,
    CLOSED_TEMPORARILY,
    PERMANENTLY_CLOSED
}

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private String locationId;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phoneNumber;
    private String email;

    // Operating hours
    private String openingTime;
    private String closingTime;

    // Coordinates for mapping
    private double latitude;
    private double longitude;

    // Location capacity
    private int parkingCapacity;
    private int currentVehicleCount;

    // Location status
    private LocationStatus status;
}