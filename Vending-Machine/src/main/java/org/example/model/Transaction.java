package org.example.model;

import org.example.enums.PaymentMethod;
import org.example.enums.TransactionStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a transaction in the vending machine.
 * Tracks payment details, product information, and transaction status.
 * Follows Single Responsibility Principle and encapsulation.
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

    /**
     * Creates a new Transaction instance.
     * 
     * @param productId the ID of the product being purchased
     * @param productPrice the price of the product in cents
     * @param paymentMethod the payment method to use
     */
    public Transaction(String productId, int productPrice, PaymentMethod paymentMethod) {
        this.transactionId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.productId = productId;
        this.productPrice = productPrice;
        this.paymentMethod = paymentMethod;
        this.insertedCoins = new ArrayList<>();
        this.status = TransactionStatus.PENDING;
        this.totalInsertedAmount = 0;
    }

    /**
     * Gets the transaction ID.
     * 
     * @return the transaction ID
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Gets the transaction timestamp.
     * 
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the product ID.
     * 
     * @return the product ID
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Gets the product price in cents.
     * 
     * @return the product price
     */
    public int getProductPrice() {
        return productPrice;
    }

    /**
     * Gets the list of inserted coins.
     * 
     * @return list of coins
     */
    public List<Coin> getInsertedCoins() {
        return new ArrayList<>(insertedCoins);
    }

    /**
     * Gets the payment method.
     * 
     * @return the payment method
     */
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Gets the current transaction status.
     * 
     * @return the status
     */
    public TransactionStatus getStatus() {
        return status;
    }

    /**
     * Sets the transaction status.
     * 
     * @param status the new status
     */
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    /**
     * Gets the total amount inserted in cents.
     * 
     * @return the total inserted amount
     */
    public int getTotalInsertedAmount() {
        return totalInsertedAmount;
    }

    /**
     * Adds a coin to the transaction.
     * 
     * @param coin the coin to add
     */
    public void addCoin(Coin coin) {
        insertedCoins.add(coin);
        totalInsertedAmount += coin.getValue();
    }

    /**
     * Checks if payment is complete (sufficient funds inserted).
     * 
     * @return true if payment is complete, false otherwise
     */
    public boolean isPaymentComplete() {
        return totalInsertedAmount >= productPrice;
    }

    /**
     * Gets the change amount to be returned.
     * 
     * @return the change amount in cents (0 if payment not complete)
     */
    public int getChangeAmount() {
        if (totalInsertedAmount >= productPrice) {
            return totalInsertedAmount - productPrice;
        }
        return 0;
    }

    /**
     * Gets the remaining amount needed for payment.
     * 
     * @return the remaining amount in cents (0 if payment complete)
     */
    public int getRemainingAmount() {
        if (totalInsertedAmount < productPrice) {
            return productPrice - totalInsertedAmount;
        }
        return 0;
    }

    /**
     * Checks if the transaction needs a refund.
     * 
     * @return true if money was inserted, false otherwise
     */
    public boolean needsRefund() {
        return totalInsertedAmount > 0;
    }

    @Override
    public String toString() {
        return String.format("Transaction[ID=%s, Product=%s, Price=$%.2f, Inserted=$%.2f, Status=%s]",
            transactionId.substring(0, 8), productId, 
            productPrice / 100.0, totalInsertedAmount / 100.0, status);
    }
}

