package org.example.enums;

/**
 * Represents different coin/bill denominations
 */
public enum CoinType {
    PENNY(0.01),
    NICKEL(0.05),
    DIME(0.10),
    QUARTER(0.25),
    HALF_DOLLAR(0.50),
    DOLLAR(1.00),
    FIVE_DOLLAR(5.00),
    TEN_DOLLAR(10.00),
    TWENTY_DOLLAR(20.00);

    private final double value;

    CoinType(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public static CoinType fromValue(double value) {
        for (CoinType coin : values()) {
            if (Math.abs(coin.value - value) < 0.001) {
                return coin;
            }
        }
        return null;
    }
}

