package com.bits.hr.service.impl;

import com.bits.hr.domain.PfLoanRepayment;
import com.bits.hr.repository.PfLoanRepaymentRepository;
import com.bits.hr.service.PfLoanRepaymentService;
import com.bits.hr.service.PfLoanRepaymentService;
import com.bits.hr.service.dto.PfLoanRepaymentDTO;
import com.bits.hr.service.mapper.PfLoanRepaymentMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PfLoanRepayment}.
 */
@Service
@Transactional
public class PfLoanRepaymentServiceImpl implements PfLoanRepaymentService {

    private final Logger log = LoggerFactory.getLogger(PfLoanRepaymentServiceImpl.class);

    private final PfLoanRepaymentRepository pfLoanRepaymentRepository;

    private final PfLoanRepaymentMapper pfLoanRepaymentMapper;

    public PfLoanRepaymentServiceImpl(PfLoanRepaymentRepository pfLoanRepaymentRepository, PfLoanRepaymentMapper pfLoanRepaymentMapper) {
        this.pfLoanRepaymentRepository = pfLoanRepaymentRepository;
        this.pfLoanRepaymentMapper = pfLoanRepaymentMapper;
    }

    @Override
    public PfLoanRepaymentDTO save(PfLoanRepaymentDTO pfLoanRepaymentDTO) {
        log.debug("Request to save PfLoanRepayment : {}", pfLoanRepaymentDTO);
        PfLoanRepayment pfLoanRepayment = pfLoanRepaymentMapper.toEntity(pfLoanRepaymentDTO);
        pfLoanRepayment = pfLoanRepaymentRepository.save(pfLoanRepayment);
        return pfLoanRepaymentMapper.toDto(pfLoanRepayment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PfLoanRepaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PfLoanRepayments");
        return pfLoanRepaymentRepository.findAll(pageable).map(pfLoanRepaymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PfLoanRepaymentDTO> findOne(Long id) {
        log.debug("Request to get PfLoanRepayment : {}", id);
        return pfLoanRepaymentRepository.findById(id).map(pfLoanRepaymentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PfLoanRepayment : {}", id);
        pfLoanRepaymentRepository.deleteById(id);
    }

    @Override
    public List<PfLoanRepaymentDTO> findAllByYearAndMonth(int year, int month) {
        return pfLoanRepaymentMapper.toDto(pfLoanRepaymentRepository.findAllByDeductionYearAndDeductionMonth(year, month));
    }
}
