package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.ConfigRepository;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.service.EmployeeResignationService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.LeaveApplicationService;
import com.bits.hr.service.LeaveManagement.userLeaveApplication.UserLeaveApplicationService;
import com.bits.hr.service.attendanceTimeSheet.AttendanceSharedService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.LeaveApplicationEvent;
import com.bits.hr.service.mapper.LeaveApplicationMapper;
import com.bits.hr.web.rest.LeaveApplicationResource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;

/**
 * REST controller for managing {@link com.bits.hr.domain.LeaveApplication}.
 */
@RestController
@RequestMapping("/api/common/user-leave-application")
public class UserLeaveApplicationResource {

    private static final String ENTITY_NAME = "leaveApplication";
    private static final String RESOURCE_NAME = "UserLeaveApplicationResource";
    private final Logger log = LoggerFactory.getLogger(LeaveApplicationResource.class);
    private final LeaveApplicationService leaveApplicationService;
    private final UserLeaveApplicationService userLeaveApplicationService;
    private final CurrentEmployeeService currentEmployeeService;
    private final EmployeeResignationService employeeResignationService;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final LeaveApplicationMapper leaveApplicationMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ConfigRepository configRepository;

    private final EventLoggingPublisher eventLoggingPublisher;

    private final AttendanceSharedService attendanceSharedService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public UserLeaveApplicationResource(
        LeaveApplicationService leaveApplicationService,
        UserLeaveApplicationService userLeaveApplicationService,
        CurrentEmployeeService currentEmployeeService,
        EmployeeResignationService employeeResignationService,
        LeaveApplicationRepository leaveApplicationRepository,
        LeaveApplicationMapper leaveApplicationMapper,
        ApplicationEventPublisher applicationEventPublisher,
        ConfigRepository configRepository,
        EventLoggingPublisher eventLoggingPublisher,
        AttendanceSharedService attendanceSharedService
    ) {
        this.leaveApplicationService = leaveApplicationService;
        this.userLeaveApplicationService = userLeaveApplicationService;
        this.currentEmployeeService = currentEmployeeService;
        this.employeeResignationService = employeeResignationService;
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.leaveApplicationMapper = leaveApplicationMapper;
        this.applicationEventPublisher = applicationEventPublisher;
        this.configRepository = configRepository;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.attendanceSharedService = attendanceSharedService;
    }

    @PostMapping("")
    public ResponseEntity<LeaveApplicationDTO> applyForLeaveApplication(@RequestBody @Valid LeaveApplicationDTO leaveApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaveApplication : {}", leaveApplicationDTO);
        if (leaveApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaveApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }

        LeaveApplicationDTO result = userLeaveApplicationService.create(leaveApplicationDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        publishEvent(result, EventType.CREATED);
        return ResponseEntity
            .created(new URI("/api/common/user-leave-applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("")
    public ResponseEntity<LeaveApplicationDTO> updateLeaveApplication(@RequestBody @Valid LeaveApplicationDTO leaveApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to update current user LeaveApplication : {}", leaveApplicationDTO);
        if (leaveApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Leave Application Id is Missing", ENTITY_NAME, "idnull");
        }

        LeaveApplicationDTO result = userLeaveApplicationService.update(leaveApplicationDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);
        publishEvent(result, EventType.UPDATED);
        return ResponseEntity
            .created(new URI("/api/common/user-leave-applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("")
    public List<LeaveApplicationDTO> getAllLeaveApplicationsSubordinate() {
        log.debug("REST request to get all LeaveApplications");

        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) throw new NoEmployeeProfileException();

        long id = employeeOptional.get().getId();

        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findEmployeeLeaveApplicationByEmployeeId(id);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return leaveApplicationMapper.toDto(leaveApplicationList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveApplicationDTO> getUserLeaveApplicationById(@PathVariable Long id) {
        log.debug("REST request to get current user LeaveApplication by id");
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) throw new NoEmployeeProfileException();
        long employeeId = employeeOptional.get().getId();
        Optional<LeaveApplication> leaveApplicationList1 = leaveApplicationRepository.findPendingLeaveApplicationByIdAndEmployeeId(
            id,
            employeeId
        );

        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!currentEmployeeId.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        if (employeeId != currentEmployeeId.get()) {
            log.error("Access Forbidden", leaveApplicationList1.get().getId());
            throw new BadRequestAlertException("Access Forbidden", "UserLeaveApplication", "accessForbidden");
        }

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, leaveApplicationList1, RequestMethod.GET, RESOURCE_NAME);
        return ResponseUtil.wrapOrNotFound(leaveApplicationList1.map(leaveApplicationMapper::toDto));
    }

    /**
     * {@code DELETE  /user-leave-application/:id} : delete the "id" user leave application.
     *
     * @param id the id of the user leave application to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveApplication(@PathVariable Long id) {
        log.debug("REST request to delete User Leave Application : {}", id);
        Optional<LeaveApplication> leaveApplication = leaveApplicationRepository.findById(id);

        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        Long leaveApplicationEmployeeId = leaveApplication.get().getEmployee().getId();
        if (!currentEmployeeId.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        if (!leaveApplicationEmployeeId.equals(currentEmployeeId.get())) {
            log.error("Access Forbidden", leaveApplication.get().getId());
            throw new BadRequestAlertException("Access Forbidden", "UserLeaveApplication", "accessForbidden");
        }
        User user = currentEmployeeService.getCurrentUser().get();
        if (leaveApplication.isPresent()) {
            eventLoggingPublisher.publishEvent(user, leaveApplication, RequestMethod.DELETE, RESOURCE_NAME);
        }

        if (!leaveApplication.isPresent()) {
            log.error("Access Forbidden", leaveApplication.get().getId());
            throw new BadRequestAlertException("No Leave Application Found", ENTITY_NAME, "idnull");
        }
        log.debug("checking the leave application is approved by hr or lm");

        if (leaveApplication.get().getIsHRApproved() == true || leaveApplication.get().getIsLineManagerApproved() == true) {
            log.error("Only pending requests are allowed to edit", leaveApplication.get().getId());
            throw new BadRequestAlertException("Only pending requests are allowed to edit.", "UserLeaveApplication", "statusIsNotPending");
        } else {
            leaveApplicationRepository.deleteById(id);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    private void publishEvent(LeaveApplicationDTO leaveApplication, EventType event) {
        log.debug("publishing leave application event with event: " + event);
        LeaveApplicationEvent leaveApplicationEvent = new LeaveApplicationEvent(this, leaveApplication, event);
        applicationEventPublisher.publishEvent(leaveApplicationEvent);
    }
}
