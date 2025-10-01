package org.example.enums;

/**
 * Represents the different states of the vending machine
 */
public enum MachineState {
    IDLE,           // Ready for selection
    SELECTING,      // Product being selected
    PAYING,         // Payment in progress
    DISPENSING,     // Product being dispensed
    REFUNDING,      // Refunding money
    OUT_OF_ORDER    // Machine is out of service
}
