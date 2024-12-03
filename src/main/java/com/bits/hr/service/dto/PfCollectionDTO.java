package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.PfAccountStatus;
import com.bits.hr.domain.enumeration.PfCollectionType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.PfCollection} entity.
 */
public class PfCollectionDTO implements Serializable {

    private Long id;

    private Double employeeContribution;

    private Double employerContribution;

    private LocalDate transactionDate;

    private Integer year;

    private Integer month;

    private PfCollectionType collectionType;

    @DecimalMin(value = "-10000000")
    @DecimalMax(value = "10000000")
    private Double employeeInterest;

    @DecimalMin(value = "-1000000")
    @DecimalMax(value = "10000000")
    private Double employerInterest;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double gross;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private Double basic;

    private Long pfAccountId;

    private String pfCode;

    private PfAccountStatus status;

    private String designationName;

    private String departmentName;

    private String unitName;

    private String accHolderName;

    private String pin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getEmployeeContribution() {
        return employeeContribution;
    }

    public void setEmployeeContribution(Double employeeContribution) {
        this.employeeContribution = employeeContribution;
    }

    public Double getEmployerContribution() {
        return employerContribution;
    }

    public void setEmployerContribution(Double employerContribution) {
        this.employerContribution = employerContribution;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public PfCollectionType getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(PfCollectionType collectionType) {
        this.collectionType = collectionType;
    }

    public Double getEmployeeInterest() {
        return employeeInterest;
    }

    public void setEmployeeInterest(Double employeeInterest) {
        this.employeeInterest = employeeInterest;
    }

    public Double getEmployerInterest() {
        return employerInterest;
    }

    public void setEmployerInterest(Double employerInterest) {
        this.employerInterest = employerInterest;
    }

    public Double getGross() {
        return gross;
    }

    public void setGross(Double gross) {
        this.gross = gross;
    }

    public Double getBasic() {
        return basic;
    }

    public void setBasic(Double basic) {
        this.basic = basic;
    }

    public Long getPfAccountId() {
        return pfAccountId;
    }

    public void setPfAccountId(Long pfAccountId) {
        this.pfAccountId = pfAccountId;
    }

    public String getPfCode() {
        return pfCode;
    }

    public void setPfCode(String pfCode) {
        this.pfCode = pfCode;
    }

    public PfAccountStatus getStatus() {
        return status;
    }

    public void setStatus(PfAccountStatus status) {
        this.status = status;
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

    public String getAccHolderName() {
        return accHolderName;
    }

    public void setAccHolderName(String accHolderName) {
        this.accHolderName = accHolderName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PfCollectionDTO)) {
            return false;
        }

        return id != null && id.equals(((PfCollectionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PfCollectionDTO{" +
            "id=" + getId() +
            ", employeeContribution=" + getEmployeeContribution() +
            ", employerContribution=" + getEmployerContribution() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", year=" + getYear() +
            ", month=" + getMonth() +
            ", collectionType='" + getCollectionType() + "'" +
            ", employeeInterest=" + getEmployeeInterest() +
            ", employerInterest=" + getEmployerInterest() +
            ", gross=" + getGross() +
            ", basic=" + getBasic() +
            ", pfAccountId=" + getPfAccountId() +
            "}";
    }
}
