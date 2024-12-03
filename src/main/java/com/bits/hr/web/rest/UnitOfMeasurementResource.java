package com.bits.hr.web.rest;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.UnitOfMeasurementService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.UnitOfMeasurementDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.UnitOfMeasurement}.
 */
@RestController
@RequestMapping("/api/procurement-mgt")
public class UnitOfMeasurementResource {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasurementResource.class);

    private static final String ENTITY_NAME = "unitOfMeasurement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitOfMeasurementService unitOfMeasurementService;

    private final CurrentEmployeeService currentEmployeeService;

    public UnitOfMeasurementResource(UnitOfMeasurementService unitOfMeasurementService, CurrentEmployeeService currentEmployeeService) {
        this.unitOfMeasurementService = unitOfMeasurementService;
        this.currentEmployeeService = currentEmployeeService;
    }

    /**
     * {@code POST  /unit-of-measurements} : Create a new unitOfMeasurement.
     *
     * @param unitOfMeasurementDTO the unitOfMeasurementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitOfMeasurementDTO, or with status {@code 400 (Bad Request)} if the unitOfMeasurement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unit-of-measurements")
    public ResponseEntity<UnitOfMeasurementDTO> createUnitOfMeasurement(@Valid @RequestBody UnitOfMeasurementDTO unitOfMeasurementDTO)
        throws URISyntaxException {
        log.debug("REST request to save UnitOfMeasurement : {}", unitOfMeasurementDTO);
        if (unitOfMeasurementDTO.getId() != null) {
            throw new BadRequestAlertException("A new unitOfMeasurement cannot already have an ID", ENTITY_NAME, "idexists");
        }

        unitOfMeasurementDTO.setCreatedById(currentEmployeeService.getCurrentUserId().get());

        UnitOfMeasurementDTO result = unitOfMeasurementService.save(unitOfMeasurementDTO);
        return ResponseEntity
            .created(new URI("/api/unit-of-measurements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unit-of-measurements} : Updates an existing unitOfMeasurement.
     *
     * @param unitOfMeasurementDTO the unitOfMeasurementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitOfMeasurementDTO,
     * or with status {@code 400 (Bad Request)} if the unitOfMeasurementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitOfMeasurementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unit-of-measurements")
    public ResponseEntity<UnitOfMeasurementDTO> updateUnitOfMeasurement(@Valid @RequestBody UnitOfMeasurementDTO unitOfMeasurementDTO)
        throws URISyntaxException {
        log.debug("REST request to update UnitOfMeasurement : {}", unitOfMeasurementDTO);
        if (unitOfMeasurementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        unitOfMeasurementDTO.setUpdatedById(currentEmployeeService.getCurrentUserId().get());

        UnitOfMeasurementDTO result = unitOfMeasurementService.save(unitOfMeasurementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitOfMeasurementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /unit-of-measurements} : get all the unitOfMeasurements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitOfMeasurements in body.
     */
    @GetMapping("/unit-of-measurements")
    public ResponseEntity<List<UnitOfMeasurementDTO>> getAllUnitOfMeasurements(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of UnitOfMeasurements");
        Page<UnitOfMeasurementDTO> page = unitOfMeasurementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unit-of-measurements/:id} : get the "id" unitOfMeasurement.
     *
     * @param id the id of the unitOfMeasurementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitOfMeasurementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unit-of-measurements/{id}")
    public ResponseEntity<UnitOfMeasurementDTO> getUnitOfMeasurement(@PathVariable Long id) {
        log.debug("REST request to get UnitOfMeasurement : {}", id);
        Optional<UnitOfMeasurementDTO> unitOfMeasurementDTO = unitOfMeasurementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitOfMeasurementDTO);
    }

    /**
     * {@code DELETE  /unit-of-measurements/:id} : delete the "id" unitOfMeasurement.
     *
     * @param id the id of the unitOfMeasurementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unit-of-measurements/{id}")
    public ResponseEntity<Void> deleteUnitOfMeasurement(@PathVariable Long id) {
        log.debug("REST request to delete UnitOfMeasurement : {}", id);
        unitOfMeasurementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/unit-of-measurements/is-name-unique")
    public ResponseEntity<Boolean> findNameIsUnique(@RequestParam(required = false) Long id, @RequestParam String name) {
        log.debug("REST request to get measure unit name is unique");
        boolean result = unitOfMeasurementService.isNameUnique(id, name);
        return ResponseEntity.ok().body(result);
    }
}
