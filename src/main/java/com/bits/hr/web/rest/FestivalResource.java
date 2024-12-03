package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.FestivalService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FestivalDTO;
import com.bits.hr.service.search.FilterDto;
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
 * REST controller for managing {@link com.bits.hr.domain.Festival}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class FestivalResource {

    private final Logger log = LoggerFactory.getLogger(FestivalResource.class);

    private static final String ENTITY_NAME = "festival";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FestivalService festivalService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public FestivalResource(
        FestivalService festivalService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.festivalService = festivalService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /festivals} : Create a new festival.
     *
     * @param festivalDTO the festivalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new festivalDTO, or with status {@code 400 (Bad Request)} if the festival has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/festivals")
    public ResponseEntity<FestivalDTO> createFestival(@Valid @RequestBody FestivalDTO festivalDTO) throws URISyntaxException {
        log.debug("REST request to save Festival : {}", festivalDTO);
        if (festivalDTO.getId() != null) {
            throw new BadRequestAlertException("A new festival cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FestivalDTO result = festivalService.save(festivalDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "Festival");

        return ResponseEntity
            .created(new URI("/api/festivals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /festivals} : Updates an existing festival.
     *
     * @param festivalDTO the festivalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated festivalDTO,
     * or with status {@code 400 (Bad Request)} if the festivalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the festivalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/festivals")
    public ResponseEntity<FestivalDTO> updateFestival(@Valid @RequestBody FestivalDTO festivalDTO) throws URISyntaxException {
        log.debug("REST request to update Festival : {}", festivalDTO);
        if (festivalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FestivalDTO result = festivalService.save(festivalDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "Festival");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, festivalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /festivals-search} : get all the festivals.
     *
     * @param filterDto the search text information.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of festivals in body.
     */
    @PostMapping("/festivals-search")
    public ResponseEntity<List<FestivalDTO>> getAllFestivals(
        @RequestBody FilterDto filterDto,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Festivals");
        Page<FestivalDTO> page = festivalService.findAll(filterDto, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "Festival");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/festivals/list")
    public ResponseEntity<List<FestivalDTO>> getAllFestivals() {
        log.debug("REST request to get a page of Festivals");
        List<FestivalDTO> result = festivalService.findAll();
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET  /festivals/:id} : get the "id" festival.
     *
     * @param id the id of the festivalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the festivalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/festivals/{id}")
    public ResponseEntity<FestivalDTO> getFestival(@PathVariable Long id) {
        log.debug("REST request to get Festival : {}", id);
        Optional<FestivalDTO> festivalDTO = festivalService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (festivalDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, festivalDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(festivalDTO);
    }

    /**
     * {@code DELETE  /festivals/:id} : delete the "id" festival.
     *
     * @param id the id of the festivalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/festivals/{id}")
    public ResponseEntity<Void> deleteFestival(@PathVariable Long id) {
        log.debug("REST request to delete Festival : {}", id);
        Optional<FestivalDTO> festivalDTO = festivalService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (festivalDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, festivalDTO.get(), RequestMethod.DELETE, "AitConfig");
        }
        festivalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
