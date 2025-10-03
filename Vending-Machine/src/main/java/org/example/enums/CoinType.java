package org.example.enums;

/**
 * Enumeration for different coin denominations supported by the vending machine.
 * Uses integer values in cents to avoid floating-point precision issues.
 */
public enum CoinType {
    PENNY(1),
    NICKEL(5),
    DIME(10),
    QUARTER(25),
    HALF_DOLLAR(50),
    DOLLAR(100);

    private final int value;

    CoinType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name() + " (" + value + " cents)";
    }
}
