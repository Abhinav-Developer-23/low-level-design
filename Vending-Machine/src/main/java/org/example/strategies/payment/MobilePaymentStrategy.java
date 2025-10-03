package org.example.strategies.payment;

import org.example.interfaces.PaymentStrategy;
import org.example.model.Transaction;
import java.util.Random;

/**
 * Mobile payment strategy that simulates mobile wallet processing (e.g., Apple Pay, Google Pay).
 * Simulates real-world scenario with occasional failures (15% failure rate).
 * Follows Strategy Pattern and Single Responsibility Principle.
 */
public class MobilePaymentStrategy implements PaymentStrategy {
    private static final double SUCCESS_RATE = 0.85; // 85% success rate
    private final Random random;

    public MobilePaymentStrategy() {
        this.random = new Random();
    }

    @Override
    public boolean processPayment(Transaction transaction) {
        System.out.println("Processing mobile payment...");
        System.out.println("Amount to charge: $" + 
            String.format("%.2f", transaction.getProductPrice() / 100.0));
        
        // Simulate mobile payment processing delay
        try {
            System.out.println("Connecting to mobile payment service...");
            Thread.sleep(700); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate mobile payment authorization (85% success rate)
        boolean authorized = random.nextDouble() < SUCCESS_RATE;
        
        if (authorized) {
            System.out.println("[OK] Mobile payment successful!");
            System.out.println("Payment confirmed on your device");
            System.out.println("Transaction ID: " + transaction.getTransactionId().substring(0, 8));
            return true;
        } else {
            System.out.println("[FAIL] Mobile payment failed. Please try again or use another payment method.");
            return false;
        }
    }

    @Override
    public String getPaymentMethod() {
        return "MOBILE";
    }
}

