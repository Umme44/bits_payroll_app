package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EducationDetailsService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.dto.EducationDetailsDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.EducationDetails}.
 */
@RestController
@RequestMapping("/api/employee-mgt/education-details")
public class EducationDetailsResource {

    private static final String ENTITY_NAME = "educationDetails";
    private final Logger log = LoggerFactory.getLogger(EducationDetailsResource.class);
    private final EducationDetailsService educationDetailsService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public EducationDetailsResource(
        EducationDetailsService educationDetailsService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.educationDetailsService = educationDetailsService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /education-details} : Create a new educationDetails.
     *
     * @param educationDetailsDTO the educationDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new educationDetailsDTO, or with status {@code 400 (Bad Request)} if the educationDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EducationDetailsDTO> createEducationDetails(@Valid @RequestBody EducationDetailsDTO educationDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save EducationDetails : {}", educationDetailsDTO);
        if (educationDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new educationDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EducationDetailsDTO result = educationDetailsService.save(educationDetailsDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "EducationDetails");
        return ResponseEntity
            .created(new URI("/api/employee-mgt/education-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /education-details} : Updates an existing educationDetails.
     *
     * @param educationDetailsDTO the educationDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated educationDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the educationDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the educationDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<EducationDetailsDTO> updateEducationDetails(@Valid @RequestBody EducationDetailsDTO educationDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to update EducationDetails : {}", educationDetailsDTO);
        if (educationDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EducationDetailsDTO result = educationDetailsService.save(educationDetailsDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "EducationDetails");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, educationDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /education-details} : get all the educationDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of educationDetails in body.
     */
    @GetMapping("")
    public List<EducationDetailsDTO> getAllEducationDetails() {
        log.debug("REST request to get all EducationDetails");
        return educationDetailsService.findAll();
    }

    /**
     * {@code GET  /education-details} : get all the educationDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of educationDetails in body.
     */
    @GetMapping("/get-by-employee/{employeeId}")
    public List<EducationDetailsDTO> getEducationDetailsByEmployeeId(@PathVariable long employeeId) {
        log.debug("REST request to get all EducationDetails");
        return educationDetailsService.findAllByEmployeeId(employeeId);
    }

    /**
     * {@code GET  /education-details/:id} : get the "id" educationDetails.
     *
     * @param id the id of the educationDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the educationDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EducationDetailsDTO> getEducationDetails(@PathVariable Long id) {
        log.debug("REST request to get EducationDetails : {}", id);
        Optional<EducationDetailsDTO> educationDetailsDTO = educationDetailsService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (educationDetailsDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, educationDetailsDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(educationDetailsDTO);
    }

    /**
     * {@code DELETE  /education-details/:id} : delete the "id" educationDetails.
     *
     * @param id the id of the educationDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationDetails(@PathVariable Long id) {
        log.debug("REST request to delete EducationDetails : {}", id);
        Optional<EducationDetailsDTO> educationDetailsDTO = educationDetailsService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (educationDetailsDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, educationDetailsDTO.get(), RequestMethod.DELETE, "AitConfig");
        }
        educationDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
