package org.lowLevelDesign.LowLevelDesign.RideSharingApp.exception;

public class RiderNotFoundException extends RuntimeException {
    public RiderNotFoundException(final String message) {
        super(message);
    }
}
