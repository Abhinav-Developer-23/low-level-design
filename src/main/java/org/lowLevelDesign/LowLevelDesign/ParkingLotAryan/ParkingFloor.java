package org.lowLevelDesign.LowLevelDesign.ParkingLotAryan;

import java.util.ArrayList;
import java.util.List;

public class ParkingFloor {
  // List of parking spots on this floor
  private List<ParkingSpot> spots;
  // Unique identifier for the floor
  private int floorNumber;
  
  public ParkingFloor(int floorNumber) {
    this.floorNumber = floorNumber;
    this.spots = new ArrayList<>();
  }
  
  // Adds a parking spot to this floor.
  public void addParkingSpot(ParkingSpot spot) {
    this.spots.add(spot);
  }
  
  // Finds an available parking spot for a specific vehicle type.
  public ParkingSpot findAvailableSpot(String vehicleType) {
    // Iterate through all spots to find an available spot matching the vehicle type
    for (ParkingSpot spot : spots) {
      // Check if the spot is not occupied and matches the vehicle type
      if (!spot.isOccupied() && spot.getSpotType().equalsIgnoreCase(vehicleType)) {
        return spot;
      }
    }
    return null; // No available spot found
  }
  
  // Retrieves all parking spots on this floor.
  public List<ParkingSpot> getParkingSpots() {
    return spots;
  }
  
  public int getFloorNumber() {
    return floorNumber;
  }
} 