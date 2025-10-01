package org.example.interfaces;

/**
 * Interface Segregation Principle:
 * Separate interface for entities that can be dispensed from vending machine
 */
public interface Dispensable {
    /**
     * Dispense the item
     * @return true if dispensing was successful, false otherwise
     */
    boolean dispense();

    /**
     * Check if the item is available for dispensing
     * @return true if available, false otherwise
     */
    boolean isAvailable();

    /**
     * Get the name of the dispensable item
     * @return item name
     */
    String getName();
}
