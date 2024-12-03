package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeNOC;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.domain.enumeration.PurposeOfNOC;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeNOCRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.service.EmployeeNOCService;
import com.bits.hr.service.LeaveManagement.LeaveDaysCount.LeaveDaysCalculationService;
import com.bits.hr.service.dto.CertificateApprovalDto;
import com.bits.hr.service.dto.EmployeeNOCDTO;
import com.bits.hr.service.mapper.EmployeeNOCMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmployeeNOC}.
 */
@Service
@Log4j2
@Transactional
public class EmployeeNOCServiceImpl implements EmployeeNOCService {

    private final EmployeeNOCRepository employeeNOCRepository;

    private final EmployeeNOCMapper employeeNOCMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private LeaveDaysCalculationService leaveDaysCalculationService;

    public EmployeeNOCServiceImpl(EmployeeNOCRepository employeeNOCRepository, EmployeeNOCMapper employeeNOCMapper) {
        this.employeeNOCRepository = employeeNOCRepository;
        this.employeeNOCMapper = employeeNOCMapper;
    }

    @Override
    public EmployeeNOC createEmployeeNOC(EmployeeNOCDTO employeeNOCDTO, Employee employee, User user) {
        boolean hasApprovedLeaveApplication = hasAnyApprovedLeaveApplicationBetweenDateRange(
            employeeNOCDTO.getLeaveStartDate(),
            employeeNOCDTO.getLeaveEndDate(),
            employee
        );

        if (
            !hasApprovedLeaveApplication &&
            employeeNOCDTO.getPurposeOfNOC() != PurposeOfNOC.ACADEMIC &&
            employeeNOCDTO.isIsRequiredForVisa() == false
        ) {
            throw new BadRequestAlertException("Approved leave is required between the date range.", "EmployeeNOC", "accessForbidden");
        }

        String[] countries = employeeNOCDTO.getCountryToVisit().split("]&");

        EmployeeNOC employeeNOC = employeeNOCMapper.toEntity(employeeNOCDTO);
        employeeNOC.setEmployee(employee);

        for (int i = 0; i < countries.length; i++) {
            EmployeeNOC employeeNOCCopy = SerializationUtils.clone(employeeNOC);

            employeeNOCCopy.setCountryToVisit(countries[i]);
            employeeNOCCopy.setCertificateStatus(CertificateStatus.SENT_FOR_GENERATION);
            employeeNOCCopy.setEmployee(employee);
            employeeNOCCopy.setCreatedAt(Instant.now());
            employeeNOCCopy.setCreatedBy(user);

            employeeNOCRepository.save(employeeNOCCopy);
        }
        return employeeNOC;
    }

