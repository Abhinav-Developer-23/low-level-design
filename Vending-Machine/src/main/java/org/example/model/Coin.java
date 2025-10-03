package org.example.model;

import org.example.enums.CoinType;

/**
 * Model class representing a coin inserted into the vending machine.
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
    public String toString() {
        return type.name() + " (" + value + " cents)";
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
}
