package com.bits.hr.service.communication.NID;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Nominee;
import com.bits.hr.domain.PfNominee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.NomineeRepository;
import com.bits.hr.repository.PfNomineeRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatchNIDVerificationService {

    @Autowired
    NIDVerificationService nidVerificationService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private NomineeRepository nomineeRepository;

    @Autowired
    private PfNomineeRepository pfNomineeRepository;

    public boolean routineNidVerificationService() {
        try {
            verifyEmployeesNid();
            verifyNomineesNid();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void verifyEmployeesNid() {
        List<Employee> employeeList = employeeRepository.findEmployeeByUnapprovedNid();
        for (Employee employee : employeeList) {
            if (nidVerificationService.verifyEmployeeNID(employee)) {
                employee.isNidVerified(true);
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);
            }
        }
    }

    public void verifyNomineesNid() {
        List<Nominee> nomineeList = nomineeRepository.getNomineesForNidVerification();
        for (Nominee nominee : nomineeList) {
            boolean isNomineeNidVerified = nidVerificationService.isNomineeNidVerified(nominee);
            boolean isNomineeGuardianNIDVerified = nidVerificationService.isNomineeGuardianNidVerified(nominee);
            nominee.setIsNidVerified(isNomineeNidVerified);
            if (nominee.getGuardianName() != null && !nominee.getGuardianName().isEmpty()) {
                nominee.setIsGuardianNidVerified(isNomineeGuardianNIDVerified);
            }
            nomineeRepository.save(nominee);
        }

        List<PfNominee> pfNomineeList = pfNomineeRepository.getNomineesForNidVerification();
        for (PfNominee pfNominee : pfNomineeList) {
            boolean isPfNomineeNidVerified = nidVerificationService.isPFNomineeNIDVerified(pfNominee);
            boolean isPfNomineeGuardianNIDVerified = nidVerificationService.isPFNomineeGuardianNIDVerified(pfNominee);
            pfNominee.setIsNidVerified(isPfNomineeNidVerified);
            if (pfNominee.getGuardianName() != null && !pfNominee.getGuardianName().isEmpty()) {
                pfNominee.setIsGuardianNidVerified(isPfNomineeGuardianNIDVerified);
            }
            pfNomineeRepository.save(pfNominee);
        }
    }
}
