package org.example;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.example.model.ParkingSpot;
import org.example.interfaces.ParkingStrategy;

/**
 * Concrete strategy that randomly selects an available parking spot.
 * Useful for load balancing and preventing overcrowding in specific areas.
 */
public class RandomSpotStrategy implements ParkingStrategy {
    private final Random random = new Random();

    @Override
    public Optional<ParkingSpot> findSpot(List<ParkingSpot> availableSpots) {
        if (availableSpots.isEmpty()) {
            return Optional.empty();
        }

        int randomIndex = random.nextInt(availableSpots.size());
        return Optional.of(availableSpots.get(randomIndex));
    }

    @Override
    public String getStrategyName() {
        return "Random Available Spot";
    }
}
