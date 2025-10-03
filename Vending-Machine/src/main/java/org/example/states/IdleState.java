package org.example.states;

import org.example.system.VendingMachineContext;
import org.example.model.Product;

/**
 * IdleState represents the initial state of the vending machine.
 * In this state, the machine is ready to accept product selection.
 * Valid transitions: IdleState -> SelectingState
 */
public class IdleState extends AbstractState {

    public IdleState(VendingMachineContext context) {
        super(context);
    }

    @Override
    public void selectProduct(String productId) {
        System.out.println("\n[IDLE] Selecting product: " + productId);
        
        // Check if product exists and is available
        Product product = context.getInventory().getProduct(productId);
        
        if (product == null) {
            System.out.println("ERROR: Product '" + productId + "' not found.");
            context.notifyObservers("TRANSACTION_FAILED", productId, "Product not found");
            return;
        }
        
        if (!product.isAvailable()) {
            System.out.println("ERROR: Product '" + productId + "' is out of stock.");
            context.notifyObservers("TRANSACTION_FAILED", productId, "Out of stock");
            return;
        }
        
        // Create transaction
        context.createTransaction(productId);
        System.out.println("Product selected: " + product.getName() + " - " + product.getFormattedPrice());
        System.out.println("Please insert " + product.getFormattedPrice() + " (or use card/mobile payment)");
        
        // Notify observers
        context.notifyObservers("PRODUCT_SELECTED", productId);
        
        // Transition to SelectingState
        context.setCurrentState(new SelectingState(context));
    }

    @Override
    public String getStateName() {
        return "IDLE";
    }
}

