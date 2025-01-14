package org.lowLevelDesign.LowLevelDesign.ATMSystem.src.hardware;

public class DepositSlot {
    private boolean isAvailable;

    public DepositSlot() {
        this.isAvailable = true;
    }

    public boolean acceptDeposit(double amount) {
        if (!isAvailable) {
            return false;
        }
        // Simulate deposit processing
        return true;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
} 