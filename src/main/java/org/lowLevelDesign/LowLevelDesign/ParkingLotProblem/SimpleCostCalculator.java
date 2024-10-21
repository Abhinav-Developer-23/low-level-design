package org.lowLevelDesign.LowLevelDesign.ParkingLotProblem;

import java.time.Duration;
import java.time.LocalDateTime;

public class SimpleCostCalculator implements ParkingCostCalculator {

    @Override
    public double calculateCost(ParkingSpot parkingSpot) {

        Duration timeSpent = Duration.between(parkingSpot.getParkedAt(), LocalDateTime.now());

        long hours = timeSpent.toHours();
        long minutes = timeSpent.toMinutes() % 60; // Get remaining minutes
        double cost2 = 0;
        if (minutes > 0) {
             cost2 = 10;
        }

        double cost = 10 * hours + cost2;

        System.out.println("Time spent in parking spot is hours" + hours+" minutes "+minutes +"cost is "+cost);
        return cost;
    }
}
