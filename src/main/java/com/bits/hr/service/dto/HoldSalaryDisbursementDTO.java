package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Month;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.HoldSalaryDisbursement} entity.
 */
public class HoldSalaryDisbursementDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    private Long userId;

    private String userLogin;

    private Long employeeSalaryId;

    private Month salaryMonth;

    private Integer salaryYear;

    private Double netPay;

    private Double totalDeduction;

    private Double otherDeduction;

    private String pin;

    private String employeeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getEmployeeSalaryId() {
        return employeeSalaryId;
    }

    public Month getSalaryMonth() {
        return salaryMonth;
    }

    public void setSalaryMonth(Month salaryMonth) {
        this.salaryMonth = salaryMonth;
    }

    public Integer getSalaryYear() {
        return salaryYear;
    }

    public void setSalaryYear(Integer salaryYear) {
        this.salaryYear = salaryYear;
    }

    public Double getNetPay() {
        return netPay;
    }

    public void setNetPay(Double netPay) {
        this.netPay = netPay;
    }

    public Double getTotalDeduction() {
        return totalDeduction;
    }

    public void setTotalDeduction(Double totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public Double getOtherDeduction() {
        return otherDeduction;
    }

    public void setOtherDeduction(Double otherDeduction) {
        this.otherDeduction = otherDeduction;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setEmployeeSalaryId(Long employeeSalaryId) {
        this.employeeSalaryId = employeeSalaryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HoldSalaryDisbursementDTO)) {
            return false;
        }

        return id != null && id.equals(((HoldSalaryDisbursementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HoldSalaryDisbursementDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", employeeSalaryId=" + getEmployeeSalaryId() +
            "}";
    }
}
