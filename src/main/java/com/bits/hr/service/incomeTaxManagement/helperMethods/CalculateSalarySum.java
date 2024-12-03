package com.bits.hr.service.incomeTaxManagement.helperMethods;

import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.service.incomeTaxManagement.model.SalarySum;
import com.bits.hr.service.salaryGenerationFractional.SalaryConstants;
import com.bits.hr.util.MathRoundUtil;
import java.util.List;
import java.util.Optional;

public class CalculateSalarySum {

    public static SalarySum getSummationOfEmployeeSalaries(List<EmployeeSalary> salaryList) {
        double totalBasic = salaryList.stream().mapToDouble(EmployeeSalary::getPayableGrossBasicSalary).sum();
        double totalHouseRent = salaryList.stream().mapToDouble(EmployeeSalary::getPayableGrossHouseRent).sum();
        double totalMedical = salaryList.stream().mapToDouble(EmployeeSalary::getPayableGrossMedicalAllowance).sum();
        double totalConveyance = salaryList.stream().mapToDouble(EmployeeSalary::getPayableGrossConveyanceAllowance).sum();
        double totalArrear = salaryList.stream().mapToDouble(EmployeeSalary::getArrearSalary).sum();
        double totalPfEmployerContribution = salaryList.stream().mapToDouble(EmployeeSalary::getPfContribution).sum();

        SalarySum salarySum = new SalarySum();

        salarySum.setBasic(totalBasic);
        salarySum.setHouseRent(totalHouseRent);
        salarySum.setMedical(totalMedical);
        salarySum.setConveyance(totalConveyance);
        salarySum.setArrears(totalArrear);

        salarySum.setPfContribution(totalPfEmployerContribution);

        return salarySum;
    }

    public static SalarySum getSummationOfSalarySum(List<SalarySum> salarySumList) {
        double perYearBasic = salarySumList.stream().mapToDouble(SalarySum::getBasic).sum();
        double perYearHouseRent = salarySumList.stream().mapToDouble(SalarySum::getHouseRent).sum();
        double perYearMedical = salarySumList.stream().mapToDouble(SalarySum::getMedical).sum();
        double perYearConveyance = salarySumList.stream().mapToDouble(SalarySum::getConveyance).sum();
        double perYearFestivalBonus = salarySumList.stream().mapToDouble(SalarySum::getEffectiveFestivalBonus).sum();
        double perYearPfEmployerContribution = salarySumList.stream().mapToDouble(SalarySum::getPfContribution).sum();

        SalarySum salarySum = new SalarySum();

        salarySum.setBasic(perYearBasic);
        salarySum.setHouseRent(perYearHouseRent);
        salarySum.setMedical(perYearMedical);
        salarySum.setConveyance(perYearConveyance);
        salarySum.setEffectiveFestivalBonus(perYearFestivalBonus);
        salarySum.setPfContribution(perYearPfEmployerContribution);

        return salarySum;
    }

    // pass optional of empty employee salary when it's not about monthly tax
    // pass current gross instead
    public static SalarySum salarySumMultiplier(
        Optional<EmployeeSalary> employeeSalaryOptional,
        int multiplier,
        Optional<Double> grossOptional
    ) {
        SalarySum salarySum = new SalarySum();
        if (employeeSalaryOptional.isPresent()) {
            EmployeeSalary employeeSalary = employeeSalaryOptional.get();

            double basic =
                employeeSalary.getPayableGrossBasicSalary() +
                (MathRoundUtil.round(employeeSalary.getMainGrossSalary() * SalaryConstants.BASIC_PERCENT) * (multiplier - 1));
            salarySum.setBasic(basic);

            double houseRent =
                employeeSalary.getPayableGrossHouseRent() +
                (MathRoundUtil.round(employeeSalary.getMainGrossSalary() * SalaryConstants.HOUSE_RENT_PERCENT) * (multiplier - 1));
            salarySum.setHouseRent(houseRent);

            double medical =
                employeeSalary.getPayableGrossMedicalAllowance() +
                (MathRoundUtil.round(employeeSalary.getMainGrossSalary() * SalaryConstants.MEDICAL_PERCENT) * (multiplier - 1));

            salarySum.setMedical(medical);

            double conveyance =
                employeeSalary.getPayableGrossConveyanceAllowance() + employeeSalary.getMainGrossConveyanceAllowance() * (multiplier - 1);
            salarySum.setConveyance(conveyance);

            return salarySum;
        } else if (grossOptional.isPresent()) {
            double gross = grossOptional.get();

            double basic = MathRoundUtil.round(gross * SalaryConstants.BASIC_PERCENT * multiplier);
            salarySum.setBasic(basic);

            double houseRent = MathRoundUtil.round(gross * SalaryConstants.HOUSE_RENT_PERCENT * multiplier);
            salarySum.setHouseRent(houseRent);

            double medical = MathRoundUtil.round(gross * SalaryConstants.MEDICAL_PERCENT * multiplier);
            salarySum.setMedical(medical);

            double conveyance = MathRoundUtil.round(gross * SalaryConstants.CONVEYANCE_PERCENT * multiplier);
            salarySum.setConveyance(conveyance);

            return salarySum;
        } else {
            throw new RuntimeException();
        }
    }
}
