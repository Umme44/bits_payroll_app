package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.InsuranceRelation;
import com.bits.hr.domain.enumeration.InsuranceStatus;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.bits.hr.domain.InsuranceRegistration} entity.
 */
public class InsuranceRegistrationDTO implements Serializable {

    private Long id;

    @Size(min = 0, max = 250)
    @ValidateNaturalText
    private String name;

    @NotNull
    private LocalDate dateOfBirth;

    @Lob
    private String photo;

    private String insuranceId;

    @NotNull
    private InsuranceRelation insuranceRelation;

    @NotNull
    private InsuranceStatus insuranceStatus;

    private String unapprovalReason;

    @NotNull
    private Double availableBalance;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant approvedAt;

    private Long employeeId;

    private String employeePin;

    private String employeeName;

    private EmployeeCategory employeeCategory;

    private LocalDate dateOfConfirmation;

    private LocalDate dateOfJoining;

    private Long approvedById;

    private String approvedByLogin;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    private LocalDate effectiveDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public InsuranceRelation getInsuranceRelation() {
        return insuranceRelation;
    }

    public void setInsuranceRelation(InsuranceRelation insuranceRelation) {
        this.insuranceRelation = insuranceRelation;
    }

    public LocalDate getDateOfConfirmation() {
        return dateOfConfirmation;
    }

    public void setDateOfConfirmation(LocalDate dateOfConfirmation) {
        this.dateOfConfirmation = dateOfConfirmation;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public InsuranceStatus getInsuranceStatus() {
        return insuranceStatus;
    }

    public void setInsuranceStatus(InsuranceStatus insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
    }

    public String getUnapprovalReason() {
        return unapprovalReason;
    }

    public void setUnapprovalReason(String unapprovalReason) {
        this.unapprovalReason = unapprovalReason;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public EmployeeCategory getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
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

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(Long userId) {
        this.approvedById = userId;
    }

    public String getApprovedByLogin() {
        return approvedByLogin;
    }

    public void setApprovedByLogin(String userLogin) {
        this.approvedByLogin = userLogin;
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

    public String getEmployeePin() {
        return employeePin;
    }

    public void setEmployeePin(String employeePin) {
        this.employeePin = employeePin;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuranceRegistrationDTO)) {
            return false;
        }

        return id != null && id.equals(((InsuranceRegistrationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceRegistrationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", insuranceId='" + getInsuranceId() + "'" +
            ", insuranceRelation='" + getInsuranceRelation() + "'" +
            ", insuranceStatus='" + getInsuranceStatus() + "'" +
            ", unapprovalReason='" + getUnapprovalReason() + "'" +
            ", availableBalance=" + getAvailableBalance() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", approvedAt='" + getApprovedAt() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", approvedById=" + getApprovedById() +
            ", approvedByLogin='" + getApprovedByLogin() + "'" +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            "}";
    }
}
