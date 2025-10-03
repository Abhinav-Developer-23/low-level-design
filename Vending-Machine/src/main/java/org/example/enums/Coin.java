package org.example.enums;

public enum Coin {
    PENNY(1),
    NICKEL(5),
    DIME(10),
    QUARTER(25);

    private final int cents;

    Coin(int cents) {
        this.cents = cents;
    }

    public int getValue() {
        return cents;
    }
}

