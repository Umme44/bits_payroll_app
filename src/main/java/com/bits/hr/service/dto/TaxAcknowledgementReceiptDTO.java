package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.AcknowledgementStatus;
import com.bits.hr.util.annotation.ValidateAlphaNumeric;
import com.bits.hr.util.annotation.ValidateNaturalText;
import com.bits.hr.util.annotation.ValidateNumeric;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.TaxAcknowledgementReceipt} entity.
 */
public class TaxAcknowledgementReceiptDTO implements Serializable {

    private Long id;

    @NotNull
    private String tinNumber;

    @NotNull
    @ValidateNumeric
    private String receiptNumber;

    @NotNull
    @ValidateAlphaNumeric
    private String taxesCircle;

    @NotNull
    @ValidateNaturalText
    private String taxesZone;

    @NotNull
    private LocalDate dateOfSubmission;

    @Lob
    private String filePath;

    @NotNull
    private AcknowledgementStatus acknowledgementStatus;

    private Instant receivedAt;

    private Instant createdAt;

    private Instant updatedAt;

    private Long fiscalYearId;

    private int startYear;

    private int endYear;

    private Long employeeId;

    private Long receivedById;

    private String receivedByLogin;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    private String pin;

    private String name;

    private String designation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getTaxesCircle() {
        return taxesCircle;
    }

    public void setTaxesCircle(String taxesCircle) {
        this.taxesCircle = taxesCircle;
    }

    public String getTaxesZone() {
        return taxesZone;
    }

    public void setTaxesZone(String taxesZone) {
        this.taxesZone = taxesZone;
    }

    public LocalDate getDateOfSubmission() {
        return dateOfSubmission;
    }

    public void setDateOfSubmission(LocalDate dateOfSubmission) {
        this.dateOfSubmission = dateOfSubmission;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public AcknowledgementStatus getAcknowledgementStatus() {
        return acknowledgementStatus;
    }

    public void setAcknowledgementStatus(AcknowledgementStatus acknowledgementStatus) {
        this.acknowledgementStatus = acknowledgementStatus;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
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

    public Long getFiscalYearId() {
        return fiscalYearId;
    }

    public void setFiscalYearId(Long aitConfigId) {
        this.fiscalYearId = aitConfigId;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getReceivedById() {
        return receivedById;
    }

    public void setReceivedById(Long userId) {
        this.receivedById = userId;
    }

    public String getReceivedByLogin() {
        return receivedByLogin;
    }

    public void setReceivedByLogin(String userLogin) {
        this.receivedByLogin = userLogin;
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxAcknowledgementReceiptDTO)) {
            return false;
        }

        return id != null && id.equals(((TaxAcknowledgementReceiptDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxAcknowledgementReceiptDTO{" +
            "id=" + getId() +
            ", tinNumber='" + getTinNumber() + "'" +
            ", receiptNumber='" + getReceiptNumber() + "'" +
            ", taxesCircle='" + getTaxesCircle() + "'" +
            ", taxesZone='" + getTaxesZone() + "'" +
            ", dateOfSubmission='" + getDateOfSubmission() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", acknowledgementStatus='" + getAcknowledgementStatus() + "'" +
            ", receivedAt='" + getReceivedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", fiscalYearId=" + getFiscalYearId() +
            ", employeeId=" + getEmployeeId() +
            ", receivedById=" + getReceivedById() +
            ", receivedByLogin='" + getReceivedByLogin() + "'" +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            "}";
    }
}
