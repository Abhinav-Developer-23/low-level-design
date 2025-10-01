package org.example.interfaces;

import java.util.List;
import java.util.Optional;
import org.example.model.ParkingSpot;

/**
 * Strategy interface for parking spot allocation algorithms.
 * Follows Strategy Pattern - allows different parking allocation strategies.
 * Follows Open/Closed Principle - can add new strategies without modifying existing code.
 */
public interface ParkingStrategy {

    /**
     * Finds a suitable parking spot from the list of available spots.
     * @param availableSpots List of available parking spots
     * @return Optional containing the selected spot, or empty if no suitable spot found
     */
    Optional<ParkingSpot> findSpot(List<ParkingSpot> availableSpots);

    /**
     * Gets the name/description of this parking strategy.
     * @return Strategy name
     */
    String getStrategyName();
}
