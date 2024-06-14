package org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Entities;

import lombok.Data;

import java.util.List;

@Data
public class Show {

    private Integer showId;
    private Movie movie;
    private Seat[][] seats;

}
