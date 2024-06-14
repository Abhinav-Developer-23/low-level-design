package org.lowLevelDesign.MachineCoding.Final.BookMyShow.services;


import lombok.NonNull;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.exceptions.BadRequestException;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.exceptions.NotFoundException;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.exceptions.SeatPermanentlyUnavailableException;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Booking;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Seat;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Show;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.providers.SeatLockProvider;

import java.util.*;
import java.util.stream.Collectors;

public class BookingService {

    private final Map<String, Booking> showBookings;
    private final SeatLockProvider seatLockProvider;

    public BookingService(SeatLockProvider seatLockProvider) {
        this.seatLockProvider = seatLockProvider;
        this.showBookings = new HashMap<>();
    }

    public Booking getBooking(@NonNull final String bookingId) {
        if (!showBookings.containsKey(bookingId)) {
            throw new NotFoundException();
        }
        return showBookings.get(bookingId);
    }

    public List<Booking> getAllBookings(@NonNull final Show show) {
        List<Booking> response = new ArrayList<>();
        for (Booking booking : showBookings.values()) {
            if (booking.getShow().equals(show)) {
                response.add(booking);
            }
        }

        return response;
    }

    public Booking createBooking(@NonNull final String userId, @NonNull final Show show,
                                 @NonNull final List<Seat> seats) {
        if (isAnySeatAlreadyBooked(show, seats)) {
            throw new SeatPermanentlyUnavailableException();
        }
        seatLockProvider.lockSeats(show, seats, userId);
        final String bookingId = UUID.randomUUID().toString();
        final Booking newBooking = new Booking(bookingId, show, userId, seats);
        showBookings.put(bookingId, newBooking);
        return newBooking;
        // TODO: Create timer for booking expiry
    }

    public List<Seat> getBookedSeats(@NonNull final Show show) {
        return getAllBookings(show).stream()
                .filter(Booking::isConfirmed)
                .map(Booking::getSeatsBooked)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public void confirmBooking(@NonNull final Booking booking, @NonNull final String user) {
        if (!booking.getUser().equals(user)) {
            throw new BadRequestException();
        }

        for (Seat seat : booking.getSeatsBooked()) {
            if (!seatLockProvider.validateLock(booking.getShow(), seat, user)) {
                throw new BadRequestException();
            }
        }
        booking.confirmBooking();
    }

    private boolean isAnySeatAlreadyBooked(final Show show, final List<Seat> seats) {
        final List<Seat> bookedSeats = getBookedSeats(show);
        for (Seat seat : seats) {
            if (bookedSeats.contains(seat)) {
                return true;
            }
        }
        return false;
    }
}
