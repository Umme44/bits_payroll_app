package com.bits.hr.service.impl;

import com.bits.hr.domain.FinalSettlement;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.FinalSettlementRepository;
import com.bits.hr.service.FinalSettlementService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FinalSettlementDTO;
import com.bits.hr.service.finalSettlement.FinalSettlementGenService;
import com.bits.hr.service.mapper.FinalSettlementMapper;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FinalSettlement}.
 */
@Service
@Transactional
public class FinalSettlementServiceImpl implements FinalSettlementService {

    private final Logger log = LoggerFactory.getLogger(FinalSettlementServiceImpl.class);

    private final FinalSettlementRepository finalSettlementRepository;

    private final FinalSettlementMapper finalSettlementMapper;

    private final FinalSettlementGenService finalSettlementGenService;

    private final CurrentEmployeeService currentEmployeeService;

    private final EmployeeRepository employeeRepository;

    public FinalSettlementServiceImpl(
        FinalSettlementRepository finalSettlementRepository,
        FinalSettlementMapper finalSettlementMapper,
        FinalSettlementGenService finalSettlementGenService,
        CurrentEmployeeService currentEmployeeService,
        EmployeeRepository employeeRepository
    ) {
        this.finalSettlementRepository = finalSettlementRepository;
        this.finalSettlementMapper = finalSettlementMapper;
        this.finalSettlementGenService = finalSettlementGenService;
        this.currentEmployeeService = currentEmployeeService;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public FinalSettlementDTO save(FinalSettlementDTO finalSettlementDTO) {
        log.debug("Request to save FinalSettlement : {}", finalSettlementDTO);
        FinalSettlement finalSettlement = finalSettlementMapper.toEntity(finalSettlementDTO);
        finalSettlement = finalSettlementRepository.save(finalSettlement);
        return finalSettlementMapper.toDto(finalSettlement);
    }

    @Override
    public FinalSettlementDTO update(FinalSettlementDTO finalSettlementDTO) {
        log.debug("Request to update FinalSettlement : {}", finalSettlementDTO);
        if (finalSettlementDTO.getDeductionAbsentDaysAdjustmentDays() == null) {
            finalSettlementDTO.setDeductionAbsentDaysAdjustmentDays(0);
        }
        double perDaySalary = 0;
        if (finalSettlementDTO.getEmployeeId() != null && employeeRepository.findById(finalSettlementDTO.getEmployeeId()).isPresent()) {
            if (employeeRepository.findById(finalSettlementDTO.getEmployeeId()).get().getMainGrossSalary() != null) {
                perDaySalary =
                    MathRoundUtil.round(employeeRepository.findById(finalSettlementDTO.getEmployeeId()).get().getMainGrossSalary() / 30.0d);
            }
        }

        double deductionAbsentDays = (double) finalSettlementDTO.getDeductionAbsentDaysAdjustmentDays();
        finalSettlementDTO.setDeductionAbsentDaysAdjustment(perDaySalary * deductionAbsentDays);
        finalSettlementDTO = finalSettlementGenService.balanceSum(finalSettlementDTO);
        Optional<Long> currentUsrId = currentEmployeeService.getCurrentUserId();
        if (currentUsrId.isPresent()) {
            finalSettlementDTO.setUpdatedById(currentUsrId.get());
        }
        finalSettlementDTO.setUpdatedAt(LocalDate.now());

        FinalSettlement finalSettlement = finalSettlementMapper.toEntity(finalSettlementDTO);
        finalSettlement = finalSettlementRepository.save(finalSettlement);
        return finalSettlementMapper.toDto(finalSettlement);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FinalSettlementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FinalSettlements");
        return finalSettlementRepository.findAll(pageable).map(finalSettlementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FinalSettlementDTO> findOne(Long id) {
        log.debug("Request to get FinalSettlement : {}", id);
        return finalSettlementRepository.findById(id).map(finalSettlementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FinalSettlement : {}", id);
        finalSettlementRepository.deleteById(id);
    }
}
