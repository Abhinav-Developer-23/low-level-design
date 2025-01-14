package org.lowLevelDesign.LowLevelDesign.ElevatorSystem.models;


import lombok.Data;
import org.lowLevelDesign.LowLevelDesign.ElevatorSystem.enums.Direction;
import org.lowLevelDesign.LowLevelDesign.ElevatorSystem.enums.DoorState;
import org.lowLevelDesign.LowLevelDesign.ElevatorSystem.enums.ElevatorState;

import java.util.TreeSet;

@Data
public class Elevator {
    private final int id;
    private final int capacity;
    private final TreeSet<Integer> upStops;
    private final TreeSet<Integer> downStops;
    private int currentFloor;
    private Direction direction;
    private DoorState doorState;
    private ElevatorState state;
    private int currentWeight;

    public Elevator(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.currentFloor = 1;
        this.direction = Direction.IDLE;
        this.doorState = DoorState.CLOSED;
        this.state = ElevatorState.STOPPED;
        this.currentWeight = 0;
        this.upStops = new TreeSet<>();
        this.downStops = new TreeSet<>();
    }

    // Getters and setters

    public void addStop(int floor) {
        if (direction == Direction.UP || (direction == Direction.IDLE && floor > currentFloor)) {
            upStops.add(floor);
        } else {
            downStops.add(floor);
        }
    }

    public boolean canAddStop() {
        return currentWeight < capacity;
    }

    public void move() {
        if (direction == Direction.UP && !upStops.isEmpty()) {
            currentFloor = upStops.pollFirst();
        } else if (direction == Direction.DOWN && !downStops.isEmpty()) {
            currentFloor = downStops.pollLast();
        }

        updateDirection();
    }

    private void updateDirection() {
        if (upStops.isEmpty() && downStops.isEmpty()) {
            direction = Direction.IDLE;
        } else if (upStops.isEmpty() && !downStops.isEmpty()) {
            direction = Direction.DOWN;
        } else if (!upStops.isEmpty() && downStops.isEmpty()) {
            direction = Direction.UP;
        }
    }
} 