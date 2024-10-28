package org.lowLevelDesign.LowLevelDesign.Scratch.CarRentalSystem;

import java.util.ArrayList;
import java.util.List;

public class ReservationService {


    public Integer findWhichMemberTookVehicle(List<Reservation> reservationList, Integer vehicleId) {
        for (Reservation reservation : reservationList) {

            if (reservation.getVehicleId().equals(vehicleId)) {
                return reservation.getMemberId();

            }
        }
        throw new RuntimeException("Vehicle not found in reservation list");
    }

    public List<Integer> findWhichVehiclesReservedBySpecificMember() {


        return new ArrayList<>();
    }


}
