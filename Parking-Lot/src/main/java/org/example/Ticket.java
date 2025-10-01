package org.example;

import java.time.LocalDateTime;

/**
 * Represents a parking ticket issued when a vehicle enters the parking lot.
 * Follows Single Responsibility Principle - tracks parking session information.
 */
public class Ticket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot parkingSpot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public Ticket(Vehicle vehicle, ParkingSpot parkingSpot) {
        this.ticketId = generateTicketId();
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.entryTime = LocalDateTime.now();
        this.exitTime = null;
    }

    private String generateTicketId() {
        return "TICKET_" + System.currentTimeMillis() + "_" + (vehicle != null ? vehicle.getLicensePlate() : "UNKNOWN");
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    /**
     * Marks the ticket as exited and calculates parking duration.
     */
    public void markAsExited() {
        this.exitTime = LocalDateTime.now();
    }

    /**
     * Calculates the parking duration in minutes.
     * Returns 0 if the vehicle hasn't exited yet.
     */
    public long getParkingDurationMinutes() {
        if (exitTime == null) {
            return java.time.Duration.between(entryTime, LocalDateTime.now()).toMinutes();
        }
        return java.time.Duration.between(entryTime, exitTime).toMinutes();
    }

    /**
     * Checks if the vehicle has exited the parking lot.
     */
    public boolean isExited() {
        return exitTime != null;
    }

    @Override
    public String toString() {
        return String.format("Ticket [ID: %s, Vehicle: %s, Spot: %s, Entry: %s, Duration: %d minutes]",
                           ticketId, vehicle.getLicensePlate(), parkingSpot.getSpotId(),
                           entryTime, getParkingDurationMinutes());
    }
}
