package org.example.states;

import org.example.interfaces.State;
import org.example.model.Product;
import org.example.system.VendingMachineContext;

/**
 * Idle State: Machine is waiting for customer interaction
 * Valid operations: selectProduct
 * Invalid operations: insertCoin, insertCard, dispenseProduct
 */
public class IdleState implements State {
    private final VendingMachineContext context;

    public IdleState(VendingMachineContext context) {
        this.context = context;
    }

    @Override
    public void selectProduct(Product product) {
        if (product == null) {
            System.out.println("❌ Invalid product");
            return;
        }

        // Check if product is in inventory and available
        if (!context.getInventory().isAvailable(product.getProductId())) {
            System.out.println("❌ Product '" + product.getName() + "' is out of stock");
            context.notifyProductOutOfStock(product);
            return;
        }

        // Valid operation in Idle state
        System.out.println("✓ Product selected: " + product.getName() + " - $" + product.getPrice());
        context.setSelectedProduct(product);
        context.setTotalPaid(0.0);
        
        // Transition to Selecting state
        context.setState(new SelectingState(context));
        System.out.println("💡 Please insert payment. Required: $" + product.getPrice());
    }

    @Override
    public void insertCoin(double amount) {
        System.out.println("❌ Please select a product first");
    }

    @Override
    public void insertCard(String cardNumber, double amount) {
        System.out.println("❌ Please select a product first");
    }

    @Override
    public void insertMobilePayment(String paymentId, double amount) {
        System.out.println("❌ Please select a product first");
    }

    @Override
    public void dispenseProduct() {
        System.out.println("❌ No product selected");
    }

    @Override
    public void cancel() {
        System.out.println("❌ Nothing to cancel");
    }

    @Override
    public String getStateName() {
        return "IDLE";
    }
}

