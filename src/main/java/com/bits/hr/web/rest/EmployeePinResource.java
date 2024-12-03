package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmployeePinStatus;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EmployeePinService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeePinDTO;
import com.bits.hr.service.scheduler.schedulingService.EmployeePinStatusUpdateSchedulerService;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import com.bits.hr.util.annotation.ValidateNaturalText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.EmployeePin}.
 */
@RestController
@Validated
@RequestMapping("/api/employee-mgt")
public class EmployeePinResource {

    private final Logger log = LoggerFactory.getLogger(EmployeePinResource.class);

    private static final String ENTITY_NAME = "employeePin";
    private static final String RESOURCE_NAME = "EmployeePinResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeePinService employeePinService;

    private final EmployeePinStatusUpdateSchedulerService employeePinStatusUpdateSchedulerService;

    private final CurrentEmployeeService currentEmployeeService;

    private final EventLoggingPublisher eventLoggingPublisher;

    public EmployeePinResource(
        EmployeePinService employeePinService,
        EmployeePinStatusUpdateSchedulerService employeePinStatusUpdateSchedulerService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.employeePinService = employeePinService;
        this.employeePinStatusUpdateSchedulerService = employeePinStatusUpdateSchedulerService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /employee-pins} : Create a new employeePin.
     *
     * @param employeePinDTO the employeePinDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeePinDTO, or with status {@code 400 (Bad Request)} if the employeePin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employee-pins")
    public ResponseEntity<EmployeePinDTO> createEmployeePin(@Valid @RequestBody EmployeePinDTO employeePinDTO) throws URISyntaxException {
        log.debug("REST request to save EmployeePin : {}", employeePinDTO);
        if (employeePinDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeePin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = currentEmployeeService.getCurrentUser().get();

        EmployeePinDTO result = employeePinService.create(employeePinDTO, user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PUT  /employee-pins} : Updates an existing employeePin.
     *
     * @param employeePinDTO the employeePinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeePinDTO,
     * or with status {@code 400 (Bad Request)} if the employeePinDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeePinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employee-pins")
    public ResponseEntity<EmployeePinDTO> updateEmployeePin(@Valid @RequestBody EmployeePinDTO employeePinDTO) throws URISyntaxException {
        log.debug("REST request to update EmployeePin : {}", employeePinDTO);
        if (employeePinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        User user = currentEmployeeService.getCurrentUser().get();

        EmployeePinDTO result = employeePinService.update(employeePinDTO, user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeePinDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /employee-pins} : get all the employeePins.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeePins in body.
     */
    @GetMapping("/employee-pins")
    public ResponseEntity<List<EmployeePinDTO>> getAllEmployeePins(
        @RequestParam(required = false) @ValidateNaturalText String searchText,
        @RequestParam(required = false) EmployeeCategory selectedCategory,
        @RequestParam(required = false) EmployeePinStatus selectedStatus,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmployeePins");

        searchText = searchText != null ? searchText : "";

        Page<EmployeePinDTO> page = employeePinService.findAll(searchText, selectedCategory, selectedStatus, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, page.getContent(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-pins/:id} : get the "id" employeePin.
     *
     * @param id the id of the employeePinDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeePinDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employee-pins/{id}")
    public ResponseEntity<EmployeePinDTO> getEmployeePin(@PathVariable Long id) {
        log.debug("REST request to get EmployeePin : {}", id);
        Optional<EmployeePinDTO> employeePinDTO = employeePinService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, employeePinDTO, RequestMethod.GET, RESOURCE_NAME);

        return ResponseUtil.wrapOrNotFound(employeePinDTO);
    }

    @GetMapping("/employee-pins/find-by-pin/{pin}")
    public ResponseEntity<EmployeePinDTO> getEmployeePinByPin(@PathVariable String pin) {
        log.debug("REST request to get EmployeePin : {}", pin);
        Optional<EmployeePinDTO> employeePinDTO = employeePinService.findOneByPin(pin);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, employeePinDTO, RequestMethod.GET, RESOURCE_NAME);

        return ResponseUtil.wrapOrNotFound(employeePinDTO);
    }

    /**
     * {@code DELETE  /employee-pins/:id} : delete the "id" employeePin.
     *
     * @param id the id of the employeePinDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employee-pins/{id}")
    public ResponseEntity<Void> deleteEmployeePin(@PathVariable Long id) {
        log.debug("REST request to delete EmployeePin : {}", id);
        employeePinService.delete(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.DELETE, RESOURCE_NAME);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/employee-pins/decline-employee-onboard/{id}")
    public ResponseEntity<EmployeePinDTO> declineEmployeeOnboard(@PathVariable Long id) {
        log.debug("REST request to decline Employee onboard : {}", id);
        User user = currentEmployeeService.getCurrentUser().get();

        EmployeePinDTO dto = employeePinService.declineEmployeeOnboard(id, user);

        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.DELETE, RESOURCE_NAME);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/employee-pins/is-pin-unique")
    public ResponseEntity<Boolean> isEmployeePinUnique(
        @RequestParam(required = false) String pin,
        @RequestParam(required = false) Long employeePinId
    ) {
        log.debug("REST request to check if EmployeePin is unique or not : {}");
        boolean result = employeePinService.isPinUnique(pin, employeePinId);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/employee-pins/get-the-previous-pin/{pin}")
    public ResponseEntity<String> getThePreviousPin(@PathVariable String pin) {
        log.debug("REST request to get the previous pin : {}");

        String previousPin = employeePinService.getThePreviousPin(pin);
        return ResponseEntity.ok().body(previousPin);
    }

    @GetMapping("/employee-pins/run-employee-pin-scheduler")
    public ResponseEntity<Boolean> runEmployeePinScheduler() {
        log.debug("REST request to run employee pin scheduler : {}");
        employeePinStatusUpdateSchedulerService.updateEmployeePinStatusIfEmployeeResigns();
        return ResponseEntity.ok().body(true);
    }
}
