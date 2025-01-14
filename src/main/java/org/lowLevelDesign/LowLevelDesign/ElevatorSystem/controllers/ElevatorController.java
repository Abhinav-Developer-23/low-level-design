package org.lowLevelDesign.LowLevelDesign.ElevatorSystem.controllers;


import org.lowLevelDesign.LowLevelDesign.ElevatorSystem.enums.Direction;
import org.lowLevelDesign.LowLevelDesign.ElevatorSystem.models.Elevator;

import java.util.ArrayList;
import java.util.List;

public class ElevatorController {
    private final List<Elevator> elevators;
    private final int numberOfFloors;

    public ElevatorController(int numberOfElevators, int numberOfFloors) {
        this.elevators = new ArrayList<>();
        this.numberOfFloors = numberOfFloors;

        for (int i = 0; i < numberOfElevators; i++) {
            elevators.add(new Elevator(i, 680)); // 680kg capacity
        }
    }

    public void requestElevator(int floor, Direction direction) {
        Elevator bestElevator = findBestElevator(floor, direction);
        if (bestElevator != null) {
            bestElevator.addStop(floor);
        }
    }

    public void requestFloor(int elevatorId, int floor) {
        Elevator elevator = getElevator(elevatorId);
        if (elevator != null && elevator.canAddStop()) {
            elevator.addStop(floor);
        }
    }

    private Elevator findBestElevator(int floor, Direction direction) {
        Elevator bestElevator = null;
        int minCost = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            if (!elevator.canAddStop()) continue;

            int cost = calculateCost(elevator, floor, direction);
            if (cost < minCost) {
                minCost = cost;
                bestElevator = elevator;
            }
        }

        return bestElevator;
    }

    private int calculateCost(Elevator elevator, int targetFloor, Direction requestedDirection) {
        int cost = Math.abs(elevator.getCurrentFloor() - targetFloor);

        // Add penalty if elevator is going in opposite direction
        if (elevator.getDirection() != Direction.IDLE &&
                elevator.getDirection() != requestedDirection) {
            cost += 10;
        }

        return cost;
    }

    private Elevator getElevator(int elevatorId) {
        return elevators.stream()
                .filter(e -> e.getId() == elevatorId)
                .findFirst()
                .orElse(null);
    }

    public void step() {
        elevators.forEach(Elevator::move);
    }
} 