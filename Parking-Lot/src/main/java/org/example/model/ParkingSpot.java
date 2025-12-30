package org.example.model;

import lombok.Getter;
import org.example.enums.SpotStatus;
import org.example.enums.VehicleSize;
import org.example.model.vehicles.Vehicle;

import java.time.LocalDateTime;

/**
 * Represents a single parking spot in the parking lot.
 * Follows Single Responsibility Principle - manages its own state and parking operations.
 */
@Getter
public class ParkingSpot {
    private final String spotId;
    private final int levelNumber;
    private final int spotNumber;
    private final VehicleSize size;
    private SpotStatus status;
    private Vehicle parkedVehicle;
    private LocalDateTime occupiedSince;

    public ParkingSpot(String spotId, int levelNumber, int spotNumber, VehicleSize size) {
        this.spotId = spotId;
        this.levelNumber = levelNumber;
        this.spotNumber = spotNumber;
        this.size = size;
        this.status = SpotStatus.AVAILABLE;
    }

    /**
     * Checks if this spot can accommodate the given vehicle.
     * Follows Open/Closed Principle - can be extended for new vehicle types.
     */
    public boolean canAccommodate(Vehicle vehicle) {
        return status == SpotStatus.AVAILABLE && size.canAccommodate(vehicle.getSize());
    }

    /**
     * Parks a vehicle in this spot.
     * Returns true if parking was successful, false otherwise.
     */
    public boolean parkVehicle(Vehicle vehicle) {
        if (!canAccommodate(vehicle)) {
            return false;
        }

        this.parkedVehicle = vehicle;
        this.status = SpotStatus.OCCUPIED;
        this.occupiedSince = LocalDateTime.now();
        return true;
    }

    /**
     * Removes a vehicle from this spot.
     * Returns the vehicle that was parked, or null if spot was empty.
     */
    public Vehicle removeVehicle() {
        if (status == SpotStatus.AVAILABLE) {
            return null;
        }

        Vehicle vehicle = this.parkedVehicle;
        this.parkedVehicle = null;
        this.status = SpotStatus.AVAILABLE;
        this.occupiedSince = null;
        return vehicle;
    }

    /**
     * Checks if the spot is available for parking.
     */
    public boolean isAvailable() {
        return status == SpotStatus.AVAILABLE;
    }

    /**
     * Gets the duration for which the spot has been occupied.
     */
    public long getOccupiedDurationMinutes() {
        if (occupiedSince == null) {
            return 0;
        }
        return java.time.Duration.between(occupiedSince, LocalDateTime.now()).toMinutes();
    }

    @Override
    public String toString() {
        return String.format("ParkingSpot [ID: %s, Level: %d, Spot: %d, Size: %s, Status: %s]",
                           spotId, levelNumber, spotNumber, size, status);
    }
}
