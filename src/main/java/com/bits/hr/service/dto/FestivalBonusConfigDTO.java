package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.FestivalBonusConfig} entity.
 */
public class FestivalBonusConfigDTO implements Serializable {

    private Long id;

    @NotNull
    private EmployeeCategory employeeCategory;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "5000")
    private Double percentageFromGross;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeCategory getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public Double getPercentageFromGross() {
        return percentageFromGross;
    }

    public void setPercentageFromGross(Double percentageFromGross) {
        this.percentageFromGross = percentageFromGross;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FestivalBonusConfigDTO)) {
            return false;
        }

        return id != null && id.equals(((FestivalBonusConfigDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FestivalBonusConfigDTO{" +
            "id=" + getId() +
            ", employeeCategory='" + getEmployeeCategory() + "'" +
            ", percentageFromGross=" + getPercentageFromGross() +
            "}";
    }
}
