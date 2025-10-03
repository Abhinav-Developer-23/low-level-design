package org.example.observers;

import org.example.interfaces.VendingMachineObserver;

/**
 * Concrete observer that monitors vending machine status for maintenance needs.
 */
public class MaintenanceObserver implements VendingMachineObserver {
    private static final int LOW_INVENTORY_THRESHOLD = 5;
    private static final int CRITICAL_INVENTORY_THRESHOLD = 2;

    @Override
    public void onProductSelected(String productId) {
        // Maintenance observer doesn't need to react to product selection
    }

    @Override
    public void onCoinInserted(int coinValue) {
        // Maintenance observer doesn't need to react to coin insertion
    }

    @Override
    public void onPaymentProcessed(int amount, String method) {
        // Maintenance observer doesn't need to react to payment processing
    }

    @Override
    public void onProductDispensed(String productId) {
        // Could trigger inventory check after dispensing
        checkInventoryLevels();
    }

    @Override
    public void onTransactionCompleted(String transactionId) {
        // Could trigger general maintenance check after transaction
        checkOverallStatus();
    }

    @Override
    public void onTransactionFailed(String transactionId, String reason) {
        // Log transaction failures for maintenance review
        System.out.println("[MAINTENANCE] Transaction failure logged: " + transactionId + " - " + reason);
    }

    @Override
    public void onMaintenanceRequired(String message) {
        System.out.println("[MAINTENANCE ALERT] " + message);
        // In a real system, this could trigger notifications to maintenance staff
        sendMaintenanceNotification(message);
    }

    private void checkInventoryLevels() {
        // This would need access to the vending machine's inventory
        // For now, we'll simulate the check
        System.out.println("[MAINTENANCE] Checking inventory levels...");
    }

    private void checkOverallStatus() {
        // This would check various machine components
        System.out.println("[MAINTENANCE] Checking overall machine status...");
    }

    private void sendMaintenanceNotification(String message) {
        // In a real system, this could send emails, SMS, or integrate with maintenance systems
        System.out.println("[MAINTENANCE] Notification sent to maintenance team: " + message);
    }
}
