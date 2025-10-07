package org.example;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Comprehensive Elevator Control System
 * 
 * This system manages multiple elevators in a building, handling external pickup requests
 * from floors and internal destination requests from passengers inside elevators.
 * 
 * Design Patterns Used:
 * 1. Strategy Pattern - For scheduling algorithms
 * 2. State Pattern - For elevator states
 * 3. Observer Pattern - For request notifications
 * 4. Singleton Pattern - For the main controller
 * 5. Factory Pattern - For creating requests
 * 
 * SOLID Principles:
 * - Single Responsibility: Each class has one clear purpose
 * - Open/Closed: Extensible through strategy interfaces
 * - Liskov Substitution: Strategy implementations are interchangeable
 * - Interface Segregation: Focused interfaces for different concerns
 * - Dependency Inversion: Depends on abstractions (interfaces)
 */

// ==================== ENUMS ====================

/**
 * Direction of elevator movement or request
 */
enum Direction {
    UP,
    DOWN,
    IDLE
}

/**
 * Current state of the elevator
 */
enum ElevatorState {
    IDLE,           // Not moving, no requests
    MOVING,         // In transit between floors
    STOPPED,        // Stopped at a floor with doors closed
    DOOR_OPEN,      // Doors are open for passengers
    OUT_OF_SERVICE  // Elevator is disabled/maintenance
}

/**
 * Status of a request
 */
enum RequestStatus {
    PENDING,
    ASSIGNED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

// ==================== REQUEST MODELS ====================

/**
 * Base class for all requests
 * Demonstrates abstraction and encapsulation
 */
abstract class Request {
    private final String requestId;
    private final long timestamp;
    private RequestStatus status;
    
    public Request() {
        this.requestId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.status = RequestStatus.PENDING;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public RequestStatus getStatus() {
        return status;
    }
    
    public void setStatus(RequestStatus status) {
        this.status = status;
    }
    
    public abstract int getTargetFloor();
}

/**
 * External request from a floor (UP or DOWN button press)
 */
class ExternalRequest extends Request {
    private final int floor;
    private final Direction direction;
    
    public ExternalRequest(int floor, Direction direction) {
        super();
        if (direction == Direction.IDLE) {
            throw new IllegalArgumentException("External request cannot have IDLE direction");
        }
        this.floor = floor;
        this.direction = direction;
    }
    
    public int getFloor() {
        return floor;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    @Override
    public int getTargetFloor() {
        return floor;
    }
    
    @Override
    public String toString() {
        return String.format("ExternalRequest[floor=%d, direction=%s, status=%s]", 
            floor, direction, getStatus());
    }
}

/**
 * Internal request from inside an elevator (destination floor)
 */
class InternalRequest extends Request {
    private final int destinationFloor;
    private final String elevatorId;
    
    public InternalRequest(int destinationFloor, String elevatorId) {
        super();
        this.destinationFloor = destinationFloor;
        this.elevatorId = elevatorId;
    }
    
    public int getDestinationFloor() {
        return destinationFloor;
    }
    
    public String getElevatorId() {
        return elevatorId;
    }
    
    @Override
    public int getTargetFloor() {
        return destinationFloor;
    }
    
    @Override
    public String toString() {
        return String.format("InternalRequest[destination=%d, elevatorId=%s, status=%s]", 
            destinationFloor, elevatorId, getStatus());
    }
}

// ==================== ELEVATOR MODEL ====================

/**
 * Represents a single elevator in the system
 * Thread-safe implementation with synchronized methods
 */
class Elevator {
    private final String elevatorId;
    private final int minFloor;
    private final int maxFloor;
    
    // Current state (thread-safe access)
    private volatile int currentFloor;
    private volatile Direction currentDirection;
    private volatile ElevatorState state;
    private volatile boolean doorsOpen;
    
    // Request queues for this elevator
    private final Set<Integer> upRequests;      // Floors to stop at while going up
    private final Set<Integer> downRequests;    // Floors to stop at while going down
    
    private final ReentrantLock lock;
    
