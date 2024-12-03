package com.bits.hr.service.dto;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.bits.hr.domain.UnitOfMeasurement} entity.
 */
public class UnitOfMeasurementDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String remarks;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitOfMeasurementDTO)) {
            return false;
        }

        return id != null && id.equals(((UnitOfMeasurementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitOfMeasurementDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            "}";
    }
}
