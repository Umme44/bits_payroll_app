package com.bits.hr.service.salaryGenerationFractional;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.EventType;
import com.bits.hr.domain.enumeration.Visibility;
import com.bits.hr.repository.*;
import com.bits.hr.service.EmployeeSalaryService;
import com.bits.hr.util.SalaryGenerationMasterService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class FractionalSalaryGenerationService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmploymentHistoryRepository employmentHistoryRepository;

    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    @Autowired
    private SalaryGenerationMasterService salaryGenerationMasterService;

    @Autowired
    private SalaryGeneratorMasterRepository salaryGeneratorMasterRepository;

    @Autowired
    private FractionalSalaryGenerationServiceSingleUnit fractionalSalaryGenerationServiceSingleUnit;

    @Autowired
    private SalaryCertificateRepository salaryCertificateRepository;

    @Autowired
    private HoldSalaryDisbursementRepository holdSalaryDisbursementRepository;

    /**
     * on salary generation
     * take list of all previous salary items
     * Generate salary [ master should not be locked ]
     * <p>
     * Delete previous all entries of employee_salary table [ on successful salary generation ]
     */
    public boolean generateAndSave(int year, int month) {
        try {
            List<EmployeeSalary> newlyGeneratedSalaryList = generate(year, month);
            // delete previous record if exist , delete previous records
            // set existing isMobileBillUpload to false.
            // update salary generation master
            SalaryGeneratorMaster salaryGeneratorMaster;

            List<EmployeeSalary> oldSalaryList = employeeSalaryService.findAllByYearAndMonth(year, month);
            HashMap<String, Boolean> salaryVsHoldHashMap = getEmployeeSalaryVsIsHoldSalaryHashmap(oldSalaryList);
            HashMap<String, Boolean> salaryVsVisibilityHashMap = getEmployeeSalaryVisibilityHashmap(oldSalaryList);

            if (salaryGenerationMasterService.isExistSalaryGeneratorMaster(year, month)) {
                // delete previously generated salaries
                employeeSalaryService.deleteByYearAndMonth(year, month);
                salaryGeneratorMaster = salaryGenerationMasterService.getOrCreateSalaryGeneratorMaster(year, month);
                salaryGeneratorMaster.setIsGenerated(false); // if regeneration fails ; false will stay
                salaryGeneratorMaster.setVisibility(Visibility.NOT_VISIBLE_TO_EMPLOYEE);
                save(salaryGeneratorMaster);
            }

            for (EmployeeSalary salary : newlyGeneratedSalaryList) {
                boolean wasOnHold = salaryVsHoldHashMap.getOrDefault(makeSalaryCompositeKey(salary), false);
                if (wasOnHold) {
                    salary.setIsHold(wasOnHold);
                }

                boolean wasVisibleToEmployee = salaryVsVisibilityHashMap.getOrDefault(makeSalaryCompositeKey(salary), false);
                if (wasVisibleToEmployee) {
                    salary.setIsVisibleToEmployee(wasVisibleToEmployee);
                }

                save(salary);
            }

            salaryGeneratorMaster = salaryGenerationMasterService.getOrCreateSalaryGeneratorMaster(year, month);
            salaryGeneratorMaster.setIsGenerated(true);
            save(salaryGeneratorMaster);

            return true;
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }

    private void save(EmployeeSalary employeeSalary) {
        employeeSalaryService.save(employeeSalary);
    }

    private void save(SalaryGeneratorMaster salaryGenerationMaster) {
        salaryGeneratorMasterRepository.save(salaryGenerationMaster);
    }

    public List<EmployeeSalary> generate(int year, int month) {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        List<Employee> employeeList = getAllActiveEmployees(firstDayOfMonth);
        List<EmployeeSalary> employeeSalaryList = new ArrayList<>();
        // task 1: get all active employee ** no resigned employee salary need to be generated
        // task 2: check if promotion or increment happened in this month
        // task 3: if ( promotion or increment happened in this month ) => generate fractional salary
        // task 4: if ( NOT (promotion or increment happened in this month) ) => generate normal monthly salary
        // task 5: check if date of joining is in this month
        // task 6: get the time range of salary start date and salary end date
        int daysInMonth = firstDayOfMonth.lengthOfMonth();

        for (Employee employee : employeeList) {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = LocalDate.of(year, month, daysInMonth);

            if (employee.getDateOfJoining() != null && startDate.isBefore(employee.getDateOfJoining())) {
                startDate = employee.getDateOfJoining();
            }
            Optional<EmployeeSalary> employeeSalaryOptional = fractionalSalaryGenerationServiceSingleUnit.generateMonthlySalary(
                employee,
                year,
                month,
                startDate,
                endDate
            );

            if (employeeSalaryOptional.isPresent()) {
                employeeSalaryList.add(employeeSalaryOptional.get());
            } else {
                log.error(" Salary Generation Failed for pin: " + employee.getPin());
                continue;
            }
        }
        return employeeSalaryList;
    }

    private List<Employee> getAllActiveEmployees(LocalDate firstDayOfMonth) {
        LocalDate lastDateOfMonth = LocalDate.of(firstDayOfMonth.getYear(), firstDayOfMonth.getMonth(), firstDayOfMonth.lengthOfMonth());
        return employeeRepository.getEligibleEmployeeForSalaryGeneration_v2(firstDayOfMonth, lastDateOfMonth);
    }

    private HashMap<String, Boolean> getEmployeeSalaryVsIsHoldSalaryHashmap(List<EmployeeSalary> employeeSalaryList) {
        HashMap<String, Boolean> employeeHoldSalaryHashMap = new HashMap<>();
        for (EmployeeSalary employeeSalary : employeeSalaryList) {
            employeeHoldSalaryHashMap.put(makeSalaryCompositeKey(employeeSalary), employeeSalary.getIsHold());
        }
        return employeeHoldSalaryHashMap;
    }

    private HashMap<String, Boolean> getEmployeeSalaryVisibilityHashmap(List<EmployeeSalary> employeeSalaryList) {
        HashMap<String, Boolean> employeeSalaryVisibilityHashMap = new HashMap<>();
        for (EmployeeSalary employeeSalary : employeeSalaryList) {
            employeeSalaryVisibilityHashMap.put(makeSalaryCompositeKey(employeeSalary), employeeSalary.getIsVisibleToEmployee());
        }
        return employeeSalaryVisibilityHashMap;
    }

    private String makeSalaryCompositeKey(EmployeeSalary employeeSalary) {
        return employeeSalary.getPin().trim() + employeeSalary.getYear().toString() + employeeSalary.getMonth();
    }
}
