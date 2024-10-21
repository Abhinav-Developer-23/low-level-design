package org.lowLevelDesign.LowLevelDesign.Elevator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class ElevatorSystem {

    Logger log = Logger.getAnonymousLogger();

    private final int maxFloors = 15;
    private int roundRobin=0;

    private List<Elevator> elevators = new ArrayList<>(3);


    private void elevatorRunner(Elevator elevator) throws Exception {
        while (true) {
            if (!elevator.getDeque().isEmpty()) {
                Integer floor = elevator.getDeque().pollFirst();
                log.info("Elevator  "+elevator.getId()+"   is stopped in floor " + floor);
                Thread.sleep(5000);
            }
        }

    }

    public void callElevator(List<Integer> floor) {


        for (Integer integer : floor) {
            if (integer > 15 || integer < 0) {
                System.out.println("Error invalid floor");
                continue;
            }
            int elevatorIndex = roundRobin % 3;
            elevators.get(elevatorIndex).getDeque().addLast(integer);
            roundRobin++;
        }


    }


    private Elevator getNewElevator(int id) {
        Elevator elevator1 = new Elevator();
        elevator1.setId(id);
        elevator1.setDirection(Direction.IDLE);
        elevator1.setDoorOpen(false);


        InternalControlPanel internalControlPanel = new InternalControlPanel();

        elevator1.setInternalControlPanel(internalControlPanel);


        InternalDisplay internalDisplay = new InternalDisplay();
        internalDisplay.setCurrentFloor(1);
        internalDisplay.setCurrentWeight(0);
        internalDisplay.setDirection(Direction.IDLE);
        internalDisplay.setCurrentCapacity(0);

        elevator1.setInternalDisplay(internalDisplay);
        return elevator1;

    }

    public static void main(String[] args) throws Exception {

        try {
            ElevatorSystem elevatorSystem = new ElevatorSystem();

            // Create 3 elevators and add them to the system
            Elevator elevator1 = elevatorSystem.getNewElevator(1);
            Elevator elevator2 = elevatorSystem.getNewElevator(2);
            Elevator elevator3 = elevatorSystem.getNewElevator(3);
            elevatorSystem.elevators.addAll(List.of(elevator1, elevator2, elevator3));

            // Simulate some elevator calls
            List<Integer> stops=List.of(1,4,5,3,3,2,5,13,15,16,14,10,9,65,-5);
            elevatorSystem.callElevator(stops);

            // Run elevator runners in the background
            CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
                try {
                    elevatorSystem.elevatorRunner(elevator1);
                } catch (Exception e) {
                    e.printStackTrace();  // Handle exception in async thread
                }
            });

            CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
                try {
                    elevatorSystem.elevatorRunner(elevator2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> {
                try {
                    elevatorSystem.elevatorRunner(elevator3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Ensure that the main thread waits for the background tasks
            CompletableFuture.allOf(future1, future2, future3).join();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
