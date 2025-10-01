package org.example;

/**
 * Observer interface for receiving parking lot status updates.
 * Follows Observer Pattern - allows multiple observers to be notified of parking lot changes.
 * Follows Interface Segregation Principle - clients only need to implement what they use.
 */
public interface ParkingObserver {

    /**
     * Called when the parking lot status changes.
     * @param status Current status of the parking lot
     */
    void onParkingLotUpdate(ParkingLotStatus status);
}
