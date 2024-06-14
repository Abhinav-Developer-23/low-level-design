package org.lowLevelDesign.MachineCoding.Final.BookMyShow.services;


import lombok.NonNull;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.exceptions.BadRequestException;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Booking;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.providers.SeatLockProvider;

import java.util.HashMap;
import java.util.Map;

public class PaymentsService {

    Map<Booking, Integer> bookingFailures;
    private final Integer allowedRetries;
    private final SeatLockProvider seatLockProvider;

    public PaymentsService(@NonNull final Integer allowedRetries, SeatLockProvider seatLockProvider) {
        this.allowedRetries = allowedRetries;
        this.seatLockProvider = seatLockProvider;
        bookingFailures = new HashMap<>();
    }

    public void processPaymentFailed(@NonNull final Booking booking, @NonNull final String user) {
        if (!booking.getUser().equals(user)) {
            throw new BadRequestException();
        }
        if (!bookingFailures.containsKey(booking)) {
            bookingFailures.put(booking, 0);
        }
        final Integer currentFailuresCount = bookingFailures.get(booking);
        final Integer newFailuresCount = currentFailuresCount + 1;
        bookingFailures.put(booking, newFailuresCount);
        if (newFailuresCount > allowedRetries) {
            seatLockProvider.unlockSeats(booking.getShow(), booking.getSeatsBooked(), booking.getUser());
        }
    }
}
