# State Pattern - In-Depth Explanation

## What is the State Pattern?

The **State Pattern** is a behavioral design pattern that allows an object to change its behavior when its internal state changes. It appears as if the object changes its class.

### Real-World Analogy

Think of a traffic light:
- 🔴 **Red Light State:** Cars must stop, pedestrians can cross
- 🟡 **Yellow Light State:** Cars should prepare to stop
- 🟢 **Green Light State:** Cars can go, pedestrians must wait

The traffic light's behavior changes based on its current state. The same action (approaching the intersection) results in different behaviors depending on the state.

---

## Why Use State Pattern for Vending Machine?

A vending machine is a **textbook example** of the State Pattern because:

1. **Multiple Distinct States:**
   - Idle (waiting)
   - Selecting (product chosen)
   - Payment (money received)
   - Dispensing (giving product)

2. **State-Dependent Behavior:**
   - Same operation (e.g., "insert coin") behaves differently in each state
   - In Idle: "Select product first"
   - In Selecting: "Accept coin and accumulate"
   - In Payment: "Already paid"
   - In Dispensing: "Please wait"

3. **Clear State Transitions:**
   - Idle → Selecting (when product selected)
   - Selecting → Payment (when payment complete)
   - Payment → Dispensing (when ready to dispense)
   - Dispensing → Idle (when done)

---

## Problem: Without State Pattern

### The Anti-Pattern Approach

```java
public class VendingMachine {
    private String currentState = "IDLE";
    private Product selectedProduct;
    private double totalPaid;
    
    public void selectProduct(Product product) {
        if (currentState.equals("IDLE")) {
            selectedProduct = product;
            currentState = "SELECTING";
        } else if (currentState.equals("SELECTING")) {
            System.out.println("Product already selected");
        } else if (currentState.equals("PAYMENT")) {
            System.out.println("Please wait, processing payment");
        } else if (currentState.equals("DISPENSING")) {
            System.out.println("Please wait, dispensing");
        }
    }
    
    public void insertCoin(double amount) {
        if (currentState.equals("IDLE")) {
            System.out.println("Select product first");
        } else if (currentState.equals("SELECTING")) {
            totalPaid += amount;
            if (totalPaid >= selectedProduct.getPrice()) {
                currentState = "PAYMENT";
                dispenseProduct();
            }
        } else if (currentState.equals("PAYMENT")) {
            System.out.println("Payment already received");
        } else if (currentState.equals("DISPENSING")) {
            System.out.println("Please wait");
        }
    }
    
    public void dispenseProduct() {
        if (currentState.equals("IDLE")) {
            System.out.println("Select product first");
        } else if (currentState.equals("SELECTING")) {
            System.out.println("Please pay first");
        } else if (currentState.equals("PAYMENT")) {
            currentState = "DISPENSING";
            // dispense logic
            currentState = "IDLE";
        } else if (currentState.equals("DISPENSING")) {
            System.out.println("Already dispensing");
        }
    }
    
    public void cancel() {
        if (currentState.equals("IDLE")) {
            System.out.println("Nothing to cancel");
        } else if (currentState.equals("SELECTING")) {
            // refund logic
            currentState = "IDLE";
        } else if (currentState.equals("PAYMENT")) {
            System.out.println("Cannot cancel");
        } else if (currentState.equals("DISPENSING")) {
            System.out.println("Cannot cancel");
        }
    }
}
```

### Problems with This Approach

1. ❌ **Repeated Conditionals:** Every method has the same if-else chain
2. ❌ **Scattered Logic:** State-specific behavior is spread across multiple methods
3. ❌ **Hard to Maintain:** Adding a new state requires modifying every method
4. ❌ **Error-Prone:** Easy to miss a state in one of the methods
5. ❌ **Not Extensible:** Can't add states without modifying existing code (violates OCP)
6. ❌ **Code Duplication:** Same state checks repeated everywhere
7. ❌ **Complex Testing:** Must test every combination of state and method

---

## Solution: With State Pattern

### Step 1: Define State Interface

```java
public interface State {
    void selectProduct(Product product);
    void insertCoin(double amount);
    void insertCard(String cardNumber, double amount);
    void dispenseProduct();
    void cancel();
    String getStateName();
}
```

### Step 2: Create Context Class

```java
public class VendingMachineContext {
    private State currentState;  // Reference to current state
    private Product selectedProduct;
    private double totalPaid;
    
    public VendingMachineContext(State initialState) {
        this.currentState = initialState;
    }
    
    // Change state
    public void setState(State newState) {
        System.out.println("Transitioning: " + 
            currentState.getStateName() + " -> " + 
            newState.getStateName());
        this.currentState = newState;
    }
    
    // Delegate to current state
    public void selectProduct(Product product) {
        currentState.selectProduct(product);  // Let state handle it
    }
    
    public void insertCoin(double amount) {
        currentState.insertCoin(amount);  // Let state handle it
    }
    
    public void dispenseProduct() {
        currentState.dispenseProduct();  // Let state handle it
    }
    
    public void cancel() {
        currentState.cancel();  // Let state handle it
    }
    
    // Getters and setters for state to use
    public Product getSelectedProduct() { return selectedProduct; }
    public void setSelectedProduct(Product p) { selectedProduct = p; }
    public double getTotalPaid() { return totalPaid; }
    public void addPayment(double amount) { totalPaid += amount; }
    public void resetTransaction() { 
        selectedProduct = null;
        totalPaid = 0;
    }
}
```

