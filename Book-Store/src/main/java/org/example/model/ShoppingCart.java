package org.example.model;

import java.util.*;

public class ShoppingCart {
    private Map<Book, Integer> items;

    public ShoppingCart() {
        items = new HashMap<>();
    }

    public void addBook(Book book, int quantity) {
        items.put(book, items.getOrDefault(book, 0) + quantity);
    }

    public void removeBook(Book book) {
        items.remove(book);
    }

    public double getTotalPrice() {
        return items.entrySet()
                    .stream()
                    .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                    .sum();
    }

    public Map<Book, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public void clear() {
        items.clear();
    }
}

