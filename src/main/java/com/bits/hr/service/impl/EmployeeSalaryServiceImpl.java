package com.bits.hr.service.impl;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EventType;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.*;
import com.bits.hr.security.SecurityUtils;
import com.bits.hr.service.EmployeeSalaryService;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.dto.EmployeeSalaryPayslipDto;
import com.bits.hr.service.mapper.EmployeeMapper;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import com.bits.hr.service.mapper.EmployeeSalaryMapper;
import com.bits.hr.service.salaryGenerationFractional.Fraction;
import com.bits.hr.service.salaryGenerationFractional.FractionService;
import com.bits.hr.service.salaryGenerationFractional.PfCollectionService;
import com.bits.hr.service.selecteable.SelectableDTO;
import com.bits.hr.service.selecteable.SelectableGenerationService;
import com.bits.hr.service.utility.EmployeeSalaryUtilService;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import com.bits.hr.util.CommonUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmployeeSalary}.
 */
@Log4j2
@Service
@Transactional
public class EmployeeSalaryServiceImpl implements EmployeeSalaryService {

    private final EmployeeSalaryRepository employeeSalaryRepository;
    private final EmployeeMinimalMapper employeeMinimalMapper;
    private final EmployeeRepository employeeRepository;
    private final EmploymentHistoryRepository employmentHistoryRepository;
    private final EmployeeSalaryMapper employeeSalaryMapper;
    private final FractionService fractionService;
    private final EmployeeMapper employeeMapper;
    private final PfCollectionRepository pfCollectionRepository;
    private final HoldSalaryDisbursementRepository holdSalaryDisbursementRepository;
    private final EmployeeSalaryUtilService employeeSalaryUtilService;
    private final EmployeeService employeeService;
    private final PfCollectionService pfCollectionService;

    public EmployeeSalaryServiceImpl(
        EmployeeSalaryRepository employeeSalaryRepository,
        EmploymentHistoryRepository employmentHistoryRepository,
        EmployeeSalaryMapper employeeSalaryMapper,
        FractionService fractionService,
        EmployeeMinimalMapper employeeMinimalMapper,
        EmployeeMapper employeeMapper,
        EmployeeRepository employeeRepository,
        PfCollectionRepository pfCollectionRepository,
        HoldSalaryDisbursementRepository holdSalaryDisbursementRepository,
        EmployeeSalaryTempDataRepository employeeSalaryTempDataRepository,
        EmployeeSalaryUtilService employeeSalaryUtilService,
        EmployeeService employeeService,
        PfCollectionService pfCollectionService
    ) {
        this.employeeSalaryRepository = employeeSalaryRepository;
        this.employeeMinimalMapper = employeeMinimalMapper;
        this.employeeRepository = employeeRepository;
        this.employmentHistoryRepository = employmentHistoryRepository;
        this.employeeSalaryMapper = employeeSalaryMapper;
        this.fractionService = fractionService;
        this.employeeMapper = employeeMapper;
        this.employeeSalaryUtilService = employeeSalaryUtilService;
        this.pfCollectionRepository = pfCollectionRepository;
        this.holdSalaryDisbursementRepository = holdSalaryDisbursementRepository;
        this.employeeService = employeeService;
        this.pfCollectionService = pfCollectionService;
    }

    @Autowired
    private SelectableGenerationService selectableGenerationService;

    @Override
    public EmployeeSalaryDTO save(EmployeeSalaryDTO employeeSalaryDTO) {
        log.debug("Request to save EmployeeSalary : {}", employeeSalaryDTO);
        EmployeeSalary employeeSalary = employeeSalaryMapper.toEntity(employeeSalaryDTO);
        employeeSalary = employeeSalaryRepository.save(employeeSalary);
        return employeeSalaryMapper.toDto(employeeSalary);
    }

    @Override
    public EmployeeSalary save(EmployeeSalary employeeSalary) {
        return employeeSalaryRepository.save(employeeSalary);
    }

