package org.example.model;

import org.example.enums.CoinType;

/**
 * Represents a physical coin in the vending machine system.
 * Wraps the CoinType enum with additional functionality.
 */
public class Coin {
    private final CoinType coinType;

    public Coin(CoinType coinType) {
        this.coinType = coinType;
    }

    public CoinType getCoinType() {
        return coinType;
    }

    public int getValue() {
        return coinType.getValue();
    }

    @Override
    public String toString() {
        return coinType.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coin coin = (Coin) obj;
        return coinType == coin.coinType;
    }

    @Override
    public int hashCode() {
        return coinType.hashCode();
    }
}
