package com.bits.hr.service;

import com.bits.hr.service.dto.VehicleRequisitionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.VehicleRequisition}.
 */
public interface VehicleRequisitionService {
    /**
     * Save a vehicleRequisition.
     *
     * @param vehicleRequisitionDTO the entity to save.
     * @return the persisted entity.
     */
    VehicleRequisitionDTO save(VehicleRequisitionDTO vehicleRequisitionDTO);

    /**
     * Get all the vehicleRequisitions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehicleRequisitionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vehicleRequisition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehicleRequisitionDTO> findOne(Long id);

    /**
     * Delete the "id" vehicleRequisition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
