package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.NomineeType;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.NomineeService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeDetailsNomineeReportDTO;
import com.bits.hr.service.dto.NomineeDTO;
import com.bits.hr.service.dto.NomineeEligibilityDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.NomineeUpdateEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.Nominee}.
 */
@RestController
@RequestMapping("/api/common")
public class NomineeCommonResource {

    private final Logger log = LoggerFactory.getLogger(NomineeCommonResource.class);

    private static final String ENTITY_NAME = "Nominee";
    private final String RESOURCE_NAME = this.getClass().getSimpleName();

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NomineeService nomineeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final EventLoggingPublisher eventLoggingPublisher;

    public NomineeCommonResource(NomineeService nomineeService, EventLoggingPublisher eventLoggingPublisher) {
        this.nomineeService = nomineeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /nominees} : Create a new nominee.
     *
     * @param nomineeDTO the nomineeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nomineeDTO, or with status {@code 400 (Bad Request)} if the nominee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nominees")
    public ResponseEntity<NomineeDTO> createNominee(@Valid @RequestBody NomineeDTO nomineeDTO) throws URISyntaxException {
        log.debug("REST request to save Nominee : {}", nomineeDTO);
        if (nomineeDTO.getId() != null) {
            throw new BadRequestAlertException("A new nominee cannot already have an ID", ENTITY_NAME, "id exists");
        }
        NomineeDTO result = nomineeService.save(nomineeDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity
            .created(new URI("/api/nominees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, RESOURCE_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/nominees/employee-details-for-nominee")
    public ResponseEntity<EmployeeDetailsNomineeReportDTO> getEmployeeDetailsForNominee() {
        log.debug("REST request to get employee details with pf Nominee ");
        Long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
        if (employeeId == null) {
            throw new NoEmployeeProfileException();
        }

        EmployeeDetailsNomineeReportDTO employeeDetails = nomineeService.getGfNomineeDetailsByEmployeeId(employeeId);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(employeeDetails);
    }

    @PostMapping("/nominees/multipart")
    public ResponseEntity<NomineeDTO> createNominee(@RequestPart("nominee") NomineeDTO nomineeDTO, @RequestPart("file") MultipartFile file)
        throws URISyntaxException {
        log.debug("REST request to save Nominee : {}", nomineeDTO);
        if (nomineeDTO.getId() != null) {
            throw new BadRequestAlertException("A new nominee cannot already have an ID", ENTITY_NAME, "id exists");
        }

        // check
        // set employee from common. for common resource
        //
        nomineeDTO.setEmployeeId(currentEmployeeService.getCurrentEmployeeId().get());
        nomineeDTO.setStatus(Status.PENDING);

        nomineeDTO = nomineeService.validateNomineeAndGuardianNID(nomineeDTO);

        NomineeDTO result = nomineeService.saveWithFile(nomineeDTO, file);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity
            .created(new URI("/api/nominees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nominees} : Updates an existing nominee.
     *
     * @param nomineeDTO the nomineeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nomineeDTO,
     * or with status {@code 400 (Bad Request)} if the nomineeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nomineeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nominees")
    public ResponseEntity<NomineeDTO> updateNominee(@RequestBody NomineeDTO nomineeDTO) throws URISyntaxException {
        log.debug("REST request to update Nominee : {}", nomineeDTO);
        if (nomineeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }

        nomineeDTO = nomineeService.validateNomineeAndGuardianNID(nomineeDTO);
        nomineeDTO.setStatus(Status.PENDING);
        NomineeDTO result = nomineeService.save(nomineeDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);

        publishEvent(nomineeDTO.getEmployeeId(), result.getNomineeType(), EventType.UPDATED);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nomineeDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/nominees/multipart")
    public ResponseEntity<NomineeDTO> updateNominee(@RequestPart("nominee") NomineeDTO nomineeDTO, @RequestPart("file") MultipartFile file)
        throws URISyntaxException {
        log.debug("REST request to update Nominee : {}", nomineeDTO);
        if (nomineeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }

        nomineeDTO.setStatus(Status.PENDING);
        nomineeDTO = nomineeService.validateNomineeAndGuardianNID(nomineeDTO);

        NomineeDTO result = nomineeService.saveWithFile(nomineeDTO, file);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);

        publishEvent(nomineeDTO.getEmployeeId(), result.getNomineeType(), EventType.UPDATED);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nomineeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /nominees} : get all the nominees.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nominees in body.
     */
    @GetMapping("/nominees")
    public ResponseEntity<List<NomineeDTO>> getAllNominees(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Nominees");
        Page<NomineeDTO> page = nomineeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nominees/:id} : get the "id" nominee.
     *
     * @param id the id of the nomineeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nomineeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nominees/{id}")
    public ResponseEntity<NomineeDTO> getNominee(@PathVariable Long id) {
        log.debug("REST request to get Nominee : {}", id);
        Optional<NomineeDTO> nomineeDTO = nomineeService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseUtil.wrapOrNotFound(nomineeDTO);
    }

    /**
     * {@code DELETE  /nominees/:id} : delete the "id" nominee.
     *
     * @param id the id of the nomineeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nominees/{id}")
    public ResponseEntity<Void> deleteNominee(@PathVariable Long id) {
        log.debug("REST request to delete Nominee : {}", id);
        Optional<NomineeDTO> nomineeDTO = nomineeService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (nomineeDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, nomineeDTO.get(), RequestMethod.DELETE, RESOURCE_NAME);
        }
        nomineeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/nominees/list")
    public ResponseEntity<List<NomineeDTO>> getNomineeList(@RequestBody NomineeDTO nomineeDTO) {
        Optional<Long> currentEmployeeIdOptional = currentEmployeeService.getCurrentEmployeeId();
        if (!currentEmployeeIdOptional.isPresent()) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        long currentEmployeeId = currentEmployeeIdOptional.get();

        List<NomineeDTO> nomineeListCommon;

        if (nomineeDTO.getNomineeType() == null) {
            nomineeListCommon = nomineeService.getNomineeListByEmployeeId(currentEmployeeId);
        } else {
            nomineeListCommon = nomineeService.getNomineeListByEmployeeIdAndNomineeType(currentEmployeeId, nomineeDTO.getNomineeType());
        }

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity.ok(nomineeListCommon);
    }

    @PostMapping("/nominees/get-remaining-percentage")
    public ResponseEntity<Double> getRemainingPercentage(@RequestBody NomineeDTO nomineeDTO) {
        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        double result = 0;

        if (currentEmployeeId.isPresent()) {
            nomineeDTO.setEmployeeId(currentEmployeeId.get());
            result = nomineeService.getRemainingPercentage(nomineeDTO);
        }

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/nominees/is-eligible-for-gf")
    public ResponseEntity<Boolean> isEmployeeEligibleForGF() {
        Optional<Employee> currentEmployee = currentEmployeeService.getCurrentEmployee();

        if (currentEmployee.isPresent()) {} else {
            throw new RuntimeException();
        }

        boolean result = nomineeService.isEmployeeEligibleForGF(currentEmployee.get());
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/nominees/is-eligible-for-general-nominee")
    public ResponseEntity<Boolean> isEmployeeEligibleForGeneralNominee() {
        Optional<Employee> currentEmployee = currentEmployeeService.getCurrentEmployee();

        if (currentEmployee.isPresent()) {} else {
            throw new RuntimeException();
        }

        boolean result = nomineeService.isEmployeeEligibleForGeneralNominee(currentEmployee.get());
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/nominees/eligibility-check")
    public ResponseEntity<NomineeEligibilityDTO> getEligibility() {
        Employee currentEmployee = currentEmployeeService.getCurrentEmployee().get();
        NomineeEligibilityDTO nomineeEligibilityDTO = new NomineeEligibilityDTO();
        if (currentEmployee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
            nomineeEligibilityDTO.setEligibleForPf(true);
            nomineeEligibilityDTO.setEligibleForGf(true);
        }

        if (currentEmployee.getEmployeeCategory() == EmployeeCategory.INTERN) {
            nomineeEligibilityDTO.setEligibleForGeneral(false);
        } else {
            nomineeEligibilityDTO.setEligibleForGeneral(true);
        }
        return ResponseEntity.ok(nomineeEligibilityDTO);
    }

    private void publishEvent(long employeeId, NomineeType nomineeType, EventType event) {
        log.info("publishing email event for nominee update with : " + event);
        Employee employee = employeeRepository.findById(employeeId).get();
        String nomineeTypeValue = nomineeType.equals(NomineeType.GENERAL) ? "general" : "gratuity fund";
        NomineeUpdateEvent nomineeUpdateEvent = new NomineeUpdateEvent(this, employee, nomineeTypeValue, event);
        applicationEventPublisher.publishEvent(nomineeUpdateEvent);
    }
}
