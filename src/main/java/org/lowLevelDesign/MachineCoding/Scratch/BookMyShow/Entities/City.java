package org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Entities;

import lombok.Data;

import java.util.List;

@Data
public class City {

    private String cityName;
    private List<Cinema> cinema;
}
