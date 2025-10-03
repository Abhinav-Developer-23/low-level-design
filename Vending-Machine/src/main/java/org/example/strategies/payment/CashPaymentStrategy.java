package org.example.strategies.payment;

import org.example.enums.PaymentMethod;
import org.example.interfaces.PaymentStrategy;

/**
 * Cash payment strategy - coins and bills
 */
public class CashPaymentStrategy implements PaymentStrategy {
    
    @Override
    public boolean processPayment(double amount) {
        // Validate cash payment
        if (amount <= 0) {
            return false;
        }
        
        System.out.println("💵 Processing cash payment: $" + String.format("%.2f", amount));
        
        // In real implementation, this would interface with bill/coin acceptor hardware
        // For demo, we always succeed
        return true;
    }

    @Override
    public boolean refundPayment(double amount) {
        if (amount <= 0) {
            return true; // Nothing to refund
        }
        
        System.out.println("💵 Dispensing cash refund: $" + String.format("%.2f", amount));
        
        // In real implementation, this would dispense coins/bills
        return true;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CASH;
    }

    @Override
    public String getPaymentMethodName() {
        return "Cash";
    }
}

