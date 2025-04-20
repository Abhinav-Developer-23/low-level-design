package org.lowLevelDesign.LowLevelDesign.ParkingLotAryan;

public class BikeVehicle extends Vehicle { 
  public BikeVehicle(String licensePlate, ParkingFeeStrategy feeStrategy) { 
    super(licensePlate, "Bike", feeStrategy); 
  } 
} 