package org.example.interfaces;

/**
 * Observer interface for vending machine events and notifications.
 */
public interface VendingMachineObserver {
    /**
     * Called when a product is selected.
     *
     * @param productId The ID of the selected product
     */
    void onProductSelected(String productId);

    /**
     * Called when a coin is inserted.
     *
     * @param coinValue The value of the inserted coin
     */
    void onCoinInserted(int coinValue);

    /**
     * Called when payment is processed.
     *
     * @param amount The payment amount
     * @param method The payment method used
     */
    void onPaymentProcessed(int amount, String method);

    /**
     * Called when a product is dispensed.
     *
     * @param productId The ID of the dispensed product
     */
    void onProductDispensed(String productId);

    /**
     * Called when a transaction is completed.
     *
     * @param transactionId The ID of the completed transaction
     */
    void onTransactionCompleted(String transactionId);

    /**
     * Called when a transaction fails.
     *
     * @param transactionId The ID of the failed transaction
     * @param reason The reason for failure
     */
    void onTransactionFailed(String transactionId, String reason);

    /**
     * Called when the vending machine needs maintenance (low inventory, etc.).
     *
     * @param message The maintenance message
     */
    void onMaintenanceRequired(String message);
}
