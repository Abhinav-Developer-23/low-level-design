# Vending Machine - System Architecture

## 🏗️ High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    VendingMachineSystem                         │
│                        (Singleton)                              │
│  - Single instance across application                           │
│  - Thread-safe initialization                                   │
│  - Manages inventory, observers, context                        │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         │ delegates to
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│                  VendingMachineContext                          │
│                    (State Context)                              │
│  - Holds current state                                          │
│  - Delegates operations to current state                        │
│  - Manages observers and strategies                             │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         │ maintains reference to
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│                       State (Interface)                         │
│  - selectProduct()                                              │
│  - insertCoin()                                                 │
│  - insertCard()                                                 │
│  - insertMobilePayment()                                        │
│  - dispenseProduct()                                            │
│  - cancel()                                                     │
└────────────────────────┬────────────────────────────────────────┘
                         │
         ┌───────────────┼───────────────┬────────────────┐
         │               │               │                │
         ↓               ↓               ↓                ↓
┌────────────┐  ┌────────────────┐  ┌──────────┐  ┌──────────────┐
│ IdleState  │  │ SelectingState │  │ Payment  │  │ Dispensing   │
│            │  │                │  │  State   │  │    State     │
└────────────┘  └────────────────┘  └──────────┘  └──────────────┘
```

---

## 🎭 State Pattern Architecture

### State Transition Diagram

```
                    ┌──────────────┐
         ┌─────────→│  IdleState   │←─────────┐
         │          │              │          │
         │          └──────┬───────┘          │
         │                 │                  │
         │                 │ selectProduct()  │
         │                 ↓                  │
         │          ┌────────────────┐        │
         │          │ SelectingState │        │
         │          │                │        │
         │    ┌─────┤ - Accept coins │        │
         │    │     │ - Accept card  │        │
         │    │     │ - Accept mobile│        │
         │    │     └───────┬────────┘        │
         │    │             │                 │
         │    │ cancel()    │ payment >= price
         │    │             ↓                 │
         │    │      ┌─────────────┐         │
         │    │      │ PaymentState│         │
         │    │      └──────┬──────┘         │
         │    │             │                │
         │    │             │ dispenseProduct()
         │    │             ↓                │
         │    │      ┌───────────────┐      │
         │    └────→ │ DispensingState│─────┘
         │           │                │ after dispensing
         │           └────────────────┘
         │                   │
         └───────────────────┘
              (refund on cancel)
```

### State Responsibilities

```
┌─────────────────────────────────────────────────────────────────┐
│ IdleState                                                       │
├─────────────────────────────────────────────────────────────────┤
│ ✓ selectProduct()        → SelectingState                      │
│ ✗ insertCoin()           → "Select product first"              │
│ ✗ dispenseProduct()      → "No product selected"               │
│ ✗ cancel()               → "Nothing to cancel"                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│ SelectingState                                                  │
├─────────────────────────────────────────────────────────────────┤
│ ✗ selectProduct()        → "Already selected"                  │
│ ✓ insertCoin()           → Accumulate, check if enough         │
│ ✓ insertCard()           → Process, go to PaymentState         │
│ ✓ insertMobilePayment()  → Process, go to PaymentState         │
│ ✗ dispenseProduct()      → "Complete payment first"            │
│ ✓ cancel()               → Refund, go to IdleState             │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│ PaymentState                                                    │
├─────────────────────────────────────────────────────────────────┤
│ ✗ selectProduct()        → "Transaction in progress"           │
│ ✗ insertCoin()           → "Already paid"                      │
│ ✓ dispenseProduct()      → Go to DispensingState               │
│ ✗ cancel()               → "Cannot cancel"                     │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│ DispensingState                                                 │
├─────────────────────────────────────────────────────────────────┤
│ ✗ selectProduct()        → "Please wait"                       │
│ ✗ insertCoin()           → "Please wait"                       │
│ ✓ dispenseProduct()      → Actually dispense, go to IdleState  │
│ ✗ cancel()               → "Cannot cancel"                     │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📦 Component Architecture

### Model Layer

