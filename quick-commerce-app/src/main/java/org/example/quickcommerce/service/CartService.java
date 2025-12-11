package org.example.quickcommerce.service;

import org.example.quickcommerce.model.Cart;
import org.example.quickcommerce.model.CartItem;
import org.example.quickcommerce.model.Product;
import org.example.quickcommerce.observer.InventoryObserver;
import org.example.quickcommerce.repository.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing user carts.
 * Uses Repository singleton for data storage.
 * Implements InventoryObserver to auto-remove out-of-stock items.
 */
public class CartService implements InventoryObserver {

    private final Map<String, Set<String>> productToUsers = new HashMap<>(); // productId -> userIds
    private final InventoryService inventoryService;
    private final ProductService productService;

    public CartService(InventoryService inventoryService, ProductService productService) {
        this.inventoryService = inventoryService;
        this.productService = productService;
        inventoryService.addObserver(this);
    }

    private ConcurrentHashMap<String, Cart> getCartDb() {
        return Repository.getInstance().getCartDb();
    }

    public Cart getOrCreateCart(String userId) {
        return getCartDb().computeIfAbsent(userId, Cart::new);
    }

    public Optional<Cart> getCart(String userId) {
        return Optional.ofNullable(getCartDb().get(userId));
    }

    public void addToCart(String userId, String productId, int quantity, String warehouseId) {
        Product product = productService.getProductOrThrow(productId);

        if (!inventoryService.hasStock(warehouseId, productId, quantity)) {
            throw new IllegalStateException("Product not available in sufficient quantity");
        }

        Cart cart = getOrCreateCart(userId);
        cart.addItem(product, quantity);

        productToUsers.computeIfAbsent(productId, k -> new HashSet<>()).add(userId);
    }

    public void removeFromCart(String userId, String productId) {
        getCart(userId).ifPresent(cart -> {
            cart.removeItem(productId);
            Set<String> users = productToUsers.get(productId);
            if (users != null) {
                users.remove(userId);
            }
        });
    }

    public void updateCartItemQuantity(String userId, String productId, int quantity) {
        Cart cart = getCart(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + userId));

        if (quantity <= 0) {
            removeFromCart(userId, productId);
        } else {
            cart.updateItemQuantity(productId, quantity);
        }
    }

    public List<CartItem> getCartItems(String userId) {
        return getCart(userId).map(Cart::getItems).orElse(Collections.emptyList());
    }

    public double getCartTotal(String userId) {
        return getCart(userId).map(Cart::getTotalPrice).orElse(0.0);
    }

    public void clearCart(String userId) {
        getCart(userId).ifPresent(cart -> {
            for (CartItem item : cart.getItems()) {
                Set<String> users = productToUsers.get(item.getProduct().getProductId());
                if (users != null) {
                    users.remove(userId);
                }
            }
            cart.clear();
        });
    }

    public boolean isCartEmpty(String userId) {
        return getCart(userId).map(Cart::isEmpty).orElse(true);
    }

    @Override
    public void onInventoryChange(String warehouseId, String productId, int newQuantity) {
        if (newQuantity <= 0) {
            Set<String> affectedUsers = productToUsers.get(productId);
            if (affectedUsers != null && !affectedUsers.isEmpty()) {
                for (String userId : new HashSet<>(affectedUsers)) {
                    getCart(userId).ifPresent(cart -> {
                        cart.removeItem(productId);
                        System.out.println("Auto-removed out-of-stock product " + productId +
                                " from cart of user " + userId);
                    });
                }
                affectedUsers.clear();
            }
        }
    }
}

