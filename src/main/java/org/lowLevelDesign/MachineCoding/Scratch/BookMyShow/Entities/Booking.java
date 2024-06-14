package org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Entities;

import lombok.Data;

@Data
public class Booking {

    private String city;
    private String movieName;
    private Integer row;
    private Integer column;
    private String hallId;
    private String cinema;
    private String showId;

}
