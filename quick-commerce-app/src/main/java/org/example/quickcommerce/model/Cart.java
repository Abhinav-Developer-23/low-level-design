package org.example.quickcommerce.model;

import java.util.*;

/**
 * Represents a user's shopping cart.
 */
public class Cart {
    private final String cartId;
    private final String userId;
    private final Map<String, CartItem> items; // productId -> CartItem

    public Cart(String userId) {
        this.cartId = UUID.randomUUID().toString();
        this.userId = userId;
        this.items = new HashMap<>();
    }

    public String getCartId() {
        return cartId;
    }

    public String getUserId() {
        return userId;
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public int getItemCount() {
        return items.values().stream().mapToInt(CartItem::getQuantity).sum();
    }

    public double getTotalPrice() {
        return items.values().stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void addItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        String productId = product.getProductId();
        if (items.containsKey(productId)) {
            items.get(productId).incrementQuantity(quantity);
        } else {
            items.put(productId, new CartItem(product, quantity));
        }
    }

    public void removeItem(String productId) {
        items.remove(productId);
    }

    public void updateItemQuantity(String productId, int quantity) {
        if (!items.containsKey(productId)) {
            throw new IllegalArgumentException("Product not in cart: " + productId);
        }
        if (quantity <= 0) {
            removeItem(productId);
        } else {
            items.get(productId).setQuantity(quantity);
        }
    }

    public CartItem getItem(String productId) {
        return items.get(productId);
    }

    public boolean hasItem(String productId) {
        return items.containsKey(productId);
    }

    public void clear() {
        items.clear();
    }

    public List<String> removeOutOfStockItems(Set<String> outOfStockProductIds) {
        List<String> removed = new ArrayList<>();
        for (String productId : outOfStockProductIds) {
            if (items.containsKey(productId)) {
                items.remove(productId);
                removed.add(productId);
            }
        }
        return removed;
    }

    @Override
    public String toString() {
        return "Cart{cartId='" + cartId + "', userId='" + userId + "', itemCount=" + getItemCount() + ", totalPrice=" + getTotalPrice() + "}";
    }
}

