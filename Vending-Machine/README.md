# Vending Machine System - Low Level Design

A comprehensive vending machine implementation demonstrating proper design patterns and SOLID principles.

## Features

✅ **Product Management**
- Multiple product types (Beverages, Snacks, Candy)
- Inventory tracking with automatic low-stock alerts
- Product expiration monitoring
- Restocking capabilities

✅ **Payment Processing**
- Coin-based payments with change calculation
- Card payment support
- Refund processing
- Multiple payment strategies

✅ **Product Selection**
- Code-based selection (e.g., "P001")
- Name-based selection (e.g., "chips")
- Pluggable selection strategies

✅ **Transaction Management**
- Thread-safe transaction processing
- Transaction state tracking
- Automatic failure handling and refunds

✅ **Monitoring & Notifications**
- Real-time event notifications
- Console logging observer
- Maintenance monitoring with failure rate tracking
- Low inventory alerts

✅ **Thread Safety**
- Concurrent access handling
- Thread-safe collections (ConcurrentHashMap, CopyOnWriteArrayList)
- Atomic counters (AtomicInteger, AtomicLong)
- Synchronized critical sections

## Design Patterns Used

### 1. Singleton Pattern
**Class:** `VendingMachineSystem`

Ensures only one instance of the vending machine system exists. Uses double-checked locking for thread safety.

```java
VendingMachineSystem machine = VendingMachineSystem.getInstance();
```

### 2. Strategy Pattern
**Interfaces:** `PaymentStrategy`, `ProductSelectionStrategy`

**Implementations:**
- Payment strategies: `CoinPaymentStrategy`, `CardPaymentStrategy`
- Selection strategies: `BasicProductSelectionStrategy`, `NameBasedSelectionStrategy`

Allows runtime selection of payment processing and product selection algorithms.

```java
machine.setPaymentStrategy(new CardPaymentStrategy());
machine.setSelectionStrategy(new NameBasedSelectionStrategy());
```

### 3. Observer Pattern
**Interface:** `VendingMachineObserver`

**Implementations:** `ConsoleVendingObserver`, `MaintenanceObserver`

Notifies interested parties about vending machine events (product dispensed, payment received, etc.).

```java
machine.registerObserver(new ConsoleVendingObserver("LOG"));
machine.registerObserver(new MaintenanceObserver());
```

### 4. Template Method Pattern
**Class:** `VendingItem` (abstract base class)

Defines the skeleton of item validation in base class while allowing `Product` to provide specific implementations.

## SOLID Principles

### Single Responsibility Principle (SRP)
Each class has one clear responsibility:
- `Product` - manages product data and inventory
- `Transaction` - represents a purchase transaction
- `VendingMachineSystem` - orchestrates the vending machine operations

### Open/Closed Principle (OCP)
System is open for extension but closed for modification:
- New payment strategies can be added without modifying existing code
- New selection strategies can be added without changing the selection mechanism
- New observers can be added without modifying the notification system

### Liskov Substitution Principle (LSP)
All strategy implementations are interchangeable:
- Any `PaymentStrategy` can replace another
- Any `ProductSelectionStrategy` can replace another
- Any `VendingMachineObserver` can replace another

### Interface Segregation Principle (ISP)
Small, focused interfaces:
- `PaymentStrategy` - only payment-related methods
- `ProductSelectionStrategy` - only selection methods
- `VendingMachineObserver` - only notification methods
- `Dispensable` - only dispensing-related methods

### Dependency Inversion Principle (DIP)
High-level modules depend on abstractions:
- `VendingMachineSystem` depends on `PaymentStrategy` interface, not concrete implementations
- Selection functionality depends on `ProductSelectionStrategy` interface
- Notification system depends on `VendingMachineObserver` interface

## Thread Safety Features

1. **Thread-safe Singleton:** Double-checked locking with volatile keyword
2. **Concurrent Collections:**
   - `ConcurrentHashMap` for products and transactions
   - `CopyOnWriteArrayList` for observers
