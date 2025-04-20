package org.lowLevelDesign.LowLevelDesign.InventoryManagementSystemAryan;

// Bulk Order replenishment strategy
public class BulkOrderStrategy implements ReplenishmentStrategy {
    private final int bulkOrderMultiplier;
    
    // Default constructor with a default multiplier
    public BulkOrderStrategy() {
        this.bulkOrderMultiplier = 5; // Default to ordering 5x the threshold
    }
    
    // Constructor with custom multiplier
    public BulkOrderStrategy(int bulkOrderMultiplier) {
        if (bulkOrderMultiplier <= 0) {
            throw new IllegalArgumentException("Bulk order multiplier must be positive");
        }
        this.bulkOrderMultiplier = bulkOrderMultiplier;
    }
    
    @Override
    public void replenish(Product product) {
        // Implement Bulk Order replenishment logic
        System.out.println("Applying Bulk Order replenishment for " + product.getName());
        
        int currentQuantity = product.getQuantity();
        int threshold = product.getThreshold();
        
        // Only order if below threshold
        if (currentQuantity < threshold) {
            int orderQuantity = threshold * bulkOrderMultiplier;
            
            System.out.println("Bulk ordering " + orderQuantity + " units of " + product.getName() + 
                " to minimize order costs.");
            // Here you would typically integrate with an ordering system
        } else {
            System.out.println("No bulk replenishment needed for " + product.getName() + 
                ". Current quantity is above threshold.");
        }
    }
} 