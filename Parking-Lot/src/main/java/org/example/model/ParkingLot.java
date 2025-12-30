package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import org.example.enums.VehicleSize;
import org.example.interfaces.ParkingStrategy;
import org.example.interfaces.ParkingObserver;
import org.example.model.vehicles.Vehicle;

/**
 * Main parking lot management system.
 * Follows Singleton Pattern - ensures only one instance of the parking lot exists.
 * Follows Single Responsibility Principle - manages overall parking operations.
 * Supports concurrent access with thread-safe operations.
 */
public class ParkingLot {
    private static ParkingLot instance;
    private static final ReentrantLock lock = new ReentrantLock();

    private final String name;
    private final List<Level> levels;
    private final List<ParkingObserver> observers;
    private final ParkingStrategy defaultStrategy;

    /**
     * Private constructor for Singleton pattern.
     */
    private ParkingLot(String name, int numberOfLevels, int spotsPerLevel, ParkingStrategy strategy) {
        this.name = name;
        this.levels = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.defaultStrategy = strategy;

        initializeLevels(numberOfLevels, spotsPerLevel);
    }

    /**
     * Gets the singleton instance of the parking lot.
     * Follows Singleton Pattern with thread-safe double-checked locking.
     */
    public static ParkingLot getInstance(String name, int numberOfLevels, int spotsPerLevel, ParkingStrategy strategy) {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new ParkingLot(name, numberOfLevels, spotsPerLevel, strategy);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    /**
     * Gets the existing instance or throws exception if not initialized.
     */
    public static ParkingLot getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ParkingLot not initialized. Call getInstance(name, levels, spotsPerLevel, strategy) first.");
        }
        return instance;
    }

    private void initializeLevels(int numberOfLevels, int spotsPerLevel) {
        for (int i = 1; i <= numberOfLevels; i++) {
            levels.add(new Level(i, spotsPerLevel, defaultStrategy));
        }
    }

    public String getName() {
        return name;
    }

    public List<Level> getLevels() {
        return new ArrayList<>(levels);
    }

    /**
     * Parks a vehicle in the parking lot.
     * Returns a ticket if parking was successful, empty Optional otherwise.
     * Uses thread-safe operations for concurrent access.
     */
    public synchronized Optional<Ticket> parkVehicle(Vehicle vehicle) {
        lock.lock();
        try {
            for (Level level : levels) {
                Optional<ParkingSpot> spot = level.parkVehicle(vehicle);
                if (spot.isPresent()) {
                    Ticket ticket = new Ticket(vehicle, spot.get());
                    notifyObservers();
                    return Optional.of(ticket);
                }
            }
            return Optional.empty();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes a vehicle from the parking lot based on ticket.
     * Returns the vehicle if found and removed, empty Optional otherwise.
     */
    public synchronized Optional<Vehicle> removeVehicle(Ticket ticket) {
        lock.lock();
        try {
            for (Level level : levels) {
                Optional<ParkingSpot> spot = level.removeVehicle(ticket.getVehicle().getLicensePlate());
                if (spot.isPresent())  {
                    notifyObservers();
                    return Optional.of(ticket.getVehicle());
                }
            }
            return Optional.empty();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the total number of parking spots in the lot.
     */
    public int getTotalSpots() {
        return levels.stream().mapToInt(Level::getTotalSpots).sum();
    }

    /**
     * Gets the total number of available parking spots.
     */
    public int getAvailableSpotsCount() {
        return levels.stream().mapToInt(Level::getAvailableSpotsCount).sum();
    }

    /**
     * Gets the total number of occupied parking spots.
     */
    public int getOccupiedSpotsCount() {
        return getTotalSpots() - getAvailableSpotsCount();
    }

    /**
     * Gets overall occupancy rate as a percentage.
     */
    public double getOccupancyRate() {
        return (double) getOccupiedSpotsCount() / getTotalSpots() * 100.0;
    }

    /**
     * Gets available spots for a specific vehicle size across all levels.
     */
    public List<ParkingSpot> getAvailableSpotsForSize(VehicleSize size) {
        List<ParkingSpot> availableSpots = new ArrayList<>();
        for (Level level : levels) {
            availableSpots.addAll(level.getAvailableSpotsForSize(size));
        }
        return availableSpots;
    }

    /**
     * Checks if the parking lot can accommodate the given vehicle.
     */
    public boolean canAccommodate(Vehicle vehicle) {
        return levels.stream().anyMatch(level -> level.canAccommodate(vehicle));
    }

    // Observer Pattern Implementation

    /**
     * Adds an observer to receive parking lot updates.
     * Follows Observer Pattern for real-time availability updates.
     */
    public void addObserver(ParkingObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from receiving updates.
     */
    public void removeObserver(ParkingObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers about parking lot status changes.
     */
    private void notifyObservers() {
        ParkingLotStatus status = new ParkingLotStatus(
            getTotalSpots(),
            getAvailableSpotsCount(),
            getOccupancyRate()
        );

        for (ParkingObserver observer : observers) {
            observer.onParkingLotUpdate(status);
        }
    }

    /**
     * Displays current status of all levels.
     */
    public void displayStatus() {
        System.out.println("\n=== " + name + " Status ===");
        System.out.printf("Total Spots: %d, Available: %d, Occupied: %d (%.1f%%)\n",
                         getTotalSpots(), getAvailableSpotsCount(), getOccupiedSpotsCount(), getOccupancyRate());

        for (Level level : levels) {
            System.out.println(level);
        }
        System.out.println("=====================\n");
    }

    @Override
    public String toString() {
        return String.format("%s Parking Lot: %d levels, %d total spots, %.1f%% occupied",
                           name, levels.size(), getTotalSpots(), getOccupancyRate());
    }
}
