package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.MaritalStatus;
import com.bits.hr.util.annotation.ValidateNaturalText;
import com.bits.hr.util.annotation.ValidatePhoneNumber;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class UserEditAccountDTO implements Serializable {

    private Long id;

    private String fullName;

    private MaritalStatus maritalStatus;

    private LocalDate dateOfMarriage;

    @ValidateNaturalText
    private String spouseName;

    @ValidateNaturalText
    private String presentAddress;

    @ValidateNaturalText
    private String permanentAddress;

    @ValidatePhoneNumber
    private String personalContactNo;

    @ValidateNaturalText
    private String whatsappId;

    @Email
    private String personalEmail;

    @ValidateNaturalText
    private String skypeId;

    @ValidateNaturalText
    private String emergencyContactPersonName;

    private String emergencyContactPersonRelationshipWithEmployee;

    @ValidatePhoneNumber
    private String emergencyContactPersonContactNumber;

    private String PIN;
    private String designationName;
    private String departmentName;
    private String unitName;
    private String bandName;
    private String reportingToName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public UserEditAccountDTO fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public UserEditAccountDTO maritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
        return this;
    }

    public LocalDate getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(LocalDate dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public UserEditAccountDTO dateOfMarriage(LocalDate dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
        return this;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public UserEditAccountDTO spouseName(String spouseName) {
        this.spouseName = spouseName;
        return this;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public UserEditAccountDTO presentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
        return this;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public UserEditAccountDTO permanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
        return this;
    }

    public String getPersonalContactNo() {
        return personalContactNo;
    }

    public void setPersonalContactNo(String personalContactNo) {
        this.personalContactNo = personalContactNo;
    }

    public UserEditAccountDTO personalContactNo(String personalContactNo) {
        this.personalContactNo = personalContactNo;
        return this;
    }

    public String getWhatsappId() {
        return whatsappId;
    }

    public void setWhatsappId(String whatsappId) {
        this.whatsappId = whatsappId;
    }

    public UserEditAccountDTO whatsappId(String whatsappId) {
        this.whatsappId = whatsappId;
        return this;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public UserEditAccountDTO personalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
        return this;
    }

    public String getSkypeId() {
        return skypeId;
    }

    public void setSkypeId(String skypeId) {
        this.skypeId = skypeId;
    }

    public UserEditAccountDTO skypeId(String skypeId) {
        this.skypeId = skypeId;
        return this;
    }

    public String getEmergencyContactPersonName() {
        return emergencyContactPersonName;
    }

    public void setEmergencyContactPersonName(String emergencyContactPersonName) {
        this.emergencyContactPersonName = emergencyContactPersonName;
    }

    public UserEditAccountDTO emergencyContactPersonName(String emergencyContactPersonName) {
        this.emergencyContactPersonName = emergencyContactPersonName;
        return this;
    }

    public String getEmergencyContactPersonRelationshipWithEmployee() {
        return emergencyContactPersonRelationshipWithEmployee;
    }

    public void setEmergencyContactPersonRelationshipWithEmployee(String emergencyContactPersonRelationshipWithEmployee) {
        this.emergencyContactPersonRelationshipWithEmployee = emergencyContactPersonRelationshipWithEmployee;
    }

    public UserEditAccountDTO emergencyContactPersonRelationshipWithEmployee(String emergencyContactPersonRelationshipWithEmployee) {
        this.emergencyContactPersonRelationshipWithEmployee = emergencyContactPersonRelationshipWithEmployee;
        return this;
    }

    public String getEmergencyContactPersonContactNumber() {
        return emergencyContactPersonContactNumber;
    }

    public void setEmergencyContactPersonContactNumber(String emergencyContactPersonContactNumber) {
        this.emergencyContactPersonContactNumber = emergencyContactPersonContactNumber;
    }

    public UserEditAccountDTO emergencyContactPersonContactNumber(String emergencyContactPersonContactNumber) {
        this.emergencyContactPersonContactNumber = emergencyContactPersonContactNumber;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
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

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public String getReportingToName() {
        return reportingToName;
    }

    public void setReportingToName(String reportingToName) {
        this.reportingToName = reportingToName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEditAccountDTO that = (UserEditAccountDTO) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            maritalStatus == that.maritalStatus &&
            Objects.equals(dateOfMarriage, that.dateOfMarriage) &&
            Objects.equals(spouseName, that.spouseName) &&
            Objects.equals(presentAddress, that.presentAddress) &&
            Objects.equals(permanentAddress, that.permanentAddress) &&
            Objects.equals(personalContactNo, that.personalContactNo) &&
            Objects.equals(whatsappId, that.whatsappId) &&
            Objects.equals(personalEmail, that.personalEmail) &&
            Objects.equals(skypeId, that.skypeId) &&
            Objects.equals(emergencyContactPersonName, that.emergencyContactPersonName) &&
            Objects.equals(emergencyContactPersonRelationshipWithEmployee, that.emergencyContactPersonRelationshipWithEmployee) &&
            Objects.equals(emergencyContactPersonContactNumber, that.emergencyContactPersonContactNumber)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fullName,
            maritalStatus,
            dateOfMarriage,
            spouseName,
            presentAddress,
            permanentAddress,
            personalContactNo,
            whatsappId,
            personalEmail,
            skypeId,
            emergencyContactPersonName,
            emergencyContactPersonRelationshipWithEmployee,
            emergencyContactPersonContactNumber
        );
    }

    @Override
    public String toString() {
        return (
            "UserEditAccountDTO{" +
            "id=" +
            id +
            ", fullName='" +
            fullName +
            '\'' +
            ", maritalStatus=" +
            maritalStatus +
            ", dateOfMarriage=" +
            dateOfMarriage +
            ", spouseName='" +
            spouseName +
            '\'' +
            ", presentAddress='" +
            presentAddress +
            '\'' +
            ", permanentAddress='" +
            permanentAddress +
            '\'' +
            ", personalContactNo='" +
            personalContactNo +
            '\'' +
            ", whatsappId='" +
            whatsappId +
            '\'' +
            ", personalEmail='" +
            personalEmail +
            '\'' +
            ", skypeId='" +
            skypeId +
            '\'' +
            ", emergencyContactPersonName='" +
            emergencyContactPersonName +
            '\'' +
            ", emergencyContactPersonRelationshipWithEmployee='" +
            emergencyContactPersonRelationshipWithEmployee +
            '\'' +
            ", emergencyContactPersonContactNumber='" +
            emergencyContactPersonContactNumber +
            '\'' +
            '}'
        );
    }
}
