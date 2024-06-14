package org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Enum.BookingStatus;

@Data
@Setter
@Getter
public class Seat {


    private BookingStatus bookingStatus;

}
