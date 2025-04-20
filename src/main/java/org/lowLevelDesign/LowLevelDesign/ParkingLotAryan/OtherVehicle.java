package org.lowLevelDesign.LowLevelDesign.ParkingLotAryan;

// OtherVehicle class for any other vehicle type
public class OtherVehicle extends Vehicle { 
  public OtherVehicle(String licensePlate, ParkingFeeStrategy feeStrategy) { 
    super(licensePlate, "Other", feeStrategy); 
  }
} 