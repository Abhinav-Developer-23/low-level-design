package org.lowLevelDesign.MachineCoding.Final.BookMyShow.providers;



import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Seat;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.model.Show;

import java.util.List;

public interface SeatLockProvider {

    void lockSeats(Show show, List<Seat> seat, String user);
    void unlockSeats(Show show, List<Seat> seat, String user);
    boolean validateLock(Show show, Seat seat, String user);

    List<Seat> getLockedSeats(Show show);
}
