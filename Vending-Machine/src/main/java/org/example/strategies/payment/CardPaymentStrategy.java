package org.example.strategies.payment;

import org.example.interfaces.PaymentStrategy;
import org.example.model.Transaction;

/**
 * Concrete strategy for handling card payments.
 */
public class CardPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(Transaction transaction) {
        try {
            // In a real system, this would integrate with a card payment processor
            // For this demo, we'll simulate card validation
            System.out.println("Processing card payment for " + transaction.getProductId());
            System.out.println("Card authorization in progress...");

            // Simulate card processing delay
            Thread.sleep(1000);

            // Simulate successful authorization (90% success rate)
            boolean authorized = Math.random() > 0.1;

            if (authorized) {
                System.out.println("Card payment authorized for " + transaction.getProductPrice() + " cents");
                return true;
            } else {
                System.out.println("Card payment declined. Please try another payment method.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error processing card payment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getPaymentMethod() {
        return "CARD";
    }
}
