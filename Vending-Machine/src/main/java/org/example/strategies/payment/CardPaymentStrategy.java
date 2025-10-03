package org.example.strategies.payment;

import org.example.enums.PaymentMethod;
import org.example.interfaces.PaymentStrategy;

/**
 * Card payment strategy - credit/debit cards
 */
public class CardPaymentStrategy implements PaymentStrategy {
    
    @Override
    public boolean processPayment(double amount) {
        if (amount <= 0) {
            return false;
        }
        
        System.out.println("💳 Processing card payment: $" + String.format("%.2f", amount));
        
        // Simulate card processing
        try {
            Thread.sleep(300); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        
        // In real implementation:
        // 1. Connect to payment gateway
        // 2. Validate card
        // 3. Process transaction
        // 4. Wait for approval
        
        // For demo, we simulate 95% success rate
        boolean success = Math.random() < 0.95;
        
        if (success) {
            System.out.println("✓ Card payment approved");
        } else {
            System.out.println("❌ Card payment declined");
        }
        
        return success;
    }

    @Override
    public boolean refundPayment(double amount) {
        if (amount <= 0) {
            return true;
        }
        
        System.out.println("💳 Processing card refund: $" + String.format("%.2f", amount));
        
        // In real implementation, this would reverse the card transaction
        return true;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CARD;
    }

    @Override
    public String getPaymentMethodName() {
        return "Credit/Debit Card";
    }
}

