package org.example;

/**
 * Enum representing different vehicle sizes.
 * Defines the hierarchy of vehicle sizes for parking spot compatibility.
 */
public enum VehicleSize {
    MOTORCYCLE(1),
    COMPACT(2),
    REGULAR(3),
    LARGE(4);

    private final int sizeValue;

    VehicleSize(int sizeValue) {
        this.sizeValue = sizeValue;
    }

    public int getSizeValue() {
        return sizeValue;
    }

    /**
     * Checks if this size can accommodate a vehicle of the given size.
     * Larger spots can accommodate smaller vehicles.
     */
    public boolean canAccommodate(VehicleSize vehicleSize) {
        return this.sizeValue >= vehicleSize.sizeValue;
    }
}
