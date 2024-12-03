package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.VehicleRequisition} entity.
 */
public class VehicleRequisitionDTO implements Serializable {

    private Long id;

    @NotNull
    private String purpose;

    private String otherPassengersName;

    @NotNull
    @Min(value = 0L)
    @Max(value = 500L)
    private Long totalNumberOfPassengers;

    @NotNull
    private Status status;

    private String remarks;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant sanctionAt;

    private String transactionNumber;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "24")
    private Double startTime;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "24")
    private Double endTime;

    @NotNull
    private String area;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    private Long approvedById;

    private String approvedByLogin;

    private Long requesterId;

    private Long vehicleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getOtherPassengersName() {
        return otherPassengersName;
    }

    public void setOtherPassengersName(String otherPassengersName) {
        this.otherPassengersName = otherPassengersName;
    }

    public Long getTotalNumberOfPassengers() {
        return totalNumberOfPassengers;
    }

    public void setTotalNumberOfPassengers(Long totalNumberOfPassengers) {
        this.totalNumberOfPassengers = totalNumberOfPassengers;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getSanctionAt() {
        return sanctionAt;
    }

    public void setSanctionAt(Instant sanctionAt) {
        this.sanctionAt = sanctionAt;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getStartTime() {
        return startTime;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String userLogin) {
        this.createdByLogin = userLogin;
    }

    public Long getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Long userId) {
        this.updatedById = userId;
    }

    public String getUpdatedByLogin() {
        return updatedByLogin;
    }

    public void setUpdatedByLogin(String userLogin) {
        this.updatedByLogin = userLogin;
    }

    public Long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(Long userId) {
        this.approvedById = userId;
    }

    public String getApprovedByLogin() {
        return approvedByLogin;
    }

    public void setApprovedByLogin(String userLogin) {
        this.approvedByLogin = userLogin;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long employeeId) {
        this.requesterId = employeeId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleRequisitionDTO)) {
            return false;
        }

        return id != null && id.equals(((VehicleRequisitionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleRequisitionDTO{" +
            "id=" + getId() +
            ", purpose='" + getPurpose() + "'" +
            ", otherPassengersName='" + getOtherPassengersName() + "'" +
            ", totalNumberOfPassengers=" + getTotalNumberOfPassengers() +
            ", status='" + getStatus() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", sanctionAt='" + getSanctionAt() + "'" +
            ", transactionNumber='" + getTransactionNumber() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", startTime=" + getStartTime() +
            ", endTime=" + getEndTime() +
            ", area='" + getArea() + "'" +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            ", approvedById=" + getApprovedById() +
            ", approvedByLogin='" + getApprovedByLogin() + "'" +
            ", requesterId=" + getRequesterId() +
            ", vehicleId=" + getVehicleId() +
            "}";
    }
}
