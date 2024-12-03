package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.domain.enumeration.Gender;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.bits.hr.domain.EmploymentCertificate} entity.
 */
public class EmploymentCertificateDTO implements Serializable {

    private Long id;

    @NotNull
    private CertificateStatus certificateStatus;

    private String referenceNumber;

    private LocalDate issueDate;

    private String reason;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant generatedAt;

    private Long employeeId;

    private String employeeName;

    private String employeePin;

    private String departmentName;

    private String designationName;

    private String unitName;

    private LocalDate dateOfJoining;

    private Gender employeeGender;

    private Long signatoryPersonId;

    private String signatoryPersonPin;

    private String signatoryPersonName;

    private String signatoryPersonDepartment;

    private String signatoryPersonDesignation;

    private String signatoryPersonUnit;

    private String signatoryPersonEmail;

    private String signatoryPersonCell;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    private Long generatedById;

    private String generatedByLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CertificateStatus getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(CertificateStatus certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getSignatoryPersonId() {
        return signatoryPersonId;
    }

    public void setSignatoryPersonId(Long employeeId) {
        this.signatoryPersonId = employeeId;
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

    public Long getGeneratedById() {
        return generatedById;
    }

    public void setGeneratedById(Long userId) {
        this.generatedById = userId;
    }

    public String getGeneratedByLogin() {
        return generatedByLogin;
    }

    public void setGeneratedByLogin(String userLogin) {
        this.generatedByLogin = userLogin;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeePin() {
        return employeePin;
    }

    public void setEmployeePin(String employeePin) {
        this.employeePin = employeePin;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public Gender getEmployeeGender() {
        return employeeGender;
    }

    public void setEmployeeGender(Gender employeeGender) {
        this.employeeGender = employeeGender;
    }

    public String getSignatoryPersonPin() {
        return signatoryPersonPin;
    }

    public void setSignatoryPersonPin(String signatoryPersonPin) {
        this.signatoryPersonPin = signatoryPersonPin;
    }

    public String getSignatoryPersonName() {
        return signatoryPersonName;
    }

    public void setSignatoryPersonName(String signatoryPersonName) {
        this.signatoryPersonName = signatoryPersonName;
    }

    public String getSignatoryPersonDepartment() {
        return signatoryPersonDepartment;
    }

    public void setSignatoryPersonDepartment(String signatoryPersonDepartment) {
        this.signatoryPersonDepartment = signatoryPersonDepartment;
    }

    public String getSignatoryPersonDesignation() {
        return signatoryPersonDesignation;
    }

    public void setSignatoryPersonDesignation(String signatoryPersonDesignation) {
        this.signatoryPersonDesignation = signatoryPersonDesignation;
    }

    public String getSignatoryPersonUnit() {
        return signatoryPersonUnit;
    }

    public void setSignatoryPersonUnit(String signatoryPersonUnit) {
        this.signatoryPersonUnit = signatoryPersonUnit;
    }

    public String getSignatoryPersonEmail() {
        return signatoryPersonEmail;
    }

    public void setSignatoryPersonEmail(String signatoryPersonEmail) {
        this.signatoryPersonEmail = signatoryPersonEmail;
    }

    public String getSignatoryPersonCell() {
        return signatoryPersonCell;
    }

    public void setSignatoryPersonCell(String signatoryPersonCell) {
        this.signatoryPersonCell = signatoryPersonCell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmploymentCertificateDTO)) {
            return false;
        }

        return id != null && id.equals(((EmploymentCertificateDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmploymentCertificateDTO{" +
            "id=" + getId() +
            ", certificateStatus='" + getCertificateStatus() + "'" +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", reason='" + getReason() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", generatedAt='" + getGeneratedAt() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", signatoryPersonId=" + getSignatoryPersonId() +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            ", generatedById=" + getGeneratedById() +
            ", generatedByLogin='" + getGeneratedByLogin() + "'" +
            "}";
    }
}
