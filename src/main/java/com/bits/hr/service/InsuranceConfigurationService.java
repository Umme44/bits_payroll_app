package com.bits.hr.service;

import com.bits.hr.service.dto.InsuranceConfigurationDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.InsuranceConfiguration}.
 */
public interface InsuranceConfigurationService {
    /**
     * Save a insuranceConfiguration.
     *
     * @param insuranceConfigurationDTO the entity to save.
     * @return the persisted entity.
     */
    InsuranceConfigurationDTO save(InsuranceConfigurationDTO insuranceConfigurationDTO);

    /**
     * Get all the insuranceConfigurations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InsuranceConfigurationDTO> findAll(Pageable pageable);

    List<InsuranceConfigurationDTO> getAllInsuranceConfiguration();

    /**
     * Get the "id" insuranceConfiguration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InsuranceConfigurationDTO> findOne(Long id);

    /**
     * Delete the "id" insuranceConfiguration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
