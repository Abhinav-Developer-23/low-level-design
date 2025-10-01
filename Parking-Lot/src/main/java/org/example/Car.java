package org.example;

/**
 * Represents a car vehicle.
 * Follows Liskov Substitution Principle - can be used anywhere a Vehicle is expected.
 */
public class Car extends Vehicle {
    public Car(String licensePlate, String ownerName) {
        super(licensePlate, ownerName, VehicleSize.REGULAR);
    }
}
