package org.lowLevelDesign.LowLevelDesign.RideSharingApp.exception;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException(final String message) {
        super(message);
    }
}
