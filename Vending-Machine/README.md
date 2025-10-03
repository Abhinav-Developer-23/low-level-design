# Vending Machine - Low Level Design (LLD)

A comprehensive Java implementation of a Vending Machine system using multiple design patterns including State, Strategy, Observer, and Facade patterns.

## Design Patterns Used

### 1. State Pattern
- **Purpose**: Manages different operational states of the vending machine
- **States**: Idle, Selecting, Payment, Dispensing
- **Benefits**: Clean state transitions and proper behavior in each state

### 2. Strategy Pattern
- **Payment Strategies**:
  - Cash Payment
  - Card Payment
  - Mobile Payment
- **Selection Strategies**:
  - Slot ID based selection
  - Name-based selection
- **Benefits**: Easy to add new payment methods and selection criteria

### 3. Observer Pattern
- **Purpose**: Notifies different components about vending machine events
- **Observers**:
  - Console Observer (logs events)
  - Maintenance Observer (monitors machine health)
- **Benefits**: Decoupled event handling and monitoring

### 4. Facade Pattern
- **Purpose**: Provides simplified interface to complex system
- **Facade**: `VendingMachineSystem` class
- **Benefits**: Easy-to-use high-level API

## Project Structure

```
src/main/java/org/example/
├── enums/                          # Enumeration classes
│   ├── CoinType.java              # Coin denominations
│   ├── MachineState.java          # Vending machine states
│   ├── PaymentMethod.java         # Payment methods
│   ├── ProductType.java           # Product categories
│   └── TransactionStatus.java     # Transaction states
├── interfaces/                     # Interface definitions
│   ├── State.java                 # State pattern interface
│   ├── PaymentStrategy.java       # Payment strategy interface
│   ├── ProductSelectionStrategy.java # Selection strategy interface
│   └── VendingMachineObserver.java # Observer interface
├── model/                         # Data model classes
│   ├── Coin.java                  # Coin representation
│   ├── Product.java               # Product representation
│   ├── Inventory.java             # Inventory management
│   └── Transaction.java           # Transaction tracking
├── states/                        # State pattern implementations
│   ├── IdleState.java             # Ready for product selection
│   ├── SelectingState.java        # Product selected, awaiting payment
│   ├── PaymentState.java          # Processing payment
│   └── DispensingState.java       # Dispensing product
├── strategies/                    # Strategy pattern implementations
│   ├── payment/
│   │   ├── CashPaymentStrategy.java
│   │   ├── CardPaymentStrategy.java
│   │   └── MobilePaymentStrategy.java
│   └── selection/
│       ├── BasicProductSelectionStrategy.java
│       └── NameBasedSelectionStrategy.java
├── observers/                     # Observer pattern implementations
│   ├── ConsoleVendingObserver.java
│   └── MaintenanceObserver.java
└── system/                        # Main system classes
    ├── VendingMachineContext.java  # Core context class
    └── VendingMachineSystem.java   # High-level facade
```

## Key Features

### State Management
- **Idle State**: Machine ready for product selection
- **Selecting State**: Product selected, accepting coins
- **Payment State**: Processing payment validation
- **Dispensing State**: Dispensing product and returning change

### Payment Processing
- **Cash Payment**: Validates coin insertion and calculates change
- **Card Payment**: Simulates card authorization (90% success rate)
- **Mobile Payment**: Simulates mobile payment processing (85% success rate)

### Product Selection
- **Slot ID Selection**: Select products by machine slot (e.g., "A1", "B2")
- **Name-based Selection**: Search products by name or type

### Inventory Management
- Track product quantities and availability
- Automatic restocking capabilities
- Total value and item count calculations

### Transaction Handling
- Complete transaction lifecycle management
- Change calculation and return
- Transaction status tracking
- Error handling and rollback

### Event Notification
- Real-time event notifications for all machine activities
- Maintenance alerts for low inventory
- Transaction status updates

## Usage Examples

### Basic Product Purchase
```java
VendingMachineSystem vendingMachine = new VendingMachineSystem();

// Display available products
vendingMachine.displayInventory();

// Select product by slot
vendingMachine.selectProductBySlot("A1");

// Insert coins
vendingMachine.insertCoin(25);  // Quarter
vendingMachine.insertCoin(25);  // Quarter
vendingMachine.insertCoin(100); // Dollar

// Process payment
vendingMachine.processPayment();
```

### Name-based Product Selection
```java
// Select product by name
vendingMachine.selectProductByName("chocolate");
```

### Different Payment Methods
```java
// Set payment method
vendingMachine.setPaymentMethod(PaymentMethod.CARD);

// Process payment with card
vendingMachine.processPayment();
```

## Running the Demo

1. Compile the project:
   ```bash
   mvn clean compile
   ```

2. Run the main class:
   ```bash
   mvn exec:java -Dexec.mainClass="org.example.Main"
   ```

3. The demo will show:
   - Basic product purchase flow
   - Name-based product selection
   - Different payment methods
   - Maintenance operations

## UML Diagram

A comprehensive UML diagram is provided in `vending_machine_uml.mmd` showing:
- Class relationships and hierarchies
- Design pattern implementations
- System architecture overview
- Key interactions between components

## Design Highlights

1. **Extensibility**: Easy to add new payment methods, product types, or selection strategies
2. **Maintainability**: Clear separation of concerns and modular design
3. **Testability**: Each component can be tested independently
4. **Scalability**: Observer pattern allows for easy addition of new event handlers
5. **Robustness**: Comprehensive error handling and state validation

## Future Enhancements

- **Database Integration**: Persistent storage for inventory and transactions
- **Network Support**: Remote monitoring and control capabilities
- **Advanced Features**: Loyalty programs, discount coupons, bulk purchases
- **Security**: Input validation, fraud detection, audit trails
- **Analytics**: Sales reporting, usage patterns, predictive maintenance

This implementation demonstrates a production-ready vending machine system that follows SOLID principles and established design patterns for maximum maintainability and extensibility.