3. **Atomic Counters:**
   - `AtomicInteger` for product quantities
   - `AtomicLong` for ID generation
4. **Synchronized Methods:**
   - Critical sections for transaction processing
   - State changes and inventory updates

## Project Structure

```
Vending-Machine/
├── src/main/java/org/example/
│   ├── enums/
│   │   ├── MachineState.java
│   │   ├── ProductType.java
│   │   ├── CoinType.java
│   │   ├── PaymentMethod.java
│   │   └── TransactionStatus.java
│   ├── interfaces/
│   │   ├── PaymentStrategy.java
│   │   ├── ProductSelectionStrategy.java
│   │   ├── VendingMachineObserver.java
│   │   └── Dispensable.java
│   ├── model/
│   │   ├── Product.java
│   │   ├── Coin.java
│   │   ├── Transaction.java
│   │   └── VendingItem.java (abstract)
│   ├── strategies/
│   │   ├── payment/
│   │   │   ├── CoinPaymentStrategy.java
│   │   │   └── CardPaymentStrategy.java
│   │   └── selection/
│   │       ├── BasicProductSelectionStrategy.java
│   │       └── NameBasedSelectionStrategy.java
│   ├── observers/
│   │   ├── ConsoleVendingObserver.java
│   │   └── MaintenanceObserver.java
│   ├── system/
│   │   └── VendingMachineSystem.java
│   └── Main.java
├── README.md
└── pom.xml
```

## Usage Example

```java
// Get vending machine instance (Singleton)
VendingMachineSystem machine = VendingMachineSystem.getInstance();

// Register observers
machine.registerObserver(new ConsoleVendingObserver("CONSOLE"));
machine.registerObserver(new MaintenanceObserver());

// Display available products
machine.displayStatus();

// Select a product
Transaction transaction = machine.selectProduct("P001");
System.out.println("Selected: " + transaction.getProduct().getName());

// Insert coins
machine.insertCoin(transaction, new Coin(CoinType.TWENTY_FIVE));
machine.insertCoin(transaction, new Coin(CoinType.TWENTY_FIVE));
machine.insertCoin(transaction, new Coin(CoinType.ONE));

// Product is automatically dispensed with change

// Try name-based selection
machine.setSelectionStrategy(new NameBasedSelectionStrategy());
Transaction chipsTransaction = machine.selectProduct("chips");
```

## Running the Demo

```bash
cd Vending-Machine
mvn clean compile
mvn exec:java -Dexec.mainClass="org.example.Main"
```

## Requirements Met

✅ Users can select products by code or name  
✅ Multiple payment methods supported (coins, card)  
✅ Change calculation and dispensing  
✅ Inventory management with low-stock alerts  
✅ Transaction processing with failure handling  
✅ Real-time notifications and monitoring  
✅ Thread-safe concurrent operations  
✅ Proper design patterns implemented (Singleton, Strategy, Observer, Template Method)  
✅ SOLID principles followed throughout the design  

## Key Design Decisions

1. **Abstract base classes:** `VendingItem` provides template for item validation
2. **Strategy pattern for flexibility:** Payment and selection strategies are pluggable
3. **Observer pattern for monitoring:** Multiple observers can monitor different aspects
4. **Thread safety first:** All shared data structures are thread-safe
5. **Immutable enums:** All enums are properly defined with validation
6. **Defensive copying:** Collections returned from getters are copies
7. **Fail-fast validation:** Input validation prevents invalid states
8. **Clear separation of concerns:** Each class has a single responsibility

## Future Enhancements

- Add user authentication and loyalty programs
- Implement product categories and filtering
- Add temperature monitoring for perishable items
- Implement sales analytics and reporting
- Add remote monitoring and management
- Implement energy-saving features
- Add multi-language support
- Implement touch screen interface simulation

## License

This is a demonstration project for educational purposes.
