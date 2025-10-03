package org.example.interfaces;

/**
 * State interface for the State Design Pattern.
 * Each concrete state implements this interface to define behavior in that state.
 * This follows the Open/Closed Principle - open for extension, closed for modification.
 */
public interface State {
    
    /**
     * Handles product selection in the current state.
     * 
     * @param productId the ID of the product to select
     */
    void selectProduct(String productId);
    
    /**
     * Handles coin insertion in the current state.
     * 
     * @param coinValue the value of the coin in cents
     */
    void insertCoin(int coinValue);
    
    /**
     * Processes payment in the current state.
     */
    void processPayment();
    
    /**
     * Dispenses the product in the current state.
     */
    void dispenseProduct();
    
    /**
     * Cancels the current transaction and refunds money.
     */
    void cancelTransaction();
    
    /**
     * Gets the name of the current state.
     * 
     * @return the state name
     */
    String getStateName();
}

