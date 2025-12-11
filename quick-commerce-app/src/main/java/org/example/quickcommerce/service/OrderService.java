package org.example.quickcommerce.service;

import org.example.quickcommerce.enums.OrderStatus;
import org.example.quickcommerce.enums.PaymentMethod;
import org.example.quickcommerce.exception.InsufficientInventoryException;
import org.example.quickcommerce.exception.PaymentFailedException;
import org.example.quickcommerce.exception.UserNotLoggedInException;
import org.example.quickcommerce.model.*;
import org.example.quickcommerce.payment.PaymentProcessor;
import org.example.quickcommerce.repository.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service for managing orders.
 * Uses Repository singleton for data storage.
 */
public class OrderService {

    private final UserService userService;
    private final CartService cartService;
    private final InventoryService inventoryService;
    private final DeliveryPartnerService deliveryPartnerService;
    private final PaymentProcessor paymentProcessor;

    public OrderService(UserService userService, CartService cartService,
                        InventoryService inventoryService,
                        DeliveryPartnerService deliveryPartnerService) {
        this.userService = userService;
        this.cartService = cartService;
        this.inventoryService = inventoryService;
        this.deliveryPartnerService = deliveryPartnerService;
        this.paymentProcessor = new PaymentProcessor();
    }

    private ConcurrentHashMap<String, Order> getOrderDb() {
        return Repository.getInstance().getOrderDb();
    }

    private ConcurrentHashMap<String, Payment> getPaymentDb() {
        return Repository.getInstance().getPaymentDb();
    }

    private ConcurrentHashMap<String, List<String>> getUserOrdersDb() {
        return Repository.getInstance().getUserOrdersDb();
    }

    public Order placeOrder(String userId, PaymentMethod paymentMethod) {
        // Validate user is logged in
        User user = userService.getUserOrThrow(userId);
        if (!user.isLoggedIn()) {
            throw new UserNotLoggedInException(userId);
        }

        // Validate cart is not empty
        if (cartService.isCartEmpty(userId)) {
            throw new IllegalStateException("Cart is empty");
        }

        // Validate delivery address
        Address deliveryAddress = user.getSelectedAddress();
        if (deliveryAddress == null) {
            throw new IllegalStateException("No delivery address selected");
        }

        // Find nearest warehouse
        Warehouse nearestWarehouse = inventoryService.findNearestWarehouse(deliveryAddress.getLocation())
                .orElseThrow(() -> new IllegalStateException("No warehouse available"));

        // Validate inventory and prepare order items
        List<CartItem> cartItems = cartService.getCartItems(userId);
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            String productId = cartItem.getProduct().getProductId();
            int quantity = cartItem.getQuantity();

            if (!inventoryService.hasStock(nearestWarehouse.getWarehouseId(), productId, quantity)) {
                int available = inventoryService.getInventory(nearestWarehouse.getWarehouseId(), productId);
                throw new InsufficientInventoryException(productId, quantity, available);
            }

            orderItems.add(new OrderItem(cartItem.getProduct(), quantity));
        }

        double totalPrice = cartService.getCartTotal(userId);

        // Process payment
        Order tempOrder = new Order(userId, orderItems, totalPrice, deliveryAddress,
                nearestWarehouse.getWarehouseId());
        Payment payment = new Payment(tempOrder.getOrderId(), totalPrice, paymentMethod);

        boolean paymentSuccess = paymentProcessor.processPayment(payment);

        if (!payment.isCashOnDelivery() && !paymentSuccess) {
            throw new PaymentFailedException(tempOrder.getOrderId());
        }

        // Create order
        Order order = new Order(userId, orderItems, totalPrice, deliveryAddress,
                nearestWarehouse.getWarehouseId());
        getOrderDb().put(order.getOrderId(), order);
        getPaymentDb().put(order.getOrderId(), payment);
        getUserOrdersDb().computeIfAbsent(userId, k -> new ArrayList<>()).add(order.getOrderId());

        // Reduce inventory
        for (OrderItem item : orderItems) {
            inventoryService.reduceInventory(nearestWarehouse.getWarehouseId(),
                    item.getProductId(), item.getQuantity());
        }

        // Clear cart
        cartService.clearCart(userId);

        // Assign delivery partner
        try {
            DeliveryPartner partner = deliveryPartnerService.assignNearestPartner(
                    order.getOrderId(), nearestWarehouse.getLocation());
            order.assignDeliveryPartner(partner.getPartnerId());
            System.out.println("Delivery partner assigned: " + partner.getName());
        } catch (Exception e) {
            System.out.println("Warning: Could not assign delivery partner. " + e.getMessage());
        }

        return order;
    }

    public Optional<Order> getOrder(String orderId) {
        return Optional.ofNullable(getOrderDb().get(orderId));
    }

    public Order getOrderOrThrow(String orderId) {
        return getOrder(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    public List<Order> getUserOrders(String userId) {
        List<String> orderIds = getUserOrdersDb().getOrDefault(userId, Collections.emptyList());
        return orderIds.stream()
                .map(getOrderDb()::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Optional<Payment> getPayment(String orderId) {
        return Optional.ofNullable(getPaymentDb().get(orderId));
    }

    public void advanceOrderStatus(String orderId) {
        Order order = getOrderOrThrow(orderId);

        if (order.advanceStatus()) {
            System.out.println("Order " + orderId + " status updated to: " + order.getStatus());

            if (order.getStatus() == OrderStatus.DELIVERED && order.getDeliveryPartnerId() != null) {
                deliveryPartnerService.completeDelivery(order.getDeliveryPartnerId());
            }
        } else {
            System.out.println("Cannot advance order status. Current status: " + order.getStatus());
        }
    }

    public boolean cancelOrder(String orderId) {
        Order order = getOrderOrThrow(orderId);

        if (!order.canCancel()) {
            System.out.println("Order cannot be cancelled. Current status: " + order.getStatus());
            return false;
        }

        // Restore inventory
        for (OrderItem item : order.getItems()) {
            inventoryService.restoreInventory(order.getWarehouseId(),
                    item.getProductId(), item.getQuantity());
        }

        // Free delivery partner
        if (order.getDeliveryPartnerId() != null) {
            deliveryPartnerService.completeDelivery(order.getDeliveryPartnerId());
        }

        order.setStatus(OrderStatus.CANCELLED);
        System.out.println("Order " + orderId + " has been cancelled");
        return true;
    }

    public void markDelivered(String orderId) {
        Order order = getOrderOrThrow(orderId);

        if (order.getStatus() != OrderStatus.OUT_FOR_DELIVERY) {
            throw new IllegalStateException("Order must be OUT_FOR_DELIVERY to mark as delivered");
        }

        order.setStatus(OrderStatus.DELIVERED);

        if (order.getDeliveryPartnerId() != null) {
            deliveryPartnerService.completeDelivery(order.getDeliveryPartnerId());
        }

        System.out.println("Order " + orderId + " has been delivered");
    }
}

