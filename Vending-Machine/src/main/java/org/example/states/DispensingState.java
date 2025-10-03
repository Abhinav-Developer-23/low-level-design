package org.example.states;

import org.example.enums.TransactionStatus;
import org.example.interfaces.State;
import org.example.model.Product;
import org.example.system.VendingMachineContext;

/**
 * Concrete state class representing the product dispensing state.
 * In this state, the product is being dispensed to the customer.
 */
public class DispensingState implements State {
    private final VendingMachineContext context;

    public DispensingState(VendingMachineContext context) {
        this.context = context;
    }

    @Override
    public void selectProduct(String productId) {
        System.out.println("Product already being dispensed. Please wait.");
    }

    @Override
    public void insertCoin(int coinValue) {
        System.out.println("Product dispensing in progress. Cannot accept coins.");
    }

    @Override
    public void processPayment() {
        System.out.println("Payment already processed. Dispensing product...");
    }

    @Override
    public void dispenseProduct() {
        try {
            String productId = context.getCurrentTransaction().getProductId();

            // Dispense the product from inventory
            Product dispensedProduct = context.getInventory().dispenseProduct(productId);

            if (dispensedProduct != null) {
                context.getCurrentTransaction().setStatus(TransactionStatus.COMPLETED);

                // Calculate and return change if any
                int change = context.getCurrentTransaction().getChangeAmount();
                if (change > 0) {
                    System.out.println("Dispensing product: " + dispensedProduct.getName());
                    System.out.println("Returning change: " + change + " cents");
                    context.returnChange(change);
                } else {
                    System.out.println("Dispensing product: " + dispensedProduct.getName());
                }

                // Notify observers
                context.notifyObservers("Product dispensed: " + dispensedProduct.getName());

                System.out.println("Transaction completed successfully!");
            } else {
                context.getCurrentTransaction().setStatus(TransactionStatus.FAILED);
                System.out.println("Failed to dispense product. Please contact support.");
            }

            // Return to idle state
            context.setCurrentState(new IdleState(context));

        } catch (Exception e) {
            context.getCurrentTransaction().setStatus(TransactionStatus.FAILED);
            System.err.println("Error dispensing product: " + e.getMessage());
            context.setCurrentState(new IdleState(context));
        }
    }

    @Override
    public void cancelTransaction() {
        System.out.println("Cannot cancel during dispensing. Please wait for completion.");
    }

    @Override
    public String getStateName() {
        return "DISPENSING";
    }
}
