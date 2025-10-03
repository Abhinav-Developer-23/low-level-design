package org.example.states;

import org.example.system.VendingMachineContext;
import org.example.model.Transaction;
import org.example.model.Product;

/**
 * DispensingState represents the state when the product is being dispensed.
 * In this state, the machine dispenses the product and returns change if needed.
 * Valid transitions: DispensingState -> IdleState
 */
public class DispensingState extends AbstractState {

    public DispensingState(VendingMachineContext context) {
        super(context);
    }

    @Override
    public void dispenseProduct() {
        Transaction transaction = context.getCurrentTransaction();
        if (transaction == null) {
            System.out.println("ERROR: No active transaction.");
            context.setCurrentState(new IdleState(context));
            return;
        }

        System.out.println("\n[DISPENSING] Dispensing product...");
        
        // Dispense product from inventory
        Product product = context.getInventory().dispenseProduct(transaction.getProductId());
        
        if (product != null) {
            System.out.println("[OK] Product dispensed: " + product.getName());
            System.out.println("[OK] Remaining stock: " + product.getQuantity());
            
            // Return change if any
            int change = transaction.getChangeAmount();
            if (change > 0) {
                context.returnChange(change);
            }
            
            // Notify observers
            context.notifyObservers("PRODUCT_DISPENSED", product.getId());
            context.notifyObservers("TRANSACTION_COMPLETED", transaction.getTransactionId());
            
            // Check for low stock and notify
            if (context.getInventory().isLowStock(product.getId())) {
                context.notifyObservers("MAINTENANCE_REQUIRED", 
                    "Low stock alert: " + product.getName() + " (ID: " + 
                    product.getId() + ", Quantity: " + product.getQuantity() + ")");
            }
            
            System.out.println("\n[OK] Transaction completed successfully!");
        } else {
            System.out.println("ERROR: Failed to dispense product. Refunding...");
            
            // Refund the payment
            if (transaction.needsRefund()) {
                context.returnChange(transaction.getTotalInsertedAmount());
            }
            
            context.notifyObservers("TRANSACTION_FAILED", 
                                  transaction.getTransactionId(), 
                                  "Product dispensing failed");
        }
        
        // Clear transaction and return to idle
        context.cancelCurrentTransaction();
        context.setCurrentState(new IdleState(context));
        System.out.println("Ready for next transaction.\n");
    }

    @Override
    public String getStateName() {
        return "DISPENSING";
    }
}

