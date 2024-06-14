package org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Entities;

import lombok.Data;

import java.util.List;

@Data
public class Cinema {


    private String name;
    private List<Hall> halls;
}
