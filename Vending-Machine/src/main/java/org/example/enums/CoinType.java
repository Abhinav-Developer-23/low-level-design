package org.example.enums;

/**
 * Represents different coin denominations accepted by the vending machine
 */
public enum CoinType {
    ONE(1),
    FIVE(5),
    TEN(10),
    TWENTY_FIVE(25);

    private final int value;

    CoinType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CoinType fromValue(int value) {
        for (CoinType coin : values()) {
            if (coin.value == value) {
                return coin;
            }
        }
        throw new IllegalArgumentException("Invalid coin value: " + value);
    }
}
