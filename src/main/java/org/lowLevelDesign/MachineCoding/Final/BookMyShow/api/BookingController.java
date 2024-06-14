package org.lowLevelDesign.MachineCoding.Final.BookMyShow.api;


import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Seat;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Show;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.services.BookingService;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.services.ShowService;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.services.TheatreService;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BookingController {
    private final ShowService showService;
    private final BookingService bookingService;
    private final TheatreService theatreService;

    public String createBooking(@NonNull final String userId, @NonNull final String showId,
                                @NonNull final List<String> seatsIds) {
        final Show show = showService.getShow(showId);
        final List<Seat> seats = seatsIds.stream().map(theatreService::getSeat).collect(Collectors.toList());
        return bookingService.createBooking(userId, show, seats).getId();
    }
}
