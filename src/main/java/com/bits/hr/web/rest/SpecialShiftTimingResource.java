package com.bits.hr.web.rest;

import com.bits.hr.domain.SpecialShiftTiming;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.SpecialShiftTimingService;
import com.bits.hr.service.dto.SpecialShiftTimingDTO;
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
 * REST controller for managing {@link SpecialShiftTiming}.
 */
@RestController
@RequestMapping("/api/attendance-mgt")
public class SpecialShiftTimingResource {

    private final Logger log = LoggerFactory.getLogger(SpecialShiftTimingResource.class);

    private static final String ENTITY_NAME = "SpecialShiftTiming";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialShiftTimingService specialShiftTimingService;

    public SpecialShiftTimingResource(SpecialShiftTimingService specialShiftTimingService) {
        this.specialShiftTimingService = specialShiftTimingService;
    }

    /**
     * {@code POST  /special-shift-timings} : Create a new SpecialShiftTiming.
     *
     * @param specialShiftTimingDTO the SpecialShiftTimingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new SpecialShiftTimingDTO, or with status {@code 400 (Bad Request)} if the SpecialShiftTiming has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/special-shift-timings")
    public ResponseEntity<SpecialShiftTimingDTO> createSpecialShiftTiming(@Valid @RequestBody SpecialShiftTimingDTO specialShiftTimingDTO)
        throws URISyntaxException {
        log.debug("REST request to save SpecialShiftTiming : {}", specialShiftTimingDTO);
        if (specialShiftTimingDTO.getId() != null) {
            throw new BadRequestAlertException("A new SpecialShiftTiming cannot already have an ID", ENTITY_NAME, "idexists");
        }
        specialShiftTimingDTO.setCreatedAt(Instant.now());
        SpecialShiftTimingDTO result = specialShiftTimingService.save(specialShiftTimingDTO);
        return ResponseEntity
            .created(new URI("/api/attendance-mgt/special-shift-timings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /special-shift-timings} : Updates an existing SpecialShiftTiming.
     *
     * @param specialShiftTimingDTO the SpecialShiftTimingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated SpecialShiftTimingDTO,
     * or with status {@code 400 (Bad Request)} if the SpecialShiftTimingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the SpecialShiftTimingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/special-shift-timings")
    public ResponseEntity<SpecialShiftTimingDTO> updateSpecialShiftTiming(@Valid @RequestBody SpecialShiftTimingDTO specialShiftTimingDTO)
        throws URISyntaxException {
        log.debug("REST request to update SpecialShiftTiming : {}", specialShiftTimingDTO);
        if (specialShiftTimingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        specialShiftTimingDTO.setUpdatedAt(Instant.now());
        SpecialShiftTimingDTO result = specialShiftTimingService.save(specialShiftTimingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialShiftTimingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /special-shift-timings} : get all the SpecialShiftTimings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of SpecialShiftTimings in body.
     */
    @GetMapping("/special-shift-timings")
    public ResponseEntity<List<SpecialShiftTimingDTO>> getAllSpecialShiftTimings(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of SpecialShiftTimings");
        Page<SpecialShiftTimingDTO> page = specialShiftTimingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /special-shift-timings/:id} : get the "id" SpecialShiftTiming.
     *
     * @param id the id of the SpecialShiftTimingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the SpecialShiftTimingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/special-shift-timings/{id}")
    public ResponseEntity<SpecialShiftTimingDTO> getSpecialShiftTiming(@PathVariable Long id) {
        log.debug("REST request to get SpecialShiftTiming : {}", id);
        Optional<SpecialShiftTimingDTO> SpecialShiftTimingDTO = specialShiftTimingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(SpecialShiftTimingDTO);
    }

    /**
     * {@code DELETE  /special-shift-timings/:id} : delete the "id" SpecialShiftTiming.
     *
     * @param id the id of the SpecialShiftTimingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/special-shift-timings/{id}")
    public ResponseEntity<Void> deleteSpecialShiftTiming(@PathVariable Long id) {
        log.debug("REST request to delete SpecialShiftTiming : {}", id);
        specialShiftTimingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
