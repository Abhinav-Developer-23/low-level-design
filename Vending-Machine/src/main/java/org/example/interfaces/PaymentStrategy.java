package org.example.interfaces;

import org.example.model.Transaction;

/**
 * Strategy interface for handling different payment methods in the vending machine.
 */
public interface PaymentStrategy {
    /**
     * Process the payment for the given transaction.
     *
     * @param transaction The transaction to process payment for
     * @return true if payment was successful, false otherwise
     */
    boolean processPayment(Transaction transaction);

    /**
     * Get the payment method supported by this strategy.
     *
     * @return The payment method
     */
    String getPaymentMethod();
}
