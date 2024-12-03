package com.bits.hr.service.dto;

import java.time.LocalDate;
import java.util.List;

public class VehicleRequisitionSummaryMasterDTO {

    public LocalDate date;

    public List<VehicleRequisitionSummary> vehicleRequisitionSummaryList;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<VehicleRequisitionSummary> getVehicleRequisitionSummaryList() {
        return vehicleRequisitionSummaryList;
    }

    public void setVehicleRequisitionSummaryList(List<VehicleRequisitionSummary> vehicleRequisitionSummaryList) {
        this.vehicleRequisitionSummaryList = vehicleRequisitionSummaryList;
    }
}
