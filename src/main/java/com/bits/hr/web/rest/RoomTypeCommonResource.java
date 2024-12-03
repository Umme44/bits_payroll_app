package com.bits.hr.web.rest;

import com.bits.hr.service.RoomTypeService;
import com.bits.hr.service.dto.RoomTypeDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.RoomType}.
 */
@RestController
@RequestMapping("/api/common")
public class RoomTypeCommonResource {

    private final Logger log = LoggerFactory.getLogger(RoomTypeResource.class);

    private static final String ENTITY_NAME = "roomType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomTypeService roomTypeService;

    public RoomTypeCommonResource(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
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
}
