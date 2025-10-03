package org.example.interfaces;

/**
 * Product Selection Strategy interface for the Strategy Pattern implementation.
 * Defines different ways to select products from the vending machine.
 */
public interface ProductSelectionStrategy {

    /**
     * Selects a product based on the given criteria.
     * @param inventory The inventory to search in
     * @param criteria The selection criteria (slot ID, name, etc.)
     * @return The slot ID of the selected product, or null if not found
     */
    String selectProduct(Object inventory, String criteria);

    /**
     * Gets the name of the selection strategy.
     * @return String representation of the selection method
     */
    String getStrategyName();
}
