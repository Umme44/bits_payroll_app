package com.bits.hr.service.impl;

import com.bits.hr.domain.ArrearPayment;
import com.bits.hr.repository.ArrearPaymentRepository;
import com.bits.hr.service.ArrearPaymentService;
import com.bits.hr.service.dto.ArrearPaymentDTO;
import com.bits.hr.service.mapper.ArrearPaymentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ArrearPayment}.
 */
@Service
@Transactional
public class ArrearPaymentServiceImpl implements ArrearPaymentService {

    private final Logger log = LoggerFactory.getLogger(ArrearPaymentServiceImpl.class);

    private final ArrearPaymentRepository arrearPaymentRepository;

    private final ArrearPaymentMapper arrearPaymentMapper;

    public ArrearPaymentServiceImpl(ArrearPaymentRepository arrearPaymentRepository, ArrearPaymentMapper arrearPaymentMapper) {
        this.arrearPaymentRepository = arrearPaymentRepository;
        this.arrearPaymentMapper = arrearPaymentMapper;
    }

    @Override
    public ArrearPaymentDTO save(ArrearPaymentDTO arrearPaymentDTO) {
        log.debug("Request to save ArrearPayment : {}", arrearPaymentDTO);
        ArrearPayment arrearPayment = arrearPaymentMapper.toEntity(arrearPaymentDTO);
        arrearPayment = arrearPaymentRepository.save(arrearPayment);
        return arrearPaymentMapper.toDto(arrearPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArrearPaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ArrearPayments");
        return arrearPaymentRepository.findAll(pageable).map(arrearPaymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArrearPaymentDTO> findAllByArrearItem(Pageable pageable, long arrearSalaryItemId) {
        log.debug("Request to get all ArrearPayments by ArrearItemId");
        return arrearPaymentRepository.findAllByArrearItem(pageable, arrearSalaryItemId).map(arrearPaymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArrearPaymentDTO> findOne(Long id) {
        log.debug("Request to get ArrearPayment : {}", id);
        return arrearPaymentRepository.findById(id).map(arrearPaymentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ArrearPayment : {}", id);
        arrearPaymentRepository.deleteById(id);
    }
}
