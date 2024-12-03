package com.bits.hr.service.impl;

import com.bits.hr.domain.AitPayment;
import com.bits.hr.repository.AitPaymentRepository;
import com.bits.hr.service.AitPaymentService;
import com.bits.hr.service.dto.AitPaymentDTO;
import com.bits.hr.service.mapper.AitPaymentMapper;
import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AitPayment}.
 */
@Service
@Transactional
public class AitPaymentServiceImpl implements AitPaymentService {

    private final Logger log = LoggerFactory.getLogger(AitPaymentServiceImpl.class);

    private final AitPaymentRepository aitPaymentRepository;

    private final AitPaymentMapper aitPaymentMapper;

    public AitPaymentServiceImpl(AitPaymentRepository aitPaymentRepository, AitPaymentMapper aitPaymentMapper) {
        this.aitPaymentRepository = aitPaymentRepository;
        this.aitPaymentMapper = aitPaymentMapper;
    }

    @Override
    public AitPaymentDTO save(AitPaymentDTO aitPaymentDTO) {
        log.debug("Request to save AitPayment : {}", aitPaymentDTO);
        AitPayment aitPayment = aitPaymentMapper.toEntity(aitPaymentDTO);
        aitPayment = aitPaymentRepository.save(aitPayment);
        return aitPaymentMapper.toDto(aitPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AitPaymentDTO> findAll(String searchText, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Request to get all AitPayments");
        Page<AitPayment> aitPayments = aitPaymentRepository.findAll(searchText, startDate, endDate, pageable);

        return aitPayments.map(aitPaymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AitPaymentDTO> findOne(Long id) {
        log.debug("Request to get AitPayment : {}", id);
        return aitPaymentRepository.findById(id).map(aitPaymentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AitPayment : {}", id);
        aitPaymentRepository.deleteById(id);
    }
}
