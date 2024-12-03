package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.NationalityService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.DepartmentDTO;
import com.bits.hr.service.dto.NationalityDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.Nationality}.
 */
@RestController
@RequestMapping("/api/employee-mgt/nationalities")
public class NationalityResource {

    private static final String ENTITY_NAME = "nationality";
    private final Logger log = LoggerFactory.getLogger(NationalityResource.class);
    private final NationalityService nationalityService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public NationalityResource(
        NationalityService nationalityService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.nationalityService = nationalityService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /nationalities} : Create a new nationality.
     *
     * @param nationalityDTO the nationalityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nationalityDTO, or with status {@code 400 (Bad Request)} if the nationality has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NationalityDTO> createNationality(@Valid @RequestBody NationalityDTO nationalityDTO) throws URISyntaxException {
        log.debug("REST request to save Nationality : {}", nationalityDTO);
        if (nationalityDTO.getId() != null) {
            throw new BadRequestAlertException("A new nationality cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NationalityDTO result = nationalityService.save(nationalityDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "Nationality");
        return ResponseEntity
            .created(new URI("/api/employee-mgt/nationalities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nationalities} : Updates an existing nationality.
     *
     * @param nationalityDTO the nationalityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nationalityDTO,
     * or with status {@code 400 (Bad Request)} if the nationalityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nationalityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<NationalityDTO> updateNationality(@Valid @RequestBody NationalityDTO nationalityDTO) throws URISyntaxException {
        log.debug("REST request to update Nationality : {}", nationalityDTO);
        if (nationalityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NationalityDTO result = nationalityService.save(nationalityDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "Nationality");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nationalityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /nationalities} : get all the nationalities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nationalities in body.
     */
    @GetMapping("")
    public List<NationalityDTO> getAllNationalities() {
        log.debug("REST request to get all Nationalities");
        return nationalityService.findAll();
    }

    @GetMapping("/page")
    public ResponseEntity<List<NationalityDTO>> getNationalitiesPage(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Departments");
        Page<NationalityDTO> page = nationalityService.findAll(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nationalities/:id} : get the "id" nationality.
     *
     * @param id the id of the nationalityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nationalityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NationalityDTO> getNationality(@PathVariable Long id) {
        log.debug("REST request to get Nationality : {}", id);
        Optional<NationalityDTO> nationalityDTO = nationalityService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (nationalityDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, nationalityDTO.get(), RequestMethod.GET, "Nationality");
        }
        return ResponseUtil.wrapOrNotFound(nationalityDTO);
    }

    /**
     * {@code DELETE  /nationalities/:id} : delete the "id" nationality.
     *
     * @param id the id of the nationalityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNationality(@PathVariable Long id) {
        log.debug("REST request to delete Nationality : {}", id);
        Optional<NationalityDTO> nationalityDTO = nationalityService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (nationalityDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, nationalityDTO.get(), RequestMethod.DELETE, "Nationality");
        }
        nationalityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
