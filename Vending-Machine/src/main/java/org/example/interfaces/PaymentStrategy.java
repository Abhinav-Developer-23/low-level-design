package org.example.interfaces;

import org.example.model.Transaction;

/**
 * Strategy Pattern: Interface for different payment processing strategies
 * Follows Interface Segregation Principle (ISP)
 */
public interface PaymentStrategy {
    /**
     * Process payment for a transaction
     * @param transaction The transaction to process
     * @return true if payment was successful, false otherwise
     */
    boolean processPayment(Transaction transaction);

    /**
     * Refund payment for a transaction
     * @param transaction The transaction to refund
     * @return true if refund was successful, false otherwise
     */
    boolean processRefund(Transaction transaction);
}
