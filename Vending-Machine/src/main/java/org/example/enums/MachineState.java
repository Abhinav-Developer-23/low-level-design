package org.example.enums;

/**
 * Enum representing the different states of the vending machine.
 * Used in the State design pattern implementation.
 */
public enum MachineState {
    IDLE("Ready for product selection"),
    SELECTING("Product selection in progress"),
    PAYMENT("Payment processing"),
    DISPENSING("Product dispensing in progress");

    private final String description;

    MachineState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
