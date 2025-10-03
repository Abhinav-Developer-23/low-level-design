# Vending Machine Implementation

## Overview
This is a clean implementation of a Vending Machine using the **State Design Pattern**. The machine handles coin insertion, item selection, dispensing, refunds, and service mode.

## Folder Structure

```
Vending-Machine/
├── src/main/java/org/example/
│   ├── enums/
│   │   └── Coin.java              # Enum for coin denominations
│   ├── interfaces/
│   │   └── State.java             # State interface for state pattern
│   ├── model/
│   │   ├── Item.java              # Product/item entity
│   │   └── Inventory.java         # Manages stock counts
│   ├── states/
│   │   ├── NoCoinState.java       # Initial state (no money inserted)
│   │   ├── HasCoinState.java      # State with money but no selection
│   │   ├── DispensingState.java   # Processing and dispensing state
│   │   └── OutOfServiceState.java # Maintenance/service state
│   ├── system/
│   │   └── VendingMachine.java    # Main context class
│   └── Main.java                  # Demo application
└── target/classes/                # Compiled classes
```

## Design Patterns Used

### 1. **State Pattern** (Primary)
- **Interface**: `State`
- **Concrete States**: `NoCoinState`, `HasCoinState`, `DispensingState`, `OutOfServiceState`
- **Context**: `VendingMachine`

The State pattern allows the vending machine to alter its behavior based on its internal state:
- Different actions are available in different states
- State transitions are encapsulated within states themselves
- Each state implements the `State` interface

### 2. **Enum Pattern**
- `Coin` enum encapsulates coin denominations with their values

## Key Features

1. **State Management**
   - Transitions between NoCoin → HasCoin → Dispensing → NoCoin
   - Special OutOfService state for maintenance
   - Proper refund handling in each state

2. **Financial Operations**
   - Uses cents (integers) to avoid floating-point precision issues
   - Greedy algorithm for change-making
   - Balance tracking across states

3. **Inventory Management**
   - Stock tracking per item
   - Out-of-stock handling
   - Stock decrement on successful purchase

4. **Error Handling**
   - Insufficient balance detection
   - Invalid item selection
   - Out-of-stock scenarios
   - Service mode restrictions

## State Transitions

```
[NoCoinState]
    insertCoin() → [HasCoinState]
    service(true) → [OutOfServiceState]

[HasCoinState]
    insertCoin() → [HasCoinState] (accumulates balance)
    selectItem() → [DispensingState] (if sufficient balance)
    selectItem() → [HasCoinState] (if insufficient balance)
    refund() → [NoCoinState]
    service(true) → [OutOfServiceState]

[DispensingState]
    dispense() → [NoCoinState] (after dispensing item + change)

[OutOfServiceState]
    service(false) → [NoCoinState]
```

## How to Run

### Compile
```bash
javac -d target/classes -sourcepath src/main/java src/main/java/org/example/Main.java
```

### Run
```bash
java -cp target/classes org.example.Main
```

## Example Usage

```java
VendingMachine vm = new VendingMachine();
vm.addItem(new Item("A1", "Coke", 125), 5);  // $1.25, 5 in stock

vm.insertCoin(Coin.QUARTER);  // Insert 25 cents
vm.insertCoin(Coin.QUARTER);  // Insert 25 cents
vm.insertCoin(Coin.QUARTER);  // Insert 25 cents
vm.insertCoin(Coin.QUARTER);  // Insert 25 cents
vm.insertCoin(Coin.QUARTER);  // Insert 25 cents (total $1.25)
vm.selectItem("A1");           // Purchase Coke
```

## Design Benefits

1. **Extensibility**: New states can be added easily
2. **Maintainability**: State logic is isolated in separate classes
3. **Testability**: Each state can be tested independently
4. **Clarity**: State transitions are explicit and easy to follow
5. **Safety**: Price calculations use integers to avoid floating-point errors

## Possible Enhancements

1. Add more coin denominations or support for bills
2. Implement a coin hopper with limited change availability
3. Add payment methods (card, mobile payment)
4. Implement observer pattern for maintenance notifications
5. Add logging and transaction history
6. Support for promotions and discounts
7. Multiple product selection strategies
8. Real database integration for persistent inventory

