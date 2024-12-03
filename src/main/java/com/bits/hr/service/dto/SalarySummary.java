package com.bits.hr.service.dto;

import java.util.Objects;

public class SalarySummary {

    private String department;
    private double headCount;
    private double basic;
    private double houseRent;
    private double medical;
    private double conveyance;
    private double arrear;
    private double totalGrossSalary;
    private double contractualSalary;
    private double longernSalary;
    private double pfDeduction;
    private double taxDeduction;
    private double hafDeduction;
    private double mobileBillDeduction;
    private double otherDeduction;
    private double totalDeductions;
    private double netPay;
    private double pfContribution;
    private double gratuityFundContribution;
    private double provisionForFestivalBonus;
    private double provisionForLeaveEncashment;
    private double provisionForLFA;

    public SalarySummary() {}

    public SalarySummary(Object[] queryResponse) {
        this.department = (String) queryResponse[0];
        this.headCount = Double.parseDouble(queryResponse[1].toString());
        this.basic = Double.parseDouble(queryResponse[2].toString());
        this.houseRent = Double.parseDouble(queryResponse[3].toString());
        this.medical = Double.parseDouble(queryResponse[4].toString());
        this.conveyance = Double.parseDouble(queryResponse[5].toString());
        this.arrear = Double.parseDouble(queryResponse[6].toString());
        this.totalGrossSalary = Double.parseDouble(queryResponse[7].toString());
        this.contractualSalary = Double.parseDouble(queryResponse[8].toString());
        this.longernSalary = Double.parseDouble(queryResponse[9].toString());
        this.pfDeduction = Double.parseDouble(queryResponse[10].toString());
        this.taxDeduction = Double.parseDouble(queryResponse[11].toString());
        this.hafDeduction = Double.parseDouble(queryResponse[12].toString());
        this.mobileBillDeduction = Double.parseDouble(queryResponse[13].toString());
        this.otherDeduction = Double.parseDouble(queryResponse[14].toString());
        this.totalDeductions = Double.parseDouble(queryResponse[15].toString());
        this.netPay = Double.parseDouble(queryResponse[16].toString());
        this.pfContribution = Double.parseDouble(queryResponse[17].toString());
        this.gratuityFundContribution = Double.parseDouble(queryResponse[18].toString());
        this.provisionForFestivalBonus = Double.parseDouble(queryResponse[19].toString());
        this.provisionForLeaveEncashment = Double.parseDouble(queryResponse[20].toString());
        this.provisionForLFA = 0;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getHeadCount() {
        return headCount;
    }

    public void setHeadCount(double headCount) {
        this.headCount = headCount;
    }

    public double getBasic() {
        return basic;
    }

    public void setBasic(double basic) {
        this.basic = basic;
    }

    public double getHouseRent() {
        return houseRent;
    }

    public void setHouseRent(double houseRent) {
        this.houseRent = houseRent;
    }

    public double getMedical() {
        return medical;
    }

    public void setMedical(double medical) {
        this.medical = medical;
    }

    public double getConveyance() {
        return conveyance;
    }

    public void setConveyance(double conveyance) {
        this.conveyance = conveyance;
    }

    public double getArrear() {
        return arrear;
    }

    public void setArrear(double arrear) {
        this.arrear = arrear;
    }

    public double getTotalGrossSalary() {
        return totalGrossSalary;
    }

    public void setTotalGrossSalary(double totalGrossSalary) {
        this.totalGrossSalary = totalGrossSalary;
    }

    public double getContractualSalary() {
        return contractualSalary;
    }

    public void setContractualSalary(double contractualSalary) {
        this.contractualSalary = contractualSalary;
    }

    public double getLongernSalary() {
        return longernSalary;
    }

    public void setLongernSalary(double longernSalary) {
        this.longernSalary = longernSalary;
    }

    public double getPfDeduction() {
        return pfDeduction;
    }

    public void setPfDeduction(double pfDeduction) {
        this.pfDeduction = pfDeduction;
    }

    public double getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public double getHafDeduction() {
        return hafDeduction;
    }

    public void setHafDeduction(double hafDeduction) {
        this.hafDeduction = hafDeduction;
    }

    public double getMobileBillDeduction() {
        return mobileBillDeduction;
    }

    public void setMobileBillDeduction(double mobileBillDeduction) {
        this.mobileBillDeduction = mobileBillDeduction;
    }

    public double getOtherDeduction() {
        return otherDeduction;
    }

    public void setOtherDeduction(double otherDeduction) {
        this.otherDeduction = otherDeduction;
    }

    public double getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(double totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    public double getPfContribution() {
        return pfContribution;
    }

    public void setPfContribution(double pfContribution) {
        this.pfContribution = pfContribution;
    }

    public double getGratuityFundContribution() {
        return gratuityFundContribution;
    }

    public void setGratuityFundContribution(double gratuityFundContribution) {
        this.gratuityFundContribution = gratuityFundContribution;
    }

    public double getProvisionForFestivalBonus() {
        return provisionForFestivalBonus;
    }

    public void setProvisionForFestivalBonus(double provisionForFestivalBonus) {
        this.provisionForFestivalBonus = provisionForFestivalBonus;
    }

    public double getProvisionForLeaveEncashment() {
        return provisionForLeaveEncashment;
    }

    public void setProvisionForLeaveEncashment(double provisionForLeaveEncashment) {
        this.provisionForLeaveEncashment = provisionForLeaveEncashment;
    }

    public double getProvisionForLFA() {
        return provisionForLFA;
    }

    public void setProvisionForLFA(double provisionForLFA) {
        this.provisionForLFA = provisionForLFA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalarySummary that = (SalarySummary) o;
        return (
            headCount == that.headCount &&
            basic == that.basic &&
            houseRent == that.houseRent &&
            medical == that.medical &&
            conveyance == that.conveyance &&
            arrear == that.arrear &&
            totalGrossSalary == that.totalGrossSalary &&
            contractualSalary == that.contractualSalary &&
            longernSalary == that.longernSalary &&
            pfDeduction == that.pfDeduction &&
            taxDeduction == that.taxDeduction &&
            hafDeduction == that.hafDeduction &&
            mobileBillDeduction == that.mobileBillDeduction &&
            otherDeduction == that.otherDeduction &&
            totalDeductions == that.totalDeductions &&
            netPay == that.netPay &&
            pfContribution == that.pfContribution &&
            gratuityFundContribution == that.gratuityFundContribution &&
            provisionForFestivalBonus == that.provisionForFestivalBonus &&
            provisionForLeaveEncashment == that.provisionForLeaveEncashment &&
            provisionForLFA == that.provisionForLFA &&
            department.equals(that.department)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            department,
            headCount,
            basic,
            houseRent,
            medical,
            conveyance,
            arrear,
            totalGrossSalary,
            contractualSalary,
            longernSalary,
            pfDeduction,
            taxDeduction,
            hafDeduction,
            mobileBillDeduction,
            otherDeduction,
            totalDeductions,
            netPay,
            pfContribution,
            gratuityFundContribution,
            provisionForFestivalBonus,
            provisionForLeaveEncashment,
            provisionForLFA
        );
    }

    @Override
    public String toString() {
        return (
            "SalarySummary{" +
            "department='" +
            department +
            '\'' +
            ", headCount=" +
            headCount +
            ", basic=" +
            basic +
            ", houseRent=" +
            houseRent +
            ", medical=" +
            medical +
            ", conveyance=" +
            conveyance +
            ", arrear=" +
            arrear +
            ", totalGrossSalary=" +
            totalGrossSalary +
            ", contractualSalary=" +
            contractualSalary +
            ", longernSalary=" +
            longernSalary +
            ", pfDeduction=" +
            pfDeduction +
            ", taxDeduction=" +
            taxDeduction +
            ", hafDeduction=" +
            hafDeduction +
            ", mobileBillDeduction=" +
            mobileBillDeduction +
            ", otherDeduction=" +
            otherDeduction +
            ", totalDeductions=" +
            totalDeductions +
            ", netPay=" +
            netPay +
            ", pfContribution=" +
            pfContribution +
            ", gratuityFundContribution=" +
            gratuityFundContribution +
            ", provisionForFestivalBonus=" +
            provisionForFestivalBonus +
            ", provisionForLeaveEncashment=" +
            provisionForLeaveEncashment +
            ", provisionForLFA=" +
            provisionForLFA +
            '}'
        );
    }

    //  Departments 	 HEAD COUNT 	 Basic 	 House rent 	 Medical Allowance 	 Conveyance Allowance 	 Arrear salary 	 Total Gross Salary 	 Contractual Salary 	 Intern Salary 	 PF Deduction 	 Tax Deduction 	 Welfare fund Deduction 	 Mobile bill Deduction 	 Other Deduction 	 Total Deductions 	 Net Pay 	 PF Contribution 	 Gratuity Fund Contribution 	 Provision for Festival Bonus 	 Provision for Leave Encashment 	 Provision for LFA
    public String getcommaSeperatedHeader() {
        return " Departments , HEAD COUNT , Basic , House rent , Medical Allowance , Conveyance Allowance , Arrear salary , Total Gross Salary , Contractual Salary , Intern Salary , PF Deduction , Tax Deduction , Welfare fund Deduction , Mobile bill Deduction , Other Deduction , Total Deductions , Net Pay , PF Contribution , Gratuity Fund Contribution , Provision for Festival Bonus , Provision for Leave Encashment , Provision for LFA \n";
    }

    public String getAsCommaSeperatedValue() {
        return (
            " " +
            department +
            ", " +
            headCount +
            ", " +
            basic +
            ", " +
            houseRent +
            ", " +
            medical +
            ", " +
            conveyance +
            ", " +
            arrear +
            ", " +
            totalGrossSalary +
            ", " +
            contractualSalary +
            ", " +
            longernSalary +
            ", " +
            pfDeduction +
            ", " +
            taxDeduction +
            ", " +
            hafDeduction +
            ", " +
            mobileBillDeduction +
            ", " +
            otherDeduction +
            ", " +
            totalDeductions +
            ", " +
            netPay +
            ", " +
            pfContribution +
            ", " +
            gratuityFundContribution +
            ", " +
            provisionForFestivalBonus +
            ", " +
            provisionForLeaveEncashment +
            ", " +
            provisionForLFA +
            "\n "
        );
    }
}
