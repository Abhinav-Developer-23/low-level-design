package org.example.interfaces;

/**
 * Observer interface for the Observer Design Pattern.
 * Allows different components to be notified of vending machine events.
 * Follows Observer Pattern and Interface Segregation Principle.
 */
public interface VendingMachineObserver {
    
    /**
     * Notifies when a product is selected.
     * 
     * @param productId the ID of the selected product
     */
    void onProductSelected(String productId);
    
    /**
     * Notifies when a coin is inserted.
     * 
     * @param coinValue the value of the inserted coin in cents
     */
    void onCoinInserted(int coinValue);
    
    /**
     * Notifies when payment is processed.
     * 
     * @param amount the payment amount in cents
     * @param method the payment method used
     */
    void onPaymentProcessed(int amount, String method);
    
    /**
     * Notifies when a product is dispensed.
     * 
     * @param productId the ID of the dispensed product
     */
    void onProductDispensed(String productId);
    
    /**
     * Notifies when a transaction is completed.
     * 
     * @param transactionId the ID of the completed transaction
     */
    void onTransactionCompleted(String transactionId);
    
    /**
     * Notifies when a transaction fails.
     * 
     * @param transactionId the ID of the failed transaction
     * @param reason the reason for failure
     */
    void onTransactionFailed(String transactionId, String reason);
    
    /**
     * Notifies when maintenance is required.
     * 
     * @param message the maintenance message
     */
    void onMaintenanceRequired(String message);
}

