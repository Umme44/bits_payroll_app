package com.bits.hr.service.incomeTaxManagement.presentToFutureSalary;

import static com.bits.hr.service.salaryGenerationFractional.SalaryConstants.*;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.ArrearSalaryRepository;
import com.bits.hr.repository.EmploymentHistoryRepository;
import com.bits.hr.repository.PfArrearRepository;
import com.bits.hr.service.finalSettlement.helperMethods.ResignationProcessingService;
import com.bits.hr.service.salaryGenerationFractional.*;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MockSalaryGenerationService {

    @Autowired
    private ExcessCellBillService excessCellBillService;

    @Autowired
    private HafDeductionService hafDeductionService;

    @Autowired
    private EmploymentHistoryRepository employmentHistoryRepository;

    @Autowired
    private ArrearSalaryRepository arrearSalaryRepository;

    @Autowired
    private AllowanceService allowanceService;

    @Autowired
    private PfArrearRepository pfArrearRepository;

    @Autowired
    private ResignationProcessingService resignationProcessingService;

    public Optional<EmployeeSalary> generateMonthlySalary(int month, int year, Employee employee, int fractionDays) {
        try {
            EmployeeSalary salary = new EmployeeSalary();

            salary.setEmployee(employee);

            salary.setPin(employee.getPin());

            salary.setRefPin(employee.getReferenceId());

            salary.setMonth(Month.fromInteger(month));

            salary.setYear(year);

            salary.setJoiningDate(employee.getDateOfJoining());

            if (
                employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE ||
                employee.getEmployeeCategory() == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE
            ) {
                salary.setConfirmationDate(employee.getDateOfConfirmation());
            }
            if (
                employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE ||
                employee.getEmployeeCategory() == EmployeeCategory.INTERN
            ) {
                if (employee.getContractPeriodExtendedTo() == null) {
                    salary.setConfirmationDate(employee.getContractPeriodEndDate());
                } else {
                    salary.setConfirmationDate(employee.getContractPeriodExtendedTo());
                }
            }

            salary.setEmployeeCategory(employee.getEmployeeCategory());
            // set category as intern if intern
            if (
                employee.getDesignation() != null &&
                employee.getDesignation().getDesignationName() != null &&
                employee.getDesignation().getDesignationName().toLowerCase(Locale.ROOT).trim().equals("intern")
            ) {
                salary.setEmployeeCategory(EmployeeCategory.INTERN);
            }

            salary.setSalaryGenerationDate(LocalDate.now());

            if (employee.getUnit() != null && employee.getUnit().getUnitName() != null) {
                salary.setUnit(employee.getUnit().getUnitName());
            } else {
                salary.setUnit(" - ");
            }

            Department department;
            if (employee.getDepartment() != null) {
                department = employee.getDepartment();
                String DeptName = department.getDepartmentName();
                salary.setDepartment(DeptName);
            } else {
                salary.setDepartment(" - ");
            }

            // gross - basic - house rent - medical convenience
            // main will be taken from recent value
            double mGross = MathRoundUtil.round(employee.getMainGrossSalary());
            double mBasic = MathRoundUtil.round(mGross * BASIC_PERCENT); //60%
            double mHouse = MathRoundUtil.round(mGross * HOUSE_RENT_PERCENT); //30%
            double mMedical = MathRoundUtil.round(mGross * MEDICAL_PERCENT); //6%

            //double mConveyance  = mGross * .04;    //4%
            double mConveyance = (double) MathRoundUtil.round(mGross - (mBasic + mHouse + mMedical));

            salary.setMainGrossSalary(mGross);
            salary.setMainGrossBasicSalary(mBasic);
            salary.setMainGrossHouseRent(mHouse);
            salary.setMainGrossMedicalAllowance(mMedical);
            salary.setMainGrossConveyanceAllowance(mConveyance);

            int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth();
            int absentDays = daysInMonth - fractionDays;

            salary.setAbsentDays(absentDays);
            salary.setFractionDays(fractionDays);

            double pGross = MathRoundUtil.round((mGross / (double) daysInMonth) * ((double) fractionDays));
            double pBasic = MathRoundUtil.round(pGross * BASIC_PERCENT);
            double pHouse = MathRoundUtil.round(pGross * HOUSE_RENT_PERCENT);
            double pMedical = MathRoundUtil.round(pGross * MEDICAL_PERCENT);
            double pConveyance = MathRoundUtil.round(pGross - (pBasic + pHouse + pMedical));

            salary.setPayableGrossSalary(pGross);
            salary.setPayableGrossBasicSalary(pBasic);
            salary.setPayableGrossHouseRent(pHouse);
            salary.setPayableGrossMedicalAllowance(pMedical);
            salary.setPayableGrossConveyanceAllowance(pConveyance);

            double welfareFundDeduction = hafDeductionService.hafDeduction(salary);
            salary.setWelfareFundDeduction(welfareFundDeduction);

            double mobileBillDeduction = excessCellBillService.getExcessCellBillDeduction(employee, year, month);
            salary.setMobileBillDeduction(mobileBillDeduction);

            double pfDeduction = mockPfContribution(salary);
            salary.setPfDeduction(pfDeduction);
            salary.setPfContribution(pfDeduction);

            // not required in tax calculation

            salary.setOtherDeduction(0d);
            salary.setGfContribution(0d);
            salary.setProvisionForFestivalBonus(0d);
            salary.setProvisionForLeaveEncashment(0d);

            double arrear = getArrear(employee, year, Month.fromInteger(month));
            double arrearPf = 0.0d;

            salary.setArrearSalary(arrear);
            salary.setProvidentFundArrear(arrearPf);

            // allowances
            Allowance allowance = allowanceService.getAllowance(employee, month, year);
            salary.setAllowance01(allowance.getAllowance01());
            salary.setAllowance02(allowance.getAllowance02());
            salary.setAllowance03(allowance.getAllowance03());
            salary.setAllowance04(allowance.getAllowance04());
            salary.setAllowance05(allowance.getAllowance05());
            salary.setAllowance06(allowance.getAllowance06());

            salary.setTotalDeduction(0d);
            salary.setTaxDeduction(0d);

            // initially set salary adjustment to 0
            double salaryAdjustment = 0d;
            salary.setSalaryAdjustment(salaryAdjustment);

            double netPay = MathRoundUtil.round(((pGross + arrear)) + salaryAdjustment);
            salary.setNetPay(netPay);

            // remarks = pf load deduction if there are pf loan deduction available
            String remarks = " - ";
            salary.setRemarks(remarks);

            return Optional.of(salary);
        } catch (Exception ex) {
            log.error(ex);
            return Optional.empty();
        }
    }

    private double getArrear(Employee employee, int year, Month month) {
        // arrear salary
        // arrear payment

        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findByEmployeeIdAndYearAndMonth(employee.getId(), year, month);

        try {
            return arrearSalaryList.stream().mapToDouble(ArrearSalary::getAmount).sum();
        } catch (Exception ex) {
            log.info("arrear pf exception occurred for employee :" + employee.getPin());
            return 0;
        }
    }

    // employee contribution = company contribution
    // provident fund = per day basic salary * fraction days(31 in full month of 31 days) * pf percent
    // or pf= perMonthPayableBasic * .10
    // if month = joining month => pay/cut full
    // if month = resigning month => pay/cut 0
    private double mockPfContribution(EmployeeSalary employeeSalary) {
        // Data Prep Layer
        Employee employee = employeeSalary.getEmployee();
        // basic validations
        if (
            employee.getEmployeeCategory().equals(EmployeeCategory.CONTRACTUAL_EMPLOYEE) ||
            employee.getEmployeeCategory().equals(EmployeeCategory.INTERN) ||
            employee.getEmployeeCategory().equals(EmployeeCategory.PART_TIME) ||
            employee.getEmployeeCategory().equals(EmployeeCategory.CONSULTANTS)
        ) {
            return 0d;
        }

        // full month leave without pay / fraction days ==0 ===> pf will be zero
        if (employeeSalary.getFractionDays().equals(0)) {
            return 0d;
        }

        int salMonth = Month.fromEnum(employeeSalary.getMonth());
        int salYear = employeeSalary.getYear();
        int lengthOfSalMonth = LocalDate.of(salYear, salMonth, 1).lengthOfMonth();

        // pf- arrears consideration
        List<PfArrear> pfArrearList = pfArrearRepository.findByEmployeeIdAndYearAndMonth(
            employee.getId(),
            salYear,
            employeeSalary.getMonth()
        );

        double pfArrear = pfArrearList.stream().mapToDouble(PfArrear::getAmount).sum();

        /// resigning employee ( not probational )
        /// resigning month == this month ==> no pf this month (only if not worked full month )
        Optional<EmployeeResignation> employeeResignationOptional = resignationProcessingService.getResignation(employee.getId());

        if (employeeResignationOptional.isPresent()) {
            int lastWorkingDay = employeeResignationOptional.get().getLastWorkingDay().getDayOfMonth();
            int lastWorkingMonth = employeeResignationOptional.get().getLastWorkingDay().getMonth().getValue();
            int lastWorkingYear = employeeResignationOptional.get().getLastWorkingDay().getYear();

            // last month
            if (salYear == lastWorkingYear && salMonth == lastWorkingMonth) {
                // isFullMonthWorked ?
                if (lengthOfSalMonth == lastWorkingDay) {
                    if (employee.getEmployeeCategory() == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE) {
                        return getMockPfForRPE(employee, employeeSalary, salYear, salMonth, pfArrear);
                    } else {
                        return getPfForRCE(employeeSalary, pfArrear);
                    }
                } else {
                    // not full month worked
                    return 0 + pfArrear;
                }
            }
        }

        /// employee currently is in probation
        if (employee.getEmployeeCategory() == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE) {
            return getMockPfForRPE(employee, employeeSalary, salYear, salMonth, pfArrear);
        }

        // default for regular confirmed employee
        return getPfForRCE(employeeSalary, pfArrear);
    }

    private double getMockPfForRPE(Employee employee, EmployeeSalary employeeSalary, int salaryYear, int salaryMonth, double pfArrear) {
        LocalDate doc = employee.getDateOfConfirmation();
        LocalDate salaryMonthStart = LocalDate.of(salaryYear, salaryMonth, 1);
        LocalDate salaryMonthEnd = LocalDate.of(salaryYear, salaryMonth, salaryMonthStart.lengthOfMonth());
        LocalDate maxDate = LocalDate.MAX;
        // doc is between salary month start and end && doc is after salary month end
        // start month
        if (DateUtil.isBetweenOrEqual(doc, salaryMonthStart, salaryMonthEnd) || DateUtil.isSmallerThenOrEqual(salaryMonthEnd, doc)) {
            return MathRoundUtil.round((employeeSalary.getMainGrossBasicSalary() * PF_PERCENT_FROM_BASIC) + pfArrear);
        } else {
            return MathRoundUtil.round(pfArrear);
        }
    }

    private double getPfForRCE(EmployeeSalary employeeSalary, double pfArrear) {
        return MathRoundUtil.round((employeeSalary.getMainGrossBasicSalary() * PF_PERCENT_FROM_BASIC) + pfArrear);
    }
}