```
┌───────────────────────────────────────────────────────────────┐
│ Product (Immutable)                                           │
│ - productId, name, price, type, calories                      │
└───────────────────────────────────────────────────────────────┘

┌───────────────────────────────────────────────────────────────┐
│ Inventory (Thread-Safe)                                       │
│ - ConcurrentHashMap<String, Product>                          │
│ - ConcurrentHashMap<String, AtomicInteger>                    │
│ - addProduct(), restockProduct(), decrementStock()            │
└───────────────────────────────────────────────────────────────┘

┌───────────────────────────────────────────────────────────────┐
│ Transaction (Immutable)                                       │
│ - transactionId, product, amount, change, method, status      │
└───────────────────────────────────────────────────────────────┘
```

### Strategy Pattern

```
                  ┌─────────────────────┐
                  │  PaymentStrategy    │
                  │    (Interface)      │
                  └──────────┬──────────┘
                             │
         ┌───────────────────┼───────────────────┐
         │                   │                   │
         ↓                   ↓                   ↓
┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐
│ CashPayment      │ │ CardPayment      │ │ MobilePayment    │
│ Strategy         │ │ Strategy         │ │ Strategy         │
│                  │ │                  │ │                  │
│ - Coins/Bills    │ │ - Credit/Debit   │ │ - NFC/QR         │
└──────────────────┘ └──────────────────┘ └──────────────────┘

                  ┌─────────────────────┐
                  │ SelectionStrategy   │
                  │    (Interface)      │
                  └──────────┬──────────┘
                             │
                  ┌──────────┴──────────┐
                  │                     │
                  ↓                     ↓
        ┌──────────────────┐  ┌─────────────────┐
        │ BasicSelection   │  │ NameBased       │
        │ Strategy         │  │ Selection       │
        │ (By ID: A1, B2)  │  │ Strategy        │
        └──────────────────┘  └─────────────────┘
```

### Observer Pattern

```
                  ┌─────────────────────────┐
                  │ VendingMachineObserver  │
                  │      (Interface)        │
                  └──────────┬──────────────┘
                             │
                  ┌──────────┴──────────┐
                  │                     │
                  ↓                     ↓
        ┌──────────────────┐  ┌──────────────────┐
        │ Console          │  │ Maintenance      │
        │ VendingObserver  │  │ Observer         │
        │                  │  │                  │
        │ - Real-time logs │  │ - Sales tracking │
        │                  │  │ - Alerts         │
        │                  │  │ - Statistics     │
        └──────────────────┘  └──────────────────┘
```

---

## 🔄 Data Flow

### Successful Purchase Flow

```
1. Customer Interaction
   ↓
   VendingMachineSystem.selectProduct("A1")
   ↓
   System → Context.selectProduct(product)
   ↓
   Context → CurrentState.selectProduct(product)
   ↓
   IdleState validates & transitions to SelectingState
   ↓

2. Payment
   ↓
   VendingMachineSystem.insertCoin(1.50)
   ↓
   Context → CurrentState.insertCoin(1.50)
   ↓
   SelectingState accumulates payment
   ↓
   If (totalPaid >= price) → transition to PaymentState
   ↓
   Auto-trigger dispenseProduct()
   ↓

3. Dispensing
   ↓
   PaymentState → transition to DispensingState
   ↓
   DispensingState.dispenseProduct()
   ↓
   - Decrement inventory
   ↓
   - Calculate change
   ↓
   - Create transaction record
   ↓
   - Notify observers
   ↓
   - Transition to IdleState
```

### Observer Notification Flow

```
Event Occurs (e.g., Product Dispensed)
   ↓
   Context.notifyProductDispensed(product, transaction)
   ↓
   Iterate through all registered observers
   ↓
   ┌─────────────────────┬─────────────────────┐
   ↓                     ↓                     ↓
ConsoleObserver    MaintenanceObserver    (Future Observers)
   │                     │
   │ Log to console      │ Update statistics
   │                     │ Check stock levels
   │                     │ Generate alerts
   ↓                     ↓
Output                Reports
```

---

## 🧵 Thread Safety Architecture

