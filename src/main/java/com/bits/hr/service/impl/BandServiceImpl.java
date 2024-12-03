package com.bits.hr.service.impl;

import com.bits.hr.domain.Band;
import com.bits.hr.repository.BandRepository;
import com.bits.hr.service.BandService;
import com.bits.hr.service.dto.BandDTO;
import com.bits.hr.service.mapper.BandMapper;
import com.bits.hr.service.selecteable.SelectableDTO;
import com.bits.hr.service.selecteable.SelectableGenerationService;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Band}.
 */
@Service
@Transactional
public class BandServiceImpl implements BandService {

    private final Logger log = LoggerFactory.getLogger(BandServiceImpl.class);

    private final BandRepository bandRepository;

    private final BandMapper bandMapper;

    @Autowired
    private SelectableGenerationService dictionaryDsService;

    public BandServiceImpl(BandRepository bandRepository, BandMapper bandMapper) {
        this.bandRepository = bandRepository;
        this.bandMapper = bandMapper;
    }

    @Override
    public BandDTO save(BandDTO bandDTO) {
        log.debug("Request to save Band : {}", bandDTO);
        Band band = bandMapper.toEntity(bandDTO);
        band = bandRepository.save(band);
        return bandMapper.toDto(band);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BandDTO> findAll() {
        log.debug("Request to get all Bands");
        return bandRepository.findAllOrderByName().stream().map(bandMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BandDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Nationalities");
        return bandRepository.findAll(pageable)
            .map(bandMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BandDTO> findOne(Long id) {
        log.debug("Request to get Band : {}", id);
        return bandRepository.findById(id).map(bandMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Band : {}", id);
        bandRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SelectableDTO> findAllForCommon() {
        log.debug("Request to get all Bands");
        List<SelectableDTO> dictionaryDsDTOS;
        List<Band> bandList = bandRepository.findAllOrderByName();
        dictionaryDsDTOS = dictionaryDsService.getListOfDictionaryDsDTO(Arrays.asList(bandList.toArray()));
        return dictionaryDsDTOS;
    }
}
