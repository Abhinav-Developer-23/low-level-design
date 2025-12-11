package org.example.quickcommerce.payment;

import org.example.quickcommerce.model.Payment;

/**
 * Strategy interface for payment processing.
 */
public interface PaymentStrategy {
    boolean processPayment(Payment payment);
    boolean validatePayment(Payment payment);
}

