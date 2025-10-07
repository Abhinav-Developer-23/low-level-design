# Elevator Control System - Design Document

## Table of Contents
1. [System Overview](#system-overview)
2. [Requirements Analysis](#requirements-analysis)
3. [Design Principles](#design-principles)
4. [Architecture](#architecture)
5. [Class Diagrams](#class-diagrams)
6. [Sequence Diagrams](#sequence-diagrams)
7. [Design Patterns](#design-patterns)
8. [Data Structures](#data-structures)
9. [Algorithms](#algorithms)
10. [Thread Safety](#thread-safety)
11. [Scalability](#scalability)

---

## System Overview

The Elevator Control System manages multiple elevator shafts in a building, handling:
- External requests from floors (UP/DOWN buttons)
- Internal requests from inside elevators (floor selection)
- Intelligent scheduling to minimize wait times
- Fault handling for out-of-service elevators

### Key Goals
- Minimize passenger wait time
- Optimize elevator utilization
- Handle concurrent requests safely
- Support fault tolerance
- Enable extensibility

---

## Requirements Analysis

### Functional Requirements

| ID | Requirement | Implementation |
|----|-------------|----------------|
| FR-1 | Accept external UP/DOWN requests from floors | `submitExternalRequest(floor, direction)` |
| FR-2 | Accept internal destination requests | `submitInternalRequest(elevatorId, floor)` |
| FR-3 | Assign requests to elevators optimally | `SchedulingStrategy` interface |
| FR-4 | Move elevators floor-by-floor | `Elevator.step()` method |
| FR-5 | Open/close doors at stops | `openDoors()`, `closeDoors()` |
| FR-6 | Query elevator status | `getElevatorStatus()`, `printAllElevatorStatus()` |
| FR-7 | Handle out-of-service elevators | `markElevatorOutOfService()` |
| FR-8 | Support multiple scheduling policies | Strategy Pattern |
| FR-9 | Handle concurrent requests | Thread-safe implementation |

### Non-Functional Requirements

| ID | Requirement | Solution |
|----|-------------|----------|
| NFR-1 | Thread-safe operations | ReentrantLock, volatile fields, ConcurrentHashMap |
| NFR-2 | Extensible scheduling | Strategy Pattern with interface |
| NFR-3 | Scalable to N elevators, M floors | Parameterized initialization |
| NFR-4 | Low latency request handling | Efficient data structures (HashSet) |
| NFR-5 | Maintainable codebase | SOLID principles, clear separation |
| NFR-6 | Real-time operation | ScheduledExecutorService |

---

## Design Principles

### SOLID Principles Applied

#### 1. Single Responsibility Principle (SRP)
Each class has one clear reason to change:

```
Elevator              → Manages single elevator state & movement
ElevatorController    → Coordinates all elevators & requests
SchedulingStrategy    → Determines optimal elevator assignment
Request               → Encapsulates request data
ExternalRequest       → Handles floor-level requests
InternalRequest       → Handles in-elevator requests
```

#### 2. Open/Closed Principle (OCP)
Open for extension, closed for modification:

```java
// Adding new scheduling strategy doesn't require modifying existing code
class CustomStrategy implements SchedulingStrategy {
    public Elevator selectElevator(ExternalRequest request, List<Elevator> elevators) {
        // Custom logic
    }
}

// Switch at runtime
controller.setSchedulingStrategy(new CustomStrategy());
```

#### 3. Liskov Substitution Principle (LSP)
All scheduling strategies are interchangeable:

```java
SchedulingStrategy strategy1 = new NearestCarStrategy();
SchedulingStrategy strategy2 = new RoundRobinStrategy();
SchedulingStrategy strategy3 = new ZoneBasedStrategy();

// All work identically from controller's perspective
controller.setSchedulingStrategy(strategy1);
controller.setSchedulingStrategy(strategy2);
```

#### 4. Interface Segregation Principle (ISP)
Focused interfaces:

```java
// Single focused interface
interface SchedulingStrategy {
    Elevator selectElevator(ExternalRequest request, List<Elevator> elevators);
}

// Not: interface MassiveElevatorInterface with 20 methods
```

#### 5. Dependency Inversion Principle (DIP)
Depend on abstractions:

```java
// Controller depends on interface, not concrete implementation
private SchedulingStrategy schedulingStrategy;

// Not: private NearestCarStrategy schedulingStrategy;
```

### OOP Principles Applied

#### Abstraction
```java
abstract class Request {
    // Common request behavior
    public abstract int getTargetFloor();
}
```

#### Encapsulation
```java
class Elevator {
    // Private state
    private volatile int currentFloor;
    private volatile Direction currentDirection;
    
    // Public interface
    public int getCurrentFloor() { return currentFloor; }
    public Direction getCurrentDirection() { return currentDirection; }
}
```

#### Inheritance
```java
class ExternalRequest extends Request { }
class InternalRequest extends Request { }
```

#### Polymorphism
```java
Request request1 = new ExternalRequest(5, Direction.UP);
Request request2 = new InternalRequest(8, "E1");

// Both treated polymorphically
int floor1 = request1.getTargetFloor();
int floor2 = request2.getTargetFloor();
```

---

## Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    External Users                        │
│              (People at floors/in elevators)             │
└────────────────┬────────────────────────┬────────────────┘
                 │                        │
                 ▼                        ▼
        ┌────────────────┐      ┌─────────────────┐
        │ External       │      │ Internal        │
        │ Request        │      │ Request         │
        └────────┬───────┘      └────────┬────────┘
                 │                       │
                 └──────────┬────────────┘
                            ▼
                 ┌────────────────────┐
                 │ ElevatorController │
                 │   (Singleton)      │
                 └──────────┬─────────┘
                            │
                ┌───────────┼───────────┐
                ▼           ▼           ▼
         ┌──────────┐ ┌──────────┐ ┌──────────┐
         │ Elevator │ │ Elevator │ │ Elevator │
         │    E1    │ │    E2    │ │    E3    │
         └──────────┘ └──────────┘ └──────────┘
                            │
                            ▼
                 ┌────────────────────┐
                 │ SchedulingStrategy │
                 │    (Interface)     │
                 └────────┬───────────┘
                          │
        ┌─────────────────┼─────────────────┐
        ▼                 ▼                 ▼
┌───────────────┐ ┌──────────────┐ ┌─────────────┐
│ NearestCar    │ │ RoundRobin   │ │ ZoneBased   │
│ Strategy      │ │ Strategy     │ │ Strategy    │
└───────────────┘ └──────────────┘ └─────────────┘
```

### Component Interaction

```
User Action                   Controller                 Elevator
     |                            |                         |
     |--External Request(5, UP)-->|                         |
     |                            |--Select Elevator(E1)--->|
     |                            |<--Acknowledgment--------|
     |                            |--Add Request----------->|
     |                            |                         |--Move Up-->
     |                            |                         |--Move Up-->
     |                            |                         |--At Floor 5->
     |                            |                         |--Open Doors->
     |<---------Elevator Arrives--|<--Door Open Event------|
     |--Enters Elevator---------->|                         |
     |--Internal Request(8)------>|                         |
     |                            |--Add Request----------->|
     |                            |                         |--Close Doors->
     |                            |                         |--Move Up-->
     |                            |                         |--At Floor 8->
     |                            |                         |--Open Doors->
     |<---------Arrives-----------|<--Door Open Event------|
```

---

## Class Diagrams

### Core Classes

```
┌─────────────────────────┐
│      «enumeration»      │
│       Direction         │
├─────────────────────────┤
│ + UP                    │
│ + DOWN                  │
│ + IDLE                  │
└─────────────────────────┘

┌─────────────────────────┐
│      «enumeration»      │
│     ElevatorState       │
├─────────────────────────┤
│ + IDLE                  │
│ + MOVING                │
│ + STOPPED               │
│ + DOOR_OPEN             │
│ + OUT_OF_SERVICE        │
└─────────────────────────┘

┌──────────────────────────────────┐
│        «abstract»                │
│          Request                 │
├──────────────────────────────────┤
│ - requestId: String              │
│ - timestamp: long                │
│ - status: RequestStatus          │
├──────────────────────────────────┤
│ + getRequestId(): String         │
│ + getTimestamp(): long           │
│ + getStatus(): RequestStatus     │
│ + setStatus(status): void        │
│ + getTargetFloor(): int {abstract}│
└──────────────────────────────────┘
           △
           │
    ┌──────┴──────┐
    │             │
┌───────────┐ ┌─────────────┐
│ External  │ │  Internal   │
│ Request   │ │  Request    │
├───────────┤ ├─────────────┤
│- floor    │ │- destination│
│- direction│ │- elevatorId │
└───────────┘ └─────────────┘

┌──────────────────────────────────────┐
│           Elevator                   │
├──────────────────────────────────────┤
│ - elevatorId: String                 │
│ - currentFloor: int (volatile)       │
│ - currentDirection: Direction        │
│ - state: ElevatorState               │
│ - doorsOpen: boolean                 │
│ - upRequests: Set<Integer>           │
│ - downRequests: Set<Integer>         │
│ - lock: ReentrantLock                │
├──────────────────────────────────────┤
│ + getCurrentFloor(): int             │
│ + getCurrentDirection(): Direction   │
│ + getState(): ElevatorState          │
│ + isDoorsOpen(): boolean             │
│ + isAvailable(): boolean             │
│ + addRequest(floor, direction): void │
│ + step(): void                       │
│ + markOutOfService(): void           │
│ + restoreToService(): void           │
│ + getStatus(): String                │
└──────────────────────────────────────┘

┌──────────────────────────────────────────┐
│       «interface»                        │
│     SchedulingStrategy                   │
├──────────────────────────────────────────┤
│ + selectElevator(request, elevators):    │
│   Elevator                               │
└──────────────────────────────────────────┘
                △
                │
    ┌───────────┼───────────┐
    │           │           │
┌───────┐ ┌─────────┐ ┌─────────┐
│Nearest│ │  Round  │ │  Zone   │
│  Car  │ │  Robin  │ │  Based  │
└───────┘ └─────────┘ └─────────┘

┌──────────────────────────────────────────┐
│      ElevatorController                  │
│         «singleton»                      │
├──────────────────────────────────────────┤
│ - instance: ElevatorController (static)  │
│ - elevators: List<Elevator>              │
│ - pendingExternalRequests: Queue         │
│ - schedulingStrategy: SchedulingStrategy │
│ - lock: ReentrantLock                    │
│ - executorService: ScheduledExecutor     │
├──────────────────────────────────────────┤
│ + getInstance(...): ElevatorController   │
│ + submitExternalRequest(...): void       │
│ + submitInternalRequest(...): void       │
│ + start(): void                          │
│ + shutdown(): void                       │
│ + markElevatorOutOfService(...): void    │
│ + getElevatorStatus(...): String         │
│ + setSchedulingStrategy(...): void       │
└──────────────────────────────────────────┘
```

---

## Sequence Diagrams

### External Request Flow

```
User    Controller    Strategy    Elevator
 |          |            |           |
 |--submitExternalRequest(5, UP)---->|
 |          |            |           |
 |          |--selectElevator------->|
 |          |            |           |
 |          |<--return E1------------|
 |          |            |           |
 |          |--addRequest(5, UP)---------------->|
 |          |            |           |           |
 |          |            |           |--add to queue
 |          |            |           |           |
 |<---------acknowledged--|           |           |
 |          |            |           |           |
 |          |            |           |--step()-->|
 |          |            |           |--moveUp-->|
 |          |            |           |--moveUp-->|
 |          |            |           |--openDoors|
```

### Out-of-Service Handling

```
Controller    Elevator(E1)    Elevator(E2)
    |              |               |
    |--markOutOfService(E1)------->|
    |              |               |
    |<--return pending requests----|
    |              |               |
    |--mark state OUT_OF_SERVICE-->|
    |              |               |
    |--for each pending request----|
    |              |               |
    |--submitExternalRequest()-----|
    |              |               |
    |--selectElevator()----------->|
    |              |               |
    |<--return E2------------------|
    |              |               |
    |--addRequest(floor)------------------------->|
    |              |               |              |
```

---

## Design Patterns

### 1. Strategy Pattern

**Problem**: Need different elevator assignment algorithms  
**Solution**: Encapsulate algorithms in separate classes

```java
// Strategy interface
interface SchedulingStrategy {
    Elevator selectElevator(ExternalRequest request, List<Elevator> elevators);
}

// Concrete strategies
class NearestCarStrategy implements SchedulingStrategy { }
class RoundRobinStrategy implements SchedulingStrategy { }
class ZoneBasedStrategy implements SchedulingStrategy { }

// Context
class ElevatorController {
    private SchedulingStrategy schedulingStrategy;
    
    public void setSchedulingStrategy(SchedulingStrategy strategy) {
        this.schedulingStrategy = strategy;
    }
}
```

**Benefits**:
- Easy to add new strategies
- Can switch algorithms at runtime
- Each strategy is independently testable

### 2. Singleton Pattern

**Problem**: Need single point of control for all elevators  
**Solution**: Singleton controller with thread-safe initialization

```java
class ElevatorController {
    private static volatile ElevatorController instance;
    
    public static ElevatorController getInstance(...) {
        if (instance == null) {
            synchronized (ElevatorController.class) {
                if (instance == null) {
                    instance = new ElevatorController(...);
                }
            }
        }
        return instance;
    }
}
```

**Benefits**:
- Centralized control
- Prevents multiple controller instances
- Lazy initialization with thread safety

### 3. State Pattern (Implicit)

**Problem**: Elevator behavior changes based on state  
**Solution**: State enum with state-dependent logic

```java
enum ElevatorState {
    IDLE, MOVING, STOPPED, DOOR_OPEN, OUT_OF_SERVICE
}

// State-dependent behavior
public void step() {
    if (state == ElevatorState.OUT_OF_SERVICE) return;
    if (doorsOpen) { closeDoors(); return; }
    // ... more state-dependent logic
}
```

---

## Data Structures

### Request Queues

```java
// Thread-safe queue for external requests
Queue<ExternalRequest> pendingExternalRequests = new ConcurrentLinkedQueue<>();

// Thread-safe map for internal requests
Map<String, InternalRequest> pendingInternalRequests = new ConcurrentHashMap<>();
```

**Why ConcurrentLinkedQueue?**
- Lock-free operations
- Good for producer-consumer pattern
- FIFO ordering

### Elevator Request Sets

```java
// Sets for O(1) lookup and automatic deduplication
Set<Integer> upRequests = ConcurrentHashMap.newKeySet();
Set<Integer> downRequests = ConcurrentHashMap.newKeySet();
```

**Why Set?**
- Automatic deduplication (multiple requests for same floor)
- O(1) contains/remove operations
- Thread-safe with ConcurrentHashMap backing

---

## Algorithms

### 1. Nearest Car Selection

```
Algorithm: selectElevator(request, elevators)
Input: request (floor, direction), list of elevators
Output: best elevator

1. Filter elevators that are available (not OUT_OF_SERVICE)
2. For each elevator:
   a. If IDLE: distance = |elevator.floor - request.floor|
   b. If moving in same direction and can pick up on way:
      distance = actual distance
   c. Otherwise: distance = actual distance + penalty (100)
3. Return elevator with minimum distance
```

**Time Complexity**: O(n) where n = number of elevators  
**Space Complexity**: O(1)

### 2. Elevator Movement (SCAN algorithm variant)

```
Algorithm: step()
1. If doors open:
   - Close doors
   - Return
2. Check if should stop at current floor:
   - If direction == UP and floor in upRequests: stop
   - If direction == DOWN and floor in downRequests: stop
3. If stopped:
   - Open doors
   - Remove floor from requests
   - Return
4. If moving UP:
   - If has more upper requests and not at top: move up
   - Else: switch direction or go IDLE
5. If moving DOWN:
   - If has more lower requests and not at bottom: move down
   - Else: switch direction or go IDLE
6. If IDLE:
   - Check if any requests exist
   - Set direction and start moving
```

**Time Complexity**: O(1) per step  
**Characteristics**: Similar to SCAN disk scheduling

### 3. Request Reassignment

```
Algorithm: markElevatorOutOfService(elevatorId)
1. Find elevator by ID
2. Lock controller
3. Get all pending requests from elevator
4. Clear elevator's request queues
5. Mark elevator as OUT_OF_SERVICE
6. For each pending request:
   - Determine direction
   - Create new external request
   - Add to pending queue
7. Unlock controller
```

**Time Complexity**: O(k) where k = number of pending requests

---

## Thread Safety

### Synchronization Mechanisms

#### 1. ReentrantLock
```java
private final ReentrantLock lock = new ReentrantLock();

public void criticalOperation() {
    lock.lock();
    try {
        // Critical section
    } finally {
        lock.unlock();
    }
}
```

**Usage**: Protects complex operations in Elevator and Controller

#### 2. Volatile Fields
```java
private volatile int currentFloor;
private volatile Direction currentDirection;
private volatile ElevatorState state;
```

**Usage**: Ensures visibility of state changes across threads

#### 3. Concurrent Collections
```java
ConcurrentLinkedQueue<ExternalRequest>
ConcurrentHashMap<String, InternalRequest>
ConcurrentHashMap.newKeySet()
```

**Usage**: Thread-safe collections without explicit locking

### Potential Race Conditions Prevented

| Scenario | Prevention |
|----------|------------|
| Multiple requests for same elevator | ReentrantLock in addRequest() |
| Concurrent status reads during state change | Volatile fields |
| Simultaneous out-of-service marking | Lock in controller |
| Multiple strategy switches | Synchronized setter |

---

## Scalability

### Vertical Scalability (More Floors)

```java
// System supports arbitrary floor ranges
ElevatorController controller = ElevatorController.getInstance(
    numElevators: 5,
    minFloor: -2,    // Underground parking
    maxFloor: 100    // 100 floors
);
```

**Impact**: O(1) - no performance impact

### Horizontal Scalability (More Elevators)

```java
// System supports arbitrary number of elevators
ElevatorController controller = ElevatorController.getInstance(
    numElevators: 20,  // 20 elevators
    minFloor: 0,
    maxFloor: 50
);
```

**Impact**: 
- Request processing: O(n) per request
- Space: O(n) for elevator storage
- Can optimize with zone-based strategy

### Handling High Load

1. **Request Batching**: Process requests in batches every 500ms
2. **Efficient Data Structures**: O(1) operations for most actions
3. **Lock-Free Operations**: Use concurrent collections where possible
4. **Asynchronous Processing**: ScheduledExecutorService for parallel operations

### Performance Characteristics

| Operation | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| Submit request | O(1) | O(1) |
| Assign request | O(n) | O(1) |
| Elevator step | O(1) | O(1) |
| Status query | O(1) | O(1) |
| Mark out-of-service | O(k) | O(k) |

Where:
- n = number of elevators
- k = number of pending requests

---

## Future Enhancements

### 1. Priority Requests
```java
enum RequestPriority {
    NORMAL, HIGH, EMERGENCY
}

class PriorityRequest extends Request {
    private RequestPriority priority;
}
```

### 2. Load Balancing
```java
class LoadAwareStrategy implements SchedulingStrategy {
    // Consider passenger count in elevator
    // Avoid overloading single elevator
}
```

### 3. Predictive Algorithm
```java
class MLBasedStrategy implements SchedulingStrategy {
    // Use machine learning to predict patterns
    // Pre-position elevators during rush hours
}
```

### 4. Energy Optimization
```java
class EnergyEfficientStrategy implements SchedulingStrategy {
    // Park idle elevators at optimal floors
    // Minimize total travel distance
}
```

---

## Conclusion

This design demonstrates:
- ✅ Clean architecture with clear separation of concerns
- ✅ SOLID principles throughout
- ✅ Multiple design patterns applied appropriately
- ✅ Thread-safe concurrent operations
- ✅ Scalable and extensible design
- ✅ Comprehensive documentation

The system is production-ready for simulation purposes and provides a solid foundation for a real elevator control system with appropriate hardware integration.

