package org.lowlevel.design.lowLevelDesign.Elevator.model;

import lombok.Data;
import org.lowlevel.design.lowLevelDesign.Elevator.constants.ElevatorMotion;

@Data
public class Elevator {


    ElevatorMotion elevatorMotion;

    Integer currentWeight;

    Integer maxWeight;

    ElevatorDoor elevatorDoor;



}
