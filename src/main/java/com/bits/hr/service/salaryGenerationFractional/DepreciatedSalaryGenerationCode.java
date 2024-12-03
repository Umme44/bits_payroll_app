package com.bits.hr.service.salaryGenerationFractional;

//import com.bits.hr.domain.*;
//import com.bits.hr.domain.enumeration.EmployeeCategory;
//import com.bits.hr.domain.enumeration.EventType;
//import com.bits.hr.domain.enumeration.Month;
//import com.bits.hr.errors.BadRequestAlertException;
//import com.bits.hr.repository.*;
//import com.bits.hr.service.approvalProcess.SalaryLockService;
//import com.bits.hr.service.config.CurrentEmployeeService;
//import com.bits.hr.service.dto.PfLoanRepaymentDTO;
//import com.bits.hr.service.incomeTaxManagement.IncomeTaxCalculatePerMonth;
//import com.bits.hr.service.incomeTaxManagement.TaxReportGeneration.TaxReport;
//import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.TaxCalculationDTO;
//import com.bits.hr.service.salaryGeneration.FestivalBonusServiceCustom;
//import com.bits.hr.service.salaryGeneration.GratuityFundService;
//import com.bits.hr.util.DateUtil;
//import com.bits.hr.util.MathRoundUtil;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static com.bits.hr.service.salaryGenerationFractional.SalaryConstants.*;

