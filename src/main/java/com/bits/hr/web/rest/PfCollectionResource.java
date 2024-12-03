package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.PfCollectionType;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.PfCollectionService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.dto.PfCollectionDTO;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
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
 * REST controller for managing {@link com.bits.hr.domain.PfCollection}.
 */
@RestController
@RequestMapping("/api/pf-mgt")
public class PfCollectionResource {

    private final Logger log = LoggerFactory.getLogger(PfCollectionResource.class);

    private static final String ENTITY_NAME = "pfCollection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PfCollectionService pfCollectionService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public PfCollectionResource(
        PfCollectionService pfCollectionService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.pfCollectionService = pfCollectionService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /pf-collections} : Create a new pfCollection.
     *
     * @param pfCollectionDTO the pfCollectionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pfCollectionDTO, or with status {@code 400 (Bad Request)} if the pfCollection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pf-collections")
    public ResponseEntity<PfCollectionDTO> createPfCollection(@Valid @RequestBody PfCollectionDTO pfCollectionDTO)
        throws URISyntaxException {
        log.debug("REST request to save PfCollection : {}", pfCollectionDTO);
        if (pfCollectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new pfCollection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PfCollectionDTO result = pfCollectionService.create(pfCollectionDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "PfCollection");
        return ResponseEntity
            .created(new URI("/api/pf-collections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pf-collections} : Updates an existing pfCollection.
     *
     * @param pfCollectionDTO the pfCollectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pfCollectionDTO,
     * or with status {@code 400 (Bad Request)} if the pfCollectionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pfCollectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pf-collections")
    public ResponseEntity<PfCollectionDTO> updatePfCollection(@Valid @RequestBody PfCollectionDTO pfCollectionDTO)
        throws URISyntaxException {
        log.debug("REST request to update PfCollection : {}", pfCollectionDTO);
        if (pfCollectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PfCollectionDTO result = pfCollectionService.update(pfCollectionDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "PfCollection");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pfCollectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pf-collections} : get all the pfCollections.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pfCollections in body.
     */
    @GetMapping("/pf-collections")
    public ResponseEntity<List<PfCollectionDTO>> getAllPfCollections(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) Long pfAccountId,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) PfCollectionType pfCollectionType
    ) {
        log.debug("REST request to get a page of PfCollections");
        Page<PfCollectionDTO> page = pfCollectionService.findAll(pageable, pfAccountId, year, month, pfCollectionType);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfCollection");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pf-collections/:id} : get the "id" pfCollection.
     *
     * @param id the id of the pfCollectionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pfCollectionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pf-collections/{id}")
    public ResponseEntity<PfCollectionDTO> getPfCollection(@PathVariable Long id) {
        log.debug("REST request to get PfCollection : {}", id);
        Optional<PfCollectionDTO> pfCollectionDTO = pfCollectionService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (pfCollectionDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfCollectionDTO.get(), RequestMethod.GET, "PfCollection");
        }
        return ResponseUtil.wrapOrNotFound(pfCollectionDTO);
    }

    /**
     * {@code DELETE  /pf-collections/:id} : delete the "id" pfCollection.
     *
     * @param id the id of the pfCollectionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pf-collections/{id}")
    public ResponseEntity<Void> deletePfCollection(@PathVariable Long id) {
        log.debug("REST request to delete PfCollection : {}", id);
        Optional<PfCollectionDTO> pfCollectionDTO = pfCollectionService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (pfCollectionDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfCollectionDTO.get(), RequestMethod.GET, "PfCollection");
        }
        pfCollectionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
