package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmploymentCertificate;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.service.dto.CertificateApprovalDto;
import com.bits.hr.service.dto.EmploymentCertificateDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.EmploymentCertificate}.
 */
public interface EmploymentCertificateService {
    EmploymentCertificate createEmploymentCertificate(Employee employee, User user);
    EmploymentCertificateDTO updateEmploymentCertificate(EmploymentCertificateDTO employmentCertificateDTO, User user);
    Page<EmploymentCertificateDTO> findAll(String searchText, CertificateStatus status, int selectedYear, Pageable pageable);
    Page<EmploymentCertificateDTO> findAllByEmployeeId(Employee employee, CertificateStatus status, Pageable pageable);
    Optional<EmploymentCertificateDTO> findOne(Long id);
    void delete(Long id);
    EmploymentCertificate approveEmployeeNoc(long employmentCertificateId, CertificateApprovalDto approvalDto, User user);
    EmploymentCertificateDTO rejectEmployeeNoc(long employmentCertificateId, CertificateApprovalDto approvalDto, User user);
    Optional<EmploymentCertificateDTO> getEmploymentCertificatePrintFormat(long employmentCertificateId);
    boolean isReferenceNumberUnique(String referenceNumber);
}
