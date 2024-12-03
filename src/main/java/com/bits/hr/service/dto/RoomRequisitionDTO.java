package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Lob;
import javax.validation.constraints.*;
import org.hibernate.annotations.Type;

/**
 * A DTO for the {@link com.bits.hr.domain.RoomRequisition} entity.
 */
public class RoomRequisitionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 0, max = 250)
    private String title;

    @NotNull
    private LocalDate bookingStartDate;

    @NotNull
    private LocalDate bookingEndDate;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private Double startTime;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private Double endTime;

    @Size(min = 0, max = 250)
    private String agenda;

    @NotNull
    private Status status;

    @Size(min = 0, max = 250)
    private String bookingTrn;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant sanctionedAt;

    @Size(min = 0, max = 500)
    @Lob
    private String participantList;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String optionalParticipantList;

    @Size(min = 0, max = 250)
    private String rejectedReason;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    private Long sanctionedById;

    private String sanctionedByLogin;

    private Long requesterId;

    private Long roomId;

    private String roomName;

    private String fullName;

    private String pin;

    private String buildingName;
    private String floorName;
    private String typeName;

    private List<Long> employeeParticipantList;

    private List<Long> employeeOptionalParticipantList;

    private Boolean isFullDay;

    private Double calendarBookingStartTime;

    private Double calendarBookingEndTime;

    public List<Long> getEmployeeParticipantList() {
        return employeeParticipantList;
    }

    public void setEmployeeParticipantList(List<Long> employeeParticipantList) {
        this.employeeParticipantList = employeeParticipantList;
    }

    public List<Long> getEmployeeOptionalParticipantList() {
        return employeeOptionalParticipantList;
    }

    public void setEmployeeOptionalParticipantList(List<Long> employeeOptionalParticipantList) {
        this.employeeOptionalParticipantList = employeeOptionalParticipantList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getBookingTrn() {
        return bookingTrn;
    }

    public void setBookingTrn(String bookingTrn) {
        this.bookingTrn = bookingTrn;
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

    public Instant getSanctionedAt() {
        return sanctionedAt;
    }

    public void setSanctionedAt(Instant sanctionedAt) {
        this.sanctionedAt = sanctionedAt;
    }

    public String getParticipantList() {
        return participantList;
    }

    public void setParticipantList(String participantList) {
        this.participantList = participantList;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
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

    public Long getSanctionedById() {
        return sanctionedById;
    }

    public void setSanctionedById(Long userId) {
        this.sanctionedById = userId;
    }

    public String getSanctionedByLogin() {
        return sanctionedByLogin;
    }

    public void setSanctionedByLogin(String userLogin) {
        this.sanctionedByLogin = userLogin;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long employeeId) {
        this.requesterId = employeeId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getBookingStartDate() {
        return bookingStartDate;
    }

    public void setBookingStartDate(LocalDate bookingStartDate) {
        this.bookingStartDate = bookingStartDate;
    }

    public LocalDate getBookingEndDate() {
        return bookingEndDate;
    }

    public void setBookingEndDate(LocalDate bookingEndDate) {
        this.bookingEndDate = bookingEndDate;
    }

    public Double getStartTime() {
        return startTime;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getOptionalParticipantList() {
        return optionalParticipantList;
    }

    public void setOptionalParticipantList(String optionalParticipantList) {
        this.optionalParticipantList = optionalParticipantList;
    }

    public Double getCalendarBookingStartTime() {
        return calendarBookingStartTime;
    }

    public void setCalendarBookingStartTime(Double calendarBookingStartTime) {
        this.calendarBookingStartTime = calendarBookingStartTime;
    }

    public Double getCalendarBookingEndTime() {
        return calendarBookingEndTime;
    }

    public void setCalendarBookingEndTime(Double calendarBookingEndTime) {
        this.calendarBookingEndTime = calendarBookingEndTime;
    }

    public Boolean isIsFullDay() {
        return isFullDay;
    }

    public void setIsFullDay(Boolean isFullDay) {
        this.isFullDay = isFullDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomRequisitionDTO)) {
            return false;
        }

        return id != null && id.equals(((RoomRequisitionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "RoomRequisitionDTO{" +
            "id=" +
            id +
            ", title='" +
            title +
            '\'' +
            ", bookingStartDate=" +
            bookingStartDate +
            ", bookingEndDate=" +
            bookingEndDate +
            ", startTime=" +
            startTime +
            ", endTime=" +
            endTime +
            ", agenda='" +
            agenda +
            '\'' +
            ", status=" +
            status +
            ", bookingTrn='" +
            bookingTrn +
            '\'' +
            ", createdAt=" +
            createdAt +
            ", updatedAt=" +
            updatedAt +
            ", sanctionedAt=" +
            sanctionedAt +
            ", participantList='" +
            participantList +
            '\'' +
            ", optionalParticipantList='" +
            optionalParticipantList +
            '\'' +
            ", rejectedReason='" +
            rejectedReason +
            '\'' +
            ", createdById=" +
            createdById +
            ", createdByLogin='" +
            createdByLogin +
            '\'' +
            ", updatedById=" +
            updatedById +
            ", updatedByLogin='" +
            updatedByLogin +
            '\'' +
            ", sanctionedById=" +
            sanctionedById +
            ", sanctionedByLogin='" +
            sanctionedByLogin +
            '\'' +
            ", requesterId=" +
            requesterId +
            ", roomId=" +
            roomId +
            ", roomName='" +
            roomName +
            '\'' +
            ", fullName='" +
            fullName +
            '\'' +
            ", pin='" +
            pin +
            '\'' +
            ", buildingName='" +
            buildingName +
            '\'' +
            ", floorName='" +
            floorName +
            '\'' +
            ", typeName='" +
            typeName +
            '\'' +
            ", employeeParticipantList=" +
            employeeParticipantList +
            ", employeeOptionalParticipantList=" +
            employeeOptionalParticipantList +
            ", isFullDay=" +
            isFullDay +
            ", calendarBookingStartDate=" +
            calendarBookingStartTime +
            ", calendarBookingEndTime=" +
            calendarBookingEndTime +
            '}'
        );
    }
}