### Concurrent Data Structures

```
┌────────────────────────────────────────────────────────────────┐
│ VendingMachineSystem (Singleton)                               │
├────────────────────────────────────────────────────────────────┤
│ private static volatile VendingMachineSystem instance;         │
│                                                                │
│ Thread-safe initialization:                                    │
│   if (instance == null) {                                      │
│     synchronized (VendingMachineSystem.class) {                │
│       if (instance == null) {                                  │
│         instance = new VendingMachineSystem();                 │
│       }                                                         │
│     }                                                           │
│   }                                                             │
└────────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────────┐
│ Inventory                                                      │
├────────────────────────────────────────────────────────────────┤
│ ConcurrentHashMap<String, Product> products;                   │
│   → Thread-safe map for products                               │
│                                                                │
│ ConcurrentHashMap<String, AtomicInteger> stock;                │
│   → Thread-safe map with atomic counters                       │
│                                                                │
│ synchronized boolean decrementStock(String id) {               │
│   → Critical section for stock updates                         │
│ }                                                               │
└────────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────────┐
│ VendingMachineContext                                          │
├────────────────────────────────────────────────────────────────┤
│ CopyOnWriteArrayList<VendingMachineObserver> observers;        │
│   → Thread-safe list optimized for reads                       │
│                                                                │
│ AtomicLong transactionIdCounter;                               │
│   → Thread-safe counter for unique IDs                         │
└────────────────────────────────────────────────────────────────┘
```

---

## 📊 Class Relationships

### Dependency Graph

```
Main
 └─→ VendingMachineSystem (Singleton)
      ├─→ VendingMachineContext
      │    ├─→ State (interface)
      │    │    ├─→ IdleState
      │    │    ├─→ SelectingState
      │    │    ├─→ PaymentState
      │    │    └─→ DispensingState
      │    │
      │    ├─→ PaymentStrategy (interface)
      │    │    ├─→ CashPaymentStrategy
      │    │    ├─→ CardPaymentStrategy
      │    │    └─→ MobilePaymentStrategy
      │    │
      │    ├─→ VendingMachineObserver (interface)
      │    │    ├─→ ConsoleVendingObserver
      │    │    └─→ MaintenanceObserver
      │    │
      │    └─→ Inventory
      │         └─→ Product
      │
      ├─→ ProductSelectionStrategy (interface)
      │    ├─→ BasicProductSelectionStrategy
      │    └─→ NameBasedSelectionStrategy
      │
      └─→ Transaction
```

---

## 🎯 SOLID Principles Mapping

### Single Responsibility

```
┌─────────────────────┬──────────────────────────────────┐
│ Class               │ Single Responsibility            │
├─────────────────────┼──────────────────────────────────┤
│ IdleState           │ Handle idle state behavior       │
│ SelectingState      │ Handle selection state behavior  │
│ Product             │ Represent product data           │
│ Inventory           │ Manage stock levels              │
│ Transaction         │ Record transaction details       │
│ CashPaymentStrategy │ Process cash payments            │
│ MaintenanceObserver │ Track maintenance data           │
└─────────────────────┴──────────────────────────────────┘
```

### Open/Closed Principle

```
✅ OPEN for Extension:
   - Add new states: class NewState implements State
   - Add new payment methods: class NewPayment implements PaymentStrategy
   - Add new observers: class NewObserver implements VendingMachineObserver

❌ CLOSED for Modification:
   - Existing states don't need changes
   - Core system logic remains unchanged
   - Interface contracts are stable
```

### Liskov Substitution

```
State s = new IdleState();
s = new SelectingState();      // ✓ Can substitute

PaymentStrategy p = new CashPaymentStrategy();
p = new CardPaymentStrategy(); // ✓ Can substitute

All implementations honor interface contracts
```

### Interface Segregation

```
Small, focused interfaces:

State                    → Only state operations
PaymentStrategy          → Only payment operations
VendingMachineObserver   → Only notification operations
ProductSelectionStrategy → Only selection operations

No class is forced to implement methods it doesn't use
```

### Dependency Inversion

