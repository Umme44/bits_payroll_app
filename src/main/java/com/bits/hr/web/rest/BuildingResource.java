package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.BuildingRepository;
import com.bits.hr.service.BuildingService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.BuildingDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.Building}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class BuildingResource {

    private final Logger log = LoggerFactory.getLogger(BuildingResource.class);

    private static final String ENTITY_NAME = "building";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BuildingService buildingService;
    private final BuildingRepository buildingRepository;

    private final CurrentEmployeeService currentEmployeeService;

    public BuildingResource(
        BuildingService buildingService,
        BuildingRepository buildingRepository,
        CurrentEmployeeService currentEmployeeService
    ) {
        this.buildingService = buildingService;
        this.buildingRepository = buildingRepository;
        this.currentEmployeeService = currentEmployeeService;
    }

    /**
     * {@code POST  /buildings} : Create a new building.
     *
     * @param buildingDTO the buildingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new buildingDTO, or with status {@code 400 (Bad Request)} if the building has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/buildings")
    public ResponseEntity<BuildingDTO> createBuilding(@Valid @RequestBody BuildingDTO buildingDTO) throws URISyntaxException {
        log.debug("REST request to save Building : {}", buildingDTO);
        if (buildingDTO.getId() != null) {
            throw new BadRequestAlertException("A new building cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = currentEmployeeService.getCurrentUser().get();
        buildingDTO.setCreatedById(user.getId());
        buildingDTO.setCreatedAt(Instant.now());
        buildingDTO.setUpdatedAt(null);
        BuildingDTO result = buildingService.save(buildingDTO);
        return ResponseEntity
            .created(new URI("/api/buildings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /buildings} : Updates an existing building.
     *
     * @param buildingDTO the buildingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated buildingDTO,
     * or with status {@code 400 (Bad Request)} if the buildingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the buildingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/buildings")
    public ResponseEntity<BuildingDTO> updateBuilding(@Valid @RequestBody BuildingDTO buildingDTO) throws URISyntaxException {
        log.debug("REST request to update Building : {}", buildingDTO);
        if (buildingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        User user = currentEmployeeService.getCurrentUser().get();
        buildingDTO.setUpdatedById(user.getId());
        buildingDTO.setUpdatedAt(Instant.now());
        BuildingDTO result = buildingService.save(buildingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, buildingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /buildings} : get all the buildings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of buildings in body.
     */
    @GetMapping("/buildings")
    public ResponseEntity<List<BuildingDTO>> getAllBuildings(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Buildings");
        Page<BuildingDTO> page = buildingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /buildings/:id} : get the "id" building.
     *
     * @param id the id of the buildingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the buildingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/buildings/{id}")
    public ResponseEntity<BuildingDTO> getBuilding(@PathVariable Long id) {
        log.debug("REST request to get Building : {}", id);
        Optional<BuildingDTO> buildingDTO = buildingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(buildingDTO);
    }

    /**
     * {@code DELETE  /buildings/:id} : delete the "id" building.
     *
     * @param id the id of the buildingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/buildings/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        log.debug("REST request to delete Building : {}", id);
        buildingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/buildings/check-duplicate-buildingName/{buildingName}")
    public ResponseEntity<Boolean> checkDuplicateTitle(@PathVariable String buildingName) {
        log.debug("REST request to check duplicate TimeSlot Title : {}", buildingName);
        Boolean check = false;
        check = buildingRepository.checkTypeNameIsExists(buildingName);
        return ResponseEntity.ok(check);
    }
}
