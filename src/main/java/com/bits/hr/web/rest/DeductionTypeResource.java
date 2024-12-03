package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.DeductionTypeService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.dto.DeductionTypeDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.DeductionType}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class DeductionTypeResource {

    private final Logger log = LoggerFactory.getLogger(DeductionTypeResource.class);

    private static final String ENTITY_NAME = "deductionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeductionTypeService deductionTypeService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public DeductionTypeResource(
        DeductionTypeService deductionTypeService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.deductionTypeService = deductionTypeService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /deduction-types} : Create a new deductionType.
     *
     * @param deductionTypeDTO the deductionTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deductionTypeDTO, or with status {@code 400 (Bad Request)} if the deductionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deduction-types")
    public ResponseEntity<DeductionTypeDTO> createDeductionType(@Valid @RequestBody DeductionTypeDTO deductionTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save DeductionType : {}", deductionTypeDTO);
        if (deductionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new deductionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeductionTypeDTO result = deductionTypeService.save(deductionTypeDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "DeductionType");
        return ResponseEntity
            .created(new URI("/api/deduction-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deduction-types} : Updates an existing deductionType.
     *
     * @param deductionTypeDTO the deductionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deductionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the deductionTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deductionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deduction-types")
    public ResponseEntity<DeductionTypeDTO> updateDeductionType(@Valid @RequestBody DeductionTypeDTO deductionTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to update DeductionType : {}", deductionTypeDTO);
        if (deductionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DeductionTypeDTO result = deductionTypeService.save(deductionTypeDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "DeductionType");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deductionTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /deduction-types} : get all the deductionTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deductionTypes in body.
     */
    @GetMapping("/deduction-types")
    public List<DeductionTypeDTO> getAllDeductionTypes() {
        log.debug("REST request to get all DeductionTypes");
        return deductionTypeService.findAll();
    }

    /**
     * {@code GET  /deduction-types/:id} : get the "id" deductionType.
     *
     * @param id the id of the deductionTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deductionTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deduction-types/{id}")
    public ResponseEntity<DeductionTypeDTO> getDeductionType(@PathVariable Long id) {
        log.debug("REST request to get DeductionType : {}", id);

        Optional<DeductionTypeDTO> deductionTypeDTO = deductionTypeService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (deductionTypeDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, deductionTypeDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(deductionTypeDTO);
    }

    /**
     * {@code DELETE  /deduction-types/:id} : delete the "id" deductionType.
     *
     * @param id the id of the deductionTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deduction-types/{id}")
    public ResponseEntity<Void> deleteDeductionType(@PathVariable Long id) {
        log.debug("REST request to delete DeductionType : {}", id);
        Optional<DeductionTypeDTO> deductionTypeDTO = deductionTypeService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (deductionTypeDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, deductionTypeDTO.get(), RequestMethod.GET, "AitConfig");
        }
        deductionTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
