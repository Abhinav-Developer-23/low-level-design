package org.example.states;

import org.example.enums.MachineState;
import org.example.enums.TransactionStatus;
import org.example.interfaces.State;
import org.example.model.Coin;

/**
 * Idle State: Machine is ready for product selection.
 * This is the initial state where users can insert coins or select products.
 */
public class IdleState implements State {

    @Override
    public void insertCoin(Object machine, Object coinType) {
        // For now, we'll implement this when we have the context class
        System.out.println("Coin inserted in Idle state: " + coinType);
    }

    @Override
    public void selectProduct(Object machine, String slotId) {
        System.out.println("Product selected in Idle state: " + slotId);
    }

    @Override
    public boolean processPayment(Object machine) {
        System.out.println("Cannot process payment in Idle state - no product selected");
        return false;
    }

    @Override
    public void dispenseProduct(Object machine) {
        System.out.println("Cannot dispense product in Idle state");
    }

    @Override
    public void cancelTransaction(Object machine) {
        System.out.println("No transaction to cancel in Idle state");
    }

    @Override
    public void setServiceMode(Object machine, boolean inService) {
        System.out.println("Setting service mode: " + inService);
    }
}
