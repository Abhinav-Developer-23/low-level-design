package org.example.strategies.payment;

import org.example.interfaces.PaymentStrategy;
import org.example.model.Transaction;

/**
 * Concrete strategy for handling mobile payments (Apple Pay, Google Pay, etc.).
 */
public class MobilePaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(Transaction transaction) {
        try {
            // In a real system, this would integrate with mobile payment APIs
            System.out.println("Processing mobile payment for " + transaction.getProductId());
            System.out.println("Initiating mobile payment session...");

            // Simulate mobile payment processing
            Thread.sleep(1500);

            // Simulate QR code display or NFC interaction
            System.out.println("Please complete payment on your mobile device...");

            // Simulate waiting for payment confirmation
            Thread.sleep(2000);

            // Simulate successful payment (85% success rate)
            boolean confirmed = Math.random() > 0.15;

            if (confirmed) {
                System.out.println("Mobile payment confirmed for " + transaction.getProductPrice() + " cents");
                return true;
            } else {
                System.out.println("Mobile payment timed out or failed. Please try again.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error processing mobile payment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getPaymentMethod() {
        return "MOBILE";
    }
}
