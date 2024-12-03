package com.bits.hr.service.incomeTaxManagement.TaxReportGeneration;

import com.bits.hr.domain.AitConfig;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.AitConfigRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.IncomeTaxChallanService;
import com.bits.hr.service.OrganizationService;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.IncomeTaxChallanDTO;
import com.bits.hr.service.dto.OrganizationDTO;
import com.bits.hr.service.incomeTaxManagement.IncomeTaxCalculatePerMonth;
import com.bits.hr.service.incomeTaxManagement.IncomeTaxDataPreparationService;
import com.bits.hr.service.incomeTaxManagement.helperMethods.CalculateTaxQueryConfig;
import com.bits.hr.service.incomeTaxManagement.model.TaxQueryConfig;
import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.*;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import com.bits.hr.service.salaryGenerationFractional.FractionalSalaryGenerationServiceSingleUnit;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class IncomeTaxReportService {

    @Autowired
    private AitConfigRepository aitConfigRepository;

    @Autowired
    private IncomeTaxDataPreparationService incomeTaxDataPreparationService;

    @Autowired
    private IncomeTaxCalculatePerMonth incomeTaxCalculatePerMonth;

    @Autowired
    private IncomeTaxChallanService incomeTaxChallanService;

    @Autowired
    private FractionalSalaryGenerationServiceSingleUnit fractionalSalaryGenerationServiceSingleUnit;

    @Autowired
    private EmployeeMinimalMapper employeeMinimalMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OrganizationService organizationService;

    public IncomeTaxReportDTO generateTaxReport(Employee employee, Long aitConfigId) {
        AitConfig aitConfig = aitConfigRepository.findById(aitConfigId).get();

        LocalDate startDate = aitConfig.getStartDate();
        LocalDate endDate = aitConfig.getEndDate();

        String tinNumber = employee.getTinNumber();
        TaxQueryConfig taxQueryConfig = CalculateTaxQueryConfig.getConfigforTaxReport(startDate, endDate);

        List<EmployeeSalary> employeeSalaryList = getSalaryListById(employee.getId(), taxQueryConfig);

        employeeSalaryList = getSortedList(employeeSalaryList);
        TaxCalculationDTO taxCalculationDTO = new TaxCalculationDTO(employee);
        //.....................................
        if (employeeSalaryList.size() == 0) {
            log.error("No employee salary found for" + employee.getPin() + "::" + employee.getFullName());
        }

        EmployeeSalary lastMonthSalary = employeeSalaryList.get(0);
        double monthlyIncomeTax = incomeTaxCalculatePerMonth.getTax(lastMonthSalary, taxCalculationDTO);

        IncomeTaxReportDTO incomeTaxReportDto = new IncomeTaxReportDTO();
        incomeTaxReportDto.setPin(taxCalculationDTO.getPin());
        incomeTaxReportDto.setName(taxCalculationDTO.getName());
        incomeTaxReportDto.setTinNumber(tinNumber);
        incomeTaxReportDto.setDesignation(taxCalculationDTO.getDesignation());
        incomeTaxReportDto.setDepartment(taxCalculationDTO.getDepartment());
        incomeTaxReportDto.setDateOfJoining(taxCalculationDTO.getDateOfJoining().toString());
        incomeTaxReportDto.setIncomeYearStart(taxCalculationDTO.getIncomeTaxData().getTaxQueryConfig().getIncomeYearStart());
        incomeTaxReportDto.setIncomeYearEnd(taxCalculationDTO.getIncomeTaxData().getTaxQueryConfig().getIncomeYearEnd());
        incomeTaxReportDto.setAssessmentYearStart(taxCalculationDTO.getIncomeTaxData().getTaxQueryConfig().getIncomeYearStart() + 1);
        incomeTaxReportDto.setAssessmentYearEnd(taxCalculationDTO.getIncomeTaxData().getTaxQueryConfig().getIncomeYearStart() + 2);
        incomeTaxReportDto.setTaxReportConfigurations(taxCalculationDTO.getTaxReportConfigurations());
        incomeTaxReportDto.setHasConsolidatedExemption(taxCalculationDTO.isHasConsolidatedExemption());

        incomeTaxReportDto.setMaxAllowedInvestment(taxCalculationDTO.getMaxAllowedInvestment());
        incomeTaxReportDto.setOtherInvestment(
            taxCalculationDTO.getMaxAllowedInvestment() - taxCalculationDTO.getPerYearPfEmployerContribution() * 2
        );
        incomeTaxReportDto.setProvidentFundInvestment(taxCalculationDTO.getPerYearPfEmployerContribution() * 2);

        if (
            taxCalculationDTO.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE ||
            taxCalculationDTO.getEmployeeCategory() == EmployeeCategory.INTERN
        ) {
            incomeTaxReportDto.setContactPeriodEndDate(taxCalculationDTO.getContactPeriodEndDate().toString());
        } else {
            incomeTaxReportDto.setDateOfConfirmation(taxCalculationDTO.getDateOfConfirmation().toString());
        }

        List<EmployeeSalary> previousSalaryList = taxCalculationDTO.getIncomeTaxData().getPreviousSalaryList();
        //        incomeTaxReportDto.setPreviousSalaryList();

        List<TaxLiability> taxLiabilityList = taxCalculationDTO.getTaxLiability();
        incomeTaxReportDto.setTaxLiabilities(taxLiabilityList);

        // summation of income slabs
        double summationOfIncomeSlabs = 0;
        for (TaxLiability taxLiability : taxLiabilityList) {
            summationOfIncomeSlabs += taxLiability.getSlab();
        }
        incomeTaxReportDto.setSummationOfIncomeSlabs(summationOfIncomeSlabs);

        incomeTaxReportDto.setSalaryIncomes(taxCalculationDTO.getSalaryIncomeList());
        incomeTaxReportDto.setTotalSalaryIncome(taxCalculationDTO.getTotalSalaryIncome());

        //        List<Double> grandTotalsList = getGrandTotals(taxCalculationDTO);

        /*       incomeTaxReportDto.setGrandTotalSalary(grandTotalsList.get(0));
        incomeTaxReportDto.setGrandTotalExemption(grandTotalsList.get(1));
        incomeTaxReportDto.setGrandTotalTaxableIncome(grandTotalsList.get(2));*/

        incomeTaxReportDto.setTotalTaxLiabilities(
            MathRoundUtil.round(taxCalculationDTO.getTaxLiability().stream().mapToDouble(TaxLiability::getTax).sum())
        );

        incomeTaxReportDto.setMaxAllowedInvestment(taxCalculationDTO.getMaxAllowedInvestment());
        incomeTaxReportDto.setRebate(taxCalculationDTO.getRebate());

        incomeTaxReportDto.setNetTaxLiability(MathRoundUtil.round(taxCalculationDTO.getNetTaxLiability()));
        incomeTaxReportDto.setLastYearAdjustment(MathRoundUtil.round(taxCalculationDTO.getLastYearAdjustment()));
        double totalDeduction =
            Math.round(taxCalculationDTO.getPreviousTaxDeduction().stream().mapToDouble(Double::doubleValue).sum()) +
            (double) MathRoundUtil.round(lastMonthSalary.getTaxDeduction()) +
            // new logic -- reason: tax generation strategy changed for resigning employees
            // we are looking at the past , no generated value to be entertained
            // + (double) MathRoundUtil.round(taxCalculationDTO.getCurrentTaxDeduction()) -- removed previous block
            (double) MathRoundUtil.round(taxCalculationDTO.getIncomeTaxData().getTaxCutFromIndividualArrears());

        incomeTaxReportDto.setDeductedAmount((int) totalDeduction);

        int value =
            incomeTaxReportDto.getNetTaxLiability() - incomeTaxReportDto.getLastYearAdjustment() - incomeTaxReportDto.getDeductedAmount();
        incomeTaxReportDto.setRefundable(value);

        //String incomeTaxChallan = incomeTaxChallanService.getIncomeTaxChallanForIncomeTaxStatement(aitConfigId);
        //incomeTaxReportDto.setIncomeTaxChallan(incomeTaxChallan);

        List<IncomeTaxChallanDTO> incomeTaxChallanList = incomeTaxChallanService.getIncomeTaxChallanListByAitConfigId(aitConfigId);
        incomeTaxReportDto.setIncomeTaxChallanList(incomeTaxChallanList);

        OrganizationDTO organizationDTO = organizationService.getOrganizationDetails();

        incomeTaxReportDto.setSignatoryPersonSignature(organizationDTO.getFinanceManagerSignature());
        incomeTaxReportDto.setSignatoryPersonName(organizationDTO.getFinanceManagerName());
        incomeTaxReportDto.setSignatoryPersonDesignation(organizationDTO.getFinanceManagerDesignation());

        return incomeTaxReportDto;
    }

    List<EmployeeSalary> getSalaryListById(Long id, TaxQueryConfig taxQueryConfig) {
        return incomeTaxDataPreparationService.getPreviousSalaries(id, taxQueryConfig);
    }

    //    List<Double> getGrandTotals(TaxCalculationDTO taxCalculationDTO) {
    //        List<SalaryIncome> salIncList = taxCalculationDTO.getSalaryIncomeList();
    //        double grandTotalSalary = MathRoundUtil.round(salIncList.stream().mapToDouble(SalaryIncome::getSalary).sum());
    //        double grandTotalExemption = MathRoundUtil.round(salIncList.stream().mapToDouble(SalaryIncome::getExemption).sum());
    //        double grandTotalTaxableIncome = MathRoundUtil.round(salIncList.stream().mapToDouble(SalaryIncome::getTaxableIncome).sum());
    //        List<Double> grandTotalList = Arrays.asList(grandTotalSalary, grandTotalExemption, grandTotalTaxableIncome);
    //        return grandTotalList;
    //    }

    //    List<SalaryIncome> getHeadSalaryDetails(TaxCalculationDTO taxCalculationDTO) {
    //
    //
    //        List<SalaryIncome> salIncList = taxCalculationDTO.getSalaryIncomeList();
    //        SalaryIncome performanceBonus = new SalaryIncome();
    //        performanceBonus.setHead("PERFORMANCE BONUS");
    //        performanceBonus.setSubHead("");
    //        performanceBonus.setSalary(0D);
    //        performanceBonus.setExemption(0D);
    //        performanceBonus.setTaxableIncome(0D);
    //
    //        SalaryIncome incentive = new SalaryIncome();
    //        incentive.setHead("INCENTIVE");
    //        incentive.setSubHead("");
    //        incentive.setSalary(0D);
    //        incentive.setExemption(0D);
    //        incentive.setTaxableIncome(0D);
    ///*        salIncList.add(5, performanceBonus);
    //        salIncList.add(6, incentive);*/
    //
    //        return salIncList;
    //    }

    //    public int getTaxDeducted(List<EmployeeSalary> previousSalaryList) {
    //        int sum = 0;
    //        for (int i = 0; i < previousSalaryList.size(); i++) {
    //            sum += MathRoundUtil.round(previousSalaryList.get(i).getTaxDeduction());
    //        }
    //        return sum;
    //    }

    public List<EmployeeSalary> getSortedList(List<EmployeeSalary> employeeSalaryList) {
        employeeSalaryList.sort((s1, s2) -> {
            if (s1.getYear() - s2.getYear() == 0) {
                return (Month.fromEnum(s2.getMonth()) - Month.fromEnum(s1.getMonth()));
            } else {
                return s2.getYear() - s1.getYear();
            }
        });
        return employeeSalaryList;
    }

    public List<IncomeTaxDropDownMenuDto> getAitConfigYears() {
        List<AitConfig> aitConfigList = aitConfigRepository.findAll();
        List<IncomeTaxDropDownMenuDto> dropDownMenuList = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        for (int i = 0; i < aitConfigList.size(); i++) {
            LocalDate taxConfigEndDate = aitConfigList.get(i).getEndDate();
            int taxYear = taxConfigEndDate.getYear();
            if (currentYear == taxYear && currentDate.isBefore(taxConfigEndDate)) {
                continue;
            } else if (taxYear > currentYear) {
                continue;
            }
            IncomeTaxDropDownMenuDto incomeTaxDropDownMenuDto = new IncomeTaxDropDownMenuDto();
            incomeTaxDropDownMenuDto.setId(aitConfigList.get(i).getId());
            incomeTaxDropDownMenuDto.setRange(
                aitConfigList.get(i).getStartDate().getYear() + " - " + aitConfigList.get(i).getEndDate().getYear()
            );
            dropDownMenuList.add(incomeTaxDropDownMenuDto);
        }
        return dropDownMenuList;
    }

    public List<EmployeeMinimalDTO> getAllActiveEmployeeListWithinTaxYear(Long aitConfigId) {
        Optional<AitConfig> aitConfig = aitConfigRepository.findById(aitConfigId);

        if (!aitConfig.isPresent()) {
            throw new BadRequestAlertException("Ait Config Not Found", "AitConfig", "idnull");
        }

        List<Employee> employeeList = employeeRepository.getAllActiveEmployeeInFiscalYear(
            aitConfig.get().getStartDate().getYear(),
            aitConfig.get().getEndDate().getYear()
        );

        return employeeMinimalMapper.toDto(employeeList);
    }

    public IncomeTaxDropDownMenuDto getAssessmentYearByAitConfigId(Long aitConfigId) {
        Optional<AitConfig> aitConfig = aitConfigRepository.findById(aitConfigId);

        int startDate;
        int endDate;
        IncomeTaxDropDownMenuDto incomeTaxDropDownMenuDto = new IncomeTaxDropDownMenuDto();
        if (aitConfig.isPresent()) {
            startDate = aitConfig.get().getStartDate().plusYears(1L).getYear();
            endDate = aitConfig.get().getEndDate().plusYears(1L).getYear();
            incomeTaxDropDownMenuDto.setId(aitConfig.get().getId());
            incomeTaxDropDownMenuDto.setRange(startDate + "-" + endDate);
        } else {
            throw new RuntimeException("No AitConfig Found!!");
        }
        return incomeTaxDropDownMenuDto;
    }
}
