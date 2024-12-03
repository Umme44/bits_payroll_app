package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.LeaveType;
import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.bits.hr.domain.LeaveAllocation} entity.
 */
public class LeaveAllocationDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2199)
    private Integer year;

    @NotNull
    private LeaveType leaveType;

    @NotNull
    @Min(value = 0)
    private Integer allocatedDays;

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

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Integer getAllocatedDays() {
        return allocatedDays;
    }

    public void setAllocatedDays(Integer allocatedDays) {
        this.allocatedDays = allocatedDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveAllocationDTO)) {
            return false;
        }

        return id != null && id.equals(((LeaveAllocationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveAllocationDTO{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", leaveType='" + getLeaveType() + "'" +
            ", allocatedDays=" + getAllocatedDays() +
            "}";
    }
}
