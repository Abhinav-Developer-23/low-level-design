package org.example.enums;

/**
 * Represents the different states of the vending machine
 * Used in State Pattern implementation
 */
public enum MachineState {
    IDLE("Idle - Waiting for customer"),
    SELECTING("Selecting - Product selected, awaiting payment"),
    PAYMENT("Payment - Payment received, ready to dispense"),
    DISPENSING("Dispensing - Dispensing product");

    private final String description;

    MachineState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

