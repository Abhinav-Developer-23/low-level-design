package org.example.model;

public class Item {
    private final String code;
    private final String name;
    private final int priceCents; // price in cents to avoid float problems

    public Item(String code, String name, int priceCents) {
        this.code = code;
        this.name = name;
        this.priceCents = priceCents;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getPriceCents() {
        return priceCents;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - $%.2f", name, code, priceCents / 100.0);
    }
}

