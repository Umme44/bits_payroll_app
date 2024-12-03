package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.RoomRequisition;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.RoomRequisitionRepository;
import com.bits.hr.service.RoomRequisitionService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.RoomRequisitionBookingDTO;
import com.bits.hr.service.dto.RoomRequisitionDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
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
 * REST controller for managing {@link com.bits.hr.domain.RoomRequisition}.
 */
@RestController
@RequestMapping("/api/common")
public class RoomRequisitionCommonResource {

    private final Logger log = LoggerFactory.getLogger(RoomRequisitionResource.class);

    private static final String ENTITY_NAME = "roomRequisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomRequisitionService roomRequisitionService;

    private final RoomRequisitionRepository roomRequisitionRepository;

    private final CurrentEmployeeService currentEmployeeService;

    public RoomRequisitionCommonResource(
        RoomRequisitionService roomRequisitionService,
        RoomRequisitionRepository roomRequisitionRepository,
        CurrentEmployeeService currentEmployeeService
    ) {
        this.roomRequisitionService = roomRequisitionService;
        this.roomRequisitionRepository = roomRequisitionRepository;
        this.currentEmployeeService = currentEmployeeService;
    }

    /**
     * {@code POST  /room-requisitions} : Create a new roomRequisition.
     *
     * @param roomRequisitionDTO the roomRequisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomRequisitionDTO, or with status {@code 400 (Bad Request)} if the roomRequisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/room-requisitions")
    public ResponseEntity<RoomRequisitionDTO> createRoomRequisition(@Valid @RequestBody RoomRequisitionDTO roomRequisitionDTO)
        throws URISyntaxException {
        log.debug("REST request to save RoomRequisition : {}", roomRequisitionDTO);
        if (roomRequisitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomRequisition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Employee employee = currentEmployeeService.getCurrentEmployee().get();
        User user = currentEmployeeService.getCurrentUser().get();
        roomRequisitionDTO.setCreatedById(user.getId());
        roomRequisitionDTO.setRequesterId(employee.getId());
        roomRequisitionDTO.setCreatedAt(Instant.now());
        roomRequisitionDTO.setUpdatedAt(null);
        roomRequisitionDTO.setStatus(Status.APPROVED);

        RoomRequisitionDTO result = roomRequisitionService.save(roomRequisitionDTO);
        return ResponseEntity
            .created(new URI("/api/room-requisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /room-requisitions} : Updates an existing roomRequisition.
     *
     * @param roomRequisitionDTO the roomRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the roomRequisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roomRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/room-requisitions")
    public ResponseEntity<RoomRequisitionDTO> updateRoomRequisition(@Valid @RequestBody RoomRequisitionDTO roomRequisitionDTO)
        throws URISyntaxException {
        log.debug("REST request to update RoomRequisition : {}", roomRequisitionDTO);
        if (roomRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Employee employee = currentEmployeeService.getCurrentEmployee().get();
        User user = currentEmployeeService.getCurrentUser().get();
        roomRequisitionDTO.setRequesterId(employee.getId());
        roomRequisitionDTO.setUpdatedById(user.getId());
        roomRequisitionDTO.setUpdatedAt(Instant.now());
        roomRequisitionDTO.setStatus(Status.APPROVED);
        RoomRequisitionDTO result = roomRequisitionService.save(roomRequisitionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomRequisitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /room-requisitions} : get all the roomRequisitions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roomRequisitions in body.
     */
    @GetMapping("/room-requisitions")
    public ResponseEntity<List<RoomRequisitionDTO>> getAllRoomRequisitions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of RoomRequisitions");
        Page<RoomRequisitionDTO> page = roomRequisitionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /room-requisitions/:id} : get the "id" roomRequisition.
     *
     * @param id the id of the roomRequisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomRequisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/room-requisitions/{id}")
    public ResponseEntity<RoomRequisitionDTO> getRoomRequisition(@PathVariable Long id) {
        log.debug("REST request to get RoomRequisition : {}", id);
        Optional<RoomRequisitionDTO> roomRequisitionDTO = roomRequisitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomRequisitionDTO);
    }

    /**
     * {@code DELETE  /room-requisitions/:id} : delete the "id" roomRequisition.
     *
     * @param id the id of the roomRequisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/room-requisitions/{id}")
    public ResponseEntity<Void> deleteRoomRequisition(@PathVariable Long id) {
        log.debug("REST request to delete RoomRequisition : {}", id);
        roomRequisitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/room-requisitions/check-booked-room")
    public ResponseEntity<Boolean> checkRoomIsBooked(@RequestBody RoomRequisitionDTO roomRequisitionDTO) {
        if (roomRequisitionDTO.isIsFullDay() != null && roomRequisitionDTO.isIsFullDay()) {
            roomRequisitionDTO.setStartTime(0.0);
            roomRequisitionDTO.setEndTime(23.59);
        }
        boolean isRoomBooked = roomRequisitionService.checkRoomIsBookedV2(roomRequisitionDTO);
        return ResponseEntity.ok(isRoomBooked);
    }

    @GetMapping("/room-requisitions/id")
    public ResponseEntity<List<RoomRequisitionDTO>> getAllRoomRequisitionsById() {
        log.debug("REST request to get a page of RoomRequisitions");
        List<RoomRequisitionDTO> roomRequisitionList = roomRequisitionService.getAllRoomRequisitionById();
        return ResponseEntity.ok(roomRequisitionList);
    }

    @GetMapping("/room-requisitions/{roomId}/{calendarDate}")
    public ResponseEntity<RoomRequisitionBookingDTO> getAllRoomRequisitionsById(
        @PathVariable Long roomId,
        @PathVariable LocalDate calendarDate
    ) {
        log.debug("REST request to get a page of RoomRequisitions");
        //List<RoomRequisitionDTO> roomRequisitionList = roomRequisitionService.getAllRoomRequisitionForThatDate(roomId,calendarDate);
        RoomRequisitionBookingDTO roomRequisitionBookingDTO = roomRequisitionService.getAllRoomRequisitionForThatDate(roomId, calendarDate);
        return ResponseEntity.ok(roomRequisitionBookingDTO);
    }
}
