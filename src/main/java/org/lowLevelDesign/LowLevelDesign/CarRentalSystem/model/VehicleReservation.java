package org.lowLevelDesign.LowLevelDesign.CarRentalSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleReservation {
    private String reservationId;
    private Account account;
    private Vehicle vehicle;
    private Date startDate;
    private Date endDate;
    private Date creationDate;
    private ReservationStatus status;
    private List<Equipment> equipments;
    private List<Service> services;
    private RentalInsurance insurance;
    private Location pickupLocation;
    private Location dropLocation;
}

