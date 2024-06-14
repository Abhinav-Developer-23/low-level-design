package org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Entities;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Movie {

    private String movieName;
    private String language;
    private LocalDateTime releaseDate;
    private List<City> cities;
}
