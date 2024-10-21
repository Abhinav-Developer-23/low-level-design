package org.lowLevelDesign.LowLevelDesign.Elevator;

public class InternalDisplay implements Display{

    private int currentFloor;
    private Direction direction;
    private int currentCapacity;
    private int currentWeight;
    private final int maxWeight=680;
    private final int maxCapacity=8;

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}
