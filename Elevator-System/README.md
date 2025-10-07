# Elevator Control System - Low Level Design

## Overview

A comprehensive elevator control system designed for buildings with multiple elevator shafts. The system handles external pickup requests from floors and internal destination requests from passengers, implementing intelligent scheduling strategies to minimize wait times.

## Features

✅ **Multi-Elevator Support**: Manages N elevators serving M floors  
✅ **External Requests**: Floor-level UP/DOWN button presses  
✅ **Internal Requests**: Destination floor selection from inside elevators  
✅ **Intelligent Scheduling**: Multiple scheduling strategies (Nearest Car, Round Robin, Zone-Based)  
✅ **Thread-Safe**: Concurrent request handling with proper synchronization  
✅ **Fault Handling**: Out-of-service elevator support with request reassignment  
✅ **Real-Time Status**: Query elevator state (floor, direction, doors, status)  
✅ **Extensible Design**: Easy to add new scheduling strategies

## Architecture

### Design Patterns Used

1. **Strategy Pattern** - Pluggable scheduling algorithms
2. **State Pattern** - Elevator state management (IDLE, MOVING, DOOR_OPEN, etc.)
3. **Singleton Pattern** - Centralized elevator controller
4. **Factory Pattern** - Request object creation
5. **Observer Pattern** - Request notification system

### SOLID Principles

- **Single Responsibility**: Each class has one clear purpose
  - `Elevator` - Manages individual elevator state and movement
  - `ElevatorController` - Coordinates all elevators and requests
  - `SchedulingStrategy` - Determines elevator assignment
  - `Request` - Encapsulates request data

- **Open/Closed**: Extensible through interfaces
  - New scheduling strategies can be added without modifying existing code
  - New elevator states can be added through the enum

- **Liskov Substitution**: All strategy implementations are interchangeable
  - Any `SchedulingStrategy` implementation can be swapped at runtime

- **Interface Segregation**: Focused interfaces for different concerns
  - `SchedulingStrategy` interface is simple and focused

- **Dependency Inversion**: Depends on abstractions
  - Controller depends on `SchedulingStrategy` interface, not concrete implementations

## Class Structure

### Core Components

#### 1. Enums
```java
Direction        // UP, DOWN, IDLE
ElevatorState    // IDLE, MOVING, STOPPED, DOOR_OPEN, OUT_OF_SERVICE
RequestStatus    // PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
```

#### 2. Request Models
```java
Request                  // Abstract base class
  ├── ExternalRequest    // Floor pickup requests (UP/DOWN)
  └── InternalRequest    // Destination requests from inside elevator
```

#### 3. Elevator
```java
Elevator                 // Represents a single elevator
  - State management (floor, direction, door status)
  - Request queues (upRequests, downRequests)
  - Movement simulation (step-by-step)
  - Thread-safe operations
```

#### 4. Scheduling Strategies
```java
SchedulingStrategy              // Interface
  ├── NearestCarStrategy        // Assigns closest available elevator
  ├── RoundRobinStrategy        // Distributes load evenly
  └── ZoneBasedStrategy         // Divides building into zones
```

#### 5. Controller
```java
ElevatorController              // Singleton controller
  - Manages all elevators
  - Processes requests
  - Schedules assignments
  - Handles fault conditions
  - Thread-safe coordination
```

## Thread Safety

The system is designed for concurrent operations:

1. **ReentrantLock** - Protects critical sections in `Elevator` and `ElevatorController`
2. **ConcurrentHashMap** - Thread-safe request storage
3. **Volatile** fields - Ensures visibility of state changes across threads
4. **ScheduledExecutorService** - Manages concurrent elevator operations

## Scheduling Strategies

### 1. Nearest Car Strategy (Default)
- Assigns the elevator closest to the requesting floor
- Considers current direction and future path
- Prioritizes elevators already moving in the requested direction
- Best for minimizing wait times

### 2. Round Robin Strategy
- Distributes requests evenly across all elevators
- Simple load balancing
- Good for preventing elevator starvation

### 3. Zone-Based Strategy
- Divides building into vertical zones
- Assigns specific elevators to specific floor ranges
- Reduces cross-traffic and wait times in tall buildings
- Example: E1 serves floors 0-3, E2 serves 4-7, E3 serves 8-10

## Usage Example

