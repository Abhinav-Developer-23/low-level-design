package org.example.states;

import org.example.interfaces.State;

/**
 * Dispensing State: Dispensing product and returning change.
 * Final state in the transaction process.
 */
public class DispensingState implements State {

    @Override
    public void insertCoin(Object machine, Object coinType) {
        System.out.println("Cannot insert coins during dispensing");
    }

    @Override
    public void selectProduct(Object machine, String slotId) {
        System.out.println("Cannot select products during dispensing");
    }

    @Override
    public boolean processPayment(Object machine) {
        System.out.println("Payment already processed");
        return false;
    }

    @Override
    public void dispenseProduct(Object machine) {
        System.out.println("Dispensing product...");
        // Simulate dispensing time
        try {
            Thread.sleep(2000);
            System.out.println("Product dispensed successfully!");
            System.out.println("Returning change...");
            Thread.sleep(1000);
            System.out.println("Change returned. Thank you!");
        } catch (InterruptedException e) {
            System.out.println("Dispensing interrupted");
        }
    }

    @Override
    public void cancelTransaction(Object machine) {
        System.out.println("Cannot cancel transaction during dispensing");
    }

    @Override
    public void setServiceMode(Object machine, boolean inService) {
        System.out.println("Cannot enter service mode during dispensing");
    }
}
