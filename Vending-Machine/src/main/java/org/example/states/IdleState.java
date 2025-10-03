package org.example.states;

import org.example.interfaces.State;
import org.example.system.VendingMachineContext;

/**
 * Concrete state class representing the idle state of the vending machine.
 * In this state, the machine is ready for product selection.
 */
public class IdleState implements State {
    private final VendingMachineContext context;

    public IdleState(VendingMachineContext context) {
        this.context = context;
    }

    @Override
    public void selectProduct(String productId) {
        if (context.getInventory().isProductAvailable(productId)) {
            context.setCurrentTransaction(
                context.createTransaction(productId)
            );
            context.setCurrentState(new SelectingState(context));
            System.out.println("Product " + productId + " selected. Please proceed to payment.");
        } else {
            System.out.println("Product " + productId + " is not available.");
        }
    }

    @Override
    public void insertCoin(int coinValue) {
        System.out.println("Please select a product first before inserting coins.");
    }

    @Override
    public void processPayment() {
        System.out.println("No product selected. Please select a product first.");
    }

    @Override
    public void dispenseProduct() {
        System.out.println("No product to dispense. Please select a product first.");
    }

    @Override
    public void cancelTransaction() {
        System.out.println("No active transaction to cancel.");
    }

    @Override
    public String getStateName() {
        return "IDLE";
    }
}
