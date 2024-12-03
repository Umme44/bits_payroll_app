package com.bits.hr.web.rest;

import com.bits.hr.domain.Floor;
import com.bits.hr.domain.User;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.FloorRepository;
import com.bits.hr.repository.RoomRepository;
import com.bits.hr.service.RoomService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FloorDTO;
import com.bits.hr.service.dto.RoomDTO;
import com.bits.hr.service.mapper.FloorMapper;
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
 * REST controller for managing {@link com.bits.hr.domain.Room}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class RoomResource {

    private final Logger log = LoggerFactory.getLogger(RoomResource.class);

    private static final String ENTITY_NAME = "room";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomService roomService;
    private final CurrentEmployeeService currentEmployeeService;
    private final FloorRepository floorRepository;
    private final RoomRepository roomRepository;
    private final FloorMapper floorMapper;

    public RoomResource(
        RoomService roomService,
        CurrentEmployeeService currentEmployeeService,
        FloorRepository floorRepository,
        RoomRepository roomRepository,
        FloorMapper floorMapper
    ) {
        this.roomService = roomService;
        this.currentEmployeeService = currentEmployeeService;
        this.floorRepository = floorRepository;
        this.roomRepository = roomRepository;
        this.floorMapper = floorMapper;
    }

    /**
     * {@code POST  /rooms} : Create a new room.
     *
     * @param roomDTO the roomDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomDTO, or with status {@code 400 (Bad Request)} if the room has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rooms")
    public ResponseEntity<RoomDTO> createRoom(@Valid @RequestBody RoomDTO roomDTO) throws URISyntaxException {
        log.debug("REST request to save Room : {}", roomDTO);
        if (roomDTO.getId() != null) {
            throw new BadRequestAlertException("A new room cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = currentEmployeeService.getCurrentUser().get();
        roomDTO.setCreatedById(user.getId());
        roomDTO.setCreatedAt(Instant.now());
        roomDTO.setUpdatedAt(null);
        RoomDTO result = roomService.save(roomDTO);
        return ResponseEntity
            .created(new URI("/api/rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rooms} : Updates an existing room.
     *
     * @param roomDTO the roomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomDTO,
     * or with status {@code 400 (Bad Request)} if the roomDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rooms")
    public ResponseEntity<RoomDTO> updateRoom(@Valid @RequestBody RoomDTO roomDTO) throws URISyntaxException {
        log.debug("REST request to update Room : {}", roomDTO);
        if (roomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        User user = currentEmployeeService.getCurrentUser().get();
        roomDTO.setUpdatedById(user.getId());
        roomDTO.setUpdatedAt(Instant.now());
        RoomDTO result = roomService.save(roomDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rooms} : get all the rooms.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rooms in body.
     */
    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDTO>> getAllRooms(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Rooms");
        Page<RoomDTO> page = roomService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rooms/:id} : get the "id" room.
     *
     * @param id the id of the roomDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rooms/{id}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable Long id) {
        log.debug("REST request to get Room : {}", id);
        Optional<RoomDTO> roomDTO = roomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomDTO);
    }

    /**
     * {@code DELETE  /rooms/:id} : delete the "id" room.
     *
     * @param id the id of the roomDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        log.debug("REST request to delete Room : {}", id);
        roomService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/rooms/get-all-floors-by-id/{buildingId}")
    public ResponseEntity<List<FloorDTO>> getAllFloorsByBuildingId(@PathVariable Long buildingId) {
        log.debug("REST request to get All Floors under same building : {}", buildingId);
        Boolean check = false;
        List<Floor> floorList = floorRepository.getAllFloorsByBuildingId(buildingId);
        List<FloorDTO> floorDTOList = floorMapper.toDto(floorList);
        return ResponseEntity.ok(floorDTOList);
    }

    @GetMapping("/rooms/check-duplicate-roomName/{buildingId}/{floorId}/{roomName}")
    public ResponseEntity<Boolean> checkDuplicateRoomName(
        @PathVariable Long buildingId,
        @PathVariable Long floorId,
        @PathVariable String roomName
    ) {
        log.debug("REST request to check duplicate Room Name: {}", roomName);
        Boolean check = false;
        check = roomRepository.checkRoomNameIsExists(buildingId, floorId, roomName);
        return ResponseEntity.ok(check);
    }
}
