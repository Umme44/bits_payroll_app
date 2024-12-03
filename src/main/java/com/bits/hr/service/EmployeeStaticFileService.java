package com.bits.hr.service;

import com.bits.hr.service.dto.EmployeeIdCardSummaryDTO;
import com.bits.hr.service.dto.EmployeeStaticFileDTO;
import java.io.IOException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.bits.hr.domain.EmployeeStaticFile}.
 */
public interface EmployeeStaticFileService {
    /**
     * Save a employeeStaticFile.
     *
     * @param employeeStaticFileDTO the entity to save.
     * @return the persisted entity.
     */
    EmployeeStaticFileDTO save(EmployeeStaticFileDTO employeeStaticFileDTO);

    /**
     * Get all the employeeStaticFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmployeeStaticFileDTO> findAll(Pageable pageable);

    /**
     * Get the "id" employeeStaticFile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeStaticFileDTO> findOne(Long id);

    /**
     * Delete the "id" employeeStaticFile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Boolean idCardBatchSave(MultipartFile[] files);
    EmployeeStaticFileDTO updateExistingEmployeeStaticFile(EmployeeStaticFileDTO employeeStaticFileDTO, MultipartFile files);

    Page<EmployeeStaticFileDTO> findAllIDCards(Pageable pageable, String searchText) throws IOException;

    EmployeeStaticFileDTO getCurrentUserIdCard() throws IOException;

    String getIdCardFilePath(String pin);

    EmployeeIdCardSummaryDTO getEmployeeIdCardShortSummary();
}
