package org.lowLevelDesign.LowLevelDesign.CarRentalSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    private String barcode;
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private int passengerCapacity;
    private String parkingStallNumber;
    private VehicleStatus status;
    private VehicleType type;
    private double mileage;
    private double dailyRate;
    private List<VehicleLog> vehicleLogs;
    private Date lastServiceDate;
    private String color;
    private String transmission;
    private FuelType fuelType;
}
