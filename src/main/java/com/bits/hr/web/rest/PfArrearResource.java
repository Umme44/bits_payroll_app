package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.PfArrearService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.PfArrearDTO;
import com.bits.hr.service.search.QuickFilterDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.PfArrear}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class PfArrearResource {

    private final Logger log = LoggerFactory.getLogger(PfArrearResource.class);

    private static final String ENTITY_NAME = "pfArrear";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PfArrearService pfArrearService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public PfArrearResource(
        PfArrearService pfArrearService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.pfArrearService = pfArrearService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /pf-arrears} : Create a new pfArrear.
     *
     * @param pfArrearDTO the pfArrearDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pfArrearDTO, or with status {@code 400 (Bad Request)} if the pfArrear has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pf-arrears")
    public ResponseEntity<PfArrearDTO> createPfArrear(@Valid @RequestBody PfArrearDTO pfArrearDTO) throws URISyntaxException {
        log.debug("REST request to save PfArrear : {}", pfArrearDTO);
        if (pfArrearDTO.getId() != null) {
            throw new BadRequestAlertException("A new pfArrear cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PfArrearDTO result = pfArrearService.save(pfArrearDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "PfArrear");
        return ResponseEntity
            .created(new URI("/api/pf-arrears/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pf-arrears} : Updates an existing pfArrear.
     *
     * @param pfArrearDTO the pfArrearDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pfArrearDTO,
     * or with status {@code 400 (Bad Request)} if the pfArrearDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pfArrearDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pf-arrears")
    public ResponseEntity<PfArrearDTO> updatePfArrear(@Valid @RequestBody PfArrearDTO pfArrearDTO) throws URISyntaxException {
        log.debug("REST request to update PfArrear : {}", pfArrearDTO);
        if (pfArrearDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PfArrearDTO result = pfArrearService.save(pfArrearDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "PfArrear");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pfArrearDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pf-arrears} : get all the pfArrears.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pfArrears in body.
     */
    @GetMapping("/pf-arrears")
    public ResponseEntity<List<PfArrearDTO>> getAllPfArrears(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PfArrears");
        Page<PfArrearDTO> page = pfArrearService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfArrear");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/pf-arrears/search")
    public ResponseEntity<List<PfArrearDTO>> getPfArrearsByFiltering(
        @RequestBody @Valid QuickFilterDTO quickFilterDTO,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PfArrears");
        Page<PfArrearDTO> page = pfArrearService.findAll(quickFilterDTO, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfArrear");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pf-arrears/:id} : get the "id" pfArrear.
     *
     * @param id the id of the pfArrearDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pfArrearDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pf-arrears/{id}")
    public ResponseEntity<PfArrearDTO> getPfArrear(@PathVariable Long id) {
        log.debug("REST request to get PfArrear : {}", id);
        Optional<PfArrearDTO> pfArrearDTO = pfArrearService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (pfArrearDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfArrearDTO.get(), RequestMethod.GET, "PfArrear");
        }
        return ResponseUtil.wrapOrNotFound(pfArrearDTO);
    }

    /**
     * {@code DELETE  /pf-arrears/:id} : delete the "id" pfArrear.
     *
     * @param id the id of the pfArrearDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pf-arrears/{id}")
    public ResponseEntity<Void> deletePfArrear(@PathVariable Long id) {
        log.debug("REST request to delete PfArrear : {}", id);
        Optional<PfArrearDTO> pfArrearDTO = pfArrearService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (pfArrearDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfArrearDTO.get(), RequestMethod.GET, "PfArrear");
        }
        pfArrearService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
