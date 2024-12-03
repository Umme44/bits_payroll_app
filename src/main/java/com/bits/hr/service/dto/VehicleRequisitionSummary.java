package com.bits.hr.service.dto;

import java.util.List;

public class VehicleRequisitionSummary {

    private double startTime;

    private double endTime;

    private long approvedPassengers;

    private long approvedRequest;

    private List<VehicleDTO> approvedVehicle;

    private long pendingRequest;

    private long pendingTotalCapacity;

    private long remainingCar;

    private long remainingTotalCapacity;

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public long getApprovedPassengers() {
        return approvedPassengers;
    }

    public void setApprovedPassengers(long approvedPassengers) {
        this.approvedPassengers = approvedPassengers;
    }

    public long getApprovedRequest() {
        return approvedRequest;
    }

    public void setApprovedRequest(long approvedRequest) {
        this.approvedRequest = approvedRequest;
    }

    public List<VehicleDTO> getApprovedVehicle() {
        return approvedVehicle;
    }

    public void setApprovedVehicle(List<VehicleDTO> approvedVehicle) {
        this.approvedVehicle = approvedVehicle;
    }

    public long getPendingRequest() {
        return pendingRequest;
    }

    public void setPendingRequest(long pendingRequest) {
        this.pendingRequest = pendingRequest;
    }

    public long getPendingTotalCapacity() {
        return pendingTotalCapacity;
    }

    public void setPendingTotalCapacity(long pendingTotalCapacity) {
        this.pendingTotalCapacity = pendingTotalCapacity;
    }

    public long getRemainingCar() {
        return remainingCar;
    }

    public void setRemainingCar(long remainingCar) {
        this.remainingCar = remainingCar;
    }

    public long getRemainingTotalCapacity() {
        return remainingTotalCapacity;
    }

    public void setRemainingTotalCapacity(long remainingTotalCapacity) {
        this.remainingTotalCapacity = remainingTotalCapacity;
    }
}
