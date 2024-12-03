package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EventType;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.bits.hr.domain.EmploymentHistory} entity.
 */
public class EmploymentHistoryDTO implements Serializable {

    private Long id;

    private String referenceId;

    private String pin;

    private EventType eventType;

    private LocalDate effectiveDate;

    private Double previousMainGrossSalary;

    private Double currentMainGrossSalary;

    private String previousWorkingHour;

    private String changedWorkingHour;

    private Boolean isModifiable;

    private Long previousDesignationId;

    private Long changedDesignationId;

    private Long previousDepartmentId;

    private Long changedDepartmentId;

    private Long previousReportingToId;

    private Long changedReportingToId;

    private Long employeeId;

    private String fullName;

    private EmployeeCategory employeeCategory;

    private String location;

    private LocalDate dateOfJoining;

    private LocalDate dateOfConfirmation;

    private String currentDesignationName;
    private String currentDepartmentName;
    private String currentBandName;
    private String currentUnitName;
    private String currentReportingToName;
    private String currentReportingToPIN;

    private Long previousUnitId;

    private Long changedUnitId;

    private Long previousBandId;

    private Long changedBandId;

    private String previousDesignationName;
    private String changedDesignationName;
    private String previousDepartmentName;
    private String changedDepartmentName;
    private String previousReportingToName;
    private String changedReportingToName;
    private String employeeName;
    private String previousUnitName;
    private String changedUnitName;
    private String previousBandName;
    private String changedBandName;

    public String getPreviousDesignationName() {
        return previousDesignationName;
    }

    public void setPreviousDesignationName(String previousDesignationName) {
        this.previousDesignationName = previousDesignationName;
    }

    public String getChangedDesignationName() {
        return changedDesignationName;
    }

    public void setChangedDesignationName(String changedDesignationName) {
        this.changedDesignationName = changedDesignationName;
    }

    public String getPreviousDepartmentName() {
        return previousDepartmentName;
    }

    public void setPreviousDepartmentName(String previousDepartmentName) {
        this.previousDepartmentName = previousDepartmentName;
    }

    public String getChangedDepartmentName() {
        return changedDepartmentName;
    }

    public void setChangedDepartmentName(String changedDepartmentName) {
        this.changedDepartmentName = changedDepartmentName;
    }

    public String getPreviousReportingToName() {
        return previousReportingToName;
    }

    public void setPreviousReportingToName(String previousReportingToName) {
        this.previousReportingToName = previousReportingToName;
    }

    public String getChangedReportingToName() {
        return changedReportingToName;
    }

