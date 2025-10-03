package org.example.system;

import org.example.enums.CoinType;
import org.example.enums.MachineState;
import org.example.enums.PaymentMethod;
import org.example.enums.TransactionStatus;
import org.example.interfaces.*;
import org.example.model.*;
import org.example.observers.ConsoleVendingObserver;
import org.example.observers.MaintenanceObserver;
import org.example.states.*;
import org.example.strategies.payment.CashPaymentStrategy;
import org.example.strategies.payment.CardPaymentStrategy;
import org.example.strategies.payment.MobilePaymentStrategy;
import org.example.strategies.selection.BasicProductSelectionStrategy;
import org.example.strategies.selection.NameBasedSelectionStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vending Machine Context: Core context class implementing the State Pattern.
 * Manages the current state and coordinates all vending machine operations.
 */
public class VendingMachineContext {
    // State Pattern
    private State currentState;
    private final Map<MachineState, State> states;

    // Core components
    private final Inventory inventory;
    private Transaction currentTransaction;

    // Strategies
    private PaymentStrategy paymentStrategy;
    private ProductSelectionStrategy selectionStrategy;
    private final Map<PaymentMethod, PaymentStrategy> paymentStrategies;

    // Observers
    private final List<VendingMachineObserver> observers;

    // Machine status
    private boolean outOfService;

    public VendingMachineContext() {
        // Initialize states
        this.states = new HashMap<>();
        states.put(MachineState.IDLE, new IdleState());
        states.put(MachineState.SELECTING, new SelectingState());
        states.put(MachineState.PAYMENT, new PaymentState());
        states.put(MachineState.DISPENSING, new DispensingState());
        states.put(MachineState.OUT_OF_SERVICE, new OutOfServiceState());

        this.currentState = states.get(MachineState.IDLE);

        // Initialize components
        this.inventory = new Inventory();
        this.outOfService = false;

        // Initialize payment strategies
        this.paymentStrategies = new HashMap<>();
        paymentStrategies.put(PaymentMethod.CASH, new CashPaymentStrategy());
        paymentStrategies.put(PaymentMethod.CARD, new CardPaymentStrategy());
        paymentStrategies.put(PaymentMethod.MOBILE, new MobilePaymentStrategy());

        // Default strategies
        this.paymentStrategy = paymentStrategies.get(PaymentMethod.CASH);
        this.selectionStrategy = new BasicProductSelectionStrategy();

        // Initialize observers
        this.observers = new ArrayList<>();
        addObserver(new ConsoleVendingObserver());
        addObserver(new MaintenanceObserver());

        initializeInventory();
    }

    /**
     * Initializes the vending machine with sample products.
     */
    private void initializeInventory() {
        // Beverages
        inventory.addProduct(new Product("A1", "Coke", 125, org.example.enums.ProductType.BEVERAGE), 10);
        inventory.addProduct(new Product("A2", "Pepsi", 125, org.example.enums.ProductType.BEVERAGE), 8);
        inventory.addProduct(new Product("A3", "Water", 100, org.example.enums.ProductType.BEVERAGE), 15);

        // Snacks
        inventory.addProduct(new Product("B1", "Chips", 150, org.example.enums.ProductType.SNACK), 12);
        inventory.addProduct(new Product("B2", "Chocolate Bar", 175, org.example.enums.ProductType.CANDY), 6);
        inventory.addProduct(new Product("B3", "Peanuts", 200, org.example.enums.ProductType.SNACK), 9);

        // More items
        inventory.addProduct(new Product("C1", "Gum", 75, org.example.enums.ProductType.CANDY), 20);
        inventory.addProduct(new Product("C2", "Cookies", 225, org.example.enums.ProductType.SNACK), 7);
    }

    // State Pattern methods
    public void insertCoin(CoinType coinType) {
        if (outOfService) {
            notifyObserversMaintenance("Machine is out of service");
            return;
        }

        Coin coin = new Coin(coinType);
        currentState.insertCoin(this, coin);

        // Start or update transaction
        if (currentTransaction == null) {
            currentTransaction = new Transaction();
        }
        currentTransaction.addCoin(coin);

        notifyObserversCoinInserted(coinType, coinType.getValue());
    }

    public void selectProduct(String slotId) {
        if (outOfService) {
            notifyObserversMaintenance("Machine is out of service");
            return;
        }

        String selectedSlot = selectionStrategy.selectProduct(inventory, slotId);
        if (selectedSlot != null) {
            currentState.selectProduct(this, selectedSlot);

            if (currentTransaction == null) {
                currentTransaction = new Transaction();
            }
            currentTransaction.setSelectedProductSlot(selectedSlot);
            currentTransaction.setSelectedProduct(inventory.getProduct(selectedSlot).orElse(null));

            // Transition to selecting state
            changeState(MachineState.SELECTING);
        }
    }

