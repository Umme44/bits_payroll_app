package com.bits.hr.service.dto;

import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.bits.hr.domain.Department} entity.
 */
public class DepartmentDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 250)
    @ValidateNaturalText
    private String departmentName;

    private Long departmentHeadId;

    private String departmentHeadFullName;

    private String departmentHeadPin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getDepartmentHeadId() {
        return departmentHeadId;
    }

    public void setDepartmentHeadId(Long employeeId) {
        this.departmentHeadId = employeeId;
    }

    public String getDepartmentHeadFullName() {
        return departmentHeadFullName;
    }

    public void setDepartmentHeadFullName(String departmentHeadFullName) {
        this.departmentHeadFullName = departmentHeadFullName;
    }

    public String getDepartmentHeadPin() {
        return departmentHeadPin;
    }

    public void setDepartmentHeadPin(String departmentHeadPin) {
        this.departmentHeadPin = departmentHeadPin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentDTO)) {
            return false;
        }

        return id != null && id.equals(((DepartmentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "DepartmentDTO{" +
            "id=" + id +
            ", departmentName='" + departmentName + '\'' +
            ", departmentHeadId=" + departmentHeadId +
            ", departmentHeadFullName='" + departmentHeadFullName + '\'' +
            ", departmentHeadPin='" + departmentHeadPin + '\'' +
            '}';
    }
}
