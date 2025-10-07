package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Moderate difficulty Elevator System LLD
 * ----------------------------------------
 * Features:
 * - Multiple elevators
 * - External + Internal requests
 * - Simple nearest-car scheduling
 * - Elevator movement simulation
 * - Extensible design (Strategy Pattern)
 *
 * Author: Abhinav
 */
public class ElevatorSystem {

    // ---------------- ENUMS ----------------
    enum Direction { UP, DOWN, IDLE }
    enum DoorState { OPEN, CLOSED }
    enum ElevatorStatus { ACTIVE, OUT_OF_SERVICE }

    // ---------------- REQUESTS ----------------
    static abstract class Request {
        final int floor;
        final long timestamp;

        Request(int floor) {
            this.floor = floor;
            this.timestamp = System.currentTimeMillis();
        }

        int getFloor() { return floor; }
    }

    static class ExternalRequest extends Request {
        final Direction direction;
        ExternalRequest(int floor, Direction direction) {
            super(floor);
            this.direction = direction;
        }
    }

    static class InternalRequest extends Request {
        InternalRequest(int floor) {
            super(floor);
        }
    }

    // ---------------- ELEVATOR ----------------
    static class Elevator {
        private final int id;
        private int currentFloor;
        private Direction direction;
        private DoorState doorState;
        private ElevatorStatus status;

        private final NavigableSet<Integer> upStops = new TreeSet<>();
        private final NavigableSet<Integer> downStops = new TreeSet<>(Collections.reverseOrder());

        Elevator(int id, int initialFloor) {
            this.id = id;
            this.currentFloor = initialFloor;
            this.direction = Direction.IDLE;
            this.doorState = DoorState.CLOSED;
            this.status = ElevatorStatus.ACTIVE;
        }

        public synchronized int getCurrentFloor() { return currentFloor; }
        public synchronized Direction getDirection() { return direction; }
        public synchronized DoorState getDoorState() { return doorState; }
        public synchronized ElevatorStatus getStatus() { return status; }
        public int getId() { return id; }

        public synchronized void addStop(int floor) {
            if (status == ElevatorStatus.OUT_OF_SERVICE) return;
            if (floor == currentFloor) return;

            if (floor > currentFloor) upStops.add(floor);
            else downStops.add(floor);

            if (direction == Direction.IDLE)
                direction = (floor > currentFloor) ? Direction.UP : Direction.DOWN;
        }

        public synchronized void moveOneStep() {
            if (status == ElevatorStatus.OUT_OF_SERVICE) return;

            if (direction == Direction.UP) currentFloor++;
            else if (direction == Direction.DOWN) currentFloor--;

            // Check if reached any stop
            if (upStops.contains(currentFloor)) {
                upStops.remove(currentFloor);
                openDoors();
                closeDoors();
            } else if (downStops.contains(currentFloor)) {
                downStops.remove(currentFloor);
                openDoors();
                closeDoors();
            }

            // Decide next direction
            if (direction == Direction.UP && upStops.isEmpty()) {
                if (!downStops.isEmpty()) direction = Direction.DOWN;
                else direction = Direction.IDLE;
            } else if (direction == Direction.DOWN && downStops.isEmpty()) {
                if (!upStops.isEmpty()) direction = Direction.UP;
                else direction = Direction.IDLE;
            }
        }

        public synchronized void openDoors() {
            doorState = DoorState.OPEN;
            System.out.println("Elevator " + id + " opened doors at floor " + currentFloor);
        }

        public synchronized void closeDoors() {
            doorState = DoorState.CLOSED;
        }

        public synchronized void setOutOfService() {
            status = ElevatorStatus.OUT_OF_SERVICE;
            direction = Direction.IDLE;
            upStops.clear();
            downStops.clear();
        }

        public synchronized void setActive() {
            status = ElevatorStatus.ACTIVE;
        }

        @Override
        public synchronized String toString() {
            return "Elevator{" +
                    "id=" + id +
                    ", floor=" + currentFloor +
                    ", dir=" + direction +
                    ", doors=" + doorState +
                    ", status=" + status +
                    ", upStops=" + upStops +
                    ", downStops=" + downStops +
                    '}';
        }
    }

    // ---------------- SCHEDULER ----------------
    interface Scheduler {
        Elevator selectElevator(ExternalRequest req, Collection<Elevator> elevators);
    }

    static class NearestCarScheduler implements Scheduler {
        @Override
        public Elevator selectElevator(ExternalRequest req, Collection<Elevator> elevators) {
            Elevator best = null;
            int minDistance = Integer.MAX_VALUE;

            for (Elevator e : elevators) {
                if (e.getStatus() != ElevatorStatus.ACTIVE) continue;
                int distance = Math.abs(e.getCurrentFloor() - req.getFloor());
                if (distance < minDistance) {
                    minDistance = distance;
                    best = e;
                }
            }
            return best;
        }
    }

    // ---------------- CONTROLLER ----------------
    static class ElevatorController {
        private final Map<Integer, Elevator> elevators = new ConcurrentHashMap<>();
        private final Scheduler scheduler;
        private final LinkedBlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();

        ElevatorController(List<Elevator> elevatorList, Scheduler scheduler) {
            for (Elevator e : elevatorList)
                elevators.put(e.getId(), e);
            this.scheduler = scheduler;
        }

        public void submitExternalRequest(int floor, Direction dir) {
            requestQueue.add(new ExternalRequest(floor, dir));
        }

        public void submitInternalRequest(int elevatorId, int floor) {
            Elevator e = elevators.get(elevatorId);
            if (e != null) e.addStop(floor);
        }

        public void step() {
            // Process queued external requests
            while (!requestQueue.isEmpty()) {
                Request r = requestQueue.poll();
                if (r instanceof ExternalRequest req) {
                    Elevator chosen = scheduler.selectElevator(req, elevators.values());
                    if (chosen != null) {
                        chosen.addStop(req.floor);
                        System.out.println("Request at floor " + req.floor +
                                " assigned to elevator " + chosen.getId());
                    }
                }
            }

            // Move all elevators one step
            for (Elevator e : elevators.values()) {
                e.moveOneStep();
            }
        }

        public void printStatus() {
            System.out.println("----- Elevator States -----");
            for (Elevator e : elevators.values()) {
                System.out.println(e);
            }
            System.out.println("---------------------------");
        }
    }

    // ---------------- MAIN TEST ----------------
    public static void main(String[] args) throws InterruptedException {
        Elevator e1 = new Elevator(1, 0);
        Elevator e2 = new Elevator(2, 5);
        ElevatorController controller = new ElevatorController(
                List.of(e1, e2), new NearestCarScheduler());

        controller.submitExternalRequest(3, Direction.UP);
        controller.submitExternalRequest(7, Direction.DOWN);
        controller.submitInternalRequest(1, 9);

        for (int i = 0; i < 15; i++) {
            controller.step();
            controller.printStatus();
            Thread.sleep(500);
        }
    }
}
