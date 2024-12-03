package com.bits.hr.service.incomeTaxManagement.model;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.IndividualArrearSalary;
import com.bits.hr.domain.enumeration.Gender;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.Data;

@Data
public class IncomeTaxData {

    // default configuration
    private Gender gender = Gender.MALE;
    private int age = 0;
    private boolean hasDisabledChild = false;
    private boolean isDisabled = false;
    private boolean isFreedomFighter = false;
    private boolean isFirstTimeTaxGiver = false;

    private TaxQueryConfig taxQueryConfig;

    /// not mandatory , but will be processed and will have reflection on tax calculation reports.
    Employee employee;
    List<EmployeeSalary> previousSalaryList;
    List<EmployeeSalary> presentToFutureSalaryList;

    // Optional<EmployeeSalary> presentSalaryOptional;
    SalarySum presentToFutureSalarySum;
    double mergedArrears;

    /// mandatory data for tax calculations
    private double effectiveFestivalBonus;
    private double ait;

    private double yearlyBasic;
    private double yearlyHouseRent;
    private double yearlyMedical;
    private double yearlyConveyance;
    private double yearlyPf;

    private double arrearPf;

    // individual arrears
    private List<IndividualArrearSalary> individualArrearSalaryList;
    private double individualArrearSalary;
    private double individualArrearPf;
    private double individualArrearFestivalBonus;
    private double taxCutFromIndividualArrears;
    private double individualArrearNetPay;

    private double arrearFestivalBonus;

    private Optional<EmployeeResignation> employeeResignationOptional = Optional.empty();

    public IncomeTaxData loadDefault(Employee employee) {
        if (employee.getGender() != null) gender = employee.getGender();
        if (employee.getDateOfBirth() != null) age = LocalDate.now().compareTo(employee.getDateOfBirth());
        if (employee.getHasDisabledChild() != null) hasDisabledChild = employee.getHasDisabledChild();
        if (employee.getIsPhysicallyDisabled() != null) isDisabled = employee.getIsPhysicallyDisabled();
        if (employee.getIsFreedomFighter() != null) isFreedomFighter = employee.getIsFreedomFighter();
        if (employee.getIsFirstTimeAitGiver() != null) isFirstTimeTaxGiver = employee.getIsFirstTimeAitGiver();
        this.employee = employee;
        return this;
    }
}
