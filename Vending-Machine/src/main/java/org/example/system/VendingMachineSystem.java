package org.example.system;

import org.example.enums.CoinType;
import org.example.enums.MachineState;
import org.example.enums.PaymentMethod;
import org.example.enums.TransactionStatus;
import org.example.interfaces.PaymentStrategy;
import org.example.interfaces.ProductSelectionStrategy;
import org.example.interfaces.VendingMachineObserver;
import org.example.model.Coin;
import org.example.model.Product;
import org.example.model.Transaction;
import org.example.strategies.payment.CoinPaymentStrategy;
import org.example.strategies.selection.BasicProductSelectionStrategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Singleton Pattern: Central vending machine system
 * Thread-safe implementation using double-checked locking
 * Coordinates all vending machine operations
 */
public class VendingMachineSystem {

    private static volatile VendingMachineSystem instance;

    // Core data structures
    private final Map<String, Product> products;
    private final Map<String, Transaction> activeTransactions;
    private final List<VendingMachineObserver> observers;

    // Machine state
    private volatile MachineState currentState;
    private final Map<CoinType, Integer> coinInventory;

    // Strategies
    private PaymentStrategy paymentStrategy;
    private ProductSelectionStrategy selectionStrategy;

    // ID generators
    private final AtomicLong transactionIdCounter;
    private final AtomicLong productIdCounter;

    private VendingMachineSystem() {
        this.products = new ConcurrentHashMap<>();
        this.activeTransactions = new ConcurrentHashMap<>();
        this.observers = new CopyOnWriteArrayList<>();
        this.coinInventory = new ConcurrentHashMap<>();
        this.currentState = MachineState.IDLE;

        // Initialize coin inventory
        for (CoinType coinType : CoinType.values()) {
            coinInventory.put(coinType, 50); // Start with 50 coins of each type
        }

        // Default strategies
        this.paymentStrategy = new CoinPaymentStrategy();
        this.selectionStrategy = new BasicProductSelectionStrategy();

        this.transactionIdCounter = new AtomicLong(0);
        this.productIdCounter = new AtomicLong(0);

        // Initialize with some default products
        initializeDefaultProducts();
    }

    /**
     * Thread-safe singleton implementation using double-checked locking
     */
    public static VendingMachineSystem getInstance() {
        if (instance == null) {
            synchronized (VendingMachineSystem.class) {
                if (instance == null) {
                    instance = new VendingMachineSystem();
                }
            }
        }
        return instance;
    }

    // Strategy Pattern: Allow changing payment strategy
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    // Strategy Pattern: Allow changing selection strategy
    public void setSelectionStrategy(ProductSelectionStrategy strategy) {
        this.selectionStrategy = strategy;
    }

