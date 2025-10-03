package org.example.states;

import org.example.interfaces.State;

/**
 * Payment State: Processing payment for selected product.
 * Payment validation and processing occurs in this state.
 */
public class PaymentState implements State {

    @Override
    public void insertCoin(Object machine, Object coinType) {
        System.out.println("Cannot insert coins during payment processing");
    }

    @Override
    public void selectProduct(Object machine, String slotId) {
        System.out.println("Cannot change product selection during payment");
    }

    @Override
    public boolean processPayment(Object machine) {
        System.out.println("Processing payment...");
        // Simulate payment processing
        try {
            Thread.sleep(1000); // Simulate processing time
            System.out.println("Payment processed successfully");
            return true;
        } catch (InterruptedException e) {
            System.out.println("Payment processing interrupted");
            return false;
        }
    }

    @Override
    public void dispenseProduct(Object machine) {
        System.out.println("Dispensing product after payment");
    }

    @Override
    public void cancelTransaction(Object machine) {
        System.out.println("Cancelling transaction during payment - refund will be processed");
    }

    @Override
    public void setServiceMode(Object machine, boolean inService) {
        System.out.println("Cannot enter service mode during payment processing");
    }
}
