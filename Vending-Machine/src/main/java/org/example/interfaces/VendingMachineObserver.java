package org.example.interfaces;

import org.example.enums.MachineState;
import org.example.enums.TransactionStatus;

/**
 * Observer interface for the Observer Pattern implementation.
 * Defines the contract for classes that want to observe vending machine events.
 */
public interface VendingMachineObserver {

    /**
     * Called when the machine state changes.
     * @param oldState The previous state
     * @param newState The new state
     */
    void onStateChange(MachineState oldState, MachineState newState);

    /**
     * Called when a transaction status changes.
     * @param transactionId The ID of the transaction
     * @param status The new transaction status
     */
    void onTransactionUpdate(String transactionId, TransactionStatus status);

    /**
     * Called when a product is dispensed.
     * @param slotId The slot ID of the dispensed product
     * @param productName The name of the dispensed product
     */
    void onProductDispensed(String slotId, String productName);

    /**
     * Called when coins are inserted.
     * @param coinType The type of coin inserted
     * @param amount The amount in cents
     */
    void onCoinInserted(Object coinType, int amount);

    /**
     * Called when maintenance is needed (e.g., low inventory).
     * @param message The maintenance message
     */
    void onMaintenanceAlert(String message);

    /**
     * Gets the name/identifier of this observer.
     * @return String identifier for the observer
     */
    String getObserverName();
}
