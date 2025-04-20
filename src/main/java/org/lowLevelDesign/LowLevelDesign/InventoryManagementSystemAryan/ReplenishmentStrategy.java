package org.lowLevelDesign.LowLevelDesign.InventoryManagementSystemAryan;

// Interface for different replenishment strategies
public interface ReplenishmentStrategy {
    // Method to replenish stock for a given product
    void replenish(Product product);
} 