### Step 3: Implement Concrete States

#### IdleState

```java
public class IdleState implements State {
    private final VendingMachineContext context;
    
    public IdleState(VendingMachineContext context) {
        this.context = context;
    }
    
    @Override
    public void selectProduct(Product product) {
        // ✓ VALID in this state
        System.out.println("Product selected: " + product.getName());
        context.setSelectedProduct(product);
        context.setState(new SelectingState(context));  // Transition
    }
    
    @Override
    public void insertCoin(double amount) {
        // ✗ INVALID in this state
        System.out.println("Please select a product first");
    }
    
    @Override
    public void dispenseProduct() {
        // ✗ INVALID in this state
        System.out.println("Please select product and pay first");
    }
    
    @Override
    public void cancel() {
        // ✗ INVALID in this state
        System.out.println("Nothing to cancel");
    }
    
    @Override
    public String getStateName() {
        return "IDLE";
    }
}
```

#### SelectingState

```java
public class SelectingState implements State {
    private final VendingMachineContext context;
    
    public SelectingState(VendingMachineContext context) {
        this.context = context;
    }
    
    @Override
    public void selectProduct(Product product) {
        // ✗ INVALID in this state
        System.out.println("Product already selected");
    }
    
    @Override
    public void insertCoin(double amount) {
        // ✓ VALID in this state
        context.addPayment(amount);
        System.out.println("Inserted: $" + amount);
        
        double totalPaid = context.getTotalPaid();
        double price = context.getSelectedProduct().getPrice();
        
        if (totalPaid >= price) {
            // Transition to PaymentState
            context.setState(new PaymentState(context));
            // Automatically dispense
            context.dispenseProduct();
        } else {
            System.out.println("Please insert $" + (price - totalPaid) + " more");
        }
    }
    
    @Override
    public void dispenseProduct() {
        // ✗ INVALID in this state
        System.out.println("Please complete payment first");
    }
    
    @Override
    public void cancel() {
        // ✓ VALID in this state
        double refund = context.getTotalPaid();
        System.out.println("Transaction cancelled. Refunding: $" + refund);
        context.resetTransaction();
        context.setState(new IdleState(context));  // Back to idle
    }
    
    @Override
    public String getStateName() {
        return "SELECTING";
    }
}
```

#### PaymentState

```java
public class PaymentState implements State {
    private final VendingMachineContext context;
    
    public PaymentState(VendingMachineContext context) {
        this.context = context;
    }
    
    @Override
    public void selectProduct(Product product) {
        // ✗ INVALID in this state
        System.out.println("Transaction in progress");
    }
    
    @Override
    public void insertCoin(double amount) {
        // ✗ INVALID in this state
        System.out.println("Payment already received");
    }
    
    @Override
    public void dispenseProduct() {
        // ✓ VALID in this state - transition to dispensing
        context.setState(new DispensingState(context));
        context.dispenseProduct();  // Call on new state
    }
    
    @Override
    public void cancel() {
        // ✗ INVALID in this state
        System.out.println("Cannot cancel - transaction processing");
    }
    
    @Override
    public String getStateName() {
        return "PAYMENT";
    }
}
```

#### DispensingState

```java
public class DispensingState implements State {
    private final VendingMachineContext context;
    
    public DispensingState(VendingMachineContext context) {
        this.context = context;
    }
    
    @Override
    public void selectProduct(Product product) {
        // ✗ INVALID in this state
        System.out.println("Please wait - dispensing in progress");
    }
    
    @Override
    public void insertCoin(double amount) {
        // ✗ INVALID in this state
        System.out.println("Please wait - dispensing in progress");
    }
    
    @Override
    public void dispenseProduct() {
        // ✓ VALID in this state - actually dispense
        Product product = context.getSelectedProduct();
        double change = context.getTotalPaid() - product.getPrice();
        
        System.out.println("Dispensing: " + product.getName());
        if (change > 0) {
            System.out.println("Change: $" + change);
        }
        System.out.println("Thank you!");
        
        // Clean up and return to idle
        context.resetTransaction();
        context.setState(new IdleState(context));
    }
    
    @Override
    public void cancel() {
        // ✗ INVALID in this state
        System.out.println("Cannot cancel - dispensing in progress");
    }
    
    @Override
    public String getStateName() {
        return "DISPENSING";
    }
}
```

---

## Benefits of State Pattern

### 1. Clean Code

**Before:**
```java
public void insertCoin(double amount) {
    if (state == IDLE) {
        // ...
    } else if (state == SELECTING) {
        // ...
    } else if (state == PAYMENT) {
        // ...
    } else if (state == DISPENSING) {
        // ...
    }
}
```

**After:**
```java
public void insertCoin(double amount) {
    currentState.insertCoin(amount);  // That's it!
}
```

