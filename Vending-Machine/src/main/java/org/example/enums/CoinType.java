package org.example.enums;

/**
 * Enum representing different types of coins that can be inserted into the vending machine.
 */
public enum CoinType {
    PENNY(1),
    NICKEL(5),
    DIME(10),
    QUARTER(25),
    DOLLAR(100);

    private final int value;

    CoinType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
