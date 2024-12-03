package com.bits.hr.service.dto;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.bits.hr.domain.ItemInformation} entity.
 */
public class ItemInformationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String code;

    @NotNull
    @Size(max = 255)
    private String name;

    @Lob
    private String specification;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

    private Long departmentId;

    private String departmentName;

    private Long unitOfMeasurementId;

    private String unitOfMeasurementName;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getUnitOfMeasurementId() {
        return unitOfMeasurementId;
    }

    public void setUnitOfMeasurementId(Long unitOfMeasurementId) {
        this.unitOfMeasurementId = unitOfMeasurementId;
    }

    public String getUnitOfMeasurementName() {
        return unitOfMeasurementName;
    }

    public void setUnitOfMeasurementName(String unitOfMeasurementName) {
        this.unitOfMeasurementName = unitOfMeasurementName;
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
        if (!(o instanceof ItemInformationDTO)) {
            return false;
        }

        return id != null && id.equals(((ItemInformationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemInformationDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", specification='" + getSpecification() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", departmentId=" + getDepartmentId() +
            ", departmentName='" + getDepartmentName() + "'" +
            ", unitOfMeasurementId=" + getUnitOfMeasurementId() +
            ", unitOfMeasurementName='" + getUnitOfMeasurementName() + "'" +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            "}";
    }
}