```java
// Initialize system with 3 elevators serving floors 0-10
ElevatorController controller = ElevatorController.getInstance(3, 0, 10);

// Start the system
controller.start();

// Submit external request (person at floor 5 presses UP)
controller.submitExternalRequest(5, Direction.UP);

// Submit internal request (person in elevator E1 selects floor 8)
controller.submitInternalRequest("E1", 8);

// Check status
controller.printAllElevatorStatus();
String status = controller.getElevatorStatus("E1");

// Handle maintenance
controller.markElevatorOutOfService("E2");  // Reassigns pending requests
controller.restoreElevatorToService("E2");

// Change scheduling strategy at runtime
controller.setSchedulingStrategy(new RoundRobinStrategy());

// Shutdown
controller.shutdown();
```

## Functional Requirements Met

✅ Accept external requests (UP/DOWN from floors)  
✅ Accept internal requests (destination floor from inside)  
✅ Assign requests to elevators intelligently  
✅ Move elevators floor-by-floor with door operations  
✅ Query elevator status (floor, direction, doors, state)  
✅ Mark elevators out of service and reassign requests  
✅ Multiple scheduling policies (extensible)  
✅ Thread-safe concurrent request handling  

## Non-Functional Requirements Met

✅ **Thread-Safe**: All operations protected with locks  
✅ **Concurrent**: Handles multiple simultaneous requests  
✅ **Extensible**: Easy to add new scheduling strategies  
✅ **Scalable**: Supports N elevators and M floors  
✅ **Maintainable**: Clean code with SOLID principles  
✅ **Documented**: Comprehensive comments throughout  

## Key Algorithms

### Request Assignment
1. External request arrives → Added to pending queue
2. Controller polls queue every 500ms
3. Scheduling strategy selects best elevator
4. Request added to elevator's internal queue
5. Request marked as ASSIGNED

### Elevator Movement
1. Each elevator runs on 1-second intervals
2. Check if current floor has pending requests
3. If yes: Open doors → Wait → Close doors
4. If no: Move one floor in current direction
5. At boundaries: Switch direction or go IDLE
6. Smart direction switching based on pending requests

### Out-of-Service Handling
1. Mark elevator state as OUT_OF_SERVICE
2. Extract all pending requests from that elevator
3. Create new external requests for each pending floor
4. Reassign to other available elevators
5. Elevator stops accepting new requests until restored

## Compilation and Execution

### Using Java directly:
```bash
# Compile
cd Elevator-System/src/main/java
javac org/example/ElevatorControlSystem.java

# Run
java org.example.ElevatorControlSystem
```

### Using Maven:
```bash
cd Elevator-System
mvn clean compile
mvn exec:java -Dexec.mainClass="org.example.ElevatorControlSystem"
```

## Demo Scenarios

The main class demonstrates:

1. **Basic Requests** - Simple UP/DOWN requests
2. **Internal Requests** - Destination selection
3. **Concurrent Requests** - Multiple simultaneous requests
4. **Fault Handling** - Out-of-service scenarios
5. **Strategy Switching** - Round Robin strategy
6. **Zone-Based** - Zone assignment
7. **Stress Test** - 15 random requests

## Future Enhancements

- Priority requests (emergency, VIP)
- Energy optimization (idle elevator parking)
- Predictive algorithms (machine learning)
- Load balancing based on passenger count
- Express elevators for high floors
- Group elevator control
- Real-time performance metrics
- GUI visualization

## Design Decisions

### Why Single File?
- As requested, all components in one file
- In production, would split into packages:
  - `model/` - Elevator, Request classes
  - `enums/` - Direction, ElevatorState, RequestStatus
  - `strategies/` - Scheduling implementations
  - `controller/` - ElevatorController
  - `Main.java` - Entry point

### Why Thread-Safe?
- Real elevator systems have concurrent inputs
- Multiple users pressing buttons simultaneously
- Multiple elevators operating independently
- Need to prevent race conditions

### Why Strategy Pattern?
- Different buildings need different strategies
- Easy to A/B test different algorithms
- Can switch strategies at runtime
- Clean separation of concerns

### Why Singleton for Controller?
- Single point of control for all elevators
- Centralized state management
- Prevents multiple controller instances
- Thread-safe lazy initialization

## Testing Considerations

For production, implement:
- Unit tests for each scheduling strategy
- Integration tests for request flow
- Concurrent test scenarios
- Edge cases (boundary floors, invalid requests)
- Performance tests (high load)
- Fault injection tests

## Author

Low-Level Design Implementation  
Following SOLID principles and Design Patterns  
Java 17+

---

**Note**: This is a simulation. In a real elevator system, you'd need:
- Hardware integration (motor controllers, sensors)
- Safety systems (emergency stop, overload detection)
- Real-time OS for deterministic behavior
- Redundancy and failover mechanisms
- Regulatory compliance (elevator safety codes)

