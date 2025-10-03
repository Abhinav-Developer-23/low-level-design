package org.example.interfaces;

import org.example.model.Product;

/**
 * State Pattern: Interface for all vending machine states
 * Each state handles operations differently based on current machine state
 */
public interface State {
    /**
     * Handle product selection
     */
    void selectProduct(Product product);
    
    /**
     * Handle coin/cash insertion
     */
    void insertCoin(double amount);
    
    /**
     * Handle card payment
     */
    void insertCard(String cardNumber, double amount);
    
    /**
     * Handle mobile payment
     */
    void insertMobilePayment(String paymentId, double amount);
    
    /**
     * Dispense the product
     */
    void dispenseProduct();
    
    /**
     * Cancel current transaction
     */
    void cancel();
    
    /**
     * Get the name of current state
     */
    String getStateName();
}

