package org.example;

/**
 * Data class representing the current status of the parking lot.
 * Follows Single Responsibility Principle - holds status information.
 */
public class ParkingLotStatus {
    private final int totalSpots;
    private final int availableSpots;
    private final double occupancyRate;

    public ParkingLotStatus(int totalSpots, int availableSpots, double occupancyRate) {
        this.totalSpots = totalSpots;
        this.availableSpots = availableSpots;
        this.occupancyRate = occupancyRate;
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    public int getOccupiedSpots() {
        return totalSpots - availableSpots;
    }

    public double getOccupancyRate() {
        return occupancyRate;
    }

    @Override
    public String toString() {
        return String.format("ParkingLotStatus [Total: %d, Available: %d, Occupied: %d, Occupancy: %.1f%%]",
                           totalSpots, availableSpots, getOccupiedSpots(), occupancyRate);
    }
}
