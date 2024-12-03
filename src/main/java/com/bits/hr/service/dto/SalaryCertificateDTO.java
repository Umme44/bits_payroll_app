package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.Status;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.bits.hr.domain.SalaryCertificate} entity.
 */
public class SalaryCertificateDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 250)
    private String purpose;

    @Size(min = 3, max = 250)
    private String remarks;

    @NotNull
    private Status status;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate sanctionAt;

    @NotNull
    private Month month;

    @NotNull
    private Integer year;


    private String referenceNumber;


    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    private Long sanctionById;

    private String sanctionByLogin;

    private String employeeName;
    private String pin;
    private String designationName;

    private LocalDate dateOfJoining;

    private Long employeeId;

    private Long signatoryPersonId;

    private String signatoryPersonName;

    private String signatoryPersonDesignation;

    private Long salaryId;

    private Double basic;

    private Double houseRent;

    private Double medicalAllowance;

    private Double conveyanceAllowance;

    private Double entertainment;

    private Double utility;

    private Double otherAddition;

    private Double grossPay;

    private Double pfDeduction;

    private Double incomeTax;

    private Double mobileBill;

    private Double welfareFund;

    private Double otherDeduction;

    private Double TotalDeduction;

    private Double netPayable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getSanctionAt() {
        return sanctionAt;
    }

    public void setSanctionAt(LocalDate sanctionAt) {
        this.sanctionAt = sanctionAt;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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

    public Long getSanctionById() {
        return sanctionById;
    }

    public void setSanctionById(Long userId) {
        this.sanctionById = userId;
    }

    public String getSanctionByLogin() {
        return sanctionByLogin;
    }

    public void setSanctionByLogin(String userLogin) {
        this.sanctionByLogin = userLogin;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
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

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public Long getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(Long salaryId) {
        this.salaryId = salaryId;
    }

    public String getSignatoryPersonName() {
        return signatoryPersonName;
    }

    public void setSignatoryPersonName(String signatoryPersonName) {
        this.signatoryPersonName = signatoryPersonName;
    }

    public String getSignatoryPersonDesignation() {
        return signatoryPersonDesignation;
    }

    public void setSignatoryPersonDesignation(String signatoryPersonDesignation) {
        this.signatoryPersonDesignation = signatoryPersonDesignation;
    }

    public Double getBasic() {
        return basic;
    }

    public void setBasic(Double basic) {
        this.basic = basic;
    }

    public Double getHouseRent() {
        return houseRent;
    }

    public void setHouseRent(Double houseRent) {
        this.houseRent = houseRent;
    }

    public Double getMedicalAllowance() {
        return medicalAllowance;
    }

    public void setMedicalAllowance(Double medicalAllowance) {
        this.medicalAllowance = medicalAllowance;
    }

    public Double getConveyanceAllowance() {
        return conveyanceAllowance;
    }

    public void setConveyanceAllowance(Double conveyanceAllowance) {
        this.conveyanceAllowance = conveyanceAllowance;
    }

    public Double getEntertainment() {
        return entertainment;
    }

    public void setEntertainment(Double entertainment) {
        this.entertainment = entertainment;
    }

    public Double getUtility() {
        return utility;
    }

    public void setUtility(Double utility) {
        this.utility = utility;
    }

    public Double getOtherAddition() {
        return otherAddition;
    }

    public void setOtherAddition(Double otherAddition) {
        this.otherAddition = otherAddition;
    }

    public Double getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(Double grossPay) {
        this.grossPay = grossPay;
    }

    public Double getPfDeduction() {
        return pfDeduction;
    }

    public void setPfDeduction(Double pfDeduction) {
        this.pfDeduction = pfDeduction;
    }

    public Double getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(Double incomeTax) {
        this.incomeTax = incomeTax;
    }

    public Double getMobileBill() {
        return mobileBill;
    }

    public void setMobileBill(Double mobileBill) {
        this.mobileBill = mobileBill;
    }

    public Double getWelfareFund() {
        return welfareFund;
    }

    public void setWelfareFund(Double welfareFund) {
        this.welfareFund = welfareFund;
    }

    public Double getOtherDeduction() {
        return otherDeduction;
    }

    public void setOtherDeduction(Double otherDeduction) {
        this.otherDeduction = otherDeduction;
    }

    public Double getTotalDeduction() {
        return TotalDeduction;
    }

    public void setTotalDeduction(Double totalDeduction) {
        TotalDeduction = totalDeduction;
    }

    public Double getNetPayable() {
        return netPayable;
    }

    public void setNetPayable(Double netPayable) {
        this.netPayable = netPayable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalaryCertificateDTO)) {
            return false;
        }

        return id != null && id.equals(((SalaryCertificateDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalaryCertificateDTO{" +
            "id=" + id +
            ", purpose='" + purpose + '\'' +
            ", remarks='" + remarks + '\'' +
            ", status=" + status +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", sanctionAt=" + sanctionAt +
            ", month=" + month +
            ", year=" + year +
            ", createdById=" + createdById +
            ", createdByLogin='" + createdByLogin + '\'' +
            ", updatedById=" + updatedById +
            ", updatedByLogin='" + updatedByLogin + '\'' +
            ", sanctionById=" + sanctionById +
            ", sanctionByLogin='" + sanctionByLogin + '\'' +
            ", employeeName='" + employeeName + '\'' +
            ", pin='" + pin + '\'' +
            ", designationName='" + designationName + '\'' +
            ", employeeId=" + employeeId +
            ", salaryId=" + salaryId +
            '}';
    }
}
