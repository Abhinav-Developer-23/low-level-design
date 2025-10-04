package org.example;

import org.example.enums.BookCategory;
import org.example.enums.OrderStatus;
import org.example.enums.PaymentType;
import org.example.model.*;
import org.example.system.BookStoreSystem;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Book Store System ===\n");

        // Initialize system
        BookStoreSystem bookStore = BookStoreSystem.getInstance();

        // Create Admin
        Admin admin = new Admin("A001", "Admin User", "admin@bookstore.com");
        bookStore.registerUser(admin);
        admin.showDashboard();
        System.out.println();

        // Admin adds books to inventory
        System.out.println("--- Adding Books to Inventory ---");
        Book book1 = new Book("978-0-13-468599-1", "Effective Java", "Joshua Bloch", 45.99, BookCategory.TECHNOLOGY, 10);
        Book book2 = new Book("978-0-596-52068-7", "Design Patterns", "Gang of Four", 54.99, BookCategory.TECHNOLOGY, 5);
        Book book3 = new Book("978-0-06-112008-4", "To Kill a Mockingbird", "Harper Lee", 18.99, BookCategory.FICTION, 15);
        Book book4 = new Book("978-0-545-01022-1", "Harry Potter", "J.K. Rowling", 29.99, BookCategory.FICTION, 20);
        Book book5 = new Book("978-0-316-76948-0", "Astrophysics for People in a Hurry", "Neil deGrasse Tyson", 19.99, BookCategory.SCIENCE, 8);

        bookStore.addBookToInventory(book1);
        bookStore.addBookToInventory(book2);
        bookStore.addBookToInventory(book3);
        bookStore.addBookToInventory(book4);
        bookStore.addBookToInventory(book5);
        System.out.println();

        // Create Customer
        System.out.println("--- Customer Registration ---");
        Customer customer = new Customer("C001", "John Doe", "john@example.com");
        bookStore.registerUser(customer);
        customer.showDashboard();
        System.out.println();

        // Customer searches for books
        System.out.println("--- Searching Books ---");
        System.out.println("Searching for 'Java':");
        var searchResults = bookStore.searchBooksByTitle("Java");
        for (Book book : searchResults) {
            System.out.println("  - " + book.getTitle() + " by " + book.getAuthor() + " ($" + book.getPrice() + ")");
        }
        System.out.println();

        // Customer adds books to cart
        System.out.println("--- Adding Books to Cart ---");
        bookStore.addToCart(customer, "978-0-13-468599-1", 2);  // Effective Java x2
        bookStore.addToCart(customer, "978-0-545-01022-1", 1);  // Harry Potter x1
        bookStore.addToCart(customer, "978-0-316-76948-0", 1);  // Astrophysics x1
        System.out.println();

        // View cart
        System.out.println("--- Shopping Cart ---");
        System.out.println("Total items: " + customer.getCart().getItems().size());
        System.out.println("Total price: $" + customer.getCart().getTotalPrice());
        System.out.println();

        // Place order with payment
        System.out.println("--- Placing Order ---");
        Order order1 = bookStore.placeOrder(customer, PaymentType.CREDIT_CARD);
        System.out.println();

        if (order1 != null) {
            // Display order details
            System.out.println("--- Order Details ---");
            System.out.println("Order ID: " + order1.getOrderId());
            System.out.println("Customer: " + order1.getCustomer().getName());
            System.out.println("Status: " + order1.getStatus());
            System.out.println("Total Amount: $" + order1.getTotalAmount());
            System.out.println("Order Items:");
            for (var entry : order1.getOrderItems().entrySet()) {
                System.out.println("  - " + entry.getKey().getTitle() + " x" + entry.getValue());
            }
            System.out.println();

            // Admin updates order status
            System.out.println("--- Updating Order Status ---");
            bookStore.updateOrderStatus(order1.getOrderId(), OrderStatus.SHIPPED);
            bookStore.updateOrderStatus(order1.getOrderId(), OrderStatus.DELIVERED);
            System.out.println();
        }

        // Customer places another order with different payment
        System.out.println("--- Second Order ---");
        bookStore.addToCart(customer, "978-0-596-52068-7", 1);  // Design Patterns
        bookStore.addToCart(customer, "978-0-06-112008-4", 2);  // To Kill a Mockingbird x2
        System.out.println("Cart Total: $" + customer.getCart().getTotalPrice());
        Order order2 = bookStore.placeOrder(customer, PaymentType.UPI);
        System.out.println();

        // Display all orders
        System.out.println("--- All Orders in System ---");
        for (Order order : bookStore.getAllOrders()) {
            System.out.println("Order " + order.getOrderId() + " - Status: " + order.getStatus() + " - Amount: $" + order.getTotalAmount());
        }
        System.out.println();

        // Check updated stock
        System.out.println("--- Updated Stock Levels ---");
        System.out.println("Effective Java stock: " + book1.getStock());
        System.out.println("Harry Potter stock: " + book4.getStock());
        System.out.println("Design Patterns stock: " + book2.getStock());

        System.out.println("\n=== Book Store System Demo Complete ===");
    }
}
