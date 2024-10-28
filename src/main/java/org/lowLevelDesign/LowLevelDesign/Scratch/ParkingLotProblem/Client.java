package org.lowLevelDesign.LowLevelDesign.Scratch.ParkingLotProblem;

import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) throws Exception {

        ParkingCostCalculator parkingCostCalculator = new SimpleCostCalculator();
        ParkingSpotFinder parkingSpotFinder = new LinearParkingSpotFinder();
        int floors = 6;
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(parkingCostCalculator, parkingSpotFinder, floors);
        parkingLotSystem.initializeAllParkingSpot();
        ParkingSpot parkingSpot=parkingLotSystem.getParkingSpot(ParkingType.HANDICAPPED);

        TimeUnit.SECONDS.sleep(120);
        parkingLotSystem.leaveParkingSpot(0,2);




    }
}
