package com.bits.hr.service.dto;

import com.bits.hr.service.dtoValidationCustom.ValidDate;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.bits.hr.domain.CalenderYear} entity.
 */
@ValidDate(start = "startDate", end = "endDate")
public class CalenderYearDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2100)
    private Integer year;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CalenderYearDTO)) {
            return false;
        }

        return id != null && id.equals(((CalenderYearDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CalenderYearDTO{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
