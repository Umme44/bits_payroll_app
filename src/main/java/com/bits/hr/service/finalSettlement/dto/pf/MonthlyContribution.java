package com.bits.hr.service.finalSettlement.dto.pf;

import java.util.Objects;

public class MonthlyContribution {

    private String Month = "";
    private double grossSalary;
    private double basic;
    private double employeeContribution;
    private double employeeInterest;
    private double employerContribution;
    private double employerInterest;
    private double employeePfArrear;
    private double employerPfArrear;
    private double totalContribution;
    private double totalInterest;
    private String remarks = " - ";

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(double grossSalary) {
        this.grossSalary = grossSalary;
    }

    public double getBasic() {
        return basic;
    }

    public void setBasic(double basic) {
        this.basic = basic;
    }

    public double getEmployeeContribution() {
        return employeeContribution;
    }

    public void setEmployeeContribution(double employeeContribution) {
        this.employeeContribution = employeeContribution;
        this.totalContribution = this.employeeContribution + this.employerContribution;
    }

    public double getEmployerContribution() {
        return employerContribution;
    }

    public void setEmployerContribution(double employerContribution) {
        this.employerContribution = employerContribution;
        this.totalContribution = this.employeeContribution + this.employerContribution;
    }

    public double getTotalContribution() {
        return totalContribution;
    }

    public void setTotalContribution(double totalContribution) {
        this.totalContribution = totalContribution;
    }

    public double getEmployeeInterest() {
        return employeeInterest;
    }

    public double getEmployeePfArrear() {
        return employeePfArrear;
    }

    public void setEmployeePfArrear(double employeePfArrear) {
        this.employeePfArrear = employeePfArrear;
    }

    public double getEmployerPfArrear() {
        return employerPfArrear;
    }

    public void setEmployerPfArrear(double employerPfArrear) {
        this.employerPfArrear = employerPfArrear;
    }

    public void setEmployeeInterest(double employeeInterest) {
        this.employeeInterest = employeeInterest;
        this.totalInterest = this.totalInterest + this.employeeInterest;
    }

    public double getEmployerInterest() {
        return employerInterest;
    }

    public void setEmployerInterest(double employerInterest) {
        this.employerInterest = employerInterest;
        this.totalInterest = this.totalInterest + this.employerInterest;
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(double totalInterest) {
        this.totalInterest = totalInterest;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyContribution that = (MonthlyContribution) o;
        return (
            Double.compare(that.getGrossSalary(), getGrossSalary()) == 0 &&
            Double.compare(that.getBasic(), getBasic()) == 0 &&
            Double.compare(that.getEmployeeContribution(), getEmployeeContribution()) == 0 &&
            Double.compare(that.getEmployerContribution(), getEmployerContribution()) == 0 &&
            Double.compare(that.getTotalContribution(), getTotalContribution()) == 0 &&
            Objects.equals(getMonth(), that.getMonth()) &&
            Objects.equals(getRemarks(), that.getRemarks())
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            getMonth(),
            getGrossSalary(),
            getBasic(),
            getEmployeeContribution(),
            getEmployerContribution(),
            getTotalContribution(),
            getRemarks()
        );
    }

    @Override
    public String toString() {
        return (
            "MonthlyContribution{" +
            "Month='" +
            Month +
            '\'' +
            ", grossSalary=" +
            grossSalary +
            ", basic=" +
            basic +
            ", employeeContribution=" +
            employeeContribution +
            ", employerContribution=" +
            employerContribution +
            ", totalContribution=" +
            totalContribution +
            ", Remarks='" +
            remarks +
            '\'' +
            '}'
        );
    }
}
