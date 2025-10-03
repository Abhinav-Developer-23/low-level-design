package org.example.states;

import org.example.enums.Coin;
import org.example.interfaces.State;
import org.example.system.VendingMachine;

public class NoCoinState implements State {
    private final VendingMachine machine;

    public NoCoinState(VendingMachine m) {
        this.machine = m;
    }

    @Override
    public void insertCoin(Coin coin) {
        machine.addBalance(coin.getValue());
        System.out.printf("Inserted %s (%dc). Balance is $%.2f\n",
                coin, coin.getValue(), machine.getBalanceCents() / 100.0);
        machine.setState(machine.getHasCoinState());
    }

    @Override
    public void selectItem(String itemCode) {
        System.out.println("Please insert coin first.");
    }

    @Override
    public void dispense() {
        System.out.println("No coin and no selection. Nothing to dispense.");
    }

    @Override
    public void refund() {
        System.out.println("No money to refund.");
    }

    @Override
    public void service(boolean inService) {
        if (inService) {
            machine.setState(machine.getOutOfServiceState());
            System.out.println("Putting machine out of service.");
        } else {
            System.out.println("Machine already in service.");
        }
    }
}

