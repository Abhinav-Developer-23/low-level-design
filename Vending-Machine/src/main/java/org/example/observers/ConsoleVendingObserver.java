package org.example.observers;

import org.example.interfaces.VendingMachineObserver;

/**
 * Console observer that logs all vending machine events to the console.
 * Useful for debugging and monitoring machine operations.
 * Follows Observer Pattern and Single Responsibility Principle.
 */
public class ConsoleVendingObserver implements VendingMachineObserver {

    @Override
    public void onProductSelected(String productId) {
        log("PRODUCT_SELECTED", "Product " + productId + " has been selected");
    }

    @Override
    public void onCoinInserted(int coinValue) {
        log("COIN_INSERTED", coinValue + " cents inserted");
    }

    @Override
    public void onPaymentProcessed(int amount, String method) {
        log("PAYMENT_PROCESSED", 
            String.format("Payment of $%.2f processed using %s", amount / 100.0, method));
    }

    @Override
    public void onProductDispensed(String productId) {
        log("PRODUCT_DISPENSED", "Product " + productId + " has been dispensed");
    }

    @Override
    public void onTransactionCompleted(String transactionId) {
        log("TRANSACTION_COMPLETED", "Transaction " + transactionId.substring(0, 8) + " completed");
    }

    @Override
    public void onTransactionFailed(String transactionId, String reason) {
        String id = (transactionId != null && transactionId.length() >= 8) 
            ? transactionId.substring(0, 8) 
            : transactionId;
        log("TRANSACTION_FAILED", 
            "Transaction " + id + " failed: " + reason);
    }

    @Override
    public void onMaintenanceRequired(String message) {
        log("MAINTENANCE_REQUIRED", message);
    }

    /**
     * Logs an event to the console with formatting.
     * 
     * @param eventType the type of event
     * @param message the event message
     */
    private void log(String eventType, String message) {
        System.out.println("[OBSERVER:CONSOLE] [" + eventType + "] " + message);
    }
}

