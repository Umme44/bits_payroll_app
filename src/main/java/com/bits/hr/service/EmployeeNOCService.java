package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeNOC;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.service.dto.CertificateApprovalDto;
import com.bits.hr.service.dto.EmployeeNOCDTO;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.EmployeeNOC}.
 */
public interface EmployeeNOCService {
    EmployeeNOC createEmployeeNOC(EmployeeNOCDTO employeeNOCDTO, Employee employee, User user);

    EmployeeNOCDTO updateEmployeeNOC(EmployeeNOCDTO employeeNOCDTO, Employee employee, User user);

    Page<EmployeeNOCDTO> findAll(String searchText, CertificateStatus status, Integer selectedYear, Pageable pageable);
    Page<EmployeeNOCDTO> findAllByEmployeeId(Employee employee, CertificateStatus status, Pageable pageable);

    Optional<EmployeeNOCDTO> findOne(Long id);

    void delete(Long id);

    EmployeeNOC approveEmployeeNoc(long employeeNocId, CertificateApprovalDto approvalDto, User user);

    EmployeeNOCDTO rejectEmployeeNoc(long employeeNocId, CertificateApprovalDto approvalDto, User user);

    Optional<EmployeeNOCDTO> getEmployeeNocPrintFormat(long employeeNocId);

    boolean isReferenceNumberUnique(String referenceNumber);

    boolean hasAnyApprovedLeaveApplicationBetweenDateRange(LocalDate startDate, LocalDate endDate, Employee employee);

    long calculateTotalLeaveDays(LocalDate startDate, LocalDate endDate);
}
