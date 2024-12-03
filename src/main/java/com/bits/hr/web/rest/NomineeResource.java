package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.NomineeType;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.NomineeService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.*;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.NomineeUpdateEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
@RequestMapping("/api/employee-mgt")
public class NomineeResource {

    private final Logger log = LoggerFactory.getLogger(NomineeResource.class);

    private static final String ENTITY_NAME = "Nominee";
    private static final String RESOURCE_NAME = "NomineeCommonResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private EmployeeRepository employeeRepository;

    private final NomineeService nomineeService;

    private final EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public NomineeResource(NomineeService nomineeService, EventLoggingPublisher eventLoggingPublisher) {
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
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/nominees/multipart")
    public ResponseEntity<NomineeDTO> createNominee(@RequestPart("nominee") NomineeDTO nomineeDTO, @RequestPart("file") MultipartFile file)
        throws URISyntaxException {
        log.debug("REST request to save Nominee : {}", nomineeDTO);
        if (nomineeDTO.getId() != null) {
            throw new BadRequestAlertException("A new nominee cannot already have an ID", ENTITY_NAME, "id exists");
        }

        if (nomineeDTO.getEmployeeId() == null) {
            throw new NoEmployeeProfileException();
        }
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
        nomineeDTO.setStatus(Status.PENDING);
        NomineeDTO result = nomineeService.save(nomineeDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);
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
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
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
        eventLoggingPublisher.publishEvent(user, nomineeDTO.get(), RequestMethod.GET, RESOURCE_NAME);
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
        nomineeService.delete(id);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.DELETE, RESOURCE_NAME);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/nominees/list")
    public ResponseEntity<List<NomineeDTO>> getNomineeList(@RequestBody NomineeDTO nomineeDTO) {
        log.debug("REST request to get nominee List : {}", nomineeDTO);
        List<NomineeDTO> nomineeList = nomineeService.getNomineeListByEmployeeIdAndNomineeType(
            nomineeDTO.getEmployeeId(),
            nomineeDTO.getNomineeType()
        );
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity.ok(nomineeList);
    }

    @PostMapping("/nominees/get-remaining-percentage")
    public ResponseEntity<Double> getRemainingPercentageOfNominee(@RequestBody NomineeDTO nomineeDTO) {
        double result = nomineeService.getRemainingPercentage(nomineeDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/nominees/employees-with-nominees")
    public ResponseEntity<List<EmployeeNomineeInfo>> getAllEmployeeListWithNominees(
        @RequestParam(required = false) Long employeeId,
        @RequestParam(required = false) String nomineeType,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Nominee List");
        Page<EmployeeNomineeInfo> page = nomineeService.getAllNomineeByDateRange(employeeId, startDate, endDate, nomineeType, pageable);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/nominees/all/{pin}")
    public ResponseEntity<NomineeMasterDTO> getNomineesByEmployeePin(@PathVariable String pin) {
        log.debug("REST request to get a page of Nominees by employeePin");
        NomineeMasterDTO nomineeDTOList = nomineeService.getNomineesByPin(pin);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(nomineeDTOList);
    }

    @GetMapping("/nominees/all-gf-nominees/{employeeId}")
    public ResponseEntity<List<NomineeDTO>> getGfNomineesById(@PathVariable Long employeeId) {
        log.debug("REST request to get a page of Gf Nominees by employeeId");
        List<NomineeDTO> nomineeDTOList = nomineeService.getNomineesByEmployeeIdAndNomineeType(employeeId, NomineeType.GRATUITY_FUND);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(nomineeDTOList);
    }

    @GetMapping("/nominees/all-general-nominees/{employeeId}")
    public ResponseEntity<List<NomineeDTO>> getGeneralNomineesById(@PathVariable Long employeeId) {
        log.debug("REST request to get a page of General Nominees by employeeId");
        List<NomineeDTO> nomineeDTOList = nomineeService.getNomineesByEmployeeIdAndNomineeType(employeeId, NomineeType.GENERAL);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(nomineeDTOList);
    }

    @GetMapping("/nominees/employee-details-for-nominee/{employeeId}")
    public ResponseEntity<EmployeeDetailsNomineeReportDTO> getEmployeeDetailsForNomineeById(@PathVariable long employeeId) {
        log.debug("REST request to get EmployeeDetails by employeeId");
        EmployeeDetailsNomineeReportDTO employeeDetails = nomineeService.getEmployeeDetailsById(employeeId);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, employeeDetails, RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(employeeDetails);
    }

    private void publishEvent(long employeeId, NomineeType nomineeType, EventType event) {
        log.info("publishing email event for nominee update with : " + event);
        Employee employee = employeeRepository.findById(employeeId).get();
        String nomineeTypeValue = nomineeType.equals(NomineeType.GENERAL) ? "general" : "gratuity fund";
        NomineeUpdateEvent nomineeUpdateEvent = new NomineeUpdateEvent(this, employee, nomineeTypeValue, event);
        applicationEventPublisher.publishEvent(nomineeUpdateEvent);
    }
}
