package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.TaxAcknowledgementReceipt;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.AcknowledgementStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.TaxAcknowledgementReceiptRepository;
import com.bits.hr.service.TaxAcknowledgementReceiptService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.TaxAcknowledgementReceiptDTO;
import com.bits.hr.service.mapper.TaxAcknowledgementReceiptMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TaxAcknowledgementReceipt}.
 */
@Service
@Transactional
public class TaxAcknowledgementReceiptServiceImpl implements TaxAcknowledgementReceiptService {

    private final Logger log = LoggerFactory.getLogger(TaxAcknowledgementReceiptServiceImpl.class);

    private final TaxAcknowledgementReceiptRepository taxAcknowledgementReceiptRepository;

    private final TaxAcknowledgementReceiptMapper taxAcknowledgementReceiptMapper;
    private final CurrentEmployeeService currentEmployeeService;
    private final EmployeeRepository employeeRepository;

    public TaxAcknowledgementReceiptServiceImpl(
        TaxAcknowledgementReceiptRepository taxAcknowledgementReceiptRepository,
        TaxAcknowledgementReceiptMapper taxAcknowledgementReceiptMapper,
        CurrentEmployeeService currentEmployeeService,
        EmployeeRepository employeeRepository
    ) {
        this.taxAcknowledgementReceiptRepository = taxAcknowledgementReceiptRepository;
        this.taxAcknowledgementReceiptMapper = taxAcknowledgementReceiptMapper;
        this.currentEmployeeService = currentEmployeeService;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public TaxAcknowledgementReceiptDTO save(TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO) {
        log.debug("Request to save TaxAcknowledgementReceipt : {}", taxAcknowledgementReceiptDTO);
        TaxAcknowledgementReceipt taxAcknowledgementReceipt = taxAcknowledgementReceiptMapper.toEntity(taxAcknowledgementReceiptDTO);
        List<TaxAcknowledgementReceipt> receiptListOfEmployeeAndFiscalYear = taxAcknowledgementReceiptRepository.findByEmployeeIdAndFiscalYearId(
            taxAcknowledgementReceiptDTO.getEmployeeId(),
            taxAcknowledgementReceiptDTO.getFiscalYearId()
        );

        if (
            taxAcknowledgementReceiptDTO.getId() != null &&
            receiptListOfEmployeeAndFiscalYear.size() > 0 &&
            !receiptListOfEmployeeAndFiscalYear.get(0).getId().equals(taxAcknowledgementReceipt.getId())
        ) {
            throw new BadRequestAlertException(
                "Tax Acknowledgement Receipt already upload for selected fiscal year",
                "TaxAcknowledgementReceipt",
                "alreadyUploadedTaxAcknowledgement"
            );
        } else if (taxAcknowledgementReceiptDTO.getId() == null && receiptListOfEmployeeAndFiscalYear.size() > 0) {
            throw new BadRequestAlertException(
                "Tax Acknowledgement Receipt already upload for selected fiscal year",
                "TaxAcknowledgementReceipt",
                "alreadyUploadedTaxAcknowledgement"
            );
        }
        Employee currentEmployee = employeeRepository.findById(taxAcknowledgementReceiptDTO.getEmployeeId()).get();

        // check if Employee TIN is missing in Employee Profile
        if (
            currentEmployee.getTinNumber() == null ||
            currentEmployee.getTinNumber() == "" ||
            !Objects.equals(currentEmployee.getTinNumber(), taxAcknowledgementReceiptDTO.getTinNumber())
        ) {
            currentEmployee.setTinNumber(taxAcknowledgementReceiptDTO.getTinNumber());
            currentEmployee.setUpdatedAt(LocalDateTime.now());
            employeeRepository.save(currentEmployee);
        }

        // check if Employee Tax Zone is missing in Employee Profile
        if (
            currentEmployee.getTaxesZone() == null ||
            currentEmployee.getTaxesZone() == "" ||
            !Objects.equals(currentEmployee.getTaxesZone(), taxAcknowledgementReceiptDTO.getTaxesZone())
        ) {
            currentEmployee.setTaxesZone(taxAcknowledgementReceiptDTO.getTaxesZone());
            employeeRepository.save(currentEmployee);
        }

        // check if Employee Tax Circle is missing in Employee Profile
        if (
            currentEmployee.getTaxesCircle() == null ||
            currentEmployee.getTaxesCircle() == "" ||
            !Objects.equals(currentEmployee.getTaxesCircle(), taxAcknowledgementReceiptDTO.getTaxesCircle())
        ) {
            currentEmployee.setTaxesCircle(taxAcknowledgementReceiptDTO.getTaxesCircle());
            employeeRepository.save(currentEmployee);
        }
        taxAcknowledgementReceipt = taxAcknowledgementReceiptRepository.save(taxAcknowledgementReceipt);
        return taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaxAcknowledgementReceiptDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaxAcknowledgementReceipts");
        return taxAcknowledgementReceiptRepository.findAll(pageable).map(taxAcknowledgementReceiptMapper::toDto);
    }

    @Override
    public Page<TaxAcknowledgementReceiptDTO> findAllByEmployeeId(Pageable pageable, long employeeId) {
        Page<TaxAcknowledgementReceipt> page = taxAcknowledgementReceiptRepository.findAllByEmployeeId(pageable, employeeId);
        return page.map(taxAcknowledgementReceiptMapper::toDto);
    }

    @Override
    public Page<TaxAcknowledgementReceiptDTO> findAllReceivedByFiscalYearId(Long aitConfigId, Long employeeId, Pageable pageable) {
        Page<TaxAcknowledgementReceipt> page = taxAcknowledgementReceiptRepository.findPageByFiscalYearIdAndStatus(
            aitConfigId,
            employeeId,
            AcknowledgementStatus.RECEIVED,
            pageable
        );
        return page.map(taxAcknowledgementReceiptMapper::toDto);
    }

    @Override
    public Page<TaxAcknowledgementReceiptDTO> findAllSubmittedByFiscalYearId(Long aitConfigId, Long employeeId, Pageable pageable) {
        Page<TaxAcknowledgementReceipt> page = taxAcknowledgementReceiptRepository.findPageByFiscalYearIdAndStatus(
            aitConfigId,
            employeeId,
            AcknowledgementStatus.SUBMITTED,
            pageable
        );
        return page.map(taxAcknowledgementReceiptMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaxAcknowledgementReceiptDTO> findOne(Long id) {
        log.debug("Request to get TaxAcknowledgementReceipt : {}", id);
        return taxAcknowledgementReceiptRepository.findById(id).map(taxAcknowledgementReceiptMapper::toDto);
    }

    @Override
    public Optional<TaxAcknowledgementReceiptDTO> findOneByEmployeeId(long id, long employeeId) {
        log.debug("Request to get TaxAcknowledgementReceipt : {}", id);
        return taxAcknowledgementReceiptRepository.findByIdAndEmployeeId(id, employeeId).map(taxAcknowledgementReceiptMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaxAcknowledgementReceipt : {}", id);
        taxAcknowledgementReceiptRepository.deleteById(id);
    }

    @Override
    public void changeStatusIntoReceived(User receivedBy, Instant receivedAt, List<Long> listOfIds) {
        List<TaxAcknowledgementReceipt> taxAcknowledgementReceipts = taxAcknowledgementReceiptRepository.findAllById(listOfIds);
        taxAcknowledgementReceipts.forEach(taxAcknowledgementReceipt -> {
            taxAcknowledgementReceipt.setAcknowledgementStatus(AcknowledgementStatus.RECEIVED);
            taxAcknowledgementReceipt.setReceivedBy(receivedBy);
            taxAcknowledgementReceipt.setReceivedAt(receivedAt);
        });
        taxAcknowledgementReceiptRepository.saveAll(taxAcknowledgementReceipts);
    }
}
