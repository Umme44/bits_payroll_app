package com.bits.hr.service;

import com.bits.hr.service.dto.DepartmentItemsDTO;
import com.bits.hr.service.dto.ItemInformationDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.ItemInformation}.
 */
public interface ItemInformationService {
    /**
     * Save a itemInformation.
     *
     * @param itemInformationDTO the entity to save.
     * @return the persisted entity.
     */
    ItemInformationDTO save(ItemInformationDTO itemInformationDTO);

    /**
     * Get all the itemInformations.
     *
     * @param departmentId
     * @param pageable     the pagination information.
     * @return the list of entities.
     */
    Page<ItemInformationDTO> findAll(Long departmentId, Pageable pageable);

    /**
     * Get the "id" itemInformation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemInformationDTO> findOne(Long id);

    /**
     * Delete the "id" itemInformation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    String generateNextItemCode();

    boolean isCodeUnique(Long id, String name);

    List<ItemInformationDTO> findByDepartmentId(long departmentId);

    ItemInformationDTO create(ItemInformationDTO itemInformationDTO);

    List<DepartmentItemsDTO> findDepartmentsAndItemsMapping();
}