    // Observer Pattern: Register observers
    public void registerObserver(VendingMachineObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(VendingMachineObserver observer) {
        observers.remove(observer);
    }

    /**
     * Select a product for purchase
     */
    public synchronized Transaction selectProduct(String productCode) {
        if (currentState != MachineState.IDLE) {
            throw new IllegalStateException("Machine is not ready for selection");
        }

        Product product = selectionStrategy.selectProduct(getAvailableProducts(), productCode);
        if (product == null) {
            throw new IllegalArgumentException("Product not found or unavailable: " + productCode);
        }

        currentState = MachineState.SELECTING;
        String transactionId = "T" + transactionIdCounter.incrementAndGet();
        Transaction transaction = new Transaction(transactionId, product, PaymentMethod.COINS);
        activeTransactions.put(transactionId, transaction);

        currentState = MachineState.PAYING;
        return transaction;
    }

    /**
     * Insert coins into the machine
     */
    public synchronized void insertCoin(Transaction transaction, Coin coin) {
        if (currentState != MachineState.PAYING) {
            throw new IllegalStateException("Machine is not ready to accept coins");
        }

        transaction.addCoin(coin);

        // Add coin to inventory
        coinInventory.put(coin.getType(), coinInventory.get(coin.getType()) + 1);

        // Notify observers
        observers.forEach(observer -> observer.onPaymentReceived(transaction));

        // Check if payment is complete
        if (transaction.getAmountPaid() >= transaction.getRequiredAmount()) {
            completeTransaction(transaction);
        }
    }

    /**
     * Complete the transaction and dispense product
     */
    private synchronized void completeTransaction(Transaction transaction) {
        if (!paymentStrategy.processPayment(transaction)) {
            transaction.setStatus(TransactionStatus.FAILED);
            observers.forEach(observer ->
                observer.onTransactionFailed(transaction, "Payment processing failed"));
            refundTransaction(transaction);
            return;
        }

        // Dispense product
        Product product = transaction.getProduct();
        if (product.dispense()) {
            transaction.setStatus(TransactionStatus.COMPLETED);
            observers.forEach(observer -> observer.onProductDispensed(product, transaction));

            // Return change if any
            if (transaction.getChangeAmount() > 0) {
                dispenseChange(transaction);
            }

            currentState = MachineState.IDLE;
            activeTransactions.remove(transaction.getTransactionId());
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
            observers.forEach(observer ->
                observer.onTransactionFailed(transaction, "Product dispensing failed"));
            refundTransaction(transaction);
        }
    }

    /**
     * Cancel transaction and refund
     */
    public synchronized void cancelTransaction(Transaction transaction) {
        if (transaction.getStatus() != TransactionStatus.COMPLETED) {
            refundTransaction(transaction);
        }
    }

    /**
     * Process refund for transaction
     */
    private synchronized void refundTransaction(Transaction transaction) {
        if (paymentStrategy.processRefund(transaction)) {
            double refundAmount = transaction.getAmountPaid();
            observers.forEach(observer -> observer.onRefundProcessed(transaction, refundAmount));
        }
        currentState = MachineState.IDLE;
        activeTransactions.remove(transaction.getTransactionId());
    }

    /**
     * Dispense change coins
     */
    private synchronized void dispenseChange(Transaction transaction) {
        List<Coin> changeCoins = transaction.getChangeCoins();
        for (Coin coin : changeCoins) {
            // Remove from inventory
            CoinType coinType = coin.getType();
            coinInventory.put(coinType, coinInventory.get(coinType) - 1);
        }
    }

    /**
     * Add a new product to the machine
     */
    public synchronized void addProduct(Product product) {
        products.put(product.getProductId(), product);
    }

    /**
     * Restock a product
     */
    public synchronized void restockProduct(String productId, int quantity) {
        Product product = products.get(productId);
        if (product != null) {
            product.restock(quantity);
        }
    }

    /**
     * Get all available products
     */
    public List<Product> getAvailableProducts() {
        return products.values().stream()
                .filter(Product::isAvailable)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get product by ID
     */
    public Product getProduct(String productId) {
        return products.get(productId);
    }

    /**
     * Get current machine state
     */
    public MachineState getCurrentState() {
        return currentState;
    }

    /**
     * Get active transaction
     */
    public Transaction getActiveTransaction(String transactionId) {
        return activeTransactions.get(transactionId);
    }

    /**
     * Initialize machine with default products
     */
    private void initializeDefaultProducts() {
        addProduct(new Product("P001", "Coca Cola", org.example.enums.ProductType.BEVERAGE, 1.50, 10));
        addProduct(new Product("P002", "Pepsi", org.example.enums.ProductType.BEVERAGE, 1.50, 8));
        addProduct(new Product("P003", "Lays Chips", org.example.enums.ProductType.SNACK, 1.25, 12));
        addProduct(new Product("P004", "Snickers", org.example.enums.ProductType.CANDY, 1.00, 15));
        addProduct(new Product("P005", "Water Bottle", org.example.enums.ProductType.BEVERAGE, 1.00, 20));
        addProduct(new Product("P006", "Doritos", org.example.enums.ProductType.SNACK, 1.25, 6));
    }

    /**
     * Display machine status
     */
    public void displayStatus() {
        System.out.println("=== VENDING MACHINE STATUS ===");
        System.out.println("State: " + currentState);
        System.out.println("Available Products:");
        getAvailableProducts().forEach(product ->
            System.out.printf("  %s: %s ($%.2f) - %d remaining%n",
                product.getProductId(), product.getName(), product.getPrice(), product.getQuantity()));
        System.out.println("Coin Inventory:");
        coinInventory.forEach((type, count) ->
            System.out.printf("  %s: %d coins%n", type, count));
        System.out.println("Active Transactions: " + activeTransactions.size());
        System.out.println("================================");
    }
}
