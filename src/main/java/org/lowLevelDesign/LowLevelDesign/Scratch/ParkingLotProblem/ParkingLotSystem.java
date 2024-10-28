package org.lowLevelDesign.LowLevelDesign.Scratch.ParkingLotProblem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParkingLotSystem {


    List<List<ParkingSpot>> parkingSpotList = new ArrayList<>();
    ParkingCostCalculator parkingCostCalculator;

    ParkingSpotFinder parkingSpotFinder;

    int floor;

    public ParkingLotSystem(ParkingCostCalculator parkingCostCalculator, ParkingSpotFinder parkingSpotFinder, int floor) {
        this.parkingCostCalculator = parkingCostCalculator;
        this.parkingSpotFinder = parkingSpotFinder;
        this.floor = floor;
    }

    public List<List<ParkingSpot>> getParkingSpotList() {
        return parkingSpotList;
    }

    public void setParkingSpotList(List<List<ParkingSpot>> parkingSpotList) {
        this.parkingSpotList = parkingSpotList;
    }

    public ParkingCostCalculator getParkingCostCalculator() {
        return parkingCostCalculator;
    }

    public void setParkingCostCalculator(ParkingCostCalculator parkingCostCalculator) {
        this.parkingCostCalculator = parkingCostCalculator;
    }

    public void initializeAllParkingSpot() {

        for (int i = 0; i < floor; i++) {
            List<ParkingSpot> parkingFloor = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                int remainder = j % 3;

                ParkingSpot parkingSpot = new ParkingSpot();
                parkingSpot.setParkingNumber(j);
                parkingSpot.setFloor(i);
                parkingSpot.setIsOccupied(false);
                parkingSpot.setParkedAt(null);

                if (remainder == 0) {
                    parkingSpot.setParkingType(ParkingType.CAR);
                } else if (remainder == 1) {
                    parkingSpot.setParkingType(ParkingType.BIKE);
                } else {
                    parkingSpot.setParkingType(ParkingType.HANDICAPPED);
                }
                parkingFloor.add(parkingSpot);

            }
            parkingSpotList.add(parkingFloor);

        }

    }


    public ParkingSpot getParkingSpot(ParkingType parkingType) throws NoParkingSpotException {

        for (int i = 0; i < floor; i++) {
            List<ParkingSpot> floor = parkingSpotList.get(i);
            for (int j = 0; j < 100; j++) {
                ParkingSpot parkingSpot = floor.get(j);

                if (parkingSpot.getParkingType().equals(parkingType) && !parkingSpot.getIsOccupied()) {

                    System.out.println("you have been assigned parking spot in floor " + i + " and number " + j);

                    parkingSpot.setIsOccupied(true);
                    parkingSpot.setParkedAt(LocalDateTime.now());

                    return parkingSpot;


                }

            }
        }
        throw new NoParkingSpotException("No parking spot found");
    }

    public double leaveParkingSpot(int floor, int number) {

        if (floor - 1 > this.floor || number > 100) {
            System.out.println("invalid parking location");
            return 0;

        }

        ParkingSpot parkingSpot = parkingSpotList.get(floor).get(number);

        if (!parkingSpot.getIsOccupied()) {
            System.out.println("parking spot is not occupied  ");
            return 0;
        }


        parkingSpot.setIsOccupied(false);
        double cost = parkingCostCalculator.calculateCost(parkingSpot);

        return cost;
    }


}
