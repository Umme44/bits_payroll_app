package com.bits.hr.web.rest;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.AreaService;
import com.bits.hr.service.dto.AreaDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.Area}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class AreaResource {

    private final Logger log = LoggerFactory.getLogger(AreaResource.class);

    private static final String ENTITY_NAME = "area";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AreaService areaService;

    public AreaResource(AreaService areaService) {
        this.areaService = areaService;
    }

    /**
     * {@code POST  /areas} : Create a new area.
     *
     * @param areaDTO the areaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new areaDTO, or with status {@code 400 (Bad Request)} if the area has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/areas")
    public ResponseEntity<AreaDTO> createArea(@Valid @RequestBody AreaDTO areaDTO) throws URISyntaxException {
        log.debug("REST request to save Area : {}", areaDTO);
        if (areaDTO.getId() != null) {
            throw new BadRequestAlertException("A new area cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AreaDTO result = areaService.save(areaDTO);
        return ResponseEntity
            .created(new URI("/api/areas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /areas} : Updates an existing area.
     *
     * @param areaDTO the areaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated areaDTO,
     * or with status {@code 400 (Bad Request)} if the areaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the areaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/areas")
    public ResponseEntity<AreaDTO> updateArea(@Valid @RequestBody AreaDTO areaDTO) throws URISyntaxException {
        log.debug("REST request to update Area : {}", areaDTO);
        if (areaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AreaDTO result = areaService.save(areaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, areaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /areas} : get all the areas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of areas in body.
     */
    @GetMapping("/areas")
    public ResponseEntity<List<AreaDTO>> getAllAreas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Areas");
        Page<AreaDTO> page = areaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /areas/:id} : get the "id" area.
     *
     * @param id the id of the areaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the areaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/areas/{id}")
    public ResponseEntity<AreaDTO> getArea(@PathVariable Long id) {
        log.debug("REST request to get Area : {}", id);
        Optional<AreaDTO> areaDTO = areaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(areaDTO);
    }

    /**
     * {@code DELETE  /areas/:id} : delete the "id" area.
     *
     * @param id the id of the areaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/areas/{id}")
    public ResponseEntity<Void> deleteArea(@PathVariable Long id) {
        log.debug("REST request to delete Area : {}", id);
        areaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
