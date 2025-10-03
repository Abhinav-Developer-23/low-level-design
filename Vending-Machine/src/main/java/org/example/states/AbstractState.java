package org.example.states;

import org.example.interfaces.State;
import org.example.system.VendingMachineContext;

/**
 * Abstract base class for all states.
 * Provides default implementations that throw exceptions for invalid operations.
 * Concrete states override only the methods that are valid in their state.
 * This follows the Template Method pattern and reduces code duplication.
 */
public abstract class AbstractState implements State {
    protected VendingMachineContext context;

    public AbstractState(VendingMachineContext context) {
        this.context = context;
    }

    @Override
    public void selectProduct(String productId) {
        System.out.println("Cannot select product in " + getStateName() + " state.");
    }

    @Override
    public void insertCoin(int coinValue) {
        System.out.println("Cannot insert coin in " + getStateName() + " state.");
    }

    @Override
    public void processPayment() {
        System.out.println("Cannot process payment in " + getStateName() + " state.");
    }

    @Override
    public void dispenseProduct() {
        System.out.println("Cannot dispense product in " + getStateName() + " state.");
    }

    @Override
    public void cancelTransaction() {
        System.out.println("Cannot cancel transaction in " + getStateName() + " state.");
    }
}

