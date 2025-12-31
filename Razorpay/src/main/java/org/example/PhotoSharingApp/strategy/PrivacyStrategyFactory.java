package org.example.PhotoSharingApp.strategy;

import org.example.PhotoSharingApp.enums.PrivacyLevel;
import org.example.PhotoSharingApp.interfaces.PrivacyStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory Pattern: Creates appropriate privacy strategy based on privacy level.
 * Centralizes strategy creation logic.
 */
public class PrivacyStrategyFactory {
    private static final Map<PrivacyLevel, PrivacyStrategy> strategies = new HashMap<>();
    
    static {
        strategies.put(PrivacyLevel.PUBLIC, new PublicPrivacyStrategy());
        strategies.put(PrivacyLevel.FOLLOWERS_ONLY, new FollowersOnlyPrivacyStrategy());
        strategies.put(PrivacyLevel.PRIVATE, new PrivatePrivacyStrategy());
    }
    
    /**
     * Gets the appropriate privacy strategy for a given privacy level.
     * 
     * @param privacyLevel the privacy level
     * @return the corresponding privacy strategy
     */
    public static PrivacyStrategy getStrategy(PrivacyLevel privacyLevel) {
        return strategies.get(privacyLevel);
    }
}






