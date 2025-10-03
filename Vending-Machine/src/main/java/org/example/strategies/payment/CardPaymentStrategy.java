package org.example.strategies.payment;

import org.example.interfaces.PaymentStrategy;
import org.example.model.Transaction;
import java.util.Random;

/**
 * Card payment strategy that simulates credit/debit card processing.
 * Simulates real-world scenario with occasional failures (10% failure rate).
 * Follows Strategy Pattern and Single Responsibility Principle.
 */
public class CardPaymentStrategy implements PaymentStrategy {
    private static final double SUCCESS_RATE = 0.90; // 90% success rate
    private final Random random;

    public CardPaymentStrategy() {
        this.random = new Random();
    }

    @Override
    public boolean processPayment(Transaction transaction) {
        System.out.println("Processing card payment...");
        System.out.println("Amount to charge: $" + 
            String.format("%.2f", transaction.getProductPrice() / 100.0));
        
        // Simulate card authorization delay
        try {
            System.out.println("Authorizing card...");
            Thread.sleep(500); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate card authorization (90% success rate)
        boolean authorized = random.nextDouble() < SUCCESS_RATE;
        
        if (authorized) {
            System.out.println("[OK] Card authorized successfully!");
            System.out.println("Transaction ID: " + transaction.getTransactionId().substring(0, 8));
            return true;
        } else {
            System.out.println("[FAIL] Card declined. Please try another payment method.");
            return false;
        }
    }

    @Override
    public String getPaymentMethod() {
        return "CARD";
    }
}

