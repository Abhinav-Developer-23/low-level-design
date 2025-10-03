package org.example.states;

import org.example.enums.Coin;
import org.example.interfaces.State;
import org.example.model.Item;
import org.example.system.VendingMachine;

import java.util.Map;

public class DispensingState implements State {
    private final VendingMachine machine;

    public DispensingState(VendingMachine m) {
        this.machine = m;
    }

    @Override
    public void insertCoin(Coin coin) {
        System.out.println("Please wait, dispensing in progress.");
    }

    @Override
    public void selectItem(String itemCode) {
        System.out.println("Already dispensing. Please wait.");
    }

    @Override
    public void dispense() {
        String code = machine.getSelectedItem();
        if (code == null) {
            System.out.println("No item selected.");
            machine.setState(machine.getNoCoinState());
            return;
        }
        if (!machine.isAvailable(code)) {
            System.out.println("Item out of stock during dispensing. Refunding...");
            machine.refund();
            return;
        }
        Item item = machine.getItem(code).get();
        int price = item.getPriceCents();
        int balance = machine.getBalanceCents();

        // simulate dispensing
        System.out.printf("Dispensed: %s\n", item.getName());
        machine.decrementItem(code);

        int change = balance - price;
        if (change > 0) {
            Map<Coin, Integer> ch = machine.makeChange(change);
            System.out.printf("Returning change $%.2f as %s\n", change / 100.0, ch);
        }
        machine.resetBalance();
        machine.clearSelection();
        machine.setState(machine.getNoCoinState());
    }

    @Override
    public void refund() {
        System.out.println("Cannot refund during dispensing.");
    }

    @Override
    public void service(boolean inService) {
        System.out.println("Cannot switch to service while dispensing.");
    }
}

