package org.example.interfaces;

/**
 * Interface for items that can be dispensed
 */
public interface Dispensable {
    /**
     * Get unique identifier
     */
    String getId();
    
    /**
     * Get name of the item
     */
    String getName();
    
    /**
     * Get price of the item
     */
    double getPrice();
    
    /**
     * Check if item is available
     */
    boolean isAvailable();
}

