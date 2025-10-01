package org.example;

import java.util.List;
import java.util.Optional;
import org.example.model.*;
import org.example.interfaces.*;
import org.example.BestFitStrategy;
import org.example.NearestSpotStrategy;
import org.example.RandomSpotStrategy;

/**
 * Main demonstration class for the Parking Lot System.
 * Shows the usage of all design patterns and SOLID principles implemented.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Advanced Parking Lot System!");
        System.out.println("=============================================\n");

        // Demonstrate different parking strategies
        demonstrateParkingStrategies();

        // Demonstrate Observer pattern with display boards
        demonstrateObserverPattern();

        // Demonstrate comprehensive parking operations
        demonstrateParkingOperations();

        System.out.println("Parking Lot System Demo Completed!");
    }

    /**
     * Demonstrates different parking allocation strategies.
     */
    private static void demonstrateParkingStrategies() {
        System.out.println("DEMONSTRATING PARKING STRATEGIES");
        System.out.println("==================================");

        // Create sample vehicles
        Vehicle car1 = new Car("CAR001", "John Doe");
        Vehicle motorcycle1 = new Motorcycle("BIKE001", "Jane Smith");
        Vehicle truck1 = new Truck("TRUCK001", "Bob Wilson");

        // Test each strategy
        ParkingStrategy[] strategies = {
            new NearestSpotStrategy(),
            new RandomSpotStrategy(),
            new BestFitStrategy()
        };

        for (ParkingStrategy strategy : strategies) {
            System.out.println("\nTesting Strategy: " + strategy.getStrategyName());

            // Create a small parking lot for testing
            ParkingLot parkingLot = ParkingLot.getInstance("TestLot", 2, 10, strategy);

            // Try to park different vehicle types
            testVehicleParking(parkingLot, car1, "Car");
            testVehicleParking(parkingLot, motorcycle1, "Motorcycle");
            testVehicleParking(parkingLot, truck1, "Truck");

            parkingLot.displayStatus();
        }
    }

    /**
     * Demonstrates the Observer pattern with display boards.
     */
    private static void demonstrateObserverPattern() {
        System.out.println("\nDEMONSTRATING OBSERVER PATTERN");
        System.out.println("=================================");

        // Create main parking lot
        ParkingLot parkingLot = ParkingLot.getInstance("MainLot", 3, 20, new NearestSpotStrategy());

        // Create display boards (observers)
        DisplayBoard entranceBoard = new DisplayBoard("ENTRANCE");
        DisplayBoard exitBoard = new DisplayBoard("EXIT");
        DisplayBoard mobileAppBoard = new DisplayBoard("MOBILE_APP");

        // Register observers
        parkingLot.addObserver(entranceBoard);
        parkingLot.addObserver(exitBoard);
        parkingLot.addObserver(mobileAppBoard);

        System.out.println("Initial Status:");
        parkingLot.displayStatus();

        // Show initial display board status
        System.out.println("\nDisplay Boards Initial State:");
        entranceBoard.display();

        // Park some vehicles to trigger updates
        Vehicle car1 = new Car("OBS001", "Observer Test");
        Optional<Ticket> ticket1 = parkingLot.parkVehicle(car1);

        if (ticket1.isPresent()) {
            System.out.println("\nParked vehicle, triggering observer updates...");
            System.out.println("Display Boards After Parking:");
            entranceBoard.display();
            exitBoard.display();
            mobileAppBoard.display();
        }
    }

    /**
     * Demonstrates comprehensive parking operations.
     */
    private static void demonstrateParkingOperations() {
        System.out.println("\nCOMPREHENSIVE PARKING OPERATIONS");
        System.out.println("===================================");

        // Create main parking lot
        ParkingLot parkingLot = ParkingLot.getInstance("DemoLot", 4, 25, new BestFitStrategy());

        // Create various vehicles
        Vehicle[] vehicles = {
            new Car("CAR001", "Alice Johnson"),
            new Car("CAR002", "Bob Smith"),
            new Motorcycle("BIKE001", "Charlie Brown"),
            new Truck("TRUCK001", "Diana Prince"),
            new Car("CAR003", "Eve Wilson"),
            new Motorcycle("BIKE002", "Frank Miller"),
            new Truck("TRUCK002", "Grace Lee"),
            new Car("CAR004", "Henry Ford"),
            new Motorcycle("BIKE003", "Iris West"),
            new Car("CAR005", "Jack Ryan")
        };

        System.out.println("Initial parking lot status:");
        parkingLot.displayStatus();

        // Park vehicles
        System.out.println("\nParking vehicles...");
        Ticket[] tickets = new Ticket[vehicles.length];

        for (int i = 0; i < vehicles.length; i++) {
            Optional<Ticket> ticket = parkingLot.parkVehicle(vehicles[i]);
            if (ticket.isPresent()) {
                tickets[i] = ticket.get();
                System.out.printf("[SUCCESS] Parked %s (%s) at spot %s\n",
                    vehicles[i].getClass().getSimpleName(),
                    vehicles[i].getLicensePlate(),
                    tickets[i].getParkingSpot().getSpotId());
            } else {
                System.out.printf("[FAILED] Failed to park %s (%s) - No available spots\n",
                    vehicles[i].getClass().getSimpleName(),
                    vehicles[i].getLicensePlate());
            }
        }

        System.out.println("\nStatus after parking:");
        parkingLot.displayStatus();

        // Demonstrate ticket system
        System.out.println("\nTICKET SYSTEM DEMONSTRATION");
        System.out.println("=============================");
        if (tickets[0] != null) {
            Ticket firstTicket = tickets[0];
            System.out.println("Sample Ticket Details:");
            System.out.println(firstTicket);

            // Simulate some parking time
            try {
                Thread.sleep(100); // Small delay to show duration
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.printf("Vehicle has been parked for %d minutes\n",
                firstTicket.getParkingDurationMinutes());
        }

        // Unpark some vehicles
        System.out.println("\nUnparking vehicles...");
        String[] licensePlatesToRemove = {"CAR001", "BIKE001", "TRUCK001"};

        for (String licensePlate : licensePlatesToRemove) {
            // Find and remove vehicle by license plate
            boolean removed = false;
            for (Level level : parkingLot.getLevels()) {
                Optional<ParkingSpot> spot = level.removeVehicle(licensePlate);
                if (spot.isPresent()) {
                    removed = true;
                    break;
                }
            }

            if (removed) {
                System.out.printf("[SUCCESS] Removed vehicle with license plate: %s\n", licensePlate);
            } else {
                System.out.printf("[FAILED] Vehicle with license plate %s not found\n", licensePlate);
            }
        }

        System.out.println("\nFinal parking lot status:");
        parkingLot.displayStatus();

        // Show availability by vehicle type
        System.out.println("\nAVAILABILITY BY VEHICLE TYPE");
        System.out.println("===============================");
        for (VehicleSize size : VehicleSize.values()) {
            List<ParkingSpot> availableSpots = parkingLot.getAvailableSpotsForSize(size);
            System.out.printf("%s spots available: %d\n",
                size.name(), availableSpots.size());
        }
    }

    /**
     * Helper method to test parking a specific vehicle type.
     */
    private static void testVehicleParking(ParkingLot parkingLot, Vehicle vehicle, String vehicleType) {
        Optional<Ticket> ticket = parkingLot.parkVehicle(vehicle);
        if (ticket.isPresent()) {
            System.out.printf("[SUCCESS] Successfully parked %s at spot %s\n",
                vehicleType, ticket.get().getParkingSpot().getSpotId());
        } else {
            System.out.printf("[FAILED] No available spots for %s\n", vehicleType);
        }
    }
}