### 2. Easy to Extend

Adding a new state (e.g., `MaintenanceState`):

```java
public class MaintenanceState implements State {
    // Implement all methods to reject operations
    // or handle them specially for maintenance mode
}

// Use it
context.setState(new MaintenanceState(context));
```

**No modification of existing code needed!** ✅ Open/Closed Principle

### 3. Encapsulation

Each state encapsulates its own behavior. Changes to one state don't affect others.

### 4. Single Responsibility

Each state class has one responsibility: handle operations for that specific state.

### 5. Type Safety

Compiler ensures all states implement all operations. Can't forget to handle a case.

---

## State Transitions Visualized

```
Customer Journey:
┌─────────────────────────────────────────────────────────┐
│  1. Customer walks up                                   │
│     State: IDLE                                         │
└─────────────────────────────────────────────────────────┘
                        │
                        │ selectProduct("Coke")
                        ↓
┌─────────────────────────────────────────────────────────┐
│  2. Customer selects Coca Cola ($1.50)                  │
│     State: SELECTING                                    │
│     Prompt: "Insert $1.50"                              │
└─────────────────────────────────────────────────────────┘
                        │
                        │ insertCoin(0.50)
                        ↓
┌─────────────────────────────────────────────────────────┐
│  3. Customer inserts $0.50                              │
│     State: SELECTING                                    │
│     Prompt: "Insert $1.00 more"                         │
└─────────────────────────────────────────────────────────┘
                        │
                        │ insertCoin(1.00)
                        ↓
┌─────────────────────────────────────────────────────────┐
│  4. Customer inserts $1.00 (total: $1.50)               │
│     State: PAYMENT → DISPENSING (automatic transition)  │
└─────────────────────────────────────────────────────────┘
                        │
                        │ dispenseProduct() [automatic]
                        ↓
┌─────────────────────────────────────────────────────────┐
│  5. Machine dispenses Coca Cola                         │
│     State: DISPENSING → IDLE                            │
│     Output: "Thank you!"                                │
└─────────────────────────────────────────────────────────┘
```

---

## Key Concepts

### 1. Context Class
- Maintains reference to current state
- Delegates operations to current state
- Provides data/methods for states to use

### 2. State Interface
- Defines operations that all states must handle
- Each operation corresponds to a possible action

### 3. Concrete States
- Implement State interface
- Handle operations appropriate for that state
- Trigger state transitions by calling `context.setState()`

### 4. State Transitions
- States control their own transitions
- Transitions happen by calling `context.setState(newState)`
- Context doesn't decide transitions - states do!

---

## Common Pitfalls to Avoid

### ❌ Don't: Use String-Based States

```java
// BAD
if (currentState.equals("IDLE")) { ... }
```

**Problems:**
- Typo-prone
- No compile-time checking
- Hard to refactor

### ✅ Do: Use State Objects

```java
// GOOD
currentState.selectProduct(product);
```

### ❌ Don't: Put Business Logic in Context

```java
// BAD - Context shouldn't have business logic
public void selectProduct(Product product) {
    if (inventory.isAvailable(product)) {
        selectedProduct = product;
        currentState = new SelectingState();
    }
}
```

### ✅ Do: Delegate to States

```java
// GOOD - State handles logic
public void selectProduct(Product product) {
    currentState.selectProduct(product);
}
```

### ❌ Don't: Share State Instances

```java
// BAD - States should not be singletons
private static final IdleState IDLE_STATE = new IdleState();
context.setState(IDLE_STATE);  // All machines share same state object
```

### ✅ Do: Create New State Instances

```java
// GOOD - Each transition creates new state
context.setState(new IdleState(context));
```

---

## When to Use State Pattern

### ✅ Use State Pattern When:

1. Object behavior changes based on state
2. Multiple states with different behaviors
3. Complex conditional logic based on state
4. State transitions are well-defined
5. Many operations that behave differently per state

### ❌ Don't Use State Pattern When:

1. Only 2-3 simple states
2. State logic is trivial
3. States don't have different behaviors
4. Simple boolean flags suffice

---

## Comparison with Strategy Pattern

| Aspect | State Pattern | Strategy Pattern |
|--------|---------------|------------------|
| **Purpose** | Change behavior based on internal state | Select algorithm at runtime |
| **Who decides change** | Object itself (automatic) | Client (manual) |
| **Awareness** | States know about each other | Strategies independent |
| **Transitions** | States trigger transitions | No transitions |
| **Example** | Vending machine states | Payment methods |

---

## Conclusion

The State Pattern is **perfect for vending machines** because:

1. ✅ **Clear States:** Idle, Selecting, Payment, Dispensing
2. ✅ **State-Dependent Behavior:** Same action, different responses
3. ✅ **Clean Code:** No complex conditionals
4. ✅ **Maintainable:** Easy to understand and modify
5. ✅ **Extensible:** Add new states without modifying existing code
6. ✅ **Testable:** Each state tested independently
7. ✅ **Type-Safe:** Compiler ensures completeness

**Result:** Clean, maintainable, professional code that follows SOLID principles and design pattern best practices.

