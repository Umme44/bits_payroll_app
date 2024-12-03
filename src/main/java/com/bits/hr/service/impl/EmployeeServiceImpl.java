package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.EmploymentHistory;
import com.bits.hr.domain.RecruitmentRequisitionForm;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EventType;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeResignationRepository;
import com.bits.hr.repository.EmploymentHistoryRepository;
import com.bits.hr.repository.RecruitmentRequisitionFormRepository;
import com.bits.hr.service.DepartmentService;
import com.bits.hr.service.EmployeePinService;
import com.bits.hr.service.EmployeeResignationService;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.*;
import com.bits.hr.service.event.EmployeeRegistrationEvent;
import com.bits.hr.service.mapper.EmployeeCommonMapper;
import com.bits.hr.service.mapper.EmployeeMapper;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import com.bits.hr.util.DateUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Employee}.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final EmployeeMapper employeeMapper;

    private final EmployeeMinimalMapper employeeMinimalMapper;
    private final EmployeeCommonMapper employeeCommonMapper;
    private final EmploymentHistoryRepository employmentHistoryRepository;
    private final EmployeeResignationService employeeResignationService;
    private final EmployeePinService employeePinService;
    private final EmployeeResignationRepository employeeResignationRepository;
    private final DepartmentService departmentService;
    private final CurrentEmployeeService currentEmployeeService;
    private final RecruitmentRequisitionFormRepository recruitmentRequisitionFormRepository;

    public EmployeeServiceImpl(
        EmployeeRepository employeeRepository,
        ApplicationEventPublisher eventPublisher,
        EmployeeMapper employeeMapper,
        EmployeeCommonMapper employeeCommonMapper,
        EmployeeMinimalMapper employeeMinimalMapper,
        EmploymentHistoryRepository employmentHistoryRepository,
        EmployeeResignationRepository employeeResignationRepository,
        EmployeeResignationService employeeResignationService,
        EmployeePinService employeePinService,
        DepartmentService departmentService,
        CurrentEmployeeService currentEmployeeService,
        RecruitmentRequisitionFormRepository recruitmentRequisitionFormRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.eventPublisher = eventPublisher;
        this.employeeMapper = employeeMapper;
        this.employeeMinimalMapper = employeeMinimalMapper;
        this.employeeCommonMapper = employeeCommonMapper;
        this.employmentHistoryRepository = employmentHistoryRepository;
        this.employeeResignationRepository = employeeResignationRepository;
        this.employeeResignationService = employeeResignationService;
        this.employeePinService = employeePinService;
        this.departmentService = departmentService;
        this.currentEmployeeService = currentEmployeeService;
        this.recruitmentRequisitionFormRepository = recruitmentRequisitionFormRepository;
    }

    @Override
    public EmployeeDTO create(EmployeeDTO employeeDTO) {
        log.debug("Request to create Employee : {}", employeeDTO);
        Employee employee = employeeMapper.toEntity(employeeDTO);

        if (employeeDTO.getPin() != null) {
            employeeDTO.setPin(employeeDTO.getPin().trim());
        }

        if (employeeDTO.getReferenceId() != null) {
            employeeDTO.setReferenceId(employeeDTO.getReferenceId().trim());
        } else {
            employeeDTO.setReferenceId("");
        }

        employee.setUpdatedAt(LocalDateTime.now());
        employee = employeeRepository.save(employee);

        publishEvent(employee.getOfficialEmail(), employee.getPin());
        employeePinService.updateEmployeePinStatus(employee);

        // update RRF Total Onboard
        Optional<EmployeePinDTO> employeePin = employeePinService.findOneByPin(employee.getPin());
        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findByRrfNumber(
            employeePin.get().getRrfNumber()
        );
        if (!recruitmentRequisitionFormList.isEmpty()) {
            RecruitmentRequisitionForm recruitmentRequisitionForm = recruitmentRequisitionFormList.get(0);
            if (recruitmentRequisitionForm.getTotalOnboard() == null || recruitmentRequisitionForm.getTotalOnboard() == 0) {
                recruitmentRequisitionForm.setTotalOnboard(1);
            } else recruitmentRequisitionForm.setTotalOnboard(recruitmentRequisitionForm.getTotalOnboard() + 1);

            if (recruitmentRequisitionForm.getTotalOnboard() == recruitmentRequisitionForm.getNumberOfVacancies()) {
                recruitmentRequisitionForm.setRequisitionStatus(RequisitionStatus.CLOSED);
            }
            recruitmentRequisitionFormRepository.save(recruitmentRequisitionForm);
        }

        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeDTO update(EmployeeDTO employeeDTO) {
        log.debug("Request to update Employee : {}", employeeDTO);

        Optional<EmployeeDTO> currentEmployee = this.findOne(employeeDTO.getId());
        if (!currentEmployee.isPresent()) {
            throw new BadRequestAlertException("Employee not found", "employee", "employee");
        }

        Employee employee = employeeMapper.toEntity(employeeDTO);

        if (employee.getPin() != null) {
            employee.setPin(employee.getPin().trim());
        }

        if (employee.getReferenceId() != null) {
            employee.setReferenceId(employee.getReferenceId().trim());
        } else {
            employee.setReferenceId("");
        }

        employee.setUpdatedAt(LocalDateTime.now());
        employee.setLastWorkingDay(currentEmployee.get().getLastWorkingDay());

        employee = employeeRepository.save(employee);

        if (employee.getUser() == null) {
            publishEvent(employee.getOfficialEmail(), employee.getPin());
        }
        employeePinService.updateEmployeePinStatus(employee);

        return employeeMapper.toDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");

        return employeeRepository.findAll(pageable).map(employeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        Optional<EmployeeDTO> employeeDTO = employeeRepository.findById(id).map(employeeMapper::toDto);

        if (employeeDTO.isPresent()) {
            Optional<EmployeePinDTO> employeePinDTO = employeePinService.findOneByPin(employeeDTO.get().getPin());
            employeePinDTO.ifPresent(pinDTO -> employeeDTO.get().setRrfNumber(pinDTO.getRrfNumber()));
            return Optional.of(employeeDTO.get());
        }
        return Optional.empty();
    }

    @Override
    public EmployeeMinimalDTO findEmployeeMinimalById(Long id) {
        log.debug("Request to get Employee : {}", id);

        Optional<EmployeeMinimalDTO> employeeMinimalDTO = employeeRepository.findById(id).map(employeeMinimalMapper::toDto);
        if (!employeeMinimalDTO.isPresent()) {
            throw new BadRequestAlertException("Employee Not Found", "EmployeeMinimalDTO", "noEmployee");
        }
        return employeeMinimalDTO.get();
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.deleteById(id);
    }

    @Override
    public Optional<Employee> findEmployeeByPin(String pin) {
        return employeeRepository.findByPin(pin);
    }

    @Override
    public Optional<Employee> findEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee convertToEntity(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setPin(employeeDTO.getPin());
        employee.setFullName(employeeDTO.getFullName());
        // Set other properties if necessary
        return employee;
    }

    /**
     * "direct reporting" refers only to the immediate, one-to-one relationship
     * between a manager and their direct reports. Direct reports are the
     * employees who report directly to a particular manager or supervisor,
     * without any intermediaries.
     */
    @Override
    public List<Employee> getDirectReportingTo(Employee selectedEmployee) {
        return employeeRepository.getActiveReportingToById(selectedEmployee.getId(), LocalDate.now());
    }

    /**
     * "All reporting" encompasses all levels of the organizational hierarchy,
     * including both direct and indirect reporting relationships.
     * Indirect reports are employees who report to a manager who is not their
     * immediate supervisor.
     */
    @Override
    public List<Employee> getAllReportingTo(Employee selectedEmployee) {
        Long empId = selectedEmployee.getId();
        List<Employee> employeeList = employeeRepository.getActiveReportingToById(empId, LocalDate.now());
        List<Employee> employeeList01 = new ArrayList<>(employeeList);

        for (Employee employee : employeeList01) {
            employeeList.addAll(getAllReportingTo(employee));
            employeeList.sort(Comparator.comparing(Employee::getPin));
        }
        log.debug(employeeList.toString());
        return employeeList;
    }

    private void publishEvent(String officialEmail, String pin) {
        EmployeeRegistrationEvent event = new EmployeeRegistrationEvent(this, officialEmail, pin);
        eventPublisher.publishEvent(event);
    }

    public void createJoiningEntry(EmployeeDTO employeeDTO) {
        long employeeId = employeeDTO.getId();

        List<EmploymentHistory> joiningEntryList = employmentHistoryRepository.getAllByEmployeeIdAndEventType(employeeId, EventType.JOIN);

        if (joiningEntryList.size() > 1) {
            employmentHistoryRepository.deleteAll(joiningEntryList);
        }
        if (joiningEntryList.size() != 1) {
            Employee employee = employeeRepository.getOne(employeeId);
            EmploymentHistory employmentHistoryJoin = new EmploymentHistory();

            employmentHistoryJoin
                .employee(employee)
                .eventType(EventType.JOIN)
                .pin(employee.getPin())
                .previousDesignation(employee.getDesignation())
                .changedDesignation(employee.getDesignation())
                .previousDepartment(employee.getDepartment())
                .changedDepartment(employee.getDepartment())
                .previousUnit(employee.getUnit())
                .changedUnit(employee.getUnit())
                .previousMainGrossSalary(employee.getMainGrossSalary())
                .currentMainGrossSalary(employee.getMainGrossSalary())
                .effectiveDate(employee.getDateOfJoining());

            employmentHistoryRepository.save(employmentHistoryJoin);
        }
    }

    @Override
    public boolean isActiveInSelectedDate(Employee employee, LocalDate date) {
        DateRangeDTO serviceTenureDateRange = this.getServiceTenureDateRange(employee);
        return DateUtil.isBetweenOrEqual(date, serviceTenureDateRange.getStartDate(), serviceTenureDateRange.getEndDate());
    }

    @Override
    public EmployeeDashboardAnalyticsDTO getEmployeeDashboardAnalytics(LocalDate selectedDate) {
        EmployeeDashboardAnalyticsDTO employeeDashboardAnalyticsDTO = new EmployeeDashboardAnalyticsDTO();

        long totalEmployee = employeeRepository.count();

        // total
        employeeDashboardAnalyticsDTO.setTotalEmployee((int) totalEmployee);

        //totalActiveEmployee
        LocalDate startDate = LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), 1);
        LocalDate endDate = LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.lengthOfMonth());
        List<Employee> totalActiveEmployeeList = employeeRepository.getEligibleEmployeeForSalaryGeneration_v2(startDate, endDate);
        employeeDashboardAnalyticsDTO.setTotalActiveEmployee(totalActiveEmployeeList.size());

        int activeRegularConfirmed = 0;
        int activeRegularProbation = 0;
        int activeByContract = 0;
        int activeIntern = 0;
        for (Employee employee : totalActiveEmployeeList) {
            if (employee.getEmployeeCategory().equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
                activeRegularConfirmed++;
            } else if (employee.getEmployeeCategory().equals(EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE)) {
                activeRegularProbation++;
            } else if (employee.getEmployeeCategory().equals(EmployeeCategory.CONTRACTUAL_EMPLOYEE)) {
                activeByContract++;
            } else if (employee.getEmployeeCategory().equals(EmployeeCategory.INTERN)) {
                activeIntern++;
            }
        }

        employeeDashboardAnalyticsDTO.setActiveRegularConfirmed(activeRegularConfirmed);
        employeeDashboardAnalyticsDTO.setActiveRegularProbation(activeRegularProbation);
        employeeDashboardAnalyticsDTO.setActiveByContract(activeByContract);
        employeeDashboardAnalyticsDTO.setActiveIntern(activeIntern);

        // Inactive
        int totalInActiveEmployee =
            employeeDashboardAnalyticsDTO.getTotalEmployee() - employeeDashboardAnalyticsDTO.getTotalActiveEmployee();
        employeeDashboardAnalyticsDTO.setTotalInActiveEmployee(totalInActiveEmployee);

        //Resigned
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAll();
        employeeDashboardAnalyticsDTO.setTotalResigned(employeeResignationList.size());

        int pendingResigned = 0;
        int approvedResigned = 0;
        for (EmployeeResignation employeeResignation : employeeResignationList) {
            if (employeeResignation.getApprovalStatus() != null && employeeResignation.getApprovalStatus().equals(Status.PENDING)) {
                pendingResigned++;
            } else if (employeeResignation.getApprovalStatus() != null && employeeResignation.getApprovalStatus().equals(Status.APPROVED)) {
                approvedResigned++;
            }
        }

        employeeDashboardAnalyticsDTO.setPendingResigned(pendingResigned);
        employeeDashboardAnalyticsDTO.setApprovedResigned(approvedResigned);

        return employeeDashboardAnalyticsDTO;
    }

    @Override
    public long getTotalWorkingDays(Employee employee) {
        if (employee.getDateOfJoining() == null) {
            return -1;
        }

        LocalDate today = LocalDate.now();
        LocalDate doj = employee.getDateOfJoining();

        return ChronoUnit.DAYS.between(doj, today.plusDays(1));
    }

    @Override
    public DateRangeDTO getServiceTenureDateRange(Employee employee) {
        DateRangeDTO dateRangeDTO = new DateRangeDTO();

        if (employee.getDateOfJoining() == null) {
            throw new RuntimeException("Date of joining is missing");
        }
        LocalDate doj = employee.getDateOfJoining();
        Optional<LocalDate> lwd = employeeResignationService.getLastWorkingDay(employee.getId());
        LocalDate contractPeriodEndDate = employee.getContractPeriodEndDate();
        LocalDate contractPeriodExtendedTo = employee.getContractPeriodExtendedTo();

        dateRangeDTO.setStartDate(doj);

        boolean isFixedTermContact = employee.getIsFixedTermContract() != null ? employee.getIsFixedTermContract() : false;

        if (employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE && isFixedTermContact) {
            if (contractPeriodEndDate != null) {
                dateRangeDTO.setEndDate(contractPeriodEndDate);
            }
            if (contractPeriodExtendedTo != null) {
                dateRangeDTO.setEndDate(contractPeriodEndDate);
            }
        }

        if (lwd != null && lwd.isPresent()) {
            dateRangeDTO.setEndDate(lwd.get());
        }

        return dateRangeDTO;
    }

    @Override
    public EmployeeDTO findDeptHeadByDeptId(Long departmentId) {
        DepartmentDTO departmentDTO = departmentService
            .findOne(departmentId)
            .orElseThrow(() -> new BadRequestAlertException("Department not found by ID: " + departmentId, "", ""));
        if (departmentDTO.getDepartmentHeadId() == null) {
            throw new BadRequestAlertException("Department Head Missing for Dept. ID: " + departmentId, "", "");
        }
        return findOne(departmentDTO.getDepartmentHeadId())
            .orElseThrow(() -> new BadRequestAlertException("Employee not found by Id", "employeeService", ""));
    }

    @Override
    public boolean fixMultipleJoiningEntry() {
        try {
            employeeRepository
                .findAll()
                .forEach(employee -> {
                    List<EmploymentHistory> employmentHistoryList = employmentHistoryRepository.getAllByEmployeeIdAndEventType(
                        employee.getId(),
                        EventType.JOIN
                    );
                    for (int i = 0; i < employmentHistoryList.size() - 1; i++) {
                        employmentHistoryRepository.delete(employmentHistoryList.get(i));
                    }
                });
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
