package org.example.model;

import org.example.enums.PaymentMethod;
import org.example.enums.TransactionStatus;

import java.time.LocalDateTime;

/**
 * Immutable transaction record
 */
public class Transaction {
    private final String transactionId;
    private final Product product;
    private final double amountPaid;
    private final double changeReturned;
    private final PaymentMethod paymentMethod;
    private final TransactionStatus status;
    private final LocalDateTime timestamp;

    public Transaction(String transactionId, Product product, double amountPaid,
                      double changeReturned, PaymentMethod paymentMethod,
                      TransactionStatus status) {
        this.transactionId = transactionId;
        this.product = product;
        this.amountPaid = amountPaid;
        this.changeReturned = changeReturned;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Product getProduct() {
        return product;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public double getChangeReturned() {
        return changeReturned;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', product=%s, paid=$%.2f, change=$%.2f, method=%s, status=%s, time=%s}",
                transactionId, product.getName(), amountPaid, changeReturned, 
                paymentMethod, status, timestamp);
    }
}

