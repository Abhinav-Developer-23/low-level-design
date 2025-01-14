package org.lowLevelDesign.LowLevelDesign.ParkingLot;

import java.time.LocalDateTime;
import java.util.*;

class ParkingLotLLD {

    // Enums
    enum ParkingSpotType {
        HANDICAPPED,
        COMPACT,
        LARGE,
        MOTORCYCLE,
        ELECTRIC
    }

    enum VehicleType {
        CAR,
        TRUCK,
        ELECTRIC,
        VAN,
        MOTORCYCLE
    }

    enum AccountStatus {
        ACTIVE,
        BLOCKED,
        BANNED,
        NONE
    }

    enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED,
        REFUNDED
    }

    enum PaymentType {
        CASH,
        CREDIT_CARD
    }

    // Address class
    class Address {
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String country;
    }

    // Person class - Base class for Admin and ParkingAttendant
    class Person {
        private String name;
        private Address address;
        private String email;
        private String phone;
    }

    // Account class
    class Account {
        private String username;
        private String password;
        private AccountStatus status;
        private Person person;
    }

    // Abstract Vehicle class
    abstract class Vehicle {
        private String licenseNumber;
        private VehicleType type;
        private String color;

        public Vehicle(String licenseNumber, VehicleType type, String color) {
            this.licenseNumber = licenseNumber;
            this.type = type;
            this.color = color;
        }
    }

    // Concrete Vehicle classes
    class Car extends Vehicle {
        public Car(String licenseNumber, String color) {
            super(licenseNumber, VehicleType.CAR, color);
        }
    }

    class Electric extends Vehicle {
        public Electric(String licenseNumber, String color) {
            super(licenseNumber, VehicleType.ELECTRIC, color);
        }
    }

    // Abstract ParkingSpot class
    abstract class ParkingSpot {
        private final ParkingSpotType type;
        private String number;
        private boolean free;
        private Vehicle vehicle;

        public ParkingSpot(ParkingSpotType type) {
            this.type = type;
            this.free = true;
        }

        public boolean isFree() {
            return free;
        }

        public ParkingSpotType getType() {
            return type;
        }

        public boolean assignVehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
            free = false;
            return true;
        }

        public boolean removeVehicle() {
            this.vehicle = null;
            free = true;
            return true;
        }

        public String getNumber() {
            return String.valueOf(Math.random());
        }
    }

    // Concrete ParkingSpot classes
    class HandicappedSpot extends ParkingSpot {
        public HandicappedSpot() {
            super(ParkingSpotType.HANDICAPPED);
        }
    }

    class CompactSpot extends ParkingSpot {
        public CompactSpot() {
            super(ParkingSpotType.COMPACT);
        }
    }

    class ElectricSpot extends ParkingSpot {
        public ElectricSpot() {
            super(ParkingSpotType.ELECTRIC);
        }
    }

    // ParkingFloor class
    class ParkingFloor {
        private String name;
        private Map<String, ParkingSpot> spots;
        private Map<ParkingSpotType, Integer> availableSpots;
        private ParkingDisplayBoard displayBoard;

        public ParkingFloor(String name) {
            this.name = name;
            this.spots = new HashMap<>();
            this.availableSpots = new HashMap<>();
            this.displayBoard = new ParkingDisplayBoard();
        }

        public void addParkingSpot(ParkingSpot spot) {
            spots.putIfAbsent(spot.getNumber(), spot);
            availableSpots.put(spot.getType(), availableSpots.getOrDefault(spot.getType(), 0) + 1);
        }

        public void updateDisplayBoard() {
            // Update the display board with available spots
            displayBoard.setAvailableSpots(availableSpots);
        }
    }

    // ParkingDisplayBoard class
    class ParkingDisplayBoard {
        private Map<ParkingSpotType, Integer> availableSpots;

        public void setAvailableSpots(Map<ParkingSpotType, Integer> spots) {
            this.availableSpots = spots;
        }

        public void show() {
            // Display available spots
        }
    }

    // ParkingTicket class
    class ParkingTicket {
        private String ticketId;
        private String licensePlate;
        private LocalDateTime issuedAt;
        private LocalDateTime payedAt;
        private double payedAmount;
        private ParkingSpot spot;
        private PaymentStatus status;

        public ParkingTicket(String licensePlate, ParkingSpot spot) {
            this.ticketId = UUID.randomUUID().toString();
            this.licensePlate = licensePlate;
            this.issuedAt = LocalDateTime.now();
            this.spot = spot;
            this.status = PaymentStatus.PENDING;
        }
    }

    // Main ParkingLot class
    public class ParkingLot {
        private static ParkingLot instance = null;
        private String name;
        private Address address;
        private List<ParkingFloor> floors;
        private List<EntrancePanel> entrances;
        private List<ExitPanel> exits;

        private ParkingLot() {
            floors = new ArrayList<>();
            entrances = new ArrayList<>();
            exits = new ArrayList<>();
        }

        // Singleton pattern
        public ParkingLot getInstance() {
            if (instance == null) {
                instance = new ParkingLot();
            }
            return instance;
        }

        public boolean isFull() {
            // Check if parking lot is full
            return false;
        }

        public void addFloor(ParkingFloor floor) {
            floors.add(floor);
        }

        public void addEntrance(EntrancePanel entrance) {
            entrances.add(entrance);
        }

        public void addExit(ExitPanel exit) {
            exits.add(exit);
        }
    }

    // Panel classes
    class EntrancePanel {
        private String id;

        public ParkingTicket getParkingTicket(Vehicle vehicle) {
            // Logic to get parking ticket
            return null;
        }
    }

    class ExitPanel {
        private String id;

        public double scanTicket(ParkingTicket ticket) {
            // Logic to scan ticket and calculate fee
            return 0.0;
        }

        public boolean processPayment(ParkingTicket ticket, PaymentType paymentType) {
            // Logic to process payment
            return false;
        }
    }

    // Payment related classes
    class Payment {
        private double amount;
        private LocalDateTime timestamp;
        private PaymentStatus status;
        private PaymentType type;

        public Payment(double amount, PaymentType type) {
            this.amount = amount;
            this.type = type;
            this.timestamp = LocalDateTime.now();
            this.status = PaymentStatus.PENDING;
        }
    }

    class ParkingRate {
        private double hourlyRate;
        private Map<Integer, Double> hourlyRates;  // <hour, rate>

        public ParkingRate() {
            hourlyRates = new HashMap<>();
            // Initialize default rates
            hourlyRates.put(1, 4.0);  // $4 for first hour
            hourlyRates.put(2, 3.5);  // $3.5 for second hour
            hourlyRates.put(3, 3.5);  // $3.5 for third hour
            // Default rate for remaining hours
            this.hourlyRate = 2.5;
        }

        public double calculatePrice(LocalDateTime startTime, LocalDateTime endTime) {
            // Calculate parking fee based on duration
            return 0.0;
        }
    }
}
