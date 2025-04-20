package org.lowLevelDesign.LowLevelDesign.ParkingLotAryan;

// Modify Vehicle Factory to support fee strategy
public class VehicleFactory {
  public static Vehicle createVehicle(
      String vehicleType, String licensePlate, ParkingFeeStrategy feeStrategy) {
    if (vehicleType.equalsIgnoreCase("Car")) {
      return new CarVehicle(licensePlate, feeStrategy);
    } else if (vehicleType.equalsIgnoreCase("Bike")) {
      return new BikeVehicle(licensePlate, feeStrategy);
    }
    return new OtherVehicle(
        licensePlate, feeStrategy); // For unsupported vehicle types
  }
} 