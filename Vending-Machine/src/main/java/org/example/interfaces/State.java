package org.example.interfaces;

/**
 * State interface for the State Pattern implementation.
 * Defines the behavior that each concrete state must implement.
 */
public interface State {

    /**
     * Inserts a coin into the vending machine.
     * @param machine The vending machine context
     * @param coinType The type of coin being inserted
     */
    void insertCoin(Object machine, Object coinType);

    /**
     * Selects a product by slot ID.
     * @param machine The vending machine context
     * @param slotId The slot identifier (e.g., "A1", "B2")
     */
    void selectProduct(Object machine, String slotId);

    /**
     * Processes the payment for the selected product.
     * @param machine The vending machine context
     * @return true if payment was successful, false otherwise
     */
    boolean processPayment(Object machine);

    /**
     * Dispenses the selected product and returns change.
     * @param machine The vending machine context
     */
    void dispenseProduct(Object machine);

    /**
     * Cancels the current transaction and refunds money.
     * @param machine The vending machine context
     */
    void cancelTransaction(Object machine);

    /**
     * Sets the machine to service/maintenance mode.
     * @param machine The vending machine context
     * @param inService true to enter service mode, false to exit
     */
    void setServiceMode(Object machine, boolean inService);
}
