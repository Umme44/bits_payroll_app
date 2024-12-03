package com.bits.hr.service.finalSettlement;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.repository.FinalSettlementRepository;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.service.dto.FinalSettlementDTO;
import com.bits.hr.service.finalSettlement.dto.AbsentDaysAdjustment;
import com.bits.hr.service.finalSettlement.dto.LeaveEncashment;
import com.bits.hr.service.finalSettlement.dto.NoticePay;
import com.bits.hr.service.finalSettlement.dto.PfGfStatement;
import com.bits.hr.service.finalSettlement.dto.salary.*;
import com.bits.hr.service.finalSettlement.dto.salary.SalaryDeduction;
import com.bits.hr.service.finalSettlement.helperMethods.FinalSettlementIncomeTaxService;
import com.bits.hr.service.finalSettlement.helperMethods.LeaveProcessForFinalSettlementService;
import com.bits.hr.service.finalSettlement.helperMethods.NoticePeriodService;
import com.bits.hr.service.finalSettlement.helperMethods.ResignationProcessingService;
import com.bits.hr.service.finalSettlement.util.ServiceTenure;
import com.bits.hr.service.mapper.FinalSettlementMapper;
import com.bits.hr.service.salaryGeneration.config.IncomeTaxConfig;
import com.bits.hr.service.salaryGenerationFractional.SalaryConstants;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class FinalSettlementGenService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PfGfSettlementService pfGfSettlementService;

    @Autowired
    private NoticePeriodService noticePeriodService;

    @Autowired
    private ResignationProcessingService resignationProcessingService;

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    private GetConfigValueByKeyService getConfigValueByKeyService;

    @Autowired
    private LeaveProcessForFinalSettlementService leaveProcessForFinalSettlementService;

    @Autowired
    private FinalSettlementIncomeTaxService finalSettlementIncomeTaxService;

    @Autowired
    private FinalSettlementRepository finalSettlementRepository;

    @Autowired
    private FinalSettlementMapper finalSettlementMapper;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    public Optional<FinalSettlementDTO> generateAndSave(long employeeId) {
        Optional<FinalSettlementDTO> finalSettlementDTO = generateFinalSettlement(employeeId, Optional.of(LocalDate.now()));
        if (!finalSettlementDTO.isPresent()) {
            log.debug("error on final settlement generation");
            return Optional.empty();
        }
        boolean isRegeneration = false;

        Optional<LocalDate> createdAt = Optional.of(LocalDate.now());
        Optional<User> createdBy = currentEmployeeService.getCurrentUser();

        Optional<LocalDate> updatedAt = Optional.empty();
        Optional<Long> updatedBy = Optional.empty();

        // delete previously generated final settlement if exist
        List<FinalSettlement> finalSettlementList = finalSettlementRepository.findFinalSettlementByEmployeeId(employeeId);
        for (FinalSettlement finalSettlement : finalSettlementList) {
            if (finalSettlement.getCreatedBy() != null) {
                createdBy = Optional.of(finalSettlement.getCreatedBy());
            }
            if (finalSettlement.getCreatedAt() != null) {
                createdAt = Optional.of(finalSettlement.getCreatedAt());
            }
            finalSettlementRepository.delete(finalSettlement);
            isRegeneration = true;
        }
        // save newly generated FS
        if (createdBy.isPresent()) {
            finalSettlementDTO.get().setCreatedById(createdBy.get().getId());
        }
        if (createdAt.isPresent()) {
            finalSettlementDTO.get().setCreatedAt(createdAt.get());
        }
        if (isRegeneration == true) {
            finalSettlementDTO.get().setUpdatedAt(LocalDate.now());
            finalSettlementDTO.get().setUpdatedById(currentEmployeeService.getCurrentUserId().get());
        }
        return Optional.of(
            finalSettlementMapper.toDto(finalSettlementRepository.save(finalSettlementMapper.toEntity(finalSettlementDTO.get())))
        );
    }

    private Optional<FinalSettlementDTO> generateFinalSettlement(long employeeId, Optional<LocalDate> finalSettlementDateOptional) {
        FinalSettlementDTO finalSettlementDTO = new FinalSettlementDTO();

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (!employeeOptional.isPresent()) {
            log.debug("Employee Not Found with associative ID :: " + employeeId);
            return Optional.ofNullable(null);
        }
        Employee employee = employeeOptional.get();
        finalSettlementDTO.setEmployeeId(employeeId);

        LocalDate finalSettlementDate = LocalDate.now();
        if (finalSettlementDateOptional.isPresent()) {
            finalSettlementDate = finalSettlementDateOptional.get();
        }
        finalSettlementDTO.setFinalSettlementDate(finalSettlementDate);

        PfGfStatement pfGfStatement = new PfGfStatement();
        double netPfPayable = 0d;
        double netGfPayable = 0d;

        if (employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
            pfGfStatement = pfGfSettlementService.getPfGfStatement(employeeId).get();

            netGfPayable = pfGfStatement.getTotalGfPayable();
            netPfPayable = pfGfStatement.getPfDetails().getTotalPfPayable();
            // 50% pf if service tenure is less than 1 year

        }

        finalSettlementDTO.setTotalPayablePf(netPfPayable);
        finalSettlementDTO.setTotalPayableGf(netGfPayable);

        finalSettlementDTO.setMobileBillInCash(0d);

        Optional<EmployeeResignation> employeeResignationOptional = resignationProcessingService.getResignation(employeeId);
        if (!employeeResignationOptional.isPresent()) {
            log.debug("Please fix employee resignation entry");
            return Optional.ofNullable(null);
        }
        EmployeeResignation employeeResignation = employeeResignationOptional.get();

        if (employeeResignation.getLastWorkingDay() == null) {
            log.debug("Last working day not found");
            return Optional.ofNullable(null);
        }
        if (employeeResignation.getResignationDate() == null) {
            log.debug("Last working day not found");
            return Optional.ofNullable(null);
        }

        LocalDate lastWorkingDay = employeeResignation.getLastWorkingDay();
        LocalDate dateOfResignation = employeeResignation.getResignationDate();
        LocalDate dateOfRelease = lastWorkingDay.plusDays(1);

        int noticePeriodInDays = noticePeriodService.getNumberOfNoticeDays(employeeId);

        finalSettlementDTO.setDateOfResignation(dateOfResignation);
        finalSettlementDTO.setLastWorkingDay(lastWorkingDay);
        finalSettlementDTO.setNoticePeriod(noticePeriodInDays);
        finalSettlementDTO.setLastWorkingDay(lastWorkingDay);
        finalSettlementDTO.setDateOfRelease(dateOfRelease);

        // calculate serviceTenure
        TimeDuration serviceTenure = ServiceTenure.calculateTenure(employee.getDateOfJoining(), lastWorkingDay);
        finalSettlementDTO.setServiceTenure(
            serviceTenure.getYear() + " Years, " + serviceTenure.getMonth() + " Months, " + serviceTenure.getDay() + " Days"
        );

        if (employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE && employee.getDateOfConfirmation() != null) {
            TimeDuration pfServiceTenure = ServiceTenure.calculateTenure(employee.getDateOfConfirmation(), lastWorkingDay);
        }

        SalaryStructure salaryStructure = getSalaryStructure(employee.getMainGrossSalary());

        finalSettlementDTO.setmBasic(salaryStructure.getBasic());
        finalSettlementDTO.setmHouseRent(salaryStructure.getHouseRent());
        finalSettlementDTO.setmMedical(salaryStructure.getMedical());
        finalSettlementDTO.setmConveyance(salaryStructure.getConveyance());
        finalSettlementDTO.setTotalSalary(salaryStructure.getTotalSalary());
        List<EmployeeSalary> employeeHoldSalaryList = employeeSalaryRepository.getHoldSalaryByEmployeeId(employeeId);
        SalaryProcess salaryProcess = processSalaryPayable(employeeHoldSalaryList);
        finalSettlementDTO.setSalaryNumOfMonth(employeeHoldSalaryList.size());

        finalSettlementDTO.setSalaryPayable(salaryProcess.getSalaryPayable().getSalaryPayable());
        finalSettlementDTO.setSalaryPayableRemarks(salaryProcess.getSalaryPayable().getSalaryPayableRemarks());

        finalSettlementDTO.setAllowance01Amount(salaryProcess.getSalaryAddition().getAllowance01Amount());
        finalSettlementDTO.setAllowance02Amount(salaryProcess.getSalaryAddition().getAllowance02Amount());
        finalSettlementDTO.setAllowance03Amount(salaryProcess.getSalaryAddition().getAllowance03Amount());
        finalSettlementDTO.setAllowance04Amount(salaryProcess.getSalaryAddition().getAllowance04Amount());

        finalSettlementDTO.setAllowance01Name(salaryProcess.getSalaryAddition().getAllowance01Name());
        finalSettlementDTO.setAllowance02Name(salaryProcess.getSalaryAddition().getAllowance02Name());
        finalSettlementDTO.setAllowance03Name(salaryProcess.getSalaryAddition().getAllowance03Name());
        finalSettlementDTO.setAllowance04Name(salaryProcess.getSalaryAddition().getAllowance04Name());

        finalSettlementDTO.setAllowance01Remarks(salaryProcess.getSalaryAddition().getAllowance01Remarks());
        finalSettlementDTO.setAllowance02Remarks(salaryProcess.getSalaryAddition().getAllowance02Remarks());
        finalSettlementDTO.setAllowance03Remarks(salaryProcess.getSalaryAddition().getAllowance03Remarks());
        finalSettlementDTO.setAllowance04Remarks(salaryProcess.getSalaryAddition().getAllowance04Remarks());

        finalSettlementDTO.setDeductionHaf(salaryProcess.getSalaryDeduction().getHafDeduction());
        finalSettlementDTO.setDeductionPf(salaryProcess.getSalaryDeduction().getPfDeduction());
        finalSettlementDTO.setDeductionExcessCellBill(salaryProcess.getSalaryDeduction().getExcessPhoneBill());

        // calculate notice pay
        // calculate notice pay dates
        NoticePay noticePay = calculateNoticePay(employee, dateOfResignation, lastWorkingDay);
        finalSettlementDTO.setDeductionNoticePay(noticePay.getTotalNoticePayAmount());
        finalSettlementDTO.setDeductionNoticePayDays(noticePay.getNumOfDays());
        // deduction for absent days adjustment  << use service of AMS to calculate >>
        AbsentDaysAdjustment absentDaysAdjustment = leaveProcessForFinalSettlementService.calculateAbsentDaysAdjustment(
            employee,
            lastWorkingDay
        );
        finalSettlementDTO.setDeductionAbsentDaysAdjustment(absentDaysAdjustment.getAmountToDeduct());
        finalSettlementDTO.setDeductionAbsentDaysAdjustmentDays(absentDaysAdjustment.getNumberOfDays());
        finalSettlementDTO.setDeductionOther(0d);

        // leave encashment
        LeaveEncashment leaveEncashment = leaveProcessForFinalSettlementService.getLeaveEncashment(employee, lastWorkingDay);
        finalSettlementDTO.setTotalDaysForLeaveEncashment((double) leaveEncashment.getNumOfDays());
        finalSettlementDTO.setTotalLeaveEncashment(leaveEncashment.getTotalLeaveEncashmentPayAmount());

        // calculate income tax according to date of release
        double incomeTax = finalSettlementIncomeTaxService.getIncomeTax(employee, finalSettlementDTO);
        finalSettlementDTO.setDeductionAnnualIncomeTax(incomeTax);
        finalSettlementDTO.setIsFinalized(false);
        finalSettlementDTO = balanceSum(finalSettlementDTO);
        return Optional.of(finalSettlementDTO);
    }

    private SalaryStructure getSalaryStructure(double employeeSalary) {
        SalaryStructure salaryStructure = new SalaryStructure();

        double gross = employeeSalary;
        double basic = MathRoundUtil.round(gross * SalaryConstants.BASIC_PERCENT);
        double houseRent = MathRoundUtil.round(gross * SalaryConstants.HOUSE_RENT_PERCENT);
        double medical = MathRoundUtil.round(gross * SalaryConstants.MEDICAL_PERCENT);
        double conveyance = MathRoundUtil.round(gross - (basic + houseRent + medical));

        salaryStructure.setBasic(basic);
        salaryStructure.setHouseRent(houseRent);
        salaryStructure.setMedical(medical);
        salaryStructure.setConveyance(conveyance);
        salaryStructure.setTotalSalary(Math.ceil(basic + houseRent + medical + conveyance));
        salaryStructure.setEntertainment(0);
        salaryStructure.setUtility(0);

        return salaryStructure;
    }

    private SalaryProcess processSalaryPayable(List<EmployeeSalary> employeeSalaryList) {
        // List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.getHoldSalaryByEmployeeId(employeeId);
        StringBuilder remarks = new StringBuilder();
        double payableSalary = 0d;

        double sumAllowance01 = 0d;
        double sumAllowance02 = 0d;
        double sumAllowance03 = 0d;
        double sumAllowance04 = 0d;

        double pfDeduction = 0d;
        double excessCellBillDeduction = 0d;
        double hafDeduction = 0d;
        double otherDeduction = 0d;

        for (EmployeeSalary employeeSalary : employeeSalaryList) {
            payableSalary += employeeSalary.getNetPay();
            remarks.append(employeeSalary.getMonth().toString() + "-" + employeeSalary.getYear() + "  ");
            if (employeeSalary.getAllowance01() != null) {
                sumAllowance01 += employeeSalary.getAllowance01();
            }
            if (employeeSalary.getAllowance02() != null) {
                sumAllowance02 += employeeSalary.getAllowance02();
            }
            if (employeeSalary.getAllowance03() != null) {
                sumAllowance03 += employeeSalary.getAllowance03();
            }
            if (employeeSalary.getAllowance04() != null) {
                sumAllowance04 += employeeSalary.getAllowance04();
            }

            // deduction
            if (employeeSalary.getPfDeduction() != null) {
                pfDeduction += employeeSalary.getPfDeduction();
            }
            if (employeeSalary.getMobileBillDeduction() != null) {
                excessCellBillDeduction += employeeSalary.getMobileBillDeduction();
            }
            if (employeeSalary.getWelfareFundDeduction() != null) {
                hafDeduction += employeeSalary.getWelfareFundDeduction();
            }
            if (employeeSalary.getOtherDeduction() != null) {
                otherDeduction += employeeSalary.getOtherDeduction();
            }
        }

        SalaryPayable salaryPayable = new SalaryPayable();

        salaryPayable.setSalaryPayable(payableSalary);
        salaryPayable.setSalaryPayableRemarks(remarks.toString());

        SalaryAddition salaryAddition = new SalaryAddition();

        salaryAddition.setAllowance01Amount(sumAllowance01);
        salaryAddition.setAllowance02Amount(sumAllowance02);
        salaryAddition.setAllowance03Amount(sumAllowance03);
        salaryAddition.setAllowance04Amount(sumAllowance04);

        salaryAddition.setAllowance01Remarks(remarks.toString());
        salaryAddition.setAllowance02Remarks(remarks.toString());
        salaryAddition.setAllowance03Remarks(remarks.toString());
        salaryAddition.setAllowance04Remarks(remarks.toString());

        salaryAddition.setAllowance01Name(getConfigValueByKeyService.getAllowance01Name());
        salaryAddition.setAllowance02Name(getConfigValueByKeyService.getAllowance02Name());
        salaryAddition.setAllowance03Name(getConfigValueByKeyService.getAllowance03Name());
        salaryAddition.setAllowance04Name(getConfigValueByKeyService.getAllowance04Name());

        SalaryDeduction salaryDeduction = new SalaryDeduction();
        salaryDeduction.setExcessPhoneBill(excessCellBillDeduction);
        salaryDeduction.setPfDeduction(pfDeduction);
        salaryDeduction.setHafDeduction(hafDeduction);
        salaryDeduction.setOtherDeduction(otherDeduction);

        SalaryProcess salaryProcess = new SalaryProcess();
        salaryProcess.setSalaryPayable(salaryPayable);
        salaryProcess.setSalaryAddition(salaryAddition);
        salaryProcess.setSalaryDeduction(salaryDeduction);

        return salaryProcess;
    }

    private NoticePay calculateNoticePay(Employee employee, LocalDate dateOfResignation, LocalDate lastWorkingDay) {
        NoticePay noticePay = new NoticePay();
        double perDaySalary = employee.getMainGrossSalary() / 30d;

        int defaultNoticePeriodDays = getConfigValueByKeyService.getNoticePeriodInDays(employee.getEmployeeCategory());
        if (employee.getNoticePeriodInDays() != null) {
            defaultNoticePeriodDays = employee.getNoticePeriodInDays();
        }

        int daysTaken = (int) ChronoUnit.DAYS.between(dateOfResignation, lastWorkingDay.plusDays(1));

        int numOfDaysToDeduct = Math.max((defaultNoticePeriodDays - daysTaken), 0);
        noticePay.setNumOfDays(numOfDaysToDeduct);
        noticePay.setPerDayDeduction(perDaySalary);
        noticePay.setNoticeDays(defaultNoticePeriodDays);

        return noticePay;
    }

    public FinalSettlementDTO balanceSum(FinalSettlementDTO finalSettlementDTO) {
        double addition = MathRoundUtil.round(
            finalSettlementDTO.getSalaryPayable() +
            finalSettlementDTO.getTotalLeaveEncashment() +
            finalSettlementDTO.getAllowance01Amount() +
            finalSettlementDTO.getAllowance02Amount() +
            finalSettlementDTO.getAllowance03Amount() +
            finalSettlementDTO.getAllowance04Amount() +
            finalSettlementDTO.getMobileBillInCash()
        );

        finalSettlementDTO.setTotalGrossSalary(addition);

        double deduction = MathRoundUtil.round(
            finalSettlementDTO.getDeductionNoticePay() +
            finalSettlementDTO.getDeductionPf() +
            finalSettlementDTO.getDeductionHaf() +
            finalSettlementDTO.getDeductionExcessCellBill() +
            finalSettlementDTO.getDeductionAbsentDaysAdjustment()
        );
        finalSettlementDTO.setTotalDeduction(deduction);

        double salaryPayable = MathRoundUtil.round(addition - deduction);
        finalSettlementDTO.setTotalSalaryPayable(salaryPayable);

        double netSalaryPayable = MathRoundUtil.round(salaryPayable - finalSettlementDTO.getDeductionAnnualIncomeTax());
        finalSettlementDTO.setNetSalaryPayable(netSalaryPayable);

        double totalFinalSettlementAmount = MathRoundUtil.round(
            netSalaryPayable + finalSettlementDTO.getTotalPayablePf() + finalSettlementDTO.getTotalPayableGf()
        );

        finalSettlementDTO.setTotalFinalSettlementAmount(totalFinalSettlementAmount);

        return finalSettlementDTO;
    }
}
