package com.bits.hr.web.rest;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.VehicleRequisitionService;
import com.bits.hr.service.dto.VehicleRequisitionDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.VehicleRequisition}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class VehicleRequisitionResource {

    private final Logger log = LoggerFactory.getLogger(VehicleRequisitionResource.class);

    private static final String ENTITY_NAME = "vehicleRequisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleRequisitionService vehicleRequisitionService;

    public VehicleRequisitionResource(VehicleRequisitionService vehicleRequisitionService) {
        this.vehicleRequisitionService = vehicleRequisitionService;
    }

    /**
     * {@code POST  /vehicle-requisitions} : Create a new vehicleRequisition.
     *
     * @param vehicleRequisitionDTO the vehicleRequisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleRequisitionDTO, or with status {@code 400 (Bad Request)} if the vehicleRequisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vehicle-requisitions")
    public ResponseEntity<VehicleRequisitionDTO> createVehicleRequisition(@Valid @RequestBody VehicleRequisitionDTO vehicleRequisitionDTO)
        throws URISyntaxException {
        log.debug("REST request to save VehicleRequisition : {}", vehicleRequisitionDTO);
        if (vehicleRequisitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleRequisition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VehicleRequisitionDTO result = vehicleRequisitionService.save(vehicleRequisitionDTO);
        return ResponseEntity
            .created(new URI("/api/vehicle-requisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vehicle-requisitions} : Updates an existing vehicleRequisition.
     *
     * @param vehicleRequisitionDTO the vehicleRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleRequisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vehicle-requisitions")
    public ResponseEntity<VehicleRequisitionDTO> updateVehicleRequisition(@Valid @RequestBody VehicleRequisitionDTO vehicleRequisitionDTO)
        throws URISyntaxException {
        log.debug("REST request to update VehicleRequisition : {}", vehicleRequisitionDTO);
        if (vehicleRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VehicleRequisitionDTO result = vehicleRequisitionService.save(vehicleRequisitionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleRequisitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vehicle-requisitions} : get all the vehicleRequisitions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleRequisitions in body.
     */
    @GetMapping("/vehicle-requisitions")
    public ResponseEntity<List<VehicleRequisitionDTO>> getAllVehicleRequisitions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of VehicleRequisitions");
        Page<VehicleRequisitionDTO> page = vehicleRequisitionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-requisitions/:id} : get the "id" vehicleRequisition.
     *
     * @param id the id of the vehicleRequisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleRequisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vehicle-requisitions/{id}")
    public ResponseEntity<VehicleRequisitionDTO> getVehicleRequisition(@PathVariable Long id) {
        log.debug("REST request to get VehicleRequisition : {}", id);
        Optional<VehicleRequisitionDTO> vehicleRequisitionDTO = vehicleRequisitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleRequisitionDTO);
    }

    /**
     * {@code DELETE  /vehicle-requisitions/:id} : delete the "id" vehicleRequisition.
     *
     * @param id the id of the vehicleRequisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vehicle-requisitions/{id}")
    public ResponseEntity<Void> deleteVehicleRequisition(@PathVariable Long id) {
        log.debug("REST request to delete VehicleRequisition : {}", id);
        vehicleRequisitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
