package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.CalenderYearService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.CalenderYearDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.CalenderYear}.
 */
@RestController
@RequestMapping("/api/payroll-mgt/calender-years")
public class CalenderYearResource {

    private static final String ENTITY_NAME = "calenderYear";
    private final Logger log = LoggerFactory.getLogger(CalenderYearResource.class);
    private final CalenderYearService calenderYearService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public CalenderYearResource(
        CalenderYearService calenderYearService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.calenderYearService = calenderYearService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /calender-years} : Create a new calenderYear.
     *
     * @param calenderYearDTO the calenderYearDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calenderYearDTO, or with status {@code 400 (Bad Request)} if the calenderYear has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CalenderYearDTO> createCalenderYear(@Valid @RequestBody CalenderYearDTO calenderYearDTO)
        throws URISyntaxException {
        log.debug("REST request to save CalenderYear : {}", calenderYearDTO);
        if (calenderYearDTO.getId() != null) {
            throw new BadRequestAlertException("A new calenderYear cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CalenderYearDTO result = calenderYearService.save(calenderYearDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, " CalenderYear");

        return ResponseEntity
            .created(new URI("/api/payroll-mgt/calender-years/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /calender-years} : Updates an existing calenderYear.
     *
     * @param calenderYearDTO the calenderYearDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calenderYearDTO,
     * or with status {@code 400 (Bad Request)} if the calenderYearDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calenderYearDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<CalenderYearDTO> updateCalenderYear(@Valid @RequestBody CalenderYearDTO calenderYearDTO)
        throws URISyntaxException {
        log.debug("REST request to update CalenderYear : {}", calenderYearDTO);
        if (calenderYearDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CalenderYearDTO result = calenderYearService.save(calenderYearDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "CalenderYear");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calenderYearDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /calender-years} : get all the calenderYears.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of calenderYears in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CalenderYearDTO>> getAllCalenderYears(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CalenderYears");
        Page<CalenderYearDTO> page = calenderYearService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "CalenderYear");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /calender-years/:id} : get the "id" calenderYear.
     *
     * @param id the id of the calenderYearDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calenderYearDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CalenderYearDTO> getCalenderYear(@PathVariable Long id) {
        log.debug("REST request to get CalenderYear : {}", id);
        Optional<CalenderYearDTO> calenderYearDTO = calenderYearService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (calenderYearDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, calenderYearDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(calenderYearDTO);
    }

    /**
     * {@code DELETE  /calender-years/:id} : delete the "id" calenderYear.
     *
     * @param id the id of the calenderYearDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalenderYear(@PathVariable Long id) {
        log.debug("REST request to delete CalenderYear : {}", id);
        Optional<CalenderYearDTO> calenderYearDTO = calenderYearService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (calenderYearDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, calenderYearDTO.get(), RequestMethod.DELETE, "AitConfig");
        }
        calenderYearService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
