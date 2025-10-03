package org.example.observers;

import org.example.interfaces.VendingMachineObserver;

/**
 * Concrete observer that outputs vending machine events to the console.
 */
public class ConsoleVendingObserver implements VendingMachineObserver {

    @Override
    public void onProductSelected(String productId) {
        System.out.println("[OBSERVER] Product selected: " + productId);
    }

    @Override
    public void onCoinInserted(int coinValue) {
        System.out.println("[OBSERVER] Coin inserted: " + coinValue + " cents");
    }

    @Override
    public void onPaymentProcessed(int amount, String method) {
        System.out.println("[OBSERVER] Payment processed: " + amount + " cents via " + method);
    }

    @Override
    public void onProductDispensed(String productId) {
        System.out.println("[OBSERVER] Product dispensed: " + productId);
    }

    @Override
    public void onTransactionCompleted(String transactionId) {
        System.out.println("[OBSERVER] Transaction completed: " + transactionId);
    }

    @Override
    public void onTransactionFailed(String transactionId, String reason) {
        System.out.println("[OBSERVER] Transaction failed: " + transactionId + " - Reason: " + reason);
    }

    @Override
    public void onMaintenanceRequired(String message) {
        System.out.println("[OBSERVER] Maintenance required: " + message);
    }
}
