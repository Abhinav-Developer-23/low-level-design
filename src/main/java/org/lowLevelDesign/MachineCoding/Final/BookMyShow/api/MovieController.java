package org.lowLevelDesign.MachineCoding.Final.BookMyShow.api;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.services.MovieService;

@AllArgsConstructor
public class MovieController {
    final private MovieService movieService;

    public String createMovie(@NonNull final String movieName) {
        return movieService.createMovie(movieName).getId();
    }

}
