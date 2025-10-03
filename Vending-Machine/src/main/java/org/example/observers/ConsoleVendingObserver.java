package org.example.observers;

import org.example.enums.CoinType;
import org.example.enums.MachineState;
import org.example.enums.TransactionStatus;
import org.example.interfaces.VendingMachineObserver;

/**
 * Console Vending Observer: Logs all vending machine events to console.
 * Provides real-time monitoring and debugging information.
 */
public class ConsoleVendingObserver implements VendingMachineObserver {

    @Override
    public void onStateChange(MachineState oldState, MachineState newState) {
        System.out.println("[OBSERVER] Machine state changed: " + oldState + " → " + newState);
    }

    @Override
    public void onTransactionUpdate(String transactionId, TransactionStatus status) {
        System.out.println("[OBSERVER] Transaction " + transactionId + " status: " + status);
    }

    @Override
    public void onProductDispensed(String slotId, String productName) {
        System.out.println("[OBSERVER] Product dispensed: " + productName + " from slot " + slotId);
    }

    @Override
    public void onCoinInserted(Object coinType, int amount) {
        if (coinType instanceof CoinType) {
            CoinType coin = (CoinType) coinType;
            System.out.println("[OBSERVER] Coin inserted: " + coin + " ($" + String.format("%.2f", amount / 100.0) + ")");
        } else {
            System.out.println("[OBSERVER] Coin inserted: " + coinType + " ($" + String.format("%.2f", amount / 100.0) + ")");
        }
    }

    @Override
    public void onMaintenanceAlert(String message) {
        System.out.println("[MAINTENANCE ALERT] " + message);
    }

    @Override
    public String getObserverName() {
        return "Console Observer";
    }

    /**
     * Logs a custom event message.
     * @param message The event message to log
     */
    public void logEvent(String message) {
        System.out.println("[EVENT] " + message);
    }

    /**
     * Logs an error message.
     * @param error The error message to log
     */
    public void logError(String error) {
        System.err.println("[ERROR] " + error);
    }

    /**
     * Logs a warning message.
     * @param warning The warning message to log
     */
    public void logWarning(String warning) {
        System.out.println("[WARNING] " + warning);
    }
}
