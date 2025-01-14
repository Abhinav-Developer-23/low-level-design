package org.lowLevelDesign.LowLevelDesign.CarRentalSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

// Adding VehicleLogType enum in the same file
enum VehicleLogType {
    MAINTENANCE,
    RENTAL,
    ACCIDENT,
    INSPECTION,
    SERVICE,
    OTHER
}

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleLog {
    private String logId;
    private String vehicleId;
    private String description;
    private Date timestamp;
    private VehicleLogType type;
    private String userId; // ID of the user who created the log
    private double mileageAtLog;
}