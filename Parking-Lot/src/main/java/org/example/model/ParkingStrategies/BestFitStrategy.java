package org.example.model.ParkingStrategies;

import java.util.List;
import java.util.Optional;
import java.util.Comparator;
import org.example.model.ParkingSpot;
import org.example.interfaces.ParkingStrategy;

/**
 * Concrete strategy that finds the best fit parking spot (smallest available spot that can fit the vehicle).
 * Optimizes space utilization by using smaller spots when possible.
 */
public class BestFitStrategy implements ParkingStrategy {

    @Override
    public Optional<ParkingSpot> findSpot(List<ParkingSpot> availableSpots) {
        return availableSpots.stream()
            .min(Comparator.comparingInt(spot -> spot.getSize().getSizeValue()));
    }

    @Override
    public String getStrategyName() {
        return "Best Fit (Smallest Available)";
    }
}
