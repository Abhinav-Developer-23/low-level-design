package org.example;

import java.util.List;
import java.util.Optional;
import org.example.model.ParkingSpot;
import org.example.interfaces.ParkingStrategy;

/**
 * Concrete strategy that finds the nearest available parking spot.
 * Prioritizes spots based on their spot number (lower numbers are closer to entrance).
 */
public class NearestSpotStrategy implements ParkingStrategy {

    @Override
    public Optional<ParkingSpot> findSpot(List<ParkingSpot> availableSpots) {
        return availableSpots.stream()
            .min((spot1, spot2) -> Integer.compare(spot1.getSpotNumber(), spot2.getSpotNumber()));
    }

    @Override
    public String getStrategyName() {
        return "Nearest Available Spot";
    }
}
