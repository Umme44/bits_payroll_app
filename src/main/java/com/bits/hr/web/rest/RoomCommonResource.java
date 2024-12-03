package com.bits.hr.web.rest;

import com.bits.hr.domain.Floor;
import com.bits.hr.repository.FloorRepository;
import com.bits.hr.repository.RoomRepository;
import com.bits.hr.service.RoomService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FloorDTO;
import com.bits.hr.service.dto.RoomDTO;
import com.bits.hr.service.mapper.FloorMapper;
import java.util.List;
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

@RestController
@RequestMapping("/api/common")
public class RoomCommonResource {

    private final Logger log = LoggerFactory.getLogger(RoomCommonResource.class);

    private static final String ENTITY_NAME = "room";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomService roomService;
    private final CurrentEmployeeService currentEmployeeService;
    private final FloorRepository floorRepository;
    private final RoomRepository roomRepository;
    private final FloorMapper floorMapper;

    public RoomCommonResource(
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

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDTO>> getAllRooms(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Rooms");
        Page<RoomDTO> page = roomService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