    public boolean processPayment() {
        if (outOfService) {
            notifyObserversMaintenance("Machine is out of service");
            return false;
        }

        if (currentTransaction == null || !currentTransaction.isPaymentSufficient()) {
            notifyObserversMaintenance("Insufficient payment");
            return false;
        }

        changeState(MachineState.PAYMENT);
        boolean success = currentState.processPayment(this);

        if (success) {
            currentTransaction.setStatus(TransactionStatus.PAYMENT_SUCCESS);
            changeState(MachineState.DISPENSING);
            dispenseProduct();
        } else {
            currentTransaction.setStatus(TransactionStatus.PAYMENT_FAILED);
            notifyObserversMaintenance("Payment failed");
        }

        return success;
    }

    public void dispenseProduct() {
        if (currentTransaction != null && currentTransaction.getSelectedProductSlot() != null) {
            String slotId = currentTransaction.getSelectedProductSlot();
            Product product = currentTransaction.getSelectedProduct();

            if (inventory.dispenseProduct(slotId)) {
                currentState.dispenseProduct(this);
                notifyObserversProductDispensed(slotId, product.getName());

                // Calculate and return change
                int changeAmount = currentTransaction.getChangeAmount();
                if (changeAmount > 0) {
                    returnChange(changeAmount);
                    currentTransaction.setChangeReturned(changeAmount);
                }

                currentTransaction.setStatus(TransactionStatus.COMPLETED);
                resetTransaction();
                changeState(MachineState.IDLE);
            } else {
                notifyObserversMaintenance("Failed to dispense product from slot " + slotId);
                currentTransaction.setStatus(TransactionStatus.ERROR);
            }
        }
    }

    public void cancelTransaction() {
        if (currentTransaction != null) {
            currentState.cancelTransaction(this);

            // Refund payment
            int refundAmount = currentTransaction.getAmountPaid();
            if (refundAmount > 0) {
                returnChange(refundAmount);
                currentTransaction.setChangeReturned(refundAmount);
            }

            currentTransaction.setStatus(TransactionStatus.CANCELLED);
            resetTransaction();
            changeState(MachineState.IDLE);
        }
    }

    public void setServiceMode(boolean inService) {
        currentState.setServiceMode(this, inService);
        this.outOfService = inService;

        if (inService) {
            changeState(MachineState.OUT_OF_SERVICE);
        } else {
            changeState(MachineState.IDLE);
        }
    }

    // Strategy setters
    public void setPaymentMethod(PaymentMethod method) {
        PaymentStrategy strategy = paymentStrategies.get(method);
        if (strategy != null) {
            this.paymentStrategy = strategy;
            if (currentTransaction != null) {
                currentTransaction.setPaymentMethod(method);
            }
        }
    }

    public void setSelectionStrategy(ProductSelectionStrategy strategy) {
        this.selectionStrategy = strategy;
    }

    // Observer methods
    public void addObserver(VendingMachineObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(VendingMachineObserver observer) {
        observers.remove(observer);
    }

    // Utility methods
    public void changeState(MachineState newState) {
        MachineState oldState = getCurrentMachineState();
        this.currentState = states.get(newState);
        notifyObserversStateChange(oldState, newState);
    }

    private void resetTransaction() {
        this.currentTransaction = null;
    }

    private void returnChange(int amount) {
        if (paymentStrategy instanceof CashPaymentStrategy) {
            CashPaymentStrategy cashStrategy = (CashPaymentStrategy) paymentStrategy;
            List<CoinType> change = cashStrategy.calculateChange(amount);
            if (!change.isEmpty()) {
                System.out.println("Returning change: " + change);
            } else {
                System.out.println("Unable to make exact change for $" + String.format("%.2f", amount / 100.0));
            }
        }
    }

    // Observer notification methods
    private void notifyObserversStateChange(MachineState oldState, MachineState newState) {
        for (VendingMachineObserver observer : observers) {
            observer.onStateChange(oldState, newState);
        }
    }

    private void notifyObserversTransactionUpdate(String transactionId, TransactionStatus status) {
        for (VendingMachineObserver observer : observers) {
            observer.onTransactionUpdate(transactionId, status);
        }
    }

    private void notifyObserversProductDispensed(String slotId, String productName) {
        for (VendingMachineObserver observer : observers) {
            observer.onProductDispensed(slotId, productName);
        }
    }

    private void notifyObserversCoinInserted(CoinType coinType, int amount) {
        for (VendingMachineObserver observer : observers) {
            observer.onCoinInserted(coinType, amount);
        }
    }

    private void notifyObserversMaintenance(String message) {
        for (VendingMachineObserver observer : observers) {
            observer.onMaintenanceAlert(message);
        }
    }

    // Getters
    public MachineState getCurrentMachineState() {
        for (Map.Entry<MachineState, State> entry : states.entrySet()) {
            if (entry.getValue() == currentState) {
                return entry.getKey();
            }
        }
        return MachineState.IDLE;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    public ProductSelectionStrategy getSelectionStrategy() {
        return selectionStrategy;
    }

    public boolean isOutOfService() {
        return outOfService;
    }

    public List<VendingMachineObserver> getObservers() {
        return new ArrayList<>(observers);
    }
}
