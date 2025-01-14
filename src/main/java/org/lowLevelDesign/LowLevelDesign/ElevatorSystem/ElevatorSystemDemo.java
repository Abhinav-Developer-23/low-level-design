package org.lowLevelDesign.LowLevelDesign.ElevatorSystem;


import org.lowLevelDesign.LowLevelDesign.ElevatorSystem.controllers.ElevatorController;
import org.lowLevelDesign.LowLevelDesign.ElevatorSystem.enums.Direction;

public class ElevatorSystemDemo {
    public static void main(String[] args) {
        ElevatorController controller = new ElevatorController(3, 15);

        // Simulate some elevator requests
        controller.requestElevator(5, Direction.UP);
        controller.requestElevator(2, Direction.DOWN);
        controller.requestElevator(8, Direction.UP);

        // Simulate system running for a few steps
        for (int i = 0; i < 10; i++) {
            controller.step();
            System.out.println("Step " + i);
            // Print elevator status
        }
    }
} 