package com.bits.hr.service.impl;

import com.bits.hr.domain.PfArrear;
import com.bits.hr.repository.PfArrearRepository;
import com.bits.hr.service.PfArrearService;
import com.bits.hr.service.dto.PfArrearDTO;
import com.bits.hr.service.mapper.PfArrearMapper;
import com.bits.hr.service.search.QuickFilterDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PfArrear}.
 */
@Service
@Transactional
public class PfArrearServiceImpl implements PfArrearService {

    private final Logger log = LoggerFactory.getLogger(PfArrearServiceImpl.class);

    private final PfArrearRepository pfArrearRepository;

    private final PfArrearMapper pfArrearMapper;

    public PfArrearServiceImpl(PfArrearRepository pfArrearRepository, PfArrearMapper pfArrearMapper) {
        this.pfArrearRepository = pfArrearRepository;
        this.pfArrearMapper = pfArrearMapper;
    }

    @Override
    public PfArrearDTO save(PfArrearDTO pfArrearDTO) {
        log.debug("Request to save PfArrear : {}", pfArrearDTO);
        PfArrear pfArrear = pfArrearMapper.toEntity(pfArrearDTO);
        pfArrear = pfArrearRepository.save(pfArrear);
        return pfArrearMapper.toDto(pfArrear);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PfArrearDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PfArrears");
        return pfArrearRepository.findAll(pageable).map(pfArrearMapper::toDto);
    }

    @Override
    public Page<PfArrearDTO> findAll(QuickFilterDTO quickFilterDTO, Pageable pageable) {
        if (quickFilterDTO != null) {
            if (quickFilterDTO.getSearchText() == null) {
                //%% for wildcard
                quickFilterDTO.setSearchText("%%");
            } else {
                quickFilterDTO.setSearchText("%" + quickFilterDTO.getSearchText().toLowerCase() + "%");
            }
            return pfArrearRepository.findAll(pageable, quickFilterDTO.getSearchText()).map(pfArrearMapper::toDto);
        }
        return pfArrearRepository.findAll(pageable).map(pfArrearMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PfArrearDTO> findOne(Long id) {
        log.debug("Request to get PfArrear : {}", id);
        return pfArrearRepository.findById(id).map(pfArrearMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PfArrear : {}", id);
        pfArrearRepository.deleteById(id);
    }
}
