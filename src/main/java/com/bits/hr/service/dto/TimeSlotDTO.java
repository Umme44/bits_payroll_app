package com.bits.hr.service.dto;

import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.TimeSlot} entity.
 */
public class TimeSlotDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 0, max = 250)
    @ValidateNaturalText
    private String title;

    @NotNull
    private Instant inTime;

    @NotNull
    private Instant outTime;

    private Boolean isApplicableByEmployee;

    private Boolean isDefaultShift;

    private List<String> weekEndList;

    @Size(min = 0, max = 50)
    private String code;

    @Size(min = 0, max = 500)
    @Lob
    private String weekEnds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getInTime() {
        return inTime;
    }

    public void setInTime(Instant inTime) {
        this.inTime = inTime;
    }

    public Instant getOutTime() {
        return outTime;
    }

    public void setOutTime(Instant outTime) {
        this.outTime = outTime;
    }

    public Boolean isIsApplicableByEmployee() {
        return isApplicableByEmployee;
    }

    public void setIsApplicableByEmployee(Boolean isApplicableByEmployee) {
        this.isApplicableByEmployee = isApplicableByEmployee;
    }

    public Boolean isIsDefaultShift() {
        return isDefaultShift;
    }

    public void setIsDefaultShift(Boolean isDefaultShift) {
        this.isDefaultShift = isDefaultShift;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWeekEnds() {
        return weekEnds;
    }

    public void setWeekEnds(String weekEnds) {
        this.weekEnds = weekEnds;
    }

    public List<String> getWeekEndList() {
        return weekEndList;
    }

    public void setWeekEndList(List<String> weekEndList) {
        this.weekEndList = weekEndList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeSlotDTO)) {
            return false;
        }

        return id != null && id.equals(((TimeSlotDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeSlotDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", inTime='" + getInTime() + "'" +
            ", outTime='" + getOutTime() + "'" +
            ", isApplicableByEmployee='" + isIsApplicableByEmployee() + "'" +
            ", isDefaultShift='" + isIsDefaultShift() + "'" +
            ", code='" + getCode() + "'" +
            ", weekEnds='" + getWeekEnds() + "'" +
            "}";
    }
}
