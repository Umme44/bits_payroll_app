package com.bits.hr.service.impl;

import com.bits.hr.domain.InsuranceConfiguration;
import com.bits.hr.repository.InsuranceConfigurationRepository;
import com.bits.hr.service.InsuranceConfigurationService;
import com.bits.hr.service.dto.InsuranceConfigurationDTO;
import com.bits.hr.service.mapper.InsuranceConfigurationMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InsuranceConfiguration}.
 */
@Service
@Transactional
public class InsuranceConfigurationServiceImpl implements InsuranceConfigurationService {

    private final Logger log = LoggerFactory.getLogger(InsuranceConfigurationServiceImpl.class);

    private final InsuranceConfigurationRepository insuranceConfigurationRepository;

    private final InsuranceConfigurationMapper insuranceConfigurationMapper;

    public InsuranceConfigurationServiceImpl(
        InsuranceConfigurationRepository insuranceConfigurationRepository,
        InsuranceConfigurationMapper insuranceConfigurationMapper
    ) {
        this.insuranceConfigurationRepository = insuranceConfigurationRepository;
        this.insuranceConfigurationMapper = insuranceConfigurationMapper;
    }

    @Override
    public InsuranceConfigurationDTO save(InsuranceConfigurationDTO insuranceConfigurationDTO) {
        log.debug("Request to save InsuranceConfiguration : {}", insuranceConfigurationDTO);
        InsuranceConfiguration insuranceConfiguration = insuranceConfigurationMapper.toEntity(insuranceConfigurationDTO);
        insuranceConfiguration = insuranceConfigurationRepository.save(insuranceConfiguration);
        return insuranceConfigurationMapper.toDto(insuranceConfiguration);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InsuranceConfigurationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InsuranceConfigurations");
        return insuranceConfigurationRepository.findAll(pageable).map(insuranceConfigurationMapper::toDto);
    }

    @Override
    public List<InsuranceConfigurationDTO> getAllInsuranceConfiguration() {
        return insuranceConfigurationMapper.toDto(insuranceConfigurationRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InsuranceConfigurationDTO> findOne(Long id) {
        log.debug("Request to get InsuranceConfiguration : {}", id);
        return insuranceConfigurationRepository.findById(id).map(insuranceConfigurationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InsuranceConfiguration : {}", id);
        insuranceConfigurationRepository.deleteById(id);
    }
}
