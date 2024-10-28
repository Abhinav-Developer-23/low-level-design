package org.lowLevelDesign.LowLevelDesign.Scratch.Elevator;

import java.util.ArrayDeque;
import java.util.Deque;

public class Elevator {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private Direction direction;

    private boolean DoorOpen;

    private InternalDisplay internalDisplay;
    private InternalControlPanel internalControlPanel;

    private final Deque<Integer> deque = new ArrayDeque<>();

    public Deque<Integer> getDeque() {
        return deque;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }



    public boolean isDoorOpen() {
        return DoorOpen;
    }

    public void setDoorOpen(boolean doorOpen) {
        DoorOpen = doorOpen;
    }

    public InternalDisplay getInternalDisplay() {
        return internalDisplay;
    }

    public void setInternalDisplay(InternalDisplay internalDisplay) {
        this.internalDisplay = internalDisplay;
    }

    public InternalControlPanel getInternalControlPanel() {
        return internalControlPanel;
    }

    public void setInternalControlPanel(InternalControlPanel internalControlPanel) {
        this.internalControlPanel = internalControlPanel;
    }





}
