package com.bits.hr.service;

import com.bits.hr.domain.EmployeeDocument;
import com.bits.hr.service.dto.EmployeeDocumentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link EmployeeDocument}.
 */
public interface EmployeeDocumentService {
    /**
     * Save a employeeDocument.
     *
     * @param employeeDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    EmployeeDocumentDTO save(String pin, EmployeeDocumentDTO employeeDocumentDTO, MultipartFile file);

    /**
     * Updates a employeeDocument.
     *
     * @param employeeDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    EmployeeDocumentDTO update(String pin, EmployeeDocumentDTO employeeDocumentDTO);

    /**
     * Partially updates a employeeDocument.
     *
     * @param employeeDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmployeeDocumentDTO> partialUpdate(EmployeeDocumentDTO employeeDocumentDTO);

    /**
     * Get all the employeeDocument.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmployeeDocumentDTO> findAll(Pageable pageable);

    Page<EmployeeDocumentDTO> getAllByPinOrderByFileName(String pin, Pageable pageable);

    /**
     * Get the "id" employeeDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeDocumentDTO> findOne(Long id);

    /**
     * Delete the "id" employeeDocument.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    boolean existsByFileNameAndPin(String fileName, String pin);

    boolean uploadAndExtractZip(MultipartFile file);

    boolean uploadAndProcess(MultipartFile file);
}
