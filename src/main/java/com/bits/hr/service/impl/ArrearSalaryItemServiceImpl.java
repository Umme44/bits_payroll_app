package com.bits.hr.service.impl;

import com.bits.hr.domain.ArrearSalaryItem;
import com.bits.hr.domain.enumeration.ArrearPaymentType;
import com.bits.hr.repository.ArrearPaymentRepository;
import com.bits.hr.repository.ArrearSalaryItemRepository;
import com.bits.hr.service.ArrearSalaryItemService;
import com.bits.hr.service.dto.ArrearPaymentDTO;
import com.bits.hr.service.dto.ArrearSalaryItemDTO;
import com.bits.hr.service.dto.ArrearSalaryItemDisburseDTO;
import com.bits.hr.service.mapper.ArrearPaymentMapper;
import com.bits.hr.service.mapper.ArrearSalaryItemMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ArrearSalaryItem}.
 */
@Service
@Transactional
public class ArrearSalaryItemServiceImpl implements ArrearSalaryItemService {

    private final Logger log = LoggerFactory.getLogger(ArrearSalaryItemServiceImpl.class);

    private final ArrearSalaryItemRepository arrearSalaryItemRepository;

    private final ArrearPaymentRepository arrearPaymentRepository;

    private final ArrearSalaryItemMapper arrearSalaryItemMapper;

    private final ArrearPaymentMapper arrearPaymentMapper;

    public ArrearSalaryItemServiceImpl(
        ArrearSalaryItemRepository arrearSalaryItemRepository,
        ArrearPaymentRepository arrearPaymentRepository,
        ArrearSalaryItemMapper arrearSalaryItemMapper,
        ArrearPaymentMapper arrearPaymentMapper
    ) {
        this.arrearSalaryItemRepository = arrearSalaryItemRepository;
        this.arrearPaymentRepository = arrearPaymentRepository;
        this.arrearSalaryItemMapper = arrearSalaryItemMapper;
        this.arrearPaymentMapper = arrearPaymentMapper;
    }

    @Override
    public ArrearSalaryItemDTO save(ArrearSalaryItemDTO arrearSalaryItemDTO) {
        log.debug("Request to save ArrearSalaryItem : {}", arrearSalaryItemDTO);
        ArrearSalaryItem arrearSalaryItem = arrearSalaryItemMapper.toEntity(arrearSalaryItemDTO);
        arrearSalaryItem = arrearSalaryItemRepository.save(arrearSalaryItem);
        return arrearSalaryItemMapper.toDto(arrearSalaryItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArrearSalaryItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ArrearSalaryItems");
        return arrearSalaryItemRepository.findAll(pageable).map(arrearSalaryItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArrearSalaryItemDTO> findOne(Long id) {
        log.debug("Request to get ArrearSalaryItem : {}", id);
        return arrearSalaryItemRepository.findById(id).map(arrearSalaryItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ArrearSalaryItem : {}", id);
        arrearSalaryItemRepository.deleteById(id);
    }

    @Override
    public Page<ArrearSalaryItemDTO> findByArrearsSalaryMaster(long arrearsSalaryMasterId, Pageable pageable) {
        return arrearSalaryItemRepository.findByArrearsSalaryMaster(arrearsSalaryMasterId, pageable).map(arrearSalaryItemMapper::toDto);
    }

    @Override
    public Boolean disburseSelectedArrearSalaryItem(ArrearSalaryItemDisburseDTO arrearSalaryItemDisburseDTO) {
        List<ArrearPaymentDTO> arrearPaymentDTOList = new ArrayList<>();

        try {
            arrearSalaryItemDisburseDTO
                .getApprovalDTO()
                .getListOfIds()
                .forEach(x -> {
                    ArrearPaymentDTO arrearPaymentDTO = mapToArrearPayment(arrearSalaryItemDisburseDTO);
                    arrearPaymentDTO.setArrearSalaryItemId(x);
                    arrearPaymentDTOList.add(arrearPaymentDTO);
                });
            arrearPaymentRepository.saveAll(arrearPaymentMapper.toEntity(arrearPaymentDTOList));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private ArrearPaymentDTO mapToArrearPayment(ArrearSalaryItemDisburseDTO arrearSalaryItemDisburseDTO) {
        ArrearPaymentDTO arrearPaymentDTO = new ArrearPaymentDTO();
        arrearPaymentDTO.setPaymentType(arrearSalaryItemDisburseDTO.getPaymentType());
        if (arrearSalaryItemDisburseDTO.getPaymentType().equals(ArrearPaymentType.INDIVIDUAL)) {
            arrearPaymentDTO.setDisbursementDate(arrearSalaryItemDisburseDTO.getDisbursementDate());
        } else {
            arrearPaymentDTO.setSalaryMonth(arrearSalaryItemDisburseDTO.getSalaryMonth());
            if (arrearSalaryItemDisburseDTO.getSalaryYear() != null) {
                arrearPaymentDTO.setSalaryYear(arrearSalaryItemDisburseDTO.getSalaryYear());
            }
        }
        arrearPaymentDTO.setDisbursementAmount(arrearSalaryItemDisburseDTO.getDisbursementAmount());
        arrearPaymentDTO.setArrearPF(arrearSalaryItemDisburseDTO.getArrearPF());
        arrearPaymentDTO.setIsDeleted(false);

        if (arrearSalaryItemDisburseDTO.getDeductTaxUponPayment() != null && arrearSalaryItemDisburseDTO.getDeductTaxUponPayment()) {
            arrearPaymentDTO.setDeductTaxUponPayment(true);
            arrearPaymentDTO.setTaxDeduction(arrearSalaryItemDisburseDTO.getTaxDeduction());
        } else {
            arrearPaymentDTO.setTaxDeduction(0d);
            arrearPaymentDTO.setDeductTaxUponPayment(false);
        }
        return arrearPaymentDTO;
    }
}
