package com.bits.hr.web.rest;

import com.bits.hr.service.FloorService;
import com.bits.hr.service.dto.FloorDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.Building}.
 */
@RestController
@RequestMapping("/api/common")
public class FloorCommonResource {

    private final Logger log = LoggerFactory.getLogger(FloorResource.class);

    private static final String ENTITY_NAME = "floor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FloorService floorService;

    public FloorCommonResource(FloorService floorService) {
        this.floorService = floorService;
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
}
