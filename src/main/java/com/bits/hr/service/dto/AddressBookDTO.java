package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.BloodGroup;
import com.bits.hr.domain.enumeration.Gender;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for AddressBook
 */

public class AddressBookDTO implements Serializable {

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
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressBookDTO that = (AddressBookDTO) o;
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
