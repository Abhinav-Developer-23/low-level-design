package org.example.interfaces;

import org.example.enums.PaymentMethod;

/**
 * Strategy Pattern: Different payment processing strategies
 */
public interface PaymentStrategy {
    /**
     * Process payment
     * @param amount Amount to charge
     * @return true if payment successful
     */
    boolean processPayment(double amount);
    
    /**
     * Process refund
     * @param amount Amount to refund
     * @return true if refund successful
     */
    boolean refundPayment(double amount);
    
    /**
     * Get payment method type
     */
    PaymentMethod getPaymentMethod();
    
    /**
     * Get display name for this payment method
     */
    String getPaymentMethodName();
}

