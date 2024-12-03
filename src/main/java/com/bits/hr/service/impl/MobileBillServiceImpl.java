package com.bits.hr.service.impl;

import com.bits.hr.domain.MobileBill;
import com.bits.hr.repository.MobileBillRepository;
import com.bits.hr.service.MobileBillService;
import com.bits.hr.service.dto.MobileBillDTO;
import com.bits.hr.service.mapper.MobileBillMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MobileBill}.
 */
@Service
@Transactional
public class MobileBillServiceImpl implements MobileBillService {

    private final Logger log = LoggerFactory.getLogger(MobileBillServiceImpl.class);

    private final MobileBillRepository mobileBillRepository;

    private final MobileBillMapper mobileBillMapper;

    public MobileBillServiceImpl(MobileBillRepository mobileBillRepository, MobileBillMapper mobileBillMapper) {
        this.mobileBillRepository = mobileBillRepository;
        this.mobileBillMapper = mobileBillMapper;
    }

    @Override
    public MobileBillDTO save(MobileBillDTO mobileBillDTO) {
        log.debug("Request to save MobileBill : {}", mobileBillDTO);
        MobileBill mobileBill = mobileBillMapper.toEntity(mobileBillDTO);
        mobileBill = mobileBillRepository.save(mobileBill);
        return mobileBillMapper.toDto(mobileBill);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MobileBillDTO> findAll(Pageable pageable, String searchText, int month, int year) {
        log.debug("Request to get all MobileBills");
        return mobileBillRepository.findAll(pageable, searchText, month, year).map(mobileBillMapper::toDto);
        //PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted()))
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MobileBillDTO> findOne(Long id) {
        log.debug("Request to get MobileBill : {}", id);
        return mobileBillRepository.findById(id).map(mobileBillMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MobileBill : {}", id);
        mobileBillRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MobileBillDTO> findAllByYearAndMonth(int year, int month, Pageable pageable, String searchText) {
        log.debug("Request to get all MobileBills by year and month");
        return mobileBillRepository.findByYearAndMonth(year, month, pageable, searchText).map(mobileBillMapper::toDto);
    }
}
