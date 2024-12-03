package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.security.SecurityUtils;
import com.bits.hr.service.LeaveManagement.LeaveDaysCount.LeaveDaysCalculationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/common/leave-date-calculation")
public class LeaveDaysCalculationResource {

    private static final String ENTITY_NAME = "LeaveDaysCalculation";
    private final Logger log = LoggerFactory.getLogger(LeaveDaysCalculationResource.class);
    private final LeaveDaysCalculationService leaveDaysCalculationService;
    private final EmployeeRepository employeeRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    public LeaveDaysCalculationResource(LeaveDaysCalculationService leaveDaysCalculationService, EmployeeRepository employeeRepository) {
        this.leaveDaysCalculationService = leaveDaysCalculationService;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Method for calculating leave duration
     * @param leaveStartDate
     * @param leaveEndDate
     * @return
     */
    @GetMapping("/{fromdate}/{todate}")
    public int getLeave(@PathVariable("fromdate") LocalDate leaveStartDate, @PathVariable("todate") LocalDate leaveEndDate) {
        log.debug("REST request to get the number of of vacation days");
        // considering there is no intersection between vacations

        boolean isConflict = false;
        try {
            isConflict =
                leaveDaysCalculationService.leaveApplicationConflict(SecurityUtils.getCurrentEmployeePin(), leaveStartDate, leaveEndDate);
        } catch (Exception e) {
            log.debug(e.toString());
        }
        if (isConflict) return -1;
        // return -1 for conflicting leave application.

        return leaveDaysCalculationService.leaveDaysCalculation(leaveStartDate, leaveEndDate);
    }

    @PostMapping("/calculate-for-current-employee")
    public int getLeave(@RequestBody LeaveApplicationDTO leaveApplicationDTO) {
        log.debug("REST request to get the number of of vacation days");
        // considering there is no intersection between vacations

        boolean isConflict = false;
        try {
            isConflict =
                leaveDaysCalculationService.hasAnyLAConflict(
                    currentEmployeeService.getCurrentEmployeePin().get(),
                    leaveApplicationDTO.getId(),
                    leaveApplicationDTO.getStartDate(),
                    leaveApplicationDTO.getEndDate()
                );
        } catch (Exception e) {
            log.debug(e.toString());
        }
        if (isConflict) return -1;
        // return -1 for conflicting leave application.

        return leaveDaysCalculationService.leaveDaysCalculation(leaveApplicationDTO.getStartDate(), leaveApplicationDTO.getEndDate());
    }

    @GetMapping("/calculate-casual-leave-remaining/{startDate}/{endDate}/{leaveApplicationId}")
    public ResponseEntity<Boolean> hasCasualLeaveRemaining(@PathVariable long leaveApplicationId,
                                                           @PathVariable LocalDate startDate,
                                                           @PathVariable LocalDate endDate) {
        log.debug("REST request to get the remaining days of casual leave");

        Employee employee = currentEmployeeService.getCurrentEmployee().get();

        if(employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE){
            return ResponseEntity.ok(true);
        }
        // start date and end date in same month
        if (startDate.getMonth().equals(endDate.getMonth())){
            int remaining = leaveDaysCalculationService.monthlyRemainingCasualLeave(employee.getId(), startDate.getMonth(), startDate.getYear());
            final int duration = leaveDaysCalculationService.leaveDaysCalculation(startDate, endDate);

            Optional<LeaveApplication> leaveApplicationOptional = leaveApplicationRepository.findById(leaveApplicationId);
            if (leaveApplicationOptional.isPresent() &&
                leaveApplicationOptional.get().getLeaveType().equals(LeaveType.MENTIONABLE_CASUAL_LEAVE))
            {
                remaining += leaveDaysCalculationService.leaveDaysCalculation(leaveApplicationOptional.get().getStartDate(),
                    leaveApplicationOptional.get().getEndDate());
            }

            if (duration > remaining){
                return ResponseEntity.ok(false);
            }else {
                return ResponseEntity.ok(true);
            }
        }else{
            final int startMonthRemaining = leaveDaysCalculationService.monthlyRemainingCasualLeave(employee.getId(), startDate.getMonth(), startDate.getYear());
            final int endMonthRemaining = leaveDaysCalculationService.monthlyRemainingCasualLeave(employee.getId(), endDate.getMonth(), endDate.getYear());
            final int duration = leaveDaysCalculationService.leaveDaysCalculation(startDate, endDate);
            int totalRemaining = startMonthRemaining + endMonthRemaining;

            Optional<LeaveApplication> leaveApplicationOptional = leaveApplicationRepository.findById(leaveApplicationId);
            if (leaveApplicationOptional.isPresent() &&
                leaveApplicationOptional.get().getLeaveType().equals(LeaveType.MENTIONABLE_CASUAL_LEAVE))
            {
                totalRemaining += leaveDaysCalculationService.leaveDaysCalculation(leaveApplicationOptional.get().getStartDate(),
                    leaveApplicationOptional.get().getEndDate());
            }

            if (duration > totalRemaining){
                return ResponseEntity.ok(false);
            }else {
                return ResponseEntity.ok(true);
            }
        }

    }

    @GetMapping("/{employeeId}/{fromdate}/{todate}")
    public int getLeaveApplicationIsPossible(
        @PathVariable("employeeId") long employeeId,
        @PathVariable("fromdate") LocalDate leaveStartDate,
        @PathVariable("todate") LocalDate leaveEndDate
    ) {
        log.debug("REST request to get the number of of vacation days");
        // considering there is no intersection between vacations

        boolean isConflict = false;

        Optional<Employee> employee = employeeRepository.findById(employeeId);
        String pin = "";

        if (employee.isPresent()) {
            pin = employeeRepository.findById(employeeId).get().getPin();
        } else {
            return -2;
            // for not getting any employee;
        }
        isConflict = leaveDaysCalculationService.leaveApplicationConflict(pin, leaveStartDate, leaveEndDate);

        if (isConflict) return -1;

        return leaveDaysCalculationService.leaveDaysCalculation(leaveStartDate, leaveEndDate);
    }
}
