package com.bits.hr.service.dto;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.BloodGroup;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.domain.enumeration.MaritalStatus;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.bits.hr.domain.Employee} entity.
 */

public class EmployeeCommonDTO implements Serializable {

    private Long id;

    private String referenceId;

    @NotNull
    private String pin;

    private String picture;

    private String fullName;

    private String surName;

    private BloodGroup bloodGroup;

    private String presentAddress;

    private String permanentAddress;

    private String personalContactNo;

    private String personalEmail;

    private MaritalStatus maritalStatus;

    private String officialEmail;

    private String officialContactNo;

    private String officePhoneExtension;

    private String whatsappId;

    private String skypeId;

    private String emergencyContactPersonName;

    private String emergencyContactPersonRelationshipWithEmployee;

    private String emergencyContactPersonContactNumber;

    private EmployeeCategory employeeCategory;

    private String location;

    private Gender gender;

    private Long designationId;

    private Long departmentId;

    private Long nationalityId;

    private String nationalityNationalityName;

    private Long bankBranchId;

    private Long bandId;

    private Long unitId;
    private String designationName;
    private String departmentName;
    private String reportingToName;
    private String bankBranchName;
    private String bandName;
    private String unitName;

    private Boolean isAllowedToGiveOnlineAttendance;

    private LocalDate probationPeriodEndDate;

    private LocalDate contractPeriodEndDate;

    private LocalDate dateOfJoining;

    private Instant currentInTime;

    private Instant currentOutTime;

    private boolean isCurrentlyResigned;

    public boolean isCurrentlyResigned() {
        return isCurrentlyResigned;
    }

    public void setCurrentlyResigned(boolean currentlyResigned) {
        isCurrentlyResigned = currentlyResigned;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public LocalDate getProbationPeriodEndDate() {
        return probationPeriodEndDate;
    }

    public void setProbationPeriodEndDate(LocalDate probationPeriodEndDate) {
        this.probationPeriodEndDate = probationPeriodEndDate;
    }

    public LocalDate getContractPeriodEndDate() {
        return contractPeriodEndDate;
    }

    public void setContractPeriodEndDate(LocalDate contractPeriodEndDate) {
        this.contractPeriodEndDate = contractPeriodEndDate;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public Instant getCurrentInTime() {
        return currentInTime;
    }

    public void setCurrentInTime(Instant currentInTime) {
        this.currentInTime = currentInTime;
    }

    public Instant getCurrentOutTime() {
        return currentOutTime;
    }

    public void setCurrentOutTime(Instant currentOutTime) {
        this.currentOutTime = currentOutTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDTO)) {
            return false;
        }

        return id != null && id.equals(((EmployeeDTO) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPersonalContactNo() {
        return personalContactNo;
    }

    public void setPersonalContactNo(String personalContactNo) {
        this.personalContactNo = personalContactNo;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getOfficialEmail() {
        return officialEmail;
    }

    public void setOfficialEmail(String officialEmail) {
        this.officialEmail = officialEmail;
    }

    public String getOfficialContactNo() {
        return officialContactNo;
    }

    public void setOfficialContactNo(String officialContactNo) {
        this.officialContactNo = officialContactNo;
    }

    public String getOfficePhoneExtension() {
        return officePhoneExtension;
    }

    public void setOfficePhoneExtension(String officePhoneExtension) {
        this.officePhoneExtension = officePhoneExtension;
    }

    public String getWhatsappId() {
        return whatsappId;
    }

    public void setWhatsappId(String whatsappId) {
        this.whatsappId = whatsappId;
    }

    public String getSkypeId() {
        return skypeId;
    }

    public void setSkypeId(String skypeId) {
        this.skypeId = skypeId;
    }

    public String getEmergencyContactPersonName() {
        return emergencyContactPersonName;
    }

    public void setEmergencyContactPersonName(String emergencyContactPersonName) {
        this.emergencyContactPersonName = emergencyContactPersonName;
    }

    public String getEmergencyContactPersonRelationshipWithEmployee() {
        return emergencyContactPersonRelationshipWithEmployee;
    }

    public void setEmergencyContactPersonRelationshipWithEmployee(String emergencyContactPersonRelationshipWithEmployee) {
        this.emergencyContactPersonRelationshipWithEmployee = emergencyContactPersonRelationshipWithEmployee;
    }

    public String getEmergencyContactPersonContactNumber() {
        return emergencyContactPersonContactNumber;
    }

    public void setEmergencyContactPersonContactNumber(String emergencyContactPersonContactNumber) {
        this.emergencyContactPersonContactNumber = emergencyContactPersonContactNumber;
    }

    //    public Double getMainGrossSalary() {
    //        return mainGrossSalary;
    //    }

    //    public void setMainGrossSalary(Double mainGrossSalary) {
    //        this.mainGrossSalary = mainGrossSalary;
    //    }

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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(Long nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getNationalityNationalityName() {
        return nationalityNationalityName;
    }

    public void setNationalityNationalityName(String nationalityNationalityName) {
        this.nationalityNationalityName = nationalityNationalityName;
    }

    public Long getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(Long bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public Long getBandId() {
        return bandId;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
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

    public String getReportingToName() {
        return reportingToName;
    }

    public void setReportingToName(String reportingToName) {
        this.reportingToName = reportingToName;
    }

    public Boolean isIsAllowedToGiveOnlineAttendance() {
        return isAllowedToGiveOnlineAttendance;
    }

    public void setIsAllowedToGiveOnlineAttendance(Boolean isAllowedToGiveOnlineAttendance) {
        this.isAllowedToGiveOnlineAttendance = isAllowedToGiveOnlineAttendance;
    }
}
