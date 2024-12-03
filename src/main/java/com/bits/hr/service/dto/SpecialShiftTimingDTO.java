package com.bits.hr.service.dto;

import com.bits.hr.domain.SpecialShiftTiming;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link SpecialShiftTiming} entity.
 */
public class SpecialShiftTimingDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Boolean overrideRoaster;

    @NotNull
    private Boolean overrideFlexSchedule;

    @ValidateNaturalText
    private String remarks;

    private Instant createdAt;

    private Instant updatedAt;

    private Long timeSlotId;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    private Instant inTime;

    private Instant outTime;

    @Size(max = 250)
    @ValidateNaturalText
    private String reason;

    private String timeSlotTitle;

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

    public Boolean isOverrideRoaster() {
        return overrideRoaster;
    }

    public void setOverrideRoaster(Boolean overrideRoaster) {
        this.overrideRoaster = overrideRoaster;
    }

    public Boolean isOverrideFlexSchedule() {
        return overrideFlexSchedule;
    }

    public void setOverrideFlexSchedule(Boolean overrideFlexSchedule) {
        this.overrideFlexSchedule = overrideFlexSchedule;
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

    public Long getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Long timeSlotId) {
        this.timeSlotId = timeSlotId;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTimeSlotTitle() {
        return timeSlotTitle;
    }

    public void setTimeSlotTitle(String timeSlotTitle) {
        this.timeSlotTitle = timeSlotTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecialShiftTimingDTO)) {
            return false;
        }

        return id != null && id.equals(((SpecialShiftTimingDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "SpecialShiftTimingDTO{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", overrideRoaster=" + overrideRoaster +
                ", overrideFlexSchedule=" + overrideFlexSchedule +
                ", remarks='" + remarks + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", timeSlotId=" + timeSlotId +
                ", createdById=" + createdById +
                ", createdByLogin='" + createdByLogin + '\'' +
                ", updatedById=" + updatedById +
                ", updatedByLogin='" + updatedByLogin + '\'' +
                ", inTime=" + inTime +
                ", outTime=" + outTime +
                ", reason='" + reason + '\'' +
                ", timeSlotTitle='" + timeSlotTitle + '\'' +
                '}';
    }
}