    /**
     * Action Before update
     * ** pf collection
     *
     * Action After Update
     * nothing
     * Action on Update Fail
     * ** pf collection
     * */
    @Override
    public EmployeeSalaryDTO update(EmployeeSalaryDTO employeeSalaryDTO) {
        EmployeeSalary employeeSalary = employeeSalaryMapper.toEntity(employeeSalaryDTO);
        return employeeSalaryMapper.toDto(update(employeeSalary));
    }

    @Override
    public EmployeeSalary update(EmployeeSalary employeeSalary) {
        EmployeeSalary existingEmployeeSalary = employeeSalaryMapper.toEntity(this.findOne(employeeSalary.getId()).get());

        EmployeeDTO employee = employeeService
            .findOne(employeeSalary.getEmployee().getId())
            .orElseThrow(() ->
                new BadRequestAlertException(
                    "Employee Not Found, Employee Id =" + employeeSalary.getEmployee().getId(),
                    "employeeSalary",
                    "employeeNotFound"
                )
            );

        if (employee.isIsTaxPaidByOrganisation() != null && employee.isIsTaxPaidByOrganisation()) {
            employeeSalary.setTotalDeduction(
                employeeSalary.getPfDeduction() +
                employeeSalary.getWelfareFundDeduction() +
                employeeSalary.getOtherDeduction() +
                employeeSalary.getMobileBillDeduction()
            );
        } else {
            employeeSalary.setTotalDeduction(
                employeeSalary.getPfDeduction() +
                employeeSalary.getTaxDeduction() +
                employeeSalary.getWelfareFundDeduction() +
                employeeSalary.getOtherDeduction() +
                employeeSalary.getMobileBillDeduction()
            );
        }

        employeeSalary.setPayableGrossSalary(
            employeeSalary.getPayableGrossBasicSalary() +
            employeeSalary.getPayableGrossHouseRent() +
            employeeSalary.getPayableGrossMedicalAllowance() +
            employeeSalary.getPayableGrossConveyanceAllowance()
        );

        employeeSalary.setNetPay(
            employeeSalary.getPayableGrossSalary() +
            employeeSalary.getOtherAddition() +
            employeeSalary.getSalaryAdjustment() +
            employeeSalary.getArrearSalary() -
            employeeSalary.getTotalDeduction()
        );

        employeeSalary.setIsVisibleToEmployee(existingEmployeeSalary.getIsVisibleToEmployee());

        employeeSalary.setUpdatedAt(Instant.now());
        employeeSalary.setUpdatedBy(SecurityUtils.getCurrentUserEmail());
        EmployeeSalary updatedEmployeeSalary = this.save(employeeSalary);
        pfCollectionService.alignPfCollectionWithEmployeeSalary(updatedEmployeeSalary);
        return updatedEmployeeSalary;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeSalaryDTO> findAllByYearAndMonth(int year, int month, String searchText, Pageable pageable) {
        log.debug("Request to get YEAR - month based EmployeeSalaries");
        return employeeSalaryRepository
            .findEmployeeSalaryByYearAndMonth(year, Month.fromInteger(month), searchText, pageable)
            .map(employeeSalaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeSalaryDTO> findByEmployeeIdAndYearAndMonth(long employeeId, int year, int month) {
        log.debug("Request to get YEAR - month based EmployeeSalaries");
        List<EmployeeSalary> employeeSalary = employeeSalaryRepository.findByEmployeeIdAndYearAndMonth(
            employeeId,
            year,
            Month.fromInteger(month)
        );
        return employeeSalaryMapper.toDto(employeeSalary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeSalary> findByEmployeeAndYearAndMonth(long employeeId, int year, int month) {
        return employeeSalaryRepository.findByEmployeeIdAndYearAndMonth(employeeId, year, Month.fromInteger(month));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeSalary> findAllByYearAndMonth(int year, int month) {
        return employeeSalaryRepository.findEmployeeSalaryByYearAndMonth(year, Month.fromInteger(month));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeSalaryDTO> findOne(Long id) {
        log.debug("Request to get EmployeeSalary : {}", id);
        return employeeSalaryRepository.findById(id).map(employeeSalaryMapper::toDto);
    }

    /**
     *  before delete
     * 1. Check Is Eligible to delete ??
     * 2. delete pf collection data
     *
     * after delete
     * Noting till now
     *
     * if delete failed
     * 1. Repopulate PF data [ otherwise handle it with transactions ]
     *
     * */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmployeeSalary : {}", id);
        EmployeeSalary employeeSalary = employeeSalaryRepository.findById(id).get();
        holdSalaryDisbursementRepository.deleteBySalaryId(id);
        employeeSalaryRepository.deleteById(id);
    }

    @Override
    public void delete(List<Long> idList) {
        for (Long id : idList) {
            delete(id);
        }
    }

    @Override
    public void deleteByYearAndMonth(int year, int month) {
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findEmployeeSalaryByYearAndMonth(year, Month.fromInteger(month));
        for (EmployeeSalary employeeSalary : employeeSalaryList) {
            delete(employeeSalary.getId());
        }
    }

    @Override
    public EmployeeSalaryPayslipDto getSalaryPayslipByYearAndMonthForUser(Employee employee, int year, int month) {
        try {
            List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findByEmployeeIdAndYearAndMonth(
                employee.getId(),
                year,
                Month.fromInteger(month)
            );
            if (employeeSalaryList.isEmpty()) {
                return employeeSalaryUtilService.dummySalaryPayslip();
            } else if (!employeeSalaryList.get(0).getIsVisibleToEmployee()) {
                throw new RuntimeException(
                    "Salary for month " + Month.fromInteger(month) + ", " + year + " is not accessible for user yet."
                );
            } else {
                return getSalaryPayslip(employee, employeeSalaryList.get(0));
            }
        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException("Salary for month " + Month.fromInteger(month) + ", " + year + " is not accessible for user yet.");
        }
    }

    @Override
    public EmployeeSalaryPayslipDto getSalaryPayslipByEmployeeAndYearAndMonth(Employee employee, int year, int month) {
        try {
            List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findByEmployeeIdAndYearAndMonth(
                employee.getId(),
                year,
                Month.fromInteger(month)
            );
            if (employeeSalaryList.size() == 0) {
                return employeeSalaryUtilService.dummySalaryPayslip();
            } else {
                return getSalaryPayslip(employee, employeeSalaryList.get(0));
            }
        } catch (Exception ex) {
            log.error(ex);
            return employeeSalaryUtilService.dummySalaryPayslip();
        }
    }

    private EmployeeSalaryPayslipDto getSalaryPayslip(Employee employee, EmployeeSalary employeeSalaryList) {
        EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryMapper.toDto(employeeSalaryList);
        double grossPay = employeeSalaryDTO.getPayableGrossSalary() != null ? employeeSalaryDTO.getPayableGrossSalary() : 0d;
        double arrear = employeeSalaryDTO.getArrearSalary() != null ? employeeSalaryDTO.getArrearSalary() : 0d;
        double livingAllowance = employeeSalaryDTO.getAllowance01() != null ? employeeSalaryDTO.getAllowance01() : 0d;
        //            double festivalBonus = employeeSalaryDTOS.get(0).getFestivalBonus() != null ? employeeSalaryDTOS.get(0).getFestivalBonus() : 0d;
        //            double otherAddition = employeeSalaryDTOS.get(0).getOtherAddition() != null ? employeeSalaryDTOS.get(0).getOtherAddition() : 0d;
        //            double salaryAdjustment = employeeSalaryDTOS.get(0).getSalaryAdjustment() != null ? employeeSalaryDTOS.get(0).getSalaryAdjustment() : 0d;

        double totalAddition = grossPay + arrear + livingAllowance;
        employeeSalaryDTO.setTotalAddition(totalAddition);

        double netPayable = employeeSalaryDTO.getNetPay() != null ? employeeSalaryDTO.getNetPay() : 0d;
        netPayable = netPayable + livingAllowance;
        employeeSalaryDTO.setNetPay(netPayable);

        EmployeeSalaryPayslipDto employeeSalaryPayslipDto = new EmployeeSalaryPayslipDto();
        employeeSalaryPayslipDto.setEmployee(employeeMapper.toDto(employee));
        employeeSalaryPayslipDto.setEmployeeSalary(employeeSalaryDTO);

        return employeeSalaryPayslipDto;
    }

    // todo: code done in bad approach, must be changed
    @Override
    public HashSet<Integer> getSelectableYearsByEmployee(Employee employee) {
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAllByEmployeeId(employee.getId());
        HashSet<Integer> years = new HashSet<>();
        employeeSalaryList.forEach(employeeSalary -> {
            years.add(employeeSalary.getYear());
        });
        return years;
    }

    @Override
    public List<SelectableDTO> getAllSelectableListByEmployeeAndYear(Employee employee, int year) {
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAllByEmployeeIdAndYear(employee.getId(), year);

        List<SelectableDTO> selectableDTOList = new ArrayList<>();

        employeeSalaryList.forEach(employeeSalary -> {
            String monthName = employeeSalary.getMonth().name();
            int monthInInteger = Month.fromEnum(employeeSalary.getMonth());
            String monthNameProperCase = monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
            SelectableDTO selectableDTO = new SelectableDTO(monthInInteger, monthNameProperCase);
            selectableDTOList.add(selectableDTO);
        });
        // sort by key = Month (int)
        selectableDTOList.sort(Comparator.comparing(SelectableDTO::getKey));

        return selectableDTOList;
    }

    @Override
    public List<SelectableDTO> getVisibleSelectableListByEmployeeAndYear(Employee employee, int year) {
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAllVisibleByEmployeeIdAndYear(employee.getId(), year);

        List<SelectableDTO> selectableDTOList = new ArrayList<>();
        employeeSalaryList.forEach(employeeSalary -> {
            String monthName = employeeSalary.getMonth().name();
            int monthInInteger = Month.fromEnum(employeeSalary.getMonth());
            String monthNameProperCase = monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
            SelectableDTO selectableDTO = new SelectableDTO(monthInInteger, monthNameProperCase);
            selectableDTOList.add(selectableDTO);
        });
        // sort by key = Month (int)
        selectableDTOList.sort(Comparator.comparing(SelectableDTO::getKey));
        return selectableDTOList;
    }

    @Override
    public List<Fraction> getFractions(Long id) {
        Optional<EmployeeSalary> employeeSalaryOptional = employeeSalaryRepository.findById(id);
        if (!employeeSalaryOptional.isPresent()) return new ArrayList<Fraction>();
        EmployeeSalary employeeSalary = employeeSalaryOptional.get();
        Employee employee = employeeSalary.getEmployee();

        int year = employeeSalary.getYear();
        int month = employeeSalary.getMonth().ordinal() + 1;
        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth();

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, daysInMonth);

        // sort by date
        List<EmploymentHistory> employmentHistories = employmentHistoryRepository
            .findAllByEmployeeAndEffectiveDateBetween(employee, startDate, endDate)
            .stream()
            .filter(x -> x.getEventType() == EventType.INCREMENT || x.getEventType() == EventType.PROMOTION)
            .sorted(Comparator.comparing(EmploymentHistory::getEffectiveDate))
            .collect(Collectors.toList());

        return fractionService.getFractions(employee, startDate, endDate, employmentHistories);
    }

    @Override
    public List<EmployeeMinimalDTO> getAllEmployeesExceptInterns() {
        List<Employee> employeeList = employeeRepository.getAllEmployeeExceptInterns();
        return employeeMinimalMapper.toDto(employeeList);
    }

    @Override
    public boolean changeHoldStatusTo(String holdStatus, EmployeeSalary employeeSalary) {
        try {
            if (holdStatus.equals("hold")) {
                employeeSalary.setIsHold(true);
            } else {
                employeeSalary.setIsHold(false);
            }
            employeeSalaryRepository.save(employeeSalary);

            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    @Override
    public ExportXLPropertiesDTO prepareDataForSalaryExport(int startYear, int startMonth, int endYear, int endMonth) {
        try {
            LocalDate startDate = LocalDate.of(startYear, startMonth, 1);
            LocalDate endDate = LocalDate.of(endYear, endMonth, 1);

            HashSet<Employee> employeeHashSet = new HashSet<>();

            List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAllSalaryBetweenYears(startYear, endYear);

            // Filter the results based on the given startMonth and endMonth
            employeeSalaryList =
                employeeSalaryList
                    .stream()
                    .filter(salary -> {
                        int month = Month.fromEnum(salary.getMonth());

                        if (startYear <= salary.getYear() && salary.getYear() <= endYear) {
                            if (startYear == endYear) {
                                if (startMonth <= month && month <= endMonth) {
                                    return true;
                                } else {
                                    return false;
                                }
                            } else if (salary.getYear() == startYear && month >= startMonth) {
                                return true;
                            } else if (salary.getYear() == endYear && month <= endMonth) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

            employeeSalaryList.sort((s1, s2) -> {
                if (s2.getYear() - s1.getYear() == 0) {
                    return Month.fromEnum(s2.getMonth()) - Month.fromEnum(s1.getMonth());
                } else {
                    return s2.getYear() - s1.getYear();
                }
            });

            for (EmployeeSalary employeeSalary : employeeSalaryList) {
                employeeHashSet.add(employeeSalary.getEmployee());
            }

            List<Employee> employees = employeeHashSet
                .stream()
                .sorted(Comparator.comparing((Employee::getPin)))
                .collect(Collectors.toList());
            String sheetName = "Salary Report within month range";
            List<String> titleList = new ArrayList<>();

            List<String> subTitleList = new ArrayList<>();
            LocalDate today = LocalDate.now();
            subTitleList.add(
                "Salary report from " +
                startDate.getMonth() +
                ", " +
                startDate.getYear() +
                " to " +
                endDate.getMonth() +
                ", " +
                endDate.getYear()
            );
            subTitleList.add("Report Generated On " + today.toString());

            List<String> tableHeaderList = new ArrayList<>();
            tableHeaderList.add("S/L");
            tableHeaderList.add("Ref Pin");
            tableHeaderList.add("Pin");
            tableHeaderList.add("Name");
            tableHeaderList.add("Year");
            tableHeaderList.add("Month");
            tableHeaderList.add("Joining Date");
            tableHeaderList.add("Confirmation Date/Contract End Date");
            tableHeaderList.add("Employee Category");
            tableHeaderList.add("Unit");
            tableHeaderList.add("Department");
            tableHeaderList.add("Main Gross Salary");
            tableHeaderList.add("Basic");
            tableHeaderList.add("House Rent");
            tableHeaderList.add("Medical");

            tableHeaderList.add("Conveyance");
            tableHeaderList.add("Absent Days");
            tableHeaderList.add("Fraction Days");
            tableHeaderList.add("Payable Gross Salary");

            tableHeaderList.add("Payable Gross Basic");
            tableHeaderList.add("Payable Gross House Rent");
            tableHeaderList.add("Payable Gross Medical");
            tableHeaderList.add("Payable Gross Conveyance");

            tableHeaderList.add("Arrear Salary");
            tableHeaderList.add("PF Deduction");
            tableHeaderList.add("Tax Deduction");

            tableHeaderList.add("Mobile Bill Deduction");
            tableHeaderList.add("Other Deduction");
            tableHeaderList.add("Total Deduction");
            tableHeaderList.add("Net Pay");

            tableHeaderList.add("PF Contribution");
            tableHeaderList.add("Gratuity Fund Contribution");
            tableHeaderList.add("Provision for Festival Bonus");
            tableHeaderList.add("Provision For Leave Encashment");
            tableHeaderList.add("Provision For Project Bonus");
            tableHeaderList.add("Living Allowance");
            tableHeaderList.add("Car Allowance");
            tableHeaderList.add("House Rent Reimbursement");
            tableHeaderList.add("Company Secretary");

            List<List<Object>> dataList = new ArrayList<>();

            if (employees.size() == 0) {
                List<Object> data = new ArrayList<>();
                for (int i = 0; i < 39; i++) {
                    data.add("-");
                }
                dataList.add(data);
            }

            int count = 1;
            for (EmployeeSalary employeeSalary : employeeSalaryList) {
                List<Object> data = new ArrayList<>();
                data.add(count++);
                data.add(employeeSalary.getEmployee().getReferenceId());
                data.add(employeeSalary.getPin());
                data.add(employeeSalary.getEmployee().getFullName());
                data.add(employeeSalary.getYear());
                data.add(Month.fromEnum(employeeSalary.getMonth()));
                data.add(employeeSalary.getEmployee().getDateOfJoining());
                data.add(CommonUtil.getConfirmationDateOrContractEndDate(employeeSalary.getEmployee()));
                data.add(EmployeeCategory.employeeCategoryEnumToNaturalText(employeeSalary.getEmployee().getEmployeeCategory()));
                data.add(employeeSalary.getEmployee().getUnit().getUnitName());
                data.add(employeeSalary.getEmployee().getDepartment().getDepartmentName());
                data.add(employeeSalary.getMainGrossSalary());
                data.add(employeeSalary.getMainGrossBasicSalary());
                data.add(employeeSalary.getMainGrossHouseRent());
                data.add(employeeSalary.getMainGrossMedicalAllowance());
                data.add(employeeSalary.getMainGrossConveyanceAllowance());
                data.add(employeeSalary.getAbsentDays());
                data.add(employeeSalary.getFractionDays());
                data.add(employeeSalary.getPayableGrossSalary());
                data.add(employeeSalary.getPayableGrossBasicSalary());
                data.add(employeeSalary.getPayableGrossHouseRent());
                data.add(employeeSalary.getPayableGrossMedicalAllowance());
                data.add(employeeSalary.getPayableGrossConveyanceAllowance());
                data.add(employeeSalary.getArrearSalary());
                data.add(employeeSalary.getPfDeduction());
                data.add(employeeSalary.getTaxDeduction());
                data.add(employeeSalary.getMobileBillDeduction());
                data.add(employeeSalary.getOtherDeduction());
                data.add(employeeSalary.getTotalDeduction());
                data.add(employeeSalary.getNetPay());
                data.add(employeeSalary.getPfContribution());
                data.add(employeeSalary.getGfContribution());
                data.add(employeeSalary.getProvisionForFestivalBonus());
                data.add(employeeSalary.getProvisionForLeaveEncashment());
                data.add(employeeSalary.getProvisionForProjectBonus());
                data.add(employeeSalary.getAllowance01());
                data.add(employeeSalary.getAllowance02());
                data.add(employeeSalary.getAllowance03());
                data.add(employeeSalary.getAllowance04());

                dataList.add(data);
            }

            ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

            exportXLPropertiesDTO.setSheetName(sheetName);
            exportXLPropertiesDTO.setTitleList(titleList);
            exportXLPropertiesDTO.setSubTitleList(subTitleList);
            exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
            exportXLPropertiesDTO.setTableDataListOfList(dataList);
            exportXLPropertiesDTO.setHasAutoSummation(false);
            exportXLPropertiesDTO.setAutoSizeColumnUpTo(38);
            return exportXLPropertiesDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