    @Override
    public EmployeeNOCDTO updateEmployeeNOC(EmployeeNOCDTO employeeNOCDTO, Employee employee, User user) {
        boolean hasApprovedLeaveApplication = hasAnyApprovedLeaveApplicationBetweenDateRange(
            employeeNOCDTO.getLeaveStartDate(),
            employeeNOCDTO.getLeaveEndDate(),
            employee
        );

        if (
            !hasApprovedLeaveApplication &&
            employeeNOCDTO.getPurposeOfNOC() != PurposeOfNOC.ACADEMIC &&
            employeeNOCDTO.isIsRequiredForVisa() == false
        ) {
            throw new BadRequestAlertException("Approved leave is required between the date range.", "EmployeeNOC", "accessForbidden");
        }

        String[] countries = employeeNOCDTO.getCountryToVisit().split("]&");

        if (countries.length > 1) {
            throw new BadRequestAlertException(
                "You are not allowed add multiple countries while updating employee NOC. Please apply for a new one.",
                "EmployeeNOC",
                "accessForbidden"
            );
        }

        EmployeeNOC employeeNOC = employeeNOCMapper.toEntity(employeeNOCDTO);

        for (int i = 0; i < countries.length; i++) {
            employeeNOC.setUpdatedAt(Instant.now());
            employeeNOC.setUpdatedBy(user);
            employeeNOCRepository.save(employeeNOC);
        }
        return employeeNOCDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeNOCDTO> findAll(String searchText, CertificateStatus status, Integer selectedYear, Pageable pageable) {
        log.debug("Request to get all EmployeeNOCS");

        Instant startDate = null;
        Instant endDate = null;

        if (selectedYear != 0) {
            startDate = LocalDate.of(selectedYear, 1, 1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            endDate = LocalDate.of(selectedYear, 12, 31).plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        }

        return employeeNOCRepository.findAllUsingFilter(searchText, status, startDate, endDate, pageable).map(employeeNOCMapper::toDto);
    }

    @Override
    public Page<EmployeeNOCDTO> findAllByEmployeeId(Employee employee, CertificateStatus status, Pageable pageable) {
        log.debug("Request to get all EmployeeNOCS");
        return employeeNOCRepository.findAllByEmployeeIdUsingFilter(employee.getId(), status, pageable).map(employeeNOCMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeNOCDTO> findOne(Long id) {
        log.debug("Request to get EmployeeNOC : {}", id);
        return employeeNOCRepository.findById(id).map(employeeNOCMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmployeeNOC : {}", id);
        employeeNOCRepository.deleteById(id);
    }

    @Override
    public EmployeeNOC approveEmployeeNoc(long employeeNocId, CertificateApprovalDto approvalDto, User user) {
        Optional<EmployeeNOC> employeeNOCOptional = employeeNOCRepository.findById(employeeNocId);
        if (!employeeNOCOptional.isPresent()) {
            throw new BadRequestAlertException("Employee NOC not found", "EmployeeNOC", "idnull");
        }

        String referenceNumber = getReferenceNumber();

        if (employeeNOCOptional.get().getCertificateStatus().equals(CertificateStatus.SENT_FOR_GENERATION)) {
            Optional<Employee> signatoryPerson = employeeRepository.findById(approvalDto.getSignatoryPersonId());

            employeeNOCOptional.get().setCertificateStatus(CertificateStatus.GENERATED);
            employeeNOCOptional.get().setSignatoryPerson(signatoryPerson.get());
            employeeNOCOptional.get().setIssueDate(approvalDto.getIssueDate());
            employeeNOCOptional.get().setReferenceNumber(referenceNumber);
            employeeNOCOptional.get().setGeneratedAt(Instant.now());
            employeeNOCOptional.get().setGeneratedBy(user);

            employeeNOCRepository.save(employeeNOCOptional.get());
        } else {
            throw new BadRequestAlertException("Only Pending Requests Are Allowed To Get Approval.", "EmployeeNOC", "accessForbidden");
        }

        return employeeNOCOptional.get();
    }

    private String getReferenceNumber() {
        String referenceNumber = "";
        for (int i = 1; i <= 9999; i++) {
            referenceNumber = generateReferenceNumber(i);
            if (!employeeNOCRepository.isEmployeeNocReferenceNumberUnique(referenceNumber)) {
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
        String referenceNumber = "" + year + "/NOC/" + number;

        return referenceNumber;
    }

    @Override
    public EmployeeNOCDTO rejectEmployeeNoc(long employeeNocId, CertificateApprovalDto approvalDto, User user) {
        Optional<EmployeeNOC> employeeNOCOptional = employeeNOCRepository.findById(employeeNocId);
        if (!employeeNOCOptional.isPresent()) {
            throw new BadRequestAlertException("Employee NOC not found", "EmployeeNOC", "idnull");
        }
        if (employeeNOCOptional.get().getCertificateStatus().equals(CertificateStatus.SENT_FOR_GENERATION)) {
            employeeNOCOptional.get().setCertificateStatus(CertificateStatus.REJECTED);
            employeeNOCOptional.get().setReason(approvalDto.getReason());
            employeeNOCRepository.save(employeeNOCOptional.get());
        } else {
            throw new BadRequestAlertException("Status Should Be Pending To Get Rejected", "EmployeeNOC", "accessForbidden");
        }

        return employeeNOCMapper.toDto(employeeNOCOptional.get());
    }

    @Override
    public Optional<EmployeeNOCDTO> getEmployeeNocPrintFormat(long employeeNocId) {
        return employeeNOCRepository.findApprovedEmployeeNocById(employeeNocId).map(employeeNOCMapper::toDto);
    }

    @Override
    public boolean isReferenceNumberUnique(String referenceNumber) {
        return employeeNOCRepository.isEmployeeNocReferenceNumberUnique(referenceNumber);
    }

    @Override
    public boolean hasAnyApprovedLeaveApplicationBetweenDateRange(LocalDate startDate, LocalDate endDate, Employee employee) {
        return leaveApplicationRepository.hasAnyApprovedLeaveApplicationFromStartDateToEndDate(startDate, endDate, employee.getId());
    }

    @Override
    public long calculateTotalLeaveDays(LocalDate startDate, LocalDate endDate) {
        return leaveDaysCalculationService.leaveDaysCalculation(startDate, endDate);
    }
}
