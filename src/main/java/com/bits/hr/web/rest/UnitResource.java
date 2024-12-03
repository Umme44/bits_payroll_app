package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.UnitService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.UnitDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.Unit}.
 */
@RestController
@RequestMapping("/api/employee-mgt/units")
public class UnitResource {

    private static final String ENTITY_NAME = "unit";
    private final Logger log = LoggerFactory.getLogger(UnitResource.class);
    private final UnitService unitService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public UnitResource(
        UnitService unitService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.unitService = unitService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /units} : Create a new unit.
     *
     * @param unitDTO the unitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitDTO, or with status {@code 400 (Bad Request)} if the unit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UnitDTO> createUnit(@Valid @RequestBody UnitDTO unitDTO) throws URISyntaxException {
        log.debug("REST request to save Unit : {}", unitDTO);
        if (unitDTO.getId() != null) {
            throw new BadRequestAlertException("A new unit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UnitDTO result = unitService.save(unitDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "Unit");
        return ResponseEntity
            .created(new URI("/api/employee-mgt/units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /units} : Updates an existing unit.
     *
     * @param unitDTO the unitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitDTO,
     * or with status {@code 400 (Bad Request)} if the unitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<UnitDTO> updateUnit(@Valid @RequestBody UnitDTO unitDTO) throws URISyntaxException {
        log.debug("REST request to update Unit : {}", unitDTO);
        if (unitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UnitDTO result = unitService.save(unitDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "Unit");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /units} : get all the units.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of units in body.
     */
    @GetMapping("")
    public List<UnitDTO> getAllUnits() {
        log.debug("REST request to get all Units");
        return unitService.findAll();
    }

    /**
     * {@code GET  /units} : get a page of the units.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of units in body.
     */
    @GetMapping("/page")
    public ResponseEntity<List<UnitDTO>> getPageOfUnits(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Departments");
        Page<UnitDTO> page = unitService.findAll(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /units/:id} : get the "id" unit.
     *
     * @param id the id of the unitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UnitDTO> getUnit(@PathVariable Long id) {
        log.debug("REST request to get Unit : {}", id);
        Optional<UnitDTO> unitDTO = unitService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (unitDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, unitDTO.get(), RequestMethod.GET, "Unit");
        }
        return ResponseUtil.wrapOrNotFound(unitDTO);
    }

    /**
     * {@code DELETE  /units/:id} : delete the "id" unit.
     *
     * @param id the id of the unitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id) {
        log.debug("REST request to delete Unit : {}", id);
        Optional<UnitDTO> unitDTO = unitService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (unitDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, unitDTO.get(), RequestMethod.DELETE, "Unit");
        }
        unitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
