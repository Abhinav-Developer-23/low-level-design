package org.lowLevelDesign.MachineCoding.Final.BookMyShow.api;


import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Screen;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Theatre;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.services.TheatreService;

@AllArgsConstructor
public class TheatreController {
    final private TheatreService theatreService;

    public String createTheatre(@NonNull final String theatreName) {
        return theatreService.createTheatre(theatreName).getId();
    }

    public String createScreenInTheatre(@NonNull final String screenName, @NonNull final String theatreId) {
        final Theatre theatre = theatreService.getTheatre(theatreId);
        return theatreService.createScreenInTheatre(screenName, theatre).getId();
    }

    public String createSeatInScreen(@NonNull final Integer rowNo, @NonNull final Integer seatNo, @NonNull final String screenId) {
        final Screen screen = theatreService.getScreen(screenId);
        return theatreService.createSeatInScreen(rowNo, seatNo, screen).getId();
    }
}
