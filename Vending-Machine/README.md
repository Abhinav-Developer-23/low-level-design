# Vending Machine System - Low Level Design

A comprehensive Vending Machine implementation demonstrating the **State Pattern** along with other design patterns and SOLID principles.

## Features

✅ **State-Based Machine Behavior**
- Machine behavior changes based on current state
- Smooth state transitions (Idle → Selecting → Payment → Dispensing)
- Proper state validation and error handling

✅ **Product & Inventory Management**
- Add and restock products
- Track inventory levels in real-time
- Automatic low stock alerts
- Thread-safe inventory operations

✅ **Payment Processing**
- Multiple payment methods (Cash, Card, Mobile)
- Support for coins and bills
- Automatic change calculation
- Transaction cancellation and refunds

✅ **Notifications & Monitoring**
- Real-time event notifications
- Maintenance tracking and reporting
- Revenue and transaction statistics

✅ **Thread Safety**
- Thread-safe Singleton implementation
- Concurrent data structures (ConcurrentHashMap, AtomicInteger)
- Synchronized critical sections

## Design Patterns Used

### 1. State Pattern ⭐ (Primary Pattern)
**Classes:** `IdleState`, `SelectingState`, `PaymentState`, `DispensingState`

The State Pattern is the core of this design. The vending machine changes its behavior based on its current state.

```java
// State interface
public interface State {
    void selectProduct(Product product);
    void insertCoin(double amount);
    void insertCard(String cardNumber, double amount);
    void dispenseProduct();
    void cancel();
}

// Usage
VendingMachineSystem machine = VendingMachineSystem.getInstance();
machine.selectProduct("A1");  // Transitions: Idle → Selecting
machine.insertCoin(1.50);     // Transitions: Selecting → Payment → Dispensing
```

**States:**
- **IdleState**: Waiting for customer interaction
- **SelectingState**: Product selected, waiting for payment
- **PaymentState**: Payment received, ready to dispense
- **DispensingState**: Dispensing product and returning change

**Benefits:**
- ✅ Clean separation of state-specific behavior
- ✅ Easy to add new states
- ✅ Eliminates complex conditional logic
- ✅ Each state is testable independently

### 2. Singleton Pattern
**Class:** `VendingMachineSystem`

Ensures only one instance of the vending machine exists. Uses thread-safe double-checked locking.

```java
VendingMachineSystem machine = VendingMachineSystem.getInstance();
```

### 3. Strategy Pattern
**Interface:** `PaymentStrategy`

**Implementations:** `CashPaymentStrategy`, `CardPaymentStrategy`, `MobilePaymentStrategy`

Allows runtime selection of payment processing algorithm.

```java
// Different payment strategies can be used interchangeably
PaymentStrategy cashPayment = new CashPaymentStrategy();
PaymentStrategy cardPayment = new CardPaymentStrategy();
PaymentStrategy mobilePayment = new MobilePaymentStrategy();
```

### 4. Observer Pattern
**Interface:** `VendingMachineObserver`

**Implementations:** `ConsoleVendingObserver`, `MaintenanceObserver`

Notifies interested parties about vending machine events.

```java
machine.registerObserver(new ConsoleVendingObserver("Console"));
machine.registerObserver(new MaintenanceObserver());
```

## SOLID Principles

### Single Responsibility Principle (SRP) ✅
Each class has one clear responsibility:
- `Product` - represents a product
- `Inventory` - manages stock
- `Transaction` - represents a transaction
- `State` implementations - handle state-specific behavior
- `VendingMachineSystem` - orchestrates the system

### Open/Closed Principle (OCP) ✅
System is open for extension but closed for modification:
- New states can be added without modifying existing states
- New payment strategies can be added without changing payment processing
- New observers can be added without modifying the notification system

### Liskov Substitution Principle (LSP) ✅
All implementations are interchangeable:
- Any `State` can replace another `State`
- Any `PaymentStrategy` can replace another
- Any `VendingMachineObserver` can replace another

### Interface Segregation Principle (ISP) ✅
Small, focused interfaces:
- `State` - only state transition methods
- `PaymentStrategy` - only payment methods
- `VendingMachineObserver` - only notification methods

### Dependency Inversion Principle (DIP) ✅
High-level modules depend on abstractions:
- `VendingMachineContext` depends on `State` interface
- System depends on `PaymentStrategy` interface
- Notification system depends on `VendingMachineObserver` interface

## Architecture Overview

```
VendingMachineSystem (Singleton)
         ↓
VendingMachineContext (State Context)
         ↓
    State (interface)
         ↓
    +-----------+-----------+-----------+
    |           |           |           |
IdleState  SelectingState  PaymentState  DispensingState

Inventory ← Thread-safe with ConcurrentHashMap + AtomicInteger
    ↓
  Product

Transaction ← Immutable transaction records

Observers ← ConsoleVendingObserver, MaintenanceObserver
```

## State Transition Diagram

```
┌─────────────┐
│  IdleState  │ ←──────────────────────────────┐
└──────┬──────┘                                 │
       │ selectProduct()                        │
       ↓                                        │
┌─────────────────┐                            │
│ SelectingState  │                             │
└────────┬────────┘                            │
         │ insertCoin() / insertCard()         │
         │ (payment >= price)                  │
         ↓                                      │
┌─────────────────┐                            │
│  PaymentState   │                             │
└────────┬────────┘                            │
         │ dispenseProduct()                    │
         ↓                                      │
┌─────────────────┐                            │
│ DispensingState │ ───────────────────────────┘
└─────────────────┘  (after dispensing)

Note: cancel() from SelectingState returns to IdleState
```

