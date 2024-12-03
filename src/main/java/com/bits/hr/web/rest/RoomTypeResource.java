package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.RoomTypeRepository;
import com.bits.hr.service.RoomTypeService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.RoomTypeDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.RoomType}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class RoomTypeResource {

    private final Logger log = LoggerFactory.getLogger(RoomTypeResource.class);

    private static final String ENTITY_NAME = "roomType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomTypeService roomTypeService;

    private final RoomTypeRepository roomTypeRepository;

    private final CurrentEmployeeService currentEmployeeService;

    public RoomTypeResource(
        RoomTypeService roomTypeService,
        RoomTypeRepository roomTypeRepository,
        CurrentEmployeeService currentEmployeeService
    ) {
        this.roomTypeService = roomTypeService;
        this.roomTypeRepository = roomTypeRepository;
        this.currentEmployeeService = currentEmployeeService;
    }

    /**
     * {@code POST  /room-types} : Create a new roomType.
     *
     * @param roomTypeDTO the roomTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomTypeDTO, or with status {@code 400 (Bad Request)} if the roomType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/room-types")
    public ResponseEntity<RoomTypeDTO> createRoomType(@Valid @RequestBody RoomTypeDTO roomTypeDTO) throws URISyntaxException {
        log.debug("REST request to save RoomType : {}", roomTypeDTO);
        if (roomTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = currentEmployeeService.getCurrentUser().get();
        roomTypeDTO.setCreatedById(user.getId());
        roomTypeDTO.setCreatedAt(Instant.now());
        roomTypeDTO.setUpdatedAt(null);
        RoomTypeDTO result = roomTypeService.save(roomTypeDTO);
        return ResponseEntity
            .created(new URI("/api/room-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /room-types} : Updates an existing roomType.
     *
     * @param roomTypeDTO the roomTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomTypeDTO,
     * or with status {@code 400 (Bad Request)} if the roomTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roomTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/room-types")
    public ResponseEntity<RoomTypeDTO> updateRoomType(@Valid @RequestBody RoomTypeDTO roomTypeDTO) throws URISyntaxException {
        log.debug("REST request to update RoomType : {}", roomTypeDTO);
        if (roomTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        User user = currentEmployeeService.getCurrentUser().get();
        roomTypeDTO.setUpdatedById(user.getId());
        roomTypeDTO.setUpdatedAt(Instant.now());
        RoomTypeDTO result = roomTypeService.save(roomTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /room-types} : get all the roomTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roomTypes in body.
     */
    @GetMapping("/room-types")
    public ResponseEntity<List<RoomTypeDTO>> getAllRoomTypes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of RoomTypes");
        Page<RoomTypeDTO> page = roomTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /room-types/:id} : get the "id" roomType.
     *
     * @param id the id of the roomTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/room-types/{id}")
    public ResponseEntity<RoomTypeDTO> getRoomType(@PathVariable Long id) {
        log.debug("REST request to get RoomType : {}", id);
        Optional<RoomTypeDTO> roomTypeDTO = roomTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomTypeDTO);
    }

    /**
     * {@code DELETE  /room-types/:id} : delete the "id" roomType.
     *
     * @param id the id of the roomTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/room-types/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Long id) {
        log.debug("REST request to delete RoomType : {}", id);
        roomTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/room-types/check-duplicate-typeName/{typeName}")
    public ResponseEntity<Boolean> checkDuplicateTitle(@PathVariable String typeName) {
        log.debug("REST request to check duplicate TimeSlot Title : {}", typeName);
        Boolean check = false;
        check = roomTypeRepository.checkTypeNameIsExists(typeName);
        return ResponseEntity.ok(check);
    }
}
