package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.SalaryCertificate;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.SalaryCertificateRepository;
import com.bits.hr.repository.UserRepository;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.CertificateApprovalDto;
import com.bits.hr.service.dto.SalaryCertificateDTO;
import com.bits.hr.service.mapper.SalaryCertificateMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class SalaryCertificateHRServiceImpl {

    @Autowired
    private SalaryCertificateRepository salaryCertificateRepository;

    @Autowired
    private SalaryCertificateMapper salaryCertificateMapper;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<SalaryCertificateDTO> getAllPending() {
        List<SalaryCertificate> pendingList = salaryCertificateRepository.getAllPending();
        return salaryCertificateMapper.toDto(pendingList);
    }

    public boolean approveSelected(List<Long> selectedIds, Long signatoryPersonId) {
        try {
            Optional<Employee> signatoryPersonOptional = employeeRepository.findById(signatoryPersonId);
            if (!signatoryPersonOptional.isPresent()) {
                throw new BadRequestAlertException("Signatory Person Not Found :" + signatoryPersonId, "employee", "noEmployeeFound");
            }

            List<SalaryCertificate> salaryCertificates = salaryCertificateRepository.getAllPendingByIds(selectedIds);
            for (SalaryCertificate salaryCertificate : salaryCertificates) {
                salaryCertificate.setStatus(Status.APPROVED);
                salaryCertificate.setSanctionBy(this.getCurrentUser());
                salaryCertificate.setSanctionAt(LocalDate.now());
                salaryCertificate.setSignatoryPerson(signatoryPersonOptional.get());
                salaryCertificateRepository.save(salaryCertificate);
            }
            return true;
        } catch (Exception exception) {
            log.error(exception);
            return false;
        }
    }

    public boolean approveSalaryCertificate(Long certificateId, CertificateApprovalDto dto) {
        try {
            Optional<Employee> signatoryPersonOptional = employeeRepository.findById(dto.getSignatoryPersonId());
            if (!signatoryPersonOptional.isPresent()) {
                throw new BadRequestAlertException(
                    "Signatory Person Not Found :" + dto.getSignatoryPersonId(),
                    "employee",
                    "noEmployeeFound"
                );
            }

            Optional<SalaryCertificate> salaryCertificateOptional = salaryCertificateRepository.findById(certificateId);
            if (!salaryCertificateOptional.isPresent()) {
                throw new BadRequestAlertException(
                    "Salary Certificate Not Found :" + certificateId,
                    "salaryCertificate",
                    "noSalaryCertificateFound"
                );
            }

            String referenceNumber = getReferenceNumber();

            SalaryCertificate salaryCertificate = salaryCertificateOptional.get();

            salaryCertificate.setStatus(Status.APPROVED);
            salaryCertificate.setSanctionBy(this.getCurrentUser());
            salaryCertificate.setSanctionAt(LocalDate.now());
            salaryCertificate.setReferenceNumber(referenceNumber);
            salaryCertificate.setSignatoryPerson(signatoryPersonOptional.get());
            salaryCertificateRepository.save(salaryCertificate);

            return true;
        } catch (Exception exception) {
            log.error(exception);
            return false;
        }
    }

    private String getReferenceNumber() {
        String referenceNumber = "";
        for (int i = 1; i <= 9999; i++) {
            referenceNumber = generateReferenceNumber(i);
            if (!salaryCertificateRepository.isEmployeeNocReferenceNumberUnique(referenceNumber)) {
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
        String referenceNumber = "" + year + "/SC/" + number;

        return referenceNumber;
    }

    public Optional<SalaryCertificate> findById(Long id) {
        return salaryCertificateRepository.findById(id);
    }

    public boolean approveAll() {
        return false;
    }

    public boolean denySelected(List<Long> selectedIds) {
        try {
            List<SalaryCertificate> salaryCertificates = salaryCertificateRepository.getAllPendingByIds(selectedIds);
            for (SalaryCertificate salaryCertificate : salaryCertificates) {
                salaryCertificate.setStatus(Status.NOT_APPROVED);
                salaryCertificate.setSanctionBy(this.getCurrentUser());
                salaryCertificate.setSanctionAt(LocalDate.now());
                salaryCertificateRepository.save(salaryCertificate);
            }
            return true;
        } catch (Exception exception) {
            log.error(exception);
            return false;
        }
    }

    public boolean denyAll() {
        return false;
    }

    private User getCurrentUser() {
        Optional<Long> userId = currentEmployeeService.getCurrentUserId();
        return userRepository.getOne(userId.get());
    }
}
