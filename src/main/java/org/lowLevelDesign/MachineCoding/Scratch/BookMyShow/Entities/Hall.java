package org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Entities;

import lombok.Data;

import java.util.List;

@Data
public class Hall {

    private Integer hallId;
    private List<Show> shows;
}
