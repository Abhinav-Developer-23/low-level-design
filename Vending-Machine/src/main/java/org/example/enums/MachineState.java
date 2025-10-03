package org.example.enums;

/**
 * Enumeration representing the different operational states of the vending machine.
 * Used in the State Pattern implementation to manage machine behavior.
 */
public enum MachineState {
    IDLE,           // Machine ready for product selection
    SELECTING,      // Product selected, awaiting payment
    PAYMENT,        // Processing payment
    DISPENSING,     // Dispensing product and returning change
    OUT_OF_SERVICE  // Maintenance mode
}
