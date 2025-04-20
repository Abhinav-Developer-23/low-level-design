package org.lowLevelDesign.LowLevelDesign.InventoryManagementSystemAryan;

// Just-In-Time replenishment strategy
public class JustInTimeStrategy implements ReplenishmentStrategy {
    @Override
    public void replenish(Product product) {
        // Implement Just-In-Time replenishment logic
        System.out.println("Applying Just-In-Time replenishment for " + product.getName());
        
        // Calculate the optimal order quantity based on current threshold
        int currentQuantity = product.getQuantity();
        int threshold = product.getThreshold();
        int orderQuantity = threshold * 2 - currentQuantity; // Order to reach twice the threshold
        
        if (orderQuantity > 0) {
            System.out.println("Ordering " + orderQuantity + " units of " + product.getName() + 
                " to maintain optimal inventory level.");
            // Here you would typically integrate with an ordering system
        } else {
            System.out.println("No replenishment needed for " + product.getName() + 
                ". Current quantity is sufficient.");
        }
    }
} 