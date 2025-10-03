package org.example.interfaces;

/**
 * Interface for state design pattern implementation.
 * Defines the behavior for different states of the vending machine.
 */
public interface State {
    /**
     * Handle product selection in the current state.
     */
    void selectProduct(String productId);

    /**
     * Handle coin insertion in the current state.
     */
    void insertCoin(int coinValue);

    /**
     * Handle payment processing in the current state.
     */
    void processPayment();

    /**
     * Handle product dispensing in the current state.
     */
    void dispenseProduct();

    /**
     * Handle transaction cancellation in the current state.
     */
    void cancelTransaction();

    /**
     * Get the current state name.
     */
    String getStateName();
}
