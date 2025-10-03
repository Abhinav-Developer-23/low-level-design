package org.example.states;

import org.example.enums.Coin;
import org.example.interfaces.State;
import org.example.system.VendingMachine;

import java.util.Map;

public class OutOfServiceState implements State {
    private final VendingMachine machine;

    public OutOfServiceState(VendingMachine m) {
        this.machine = m;
    }

    @Override
    public void insertCoin(Coin coin) {
        System.out.println("Machine out of service. Can't accept coins.");
    }

    @Override
    public void selectItem(String itemCode) {
        System.out.println("Machine out of service.");
    }

    @Override
    public void dispense() {
        System.out.println("Machine out of service.");
    }

    @Override
    public void refund() {
        int bal = machine.getBalanceCents();
        if (bal > 0) {
            System.out.println("Out of service — refunding balance.");
            Map<Coin, Integer> change = machine.makeChange(bal);
            System.out.printf("Refunded $%.2f as %s\n", bal / 100.0, change);
            machine.resetBalance();
        } else {
            System.out.println("No balance to refund.");
        }
    }

    @Override
    public void service(boolean inService) {
        if (!inService) {
            System.out.println("Bringing machine back into service.");
            machine.setState(machine.getNoCoinState());
        } else {
            System.out.println("Machine already out of service.");
        }
    }
}

