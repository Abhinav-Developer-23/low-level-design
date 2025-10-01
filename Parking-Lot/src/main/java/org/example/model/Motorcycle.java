package org.example.model;

/**
 * Represents a motorcycle vehicle.
 * Follows Liskov Substitution Principle - can be used anywhere a Vehicle is expected.
 */
public class Motorcycle extends Vehicle {
    public Motorcycle(String licensePlate, String ownerName) {
        super(licensePlate, ownerName, VehicleSize.MOTORCYCLE);
    }
}
