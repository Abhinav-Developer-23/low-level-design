package org.lowLevelDesign.LowLevelDesign.CarRentalSystem.service;

import lombok.Data;
import org.lowLevelDesign.LowLevelDesign.CarRentalSystem.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CarRentalSystem {
    private String name;
    private List<Location> locations;
    private List<Account> accounts;
    private List<Vehicle> vehicles;
    private List<VehicleReservation> reservations;

    public boolean addNewLocation(Location location) {
        if (locations == null) {
            locations = new ArrayList<>();
        }
        return locations.add(location);
    }

    public boolean addNewVehicle(Vehicle vehicle) {
        if (vehicles == null) {
            vehicles = new ArrayList<>();
        }
        return vehicles.add(vehicle);
    }

    public boolean addNewAccount(Account account) {
        if (accounts == null) {
            accounts = new ArrayList<>();
        }
        return accounts.add(account);
    }

    public List<Vehicle> searchVehicles(VehicleType type, Date startDate, Date endDate) {
        // Implementation for searching available vehicles
        return new ArrayList<>();
    }

    public VehicleReservation makeReservation(Account account, Vehicle vehicle) {
        VehicleReservation reservation = new VehicleReservation();
        reservation.setAccount(account);
        reservation.setVehicle(vehicle);
        reservation.setStatus(ReservationStatus.SCHEDULED);

        if (reservations == null) {
            reservations = new ArrayList<>();
        }
        reservations.add(reservation);
        return reservation;
    }
} 