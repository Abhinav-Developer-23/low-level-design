package org.lowLevelDesign.MachineCoding.Final.BookMyShow.services;

import lombok.NonNull;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Seat;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Show;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.providers.SeatLockProvider;

import java.util.ArrayList;
import java.util.List;

public class SeatAvailabilityService {
    private final BookingService bookingService;
    private final SeatLockProvider seatLockProvider;

    public SeatAvailabilityService(@NonNull final BookingService bookingService,
                                   @NonNull final SeatLockProvider seatLockProvider) {
        this.bookingService = bookingService;
        this.seatLockProvider = seatLockProvider;
    }

    public List<Seat> getAvailableSeats(@NonNull final Show show) {
        final List<Seat> allSeats = show.getScreen().getSeats();
        final List<Seat> unavailableSeats = getUnavailableSeats(show);

        final List<Seat> availableSeats = new ArrayList<>(allSeats);
        availableSeats.removeAll(unavailableSeats);
        return availableSeats;
    }

    private List<Seat> getUnavailableSeats(@NonNull final Show show) {
        final List<Seat> unavailableSeats = bookingService.getBookedSeats(show);
        unavailableSeats.addAll(seatLockProvider.getLockedSeats(show));
        return unavailableSeats;
    }

}
