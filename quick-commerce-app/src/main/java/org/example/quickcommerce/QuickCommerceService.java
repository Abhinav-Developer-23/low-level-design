package org.example.quickcommerce;

import org.example.quickcommerce.enums.PaymentMethod;
import org.example.quickcommerce.enums.ProductCategory;
import org.example.quickcommerce.model.*;
import org.example.quickcommerce.service.*;

import java.util.List;

/**
 * Facade class that provides a simplified interface to the Quick Commerce system.
 */
public class QuickCommerceService {

    private final UserService userService;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final CartService cartService;
    private final OrderService orderService;
    private final DeliveryPartnerService deliveryPartnerService;

    public QuickCommerceService() {
        this.userService = new UserService();
        this.productService = new ProductService();
        this.inventoryService = new InventoryService();
        this.cartService = new CartService(inventoryService, productService);
        this.deliveryPartnerService = new DeliveryPartnerService();
        this.orderService = new OrderService(userService, cartService, inventoryService,
                deliveryPartnerService);
    }

    // ==================== User Operations ====================

    public User registerUser(String name, String email, String phone) {
        return userService.registerUser(name, email, phone);
    }

    public void loginUser(String userId) {
        userService.login(userId);
    }

    public void logoutUser(String userId) {
        userService.logout(userId);
    }

    public void addUserAddress(String userId, Address address) {
        userService.addAddress(userId, address);
    }

    public void selectUserAddress(String userId, String addressId) {
        userService.selectAddress(userId, addressId);
    }

    public User getUser(String userId) {
        return userService.getUserOrThrow(userId);
    }

    // ==================== Product Operations ====================

    public Product addProduct(String name, double price, ProductCategory category, String unit) {
        return productService.addProduct(name, price, category, unit);
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public List<Product> getProductsByCategory(ProductCategory category) {
        return productService.getProductsByCategory(category);
    }

    public List<Product> searchProducts(String keyword) {
        return productService.searchProducts(keyword);
    }

    public Product getProduct(String productId) {
        return productService.getProductOrThrow(productId);
    }

    // ==================== Warehouse & Inventory Operations ====================

    public Warehouse createWarehouse(String name, Location location) {
        return inventoryService.createWarehouse(name, location);
    }

    public void addInventory(String warehouseId, String productId, int quantity) {
        inventoryService.addInventory(warehouseId, productId, quantity);
    }

    public int getInventory(String warehouseId, String productId) {
        return inventoryService.getInventory(warehouseId, productId);
    }

    public Warehouse getNearestWarehouse(Location userLocation) {
        return inventoryService.findNearestWarehouse(userLocation)
                .orElseThrow(() -> new IllegalStateException("No warehouse available"));
    }

    // ==================== Cart Operations ====================

    public void addToCart(String userId, String productId, int quantity) {
        Address userAddress = userService.getSelectedAddress(userId);
        if (userAddress == null) {
            throw new IllegalStateException("Please select a delivery address first");
        }

        Warehouse nearestWarehouse = inventoryService.findNearestWarehouse(userAddress.getLocation())
                .orElseThrow(() -> new IllegalStateException("No warehouse available"));

        cartService.addToCart(userId, productId, quantity, nearestWarehouse.getWarehouseId());
    }

    public void removeFromCart(String userId, String productId) {
        cartService.removeFromCart(userId, productId);
    }

    public void updateCartItemQuantity(String userId, String productId, int quantity) {
        cartService.updateCartItemQuantity(userId, productId, quantity);
    }

    public List<CartItem> getCartItems(String userId) {
        return cartService.getCartItems(userId);
    }

    public double getCartTotal(String userId) {
        return cartService.getCartTotal(userId);
    }

    public void clearCart(String userId) {
        cartService.clearCart(userId);
    }

    // ==================== Order Operations ====================

    public Order placeOrder(String userId, PaymentMethod paymentMethod) {
        return orderService.placeOrder(userId, paymentMethod);
    }

    public Order getOrder(String orderId) {
        return orderService.getOrderOrThrow(orderId);
    }

    public List<Order> getUserOrders(String userId) {
        return orderService.getUserOrders(userId);
    }

    public void advanceOrderStatus(String orderId) {
        orderService.advanceOrderStatus(orderId);
    }

    public boolean cancelOrder(String orderId) {
        return orderService.cancelOrder(orderId);
    }

    public void markOrderDelivered(String orderId) {
        orderService.markDelivered(orderId);
    }

    // ==================== Delivery Partner Operations ====================

    public DeliveryPartner registerDeliveryPartner(String name, String phone, Location location) {
        return deliveryPartnerService.registerPartner(name, phone, location);
    }

    public void setDeliveryPartnerActive(String partnerId) {
        deliveryPartnerService.setPartnerActive(partnerId);
    }

    public void setDeliveryPartnerInactive(String partnerId) {
        deliveryPartnerService.setPartnerInactive(partnerId);
    }

    public List<DeliveryPartner> getAvailableDeliveryPartners() {
        return deliveryPartnerService.getAvailablePartners();
    }
}

