package org.lowLevelDesign.LowLevelDesign.ATMSystem.hardware;

public class CashDispenser {
    private static final int INITIAL_CASH_COUNT = 500;
    private int cashCount;

    public CashDispenser() {
        this.cashCount = INITIAL_CASH_COUNT;
    }

    public boolean dispenseCash(double amount) {
        if (amount <= 0 || amount > cashCount) {
            return false;
        }
        cashCount -= amount;
        return true;
    }

    public void refillCash(int amount) {
        this.cashCount += amount;
    }
} 