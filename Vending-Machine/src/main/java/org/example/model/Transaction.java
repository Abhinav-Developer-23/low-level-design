package org.example.model;

import org.example.enums.PaymentMethod;
import org.example.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Represents a transaction in the vending machine system.
 * Tracks the complete lifecycle of a purchase transaction.
 */
public class Transaction {
    private final String transactionId;
    private final LocalDateTime startTime;
    private LocalDateTime endTime;

    private TransactionStatus status;
    private String selectedProductSlot;
    private Product selectedProduct;
    private PaymentMethod paymentMethod;

    private int amountPaid; // Total amount paid in cents
    private int changeReturned; // Change returned in cents

    private final List<Coin> coinsInserted;
    private final List<String> events; // Audit trail of events

    public Transaction() {
        this.transactionId = UUID.randomUUID().toString();
        this.startTime = LocalDateTime.now();
        this.status = TransactionStatus.INITIATED;
        this.coinsInserted = new ArrayList<>();
        this.events = new ArrayList<>();
        this.amountPaid = 0;
        this.changeReturned = 0;

        addEvent("Transaction initiated");
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
        addEvent("Status changed to: " + status);
        if (status == TransactionStatus.COMPLETED || status == TransactionStatus.CANCELLED) {
            this.endTime = LocalDateTime.now();
            addEvent("Transaction ended");
        }
    }

    public String getSelectedProductSlot() {
        return selectedProductSlot;
    }

    public void setSelectedProductSlot(String selectedProductSlot) {
        this.selectedProductSlot = selectedProductSlot;
        addEvent("Product selected: " + selectedProductSlot);
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        addEvent("Payment method set to: " + paymentMethod);
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void addPayment(int amount) {
        this.amountPaid += amount;
        addEvent("Payment added: " + amount + " cents");
    }

    public int getChangeReturned() {
        return changeReturned;
    }

    public void setChangeReturned(int changeReturned) {
        this.changeReturned = changeReturned;
        addEvent("Change returned: " + changeReturned + " cents");
    }

    public List<Coin> getCoinsInserted() {
        return Collections.unmodifiableList(coinsInserted);
    }

    public void addCoin(Coin coin) {
        coinsInserted.add(coin);
        addPayment(coin.getValue());
    }

    public List<String> getEvents() {
        return Collections.unmodifiableList(events);
    }

    private void addEvent(String event) {
        events.add(String.format("[%s] %s", LocalDateTime.now(), event));
    }

    /**
     * Calculates the total cost of the transaction.
     * @return Cost in cents, or 0 if no product selected
     */
    public int getTotalCost() {
        return selectedProduct != null ? selectedProduct.getPrice() : 0;
    }

    /**
     * Checks if sufficient payment has been made.
     * @return true if amount paid >= total cost
     */
    public boolean isPaymentSufficient() {
        return amountPaid >= getTotalCost();
    }

    /**
     * Calculates the change amount needed.
     * @return Change in cents (amountPaid - totalCost), or 0 if insufficient payment
     */
    public int getChangeAmount() {
        if (!isPaymentSufficient()) {
            return 0;
        }
        return amountPaid - getTotalCost();
    }

    /**
     * Gets the transaction duration in seconds.
     * @return Duration in seconds, or current duration if not completed
     */
    public long getDurationSeconds() {
        LocalDateTime end = endTime != null ? endTime : LocalDateTime.now();
        return java.time.Duration.between(startTime, end).getSeconds();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction ID: ").append(transactionId).append("\n");
        sb.append("Status: ").append(status).append("\n");
        sb.append("Start Time: ").append(startTime).append("\n");
        if (endTime != null) {
            sb.append("End Time: ").append(endTime).append("\n");
            sb.append("Duration: ").append(getDurationSeconds()).append(" seconds\n");
        }
        if (selectedProduct != null) {
            sb.append("Product: ").append(selectedProduct.getName())
              .append(" (").append(selectedProductSlot).append(")\n");
            sb.append("Cost: ").append(selectedProduct.getFormattedPrice()).append("\n");
        }
        sb.append("Amount Paid: ").append(String.format("$%.2f", amountPaid / 100.0)).append("\n");
        if (changeReturned > 0) {
            sb.append("Change Returned: ").append(String.format("$%.2f", changeReturned / 100.0)).append("\n");
        }
        if (paymentMethod != null) {
            sb.append("Payment Method: ").append(paymentMethod).append("\n");
        }
        return sb.toString();
    }
}
