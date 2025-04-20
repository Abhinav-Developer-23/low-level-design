package org.lowLevelDesign.LowLevelDesign.ParkingLotAryan;

public class CarVehicle extends Vehicle { 
  public CarVehicle(String licensePlate, ParkingFeeStrategy feeStrategy) { 
    super(licensePlate, "Car", feeStrategy); 
  } 
} 