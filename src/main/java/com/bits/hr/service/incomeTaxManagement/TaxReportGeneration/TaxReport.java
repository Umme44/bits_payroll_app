package com.bits.hr.service.incomeTaxManagement.TaxReportGeneration;

import static com.bits.hr.service.salaryGenerationFractional.SalaryConstants.*;

import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.IndividualArrearSalary;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.SalaryIncome;
import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.TaxCalculationDTO;
import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.TaxLiability;
import com.bits.hr.util.MathRoundUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TaxReport {

    public static String generateTextBasedTaxReport(TaxCalculationDTO taxCalculationDTO) {
        try {
            StringBuilder str = new StringBuilder();
            if (taxCalculationDTO.isResigningEmployee() == true) {
                str.append("No Tax For last fraction month of resigning employee");
            } else {
                String newLine = " <br/> ";
                //____________________________________________________________________________________________________________________________________
                str.append(tableStart());
                {
                    List<String> l1 = new ArrayList<>();
                    l1.add("Name");
                    l1.add(taxCalculationDTO.getName());
                    l1.add("PIN");
                    l1.add(taxCalculationDTO.getPin());
                    str.append(createTableDataRow(l1));

                    List<String> l2 = new ArrayList<>();
                    l2.add("Date of Joining");
                    l2.add(taxCalculationDTO.getDateOfJoining().toString());
                    if (
                        taxCalculationDTO.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE ||
                        taxCalculationDTO.getEmployeeCategory() == EmployeeCategory.INTERN
                    ) {
                        l2.add("Contract End Date");
                        l2.add(taxCalculationDTO.getContactPeriodEndDate().toString());
                    } else {
                        l2.add("Date of Confirmation");
                        l2.add(taxCalculationDTO.getDateOfConfirmation().toString());
                    }
                    str.append(createTableDataRow(l2));

                    List<String> l3 = new ArrayList<>();
                    l3.add("Department");

                    String department = "-";
                    if (taxCalculationDTO.getDepartment() != null) {
                        department = taxCalculationDTO.getDepartment();
                    }
                    l3.add(department);

                    l3.add("Designation");
                    String designation = "-";
                    if (taxCalculationDTO.getDesignation() != null) {
                        designation = taxCalculationDTO.getDesignation();
                    }
                    l3.add(designation);
                    str.append(createTableDataRow(l3));

                    List<String> l4 = new ArrayList<>();
                    int incomeYearStart = taxCalculationDTO.getIncomeTaxData().getTaxQueryConfig().getIncomeYearStart();
                    l4.add("Income Year");
                    l4.add(incomeYearStart + "-" + (incomeYearStart + 1));
                    l4.add("Assessment Year");
                    l4.add((incomeYearStart + 1) + "-" + (incomeYearStart + 2));
                    str.append(createTableDataRow(l4));
                }
                str.append(tableEnd());

                //..........................................................................................................

                str.append(newLine);

                str.append(tableStart());

                List<EmployeeSalary> previousSalaryList = taxCalculationDTO.getIncomeTaxData().getPreviousSalaryList();
                List<EmployeeSalary> presentToFutureSalaryList = taxCalculationDTO.getIncomeTaxData().getPresentToFutureSalaryList();

                List<EmployeeSalary> employeeSalaryList = new ArrayList<>();

                employeeSalaryList.addAll(previousSalaryList);
                employeeSalaryList.addAll(presentToFutureSalaryList);

                double tGross = 0;
                double tBasic = 0;
                double tHr = 0;
                double tMed = 0;
                double tConv = 0;
                double tPf = 0;
                double tTax = 0;
                {
                    List<String> head = new ArrayList<>();
                    head.add("Salaries");
                    head.add("Gross");
                    head.add("Basic");
                    head.add("HR");
                    head.add("Med");
                    head.add("Conv");
                    head.add("PF");
                    head.add("Tax");
                    str.append(createTableHeadRow(head));

                    for (EmployeeSalary es : employeeSalaryList) {
                        tGross += es.getPayableGrossSalary();
                        tBasic += es.getPayableGrossBasicSalary();
                        tHr += es.getPayableGrossHouseRent();
                        tMed += es.getPayableGrossMedicalAllowance();
                        tConv += es.getPayableGrossConveyanceAllowance();
                        tPf += es.getPfContribution();
                        tTax += es.getTaxDeduction();

                        List<String> dt = new ArrayList<>();
                        dt.add(es.getMonth().toString() + " " + es.getYear());
                        dt.add(String.valueOf(MathRoundUtil.round(es.getPayableGrossSalary())));
                        dt.add(String.valueOf(MathRoundUtil.round(es.getPayableGrossBasicSalary())));
                        dt.add(String.valueOf(MathRoundUtil.round(es.getPayableGrossHouseRent())));
                        dt.add(String.valueOf(MathRoundUtil.round(es.getPayableGrossMedicalAllowance())));
                        dt.add(String.valueOf(MathRoundUtil.round(es.getPayableGrossConveyanceAllowance())));
                        dt.add(String.valueOf(MathRoundUtil.round(es.getPfContribution())));
                        dt.add(String.valueOf(MathRoundUtil.round(es.getTaxDeduction())));
                        str.append(createTableDataRow(dt));
                    }
                    {
                        List<String> dt = new ArrayList<>();
                        dt.add("Total");
                        dt.add(String.valueOf(MathRoundUtil.round(tGross)));
                        dt.add(String.valueOf(MathRoundUtil.round(tBasic)));
                        dt.add(String.valueOf(MathRoundUtil.round(tHr)));
                        dt.add(String.valueOf(MathRoundUtil.round(tMed)));
                        dt.add(String.valueOf(MathRoundUtil.round(tConv)));
                        dt.add(String.valueOf(MathRoundUtil.round(tPf)));
                        dt.add(String.valueOf(MathRoundUtil.round(tTax)));
                        str.append(createTableHeadRow(dt));
                    }
                }
                str.append(tableEnd());

                //...........................................................................................................
                // Arrear Salary & Festival Bonus

                List<IndividualArrearSalary> arrearSalaries = taxCalculationDTO.getIncomeTaxData().getIndividualArrearSalaryList();

                if (arrearSalaries.size() > 0) {
                    str.append(newLine);
                    str.append(tableStart());
                    {
                        List<String> arrearHead = new ArrayList<>();
                        arrearHead.add("Individual Arrear");
                        arrearHead.add("Gross");
                        arrearHead.add("Basic");
                        arrearHead.add("HR");
                        arrearHead.add("Med");
                        arrearHead.add("Conv");
                        arrearHead.add("Festival Bonus");
                        arrearHead.add("PF");
                        arrearHead.add("Tax");
                        arrearHead.add("Net");
                        str.append(createTableHeadRow(arrearHead));

                        double totalArrearBasic = 0;
                        double totalArrearHouseRent = 0;
                        double totalArrearMedical = 0;
                        double totalArrearConveyance = 0;

                        for (int i = 0; i < arrearSalaries.size(); i++) {
                            IndividualArrearSalary arrearSalary = arrearSalaries.get(i);
                            double arrearGross = arrearSalary.getArrearSalary();
                            double arrearBasic = arrearSalary.getArrearSalary() * BASIC_PERCENT;
                            double arrearHouseRent = arrearSalary.getArrearSalary() * HOUSE_RENT_PERCENT;
                            double arrearMedical = arrearSalary.getArrearSalary() * MEDICAL_PERCENT;
                            double arrearConveyance = arrearGross - (arrearBasic + arrearHouseRent + arrearMedical);

                            totalArrearBasic += arrearBasic;
                            totalArrearHouseRent += arrearHouseRent;
                            totalArrearMedical += arrearMedical;
                            totalArrearConveyance += arrearConveyance;

                            List<String> saBody = new ArrayList<>();
                            saBody.add(arrearSalary.getTitle());
                            saBody.add(String.valueOf(MathRoundUtil.round(arrearGross)));
                            saBody.add(String.valueOf(MathRoundUtil.round(arrearBasic)));
                            saBody.add(String.valueOf(MathRoundUtil.round(arrearHouseRent)));
                            saBody.add(String.valueOf(MathRoundUtil.round(arrearMedical)));
                            saBody.add(String.valueOf(MathRoundUtil.round(arrearConveyance)));
                            saBody.add(String.valueOf(MathRoundUtil.round(arrearSalary.getFestivalBonus())));
                            saBody.add(String.valueOf(MathRoundUtil.round(arrearSalary.getArrearPfDeduction())));
                            saBody.add(String.valueOf(MathRoundUtil.round(arrearSalary.getTaxDeduction())));
                            saBody.add(String.valueOf(MathRoundUtil.round(arrearSalary.getNetPay())));
                            str.append(createTableDataRow(saBody));
                        }

                        List<String> arrearSalaryTotal = new ArrayList<>();
                        arrearSalaryTotal.add("Total");
                        arrearSalaryTotal.add(
                            String.valueOf(MathRoundUtil.round(taxCalculationDTO.getIncomeTaxData().getIndividualArrearSalary()))
                        );
                        arrearSalaryTotal.add(String.valueOf(MathRoundUtil.round(totalArrearBasic)));
                        arrearSalaryTotal.add(String.valueOf(MathRoundUtil.round(totalArrearHouseRent)));
                        arrearSalaryTotal.add(String.valueOf(MathRoundUtil.round(totalArrearMedical)));
                        arrearSalaryTotal.add(String.valueOf(MathRoundUtil.round(totalArrearConveyance)));
                        arrearSalaryTotal.add(
                            String.valueOf(MathRoundUtil.round(taxCalculationDTO.getIncomeTaxData().getIndividualArrearFestivalBonus()))
                        );
                        arrearSalaryTotal.add(
                            String.valueOf(MathRoundUtil.round(taxCalculationDTO.getIncomeTaxData().getIndividualArrearPf()))
                        );
                        arrearSalaryTotal.add(
                            String.valueOf(MathRoundUtil.round(taxCalculationDTO.getIncomeTaxData().getTaxCutFromIndividualArrears()))
                        );
                        arrearSalaryTotal.add(
                            String.valueOf(MathRoundUtil.round(taxCalculationDTO.getIncomeTaxData().getIndividualArrearNetPay()))
                        );
                        str.append(createTableHeadRow(arrearSalaryTotal));
                    }
                    str.append(tableEnd());
                }

                //...........................................................................................................
                // HEADS OF SALARY INCOME  << html table generation >>
                str.append(newLine);

                str.append(tableStart());
                {
                    List<String> saHead = new ArrayList<>();
                    saHead.add("HEADS OF SALARY INCOME");
                    saHead.add("Gross Salary (Yearly)");
                    saHead.add("Exemption (Yearly)");
                    saHead.add("Taxable Income (Yearly)");
                    str.append(createTableHeadRow(saHead));

                    List<SalaryIncome> salIncList = taxCalculationDTO.getSalaryIncomeList();
                    for (int i = 0; i < salIncList.size(); i++) {
                        SalaryIncome s = salIncList.get(i);
                        List<String> saBody = new ArrayList<>();
                        saBody.add(s.getHead());
                        saBody.add(String.valueOf(MathRoundUtil.round(s.getSalary())));
                        saBody.add(String.valueOf(MathRoundUtil.round(s.getExemption())));
                        saBody.add(String.valueOf(MathRoundUtil.round(s.getTaxableIncome())));
                        str.append(createTableDataRow(saBody));
                    }

                    //                    double grandTotalSalary = salIncList.stream().mapToDouble(SalaryIncome::getSalary).sum();
                    //                    double grandTotalExemption = salIncList.stream().mapToDouble(SalaryIncome::getExemption).sum();
                    //                    double grandTotalTaxableIncome = salIncList.stream().mapToDouble(SalaryIncome::getTaxableIncome).sum();

                    List<String> saTotal = new ArrayList<>();
                    saTotal.add(taxCalculationDTO.getTotalSalaryIncome().getHead());
                    saTotal.add(String.valueOf(MathRoundUtil.round(taxCalculationDTO.getTotalSalaryIncome().getSalary())));
                    saTotal.add(String.valueOf(MathRoundUtil.round(taxCalculationDTO.getTotalSalaryIncome().getExemption())));
                    saTotal.add(String.valueOf(MathRoundUtil.round(taxCalculationDTO.getTotalSalaryIncome().getTaxableIncome())));
                    str.append(createTableHeadRow(saTotal));

                    List<String> saTotalTaxableIncome = new ArrayList<>();
                    saTotalTaxableIncome.add("TOTAL TAXABLE INCOME");
                    saTotalTaxableIncome.add(" ");
                    saTotalTaxableIncome.add(" ");
                    saTotalTaxableIncome.add(
                        String.valueOf(MathRoundUtil.round(taxCalculationDTO.getTotalSalaryIncome().getTaxableIncome()))
                    );
                    str.append(createTableHeadRow(saTotalTaxableIncome));
                }
                str.append(tableEnd());

                str.append(newLine);

                //_____________________________________________________________________________________________________

                str.append(tableStart());
                {
                    List<String> tlHead = new ArrayList<>();
                    tlHead.add("CALCULATION OF TAX LIABILITY");
                    tlHead.add("Income Slabs");
                    tlHead.add("TaxRate");
                    tlHead.add("Yearly Tax");

                    str.append(createTableHeadRow(tlHead));

                    List<TaxLiability> taxLiabilities = taxCalculationDTO.getTaxLiability();
                    for (int i = 0; i < taxLiabilities.size(); i++) {
                        TaxLiability tl = taxLiabilities.get(i);
                        List<String> tlData = new ArrayList<>();
                        tlData.add(tl.getHead());
                        tlData.add(String.valueOf(MathRoundUtil.round(tl.getSlab())));
                        tlData.add(String.valueOf(MathRoundUtil.round(tl.getRate() * 100)) + "%");
                        tlData.add(String.valueOf(MathRoundUtil.round(tl.getTax())));
                        str.append(createTableDataRow(tlData));
                    }

                    double totalTaxLiabilities = taxLiabilities.stream().mapToDouble(TaxLiability::getTax).sum();

                    List<String> tlTotal = new ArrayList<>();
                    tlTotal.add("TOTAL TAX LIABILITY");
                    tlTotal.add(" ");
                    tlTotal.add(" ");
                    tlTotal.add(String.valueOf(MathRoundUtil.round(totalTaxLiabilities)));
                    str.append(createTableHeadRow(tlTotal));
                }

                str.append(tableEnd());

                //_____________________________________________________________________________________________________
                str.append(newLine);

                str.append(tableStart());

                List<String> investmentStr = new ArrayList<>();
                investmentStr.add("Max Allowed investment");
                investmentStr.add(String.valueOf(MathRoundUtil.round(taxCalculationDTO.getMaxAllowedInvestment())));
                str.append(createTableDataRow(investmentStr));

                List<String> invRebateStr = new ArrayList<>();
                invRebateStr.add("Rebate");
                invRebateStr.add(String.valueOf(MathRoundUtil.round(taxCalculationDTO.getRebate())));
                str.append(createTableDataRow(invRebateStr));

                //______________________________________________________________________________________________________
                str.append(newLine);
                str.append(tableStart());
                {
                    List<String> c1 = new ArrayList<>();
                    c1.add("NET TAX LIABILITY AFTER CONSIDERING INVESTMENT REBATE");
                    c1.add(String.valueOf(MathRoundUtil.round(taxCalculationDTO.getNetTaxLiability())));
                    str.append(createTableDataRow(c1));

                    List<String> c2 = new ArrayList<>();
                    c2.add("LESS LAST YEAR ADJUSTMENT (Approved Documents from NBR)/AIT of Vehicle/Others");
                    c2.add(String.valueOf(MathRoundUtil.round(taxCalculationDTO.getLastYearAdjustment())));
                    str.append(createTableDataRow(c2));
                }

                str.append(tableEnd());
                str.append(newLine);
                //______________________________________________________________________________________________________
                str.append(tableStart());
                {
                    if (taxCalculationDTO.isMonthlyTaxCalculation()) {
                        List<String> c4 = new ArrayList<>();
                        c4.add("ACTUAL TAX PAID AND DULY DEDUCTED IN PREVIOUS MONTHS");

                        double taxPaidInPreviousMonths = taxCalculationDTO
                            .getPreviousTaxDeduction()
                            .stream()
                            .mapToDouble(Double::doubleValue)
                            .sum();
                        double taxPaidAgainstIndividualArrear = taxCalculationDTO.getIncomeTaxData().getTaxCutFromIndividualArrears();
                        double totalTaxPaidInPreviousMonths = taxPaidInPreviousMonths + taxPaidAgainstIndividualArrear;
                        c4.add(String.valueOf(MathRoundUtil.round(totalTaxPaidInPreviousMonths)));
                        str.append(createTableDataRow(c4));

                        List<String> c5 = new ArrayList<>();
                        c5.add("ACTUAL TAX PAID AND DULY DEDUCTED THIS MONTH");
                        c5.add(String.valueOf(MathRoundUtil.round(taxCalculationDTO.getCurrentTaxDeduction())));
                        str.append(createTableDataRow(c5));

                        List<String> c6 = new ArrayList<>();
                        c6.add("UPCOMING TAX WILL BE DEDUCTED IN FUTURE MONTH");
                        String upcomingTax = String.valueOf(
                            MathRoundUtil.round(
                                taxCalculationDTO.getPredictedFutureTaxDeduction().stream().mapToDouble(Double::doubleValue).sum()
                            )
                        );
                        c6.add(upcomingTax);
                        str.append(createTableHeadRow(c6));
                    } else {
                        List<String> c7 = new ArrayList<>();
                        c7.add("UPCOMING TAX WILL BE DEDUCTED IN FUTURE MONTH");
                        String tax = String.valueOf(
                            MathRoundUtil.round(taxCalculationDTO.getPreviousTaxDeduction().stream().mapToDouble(Double::doubleValue).sum())
                        );
                        c7.add(tax);
                        str.append(createTableDataRow(c7));
                    }
                }
                str.append(tableEnd());
            }

            return str.toString();
        } catch (Exception ex) {
            log.error(ex);
            return "";
        }
    }

    public static String tableStart() {
        return "<table>";
    }

    public static String tableEnd() {
        return "</table>";
    }

    public static String createTableHeadRow(List<String> stringList) {
        StringBuilder str = new StringBuilder();
        // row start
        str.append("<tr>");
        for (int i = 0; i < stringList.size(); i++) {
            str.append("<th>");
            str.append(stringList.get(i));
            str.append("</th>");
        }
        // row end
        str.append("</tr>");
        return str.toString();
    }

    public static String createTableDataRow(List<String> stringList) {
        StringBuilder str = new StringBuilder();
        // row start
        str.append("<tr>");
        for (int i = 0; i < stringList.size(); i++) {
            str.append("<td>");
            str.append(stringList.get(i));
            str.append("</td>");
        }
        // row end
        str.append("</tr>");
        return str.toString();
    }
}
