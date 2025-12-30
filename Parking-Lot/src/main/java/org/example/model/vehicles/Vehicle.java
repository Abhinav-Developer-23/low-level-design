package org.example.model.vehicles;

import lombok.Getter;
import org.example.model.ParkingSpot;
import org.example.enums.VehicleSize;

/**
 * Abstract base class for all types of vehicles in the parking lot system.
 * Follows Single Responsibility Principle - handles vehicle-specific properties and behavior.
 */
@Getter
public abstract class Vehicle {
    protected String licensePlate;
    protected String ownerName;
    protected VehicleSize size;

    public Vehicle(String licensePlate, String ownerName, VehicleSize size) {
        this.licensePlate = licensePlate;
        this.ownerName = ownerName;
        this.size = size;
    }

    /**
     * Checks if this vehicle can fit in a parking spot of the given size.
     * Follows Liskov Substitution Principle - all vehicles can be checked for fit.
     */
    public boolean canFitInSpot(ParkingSpot spot) {
        return spot.getSize().canAccommodate(size);
    }

    @Override
    public String toString() {
        return String.format("%s [License: %s, Owner: %s]",
                           this.getClass().getSimpleName(), licensePlate, ownerName);
    }
}
