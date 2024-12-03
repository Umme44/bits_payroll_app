package com.bits.hr.service.incomeTaxManagement.helperMethods;

import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.service.incomeTaxManagement.model.SalarySum;
import java.util.List;

public class GetPreviousIncomeFromSalaryList {

    public SalarySum getPreviousIncome(List<EmployeeSalary> previousSalaryList) {
        SalarySum salarySum = new SalarySum();

        salarySum.setBasic(previousSalaryList.stream().mapToDouble(x -> x.getPayableGrossBasicSalary()).sum());
        salarySum.setHouseRent(previousSalaryList.stream().mapToDouble(x -> x.getPayableGrossHouseRent()).sum());
        salarySum.setMedical(previousSalaryList.stream().mapToDouble(x -> x.getPayableGrossMedicalAllowance()).sum());
        salarySum.setConveyance(previousSalaryList.stream().mapToDouble(x -> x.getPayableGrossConveyanceAllowance()).sum());

        salarySum.setPfContribution(previousSalaryList.stream().mapToDouble(x -> x.getPfContribution()).sum());

        return salarySum;
    }
}
