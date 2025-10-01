package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Display board that shows real-time parking lot information.
 * Implements Observer Pattern to receive updates when parking lot status changes.
 * Follows Single Responsibility Principle - handles display of parking information.
 */
public class DisplayBoard implements ParkingObserver {
    private final String boardId;
    private final List<String> displayMessages;
    private ParkingLotStatus lastStatus;

    public DisplayBoard(String boardId) {
        this.boardId = boardId;
        this.displayMessages = new ArrayList<>();
        this.lastStatus = null;
    }

    @Override
    public void onParkingLotUpdate(ParkingLotStatus status) {
        this.lastStatus = status;
        updateDisplay();
    }

    /**
     * Updates the display with current parking lot status.
     * Called whenever the parking lot status changes.
     */
    private void updateDisplay() {
        displayMessages.clear();

        if (lastStatus == null) {
            displayMessages.add("Display Board " + boardId + ": No data available");
            return;
        }

        displayMessages.add("=== Display Board " + boardId + " ===");
        displayMessages.add(String.format("Total Spots: %d", lastStatus.getTotalSpots()));
        displayMessages.add(String.format("Available: %d", lastStatus.getAvailableSpots()));
        displayMessages.add(String.format("Occupied: %d", lastStatus.getOccupiedSpots()));
        displayMessages.add(String.format("Occupancy Rate: %.1f%%", lastStatus.getOccupancyRate()));

        if (lastStatus.getOccupancyRate() > 90) {
            displayMessages.add("[WARNING] HIGH OCCUPANCY - Consider alternatives");
        } else if (lastStatus.getOccupancyRate() > 75) {
            displayMessages.add("[INFO] MODERATE OCCUPANCY - Spaces filling up");
        } else if (lastStatus.getAvailableSpots() > 0) {
            displayMessages.add("[OK] SPACES AVAILABLE - Welcome!");
        } else {
            displayMessages.add("[FULL] LOT FULL - Please try again later");
        }

        displayMessages.add("Last updated: " + java.time.LocalTime.now().toString());
        displayMessages.add("================================");
    }

    /**
     * Displays the current information on this board.
     */
    public void display() {
        System.out.println("\n" + String.join("\n", displayMessages));
    }

    /**
     * Gets the current display messages.
     */
    public List<String> getDisplayMessages() {
        return new ArrayList<>(displayMessages);
    }

    /**
     * Gets the ID of this display board.
     */
    public String getBoardId() {
        return boardId;
    }

    /**
     * Manually refreshes the display with current data.
     */
    public void refresh() {
        updateDisplay();
    }

    @Override
    public String toString() {
        return "DisplayBoard [ID: " + boardId + ", Messages: " + displayMessages.size() + "]";
    }
}
