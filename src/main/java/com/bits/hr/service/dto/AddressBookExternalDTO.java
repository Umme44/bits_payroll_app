package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.BloodGroup;
import com.bits.hr.domain.enumeration.Gender;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

/**
 * A DTO for AddressBook
 */

public class AddressBookExternalDTO implements Serializable {

    private Long id;

    private String pin;

    private String fullName;

    private String designationName;

    private String departmentName;

    private String unitName;

    private BloodGroup bloodGroup;

    private String officialContactNo;

    private String officialEmail;

    private Gender gender;

    private String reportingToName;

    private String reportingToPin;

    private Map<String, String> officeLocations;

    private boolean isBillableResource;
    private boolean isAugmentedResource;
    private LocalDate lastWorkingDay;
    private LocalDate dateOfJoining;
    private String referenceId;

    public Map<String, String> getOfficeLocations() {
        return officeLocations;
    }

    private LocalDate updatedAt;
    private String employmentStatus;

    public void setOfficeLocations(Map<String, String> officeLocations) {
        this.officeLocations = officeLocations;
    }

    public String getReportingToName() {
        return reportingToName;
    }

    public void setReportingToName(String reportingToName) {
        this.reportingToName = reportingToName;
    }

    public String getReportingToPin() {
        return reportingToPin;
    }

    public void setReportingToPin(String reportingToPin) {
        this.reportingToPin = reportingToPin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getOfficialContactNo() {
        return officialContactNo;
    }

    public void setOfficialContactNo(String officialContactNo) {
        this.officialContactNo = officialContactNo;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getOfficialEmail() {
        return officialEmail;
    }

    public void setOfficialEmail(String officialEmail) {
        this.officialEmail = officialEmail;
    }

    public boolean getIsBillableResource() {
        return isBillableResource;
    }

    public void setIsBillableResource(boolean billableResource) {
        isBillableResource = billableResource;
    }

    public boolean getIsAugmentedResource() {
        return isAugmentedResource;
    }

    public void setIsAugmentedResource(boolean augmentedResource) {
        isAugmentedResource = augmentedResource;
    }

    public LocalDate getLastWorkingDay() {
        return lastWorkingDay;
    }

    public void setLastWorkingDay(LocalDate lastWorkingDay) {
        this.lastWorkingDay = lastWorkingDay;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    @Override
    public String toString() {
        return (
            "BloodGroupInfoDTO{" +
            "pin='" +
            pin +
            '\'' +
            ", fullName='" +
            fullName +
            '\'' +
            ", designationName='" +
            designationName +
            '\'' +
            ", departmentName='" +
            departmentName +
            '\'' +
            ", unitName='" +
            unitName +
            '\'' +
            ", bloodGroup=" +
            bloodGroup +
            ", officialContactNo='" +
            officialContactNo +
            '\'' +
            ", officialEmail='" +
            officialEmail +
            '\'' +
            ", gender=" +
            gender +
            '\'' +
            ", isBillableResource=" +
            isBillableResource +
            '\'' +
            ", isAugmentedResource=" +
            isAugmentedResource +
            '\'' +
            ", lastWorkingDay=" +
            lastWorkingDay +
            '\'' +
            ", dateOfJoining=" +
            dateOfJoining +
            '\'' +
            ", referenceId=" +
            referenceId +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressBookExternalDTO that = (AddressBookExternalDTO) o;
        return (
            Objects.equals(pin, that.pin) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(designationName, that.designationName) &&
            Objects.equals(departmentName, that.departmentName) &&
            Objects.equals(unitName, that.unitName) &&
            bloodGroup == that.bloodGroup &&
            Objects.equals(officialContactNo, that.officialContactNo) &&
            Objects.equals(officialEmail, that.officialEmail) &&
            gender == that.gender
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(pin, fullName, designationName, departmentName, unitName, bloodGroup, officialContactNo, officialEmail, gender);
    }
}
