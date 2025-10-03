package org.example.states;

import org.example.enums.Coin;
import org.example.interfaces.State;
import org.example.model.Item;
import org.example.system.VendingMachine;

import java.util.Map;
import java.util.Optional;

public class HasCoinState implements State {
    private final VendingMachine machine;

    public HasCoinState(VendingMachine m) {
        this.machine = m;
    }

    @Override
    public void insertCoin(Coin coin) {
        machine.addBalance(coin.getValue());
        System.out.printf("Inserted %s. New balance: $%.2f\n",
                coin, machine.getBalanceCents() / 100.0);
    }

    @Override
    public void selectItem(String itemCode) {
        Optional<Item> opt = machine.getItem(itemCode);
        if (!opt.isPresent()) {
            System.out.println("Invalid selection.");
            return;
        }
        Item item = opt.get();
        if (!machine.isAvailable(itemCode)) {
            System.out.println("Selected item is out of stock.");
            return;
        }
        machine.setSelectedItem(itemCode);
        int price = item.getPriceCents();
        int bal = machine.getBalanceCents();
        if (bal < price) {
            double need = (price - bal) / 100.0;
            System.out.printf("Insufficient balance. Please insert $%.2f more.\n", need);
            // remain in HasCoinState
        } else {
            System.out.printf("Sufficient balance. Dispensing %s...\n", item.getName());
            machine.setState(machine.getDispensingState());
            machine.dispense();
        }
    }

    @Override
    public void dispense() {
        System.out.println("Please select an item first.");
    }

    @Override
    public void refund() {
        int bal = machine.getBalanceCents();
        if (bal == 0) {
            System.out.println("No money to refund.");
            machine.setState(machine.getNoCoinState());
            return;
        }
        Map<Coin, Integer> change = machine.makeChange(bal);
        machine.resetBalance();
        machine.clearSelection();
        machine.setState(machine.getNoCoinState());
        System.out.printf("Refunding $%.2f as: %s\n", bal / 100.0, change);
    }

    @Override
    public void service(boolean inService) {
        if (inService) {
            System.out.println("Putting machine out of service. Refunding current balance first...");
            int bal = machine.getBalanceCents();
            if (bal > 0) {
                Map<Coin, Integer> change = machine.makeChange(bal);
                System.out.printf("Refunded $%.2f as %s\n", bal / 100.0, change);
            }
            machine.resetBalance();
            machine.clearSelection();
            machine.setState(machine.getOutOfServiceState());
        } else {
            System.out.println("Machine already in service.");
        }
    }
}