    public Elevator(String elevatorId, int minFloor, int maxFloor, int startFloor) {
        this.elevatorId = elevatorId;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.currentFloor = startFloor;
        this.currentDirection = Direction.IDLE;
        this.state = ElevatorState.IDLE;
        this.doorsOpen = false;
        this.upRequests = ConcurrentHashMap.newKeySet();
        this.downRequests = ConcurrentHashMap.newKeySet();
        this.lock = new ReentrantLock();
    }
    
    // Getters
    public String getElevatorId() {
        return elevatorId;
    }
    
    public int getCurrentFloor() {
        return currentFloor;
    }
    
    public Direction getCurrentDirection() {
        return currentDirection;
    }
    
    public ElevatorState getState() {
        return state;
    }
    
    public boolean isDoorsOpen() {
        return doorsOpen;
    }
    
    public int getMinFloor() {
        return minFloor;
    }
    
    public int getMaxFloor() {
        return maxFloor;
    }
    
    /**
     * Check if elevator is available for assignment
     */
    public boolean isAvailable() {
        return state != ElevatorState.OUT_OF_SERVICE;
    }
    
    /**
     * Mark elevator as out of service
     */
    public void markOutOfService() {
        lock.lock();
        try {
            this.state = ElevatorState.OUT_OF_SERVICE;
            this.currentDirection = Direction.IDLE;
            System.out.println("Elevator " + elevatorId + " marked OUT OF SERVICE");
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Restore elevator to service
     */
    public void restoreToService() {
        lock.lock();
        try {
            this.state = ElevatorState.IDLE;
            this.currentDirection = Direction.IDLE;
            System.out.println("Elevator " + elevatorId + " restored to service");
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Add a floor request to this elevator
     */
    public void addRequest(int floor, Direction direction) {
        if (floor < minFloor || floor > maxFloor) {
            throw new IllegalArgumentException("Invalid floor: " + floor);
        }
        
        lock.lock();
        try {
            if (direction == Direction.UP || direction == Direction.IDLE) {
                upRequests.add(floor);
            }
            if (direction == Direction.DOWN || direction == Direction.IDLE) {
                downRequests.add(floor);
            }
            
            // If idle, determine initial direction
            if (state == ElevatorState.IDLE) {
                if (floor > currentFloor) {
                    currentDirection = Direction.UP;
                } else if (floor < currentFloor) {
                    currentDirection = Direction.DOWN;
                } else {
                    currentDirection = Direction.IDLE;
                }
                state = ElevatorState.MOVING;
            }
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Get all pending requests for this elevator
     */
    public List<Integer> getPendingRequests() {
        lock.lock();
        try {
            List<Integer> requests = new ArrayList<>();
            requests.addAll(upRequests);
            requests.addAll(downRequests);
            return requests;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Clear all pending requests (used when elevator goes out of service)
     */
    public List<Integer> clearAllRequests() {
        lock.lock();
        try {
            List<Integer> allRequests = new ArrayList<>();
            allRequests.addAll(upRequests);
            allRequests.addAll(downRequests);
            upRequests.clear();
            downRequests.clear();
            return allRequests;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Process one step of elevator movement
     * This method simulates the elevator's operation
     */
    public void step() {
        if (state == ElevatorState.OUT_OF_SERVICE) {
            return;
        }
        
        lock.lock();
        try {
            // If doors are open, close them
            if (doorsOpen) {
                closeDoors();
                return;
            }
            
            // Check if we should stop at current floor
            boolean shouldStop = false;
            if (currentDirection == Direction.UP && upRequests.contains(currentFloor)) {
                shouldStop = true;
                upRequests.remove(currentFloor);
            } else if (currentDirection == Direction.DOWN && downRequests.contains(currentFloor)) {
                shouldStop = true;
                downRequests.remove(currentFloor);
            }
            
            if (shouldStop) {
                openDoors();
                return;
            }
            
            // Determine next move
            if (currentDirection == Direction.UP) {
                // Check if there are more floors to visit going up
                boolean hasUpperRequests = upRequests.stream().anyMatch(f -> f > currentFloor);
                
                if (hasUpperRequests && currentFloor < maxFloor) {
                    moveUp();
                } else {
                    // Change direction or go idle
                    switchDirection();
                }
            } else if (currentDirection == Direction.DOWN) {
                // Check if there are more floors to visit going down
                boolean hasLowerRequests = downRequests.stream().anyMatch(f -> f < currentFloor);
                
                if (hasLowerRequests && currentFloor > minFloor) {
                    moveDown();
                } else {
                    // Change direction or go idle
                    switchDirection();
                }
            } else {
                // IDLE - check if there are any requests
                if (!upRequests.isEmpty()) {
                    currentDirection = Direction.UP;
                    state = ElevatorState.MOVING;
                } else if (!downRequests.isEmpty()) {
                    currentDirection = Direction.DOWN;
                    state = ElevatorState.MOVING;
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Move elevator up one floor
     */
    private void moveUp() {
        currentFloor++;
        state = ElevatorState.MOVING;
        System.out.println("Elevator " + elevatorId + " moved UP to floor " + currentFloor);
    }
    
    /**
     * Move elevator down one floor
     */
    private void moveDown() {
        currentFloor--;
        state = ElevatorState.MOVING;
        System.out.println("Elevator " + elevatorId + " moved DOWN to floor " + currentFloor);
    }
    
    /**
     * Open doors at current floor
     */
    private void openDoors() {
        doorsOpen = true;
        state = ElevatorState.DOOR_OPEN;
        System.out.println("Elevator " + elevatorId + " DOORS OPEN at floor " + currentFloor);
    }
    
    /**
     * Close doors at current floor
     */
    private void closeDoors() {
        doorsOpen = false;
        state = ElevatorState.STOPPED;
        System.out.println("Elevator " + elevatorId + " DOORS CLOSED at floor " + currentFloor);
    }
    
    /**
     * Switch direction or go idle
     */
    private void switchDirection() {
        if (currentDirection == Direction.UP) {
            // Check if there are down requests
            if (!downRequests.isEmpty()) {
                currentDirection = Direction.DOWN;
                state = ElevatorState.MOVING;
            } else if (!upRequests.isEmpty()) {
                // Stay going up
                state = ElevatorState.MOVING;
            } else {
                currentDirection = Direction.IDLE;
                state = ElevatorState.IDLE;
            }
        } else if (currentDirection == Direction.DOWN) {
            // Check if there are up requests
            if (!upRequests.isEmpty()) {
                currentDirection = Direction.UP;
                state = ElevatorState.MOVING;
            } else if (!downRequests.isEmpty()) {
                // Stay going down
                state = ElevatorState.MOVING;
            } else {
                currentDirection = Direction.IDLE;
                state = ElevatorState.IDLE;
            }
        }
    }
    
    /**
     * Get status summary of this elevator
     */
    public String getStatus() {
        return String.format("Elevator[id=%s, floor=%d, direction=%s, state=%s, doors=%s, upRequests=%s, downRequests=%s]",
            elevatorId, currentFloor, currentDirection, state, 
            doorsOpen ? "OPEN" : "CLOSED",
            upRequests, downRequests);
    }
}

// ==================== SCHEDULING STRATEGIES ====================

/**
 * Strategy interface for elevator scheduling
 * Demonstrates Strategy Pattern and Open/Closed Principle
 */
interface SchedulingStrategy {
    /**
     * Select the best elevator for a given external request
     * @param request The external request to assign
     * @param elevators List of available elevators
     * @return The selected elevator, or null if none available
     */
    Elevator selectElevator(ExternalRequest request, List<Elevator> elevators);
}

/**
 * Nearest Car scheduling strategy
 * Assigns the elevator closest to the requesting floor
 */
class NearestCarStrategy implements SchedulingStrategy {
    
    @Override
    public Elevator selectElevator(ExternalRequest request, List<Elevator> elevators) {
        if (elevators.isEmpty()) {
            return null;
        }
        
        int requestFloor = request.getFloor();
        Direction requestDirection = request.getDirection();
        
        // Filter available elevators
        List<Elevator> available = elevators.stream()
            .filter(Elevator::isAvailable)
            .collect(Collectors.toList());
        
        if (available.isEmpty()) {
            return null;
        }
        
        // Priority 1: Elevators already moving in the same direction and can pick up on the way
        Elevator bestMatch = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Elevator elevator : available) {
            int distance = calculateDistance(elevator, requestFloor, requestDirection);
            if (distance < minDistance) {
                minDistance = distance;
                bestMatch = elevator;
            }
        }
        
        return bestMatch;
    }
    
    /**
     * Calculate effective distance considering direction and current state
     */
    private int calculateDistance(Elevator elevator, int requestFloor, Direction requestDirection) {
        int currentFloor = elevator.getCurrentFloor();
        Direction currentDirection = elevator.getCurrentDirection();
        
        // If elevator is idle, just use absolute distance
        if (currentDirection == Direction.IDLE) {
            return Math.abs(requestFloor - currentFloor);
        }
        
        // If moving in same direction and request is ahead
        if (currentDirection == requestDirection) {
            if (requestDirection == Direction.UP && requestFloor >= currentFloor) {
                return requestFloor - currentFloor; // On the way
            } else if (requestDirection == Direction.DOWN && requestFloor <= currentFloor) {
                return currentFloor - requestFloor; // On the way
            }
        }
        
        // Elevator needs to complete current direction first
        // Give it a penalty to prefer idle or better-aligned elevators
        return Math.abs(requestFloor - currentFloor) + 100;
    }
}

/**
 * Round Robin scheduling strategy
 * Distributes load evenly across elevators
 */
class RoundRobinStrategy implements SchedulingStrategy {
    private int lastAssignedIndex = -1;
    
    @Override
    public synchronized Elevator selectElevator(ExternalRequest request, List<Elevator> elevators) {
        if (elevators.isEmpty()) {
            return null;
        }
        
        // Filter available elevators
        List<Elevator> available = elevators.stream()
            .filter(Elevator::isAvailable)
            .collect(Collectors.toList());
        
        if (available.isEmpty()) {
            return null;
        }
        
        // Round-robin assignment
        lastAssignedIndex = (lastAssignedIndex + 1) % available.size();
        return available.get(lastAssignedIndex);
    }
}

/**
 * Zone-based scheduling strategy
 * Divides building into zones and assigns elevators to zones
 */
class ZoneBasedStrategy implements SchedulingStrategy {
    private final Map<String, int[]> elevatorZones; // elevatorId -> [minFloor, maxFloor]
    
    public ZoneBasedStrategy() {
        this.elevatorZones = new ConcurrentHashMap<>();
    }
    
    /**
     * Assign a zone to an elevator
     */
    public void assignZone(String elevatorId, int minFloor, int maxFloor) {
        elevatorZones.put(elevatorId, new int[]{minFloor, maxFloor});
    }
    
    @Override
    public Elevator selectElevator(ExternalRequest request, List<Elevator> elevators) {
        if (elevators.isEmpty()) {
            return null;
        }
        
        int requestFloor = request.getFloor();
        
        // Find elevators in the zone
        List<Elevator> inZone = elevators.stream()
            .filter(Elevator::isAvailable)
            .filter(e -> isInZone(e.getElevatorId(), requestFloor))
            .collect(Collectors.toList());
        
        if (inZone.isEmpty()) {
            // Fallback to nearest car
            return new NearestCarStrategy().selectElevator(request, elevators);
        }
        
        // Select nearest in zone
        return inZone.stream()
            .min(Comparator.comparingInt(e -> Math.abs(e.getCurrentFloor() - requestFloor)))
            .orElse(null);
    }
    
    private boolean isInZone(String elevatorId, int floor) {
        int[] zone = elevatorZones.get(elevatorId);
        if (zone == null) {
            return true; // No zone assigned, can serve any floor
        }
        return floor >= zone[0] && floor <= zone[1];
    }
}

// ==================== ELEVATOR CONTROLLER ====================

/**
 * Main controller for the elevator system
 * Implements Singleton pattern for centralized control
 * Thread-safe implementation for concurrent requests
 */
class ElevatorController {
    private static volatile ElevatorController instance;
    
    private final List<Elevator> elevators;
    private final Queue<ExternalRequest> pendingExternalRequests;
    private final Map<String, InternalRequest> pendingInternalRequests;
    
    private SchedulingStrategy schedulingStrategy;
    
    private final ReentrantLock lock;
    private final ScheduledExecutorService executorService;
    
    private final int minFloor;
    private final int maxFloor;
    
    /**
     * Private constructor for Singleton pattern
     */
    private ElevatorController(int numElevators, int minFloor, int maxFloor, SchedulingStrategy strategy) {
        this.elevators = new ArrayList<>();
        this.pendingExternalRequests = new ConcurrentLinkedQueue<>();
        this.pendingInternalRequests = new ConcurrentHashMap<>();
        this.schedulingStrategy = strategy != null ? strategy : new NearestCarStrategy();
        this.lock = new ReentrantLock();
        this.executorService = Executors.newScheduledThreadPool(numElevators + 1);
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        
        // Initialize elevators
        for (int i = 0; i < numElevators; i++) {
            Elevator elevator = new Elevator("E" + (i + 1), minFloor, maxFloor, 0);
            elevators.add(elevator);
        }
    }
    
    /**
     * Get singleton instance (thread-safe double-checked locking)
     */
    public static ElevatorController getInstance(int numElevators, int minFloor, int maxFloor, SchedulingStrategy strategy) {
        if (instance == null) {
            synchronized (ElevatorController.class) {
                if (instance == null) {
                    instance = new ElevatorController(numElevators, minFloor, maxFloor, strategy);
                }
            }
        }
        return instance;
    }
    
    /**
     * Get singleton instance with default strategy
     */
    public static ElevatorController getInstance(int numElevators, int minFloor, int maxFloor) {
        return getInstance(numElevators, minFloor, maxFloor, null);
    }
    
    /**
     * Reset singleton instance (useful for testing)
     */
    public static void resetInstance() {
        if (instance != null) {
            instance.shutdown();
            instance = null;
        }
    }
    
    /**
     * Change scheduling strategy at runtime
     */
    public void setSchedulingStrategy(SchedulingStrategy strategy) {
        lock.lock();
        try {
            this.schedulingStrategy = strategy;
            System.out.println("Scheduling strategy changed to: " + strategy.getClass().getSimpleName());
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Submit an external request (from a floor)
     */
    public void submitExternalRequest(int floor, Direction direction) {
        if (floor < minFloor || floor > maxFloor) {
            throw new IllegalArgumentException("Invalid floor: " + floor);
        }
        
        if (direction == Direction.IDLE) {
            throw new IllegalArgumentException("External request must have UP or DOWN direction");
        }
        
        ExternalRequest request = new ExternalRequest(floor, direction);
        pendingExternalRequests.offer(request);
        System.out.println("External request submitted: " + request);
    }
    
    /**
     * Submit an internal request (from inside an elevator)
     */
    public void submitInternalRequest(String elevatorId, int destinationFloor) {
        if (destinationFloor < minFloor || destinationFloor > maxFloor) {
            throw new IllegalArgumentException("Invalid destination floor: " + destinationFloor);
        }
        
        // Find the elevator
        Elevator elevator = findElevatorById(elevatorId);
        if (elevator == null) {
            System.out.println("Elevator not found: " + elevatorId);
            return;
        }
        
        if (!elevator.isAvailable()) {
            System.out.println("Elevator " + elevatorId + " is out of service");
            return;
        }
        
        InternalRequest request = new InternalRequest(destinationFloor, elevatorId);
        pendingInternalRequests.put(request.getRequestId(), request);
        
        // Determine direction based on current floor and destination
        Direction direction = Direction.IDLE;
        if (destinationFloor > elevator.getCurrentFloor()) {
            direction = Direction.UP;
        } else if (destinationFloor < elevator.getCurrentFloor()) {
            direction = Direction.DOWN;
        }
        
        elevator.addRequest(destinationFloor, direction);
        request.setStatus(RequestStatus.ASSIGNED);
        
        System.out.println("Internal request submitted and assigned: " + request);
    }
    
    /**
     * Process pending external requests and assign to elevators
     */
    private void processPendingRequests() {
        lock.lock();
        try {
            // Process external requests
            while (!pendingExternalRequests.isEmpty()) {
                ExternalRequest request = pendingExternalRequests.poll();
                
                // Find best elevator using scheduling strategy
                Elevator selectedElevator = schedulingStrategy.selectElevator(request, elevators);
                
                if (selectedElevator != null) {
                    selectedElevator.addRequest(request.getFloor(), request.getDirection());
                    request.setStatus(RequestStatus.ASSIGNED);
                    System.out.println("Assigned " + request + " to " + selectedElevator.getElevatorId());
                } else {
                    // No elevator available, put back in queue
                    pendingExternalRequests.offer(request);
                    break; // Try again later
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Start the elevator system
     */
    public void start() {
        // Schedule request processing
        executorService.scheduleAtFixedRate(
            this::processPendingRequests,
            0, 500, TimeUnit.MILLISECONDS
        );
        
        // Schedule elevator movement for each elevator
        for (Elevator elevator : elevators) {
            executorService.scheduleAtFixedRate(
                elevator::step,
                0, 1000, TimeUnit.MILLISECONDS
            );
        }
        
        System.out.println("Elevator system started with " + elevators.size() + " elevators");
    }
    
    /**
     * Shutdown the elevator system
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("Elevator system shut down");
    }
    
    /**
     * Mark an elevator as out of service and reassign its requests
     */
    public void markElevatorOutOfService(String elevatorId) {
        lock.lock();
        try {
            Elevator elevator = findElevatorById(elevatorId);
            if (elevator == null) {
                System.out.println("Elevator not found: " + elevatorId);
                return;
            }
            
            // Get all pending requests from this elevator
            List<Integer> pendingFloors = elevator.clearAllRequests();
            
            // Mark elevator as out of service
            elevator.markOutOfService();
            
            // Reassign requests to other elevators
            for (Integer floor : pendingFloors) {
                // Create new external request (we don't know original direction, so create both)
                if (floor != elevator.getCurrentFloor()) {
                    Direction dir = floor > elevator.getCurrentFloor() ? Direction.UP : Direction.DOWN;
                    submitExternalRequest(floor, dir);
                }
            }
            
            System.out.println("Elevator " + elevatorId + " marked out of service, " + 
                pendingFloors.size() + " requests reassigned");
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Restore an elevator to service
     */
    public void restoreElevatorToService(String elevatorId) {
        lock.lock();
        try {
            Elevator elevator = findElevatorById(elevatorId);
            if (elevator == null) {
                System.out.println("Elevator not found: " + elevatorId);
                return;
            }
            
            elevator.restoreToService();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Get status of a specific elevator
     */
    public String getElevatorStatus(String elevatorId) {
        Elevator elevator = findElevatorById(elevatorId);
        if (elevator == null) {
            return "Elevator not found: " + elevatorId;
        }
        return elevator.getStatus();
    }
    
    /**
     * Get status of all elevators
     */
    public void printAllElevatorStatus() {
        System.out.println("\n========== Elevator System Status ==========");
        for (Elevator elevator : elevators) {
            System.out.println(elevator.getStatus());
        }
        System.out.println("Pending external requests: " + pendingExternalRequests.size());
        System.out.println("Pending internal requests: " + pendingInternalRequests.size());
        System.out.println("===========================================\n");
    }
    
    /**
     * Find elevator by ID
     */
    private Elevator findElevatorById(String elevatorId) {
        return elevators.stream()
            .filter(e -> e.getElevatorId().equals(elevatorId))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Get all elevators (for testing)
     */
    public List<Elevator> getElevators() {
        return new ArrayList<>(elevators);
    }
}

// ==================== MAIN CLASS ====================

/**
 * Main class demonstrating the elevator control system
 */
public class ElevatorControlSystem {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Elevator Control System Demo ===\n");
        
        // Initialize the system with 3 elevators, floors 0-10
        ElevatorController controller = ElevatorController.getInstance(3, 0, 10);
        
        // Start the system
        controller.start();
        
        // Demo 1: Basic requests
        System.out.println("\n--- Demo 1: Basic Requests ---");
        controller.submitExternalRequest(5, Direction.UP);
        controller.submitExternalRequest(3, Direction.DOWN);
        controller.submitExternalRequest(8, Direction.UP);
        
        Thread.sleep(3000);
        controller.printAllElevatorStatus();
        
        // Demo 2: Internal requests
        System.out.println("\n--- Demo 2: Internal Requests ---");
        controller.submitInternalRequest("E1", 7);
        controller.submitInternalRequest("E2", 1);
        
        Thread.sleep(3000);
        controller.printAllElevatorStatus();
        
        // Demo 3: Multiple concurrent requests
        System.out.println("\n--- Demo 3: Multiple Concurrent Requests ---");
        controller.submitExternalRequest(2, Direction.UP);
        controller.submitExternalRequest(6, Direction.DOWN);
        controller.submitExternalRequest(9, Direction.DOWN);
        controller.submitInternalRequest("E3", 4);
        
        Thread.sleep(5000);
        controller.printAllElevatorStatus();
        
        // Demo 4: Out of service handling
        System.out.println("\n--- Demo 4: Out of Service Handling ---");
        controller.submitExternalRequest(5, Direction.UP);
        controller.submitInternalRequest("E1", 8);
        
        Thread.sleep(1000);
        System.out.println("Marking E1 out of service...");
        controller.markElevatorOutOfService("E1");
        
        Thread.sleep(5000);
        controller.printAllElevatorStatus();
        
        System.out.println("Restoring E1 to service...");
        controller.restoreElevatorToService("E1");
        
        Thread.sleep(3000);
        controller.printAllElevatorStatus();
        
        // Demo 5: Different scheduling strategies
        System.out.println("\n--- Demo 5: Testing Round Robin Strategy ---");
        controller.setSchedulingStrategy(new RoundRobinStrategy());
        
        controller.submitExternalRequest(2, Direction.UP);
        controller.submitExternalRequest(4, Direction.UP);
        controller.submitExternalRequest(6, Direction.UP);
        controller.submitExternalRequest(8, Direction.UP);
        
        Thread.sleep(5000);
        controller.printAllElevatorStatus();
        
        // Demo 6: Zone-based strategy
        System.out.println("\n--- Demo 6: Testing Zone-Based Strategy ---");
        ZoneBasedStrategy zoneStrategy = new ZoneBasedStrategy();
        zoneStrategy.assignZone("E1", 0, 3);   // Low floors
        zoneStrategy.assignZone("E2", 4, 7);   // Mid floors
        zoneStrategy.assignZone("E3", 8, 10);  // High floors
        controller.setSchedulingStrategy(zoneStrategy);
        
        controller.submitExternalRequest(2, Direction.UP);   // Should go to E1
        controller.submitExternalRequest(5, Direction.UP);   // Should go to E2
        controller.submitExternalRequest(9, Direction.DOWN); // Should go to E3
        
        Thread.sleep(5000);
        controller.printAllElevatorStatus();
        
        // Demo 7: Stress test with many requests
        System.out.println("\n--- Demo 7: Stress Test ---");
        controller.setSchedulingStrategy(new NearestCarStrategy());
        
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            int floor = random.nextInt(11); // 0-10
            Direction dir = random.nextBoolean() ? Direction.UP : Direction.DOWN;
            controller.submitExternalRequest(floor, dir);
        }
        
        Thread.sleep(10000);
        controller.printAllElevatorStatus();
        
        // Shutdown
        System.out.println("\n--- Shutting Down System ---");
        controller.shutdown();
        
        System.out.println("\n=== Demo Complete ===");
    }
}

