package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EmployeeResignationService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.LeaveApplicationService;
import com.bits.hr.service.LeaveManagement.LeaveDaysCount.LeaveDaysCalculationService;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetDTO;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetServiceV2;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.DateRangeDTO;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.LeaveApplicationEvent;
import com.bits.hr.util.DateUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;

/**
 * REST controller for managing {@link com.bits.hr.domain.LeaveApplication}.
 */
@RestController
@RequestMapping("/api/attendance-mgt/leave-applications")
public class LeaveApplicationResource {

    private static final String ENTITY_NAME = "leaveApplication";

    private static final String RESOURCE_NAME = "LeaveApplicationResource";
    private final Logger log = LoggerFactory.getLogger(LeaveApplicationResource.class);
    private final LeaveApplicationService leaveApplicationService;
    private final ApplicationEventPublisher applicationEventPublisher;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private LeaveDaysCalculationService leaveDaysCalculationService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeResignationService employeeResignationService;

    @Autowired
    private AttendanceTimeSheetServiceV2 attendanceTimeSheetService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public LeaveApplicationResource(
        LeaveApplicationService leaveApplicationService,
        ApplicationEventPublisher applicationEventPublisher,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.leaveApplicationService = leaveApplicationService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /leave-applications} : Create a new leaveApplication.
     *
     * @param leaveApplicationDTO the leaveApplicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaveApplicationDTO, or with status {@code 400 (Bad Request)} if the leaveApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LeaveApplicationDTO> createLeaveApplication(@RequestBody @Valid LeaveApplicationDTO leaveApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to Apply for Leave : {}", leaveApplicationDTO);

        if (leaveApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaveApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (leaveApplicationDTO.getEmployeeId() == null) {
            throw new BadRequestAlertException("Employee not specified", ENTITY_NAME, "idexists");
        }
        // balance checking is not required for lwp
        if (
            leaveApplicationDTO.getLeaveType() != LeaveType.LEAVE_WITHOUT_PAY &&
            leaveApplicationDTO.getLeaveType() != LeaveType.LEAVE_WITHOUT_PAY_SANDWICH &&
            leaveApplicationService.getApplicableLeaveBalance(leaveApplicationDTO) < leaveApplicationDTO.getDurationInDay()
        ) {
            throw new BadRequestAlertException("Insufficient Leave balance", ENTITY_NAME, "insufficientBalance");
        }
        // year validation
        if (leaveApplicationDTO.getStartDate() != null && leaveApplicationDTO.getEndDate() != null) {
            if (leaveApplicationDTO.getStartDate().getYear() != leaveApplicationDTO.getEndDate().getYear()) {
                throw new BadRequestAlertException("Start Year and End Year must be same", "LeaveApplicationDTO", "yearMustBeSame");
            }
        }

        Optional<Employee> employeeOptional = employeeRepository.findById(leaveApplicationDTO.getEmployeeId());

        // validation
        LocalDate startDate = leaveApplicationDTO.getStartDate();
        LocalDate endDate = leaveApplicationDTO.getEndDate();
        if (employeeOptional.get().getDateOfJoining() == null) {
            throw new BadRequestAlertException(
                "Employee Date of Joining is missing, contact with HR for update.",
                "LeaveApplicationDTO",
                "dojMissing"
            );
        }
        if (startDate.isBefore(employeeOptional.get().getDateOfJoining())) {
            throw new BadRequestAlertException(
                "Before your date of joining leave application is not applicable!",
                "LeaveApplicationDTO",
                "leaveApplicationBeforeDOJ"
            );
        }

        Optional<LocalDate> lwd = employeeResignationService.getLastWorkingDay(employeeOptional.get().getId());
        if (lwd.isPresent() && DateUtil.isBetween(lwd.get(), startDate, endDate)) {
            throw new BadRequestAlertException(
                "After last working day, leave application is not valid",
                "LeaveApplicationDTO",
                "leaveAfterLastWorkingDay"
            );
        }

        /*if (leaveApplicationDTO.getLeaveType() == LeaveType.NON_MENTIONABLE_MATERNITY_LEAVE) {
            leaveApplicationDTO.setDurationInDay(1); // one leave because it's on employee life cycle basis
        }*/

        // Application date = current date
        leaveApplicationDTO.setApplicationDate(LocalDate.now());
        // Approve Leave Application when HR apply on behalf of Employee
        leaveApplicationDTO.setIsHRApproved(true);
        leaveApplicationDTO.setIsLineManagerApproved(false);
        leaveApplicationDTO.setIsRejected(false);
        leaveApplicationDTO.setRejectionComment("");
        leaveApplicationDTO.setSanctionedAt(Instant.now());

        Optional<Long> userId = currentEmployeeService.getCurrentUserId();
        if (userId.isPresent()) {
            leaveApplicationDTO.setSanctionedById(userId.get());
        }

        LeaveApplicationDTO result = leaveApplicationService.save(leaveApplicationDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        //publishEvent(result, EventType.CREATED);
        return ResponseEntity
            .created(new URI("/api/attendance-mgt/leave-applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leave-applications} : Updates an existing leaveApplication.
     *
     * @param leaveApplicationDTO the leaveApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the leaveApplicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaveApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<LeaveApplicationDTO> updateLeaveApplication(@RequestBody @Valid LeaveApplicationDTO leaveApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to update LeaveApplication : {}", leaveApplicationDTO);
        if (leaveApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        // year validation
        if (leaveApplicationDTO.getStartDate() != null && leaveApplicationDTO.getEndDate() != null) {
            if (leaveApplicationDTO.getStartDate().getYear() != leaveApplicationDTO.getEndDate().getYear()) {
                throw new BadRequestAlertException("Start Year and End Year must be same", "LeaveApplicationDTO", "yearMustBeSame");
            }
        }

        Optional<Employee> employeeOptional = employeeRepository.findById(leaveApplicationDTO.getEmployeeId());

        // validation
        LocalDate startDate = leaveApplicationDTO.getStartDate();
        LocalDate endDate = leaveApplicationDTO.getEndDate();
        if (employeeOptional.get().getDateOfJoining() == null) {
            throw new BadRequestAlertException(
                "Your Date of Joining is missing, contact with HR for update.",
                "LeaveApplicationDTO",
                "dojMissing"
            );
        }
        if (startDate.isBefore(employeeOptional.get().getDateOfJoining())) {
            throw new BadRequestAlertException(
                "You can not apply leave before your date of joining!",
                "LeaveApplicationDTO",
                "leaveApplicationBeforeDOJ"
            );
        }

        Optional<LocalDate> lwd = employeeResignationService.getLastWorkingDay(employeeOptional.get().getId());
        if (lwd.isPresent() && DateUtil.isBetween(lwd.get(), startDate, endDate)) {
            throw new BadRequestAlertException(
                "After last working day, leave application is not valid",
                "LeaveApplicationDTO",
                "leaveAfterLastWorkingDay"
            );
        }

        Optional<LeaveApplicationDTO> previousLeaveApplicationOptional = leaveApplicationService.findOne(leaveApplicationDTO.getId());
        if (!previousLeaveApplicationOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // balance checking is not required for lwp
        LeaveApplicationDTO previousLeaveApplication = previousLeaveApplicationOptional.get();
        if (
            leaveApplicationDTO.getLeaveType() != LeaveType.LEAVE_WITHOUT_PAY &&
            leaveApplicationDTO.getLeaveType() != LeaveType.LEAVE_WITHOUT_PAY_SANDWICH &&
            leaveApplicationDTO.getDurationInDay() > previousLeaveApplication.getDurationInDay()
        ) {
            int remaining = leaveApplicationService.getApplicableLeaveBalance(leaveApplicationDTO);
            int increased = leaveApplicationDTO.getDurationInDay() - previousLeaveApplication.getDurationInDay();
            if (increased > remaining) {
                throw new BadRequestAlertException("Insufficient Leave balance", ENTITY_NAME, "insufficientBalance");
            }
        }

        // Approve Leave Application when HR apply on behalf of Employee
        leaveApplicationDTO.setIsHRApproved(true);
        leaveApplicationDTO.setIsLineManagerApproved(false);
        leaveApplicationDTO.setIsRejected(false);
        leaveApplicationDTO.setRejectionComment("");

        LeaveApplicationDTO result = leaveApplicationService.save(leaveApplicationDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);
        //publishEvent(result, EventType.UPDATED);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaveApplicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /leave-applications} : get all the leaveApplications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaveApplications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LeaveApplicationDTO>> getAllLeaveApplications(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of LeaveApplications");
        Page<LeaveApplicationDTO> page = leaveApplicationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leave-applications/:id} : get the "id" leaveApplication.
     *
     * @param id the id of the leaveApplicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaveApplicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LeaveApplicationDTO> getLeaveApplication(@PathVariable Long id) {
        log.debug("REST request to get LeaveApplication : {}", id);
        Optional<LeaveApplicationDTO> leaveApplicationDTO = leaveApplicationService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (leaveApplicationDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, leaveApplicationDTO.get(), RequestMethod.GET, RESOURCE_NAME);
        }
        return ResponseUtil.wrapOrNotFound(leaveApplicationDTO);
    }

    /**
     * {@code DELETE  /leave-applications/:id} : delete the "id" leaveApplication.
     *
     * @param id the id of the leaveApplicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveApplication(@PathVariable Long id) {
        log.debug("REST request to delete LeaveApplication : {}", id);
        Optional<LeaveApplicationDTO> leaveApplicationDTO = leaveApplicationService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (leaveApplicationDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, leaveApplicationDTO.get(), RequestMethod.DELETE, "LeaveApplication");
        }
        leaveApplicationService.delete(id);
        eventLoggingPublisher.publishEvent(user, leaveApplicationDTO, RequestMethod.DELETE, RESOURCE_NAME);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * Get monthly remaining casual leave
     */
    @PostMapping("/monthly-remaining-casual-leave")
    public ResponseEntity<Integer> getMonthlyRemainingCasualLeave(@RequestBody LeaveApplicationDTO leaveApplicationDTO) {
        long employeeId = leaveApplicationDTO.getEmployeeId();
        Month monthValue = leaveApplicationDTO.getStartDate().getMonth();
        int year = leaveApplicationDTO.getStartDate().getYear();
        int result = leaveDaysCalculationService.monthlyRemainingCasualLeave(employeeId, monthValue, year);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity.ok().body(result);
    }

    /**
     * get approved or pending leave application in date range
     */
    @PostMapping("/find-by-employee-id-and-date-range")
    public ResponseEntity<Boolean> hasAnyApprovedAndPendingLeaveByEmployeeIdAndDateRange(
        @RequestBody LeaveApplicationDTO leaveApplicationDTO
    ) {
        boolean result = leaveApplicationService.findPendingAndApprovedLeaveByEmployeeIdAndDateRange(
            leaveApplicationDTO.getEmployeeId(),
            leaveApplicationDTO.getStartDate(),
            leaveApplicationDTO.getEndDate()
        );
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity.ok().body(result);
    }

    /**
     * get leave balance by leave type
     */
    @PostMapping("/leave-balance")
    public ResponseEntity<Integer> getLeaveBalance(@RequestBody LeaveApplicationDTO leaveApplicationDTO) {
        Integer result = leaveApplicationService.getApplicableLeaveBalance(leaveApplicationDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity.ok().body(result);
    }

    /**
     * Leave application and direct approve
     */
    @PostMapping("/apply-and-approve")
    public ResponseEntity<LeaveApplicationDTO> leaveApplyAndApprove(@RequestBody LeaveApplicationDTO leaveApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaveApplication : {}", leaveApplicationDTO);
        if (leaveApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaveApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (leaveApplicationDTO.getEmployeeId() == null) {
            throw new BadRequestAlertException("Employee not specified", ENTITY_NAME, "employeeIdNotSpecified");
        }

        // no balance checking for lwp
        if (
            leaveApplicationDTO.getLeaveType() != LeaveType.LEAVE_WITHOUT_PAY &&
            leaveApplicationService.getApplicableLeaveBalance(leaveApplicationDTO) < leaveApplicationDTO.getDurationInDay()
        ) {
            throw new BadRequestAlertException("Insufficient Leave balance", ENTITY_NAME, "insufficientBalance");
        }

        Optional<Employee> employeeOptional = employeeRepository.findById(leaveApplicationDTO.getEmployeeId());
        if (employeeOptional.isPresent() && employeeOptional.get().getOfficialContactNo() != null) {
            leaveApplicationDTO.setPhoneNumberOnLeave(employeeOptional.get().getOfficialContactNo());
        }

        LeaveApplicationDTO result = leaveApplicationService.save(leaveApplicationDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        //publishEvent(result, EventType.CREATED);
        return ResponseEntity
            .created(new URI("/apply-and-approve" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/calculate-duration")
    public int getLeave(@RequestBody LeaveApplicationDTO leaveApplicationDTO) {
        log.debug("REST request to get the number of of vacation days");
        // considering there is no intersection between vacations

        boolean isConflict = false;
        try {
            String pin = employeeRepository.findById(leaveApplicationDTO.getEmployeeId()).get().getPin();
            isConflict =
                leaveDaysCalculationService.hasAnyLAConflict(
                    pin,
                    leaveApplicationDTO.getId(),
                    leaveApplicationDTO.getStartDate(),
                    leaveApplicationDTO.getEndDate()
                );
        } catch (Exception e) {
            log.debug(e.toString());
        }

        // return -1 for conflicting leave application.
        if (isConflict) return -1;

        int result = leaveDaysCalculationService.leaveDaysCalculation(leaveApplicationDTO.getStartDate(), leaveApplicationDTO.getEndDate());
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        return result;
    }

    @PostMapping("/{employeeId}/pending-applications-between-date-range")
    public AttendanceTimeSheetDTO findAnyPendingApplicationsForCurrentEmployee(
        @PathVariable long employeeId,
        @RequestBody DateRangeDTO dateRangeDTO
    ) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);

        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee not found", "Employee", "notFound");
        }

        AttendanceTimeSheetDTO attendanceTimeSheetDTO = attendanceTimeSheetService.findAnyApplicationsBetweenDateRange(
            employeeOptional.get().getPin(),
            dateRangeDTO.getStartDate(),
            dateRangeDTO.getEndDate()
        );
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, RESOURCE_NAME);
        return attendanceTimeSheetDTO;
    }

    private void publishEvent(LeaveApplicationDTO leaveApplication, EventType event) {
        log.debug("publishing leave application event with event: " + event);
        LeaveApplicationEvent leaveApplicationEvent = new LeaveApplicationEvent(this, leaveApplication, event);
        applicationEventPublisher.publishEvent(leaveApplicationEvent);
    }
}
