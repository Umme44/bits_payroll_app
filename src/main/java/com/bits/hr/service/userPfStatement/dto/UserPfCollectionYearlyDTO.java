package com.bits.hr.service.userPfStatement.dto;

public class UserPfCollectionYearlyDTO {

    private int year;
    private double totalEmployeeContributionInYear;
    private double totalEmployerContributionInYear;
    private double totalContributionInYear;
    private double incomeForTheYear;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getTotalEmployeeContributionInYear() {
        return totalEmployeeContributionInYear;
    }

    public void setTotalEmployeeContributionInYear(double totalEmployeeContributionInYear) {
        this.totalEmployeeContributionInYear = totalEmployeeContributionInYear;
        this.totalContributionInYear = this.totalEmployeeContributionInYear + this.totalEmployerContributionInYear;
    }

    public double getTotalEmployerContributionInYear() {
        return totalEmployerContributionInYear;
    }

    public void setTotalEmployerContributionInYear(double totalEmployerContributionInYear) {
        this.totalEmployerContributionInYear = totalEmployerContributionInYear;
        this.totalContributionInYear = this.totalEmployeeContributionInYear + this.totalEmployerContributionInYear;
    }

    public double getTotalContributionInYear() {
        return totalContributionInYear;
    }

    public void setTotalContributionInYear(double totalContributionInYear) {
        this.totalContributionInYear = totalContributionInYear;
    }

    public double getIncomeForTheYear() {
        return incomeForTheYear;
    }

    public void setIncomeForTheYear(double incomeForTheYear) {
        this.incomeForTheYear = incomeForTheYear;
    }
}
