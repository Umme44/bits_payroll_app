package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.HolidayType;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.bits.hr.domain.Holidays} entity.
 */
public class HolidaysDTO implements Serializable {

    private Long id;

    private HolidayType holidayType;

    @ValidateNaturalText
    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isMoonDependent;

    private String allDayNames;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HolidayType getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(HolidayType holidayType) {
        this.holidayType = holidayType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean isIsMoonDependent() {
        return isMoonDependent;
    }

    public void setIsMoonDependent(Boolean isMoonDependent) {
        this.isMoonDependent = isMoonDependent;
    }

    public String getAllDayNames() {
        return allDayNames;
    }

    public void setAllDayNames(String allDayNames) {
        this.allDayNames = allDayNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HolidaysDTO)) {
            return false;
        }

        return id != null && id.equals(((HolidaysDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HolidaysDTO{" +
            "id=" + getId() +
            ", holidayType='" + getHolidayType() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isMoonDependent='" + isIsMoonDependent() + "'" +
            "}";
    }
}
