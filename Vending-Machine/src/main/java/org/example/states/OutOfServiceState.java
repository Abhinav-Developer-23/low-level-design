package org.example.states;

import org.example.interfaces.State;

/**
 * Out of Service State: Machine is in maintenance mode.
 * Only allows exiting service mode, all other operations are blocked.
 */
public class OutOfServiceState implements State {

    @Override
    public void insertCoin(Object machine, Object coinType) {
        System.out.println("Machine is out of service - cannot insert coins");
    }

    @Override
    public void selectProduct(Object machine, String slotId) {
        System.out.println("Machine is out of service - cannot select products");
    }

    @Override
    public boolean processPayment(Object machine) {
        System.out.println("Machine is out of service - cannot process payments");
        return false;
    }

    @Override
    public void dispenseProduct(Object machine) {
        System.out.println("Machine is out of service - cannot dispense products");
    }

    @Override
    public void cancelTransaction(Object machine) {
        System.out.println("Machine is out of service - cannot cancel transactions");
    }

    @Override
    public void setServiceMode(Object machine, boolean inService) {
        if (!inService) {
            System.out.println("Exiting service mode...");
        } else {
            System.out.println("Already in service mode");
        }
    }
}
