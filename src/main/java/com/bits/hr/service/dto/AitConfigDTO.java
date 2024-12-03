package com.bits.hr.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.bits.hr.domain.AitConfig} entity.
 */
public class AitConfigDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @Lob
    private String taxConfig;

    private int startYear;

    private int endYear;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTaxConfig() {
        return taxConfig;
    }

    public void setTaxConfig(String taxConfig) {
        this.taxConfig = taxConfig;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AitConfigDTO)) {
            return false;
        }

        return id != null && id.equals(((AitConfigDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AitConfigDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", taxConfig='" + getTaxConfig() + "'" +
            "}";
    }
}
