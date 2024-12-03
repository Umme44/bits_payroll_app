package com.bits.hr.service;

import com.bits.hr.domain.RoomRequisition;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.RoomRequisitionBookingDTO;
import com.bits.hr.service.dto.RoomRequisitionDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.RoomRequisition}.
 */
public interface RoomRequisitionService {
    /**
     * Save a roomRequisition.
     *
     * @param roomRequisitionDTO the entity to save.
     * @return the persisted entity.
     */
    RoomRequisitionDTO save(RoomRequisitionDTO roomRequisitionDTO);

    /**
     * Get all the roomRequisitions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RoomRequisitionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" roomRequisition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoomRequisitionDTO> findOne(Long id);

    /**
     * Delete the "id" roomRequisition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<RoomRequisitionDTO> getAllRoomRequisitionById();

    boolean checkRoomIsBookedV2(RoomRequisitionDTO roomRequisitionDTO);

    List<RoomRequisitionDTO> getRoomRequisitionByIdAndStatus(Long requesterId, String status);

    List<RoomRequisitionDTO> getRoomRequisitionByUserIdAndStatus(Long requesterId);

    List<EmployeeMinimalDTO> getAllRoomRequester();

    RoomRequisitionBookingDTO getAllRoomRequisitionForThatDate(Long roomId, LocalDate calendarDate);
}
