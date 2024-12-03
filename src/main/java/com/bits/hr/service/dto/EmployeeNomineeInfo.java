package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.domain.enumeration.NomineeStatus;
import java.time.LocalDate;
import java.util.List;

public class EmployeeNomineeInfo {

    private Long id;
    private String pin;
    private String fullName;
    private String designationName;
    private String departmentName;
    private String unitName;
    private String bandName;
    private String personalContactNo;
    private Gender gender;

    private String officialEmail;
    private String officialContactNo;

    private EmploymentStatus employmentStatus;
    private EmployeeCategory employeeCategory;

    private List<NomineeDTO> nomineeList;

    private List<PfNomineeDTO> pfNomineeDTOList;

    private NomineeStatus isAllGFNomineeApproved;
    private NomineeStatus isAllGeneralNomineeApproved;
    private NomineeStatus isAllPfNomineeApproved;

    private LocalDate dateOfJoining;
    private LocalDate dateOfConfirmation;

    private LocalDate contractPeriodEndDate;
    private LocalDate contractPeriodExtendedTo;

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

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public EmployeeCategory getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public List<NomineeDTO> getNomineeList() {
        return nomineeList;
    }

    public void setNomineeList(List<NomineeDTO> nomineeList) {
        this.nomineeList = nomineeList;
    }

    public List<PfNomineeDTO> getPfNomineeDTOList() {
        return pfNomineeDTOList;
    }

    public void setPfNomineeDTOList(List<PfNomineeDTO> pfNomineeDTOList) {
        this.pfNomineeDTOList = pfNomineeDTOList;
    }

    public NomineeStatus getIsAllGFNomineeApproved() {
        return isAllGFNomineeApproved;
    }

    public void setIsAllGFNomineeApproved(NomineeStatus isAllGFNomineeApproved) {
        this.isAllGFNomineeApproved = isAllGFNomineeApproved;
    }

    public NomineeStatus getIsAllGeneralNomineeApproved() {
        return isAllGeneralNomineeApproved;
    }

    public void setIsAllGeneralNomineeApproved(NomineeStatus isAllGeneralNomineeApproved) {
        this.isAllGeneralNomineeApproved = isAllGeneralNomineeApproved;
    }

    public NomineeStatus getIsAllPfNomineeApproved() {
        return isAllPfNomineeApproved;
    }

    public void setIsAllPfNomineeApproved(NomineeStatus isAllPfNomineeApproved) {
        this.isAllPfNomineeApproved = isAllPfNomineeApproved;
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

    public LocalDate getContractPeriodEndDate() {
        return contractPeriodEndDate;
    }

    public void setContractPeriodEndDate(LocalDate contractPeriodEndDate) {
        this.contractPeriodEndDate = contractPeriodEndDate;
    }

    public LocalDate getContractPeriodExtendedTo() {
        return contractPeriodExtendedTo;
    }

    public void setContractPeriodExtendedTo(LocalDate contractPeriodExtendedTo) {
        this.contractPeriodExtendedTo = contractPeriodExtendedTo;
    }
}
