package org.example.model;

import org.example.enums.PaymentMethod;
import org.example.enums.TransactionStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a transaction in the vending machine.
 */
public class Transaction {
    private final String transactionId;
    private final LocalDateTime timestamp;
    private final String productId;
    private final int productPrice;
    private final List<Coin> insertedCoins;
    private final PaymentMethod paymentMethod;
    private TransactionStatus status;
    private int totalInsertedAmount;

    public Transaction(String transactionId, String productId, int productPrice, PaymentMethod paymentMethod) {
        this.transactionId = transactionId;
        this.timestamp = LocalDateTime.now();
        this.productId = productId;
        this.productPrice = productPrice;
        this.paymentMethod = paymentMethod;
        this.insertedCoins = new ArrayList<>();
        this.status = TransactionStatus.PENDING;
        this.totalInsertedAmount = 0;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getProductId() {
        return productId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public List<Coin> getInsertedCoins() {
        return new ArrayList<>(insertedCoins);
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public int getTotalInsertedAmount() {
        return totalInsertedAmount;
    }

    public void addCoin(Coin coin) {
        insertedCoins.add(coin);
        totalInsertedAmount += coin.getValue();
    }

    public boolean isPaymentComplete() {
        return totalInsertedAmount >= productPrice;
    }

    public int getChangeAmount() {
        if (totalInsertedAmount > productPrice) {
            return totalInsertedAmount - productPrice;
        }
        return 0;
    }

    public boolean needsRefund() {
        return status == TransactionStatus.FAILED || status == TransactionStatus.CANCELLED;
    }

    @Override
    public String toString() {
        return String.format("Transaction %s: %s for %s (%d cents) - Status: %s",
                           transactionId, productId, paymentMethod, productPrice, status);
    }
}
