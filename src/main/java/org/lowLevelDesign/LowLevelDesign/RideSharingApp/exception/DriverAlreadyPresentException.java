package org.lowLevelDesign.LowLevelDesign.RideSharingApp.exception;

public class DriverAlreadyPresentException extends RuntimeException {
    public DriverAlreadyPresentException(final String message) {
        super(message);
    }
}
