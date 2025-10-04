package org.example.system;

import org.example.enums.OrderStatus;
import org.example.enums.PaymentType;
import org.example.interfaces.Payment;
import org.example.model.*;
import org.example.strategies.*;

import java.util.*;

public class BookStoreSystem {
    private static BookStoreSystem instance;
    private Inventory inventory;
    private Map<String, User> users;
    private Map<String, Order> orders;
    private int orderCounter;

    private BookStoreSystem() {
        this.inventory = Inventory.getInstance();
        this.users = new HashMap<>();
        this.orders = new HashMap<>();
        this.orderCounter = 1;
    }

    public static BookStoreSystem getInstance() {
        if (instance == null) {
            instance = new BookStoreSystem();
        }
        return instance;
    }

    // User Management
    public void registerUser(User user) {
        users.put(user.getId(), user);
        System.out.println("User registered: " + user.getName());
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    // Inventory Management (Admin functionality)
    public void addBookToInventory(Book book) {
        inventory.addBook(book);
        System.out.println("Book added to inventory: " + book.getTitle());
    }

    // Search functionality
    public List<Book> searchBooksByTitle(String title) {
        return inventory.searchByTitle(title);
    }

    public Book getBookByISBN(String isbn) {
        return inventory.getBookByISBN(isbn);
    }

    // Shopping Cart Operations
    public void addToCart(Customer customer, String isbn, int quantity) {
        Book book = inventory.getBookByISBN(isbn);
        if (book != null && book.isAvailable()) {
            customer.getCart().addBook(book, quantity);
            System.out.println("Added " + quantity + " x " + book.getTitle() + " to cart");
        } else {
            System.out.println("Book not available or doesn't exist");
        }
    }

    public void removeFromCart(Customer customer, String isbn) {
        Book book = inventory.getBookByISBN(isbn);
        if (book != null) {
            customer.getCart().removeBook(book);
            System.out.println("Removed " + book.getTitle() + " from cart");
        }
    }

    // Order Processing
    public Order placeOrder(Customer customer, PaymentType paymentType) {
        if (customer.getCart().getItems().isEmpty()) {
            System.out.println("Cart is empty. Cannot place order.");
            return null;
        }

        // Create order
        String orderId = "ORD" + String.format("%04d", orderCounter++);
        Order order = new Order(orderId, customer);

        // Process payment
        Payment paymentStrategy = getPaymentStrategy(paymentType);
        boolean paymentSuccess = paymentStrategy.pay(order.getTotalAmount());

        if (paymentSuccess) {
            // Reduce stock for each item
            for (Map.Entry<Book, Integer> entry : order.getOrderItems().entrySet()) {
                entry.getKey().reduceStock(entry.getValue());
            }

            // Save order
            orders.put(orderId, order);

            // Clear cart
            customer.getCart().clear();

            System.out.println("Order placed successfully! Order ID: " + orderId);
            return order;
        } else {
            System.out.println("Payment failed. Order not placed.");
            return null;
        }
    }

    private Payment getPaymentStrategy(PaymentType paymentType) {
        switch (paymentType) {
            case CREDIT_CARD:
                return new CreditCardPayment();
            case DEBIT_CARD:
                return new DebitCardPayment();
            case NET_BANKING:
                return new NetBankingPayment();
            case UPI:
                return new UpiPayment();
            default:
                return new UpiPayment();
        }
    }

    // Order Management
    public void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.updateStatus(status);
            System.out.println("Order " + orderId + " status updated to: " + status);
        } else {
            System.out.println("Order not found");
        }
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }
}