//@Service
//@Log4j2
public class DepreciatedSalaryGenerationCode {
    //
    //    @Autowired
    //    private ProvidentFundDeductionService providentFundDeductionService;
    //
    //    @Autowired
    //    private ExcessCellBillService excessCellBillService;
    //
    //    @Autowired
    //    private IncomeTaxCalculatePerMonth incomeTaxCalculatePerMonth;
    //
    //    @Autowired
    //    private FractionService fractionService;
    //
    //    @Autowired
    //    private HafDeductionService hafDeductionService;
    //
    //    @Autowired
    //    private EmployeeRepository employeeRepository;
    //
    //    @Autowired
    //    private AttendanceSummaryRepository attendanceSummaryRepository;
    //
    //    @Autowired
    //    private GratuityFundService gratuityFundService;
    //
    //    @Autowired
    //    private FestivalBonusServiceCustom festivalBonusServiceCustom;
    //
    //    @Autowired
    //    private EmploymentHistoryRepository employmentHistoryRepository;
    //
    //    @Autowired
    //    private ArrearSalaryRepository arrearSalaryRepository;
    //
    //    @Autowired
    //    private AllowanceService allowanceService;
    //
    //    @Autowired
    //    private SalaryHoldService salaryHoldService;
    //
    //    @Autowired
    //    private PfCollectionService pfCollectionService;
    //
    //    @Autowired
    //    private CurrentEmployeeService currentEmployeeService;
    //
    //    @Autowired
    //    private CustomExecutorService customExecutorService;
    //
    //    @Autowired
    //    private OtherDeductionsService otherDeductionsService;
    //
    //    @Autowired
    //    private SalaryLockService salaryLockService;
    //
    //
    //    @Autowired
    //    private EmployeeSalaryRepository employeeSalaryRepository;
    //
    //    @Autowired
    //    private ApplicationEventPublisher applicationEventPublisher;
    //
    //
    //    @Deprecated
    //    public Optional<EmployeeSalary> generateMonthlySalary(int year,
    //                                                          int month,
    //                                                          String pin,
    //                                                          LocalDate startDate,
    //                                                          LocalDate endDate) {
    //        try {
    //            if (salaryLockService.isLocked(String.valueOf(year), String.valueOf(month))) {
    //                throw new BadRequestAlertException(" Regeneration locked!! ", "Payroll Management", "entryLocked");
    //            }
    //            if (!(employeeRepository.findEmployeeByPin(pin).isPresent()) && employeeRepository.findEmployeeByPin(pin).get().getMainGrossSalary() != null) {
    //                return Optional.empty();
    //            }
    //
    //            List<EmployeeSalary> existingDuplicateEmployeeSalaries = employeeSalaryRepository.findAllByPinAndYearAndMonth(pin, year, Month.fromInteger(month));
    //            boolean isVisibleToEmployee = false;
    //            if (existingDuplicateEmployeeSalaries.size() > 0 && existingDuplicateEmployeeSalaries.get(0).isIsVisibleToEmployee() == true) {
    //                isVisibleToEmployee = true;
    //            }
    //            List<EmploymentHistory> employmentHistoryList
    //                = employmentHistoryRepository
    //                .findAllByEmployeeAndEffectiveDateBetween(
    //                    employeeRepository
    //                        .findEmployeeByPin(pin).get(), startDate, endDate)
    //                .stream()
    //                .filter(x -> x.getEventType() == EventType.PROMOTION || x.getEventType() == EventType.INCREMENT)
    //                .collect(Collectors.toList());
    //
    //            Employee employee = employeeRepository.findEmployeeByPin(pin).get();
    //
    //            EmployeeSalary salary = new EmployeeSalary();
    //            salary.setIsVisibleToEmployee(isVisibleToEmployee);
    //
    //            salary.setEmployee(employee);
    //
    //            salary.setPin(employee.getPin().trim());
    //
    //            if (employee.getReferenceId() != null) {
    //                salary.setRefPin(employee.getReferenceId().trim());
    //            } else {
    //                salary.setRefPin("");
    //            }
    //
    //            salary.setMonth(Month.fromInteger(month));
    //
    //            salary.setYear(year);
    //
    //            salary.setJoiningDate(employee.getDateOfJoining());
    //
    //            if (employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE || employee.getEmployeeCategory() == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE) {
    //                salary.setConfirmationDate(employee.getDateOfConfirmation());
    //            }
    //            if (employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE || employee.getEmployeeCategory() == EmployeeCategory.INTERN) {
    //                if (employee.getContractPeriodExtendedTo() == null) {
    //                    salary.setConfirmationDate(employee.getContractPeriodEndDate());
    //                } else {
    //                    salary.setConfirmationDate(employee.getContractPeriodExtendedTo());
    //                }
    //
    //            }
    //
    //
    //            salary.setEmployeeCategory(employee.getEmployeeCategory());
    //            // set category as intern if intern
    //            if (employee.getDesignation() != null && employee.getDesignation().getDesignationName() != null && employee.getDesignation().getDesignationName().toLowerCase(Locale.ROOT).trim().equals("intern")) {
    //                salary.setEmployeeCategory(EmployeeCategory.INTERN);
    //            }
    //
    //            salary.setSalaryGenerationDate(LocalDate.now());
    //
    //            if (employee.getUnit() != null && employee.getUnit().getUnitName() != null) {
    //                salary.setUnit(employee.getUnit().getUnitName());
    //            } else {
    //                salary.setUnit(" - ");
    //            }
    //
    //            Department department;
    //            if (employee.getDepartment() != null) {
    //                department = employee.getDepartment();
    //                String DeptName = department.getDepartmentName();
    //                salary.setDepartment(DeptName);
    //            } else {
    //                salary.setDepartment(" - ");
    //            }
    //
    //            // gross - basic - house rent - medical convenience
    //            // main will be taken from recent value
    //            double mGross = MathRoundUtil.round(employee.getMainGrossSalary());
    //            double mBasic = MathRoundUtil.round(mGross * BASIC_PERCENT);   //60%
    //            double mHouse = MathRoundUtil.round(mGross * HOUSE_RENT_PERCENT);   //30%
    //            double mMedical = MathRoundUtil.round(mGross * MEDICAL_PERCENT);    //6%
    //
    //            //double mConveyance  = mGross * .04;    //4%
    //            double mConveyance = MathRoundUtil.round(mGross - (mBasic + mHouse + mMedical));
    //
    //            salary.setMainGrossSalary(mGross);
    //            salary.setMainGrossBasicSalary(mBasic);
    //            salary.setMainGrossHouseRent(mHouse);
    //            salary.setMainGrossMedicalAllowance(mMedical);
    //            salary.setMainGrossConveyanceAllowance(mConveyance);
    //
    //            int daysInmonth = LocalDate.of(year, month, 1).lengthOfMonth();
    //            int fractionDays = daysInmonth;
    //
    //            if (attendanceSummaryRepository.findByYearAndMonthAndAndEmployeePin(year, month, employee.getPin()).isPresent()) {
    //                fractionDays = attendanceSummaryRepository
    //                    .findByYearAndMonthAndAndEmployeePin(year, month, employee.getPin()).get()
    //                    .getTotalFractionDays();
    //                if (fractionDays > daysInmonth) {
    //                    fractionDays = daysInmonth;
    //                }
    //            }
    //
    //            YearMonth yearMonthObject = YearMonth.of(year, month);
    //            int daysInMonth = yearMonthObject.lengthOfMonth();
    //
    //            int absentDays = daysInMonth - fractionDays;
    //
    //            salary.setAbsentDays(absentDays);
    //            salary.setFractionDays(fractionDays);
    //
    //            // gross => basic - house rent - medical- convenience
    //            /*
    //             * fractional salary generation main logic will go here
    //             *-step 1: get date range last month 25 to this month 24 for attendance regularization ( non xlsx )
    //             * step 2: get date range this month 1 to this month 30/31 for attendance regularization ( with xlsx upload )
    //             * step 3: adjust start date if joining is between.
    //             * step 4: get leave without pay from xlsx upload  if not available then from service
    //             * step 5: get promotion and increment history between start date and end date
    //             * step 6: now get sorted list of << dates, effective salary on that date >> , start date - promotion effective date 1 - promotion effective date 2 - increment effective date 1 - end date.
    //             * step 7: now loop through date and calculate perDaySalary*numberOfDays .
    //             * step 8: get sum of perDaySalary and generate rest of the salary from that value.
    //             * */
    //
    //            // take only salary changing entry , no need to consider unnecessary details where salary will have no impact.
    //            List<EmploymentHistory> employmentHistoryListSalaryChange = employmentHistoryList.stream()
    //                .filter(employmentHistory -> !employmentHistory.getCurrentMainGrossSalary().equals(employmentHistory.getPreviousMainGrossSalary()))
    //                .collect(Collectors.toList());
    //
    //
    //            double pGross;
    //            double pBasic;
    //            double pHouse;
    //            double pMedical;
    //            double pConveyance;
    //
    //            // no salary change in between , so no need to generate fractional salary
    //            if (employmentHistoryList.isEmpty()) {
    //                pGross = MathRoundUtil.round((mGross / daysInMonth) * fractionDays);
    //                pBasic = MathRoundUtil.round(pGross * BASIC_PERCENT);   //60%
    //                pHouse = MathRoundUtil.round(pGross * HOUSE_RENT_PERCENT);   //30%
    //                pMedical = MathRoundUtil.round(pGross * MEDICAL_PERCENT);    //6%
    //
    //                //double mConveyance  = mGross * .04;    //4%
    //                pConveyance = MathRoundUtil.round(pGross - (pBasic + pHouse + pMedical));
    //
    //
    //                salary.setPayableGrossSalary((double) MathRoundUtil.round(pGross));
    //
    //                salary.setPayableGrossBasicSalary(pBasic);
    //                salary.setPayableGrossHouseRent(pHouse);
    //                salary.setPayableGrossMedicalAllowance(pMedical);
    //                salary.setPayableGrossConveyanceAllowance(pConveyance);
    //            } else {
    //                double fractionalGross;
    //                // double perDaySalary = mGross / daysInMonth;
    //                employmentHistoryListSalaryChange.sort(Comparator.comparing(EmploymentHistory::getEffectiveDate)); // sort by date
    //
    //                // start date will not be handled in this method
    //                // set of data needed like << startDate, endDate, effectiveSalary >>
    //
    //                List<Fraction> fractionsSet
    //                    = fractionService
    //                    .getFractions(employee, startDate, endDate, new ArrayList<>(employmentHistoryListSalaryChange));
    //
    //                fractionalGross = SalaryUtils.getPayableFractionalGross(fractionsSet);
    //                // first range -> start date to first empHis date
    //
    //                pGross = (fractionalGross / daysInMonth) * fractionDays;
    //                pBasic = MathRoundUtil.round(pGross * BASIC_PERCENT);   //60%
    //                pHouse = MathRoundUtil.round(pGross * HOUSE_RENT_PERCENT);   //30%
    //                pMedical = MathRoundUtil.round(pGross * MEDICAL_PERCENT);    //6%
    //
    //                //double mConveyance  = mGross * .04;    //4%
    //                pConveyance = MathRoundUtil.round(pGross - (pBasic + pHouse + pMedical));
    //
    //                salary.setPayableGrossSalary((double) MathRoundUtil.round(pGross));
    //                salary.setPayableGrossBasicSalary(pBasic);
    //                salary.setPayableGrossHouseRent(pHouse);
    //                salary.setPayableGrossMedicalAllowance(pMedical);
    //                salary.setPayableGrossConveyanceAllowance(pConveyance);
    //            }
    //
    //
    //        /*
    //            wfd also known as HAF
    //            welfare fund will be taken from band
    //            if band doesn't have wfd then take it from employee table
    //            band will have much priority in this case
    //            HAF is not applicable for contractual employee ***
    //         */
    //            double hafDeduction = hafDeductionService.hafDeduction(salary);
    //            salary.setWelfareFundDeduction(hafDeduction);
    //
    //            double mobileBillDeduction = excessCellBillService.getExcessCellBillDeduction(employee, year, month);
    //            salary.setMobileBillDeduction(mobileBillDeduction);
    //
    //
    //            double pfDeduction = providentFundDeductionService.providentFundContribution(salary);
    //            salary.setPfDeduction(pfDeduction);
    //            // provision
    //            salary.setPfContribution(pfDeduction);
    //
    //
    //            // todo: pf loan repayment + may other deduction will come from other deduction service.
    //            // todo: var name should be changed according to new criteria
    //            // todo : new remarks section needed for other deductions.
    //            double pfLoanRepayment = otherDeductionsService.getOtherDeductions(employee.getId(), year, month);
    //
    //            double otherDeduction = 0.0 + pfLoanRepayment;
    //            salary.setOtherDeduction(otherDeduction);
    //
    //
    //            LocalDate calculationDate = LocalDate.of(year, month, 25);
    //            double gratuityFund = 0.0;
    //            if (employee.getEmployeeCategory() == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE || employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
    //                gratuityFund = MathRoundUtil.round(gratuityFundService.calculateGratuityFundPerMonth(employee.getDateOfJoining(), calculationDate, mBasic));
    //            }
    //
    //            salary.setGfContribution(gratuityFund);
    //
    //            Integer numberOfServiceDays = fractionDays; // ( per month for R-P-E employee)
    //            double provisionForFestivalBonus = 0.0d;
    //
    //            if (employee.getDesignation() != null && !employee.getDesignation().getDesignationName().trim().toLowerCase().equals("intern")) {
    //                provisionForFestivalBonus = MathRoundUtil.round(festivalBonusServiceCustom.calculateMonthlyProvisionsForFestivalBonus(mGross, employee.getEmployeeCategory(), numberOfServiceDays));
    //            }
    //
    //            salary.setProvisionForFestivalBonus(provisionForFestivalBonus);
    //
    //            Double leaveEncashment = SalaryUtils.getLeaveEncashment(employee);
    //
    //            salary.setProvisionForLeaveEncashment(leaveEncashment);
    //
    //            // Double provisionForProjectBonus = 0.0;
    //            // salary.setProvishionForProjectBonus(provisionForProjectBonus);
    //
    //
    //            // get this month's arrear salary , if any ***
    //            double arrear = getArrear(employee, year, Month.fromInteger(month));
    //            double arrearPf = 0.0d;
    //
    //            salary.setArrearSalary(arrear);
    //            salary.setProvidentFundArrear(arrearPf);
    //
    //            // allowances
    //            Allowance allowance = allowanceService.getAllowance(employee, month, year);
    //            salary.setAllowance01(allowance.getAllowance01());
    //            salary.setAllowance02(allowance.getAllowance02());
    //            salary.setAllowance03(allowance.getAllowance03());
    //            salary.setAllowance04(allowance.getAllowance04());
    //            salary.setAllowance05(allowance.getAllowance05());
    //            salary.setAllowance06(allowance.getAllowance06());
    //            // allowances
    //
    //            // is hold determination
    //            salary.setIsHold(
    //                salaryHoldService.isSalaryHold(
    //                    employee.getId(),
    //                    year,
    //                    month
    //                )
    //            );
    //
    //            // temporary value
    //            salary.setTaxDeduction(0d);
    //
    //            // tax report tracked from here
    //            TaxCalculationDTO taxCalculationDTO = new TaxCalculationDTO(employee);
    //
    //            double incomeTaxPerMonth = MathRoundUtil.round(incomeTaxCalculatePerMonth.getTax(salary, taxCalculationDTO));
    //
    //
    //            salary.setTaxCalculationSnapshot(TaxReport.generateTextBasedTaxReport(taxCalculationDTO));
    //
    //            double totalDeduction;
    //            if (employee.isIsTaxPaidByOrganisation() != null && employee.isIsTaxPaidByOrganisation()) {
    //                totalDeduction = MathRoundUtil.round(pfDeduction + hafDeduction + mobileBillDeduction + otherDeduction);
    //            } else {
    //                totalDeduction = MathRoundUtil.round(pfDeduction + hafDeduction + mobileBillDeduction + otherDeduction + incomeTaxPerMonth);
    //            }
    //
    //            salary.setTotalDeduction(totalDeduction);
    //
    //            salary.setTaxDeduction(incomeTaxPerMonth);
    //
    //            // initially set salary adjustment to 0
    //            double salaryAdjustment = 0d;
    //            salary.setSalaryAdjustment(salaryAdjustment);
    //
    //            double netPay = MathRoundUtil.round(((pGross + arrear) - totalDeduction) + salaryAdjustment);
    //            salary.setNetPay(netPay);
    //
    //            // remarks = pf load deduction if there are pf loan deduction available
    //            String remarks = "";
    //            if (salary.isIsHold()) remarks += " Hold, FNF \n";
    //            if (pfLoanRepayment != 0d) remarks += " Deduction for PF Loan \n";
    //            if (DateUtil.isBetween(employee.getDateOfJoining(), startDate.minusDays(1), endDate.plusDays(1)))
    //                remarks += " New Joiner \n";
    //            if (remarks.length() == 0) remarks = " - ";
    //            salary.setRemarks(remarks);
    //
    //            salary.setCreatedAt(Instant.now());
    //            if (currentEmployeeService.getCurrentUserEmail().isPresent()) {
    //                salary.setCreatedBy(currentEmployeeService.getCurrentUserEmail().get());
    //            }
    //
    //            customExecutorService.executorService.submit(
    //                () -> {
    //                    pfCollectionService
    //                        .createOrUpdatePfCollections(
    //                            salary,
    //                            employee,
    //                            salary.getPfContribution(),
    //                            salary.getPfDeduction(),
    //                            month,
    //                            salary.getYear(),
    //                            salary.getMainGrossSalary(),
    //                            salary.getMainGrossBasicSalary()
    //                        );
    //                }
    //            );
    //
    //            // prepare pfLoanRepaymentDTO for event publishing
    //            PfLoanRepaymentDTO pfLoanRepaymentDTO = new PfLoanRepaymentDTO();
    //            pfLoanRepaymentDTO.setAmount(pfLoanRepayment);
    //            pfLoanRepaymentDTO.setAccHolderName(employee.getFullName());
    //            pfLoanRepaymentDTO.setPin(employee.getPin());
    //            //publishEvent(pfLoanRepaymentDTO, com.bits.hr.service.event.EventType.CREATED);
    //
    //            return Optional.of(salary);
    //        } catch (Exception ex) {
    //            log.error(ex);
    //            return Optional.empty();
    //        }
    //    }
    //
    //
    //    private double getArrear(Employee employee, int year, Month month) {
    //        // arrear salary
    //        // arrear payment
    //        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository
    //            .findByEmployeeIdAndYearAndMonth(
    //                employee.getId(),
    //                year,
    //                month
    //            );
    //
    //        try {
    //            return arrearSalaryList.stream().mapToDouble(ArrearSalary::getAmount).sum();
    //        } catch (Exception ex) {
    //            log.info("arrear exception occurred for employee :{} , exception: {}", employee.getPin(), ex);
    //            return 0;
    //        }
    //    }
}
