package com.bits.hr.service;

import com.bits.hr.domain.enumeration.OrganizationFileType;
import com.bits.hr.service.dto.FileDetailsDTO;
import com.bits.hr.service.dto.OrganizationDTO;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.bits.hr.domain.Organization}.
 */
public interface OrganizationService {
    /**
     * Save a organization.
     *
     * @param organizationDTO the entity to save.
     * @return the persisted entity.
     */
    OrganizationDTO save(OrganizationDTO organizationDTO);

    /**
     * Get all the organizations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrganizationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" organization.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationDTO> findOne(Long id);

    /**
     * Delete the "id" organization.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    OrganizationDTO createAlongMultipart(OrganizationDTO organizationDTO, List<MultipartFile> fileList);

    OrganizationDTO updateAlongMultipart(OrganizationDTO organizationDTO, List<MultipartFile> fileList);

    String getOrganizationFilePath(OrganizationFileType organizationFileType);

    FileDetailsDTO getOrganizationFile(OrganizationFileType organizationFileType, String placeHolderImagePath) throws IOException;

    OrganizationDTO getOrganizationBasic();

    OrganizationDTO getOrganizationDetails();
}
