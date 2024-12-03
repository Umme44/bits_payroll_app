package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.FloorRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.FloorService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FloorDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
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
 * REST controller for managing {@link com.bits.hr.domain.Floor}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class FloorResource {

    private final Logger log = LoggerFactory.getLogger(FloorResource.class);

    private static final String ENTITY_NAME = "floor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FloorService floorService;

    private final FloorRepository floorRepository;

    private final CurrentEmployeeService currentEmployeeService;

    public FloorResource(FloorService floorService, FloorRepository floorRepository, CurrentEmployeeService currentEmployeeService) {
        this.floorService = floorService;
        this.floorRepository = floorRepository;
        this.currentEmployeeService = currentEmployeeService;
    }

    /**
     * {@code POST  /floors} : Create a new floor.
     *
     * @param floorDTO the floorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new floorDTO, or with status {@code 400 (Bad Request)} if the floor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/floors")
    public ResponseEntity<FloorDTO> createFloor(@Valid @RequestBody FloorDTO floorDTO) throws URISyntaxException {
        log.debug("REST request to save Floor : {}", floorDTO);
        if (floorDTO.getId() != null) {
            throw new BadRequestAlertException("A new floor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = currentEmployeeService.getCurrentUser().get();
        floorDTO.setCreatedById(user.getId());
        floorDTO.setCreatedAt(Instant.now());
        floorDTO.setUpdatedAt(null);
        FloorDTO result = floorService.save(floorDTO);

        return ResponseEntity
            .created(new URI("/api/floors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /floors} : Updates an existing floor.
     *
     * @param floorDTO the floorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated floorDTO,
     * or with status {@code 400 (Bad Request)} if the floorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the floorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/floors")
    public ResponseEntity<FloorDTO> updateFloor(@Valid @RequestBody FloorDTO floorDTO) throws URISyntaxException {
        log.debug("REST request to update Floor : {}", floorDTO);
        if (floorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        User user = currentEmployeeService.getCurrentUser().get();
        floorDTO.setUpdatedById(user.getId());
        floorDTO.setUpdatedAt(Instant.now());
        FloorDTO result = floorService.save(floorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, floorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /floors} : get all the floors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of floors in body.
     */
    @GetMapping("/floors")
    public ResponseEntity<List<FloorDTO>> getAllFloors(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Floors");
        Page<FloorDTO> page = floorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /floors/:id} : get the "id" floor.
     *
     * @param id the id of the floorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the floorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/floors/{id}")
    public ResponseEntity<FloorDTO> getFloor(@PathVariable Long id) {
        log.debug("REST request to get Floor : {}", id);
        Optional<FloorDTO> floorDTO = floorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(floorDTO);
    }

    /**
     * {@code DELETE  /floors/:id} : delete the "id" floor.
     *
     * @param id the id of the floorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/floors/{id}")
    public ResponseEntity<Void> deleteFloor(@PathVariable Long id) {
        log.debug("REST request to delete Floor : {}", id);
        floorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/floors/check-duplicate-floorName/{buildingId}/{floorName}")
    public ResponseEntity<Boolean> checkDuplicateFloorName(@PathVariable Long buildingId, @PathVariable String floorName) {
        log.debug("REST request to check duplicate TimeSlot Title : {}", floorName);
        Boolean check = false;
        check = floorRepository.checkFloorNameIsExists(buildingId, floorName);
        return ResponseEntity.ok(check);
    }
}
