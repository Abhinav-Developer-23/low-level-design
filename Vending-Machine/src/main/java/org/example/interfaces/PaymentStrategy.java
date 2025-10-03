package org.example.interfaces;

import org.example.model.Transaction;

/**
 * Strategy interface for different payment methods.
 * This follows the Strategy Pattern and allows different payment processing algorithms.
 * Also follows the Open/Closed Principle and Interface Segregation Principle.
 */
public interface PaymentStrategy {
    
    /**
     * Processes payment for the given transaction.
     * 
     * @param transaction the transaction to process
     * @return true if payment was successful, false otherwise
     */
    boolean processPayment(Transaction transaction);
    
    /**
     * Gets the name of the payment method.
     * 
     * @return the payment method name
     */
    String getPaymentMethod();
}

