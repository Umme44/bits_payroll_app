package com.bits.hr.service.scheduler.schedulingService;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ScheduledDataFixingService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PfAccountRepository pfAccountRepository;

    public void changeAllInternProfileMarkedAsContractual() {
        try {
            List<Employee> internEmployeeList = employeeRepository.findAllInternProfileMarkedAsContractual();
            for (Employee employee : internEmployeeList) {
                employee.setEmployeeCategory(EmployeeCategory.INTERN);
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    public void addDojAndDocToAllExistingEmployeePfAccount() {
        try {
            List<PfAccount> pfAccountList = pfAccountRepository.findAll();

            for (int i = 0; i < pfAccountList.size(); i++) {
                Optional<Employee> employeeOptional = employeeRepository.findByPin(pfAccountList.get(i).getPin());

                if (employeeOptional.isPresent()) {
                    PfAccount pfAccount = pfAccountList.get(i);
                    pfAccount.setDateOfJoining(
                        employeeOptional.get().getDateOfJoining() != null ? employeeOptional.get().getDateOfJoining() : null
                    );
                    pfAccount.setDateOfConfirmation(
                        employeeOptional.get().getDateOfConfirmation() != null ? employeeOptional.get().getDateOfConfirmation() : null
                    );
                    pfAccountRepository.save(pfAccount);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
}
