package com.bits.hr.service.salaryGenerationFractional;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EmployeeSalaryService;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import io.undertow.util.BadRequestException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class SingeEmployeeSalaryGenerationService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private FractionalSalaryGenerationServiceSingleUnit fractionalSalaryGenerationServiceSingleUnit;

    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    public boolean generateAndSaveSingleSalary(long employeeId, int year, int month) {
        try {
            // validation and data gathering
            String pin = employeeRepository.getPinByEmployeeId(employeeId);

            Employee employee = employeeRepository.findById(employeeId).get();

            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());

            Optional<EmployeeSalary> employeeSalary = fractionalSalaryGenerationServiceSingleUnit.generateMonthlySalary(
                employee,
                year,
                month,
                startDate,
                endDate
            );

            // do not save if already exist
            // remove duplicates
            List<EmployeeSalaryDTO> alreadyGenerated = employeeSalaryService.findByEmployeeIdAndYearAndMonth(employeeId, year, month);
            if (!alreadyGenerated.isEmpty()) {
                List<Long> idList = new ArrayList<>();
                alreadyGenerated.forEach(x -> {
                    idList.add(x.getId());
                });
                employeeSalaryService.delete(idList);
            }

            if (employeeSalary.isPresent()) {
                employeeSalaryService.save(employeeSalary.get());
                return true;
            } else {
                log.error("salary generation failed for following employees pin:" + pin);
                return false;
            }
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }
}
