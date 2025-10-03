package org.example.states;

import org.example.enums.TransactionStatus;
import org.example.interfaces.State;

/**
 * Selecting State: Product has been selected, awaiting payment.
 * Users can insert coins, change selection, or cancel transaction.
 */
public class SelectingState implements State {

    @Override
    public void insertCoin(Object machine, Object coinType) {
        System.out.println("Coin inserted in Selecting state: " + coinType);
    }

    @Override
    public void selectProduct(Object machine, String slotId) {
        System.out.println("Changing product selection to: " + slotId);
    }

    @Override
    public boolean processPayment(Object machine) {
        System.out.println("Processing payment in Selecting state");
        return true;
    }

    @Override
    public void dispenseProduct(Object machine) {
        System.out.println("Cannot dispense product yet - payment not processed");
    }

    @Override
    public void cancelTransaction(Object machine) {
        System.out.println("Cancelling transaction in Selecting state");
    }

    @Override
    public void setServiceMode(Object machine, boolean inService) {
        System.out.println("Cannot enter service mode during active transaction");
    }
}
