package org.example.system;

import org.example.enums.Coin;
import org.example.interfaces.State;
import org.example.model.Inventory;
import org.example.model.Item;
import org.example.states.*;

import java.util.*;

public class VendingMachine {
    // States
    private final State noCoinState;
    private final State hasCoinState;
    private final State dispensingState;
    private final State outOfServiceState;

    private State currentState;

    // Core data
    private final Map<String, Item> items = new HashMap<>();
    private final Inventory inventory = new Inventory();
    private int balanceCents = 0;
    private String selectedItemCode = null;

    public VendingMachine() {
        this.noCoinState = new NoCoinState(this);
        this.hasCoinState = new HasCoinState(this);
        this.dispensingState = new DispensingState(this);
        this.outOfServiceState = new OutOfServiceState(this);

        this.currentState = noCoinState;
    }

    // State getters
    public State getNoCoinState() {
        return noCoinState;
    }

    public State getHasCoinState() {
        return hasCoinState;
    }

    public State getDispensingState() {
        return dispensingState;
    }

    public State getOutOfServiceState() {
        return outOfServiceState;
    }

    // Data accessors / mutators
    public void addItem(Item item, int count) {
        items.put(item.getCode(), item);
        inventory.add(item.getCode(), count);
    }

    public Optional<Item> getItem(String code) {
        return Optional.ofNullable(items.get(code));
    }

    public boolean isAvailable(String code) {
        return items.containsKey(code) && inventory.hasItem(code);
    }

    public void decrementItem(String code) {
        inventory.removeOne(code);
    }

    public int getPriceCents(String code) {
        return items.get(code).getPriceCents();
    }

    public void setState(State s) {
        this.currentState = s;
    }

    public State getState() {
        return currentState;
    }

    public void addBalance(int cents) {
        balanceCents += cents;
    }

    public int getBalanceCents() {
        return balanceCents;
    }

    public void resetBalance() {
        balanceCents = 0;
    }

    public void setSelectedItem(String code) {
        selectedItemCode = code;
    }

    public String getSelectedItem() {
        return selectedItemCode;
    }

    public void clearSelection() {
        selectedItemCode = null;
    }

    // Client API -- delegates to current state
    public void insertCoin(Coin coin) {
        currentState.insertCoin(coin);
    }

    public void selectItem(String code) {
        currentState.selectItem(code);
    }

    public void dispense() {
        currentState.dispense();
    }

    public void refund() {
        currentState.refund();
    }

    public void service(boolean inService) {
        currentState.service(inService);
    }

    // Helpers to return change (naive: returns map of coin->count using greedy)
    public Map<Coin, Integer> makeChange(int cents) {
        Map<Coin, Integer> change = new LinkedHashMap<>();
        int remaining = cents;
        Coin[] coins = {Coin.QUARTER, Coin.DIME, Coin.NICKEL, Coin.PENNY};
        for (Coin c : coins) {
            int cnt = remaining / c.getValue();
            if (cnt > 0) change.put(c, cnt);
            remaining = remaining % c.getValue();
        }
        return change;
    }

    // For debugging / info
    public void printStatus() {
        System.out.println("=== Vending Machine Status ===");
        System.out.printf("State: %s\n", currentState.getClass().getSimpleName());
        System.out.printf("Balance: $%.2f\n", balanceCents / 100.0);
        System.out.println("Items:");
        for (Map.Entry<String, Item> e : items.entrySet()) {
            System.out.printf("  %s : %s (stock=%d)\n",
                    e.getKey(), e.getValue(), inventory.getCount(e.getKey()));
        }
        System.out.println("==============================");
    }
}

