package com.bits.hr.web.rest.search;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.LeaveApplicationService;
import com.bits.hr.service.dto.AttendanceEntryDTO;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.LeaveApplicationEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.LeaveApplication}.
 */
@RestController
@RequestMapping("/api/attendance-mgt/leave-applications-search")
public class LeaveApplicationSearchResource {

    private static final String ENTITY_NAME = "leaveApplication";
    private final Logger log = LoggerFactory.getLogger(LeaveApplicationSearchResource.class);
    private final LeaveApplicationService leaveApplicationService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final EmployeeRepository employeeRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public LeaveApplicationSearchResource(
        LeaveApplicationService leaveApplicationService,
        ApplicationEventPublisher applicationEventPublisher,
        EmployeeRepository employeeRepository
    ) {
        this.leaveApplicationService = leaveApplicationService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/{employeeId}/{fromdate}/{todate}")
    public List<LeaveApplicationDTO> getAllLeaveApplicationsByEmployeeBetweenTwoDates(
        @PathVariable Long employeeId,
        @PathVariable LocalDate fromdate,
        @PathVariable LocalDate todate
    ) {
        log.debug("REST request to get all LeaveApplications");
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        String employeePin = "";

        if (employee.isPresent()) {
            employeePin = employeeRepository.findById(employeeId).get().getPin();
        } else {
            employeePin = "";
        }
        return leaveApplicationService.findAllLeaveApplicationsByEmployeeBetweenTwoDates(employeePin, fromdate, todate);
    }

    @GetMapping("")
    public ResponseEntity<List<LeaveApplicationDTO>> getLeaveApplicationsByEmployeeBetweenTwoDates(
        @RequestParam(required = false) Long employeeId,
        @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(required = false) LeaveType leaveType,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get LeaveApplications by Searching");

        String employeePin = "";
        if (employeeId != null) {
            Optional<Employee> employee = employeeRepository.findById(employeeId);
            if (employee.isPresent()) {
                employeePin = employeeRepository.findById(employeeId).get().getPin();
            }
        }

        Page<LeaveApplicationDTO> page = leaveApplicationService.findLeaveApplicationsByEmployeeBetweenDatesLeaveType(
            employeePin,
            startDate,
            endDate,
            leaveType,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{employeeId}/{leaveType}")
    public List<LeaveApplicationDTO> getLeaveApplicationsByEmployeeAndLeaveType(
        @PathVariable Long employeeId,
        @PathVariable LeaveType leaveType
    ) {
        log.debug("REST request to get LeaveApplications by Employee Id and Leave Type");
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        String employeePin = "";

        if (employee.isPresent()) {
            employeePin = employeeRepository.findById(employeeId).get().getPin();
        } else {
            employeePin = "";
        }
        return leaveApplicationService.findLeaveApplicationsByEmployeeLeaveType(employeePin, leaveType);
    }

    @GetMapping("/{id}")
    public List<LeaveApplicationDTO> getAllLeaveApplicationsByEmployee(@PathVariable Long id) {
        log.debug("REST request to get LeaveApplication : {}", id);
        Optional<Employee> employee = employeeRepository.findById(id);
        String employeePin = "";

        if (employee.isPresent()) {
            employeePin = employeeRepository.findById(id).get().getPin();
        } else {
            employeePin = "";
        }
        return leaveApplicationService.findByEmployeePin(employeePin);
    }

    @GetMapping("/{fromdate}/{todate}")
    public List<LeaveApplicationDTO> getAllLeaveApplicationsBetweenTwoDates(
        @PathVariable LocalDate fromdate,
        @PathVariable LocalDate todate
    ) {
        log.debug("REST request to get all LeaveApplications");
        return leaveApplicationService.findAllLeaveApplicationsBetweenTwoDates(fromdate, todate);
    }

    private void publishEvent(LeaveApplicationDTO leaveApplication, EventType event) {
        log.debug("publishing leave application event with event: " + event);
        LeaveApplicationEvent leaveApplicationEvent = new LeaveApplicationEvent(this, leaveApplication, event);
        applicationEventPublisher.publishEvent(leaveApplicationEvent);
    }
}