## Project Structure

```
Vending-Machine/
├── src/main/java/org/example/
│   ├── enums/
│   │   ├── MachineState.java
│   │   ├── CoinType.java
│   │   ├── ProductType.java
│   │   ├── PaymentMethod.java
│   │   └── TransactionStatus.java
│   ├── interfaces/
│   │   ├── State.java ⭐
│   │   ├── PaymentStrategy.java
│   │   └── VendingMachineObserver.java
│   ├── model/
│   │   ├── Product.java
│   │   ├── Inventory.java
│   │   └── Transaction.java
│   ├── states/ ⭐
│   │   ├── IdleState.java
│   │   ├── SelectingState.java
│   │   ├── PaymentState.java
│   │   └── DispensingState.java
│   ├── strategies/
│   │   ├── CashPaymentStrategy.java
│   │   ├── CardPaymentStrategy.java
│   │   └── MobilePaymentStrategy.java
│   ├── observers/
│   │   ├── ConsoleVendingObserver.java
│   │   └── MaintenanceObserver.java
│   ├── system/
│   │   ├── VendingMachineContext.java ⭐
│   │   └── VendingMachineSystem.java
│   └── Main.java
├── README.md
└── pom.xml
```

## Usage Example

```java
// Get singleton instance
VendingMachineSystem machine = VendingMachineSystem.getInstance();

// Register observers
machine.registerObserver(new ConsoleVendingObserver("Console"));
machine.registerObserver(new MaintenanceObserver());

// Add products
machine.addProduct("A1", "Coca Cola", 1.50, ProductType.BEVERAGE, 140, 5);
machine.addProduct("B1", "Lays Chips", 2.00, ProductType.SNACK, 160, 4);

// Display inventory
machine.displayInventory();

// Scenario 1: Cash purchase
machine.selectProduct("A1");     // Select Coca Cola ($1.50)
machine.insertCoin(0.50);        // Insert $0.50
machine.insertCoin(0.50);        // Insert $0.50
machine.insertCoin(0.50);        // Insert $0.50 - Total: $1.50
// Product automatically dispensed when payment complete

// Scenario 2: Card purchase with change
machine.selectProduct("B1");     // Select Lays Chips ($2.00)
machine.insertCard("1234-5678-9012-3456", 2.50);  // Card payment
// Product dispensed, change returned: $0.50

// Scenario 3: Cancellation
machine.selectProduct("A1");     // Select product
machine.insertCoin(0.50);        // Partial payment
machine.cancelTransaction();     // Cancel and refund
```

## Running the Demo

```bash
cd Vending-Machine
mvn clean compile
mvn exec:java -Dexec.mainClass="org.example.Main"
```

## Thread Safety Features

1. **Thread-safe Singleton:** Double-checked locking with volatile keyword
2. **Concurrent Collections:**
   - `ConcurrentHashMap` for product inventory
   - `CopyOnWriteArrayList` for observer list
3. **Atomic Counters:**
   - `AtomicInteger` for stock counts
   - `AtomicLong` for transaction ID generation
4. **Synchronized Methods:**
   - Critical sections for stock updates
   - Transaction status updates

## Key Design Decisions

1. **State Pattern as Core:** Machine behavior is state-driven, making code clean and maintainable
2. **Automatic State Transitions:** States automatically transition when conditions are met
3. **Immutable Products:** Thread-safe product definitions
4. **Observer Pattern:** Loose coupling for notifications and monitoring
5. **Strategy Pattern:** Flexible payment processing
6. **Defensive Programming:** Null checks, validation, and error handling
7. **Thread Safety First:** All shared data structures are thread-safe

## Comparison with Stack Overflow Design

This Vending Machine follows similar principles to the Stack Overflow system:

| Aspect | Stack Overflow | Vending Machine |
|--------|---------------|-----------------|
| **Primary Pattern** | Template Method (Post hierarchy) | State Pattern (State transitions) |
| **Singleton** | StackOverflowSystem | VendingMachineSystem |
| **Strategy** | ReputationStrategy, SearchStrategy | PaymentStrategy |
| **Observer** | NotificationObserver | VendingMachineObserver |
| **Thread Safety** | ConcurrentHashMap, AtomicInteger | Same approach |
| **SOLID** | All 5 principles | All 5 principles |

## State Pattern Advantages

1. **Clean Code:** No complex if-else chains
2. **Easy to Extend:** Add new states without modifying existing ones
3. **Testable:** Each state can be tested independently
4. **Maintainable:** State-specific logic is encapsulated
5. **Type Safe:** Compile-time checking of state transitions

## Future Enhancements

- Add temperature control for beverages
- Implement dynamic pricing (time-based, demand-based)
- Add touch screen UI interface
- Implement remote monitoring dashboard
- Add payment gateway integration
- Implement loyalty program
- Add product expiry tracking
- Implement multi-currency support

## License

This is a demonstration project for educational purposes.

---

**Design Patterns:** State ⭐, Singleton, Strategy, Observer  
**SOLID Principles:** All 5 principles applied  
**Thread Safety:** Complete thread-safe implementation  
**Production Ready:** Yes, with proper error handling and validation

