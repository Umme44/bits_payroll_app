package com.bits.hr.service.impl;

import com.bits.hr.domain.FestivalBonusConfig;
import com.bits.hr.repository.FestivalBonusConfigRepository;
import com.bits.hr.service.FestivalBonusConfigService;
import com.bits.hr.service.dto.FestivalBonusConfigDTO;
import com.bits.hr.service.mapper.FestivalBonusConfigMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FestivalBonusConfig}.
 */
@Service
@Transactional
public class FestivalBonusConfigServiceImpl implements FestivalBonusConfigService {

    private final Logger log = LoggerFactory.getLogger(FestivalBonusConfigServiceImpl.class);

    private final FestivalBonusConfigRepository festivalBonusConfigRepository;

    private final FestivalBonusConfigMapper festivalBonusConfigMapper;

    public FestivalBonusConfigServiceImpl(
        FestivalBonusConfigRepository festivalBonusConfigRepository,
        FestivalBonusConfigMapper festivalBonusConfigMapper
    ) {
        this.festivalBonusConfigRepository = festivalBonusConfigRepository;
        this.festivalBonusConfigMapper = festivalBonusConfigMapper;
    }

    @Override
    public FestivalBonusConfigDTO save(FestivalBonusConfigDTO festivalBonusConfigDTO) {
        log.debug("Request to save FestivalBonusConfig : {}", festivalBonusConfigDTO);
        FestivalBonusConfig festivalBonusConfig = festivalBonusConfigMapper.toEntity(festivalBonusConfigDTO);
        festivalBonusConfig = festivalBonusConfigRepository.save(festivalBonusConfig);
        return festivalBonusConfigMapper.toDto(festivalBonusConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FestivalBonusConfigDTO> findAll() {
        log.debug("Request to get all FestivalBonusConfigs");
        return festivalBonusConfigRepository
            .findAll()
            .stream()
            .map(festivalBonusConfigMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FestivalBonusConfigDTO> findOne(Long id) {
        log.debug("Request to get FestivalBonusConfig : {}", id);
        return festivalBonusConfigRepository.findById(id).map(festivalBonusConfigMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FestivalBonusConfig : {}", id);
        festivalBonusConfigRepository.deleteById(id);
    }
}
