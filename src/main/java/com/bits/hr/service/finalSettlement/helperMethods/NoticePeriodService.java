package com.bits.hr.service.finalSettlement.helperMethods;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class NoticePeriodService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private GetConfigValueByKeyService getConfigValueByKeyService;

    public int getNumberOfNoticeDays(long employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (!employeeOptional.isPresent()) {
            log.debug("No employee found with associative ID");
            return 0;
        }
        int defaultNoticePeriodDays = getConfigValueByKeyService.getNoticePeriodInDays(employeeOptional.get().getEmployeeCategory());
        Integer noticePeriodInEmployee = employeeOptional.get().getNoticePeriodInDays();
        if (noticePeriodInEmployee == null) {
            return defaultNoticePeriodDays;
        } else {
            return (int) noticePeriodInEmployee;
        }
    }
}
