package org.example.model;

import org.example.enums.CoinType;

/**
 * Represents a coin or bill
 */
public class Coin {
    private final CoinType type;
    private final double value;

    public Coin(CoinType type) {
        this.type = type;
        this.value = type.getValue();
    }

    public Coin(double value) {
        this.type = CoinType.fromValue(value);
        this.value = value;
    }

    public CoinType getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "type=" + type +
                ", value=$" + value +
                '}';
    }
}

