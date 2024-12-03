package com.bits.hr.service.LeaveManagement.leaveBalanceDetail.DTO;

import com.bits.hr.domain.enumeration.LeaveAmountType;
import com.bits.hr.domain.enumeration.LeaveType;
import java.io.Serializable;

public class LeaveBalanceDetailViewDTO implements Serializable {

    private Long id;

    private String pin;
    private String name;

    private Integer daysRequested;
    private Integer daysApproved;
    private Integer daysCancelled;
    private Integer daysRemaining;
    private Integer daysEffective;

    private LeaveType leaveType;

    private Integer openingBalance;

    private Integer closingBalance;

    private Integer consumedDuringYear;

    private Integer year;

    private Integer amount;

    private LeaveAmountType leaveAmountType;

    private Long employeeId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDaysRequested() {
        return daysRequested;
    }

    public void setDaysRequested(Integer daysRequested) {
        this.daysRequested = daysRequested;
    }

    public Integer getDaysApproved() {
        return daysApproved;
    }

    public void setDaysApproved(Integer daysApproved) {
        this.daysApproved = daysApproved;
    }

    public Integer getDaysCancelled() {
        return daysCancelled;
    }

    public void setDaysCancelled(Integer daysCancelled) {
        this.daysCancelled = daysCancelled;
    }

    public Integer getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(Integer daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    public Integer getDaysEffective() {
        return daysEffective;
    }

    public void setDaysEffective(Integer daysEffective) {
        this.daysEffective = daysEffective;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveBalanceDetailViewDTO)) {
            return false;
        }

        return id != null && id.equals(((LeaveBalanceDetailViewDTO) o).id);
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
