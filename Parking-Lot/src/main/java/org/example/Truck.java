package org.example;

/**
 * Represents a truck vehicle.
 * Follows Liskov Substitution Principle - can be used anywhere a Vehicle is expected.
 */
public class Truck extends Vehicle {
    public Truck(String licensePlate, String ownerName) {
        super(licensePlate, ownerName, VehicleSize.LARGE);
    }
}
