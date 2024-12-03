package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.FestivalBonusConfigService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FestivalBonusConfigDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.FestivalBonusConfig}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class FestivalBonusConfigResource {

    private final Logger log = LoggerFactory.getLogger(FestivalBonusConfigResource.class);

    private static final String ENTITY_NAME = "festivalBonusConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FestivalBonusConfigService festivalBonusConfigService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public FestivalBonusConfigResource(
        FestivalBonusConfigService festivalBonusConfigService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.festivalBonusConfigService = festivalBonusConfigService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /festival-bonus-configs} : Create a new festivalBonusConfig.
     *
     * @param festivalBonusConfigDTO the festivalBonusConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new festivalBonusConfigDTO, or with status {@code 400 (Bad Request)} if the festivalBonusConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/festival-bonus-configs")
    public ResponseEntity<FestivalBonusConfigDTO> createFestivalBonusConfig(
        @Valid @RequestBody FestivalBonusConfigDTO festivalBonusConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FestivalBonusConfig : {}", festivalBonusConfigDTO);
        if (festivalBonusConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new festivalBonusConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FestivalBonusConfigDTO result = festivalBonusConfigService.save(festivalBonusConfigDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "FestivalBonusConfig");
        return ResponseEntity
            .created(new URI("/api/festival-bonus-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /festival-bonus-configs} : Updates an existing festivalBonusConfig.
     *
     * @param festivalBonusConfigDTO the festivalBonusConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated festivalBonusConfigDTO,
     * or with status {@code 400 (Bad Request)} if the festivalBonusConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the festivalBonusConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/festival-bonus-configs")
    public ResponseEntity<FestivalBonusConfigDTO> updateFestivalBonusConfig(
        @Valid @RequestBody FestivalBonusConfigDTO festivalBonusConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FestivalBonusConfig : {}", festivalBonusConfigDTO);
        if (festivalBonusConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FestivalBonusConfigDTO result = festivalBonusConfigService.save(festivalBonusConfigDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "FestivalBonusConfig");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, festivalBonusConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /festival-bonus-configs} : get all the festivalBonusConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of festivalBonusConfigs in body.
     */
    @GetMapping("/festival-bonus-configs")
    public List<FestivalBonusConfigDTO> getAllFestivalBonusConfigs() {
        log.debug("REST request to get all FestivalBonusConfigs");
        return festivalBonusConfigService.findAll();
    }

    /**
     * {@code GET  /festival-bonus-configs/:id} : get the "id" festivalBonusConfig.
     *
     * @param id the id of the festivalBonusConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the festivalBonusConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/festival-bonus-configs/{id}")
    public ResponseEntity<FestivalBonusConfigDTO> getFestivalBonusConfig(@PathVariable Long id) {
        log.debug("REST request to get FestivalBonusConfig : {}", id);
        Optional<FestivalBonusConfigDTO> festivalBonusConfigDTO = festivalBonusConfigService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (festivalBonusConfigDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, festivalBonusConfigDTO.get(), RequestMethod.GET, "FestivalBonusConfig");
        }
        return ResponseUtil.wrapOrNotFound(festivalBonusConfigDTO);
    }

    /**
     * {@code DELETE  /festival-bonus-configs/:id} : delete the "id" festivalBonusConfig.
     *
     * @param id the id of the festivalBonusConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/festival-bonus-configs/{id}")
    public ResponseEntity<Void> deleteFestivalBonusConfig(@PathVariable Long id) {
        log.debug("REST request to delete FestivalBonusConfig : {}", id);
        Optional<FestivalBonusConfigDTO> festivalBonusConfigDTO = festivalBonusConfigService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (festivalBonusConfigDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, festivalBonusConfigDTO.get(), RequestMethod.DELETE, "FestivalBonusConfig");
        }
        festivalBonusConfigService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
