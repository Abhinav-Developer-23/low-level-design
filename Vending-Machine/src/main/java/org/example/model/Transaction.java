package org.example.model;

import org.example.enums.PaymentMethod;
import org.example.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a transaction in the vending machine system
 */
public class Transaction {

    private final String transactionId;
    private final Product product;
    private final List<Coin> insertedCoins;
    private final PaymentMethod paymentMethod;
    private final LocalDateTime timestamp;
    private TransactionStatus status;
    private double amountPaid;
    private List<Coin> changeCoins;

    public Transaction(String transactionId, Product product, PaymentMethod paymentMethod) {
        this.transactionId = transactionId;
        this.product = product;
        this.paymentMethod = paymentMethod;
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.PENDING;
        this.insertedCoins = new ArrayList<>();
        this.changeCoins = new ArrayList<>();
        this.amountPaid = 0.0;
    }

    public synchronized void addCoin(Coin coin) {
        insertedCoins.add(coin);
        amountPaid += coin.getValue();
    }

    public synchronized void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public synchronized void setChangeCoins(List<Coin> changeCoins) {
        this.changeCoins = new ArrayList<>(changeCoins);
    }

    // Getters
    public String getTransactionId() {
        return transactionId;
    }

    public Product getProduct() {
        return product;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public double getRequiredAmount() {
        return product.getPrice();
    }

    public double getChangeAmount() {
        return amountPaid - product.getPrice();
    }

    public List<Coin> getInsertedCoins() {
        return Collections.unmodifiableList(insertedCoins);
    }

    public List<Coin> getChangeCoins() {
        return Collections.unmodifiableList(changeCoins);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isComplete() {
        return status == TransactionStatus.COMPLETED;
    }

    public boolean isRefunded() {
        return status == TransactionStatus.REFUNDED;
    }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', product='%s', amountPaid=%.2f, required=%.2f, status=%s, change=%.2f}",
                transactionId, product.getName(), amountPaid, product.getPrice(), status, getChangeAmount());
    }
}