    public void setChangedReportingToName(String changedReportingToName) {
        this.changedReportingToName = changedReportingToName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPreviousUnitName() {
        return previousUnitName;
    }

    public void setPreviousUnitName(String previousUnitName) {
        this.previousUnitName = previousUnitName;
    }

    public String getChangedUnitName() {
        return changedUnitName;
    }

    public void setChangedUnitName(String changedUnitName) {
        this.changedUnitName = changedUnitName;
    }

    public String getPreviousBandName() {
        return previousBandName;
    }

    public void setPreviousBandName(String previousBandName) {
        this.previousBandName = previousBandName;
    }

    public String getChangedBandName() {
        return changedBandName;
    }

    public void setChangedBandName(String changedBandName) {
        this.changedBandName = changedBandName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Double getPreviousMainGrossSalary() {
        return previousMainGrossSalary;
    }

    public void setPreviousMainGrossSalary(Double previousMainGrossSalary) {
        this.previousMainGrossSalary = previousMainGrossSalary;
    }

    public Double getCurrentMainGrossSalary() {
        return currentMainGrossSalary;
    }

    public void setCurrentMainGrossSalary(Double currentMainGrossSalary) {
        this.currentMainGrossSalary = currentMainGrossSalary;
    }

    public String getPreviousWorkingHour() {
        return previousWorkingHour;
    }

    public void setPreviousWorkingHour(String previousWorkingHour) {
        this.previousWorkingHour = previousWorkingHour;
    }

    public String getChangedWorkingHour() {
        return changedWorkingHour;
    }

    public void setChangedWorkingHour(String changedWorkingHour) {
        this.changedWorkingHour = changedWorkingHour;
    }

    public Boolean isIsModifiable() {
        return isModifiable;
    }

    public void setIsModifiable(Boolean isModifiable) {
        this.isModifiable = isModifiable;
    }

    public Long getPreviousDesignationId() {
        return previousDesignationId;
    }

    public void setPreviousDesignationId(Long designationId) {
        this.previousDesignationId = designationId;
    }

    public Long getChangedDesignationId() {
        return changedDesignationId;
    }

    public void setChangedDesignationId(Long designationId) {
        this.changedDesignationId = designationId;
    }

    public Long getPreviousDepartmentId() {
        return previousDepartmentId;
    }

    public void setPreviousDepartmentId(Long departmentId) {
        this.previousDepartmentId = departmentId;
    }

    public Long getChangedDepartmentId() {
        return changedDepartmentId;
    }

    public void setChangedDepartmentId(Long departmentId) {
        this.changedDepartmentId = departmentId;
    }

    public Long getPreviousReportingToId() {
        return previousReportingToId;
    }

    public void setPreviousReportingToId(Long employeeId) {
        this.previousReportingToId = employeeId;
    }

    public Long getChangedReportingToId() {
        return changedReportingToId;
    }

    public void setChangedReportingToId(Long employeeId) {
        this.changedReportingToId = employeeId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public EmployeeCategory getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public LocalDate getDateOfConfirmation() {
        return dateOfConfirmation;
    }

    public void setDateOfConfirmation(LocalDate dateOfConfirmation) {
        this.dateOfConfirmation = dateOfConfirmation;
    }

    public String getCurrentDesignationName() {
        return currentDesignationName;
    }

    public void setCurrentDesignationName(String currentDesignationName) {
        this.currentDesignationName = currentDesignationName;
    }

    public String getCurrentDepartmentName() {
        return currentDepartmentName;
    }

    public void setCurrentDepartmentName(String currentDepartmentName) {
        this.currentDepartmentName = currentDepartmentName;
    }

    public String getCurrentBandName() {
        return currentBandName;
    }

    public void setCurrentBandName(String currentBandName) {
        this.currentBandName = currentBandName;
    }

    public String getCurrentUnitName() {
        return currentUnitName;
    }

    public void setCurrentUnitName(String currentUnitName) {
        this.currentUnitName = currentUnitName;
    }

    public String getCurrentReportingToName() {
        return currentReportingToName;
    }

    public void setCurrentReportingToName(String currentReportingToName) {
        this.currentReportingToName = currentReportingToName;
    }

    public String getCurrentReportingToPIN() {
        return currentReportingToPIN;
    }

    public void setCurrentReportingToPIN(String currentReportingToPIN) {
        this.currentReportingToPIN = currentReportingToPIN;
    }

    public Long getPreviousUnitId() {
        return previousUnitId;
    }

    public void setPreviousUnitId(Long unitId) {
        this.previousUnitId = unitId;
    }

    public Long getChangedUnitId() {
        return changedUnitId;
    }

    public void setChangedUnitId(Long unitId) {
        this.changedUnitId = unitId;
    }

    public Long getPreviousBandId() {
        return previousBandId;
    }

    public void setPreviousBandId(Long bandId) {
        this.previousBandId = bandId;
    }

    public Long getChangedBandId() {
        return changedBandId;
    }

    public void setChangedBandId(Long bandId) {
        this.changedBandId = bandId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmploymentHistoryDTO)) {
            return false;
        }

        return id != null && id.equals(((EmploymentHistoryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmploymentHistoryDTO{" +
            "id=" + getId() +
            ", referenceId='" + getReferenceId() + "'" +
            ", pin='" + getPin() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", previousMainGrossSalary=" + getPreviousMainGrossSalary() +
            ", currentMainGrossSalary=" + getCurrentMainGrossSalary() +
            ", previousWorkingHour='" + getPreviousWorkingHour() + "'" +
            ", changedWorkingHour='" + getChangedWorkingHour() + "'" +
            ", isModifiable='" + isIsModifiable() + "'" +
            ", previousDesignationId=" + getPreviousDesignationId() +
            ", changedDesignationId=" + getChangedDesignationId() +
            ", previousDepartmentId=" + getPreviousDepartmentId() +
            ", changedDepartmentId=" + getChangedDepartmentId() +
            ", previousReportingToId=" + getPreviousReportingToId() +
            ", changedReportingToId=" + getChangedReportingToId() +
            ", employeeId=" + getEmployeeId() +
            ", previousUnitId=" + getPreviousUnitId() +
            ", changedUnitId=" + getChangedUnitId() +
            ", previousBandId=" + getPreviousBandId() +
            ", changedBandId=" + getChangedBandId() +
            "}";
    }
}
