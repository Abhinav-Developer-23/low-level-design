package org.lowLevelDesign.MachineCoding.Final.BookMyShow.api;


import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Movie;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Screen;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Seat;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Show;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.services.MovieService;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.services.SeatAvailabilityService;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.services.ShowService;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.services.TheatreService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ShowController {
    private final SeatAvailabilityService seatAvailabilityService;
    private final ShowService showService;
    private final TheatreService theatreService;
    private final MovieService movieService;

    public String createShow(@NonNull final String movieId, @NonNull final String screenId, @NonNull final Date startTime,
                             @NonNull final Integer durationInSeconds) {
        final Screen screen = theatreService.getScreen(screenId);
        final Movie movie = movieService.getMovie(movieId);
        return showService.createShow(movie, screen, startTime, durationInSeconds).getId();
    }

    public List<String> getAvailableSeats(@NonNull final String showId) {
        final Show show = showService.getShow(showId);
        final List<Seat> availableSeats = seatAvailabilityService.getAvailableSeats(show);
        return availableSeats.stream().map(Seat::getId).collect(Collectors.toList());
    }
}
