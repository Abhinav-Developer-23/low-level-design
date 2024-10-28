package org.lowLevelDesign.LowLevelDesign.Scratch.CarRentalSystem;

import java.time.LocalDateTime;
import java.util.List;

public class Vehicle {

    private Integer vehicleId;
    private VehicleType vehicleType;
    private String barcode;
    private Integer parkingStallNumber;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private List<String> logs;
    private Boolean isReserved;


    // this is wrong. i should   have put reservation details in reservation class
}
