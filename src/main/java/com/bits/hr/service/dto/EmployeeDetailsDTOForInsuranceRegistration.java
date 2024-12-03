package com.bits.hr.service.dto;

import com.bits.hr.domain.Department;
import com.bits.hr.domain.Designation;
import com.bits.hr.domain.Unit;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.domain.enumeration.MaritalStatus;
import java.time.LocalDate;

public class EmployeeDetailsDTOForInsuranceRegistration {

    private String pin;

    private String fullName;

    private LocalDate dateOfBirth;

    private String picture;

    private String nationalIdNo;

    private MaritalStatus maritalStatus;

    private String spouseName;

    private EmployeeCategory employeeCategory;

    private Gender gender;

    private String designation;

    private String department;

    private String unit;

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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getNationalIdNo() {
        return nationalIdNo;
    }

    public void setNationalIdNo(String nationalIdNo) {
        this.nationalIdNo = nationalIdNo;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public EmployeeCategory getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return (
            "EmployeeDetailsDTOForInsuranceRegistration{" +
            "pin='" +
            pin +
            '\'' +
            ", fullName='" +
            fullName +
            '\'' +
            ", dateOfBirth=" +
            dateOfBirth +
            ", picture='" +
            picture +
            '\'' +
            ", nationalIdNo='" +
            nationalIdNo +
            '\'' +
            ", maritalStatus=" +
            maritalStatus +
            ", spouseName='" +
            spouseName +
            '\'' +
            ", employeeCategory=" +
            employeeCategory +
            ", gender=" +
            gender +
            ", designation=" +
            designation +
            ", department=" +
            department +
            ", unit=" +
            unit +
            '}'
        );
    }
}
