package org.lowLevelDesign.LowLevelDesign.Scratch.ParkingLotProblem;

import java.time.LocalDateTime;

public class ParkingSpot {


    int parkingNumber;

    private int floor;

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    private ParkingType parkingType;

    private LocalDateTime parkedAt;

    private boolean isOccupied;

    public int getParkingNumber() {
        return parkingNumber;
    }

    public void setParkingNumber(int parkingNumber) {
        this.parkingNumber = parkingNumber;
    }

    public ParkingType getParkingType() {
        return parkingType;
    }

    public void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    public LocalDateTime getParkedAt() {
        return parkedAt;
    }

    public void setParkedAt(LocalDateTime parkedAt) {
        this.parkedAt = parkedAt;
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}
