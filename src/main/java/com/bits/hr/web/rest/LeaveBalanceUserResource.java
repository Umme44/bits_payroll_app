package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.security.SecurityUtils;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.DTO.LeaveBalanceDetailViewDTO;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.LeaveBalanceDetailViewService;
import com.bits.hr.service.config.CurrentEmployeeService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance-user/leave-balances")
public class LeaveBalanceUserResource {

    private static final String ENTITY_NAME = "leaveBalanceUser";
    private final Logger log = LoggerFactory.getLogger(LeaveBalanceUserResource.class);
    private final LeaveBalanceDetailViewService leaveBalanceDetailViewService;
    private final EmployeeRepository employeeRepository;

    private final EventLoggingPublisher eventLoggingPublisher;

    private final CurrentEmployeeService currentEmployeeService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public LeaveBalanceUserResource(
        LeaveBalanceDetailViewService leaveBalanceDetailViewService,
        EmployeeRepository employeeRepository,
        EventLoggingPublisher eventLoggingPublisher,
        CurrentEmployeeService currentEmployeeService
    ) {
        this.leaveBalanceDetailViewService = leaveBalanceDetailViewService;
        this.employeeRepository = employeeRepository;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.currentEmployeeService = currentEmployeeService;
    }

    @GetMapping("/my-leave-summary/{year}")
    // get my user PIN
    // get all by pin and year
    // year = 0 for current year balance
    public List<LeaveBalanceDetailViewDTO> myLeaveSummary(@PathVariable Integer year) {
        String employeePin = SecurityUtils.getCurrentEmployeePin();
        long employeeId = employeeRepository.getIdByPin(employeePin);

        if (year == 0) {
            year = LocalDate.now(ZoneId.of("Asia/Dhaka")).getYear();
        }
        List<LeaveBalanceDetailViewDTO> leaveBalanceDetailViewDTOList = leaveBalanceDetailViewService.getYearlyProcessedLeaveBalance(
            year,
            employeeId
        );

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "LeaveBalanceUser");
        return leaveBalanceDetailViewDTOList;
    }

    @GetMapping("/get-subordinate-leave-summary/{subordinateId}/{year}")
    public List<LeaveBalanceDetailViewDTO> mySubordinateLeaveSummary(@PathVariable Long subordinateId, @PathVariable Integer year) {
        String employeePin = SecurityUtils.getCurrentEmployeePin();

        Optional<Employee> employeeOptional = employeeRepository.findById(subordinateId);

        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee not found", ENTITY_NAME, "");
        }

        if (!employeeOptional.get().getReportingTo().getPin().equals(employeePin)) {
            throw new BadRequestAlertException("Subordinate employee not found", ENTITY_NAME, "");
        }

        if (year == 0) {
            year = LocalDate.now(ZoneId.of("Asia/Dhaka")).getYear();
        }

        List<LeaveBalanceDetailViewDTO> leaveBalanceDetailViewDTOList = leaveBalanceDetailViewService.getYearlyProcessedLeaveBalance(
            year,
            subordinateId
        );

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "LeaveBalanceUser");
        return leaveBalanceDetailViewDTOList;
    }
}
