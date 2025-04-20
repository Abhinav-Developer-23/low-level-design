package org.lowLevelDesign.LowLevelDesign.ParkingLotAryan;

public class OtherVehicleSpot extends ParkingSpot {
  public OtherVehicleSpot(int spotNumber) {
    super(spotNumber, "Other");
  }
  
  @Override
  public boolean canParkVehicle(Vehicle vehicle) {
    return !"Car".equalsIgnoreCase(vehicle.getVehicleType()) && 
           !"Bike".equalsIgnoreCase(vehicle.getVehicleType());
  }
} 