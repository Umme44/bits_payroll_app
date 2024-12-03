package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.LeaveAmountType;
import com.bits.hr.domain.enumeration.LeaveType;
import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.bits.hr.domain.LeaveBalance} entity.
 */
public class LeaveBalanceDTO implements Serializable {

    private Long id;

    @NotNull
    private LeaveType leaveType;

    @NotNull
    private Integer openingBalance;

    @NotNull
    private Integer closingBalance;

    @NotNull
    private Integer consumedDuringYear;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2199)
    private Integer year;

    @NotNull
    private Integer amount;

    @NotNull
    private LeaveAmountType leaveAmountType;

    private Long employeeId;

    private String pin;
    private String fullName;
    private String designationName;
    private String departmentName;
    private String unitName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Integer getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Integer openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Integer getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(Integer closingBalance) {
        this.closingBalance = closingBalance;
    }

    public Integer getConsumedDuringYear() {
        return consumedDuringYear;
    }

    public void setConsumedDuringYear(Integer consumedDuringYear) {
        this.consumedDuringYear = consumedDuringYear;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LeaveAmountType getLeaveAmountType() {
        return leaveAmountType;
    }

    public void setLeaveAmountType(LeaveAmountType leaveAmountType) {
        this.leaveAmountType = leaveAmountType;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveBalanceDTO)) {
            return false;
        }

        return id != null && id.equals(((LeaveBalanceDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveBalanceDTO{" +
            "id=" + getId() +
            ", leaveType='" + getLeaveType() + "'" +
            ", openingBalance=" + getOpeningBalance() +
            ", closingBalance=" + getClosingBalance() +
            ", consumedDuringYear=" + getConsumedDuringYear() +
            ", year=" + getYear() +
            ", amount=" + getAmount() +
            ", leaveAmountType='" + getLeaveAmountType() + "'" +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
