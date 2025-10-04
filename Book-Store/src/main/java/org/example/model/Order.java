package org.example.model;

import org.example.enums.OrderStatus;
import java.util.*;

public class Order {
    private String orderId;
    private Customer customer;
    private Map<Book, Integer> orderItems;
    private OrderStatus status;
    private double totalAmount;

    public Order(String orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.orderItems = new HashMap<>(customer.getCart().getItems());
        this.status = OrderStatus.PLACED;
        this.totalAmount = customer.getCart().getTotalPrice();
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Map<Book, Integer> getOrderItems() {
        return Collections.unmodifiableMap(orderItems);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }
}

