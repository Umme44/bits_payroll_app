package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.domain.enumeration.Gender;
import java.time.LocalDate;

public class EmployeeImageUploadDTO {

    private Long id;
    private String pin;
    private String fullName;
    private String designationName;
    private String personalContactNo;
    private Gender gender;
    private String officialEmail;
    private String officialContactNo;
    private String whatsappId;
    private String skypeId;
    private Long reportingToId;
    private Long employeeId;

    private LocalDate DateOfJoining;

    private EmploymentStatus employmentStatus;

    private byte[] getByteStreamFromFilePath;

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

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getPersonalContactNo() {
        return personalContactNo;
    }

    public void setPersonalContactNo(String personalContactNo) {
        this.personalContactNo = personalContactNo;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public Long getReportingToId() {
        return reportingToId;
    }

    public void setReportingToId(Long reportingToId) {
        this.reportingToId = reportingToId;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public byte[] getGetByteStreamFromFilePath() {
        return getByteStreamFromFilePath;
    }

    public void setGetByteStreamFromFilePath(byte[] getByteStreamFromFilePath) {
        this.getByteStreamFromFilePath = getByteStreamFromFilePath;
    }

    public LocalDate getDateOfJoining() {
        return DateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        DateOfJoining = dateOfJoining;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
