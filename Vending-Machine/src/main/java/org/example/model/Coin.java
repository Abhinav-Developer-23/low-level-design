package org.example.model;

import org.example.enums.CoinType;

/**
 * Represents a coin in the vending machine system
 */
public class Coin {

    private final CoinType type;
    private final int value;

    public Coin(CoinType type) {
        this.type = type;
        this.value = type.getValue();
    }

    public CoinType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coin coin = (Coin) obj;
        return type == coin.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Coin{type=%s, value=%d}", type, value);
    }
}
