package org.lowLevelDesign.LowLevelDesign.Scratch.CarRentalSystem;

import java.util.List;

public class Reservation {

    private Integer memberId;

    private Integer vehicleId;
    private Boolean isEnsured;
    private List<String> additionalEquipments;
    private List<String> additionalServices;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Boolean getEnsured() {
        return isEnsured;
    }

    public void setEnsured(Boolean ensured) {
        isEnsured = ensured;
    }

    public List<String> getAdditionalEquipments() {
        return additionalEquipments;
    }

    public void setAdditionalEquipments(List<String> additionalEquipments) {
        this.additionalEquipments = additionalEquipments;
    }

    public List<String> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(List<String> additionalServices) {
        this.additionalServices = additionalServices;
    }
}
