# Elevator Control System - Quick Reference

## Quick Start

```java
// 1. Initialize system
ElevatorController controller = ElevatorController.getInstance(3, 0, 10);

// 2. Start
controller.start();

// 3. Submit requests
controller.submitExternalRequest(5, Direction.UP);
controller.submitInternalRequest("E1", 8);

// 4. Check status
controller.printAllElevatorStatus();

// 5. Shutdown
controller.shutdown();
```

## API Reference

### ElevatorController

#### Initialization
```java
// Get singleton instance
ElevatorController getInstance(int numElevators, int minFloor, int maxFloor)
ElevatorController getInstance(int numElevators, int minFloor, int maxFloor, SchedulingStrategy strategy)

// Reset instance (for testing)
void resetInstance()
```

#### Request Management
```java
// Submit external request (from floor)
void submitExternalRequest(int floor, Direction direction)

// Submit internal request (from inside elevator)
void submitInternalRequest(String elevatorId, int destinationFloor)
```

#### System Control
```java
// Start the system
void start()

// Shutdown the system
void shutdown()

// Change scheduling strategy
void setSchedulingStrategy(SchedulingStrategy strategy)
```

#### Elevator Management
```java
// Mark elevator out of service
void markElevatorOutOfService(String elevatorId)

// Restore elevator to service
void restoreElevatorToService(String elevatorId)

// Get status of specific elevator
String getElevatorStatus(String elevatorId)

// Print all elevator statuses
void printAllElevatorStatus()

// Get all elevators
List<Elevator> getElevators()
```

### Elevator

#### Getters
```java
String getElevatorId()
int getCurrentFloor()
Direction getCurrentDirection()
ElevatorState getState()
boolean isDoorsOpen()
int getMinFloor()
int getMaxFloor()
boolean isAvailable()
```

#### Status
```java
String getStatus()  // Detailed status string
List<Integer> getPendingRequests()
```

### Scheduling Strategies

#### NearestCarStrategy
```java
SchedulingStrategy strategy = new NearestCarStrategy();
```
- Assigns closest available elevator
- Considers direction and future path
- Best for minimizing wait times

#### RoundRobinStrategy
```java
SchedulingStrategy strategy = new RoundRobinStrategy();
```
- Distributes requests evenly
- Simple load balancing
- Prevents elevator starvation

#### ZoneBasedStrategy
```java
ZoneBasedStrategy strategy = new ZoneBasedStrategy();
strategy.assignZone("E1", 0, 3);   // Low floors
strategy.assignZone("E2", 4, 7);   // Mid floors
strategy.assignZone("E3", 8, 10);  // High floors
```
- Divides building into zones
- Assigns elevators to floor ranges
- Good for tall buildings

## Enums

### Direction
```java
Direction.UP      // Moving up
Direction.DOWN    // Moving down
Direction.IDLE    // Not moving
```

### ElevatorState
```java
ElevatorState.IDLE            // Not moving, no requests
ElevatorState.MOVING          // In transit
ElevatorState.STOPPED         // Stopped with doors closed
ElevatorState.DOOR_OPEN       // Doors open
ElevatorState.OUT_OF_SERVICE  // Disabled
```

### RequestStatus
```java
RequestStatus.PENDING       // Waiting for assignment
RequestStatus.ASSIGNED      // Assigned to elevator
RequestStatus.IN_PROGRESS   // Being processed
RequestStatus.COMPLETED     // Finished
RequestStatus.CANCELLED     // Cancelled
```

## Common Patterns

### Basic Usage
```java
ElevatorController controller = ElevatorController.getInstance(3, 0, 10);
controller.start();

// Person at floor 5 presses UP
controller.submitExternalRequest(5, Direction.UP);

// Person enters elevator E1 and selects floor 8
controller.submitInternalRequest("E1", 8);

Thread.sleep(5000);  // Wait for operation
controller.printAllElevatorStatus();

controller.shutdown();
```

### Switching Strategies
```java
// Start with nearest car
ElevatorController controller = ElevatorController.getInstance(
    3, 0, 10, new NearestCarStrategy()
);
controller.start();

// Switch to round robin
controller.setSchedulingStrategy(new RoundRobinStrategy());

// Switch to zone-based
ZoneBasedStrategy zoneStrategy = new ZoneBasedStrategy();
zoneStrategy.assignZone("E1", 0, 3);
zoneStrategy.assignZone("E2", 4, 7);
zoneStrategy.assignZone("E3", 8, 10);
controller.setSchedulingStrategy(zoneStrategy);
```

### Handling Maintenance
```java
// Mark elevator out of service
controller.markElevatorOutOfService("E2");

// Pending requests automatically reassigned to other elevators

// Restore to service
controller.restoreElevatorToService("E2");
```

### Query Status
```java
// Single elevator
String status = controller.getElevatorStatus("E1");
System.out.println(status);

// All elevators
controller.printAllElevatorStatus();
```

### Concurrent Requests
```java
// Multiple requests can be submitted simultaneously
ExecutorService executor = Executors.newFixedThreadPool(10);

executor.submit(() -> controller.submitExternalRequest(2, Direction.UP));
executor.submit(() -> controller.submitExternalRequest(5, Direction.DOWN));
executor.submit(() -> controller.submitInternalRequest("E1", 7));
executor.submit(() -> controller.submitExternalRequest(8, Direction.UP));

executor.shutdown();
```

## Configuration Examples

### Small Building (1 Elevator, 5 Floors)
```java
ElevatorController controller = ElevatorController.getInstance(1, 0, 5);
```

