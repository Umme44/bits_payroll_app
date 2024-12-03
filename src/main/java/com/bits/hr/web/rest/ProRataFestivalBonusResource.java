package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.ProRataFestivalBonusService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ProRataFestivalBonusDTO;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;

/**
 * REST controller for managing {@link com.bits.hr.domain.ProRataFestivalBonus}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class ProRataFestivalBonusResource {

    private final Logger log = LoggerFactory.getLogger(ProRataFestivalBonusResource.class);

    private static final String ENTITY_NAME = "proRataFestivalBonus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProRataFestivalBonusService proRataFestivalBonusService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public ProRataFestivalBonusResource(
        ProRataFestivalBonusService proRataFestivalBonusService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.proRataFestivalBonusService = proRataFestivalBonusService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /pro-rata-festival-bonuses} : Create a new proRataFestivalBonus.
     *
     * @param proRataFestivalBonusDTO the proRataFestivalBonusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proRataFestivalBonusDTO, or with status {@code 400 (Bad Request)} if the proRataFestivalBonus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pro-rata-festival-bonuses")
    public ResponseEntity<ProRataFestivalBonusDTO> createProRataFestivalBonus(@RequestBody @Valid ProRataFestivalBonusDTO proRataFestivalBonusDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProRataFestivalBonus : {}", proRataFestivalBonusDTO);
        if (proRataFestivalBonusDTO.getId() != null) {
            throw new BadRequestAlertException("A new proRataFestivalBonus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProRataFestivalBonusDTO result = proRataFestivalBonusService.save(proRataFestivalBonusDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, " ProRataFestivalBonus");
        return ResponseEntity
            .created(new URI("/api/pro-rata-festival-bonuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pro-rata-festival-bonuses} : Updates an existing proRataFestivalBonus.
     *
     * @param proRataFestivalBonusDTO the proRataFestivalBonusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proRataFestivalBonusDTO,
     * or with status {@code 400 (Bad Request)} if the proRataFestivalBonusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proRataFestivalBonusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pro-rata-festival-bonuses")
    public ResponseEntity<ProRataFestivalBonusDTO> updateProRataFestivalBonus(@RequestBody @Valid ProRataFestivalBonusDTO proRataFestivalBonusDTO)
        throws URISyntaxException {
        log.debug("REST request to update ProRataFestivalBonus : {}", proRataFestivalBonusDTO);
        if (proRataFestivalBonusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProRataFestivalBonusDTO result = proRataFestivalBonusService.save(proRataFestivalBonusDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, " ProRataFestivalBonus");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proRataFestivalBonusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pro-rata-festival-bonuses} : get all the proRataFestivalBonuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proRataFestivalBonuses in body.
     */
    @GetMapping("/pro-rata-festival-bonuses")
    public ResponseEntity<List<ProRataFestivalBonusDTO>> getAllProRataFestivalBonuses(
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ProRataFestivalBonuses");
        Page<ProRataFestivalBonusDTO> page = proRataFestivalBonusService.findAll(searchText, startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "ProRataFestivalBonus");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pro-rata-festival-bonuses/:id} : get the "id" proRataFestivalBonus.
     *
     * @param id the id of the proRataFestivalBonusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proRataFestivalBonusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pro-rata-festival-bonuses/{id}")
    public ResponseEntity<ProRataFestivalBonusDTO> getProRataFestivalBonus(@PathVariable Long id) {
        log.debug("REST request to get ProRataFestivalBonus : {}", id);
        Optional<ProRataFestivalBonusDTO> proRataFestivalBonusDTO = proRataFestivalBonusService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (proRataFestivalBonusDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, proRataFestivalBonusDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(proRataFestivalBonusDTO);
    }

    /**
     * {@code DELETE  /pro-rata-festival-bonuses/:id} : delete the "id" proRataFestivalBonus.
     *
     * @param id the id of the proRataFestivalBonusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pro-rata-festival-bonuses/{id}")
    public ResponseEntity<Void> deleteProRataFestivalBonus(@PathVariable Long id) {
        log.debug("REST request to delete ProRataFestivalBonus : {}", id);
        Optional<ProRataFestivalBonusDTO> proRataFestivalBonusDTO = proRataFestivalBonusService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (proRataFestivalBonusDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, proRataFestivalBonusDTO.get(), RequestMethod.DELETE, "AitConfig");
        }
        proRataFestivalBonusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
