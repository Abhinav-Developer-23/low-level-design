package org.example.quickcommerce.model;

import org.example.quickcommerce.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents an order in the quick commerce system.
 */
public class Order {
    private final String orderId;
    private final String userId;
    private final List<OrderItem> items;
    private final double totalPrice;
    private final Address deliveryAddress;
    private final String warehouseId;
    private OrderStatus status;
    private String deliveryPartnerId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(String userId, List<OrderItem> items, double totalPrice,
                 Address deliveryAddress, String warehouseId) {
        this.orderId = UUID.randomUUID().toString();
        this.userId = userId;
        this.items = new ArrayList<>(items);
        this.totalPrice = totalPrice;
        this.deliveryAddress = deliveryAddress;
        this.warehouseId = warehouseId;
        this.status = OrderStatus.PLACED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getDeliveryPartnerId() {
        return deliveryPartnerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignDeliveryPartner(String deliveryPartnerId) {
        this.deliveryPartnerId = deliveryPartnerId;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean advanceStatus() {
        switch (status) {
            case PLACED:
                status = OrderStatus.PACKED;
                break;
            case PACKED:
                status = OrderStatus.OUT_FOR_DELIVERY;
                break;
            case OUT_FOR_DELIVERY:
                status = OrderStatus.DELIVERED;
                break;
            default:
                return false;
        }
        this.updatedAt = LocalDateTime.now();
        return true;
    }

    public boolean canCancel() {
        return status == OrderStatus.PLACED || status == OrderStatus.PACKED;
    }

    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', status=" + status + ", totalPrice=" + totalPrice + "}";
    }
}

