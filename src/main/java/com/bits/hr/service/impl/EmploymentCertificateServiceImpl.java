package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmploymentCertificate;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmploymentCertificateRepository;
import com.bits.hr.service.EmploymentCertificateService;
import com.bits.hr.service.dto.CertificateApprovalDto;
import com.bits.hr.service.dto.EmploymentCertificateDTO;
import com.bits.hr.service.mapper.EmploymentCertificateMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmploymentCertificate}.
 */
@Log4j2
@Service
@Transactional
public class EmploymentCertificateServiceImpl implements EmploymentCertificateService {

    private final EmploymentCertificateRepository employmentCertificateRepository;

    private final EmploymentCertificateMapper employmentCertificateMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    public EmploymentCertificateServiceImpl(
        EmploymentCertificateRepository employmentCertificateRepository,
        EmploymentCertificateMapper employmentCertificateMapper
    ) {
        this.employmentCertificateRepository = employmentCertificateRepository;
        this.employmentCertificateMapper = employmentCertificateMapper;
    }

    @Override
    public EmploymentCertificate createEmploymentCertificate(Employee employee, User user) {
        try {
            EmploymentCertificate certificate = new EmploymentCertificate();

            certificate.setEmployee(employee);
            certificate.certificateStatus(CertificateStatus.SENT_FOR_GENERATION);
            certificate.createdAt(Instant.now());
            certificate.createdBy(user);

            employmentCertificateRepository.save(certificate);

            return certificate;
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException();
        }
    }

    @Override
    public EmploymentCertificateDTO updateEmploymentCertificate(EmploymentCertificateDTO employmentCertificateDTO, User user) {
        try {
            employmentCertificateDTO.setUpdatedAt(Instant.now());
            employmentCertificateDTO.setUpdatedById(user.getId());

            EmploymentCertificate certificate = employmentCertificateRepository.save(
                employmentCertificateMapper.toEntity(employmentCertificateDTO)
            );

            return employmentCertificateMapper.toDto(certificate);
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmploymentCertificateDTO> findAll(String searchText, CertificateStatus status, int selectedYear, Pageable pageable) {
        log.debug("Request to get all EmploymentCertificates");

        Instant startDate = null;
        Instant endDate = null;

        if (selectedYear != 0) {
            startDate = LocalDate.of(selectedYear, 1, 1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            endDate = LocalDate.of(selectedYear, 12, 31).plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        }

        return employmentCertificateRepository
            .findAllUsingFilter(searchText, status, startDate, endDate, pageable)
            .map(employmentCertificateMapper::toDto);
    }

    @Override
    public Page<EmploymentCertificateDTO> findAllByEmployeeId(Employee employee, CertificateStatus status, Pageable pageable) {
        log.debug("Request to get all EmploymentCertificates");
        return employmentCertificateRepository
            .findAllByEmployeeIdUsingFilter(employee.getId(), status, pageable)
            .map(employmentCertificateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmploymentCertificateDTO> findOne(Long id) {
        log.debug("Request to get EmploymentCertificate : {}", id);
        return employmentCertificateRepository.findById(id).map(employmentCertificateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmploymentCertificate : {}", id);
        employmentCertificateRepository.deleteById(id);
    }

    @Override
    public EmploymentCertificate approveEmployeeNoc(long employmentCertificateId, CertificateApprovalDto approvalDto, User user) {
        try {
            Optional<EmploymentCertificate> employmentCertificate = employmentCertificateRepository.findById(employmentCertificateId);
            if (!employmentCertificate.isPresent()) {
                throw new BadRequestAlertException("Employment Certificate not found", "EmploymentCertificate", "idnull");
            }

            String referenceNumber = getReferenceNumber();

            if (employmentCertificate.get().getCertificateStatus().equals(CertificateStatus.SENT_FOR_GENERATION)) {
                Optional<Employee> signatoryPerson = employeeRepository.findById(approvalDto.getSignatoryPersonId());

                employmentCertificate.get().setCertificateStatus(CertificateStatus.GENERATED);
                employmentCertificate.get().setSignatoryPerson(signatoryPerson.get());
                employmentCertificate.get().setIssueDate(approvalDto.getIssueDate());
                employmentCertificate.get().setReferenceNumber(referenceNumber);
                employmentCertificate.get().setGeneratedAt(Instant.now());
                employmentCertificate.get().setGeneratedBy(user);
            } else {
                throw new BadRequestAlertException("Only Pending Requests Are Allowed To Get Approval.", "EmployeeNOC", "accessForbidden");
            }
            return employmentCertificate.get();
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException();
        }
    }

    private String getReferenceNumber() {
        String referenceNumber = "";
        for (int i = 1; i <= 9999; i++) {
            referenceNumber = generateReferenceNumber(i);
            if (!employmentCertificateRepository.isEmploymentCertificateReferenceNumberUnique(referenceNumber)) {
                continue;
            } else {
                break;
            }
        }
        return referenceNumber;
    }

    private String generateReferenceNumber(int i) {
        String number;
        if (i < 10) {
            number = "000" + i;
        } else if (i < 100) {
            number = "00" + i;
        } else if (i < 1000) {
            number = "0" + i;
        } else {
            number = "" + i;
        }
        int year = LocalDate.now().getYear();
        String referenceNumber = "" + year + "/EC/" + number;

        return referenceNumber;
    }

    @Override
    public EmploymentCertificateDTO rejectEmployeeNoc(long employmentCertificateId, CertificateApprovalDto approvalDto, User user) {
        try {
            Optional<EmploymentCertificate> employmentCertificate = employmentCertificateRepository.findById(employmentCertificateId);
            if (!employmentCertificate.isPresent()) {
                throw new BadRequestAlertException("Employee NOC not found", "EmployeeNOC", "idnull");
            }
            if (employmentCertificate.get().getCertificateStatus().equals(CertificateStatus.SENT_FOR_GENERATION)) {
                employmentCertificate.get().setCertificateStatus(CertificateStatus.REJECTED);
                employmentCertificate.get().setReason(approvalDto.getReason());
                employmentCertificateRepository.save(employmentCertificate.get());
            } else {
                throw new BadRequestAlertException("Status Should Be Pending To Get Rejected", "EmployeeNOC", "accessForbidden");
            }

            return employmentCertificateMapper.toDto(employmentCertificate.get());
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<EmploymentCertificateDTO> getEmploymentCertificatePrintFormat(long employmentCertificateId) {
        return employmentCertificateRepository
            .findApprovedEmploymentCertificateById(employmentCertificateId)
            .map(employmentCertificateMapper::toDto);
    }

    @Override
    public boolean isReferenceNumberUnique(String referenceNumber) {
        return employmentCertificateRepository.isEmploymentCertificateReferenceNumberUnique(referenceNumber);
    }
}
