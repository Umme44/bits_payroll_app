package com.bits.hr.service.impl;

import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.HoldSalaryDisbursement;
import com.bits.hr.domain.SalaryGeneratorMaster;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.repository.HoldSalaryDisbursementRepository;
import com.bits.hr.repository.SalaryGeneratorMasterRepository;
import com.bits.hr.service.HoldSalaryDisbursementService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.HoldSalaryDisbursementDTO;
import com.bits.hr.service.mapper.HoldSalaryDisbursementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HoldSalaryDisbursement}.
 */
@Service
@Transactional
public class HoldSalaryDisbursementServiceImpl implements HoldSalaryDisbursementService {

    private final Logger log = LoggerFactory.getLogger(HoldSalaryDisbursementServiceImpl.class);

    private final HoldSalaryDisbursementRepository holdSalaryDisbursementRepository;

    private final EmployeeSalaryRepository employeeSalaryRepository;

    private final HoldSalaryDisbursementMapper holdSalaryDisbursementMapper;

    private final CurrentEmployeeService currentEmployeeService;

    public HoldSalaryDisbursementServiceImpl(
        HoldSalaryDisbursementRepository holdSalaryDisbursementRepository,
        EmployeeSalaryRepository employeeSalaryRepository,
        HoldSalaryDisbursementMapper holdSalaryDisbursementMapper,
        CurrentEmployeeService currentEmployeeService
    ) {
        this.holdSalaryDisbursementRepository = holdSalaryDisbursementRepository;
        this.employeeSalaryRepository = employeeSalaryRepository;
        this.holdSalaryDisbursementMapper = holdSalaryDisbursementMapper;
        this.currentEmployeeService = currentEmployeeService;
    }

    @Override
    @Transactional
    public HoldSalaryDisbursementDTO save(HoldSalaryDisbursementDTO holdSalaryDisbursementDTO) {
        log.debug("Request to save HoldSalaryDisbursement : {}", holdSalaryDisbursementDTO);
        if (currentEmployeeService.getCurrentUser().isPresent()) {
            holdSalaryDisbursementDTO.setUserId(currentEmployeeService.getCurrentUser().get().getId());
        } else {
            throw new BadRequestAlertException("You are not logged", "HoldSalaryDisbursement", "noEmployee");
        }

        Optional<EmployeeSalary> employeeSalary = employeeSalaryRepository.findById(holdSalaryDisbursementDTO.getEmployeeSalaryId());
        if (employeeSalary.isPresent()) {
            employeeSalary.get().setIsHold(false);
            employeeSalaryRepository.save(employeeSalary.get());
        } else {
            throw new BadRequestAlertException("Not Found", "SalaryGeneratorMaster", "notFound");
        }

        HoldSalaryDisbursement holdSalaryDisbursement = holdSalaryDisbursementMapper.toEntity(holdSalaryDisbursementDTO);
        holdSalaryDisbursement = holdSalaryDisbursementRepository.save(holdSalaryDisbursement);
        return holdSalaryDisbursementMapper.toDto(holdSalaryDisbursement);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HoldSalaryDisbursementDTO> findAll(String searchText, Pageable pageable) {
        if (searchText == null) {
            searchText = "";
        }
        log.debug("Request to get all HoldSalaryDisbursements");
        return holdSalaryDisbursementRepository
            .findAll("%" + searchText.toLowerCase() + "%", pageable)
            .map(holdSalaryDisbursementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HoldSalaryDisbursementDTO> findOne(Long id) {
        log.debug("Request to get HoldSalaryDisbursement : {}", id);
        return holdSalaryDisbursementRepository.findById(id).map(holdSalaryDisbursementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HoldSalaryDisbursement : {}", id);
        holdSalaryDisbursementRepository.deleteById(id);
    }
}
