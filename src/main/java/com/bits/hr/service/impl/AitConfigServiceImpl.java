package com.bits.hr.service.impl;

import com.bits.hr.domain.AitConfig;
import com.bits.hr.repository.AitConfigRepository;
import com.bits.hr.service.AitConfigService;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.mapper.AitConfigMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AitConfig}.
 */
@Service
@Transactional
public class AitConfigServiceImpl implements AitConfigService {

    private final Logger log = LoggerFactory.getLogger(AitConfigServiceImpl.class);

    private final AitConfigRepository aitConfigRepository;

    private final AitConfigMapper aitConfigMapper;

    public AitConfigServiceImpl(AitConfigRepository aitConfigRepository, AitConfigMapper aitConfigMapper) {
        this.aitConfigRepository = aitConfigRepository;
        this.aitConfigMapper = aitConfigMapper;
    }

    @Override
    public AitConfigDTO save(AitConfigDTO aitConfigDTO) {
        log.debug("Request to save AitConfig : {}", aitConfigDTO);
        AitConfig aitConfig = aitConfigMapper.toEntity(aitConfigDTO);
        aitConfig = aitConfigRepository.save(aitConfig);
        return aitConfigMapper.toDto(aitConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AitConfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AitConfigs");
        Page<AitConfigDTO> aitConfigs = aitConfigRepository.findAll(pageable).map(aitConfigMapper::toDto);

        for (AitConfigDTO aitConfigDTO : aitConfigs) {
            aitConfigDTO.setStartYear(aitConfigDTO.getStartDate().getYear());
            aitConfigDTO.setEndYear(aitConfigDTO.getEndDate().getYear());
        }
        return aitConfigs;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AitConfigDTO> findOne(Long id) {
        log.debug("Request to get AitConfig : {}", id);
        return aitConfigRepository.findById(id).map(aitConfigMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AitConfig : {}", id);
        aitConfigRepository.deleteById(id);
    }

    @Override
    public List<AitConfigDTO> findByYearAndMonth(int year, int month) {
        LocalDate startDayOfMonth = LocalDate.of(year, month, 1);
        return aitConfigMapper.toDto(aitConfigRepository.findAllBetweenOneDate(startDayOfMonth));
    }
}
