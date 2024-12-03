package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.LeaveBalanceService;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.DTO.LeaveBalanceDetailViewDTO;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.LeaveBalanceDetailViewService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.LeaveBalanceDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.LeaveBalance}.
 */
@RestController
@RequestMapping("/api/attendance-mgt/leave-balances")
public class LeaveBalanceResource {

    private static final String ENTITY_NAME = "leaveBalance";
    private final Logger log = LoggerFactory.getLogger(LeaveBalanceResource.class);
    private final LeaveBalanceService leaveBalanceService;
    private final LeaveBalanceDetailViewService leaveBalanceDetailViewService;
    private final EmployeeRepository employeeRepository;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public LeaveBalanceResource(
        LeaveBalanceService leaveBalanceService,
        LeaveBalanceDetailViewService leaveBalanceDetailViewService,
        EmployeeRepository employeeRepository,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.leaveBalanceService = leaveBalanceService;
        this.leaveBalanceDetailViewService = leaveBalanceDetailViewService;
        this.employeeRepository = employeeRepository;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /leave-balances} : Create a new leaveBalance.
     *
     * @param leaveBalanceDTO the leaveBalanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaveBalanceDTO, or with status {@code 400 (Bad Request)} if the leaveBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LeaveBalanceDTO> createLeaveBalance(@Valid @RequestBody LeaveBalanceDTO leaveBalanceDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaveBalance : {}", leaveBalanceDTO);
        if (leaveBalanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaveBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Gender gender = employeeRepository.findById(leaveBalanceDTO.getEmployeeId()).get().getGender();
        if (!leaveBalanceService.validForMaternityOrPaternity(leaveBalanceDTO.getLeaveType(), gender)) {
            throw new BadRequestAlertException("Wrong Leave type Selection", ENTITY_NAME, "leaveTypeError");
        }
        if (leaveBalanceService.checkExists(leaveBalanceDTO)) {
            throw new BadRequestAlertException(
                " Leave balance of same type already exists for the following employee",
                ENTITY_NAME,
                "exists"
            );
        }
        LeaveBalanceDTO result = leaveBalanceService.save(leaveBalanceDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "LeaveBalance");
        return ResponseEntity
            .created(new URI("/api/attendance-mgt/leave-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leave-balances} : Updates an existing leaveBalance.
     *
     * @param leaveBalanceDTO the leaveBalanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveBalanceDTO,
     * or with status {@code 400 (Bad Request)} if the leaveBalanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaveBalanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<LeaveBalanceDTO> updateLeaveBalance(@Valid @RequestBody LeaveBalanceDTO leaveBalanceDTO)
        throws URISyntaxException {
        log.debug("REST request to update LeaveBalance : {}", leaveBalanceDTO);
        if (leaveBalanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Gender gender = employeeRepository.findById(leaveBalanceDTO.getEmployeeId()).get().getGender();
        if (!leaveBalanceService.validForMaternityOrPaternity(leaveBalanceDTO.getLeaveType(), gender)) {
            throw new BadRequestAlertException("Wrong Leave type Selection", ENTITY_NAME, "leaveTypeError");
        }

        if (leaveBalanceService.checkExists(leaveBalanceDTO)) {
            LeaveBalanceDTO leaveBalanceDTO01 = leaveBalanceService.getByYearAndEmployeeIdAndLeaveType(
                leaveBalanceDTO.getYear(),
                leaveBalanceDTO.getEmployeeId(),
                leaveBalanceDTO.getLeaveType()
            );
            if (leaveBalanceDTO01 != null && leaveBalanceDTO01.getId().equals(leaveBalanceDTO.getId())) {} else {
                throw new BadRequestAlertException(
                    " Leave balance of same type already exists for the following employee",
                    ENTITY_NAME,
                    "exists"
                );
            }
        }
        LeaveBalanceDTO result = leaveBalanceService.save(leaveBalanceDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "LeaveBalance");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaveBalanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /leave-balances} : get all the leaveBalances.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaveBalances in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LeaveBalanceDTO>> getAllLeaveBalances(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LeaveBalances");
        Page<LeaveBalanceDTO> page = leaveBalanceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "LeaveBalance");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leave-balances/:id} : get the "id" leaveBalance.
     *
     * @param id the id of the leaveBalanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaveBalanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LeaveBalanceDTO> getLeaveBalance(@PathVariable Long id) {
        log.debug("REST request to get LeaveBalance : {}", id);
        Optional<LeaveBalanceDTO> leaveBalanceDTO = leaveBalanceService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (leaveBalanceDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, leaveBalanceDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(leaveBalanceDTO);
    }

    @GetMapping("/{year}/{employeeId}")
    public List<LeaveBalanceDetailViewDTO> getLeaveBalanceOfCurrentYearByEmployeeId(@PathVariable int year, @PathVariable long employeeId) {
        if (year == 0) {
            year = LocalDate.now(ZoneId.of("Asia/Dhaka")).getYear();
        }

        List<LeaveBalanceDetailViewDTO> leaveBalanceDetailViewDTOList = leaveBalanceDetailViewService.getYearlyProcessedLeaveBalance(
            year,
            employeeId
        );
        return leaveBalanceDetailViewDTOList;
    }

    /**
     * {@code DELETE  /leave-balances/:id} : delete the "id" leaveBalance.
     *
     * @param id the id of the leaveBalanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveBalance(@PathVariable Long id) {
        log.debug("REST request to delete LeaveBalance : {}", id);
        Optional<LeaveBalanceDTO> leaveBalanceDTO = leaveBalanceService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (leaveBalanceDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, leaveBalanceDTO.get(), RequestMethod.DELETE, "AitConfig");
        }
        leaveBalanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/filter")
    public ResponseEntity<List<LeaveBalanceDTO>> getAllLeaveBalancesByFiltering(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestBody LeaveBalanceDTO leaveBalanceDTO
    ) {
        log.debug("REST request to get a page of LeaveBalances");
        Page<LeaveBalanceDTO> page = leaveBalanceService.findAllByFiltering(pageable, leaveBalanceDTO);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "LeaveBalance");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
