package com.bits.hr.web.rest;

import com.bits.hr.service.BuildingService;
import com.bits.hr.service.dto.BuildingDTO;
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
public class BuildingCommonResource {

    private final Logger log = LoggerFactory.getLogger(BuildingResource.class);

    private static final String ENTITY_NAME = "building";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BuildingService buildingService;

    public BuildingCommonResource(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

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
}
