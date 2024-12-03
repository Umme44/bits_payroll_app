package com.bits.hr.service.impl;

import com.bits.hr.domain.SalaryDeduction;
import com.bits.hr.repository.SalaryDeductionRepository;
import com.bits.hr.service.SalaryDeductionService;
import com.bits.hr.service.dto.SalaryDeductionDTO;
import com.bits.hr.service.mapper.SalaryDeductionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SalaryDeduction}.
 */
@Service
@Transactional
public class SalaryDeductionServiceImpl implements SalaryDeductionService {

    private final Logger log = LoggerFactory.getLogger(SalaryDeductionServiceImpl.class);

    private final SalaryDeductionRepository salaryDeductionRepository;

    private final SalaryDeductionMapper salaryDeductionMapper;

    public SalaryDeductionServiceImpl(SalaryDeductionRepository salaryDeductionRepository, SalaryDeductionMapper salaryDeductionMapper) {
        this.salaryDeductionRepository = salaryDeductionRepository;
        this.salaryDeductionMapper = salaryDeductionMapper;
    }

    @Override
    public SalaryDeductionDTO save(SalaryDeductionDTO salaryDeductionDTO) {
        log.debug("Request to save SalaryDeduction : {}", salaryDeductionDTO);
        SalaryDeduction salaryDeduction = salaryDeductionMapper.toEntity(salaryDeductionDTO);
        salaryDeduction = salaryDeductionRepository.save(salaryDeduction);
        return salaryDeductionMapper.toDto(salaryDeduction);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalaryDeductionDTO> findAll(Pageable pageable, String searchText, int month, int year) {
        log.debug("Request to get all SalaryDeductions");
        return salaryDeductionRepository.findAll(pageable, searchText, month, year).map(salaryDeductionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalaryDeductionDTO> findAllByYearAndMonth(int year, int month, Pageable pageable, String searchText) {
        log.debug("Request to get all SalaryDeductions");
        return salaryDeductionRepository.findPageByYearAndMonth(year, month, pageable, searchText).map(salaryDeductionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SalaryDeductionDTO> findOne(Long id) {
        log.debug("Request to get SalaryDeduction : {}", id);
        return salaryDeductionRepository.findById(id).map(salaryDeductionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SalaryDeduction : {}", id);
        salaryDeductionRepository.deleteById(id);
    }
}
