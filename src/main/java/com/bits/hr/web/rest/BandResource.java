package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.BandService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.BandDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.Band}.
 */
@RestController
@RequestMapping("/api/employee-mgt/bands")
public class BandResource {

    private static final String ENTITY_NAME = "band";
    private final Logger log = LoggerFactory.getLogger(BandResource.class);
    private final BandService bandService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public BandResource(
        BandService bandService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.bandService = bandService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /bands} : Create a new band.
     *
     * @param bandDTO the bandDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bandDTO, or with status {@code 400 (Bad Request)} if the band has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BandDTO> createBand(@Valid @RequestBody BandDTO bandDTO) throws URISyntaxException {
        log.debug("REST request to save Band : {}", bandDTO);
        if (bandDTO.getId() != null) {
            throw new BadRequestAlertException("A new band cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BandDTO result = bandService.save(bandDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "Band");
        return ResponseEntity
            .created(new URI("/api/employee-mgt/bands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bands} : Updates an existing band.
     *
     * @param bandDTO the bandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bandDTO,
     * or with status {@code 400 (Bad Request)} if the bandDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<BandDTO> updateBand(@Valid @RequestBody BandDTO bandDTO) throws URISyntaxException {
        log.debug("REST request to update Band : {}", bandDTO);
        if (bandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BandDTO result = bandService.save(bandDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "Band");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bandDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bands} : get all the bands.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bands in body.
     */
    @GetMapping("")
    public List<BandDTO> getAllBands() {
        log.debug("REST request to get all Bands");
        return bandService.findAll();
    }


    /**
     * {@code GET  /bands} : get a page of the bands.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bands in body.
     */
    @GetMapping("/page")
    public ResponseEntity<List<BandDTO>> getNationalitiesPage(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Departments");
        Page<BandDTO> page = bandService.findAll(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    /**
     * {@code GET  /bands/:id} : get the "id" band.
     *
     * @param id the id of the bandDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bandDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BandDTO> getBand(@PathVariable Long id) {
        log.debug("REST request to get Band : {}", id);
        Optional<BandDTO> bandDTO = bandService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (bandDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, bandDTO.get(), RequestMethod.GET, "Band");
        }
        return ResponseUtil.wrapOrNotFound(bandDTO);
    }

    /**
     * {@code DELETE  /bands/:id} : delete the "id" band.
     *
     * @param id the id of the bandDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBand(@PathVariable Long id) {
        log.debug("REST request to delete Band : {}", id);
        Optional<BandDTO> bandDTO = bandService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (bandDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, bandDTO.get(), RequestMethod.DELETE, "Band");
        }
        bandService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
