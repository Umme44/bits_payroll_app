package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.DesignationService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.dto.DesignationDTO;
import java.net.URI;
import java.net.URISyntaxException;
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
 * REST controller for managing {@link com.bits.hr.domain.Designation}.
 */
@RestController
@RequestMapping("/api/employee-mgt/designations")
public class DesignationResource {

    private static final String ENTITY_NAME = "designation";
    private final Logger log = LoggerFactory.getLogger(DesignationResource.class);
    private final DesignationService designationService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public DesignationResource(
        DesignationService designationService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.designationService = designationService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /designations} : Create a new designation.
     *
     * @param designationDTO the designationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new designationDTO, or with status {@code 400 (Bad Request)} if the designation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DesignationDTO> createDesignation(@Valid @RequestBody DesignationDTO designationDTO) throws URISyntaxException {
        log.debug("REST request to save Designation : {}", designationDTO);
        if (designationDTO.getId() != null) {
            throw new BadRequestAlertException("A new designation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DesignationDTO result = designationService.save(designationDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "Designation");
        return ResponseEntity
            .created(new URI("/api/employee-mgt/designations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /designations} : Updates an existing designation.
     *
     * @param designationDTO the designationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated designationDTO,
     * or with status {@code 400 (Bad Request)} if the designationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the designationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<DesignationDTO> updateDesignation(@Valid @RequestBody DesignationDTO designationDTO) throws URISyntaxException {
        log.debug("REST request to update Designation : {}", designationDTO);
        if (designationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DesignationDTO result = designationService.save(designationDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "Designation");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, designationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /designations} : get all the designations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of designations in body.
     */
    @GetMapping("")
    public List<DesignationDTO> getAllDesignations() {
        log.debug("REST request to get all Designations");
        return designationService.findAll();
    }


    /**
     * {@code GET  /designations/page} : get a page of the designations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of designations in body.
     */
    @GetMapping("/page")
    public ResponseEntity<List<DesignationDTO>> getAllDesignations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Designations");
        Page<DesignationDTO> page = designationService.findAll(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /designations/:id} : get the "id" designation.
     *
     * @param id the id of the designationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the designationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DesignationDTO> getDesignation(@PathVariable Long id) {
        log.debug("REST request to get Designation : {}", id);
        Optional<DesignationDTO> designationDTO = designationService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (designationDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, designationDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(designationDTO);
    }

    /**
     * {@code DELETE  /designations/:id} : delete the "id" designation.
     *
     * @param id the id of the designationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDesignation(@PathVariable Long id) {
        log.debug("REST request to delete Designation : {}", id);
        Optional<DesignationDTO> designationDTO = designationService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (designationDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, designationDTO.get(), RequestMethod.DELETE, "AitConfig");
        }
        designationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
