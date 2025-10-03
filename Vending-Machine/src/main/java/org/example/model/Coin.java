package org.example.model;

import org.example.enums.CoinType;
import java.util.Objects;

/**
 * Represents a coin in the vending machine.
 * Immutable class following good OOP practices.
 */
public class Coin {
    private final CoinType type;
    private final int value;

    /**
     * Creates a new Coin instance.
     * 
     * @param type the type of coin
     */
    public Coin(CoinType type) {
        this.type = type;
        this.value = type.getValue();
    }

    /**
     * Gets the coin type.
     * 
     * @return the coin type
     */
    public CoinType getType() {
        return type;
    }

    /**
     * Gets the coin value in cents.
     * 
     * @return the coin value
     */
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type.toString();
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
        return Objects.hash(type);
    }
}

