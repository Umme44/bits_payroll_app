package com.bits.hr.service;

import com.bits.hr.service.dto.FileTemplatesDTO;
import java.io.IOException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.bits.hr.domain.FileTemplates}.
 */
public interface FileTemplatesService {
    /**
     * Get the "id" fileTemplates.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileTemplatesDTO> findOne(Long id);

    /**
     * Delete the "id" fileTemplates.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    FileTemplatesDTO save(FileTemplatesDTO fileTemplatesDTO, MultipartFile file);

    FileTemplatesDTO update(FileTemplatesDTO fileTemplatesDTO);

    Page<FileTemplatesDTO> findAllTemplatesOfUser(String searchText, String fileType, Pageable pageable) throws IOException;

    Page<FileTemplatesDTO> findAll(String searchText, String fileType, Pageable pageable) throws IOException;
}
