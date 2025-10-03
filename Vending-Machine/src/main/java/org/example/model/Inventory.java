package org.example.model;

import java.util.*;

public class Inventory {
    private final Map<String, Integer> stock = new HashMap<>();

    public void add(String itemCode, int count) {
        stock.put(itemCode, stock.getOrDefault(itemCode, 0) + count);
    }

    public boolean hasItem(String code) {
        return stock.getOrDefault(code, 0) > 0;
    }

    public void removeOne(String code) {
        stock.computeIfPresent(code, (k, v) -> (v > 0) ? v - 1 : 0);
    }

    public int getCount(String code) {
        return stock.getOrDefault(code, 0);
    }

    public Set<String> getItems() {
        return Collections.unmodifiableSet(stock.keySet());
    }
}

