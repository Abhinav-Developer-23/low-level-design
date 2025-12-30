package org.example.model;

import lombok.Getter;

/**
 * Data class representing the current status of the parking lot.
 * Follows Single Responsibility Principle - holds status information.
 */
@Getter
public class ParkingLotStatus {
    private final int totalSpots;
    private final int availableSpots;
    private final double occupancyRate;

    public ParkingLotStatus(int totalSpots, int availableSpots, double occupancyRate) {
        this.totalSpots = totalSpots;
        this.availableSpots = availableSpots;
        this.occupancyRate = occupancyRate;
    }

    public int getOccupiedSpots() {
        return totalSpots - availableSpots;
    }

    @Override
    public String toString() {
        return String.format("ParkingLotStatus [Total: %d, Available: %d, Occupied: %d, Occupancy: %.1f%%]",
                           totalSpots, availableSpots, getOccupiedSpots(), occupancyRate);
    }
}