### Medium Building (3 Elevators, 15 Floors)
```java
ElevatorController controller = ElevatorController.getInstance(3, 0, 15);
```

### Large Building (10 Elevators, 50 Floors)
```java
ElevatorController controller = ElevatorController.getInstance(10, 0, 50);
```

### Building with Basement (5 Elevators, -2 to 30)
```java
ElevatorController controller = ElevatorController.getInstance(5, -2, 30);
```

### Skyscraper with Zone Strategy
```java
ElevatorController controller = ElevatorController.getInstance(8, 0, 100);
ZoneBasedStrategy strategy = new ZoneBasedStrategy();

// Low-rise elevators
strategy.assignZone("E1", 0, 25);
strategy.assignZone("E2", 0, 25);

// Mid-rise elevators
strategy.assignZone("E3", 26, 50);
strategy.assignZone("E4", 26, 50);

// High-rise elevators
strategy.assignZone("E5", 51, 75);
strategy.assignZone("E6", 51, 75);

// Sky lobbies
strategy.assignZone("E7", 76, 100);
strategy.assignZone("E8", 76, 100);

controller.setSchedulingStrategy(strategy);
controller.start();
```

## Timing Parameters

```java
// Request processing interval: 500ms
// Elevator movement interval: 1000ms (1 second per floor)
// Door open duration: 1 second (one cycle)

// Configured in ElevatorController:
executorService.scheduleAtFixedRate(
    this::processPendingRequests,
    0, 500, TimeUnit.MILLISECONDS
);

executorService.scheduleAtFixedRate(
    elevator::step,
    0, 1000, TimeUnit.MILLISECONDS
);
```

## Error Handling

### Invalid Floor
```java
try {
    controller.submitExternalRequest(100, Direction.UP);  // If maxFloor < 100
} catch (IllegalArgumentException e) {
    System.out.println("Invalid floor: " + e.getMessage());
}
```

### Invalid Direction
```java
try {
    controller.submitExternalRequest(5, Direction.IDLE);  // IDLE not allowed
} catch (IllegalArgumentException e) {
    System.out.println("Invalid direction: " + e.getMessage());
}
```

### Elevator Not Found
```java
controller.submitInternalRequest("E99", 5);  // Non-existent elevator
// Prints: "Elevator not found: E99"
```

### Out of Service
```java
controller.markElevatorOutOfService("E1");
controller.submitInternalRequest("E1", 5);
// Prints: "Elevator E1 is out of service"
```

## Testing Examples

### Unit Test - Scheduling Strategy
```java
@Test
public void testNearestCarStrategy() {
    List<Elevator> elevators = Arrays.asList(
        new Elevator("E1", 0, 10, 0),
        new Elevator("E2", 0, 10, 5),
        new Elevator("E3", 0, 10, 9)
    );
    
    ExternalRequest request = new ExternalRequest(6, Direction.UP);
    SchedulingStrategy strategy = new NearestCarStrategy();
    
    Elevator selected = strategy.selectElevator(request, elevators);
    
    assertEquals("E2", selected.getElevatorId());
}
```

### Integration Test - Request Flow
```java
@Test
public void testRequestFlow() throws InterruptedException {
    ElevatorController controller = ElevatorController.getInstance(2, 0, 10);
    controller.start();
    
    controller.submitExternalRequest(5, Direction.UP);
    
    Thread.sleep(6000);  // Wait for elevator to arrive
    
    List<Elevator> elevators = controller.getElevators();
    boolean foundAtFloor5 = elevators.stream()
        .anyMatch(e -> e.getCurrentFloor() == 5);
    
    assertTrue(foundAtFloor5);
    
    controller.shutdown();
    ElevatorController.resetInstance();
}
```

## Performance Tips

1. **Batch Requests**: Submit multiple requests together rather than one-by-one
2. **Choose Right Strategy**: 
   - Use NearestCar for minimizing wait times
   - Use RoundRobin for even load distribution
   - Use ZoneBased for tall buildings
3. **Avoid Frequent Strategy Switches**: Changing strategy frequently can disrupt optimization
4. **Monitor Status Periodically**: Use `printAllElevatorStatus()` to debug issues

## Common Issues

### Issue: Elevator not moving
**Check**:
- Is the system started? `controller.start()`
- Is elevator out of service? `elevator.getState()`
- Are there any requests? `elevator.getPendingRequests()`

### Issue: Long wait times
**Solution**:
- Add more elevators
- Switch to NearestCarStrategy
- Use ZoneBasedStrategy for tall buildings

### Issue: Uneven load distribution
**Solution**:
- Switch to RoundRobinStrategy
- Ensure elevators are not out of service

### Issue: Thread safety concerns
**Note**:
- All methods are thread-safe
- Use concurrent collections
- Protected with ReentrantLock

## Best Practices

1. ✅ Always call `start()` before submitting requests
2. ✅ Always call `shutdown()` when done
3. ✅ Use `resetInstance()` between tests
4. ✅ Handle InterruptedException when using Thread.sleep()
5. ✅ Check elevator availability before internal requests
6. ✅ Use appropriate scheduling strategy for building type
7. ✅ Monitor system status during operation
8. ✅ Handle out-of-service scenarios gracefully

## Additional Resources

- **Full Documentation**: See `README.md`
- **Design Details**: See `DESIGN.md`
- **Source Code**: `src/main/java/org/example/ElevatorControlSystem.java`

---

**Version**: 1.0  
**Last Updated**: 2025

