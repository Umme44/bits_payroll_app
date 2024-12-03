package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.PfNomineeFormService;
import com.bits.hr.service.PfNomineeService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeDetailsNomineeReportDTO;
import com.bits.hr.service.dto.PfAccountDTO;
import com.bits.hr.service.dto.PfNomineeDTO;
import com.bits.hr.service.fileOperations.fileService.NomineeFileService;
import com.bits.hr.service.search.FilterDto;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
 * REST controller for managing {@link com.bits.hr.domain.PfNominee}.
 */
@RestController
@RequestMapping("/api/pf-mgt/pf-nominees")
public class PfNomineeResource {

    private static final String ENTITY_NAME = "pfNominee";
    private final Logger log = LoggerFactory.getLogger(PfNomineeResource.class);
    private final PfNomineeService pfNomineeService;
    private final PfNomineeFormService pfNomineeFormService;

    private final NomineeFileService nomineeFileService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PfNomineeResource(
        PfNomineeService pfNomineeService,
        PfNomineeFormService pfNomineeFormService,
        NomineeFileService nomineeFileService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.pfNomineeService = pfNomineeService;
        this.pfNomineeFormService = pfNomineeFormService;
        this.nomineeFileService = nomineeFileService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /pf-nominees} : Create a new pfNominee.
     *
     * @param pfNomineeDTO the pfNomineeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pfNomineeDTO, or with status {@code 400 (Bad Request)} if the pfNominee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PfNomineeDTO> createPfNominee(
        @RequestPart(name = "file") MultipartFile file,
        @RequestPart(name = "pfNominee") PfNomineeDTO pfNomineeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PfNominee : {}", pfNomineeDTO);
        File savedFile = nomineeFileService.save(file);

        pfNomineeDTO.setPhoto(savedFile.getAbsolutePath());
        if (pfNomineeDTO.getId() != null) {
            throw new BadRequestAlertException("A new pfNominee cannot already have an ID", ENTITY_NAME, "idexists");
        }

        PfNomineeDTO result = pfNomineeService.save(pfNomineeDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "PfNominee");
        return ResponseEntity
            .created(new URI("/api/pf-mgt/pf-nominees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pf-nominees} : Updates an existing pfNominee.
     *
     * @param pfNomineeDTO the pfNomineeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pfNomineeDTO,
     * or with status {@code 400 (Bad Request)} if the pfNomineeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pfNomineeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<PfNomineeDTO> updatePfNominee(
        @RequestPart(name = "file") MultipartFile file,
        @RequestPart(name = "pfNominee") PfNomineeDTO pfNomineeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PfNominee : {}", pfNomineeDTO);
        File savedFile = nomineeFileService.save(file);
        pfNomineeDTO.setPhoto(savedFile.getAbsolutePath());
        if (pfNomineeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PfNomineeDTO result = pfNomineeService.save(pfNomineeDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "PfNominee");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pfNomineeDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/without-file")
    public ResponseEntity<PfNomineeDTO> updateNomineeWithoutFile(@RequestBody PfNomineeDTO pfNomineeDTO) throws URISyntaxException {
        log.debug("REST request to update PfNominee: {}", pfNomineeDTO);

        if (pfNomineeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        pfNomineeDTO = pfNomineeService.validateNomineeAndGuardianNID(pfNomineeDTO);

        //pfNomineeDTO = pfNomineeService.validateIsApproved(pfNomineeDTO);

        PfNomineeDTO result = pfNomineeService.save(pfNomineeDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "PfNominee");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pfNomineeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pf-nominees} : get all the pfNominees.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pfNominees in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PfNomineeDTO>> getAllPfNominees(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PfNominees");
        Page<PfNomineeDTO> page = pfNomineeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfNominee");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pf-nominees/:id} : get the "id" pfNominee.
     *
     * @param id the id of the pfNomineeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pfNomineeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PfNomineeDTO> getPfNominee(@PathVariable Long id) {
        log.debug("REST request to get PfNominee : {}", id);
        Optional<PfNomineeDTO> pfNomineeDTO = pfNomineeService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (pfNomineeDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfNomineeDTO.get(), RequestMethod.GET, "PfNominee");
        }
        return ResponseUtil.wrapOrNotFound(pfNomineeDTO);
    }

    /**
     * {@code DELETE  /pf-nominees/:id} : delete the "id" pfNominee.
     *
     * @param id the id of the pfNomineeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePfNominee(@PathVariable Long id) {
        log.debug("REST request to delete PfNominee : {}", id);
        Optional<PfNomineeDTO> pfNomineeDTO = pfNomineeService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (pfNomineeDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfNomineeDTO.get(), RequestMethod.DELETE, ENTITY_NAME);
        }
        pfNomineeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     *
     * @param pfAccountId
     * @return remaining share percentage
     */
    @GetMapping("/remaining-share-percentage/{pfAccountId}")
    public ResponseEntity<Double> getRemainingSharePercentage(@PathVariable long pfAccountId) {
        log.debug("REST request to get remaining share percentage: {}", pfAccountId);
        Double result = pfNomineeService.getRemainingSharePercentageByPFAccountId(pfAccountId);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/remaining-share-percentage")
    public ResponseEntity<Double> getRemainingSharePercentage(@RequestBody PfNomineeDTO pfNomineeDTO) {
        log.debug("REST request to get remaining share percentage for saved PfNominee");
        Double result = pfNomineeService.getRemainingSharePercentageByPFNominee(pfNomineeDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/employee-details/{pin}")
    public ResponseEntity<EmployeeDetailsNomineeReportDTO> getEmployeeDetails(@PathVariable String pin) {
        EmployeeDetailsNomineeReportDTO employeeDetailsDTO = pfNomineeFormService.getEmployeeDetailsByPin(pin);
        return ResponseEntity.ok(employeeDetailsDTO);
    }

    @GetMapping("/get-all-by-pf-account-id/{id}")
    public ResponseEntity<List<PfNomineeDTO>> getAllPfNomineesByPfAccountId(@PathVariable Long id) {
        log.debug("REST request to get a page of PfNominees");
        List<PfNomineeDTO> result = pfNomineeService.findAllByPfAccountId(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/get-pf-accounts-with-pf-nominee")
    public ResponseEntity<List<PfAccountDTO>> getAllPfAccountsOfPfNominees(@RequestBody FilterDto filterDto) {
        log.debug("REST request to get a page of PfAccounts");
        List<PfAccountDTO> result = pfNomineeService.getPfAccountWithNominees(filterDto);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/consumed-share-percentage/{pfAccountId}")
    public ResponseEntity<Double> getTotalConsumedSharedPercentage(@PathVariable Long pfAccountId) throws Exception {
        double consumedSharedPercentage = pfNomineeService.getTotalConsumedSharedPercentageOfCurrentUser(pfAccountId);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, consumedSharedPercentage, RequestMethod.GET, ENTITY_NAME);
        return ResponseEntity.ok(consumedSharedPercentage);
    }

    @GetMapping("/get-pf-nominees-by-date")
    public ResponseEntity<List<PfNomineeDTO>> getAllPFNomineeByDateRange(
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) throws Exception {
        Page<PfNomineeDTO> page = pfNomineeService.getAllPFNomineeByDateRange(startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfNominee");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/list/{pin}")
    public ResponseEntity<List<PfNomineeDTO>> getAllPfNominees(@PathVariable String pin) throws Exception {
        log.debug("REST request to get list of PfNominees of current user");
        List<PfNomineeDTO> pfNomineeDTOList = pfNomineeService.findAllPfAccountByPin(pin);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfNomineeForm");
        return ResponseEntity.ok(pfNomineeDTOList);
    }
}
