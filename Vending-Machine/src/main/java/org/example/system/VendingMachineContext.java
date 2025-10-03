package org.example.system;

import org.example.enums.PaymentMethod;
import org.example.enums.TransactionStatus;
import org.example.interfaces.PaymentStrategy;
import org.example.interfaces.State;
import org.example.interfaces.VendingMachineObserver;
import org.example.model.Inventory;
import org.example.model.Product;
import org.example.model.Transaction;
import org.example.strategies.payment.CardPaymentStrategy;
import org.example.strategies.payment.CashPaymentStrategy;
import org.example.strategies.payment.MobilePaymentStrategy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Context class for State Pattern
 * Maintains current state and delegates operations to current state
 */
public class VendingMachineContext {
    private State currentState;
    private Product selectedProduct;
    private double totalPaid;
    private PaymentMethod paymentMethod;
    private final Inventory inventory;
    private final List<VendingMachineObserver> observers;
    private final AtomicLong transactionIdCounter;
    
    // Payment strategies
    private final PaymentStrategy cashStrategy;
    private final PaymentStrategy cardStrategy;
    private final PaymentStrategy mobileStrategy;

    public VendingMachineContext(State initialState, Inventory inventory) {
        this.currentState = initialState;
        this.inventory = inventory;
        this.observers = new CopyOnWriteArrayList<>();
        this.transactionIdCounter = new AtomicLong(0);
        this.totalPaid = 0.0;
        
        // Initialize payment strategies
        this.cashStrategy = new CashPaymentStrategy();
        this.cardStrategy = new CardPaymentStrategy();
        this.mobileStrategy = new MobilePaymentStrategy();
    }

    // State management
    public void setState(State newState) {
        if (currentState != null) {
            System.out.println("🔄 State transition: " + currentState.getStateName() + 
                             " → " + newState.getStateName());
        }
        this.currentState = newState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public String getCurrentStateName() {
        return currentState != null ? currentState.getStateName() : "UNKNOWN";
    }

    // Delegate operations to current state
    public void selectProduct(Product product) {
        currentState.selectProduct(product);
    }

    public void insertCoin(double amount) {
        currentState.insertCoin(amount);
    }

    public void insertCard(String cardNumber, double amount) {
        currentState.insertCard(cardNumber, amount);
    }

    public void insertMobilePayment(String paymentId, double amount) {
        currentState.insertMobilePayment(paymentId, amount);
    }

    public void dispenseProduct() {
        currentState.dispenseProduct();
    }

    public void cancel() {
        currentState.cancel();
    }

    // Payment processing using Strategy Pattern
    public boolean processCardPayment(String cardNumber, double amount) {
        return cardStrategy.processPayment(amount);
    }

    public boolean processMobilePayment(String paymentId, double amount) {
        return mobileStrategy.processPayment(amount);
    }

    // Transaction management
    public void resetTransaction() {
        this.selectedProduct = null;
        this.totalPaid = 0.0;
        this.paymentMethod = null;
    }

    public Transaction createTransaction(Product product, double amountPaid, 
                                        double change, TransactionStatus status) {
        String transactionId = "T" + transactionIdCounter.incrementAndGet();
        return new Transaction(transactionId, product, amountPaid, change, 
                             paymentMethod, status);
    }

    // Observer Pattern - Notification methods
    public void registerObserver(VendingMachineObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(VendingMachineObserver observer) {
        observers.remove(observer);
    }

    public void notifyProductDispensed(Product product, Transaction transaction) {
        for (VendingMachineObserver observer : observers) {
            observer.onProductDispensed(product, transaction);
        }
    }

    public void notifyPaymentReceived(double amount, String paymentMethod) {
        for (VendingMachineObserver observer : observers) {
            observer.onPaymentReceived(amount, paymentMethod);
        }
    }

    public void notifyChangeReturned(double changeAmount) {
        for (VendingMachineObserver observer : observers) {
            observer.onChangeReturned(changeAmount);
        }
    }

    public void notifyProductOutOfStock(Product product) {
        for (VendingMachineObserver observer : observers) {
            observer.onProductOutOfStock(product);
        }
    }

    public void notifyProductLowStock(Product product, int remainingStock) {
        for (VendingMachineObserver observer : observers) {
            observer.onProductLowStock(product, remainingStock);
        }
    }

    public void notifyTransactionCancelled(double refundAmount) {
        for (VendingMachineObserver observer : observers) {
            observer.onTransactionCancelled(refundAmount);
        }
    }

    public void notifyError(String errorMessage) {
        for (VendingMachineObserver observer : observers) {
            observer.onError(errorMessage);
        }
    }

    // Getters and setters
    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product product) {
        this.selectedProduct = product;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double amount) {
        this.totalPaid = amount;
    }

    public void addPayment(double amount) {
        this.totalPaid += amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Inventory getInventory() {
        return inventory;
    }
}

