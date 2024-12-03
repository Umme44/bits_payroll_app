package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.Vehicle} entity.
 */
public class VehicleDTO implements Serializable {

    private Long id;

    @NotNull
    private String modelName;

    @NotNull
    private String chassisNumber;

    @NotNull
    private String registrationNumber;

    @NotNull
    private Status status;

    @NotNull
    private Integer capacity;

    private String remarks;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant approvedAt;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    private Long approvedById;

    private String approvedByLogin;

    private Long assignedDriverId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
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

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
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

    public Long getAssignedDriverId() {
        return assignedDriverId;
    }

    public void setAssignedDriverId(Long employeeId) {
        this.assignedDriverId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleDTO)) {
            return false;
        }

        return id != null && id.equals(((VehicleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleDTO{" +
            "id=" + getId() +
            ", modelName='" + getModelName() + "'" +
            ", chassisNumber='" + getChassisNumber() + "'" +
            ", registrationNumber='" + getRegistrationNumber() + "'" +
            ", status='" + getStatus() + "'" +
            ", capacity=" + getCapacity() +
            ", remarks='" + getRemarks() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", approvedAt='" + getApprovedAt() + "'" +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            ", approvedById=" + getApprovedById() +
            ", approvedByLogin='" + getApprovedByLogin() + "'" +
            ", assignedDriverId=" + getAssignedDriverId() +
            "}";
    }
}
