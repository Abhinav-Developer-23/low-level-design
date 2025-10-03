package org.example.enums;

/**
 * Represents different coin denominations supported by the vending machine.
 * Each coin type has an associated value in cents.
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

    /**
     * Gets the value of the coin in cents.
     * 
     * @return the coin value in cents
     */
    public int getValue() {
        return value;
    }

    /**
     * Creates a CoinType from a value in cents.
     * 
     * @param value the value in cents
     * @return the corresponding CoinType, or null if no match
     */
    public static CoinType fromValue(int value) {
        for (CoinType coin : CoinType.values()) {
            if (coin.value == value) {
                return coin;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name() + " (" + value + " cents)";
    }
}

