package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.example.interfaces.ParkingStrategy;

/**
 * Represents a single level/floor in the parking lot.
 * Follows Single Responsibility Principle - manages parking spots on one level.
 */
public class Level {
    private final int levelNumber;
    private final List<ParkingSpot> parkingSpots;
    private final ParkingStrategy parkingStrategy;

    public Level(int levelNumber, int totalSpots, ParkingStrategy parkingStrategy) {
        this.levelNumber = levelNumber;
        this.parkingStrategy = parkingStrategy;
        this.parkingSpots = new ArrayList<>();

        // Initialize parking spots with distribution: 20% motorcycle, 50% compact, 30% regular
        initializeSpots(totalSpots);
    }

    private void initializeSpots(int totalSpots) {
        int motorcycleSpots = totalSpots / 5; // 20%
        int compactSpots = totalSpots / 2;    // 50%
        int regularSpots = totalSpots - motorcycleSpots - compactSpots; // 30%

        int spotNumber = 1;

        // Add motorcycle spots
        for (int i = 0; i < motorcycleSpots; i++) {
            parkingSpots.add(new ParkingSpot(
                String.format("L%d-M%d", levelNumber, spotNumber++),
                levelNumber, spotNumber, VehicleSize.MOTORCYCLE));
        }

        // Add compact spots
        for (int i = 0; i < compactSpots; i++) {
            parkingSpots.add(new ParkingSpot(
                String.format("L%d-C%d", levelNumber, spotNumber++),
                levelNumber, spotNumber, VehicleSize.COMPACT));
        }

        // Add regular spots
        for (int i = 0; i < regularSpots; i++) {
            parkingSpots.add(new ParkingSpot(
                String.format("L%d-R%d", levelNumber, spotNumber++),
                levelNumber, spotNumber, VehicleSize.REGULAR));
        }
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public List<ParkingSpot> getParkingSpots() {
        return new ArrayList<>(parkingSpots);
    }

    /**
     * Finds an available parking spot for the given vehicle using the configured strategy.
     * Follows Strategy Pattern - delegates to the parking strategy for spot selection.
     */
    public Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle) {
        List<ParkingSpot> availableSpots = parkingSpots.stream()
            .filter(spot -> spot.canAccommodate(vehicle))
            .collect(Collectors.toList());

        return parkingStrategy.findSpot(availableSpots);
    }

    /**
     * Parks a vehicle in an available spot on this level.
     * Returns the assigned spot if successful, empty Optional otherwise.
     */
    public Optional<ParkingSpot> parkVehicle(Vehicle vehicle) {
        Optional<ParkingSpot> spot = findAvailableSpot(vehicle);
        if (spot.isPresent() && spot.get().parkVehicle(vehicle)) {
            return spot;
        }
        return Optional.empty();
    }

    /**
     * Removes a vehicle from a parking spot based on license plate.
     * Returns the spot if found and vehicle removed, empty Optional otherwise.
     */
    public Optional<ParkingSpot> removeVehicle(String licensePlate) {
        return parkingSpots.stream()
            .filter(spot -> spot.getStatus() == SpotStatus.OCCUPIED)
            .filter(spot -> spot.getParkedVehicle().getLicensePlate().equals(licensePlate))
            .findFirst()
            .map(spot -> {
                spot.removeVehicle();
                return spot;
            });
    }

    /**
     * Gets the total number of parking spots on this level.
     */
    public int getTotalSpots() {
        return parkingSpots.size();
    }

    /**
     * Gets the number of available parking spots on this level.
     */
    public int getAvailableSpotsCount() {
        return (int) parkingSpots.stream()
            .filter(ParkingSpot::isAvailable)
            .count();
    }

    /**
     * Gets the number of occupied parking spots on this level.
     */
    public int getOccupiedSpotsCount() {
        return getTotalSpots() - getAvailableSpotsCount();
    }

    /**
     * Gets occupancy rate as a percentage.
     */
    public double getOccupancyRate() {
        return (double) getOccupiedSpotsCount() / getTotalSpots() * 100.0;
    }

    /**
     * Gets available spots for a specific vehicle size.
     */
    public List<ParkingSpot> getAvailableSpotsForSize(VehicleSize size) {
        return parkingSpots.stream()
            .filter(ParkingSpot::isAvailable)
            .filter(spot -> spot.getSize().canAccommodate(size))
            .collect(Collectors.toList());
    }

    /**
     * Checks if this level can accommodate the given vehicle.
     */
    public boolean canAccommodate(Vehicle vehicle) {
        return findAvailableSpot(vehicle).isPresent();
    }

    @Override
    public String toString() {
        return String.format("Level %d: %d/%d spots occupied (%.1f%%)",
                           levelNumber, getOccupiedSpotsCount(), getTotalSpots(), getOccupancyRate());
    }
}
