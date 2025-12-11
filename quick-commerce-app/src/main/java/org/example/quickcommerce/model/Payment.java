package org.example.quickcommerce.model;

import org.example.quickcommerce.enums.PaymentMethod;
import org.example.quickcommerce.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a payment for an order.
 */
public class Payment {
    private final String paymentId;
    private final String orderId;
    private final double amount;
    private final PaymentMethod method;
    private PaymentStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String transactionId;

    public Payment(String orderId, double amount, PaymentMethod method) {
        this.paymentId = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void markSuccess(String transactionId) {
        this.status = PaymentStatus.SUCCESS;
        this.transactionId = transactionId;
        this.processedAt = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = PaymentStatus.FAILED;
        this.processedAt = LocalDateTime.now();
    }

    public boolean isSuccessful() {
        return status == PaymentStatus.SUCCESS;
    }

    public boolean isCashOnDelivery() {
        return method == PaymentMethod.CASH_ON_DELIVERY;
    }

    @Override
    public String toString() {
        return "Payment{paymentId='" + paymentId + "', amount=" + amount + ", method=" + method + ", status=" + status + "}";
    }
}

