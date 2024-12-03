package com.bits.hr.service.incomeTaxManagement;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.service.incomeTaxManagement.model.TaxQueryConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * this class will be responsible for calculating earned income which has been already generated
 * this method will take beginning year of financial year and then calculate already generated all salary
 * which belongs to a particular pin
 * and sum up all necessary value into an object
 * todo: sum of festival bonus of that particular income year (considering promotion , increment )
 * */
@Service
public class CalculatePreviousIncomeService {

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    public List<EmployeeSalary> getAllPreviousSalaries(long employeeId, TaxQueryConfig taxQueryConfig) {
        ArrayList<EmployeeSalary> finalSalaryList = new ArrayList<>();

        if (!taxQueryConfig.getStartYearMonths().isEmpty()) {
            Set<EmployeeSalary> incomeStartYearSalarySet = employeeSalaryRepository.getEmployeeSalaryByYearAndListOfMonth(
                employeeId,
                taxQueryConfig.getIncomeYearStart(),
                taxQueryConfig.getStartYearMonths()
            );
            finalSalaryList.addAll(incomeStartYearSalarySet);
        }
        if (!taxQueryConfig.getEndYearMonths().isEmpty()) {
            Set<EmployeeSalary> incomeEndYearSalarySet = employeeSalaryRepository.getEmployeeSalaryByYearAndListOfMonth(
                employeeId,
                taxQueryConfig.getIncomeYearEnd(),
                taxQueryConfig.getEndYearMonths()
            );
            finalSalaryList.addAll(incomeEndYearSalarySet);
        }
        return finalSalaryList;
    }

    public List<EmployeeSalary> getAllPreviousSalariesWhichIsNotInHold(long employeeId, TaxQueryConfig taxQueryConfig) {
        ArrayList<EmployeeSalary> finalSalaryList = new ArrayList<>();

        if (!taxQueryConfig.getStartYearMonths().isEmpty()) {
            Set<EmployeeSalary> incomeStartYearSalarySet = employeeSalaryRepository.getNotHoldEmployeeSalaryByYearAndListOfMonth(
                employeeId,
                taxQueryConfig.getIncomeYearStart(),
                taxQueryConfig.getStartYearMonths()
            );
            finalSalaryList.addAll(incomeStartYearSalarySet);
        }
        if (!taxQueryConfig.getEndYearMonths().isEmpty()) {
            Set<EmployeeSalary> incomeEndYearSalarySet = employeeSalaryRepository.getNotHoldEmployeeSalaryByYearAndListOfMonth(
                employeeId,
                taxQueryConfig.getIncomeYearEnd(),
                taxQueryConfig.getEndYearMonths()
            );
            finalSalaryList.addAll(incomeEndYearSalarySet);
        }
        return finalSalaryList;
    }
}
