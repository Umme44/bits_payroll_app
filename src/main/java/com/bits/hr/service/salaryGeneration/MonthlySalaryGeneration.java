package com.bits.hr.service.salaryGeneration;

import com.bits.hr.domain.Department;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.SalaryGeneratorMaster;
import com.bits.hr.domain.Unit;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.*;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.mapper.EmployeeMapper;
import com.bits.hr.util.MathRoundUtil;
import com.bits.hr.util.SalaryGenerationMasterService;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MonthlySalaryGeneration {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceSummaryRepository attendanceSummaryRepository;

    @Autowired
    private MobileBillRepository mobileBillRepository;

    @Autowired
    private GratuityFundService gratuityFundService;

    @Autowired
    private FestivalBonusServiceCustom festivalBonusServiceCustom;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    private SalaryGenerationMasterService salaryGenerationMasterService;

    @Autowired
    private SalaryGeneratorMasterRepository salaryGeneratorMasterRepository;

    @Autowired
    private UnitRepository unitRepository;

    public Boolean generateMonthlySalaryforAll(int year, int month) {
        ArrayList<EmployeeSalary> salaryList = new ArrayList<>();
        try {
            // if exist , delete previous records
            // set existing isMobileBillUpload to false.
            // update salary generation master
            SalaryGeneratorMaster salaryGeneratorMaster;
            if (salaryGenerationMasterService.isExistSalaryGeneratorMaster(year, month)) {
                employeeSalaryRepository.deleteAllByYearAndMonth(year, Month.fromInteger(month));
                salaryGeneratorMaster = salaryGenerationMasterService.getOrCreateSalaryGeneratorMaster(year, month);
                salaryGeneratorMaster.setIsGenerated(false); // if regenaration fails ; false will stay
                salaryGeneratorMasterRepository.save(salaryGeneratorMaster);
            }

            ArrayList<EmployeeDTO> employees = new ArrayList<>(employeeService.findAll(Pageable.unpaged()).getContent());

            for (EmployeeDTO employee : employees) {
                EmployeeSalary salary = new EmployeeSalary();

                salary.setPin(employee.getPin());
                salary.setRefPin(employee.getReferenceId());
                salary.setMonth(Month.fromInteger(month));
                salary.setYear(year);
                salary.setJoiningDate(employee.getDateOfJoining());
                salary.setConfirmationDate(employee.getDateOfConfirmation());
                salary.setEmployeeCategory(employee.getEmployeeCategory());
                salary.setSalaryGenerationDate(LocalDate.now());

                //salary.setUnit(employee.getUnit());
                Optional<Department> department = departmentRepository.findById(employee.getDepartmentId());
                String name = department.orElseThrow(RuntimeException::new).getDepartmentName();

                salary.setDepartment(name);
                Optional<Unit> unit = unitRepository.findById(employee.getUnitId());
                String unitName = unit.orElseThrow(RuntimeException::new).getUnitName();
                salary.setUnit(unitName);

                //            salary.setDepartment(
                //                department.orElseThrow(
                //                                () -> new RuntimeException("No Employee found by this PIN")
                //                )
                //        );
                // gross - basic - house rent - medical convenience
                double mGross = (double) MathRoundUtil.round(employee.getMainGrossSalary());
                double mBasic = (double) MathRoundUtil.round(mGross * .60); //60%
                double mHouse = (double) MathRoundUtil.round(mGross * .30); //30%
                double mMedical = (double) MathRoundUtil.round(mGross * .06); //6%

                //double mConveyance  = mGross * .04;    //4%
                double mConveyance = (double) MathRoundUtil.round(mGross - (mBasic + mHouse + mMedical));

                salary.setMainGrossSalary(mGross);
                salary.setMainGrossBasicSalary(mBasic);
                salary.setMainGrossHouseRent(mHouse);
                salary.setMainGrossMedicalAllowance(mMedical);
                salary.setMainGrossConveyanceAllowance(mConveyance);

                int fractionDays = 0;
                if (attendanceSummaryRepository.findByYearAndMonthAndAndEmployeePin(year, month, employee.getPin()).isPresent()) {
                    fractionDays =
                        attendanceSummaryRepository
                            .findByYearAndMonthAndAndEmployeePin(year, month, employee.getPin())
                            .get()
                            .getTotalFractionDays();
                }

                YearMonth yearMonthObject = YearMonth.of(year, month);
                int daysInMonth = yearMonthObject.lengthOfMonth();

                int absentDays = daysInMonth - fractionDays;

                salary.setAbsentDays(absentDays);
                salary.setFractionDays(fractionDays);

                // gross => basic - house rent - medical- convenience

                double pGross = (mGross / daysInMonth) * fractionDays;
                double pBasic = (double) MathRoundUtil.round(pGross * .60); //60%
                double pHouse = (double) MathRoundUtil.round(pGross * .30); //30%
                double pMedical = (double) MathRoundUtil.round(pGross * .06); //6%

                //double mConveyance  = mGross * .04;    //4%
                double pConveyance = (double) MathRoundUtil.round(pGross - (pBasic + pHouse + pMedical));

                salary.setPayableGrossSalary((double) MathRoundUtil.round(pGross));

                salary.setPayableGrossBasicSalary(pBasic);
                salary.setPayableGrossHouseRent(pHouse);
                salary.setPayableGrossMedicalAllowance(pMedical);
                salary.setPayableGrossConveyanceAllowance(pConveyance);

                salary.setArrearSalary(0.0);

                /*
            double pfDeduction =MathRoundUtil.round(
                providentFundService.calculateProvidentFund(pGross,fractionDays,month,year, employee.getDateOfConfirmation()));
            */
                double pfDeduction = mBasic * .10;
                if (employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
                    //double pfDeduction = providentFundService.calculateProvidentFund(pGross,fractionDays,month,year, employee.getDateOfConfirmation());
                    salary.setPfDeduction(pfDeduction);
                } else {
                    pfDeduction = 0.0;
                    salary.pfDeduction(pfDeduction);
                }

                double taxDeduction = 0.0;
                double welfareFundDeduction = employee.getWelfareFundDeduction();

                salary.setTaxDeduction(taxDeduction);
                salary.setWelfareFundDeduction(welfareFundDeduction);

                double mobileLimit = 0;
                double mobileSpent = 0;
                if (employee.getMobileCelling() != null) {
                    mobileLimit = employee.getMobileCelling();
                }
                if (!mobileBillRepository.findByYearAndMonthAndAndEmployeePin(year, month, employee.getPin()).isEmpty()) {
                    mobileSpent =
                        mobileBillRepository.findByYearAndMonthAndAndEmployeePin(year, month, employee.getPin()).get(0).getAmount();
                }

                double mobileBillDeduction = 0.0;

                if (mobileLimit >= mobileSpent) {
                    mobileBillDeduction = 0.0;
                } else {
                    mobileBillDeduction = mobileSpent - mobileLimit;
                }

                salary.setMobileBillDeduction((double) MathRoundUtil.round(mobileBillDeduction));

                double otherDeduction = 0.0;

                salary.setOtherDeduction(otherDeduction);

                double totalDeduction = (double) MathRoundUtil.round(
                    pfDeduction + taxDeduction + welfareFundDeduction + mobileBillDeduction + otherDeduction
                );

                salary.setTotalDeduction(totalDeduction);

                double netPay = MathRoundUtil.round(pGross - totalDeduction);
                salary.setNetPay(netPay);
                String remarks = "None";
                salary.setRemarks(remarks);

                // provishion
                salary.setPfContribution(pfDeduction);

                LocalDate calculationDate = LocalDate.of(year, month, 25); // need correction
                double gratuityFund = 0;
                if (
                    employee.getEmployeeCategory() != null && employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE
                ) {
                    gratuityFund =
                        MathRoundUtil.round(
                            gratuityFundService.getProvisionForGratuityFundPerMonth(employee.getDateOfJoining(), calculationDate, mBasic)
                        );
                }

                salary.setGfContribution(gratuityFund);

                Integer numberOfServiceDays = fractionDays; // ( per month for R-P-E employee)
                double provisionForFestivalBonus = MathRoundUtil.round(
                    festivalBonusServiceCustom.calculateMonthlyProvisionsForFestivalBonus(
                        mGross,
                        employee.getEmployeeCategory(),
                        numberOfServiceDays
                    )
                );

                salary.setProvisionForFestivalBonus(provisionForFestivalBonus);

                double leaveEncashment = 0.0;
                if (employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
                    leaveEncashment = (mGross / daysInMonth) * (13.0 / 12.0);
                } else {
                    leaveEncashment = 0.0;
                }

                salary.setProvisionForLeaveEncashment((double) MathRoundUtil.round(leaveEncashment));

                //                Double provishionForProjectBonus = 0.0;
                //                salary.setProvishionForProjectBonus(provishionForProjectBonus);

                salary.setEmployee(employeeMapper.toEntity(employee));
                salaryList.add(salary);
            }
        } catch (Exception e) {
            log.debug(e);
            return false;
        }

        for (EmployeeSalary salary : salaryList) {
            employeeSalaryRepository.save(salary);
        }
        // if new , create  and set true isMobileBillUploaded
        // if old , get     and set true isMobileBillUploaded

        SalaryGeneratorMaster salaryGeneratorMaster;
        salaryGeneratorMaster = salaryGenerationMasterService.getOrCreateSalaryGeneratorMaster(year, month);
        salaryGeneratorMaster.setIsGenerated(true);
        salaryGeneratorMasterRepository.save(salaryGeneratorMaster);

        return true;
    }
}