```
High-level modules depend on abstractions:

VendingMachineContext → depends on State (interface)
VendingMachineContext → depends on PaymentStrategy (interface)
VendingMachineContext → depends on VendingMachineObserver (interface)

Not on concrete implementations
```

---

## 🚀 Execution Flow Example

### Complete Purchase Flow

```
Step 1: Initialize System
─────────────────────────
VendingMachineSystem.getInstance()
  ↓
Create Inventory, Context
  ↓
Set initial state to IdleState
  ↓
Register observers

Step 2: Add Products
─────────────────────
addProduct("A1", "Coca Cola", 1.50, BEVERAGE, 140, 5)
  ↓
Inventory.addProduct(product, stock)
  ↓
ConcurrentHashMap.put(productId, product)
AtomicInteger(5) for stock

Step 3: Customer Selects Product
─────────────────────────────────
machine.selectProduct("A1")
  ↓
System → Context.selectProduct(product)
  ↓
Context → IdleState.selectProduct(product)
  ↓
IdleState validates:
  - Product exists? ✓
  - Stock available? ✓
  ↓
Set selectedProduct
  ↓
Transition: IdleState → SelectingState
  ↓
Console: "Product selected: Coca Cola - $1.50"
        "Please insert payment. Required: $1.50"

Step 4: Insert Payment
──────────────────────
machine.insertCoin(0.50)
  ↓
Context → SelectingState.insertCoin(0.50)
  ↓
Accumulate payment: totalPaid = 0.50
  ↓
Console: "Inserted: $0.50 | Total: $0.50"
        "Please insert $1.00 more"

machine.insertCoin(1.00)
  ↓
Accumulate: totalPaid = 1.50
  ↓
Check: totalPaid (1.50) >= price (1.50)? ✓
  ↓
Transition: SelectingState → PaymentState
  ↓
Auto-trigger: context.dispenseProduct()

Step 5: Dispense Product
─────────────────────────
PaymentState → transition to DispensingState
  ↓
DispensingState.dispenseProduct()
  ↓
Calculate change: 1.50 - 1.50 = 0
  ↓
Inventory.decrementStock("A1")
  ↓
AtomicInteger.decrementAndGet() → Stock: 5 → 4
  ↓
Create Transaction record
  ↓
Notify observers:
  - ConsoleObserver → Log to console
  - MaintenanceObserver → Update sales stats
  ↓
Console: "Product dispensed successfully!"
         "Thank you for your purchase!"
  ↓
Transition: DispensingState → IdleState
  ↓
Reset transaction data
  ↓
Ready for next customer
```

---

## 📈 Performance Characteristics

```
┌────────────────────────┬──────────────┬─────────────────┐
│ Operation              │ Time         │ Thread-Safe     │
├────────────────────────┼──────────────┼─────────────────┤
│ selectProduct()        │ O(1)         │ ✓               │
│ insertCoin()           │ O(1)         │ ✓               │
│ dispenseProduct()      │ O(n)         │ ✓               │
│ getInventory()         │ O(n)         │ ✓               │
│ State transition       │ O(1)         │ ✓               │
│ Observer notification  │ O(n)         │ ✓               │
└────────────────────────┴──────────────┴─────────────────┘

Where n = number of observers/products (typically small)
```

---

## 🔧 Extension Points

### How to Add New Features

```
1. New State
   ├─→ Implement State interface
   ├─→ Add transition logic in existing states
   └─→ No changes to core system

2. New Payment Method
   ├─→ Implement PaymentStrategy interface
   ├─→ Add to VendingMachineContext
   └─→ Call from SelectingState

3. New Observer
   ├─→ Implement VendingMachineObserver interface
   ├─→ Register with context
   └─→ Automatically receives events

4. New Selection Strategy
   ├─→ Implement ProductSelectionStrategy interface
   ├─→ Set via setSelectionStrategy()
   └─→ No changes to selection logic
```

---

**Architecture:** Clean, modular, extensible  
**Patterns:** State, Singleton, Strategy, Observer  
**Principles:** All 5 SOLID  
**Thread Safety:** Complete  
**Status:** Production-ready ✅

