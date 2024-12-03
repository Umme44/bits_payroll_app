package com.bits.hr.service.salaryGeneration;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.SalaryGeneratorMaster;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.Visibility;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.repository.SalaryGeneratorMasterRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class SalaryVisibilityControlServiceImpl implements SalaryVisibilityControlService {

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    private SalaryGeneratorMasterRepository salaryGeneratorMasterRepository;

    //note: Service methods to change Salary Generator Master visibility status are below

    public boolean makeSalaryVisibleToEmployee(int year, int month) {
        try {
            String yearString = String.valueOf(year);
            String monthString = String.valueOf(month);

            Optional<SalaryGeneratorMaster> salaryGeneratorMasterOptional = salaryGeneratorMasterRepository.findSalaryGeneratorMastersByYearAndMonth(
                yearString,
                monthString
            );
            if (!salaryGeneratorMasterOptional.isPresent()) {
                throw new RuntimeException("Salary Not Found for the month" + Month.fromInteger(month) + ", " + year);
            }

            List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findEmployeeSalaryByYearAndMonth(
                year,
                Month.fromInteger(month)
            );

            salaryGeneratorMasterOptional.get().setVisibility(Visibility.VISIBLE_TO_EMPLOYEE);
            for (int i = 0; i < employeeSalaryList.size(); i++) {
                EmployeeSalary employeeSalary = employeeSalaryList.get(i);
                employeeSalary.setIsVisibleToEmployee(true);

                employeeSalaryRepository.save(employeeSalary);
            }
            salaryGeneratorMasterRepository.save(salaryGeneratorMasterOptional.get());

            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    public boolean makeSalaryHideFromEmployee(int year, int month) {
        try {
            String yearString = String.valueOf(year);
            String monthString = String.valueOf(month);

            Optional<SalaryGeneratorMaster> salaryGeneratorMasterOptional = salaryGeneratorMasterRepository.findSalaryGeneratorMastersByYearAndMonth(
                yearString,
                monthString
            );
            if (!salaryGeneratorMasterOptional.isPresent()) {
                throw new RuntimeException("Salary Not Found for the month" + Month.fromInteger(month) + ", " + year);
            }

            List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findEmployeeSalaryByYearAndMonth(
                year,
                Month.fromInteger(month)
            );

            salaryGeneratorMasterOptional.get().setVisibility(Visibility.NOT_VISIBLE_TO_EMPLOYEE);
            for (int i = 0; i < employeeSalaryList.size(); i++) {
                EmployeeSalary employeeSalary = employeeSalaryList.get(i);
                employeeSalary.setIsVisibleToEmployee(false);

                employeeSalaryRepository.save(employeeSalary);
            }
            salaryGeneratorMasterRepository.save(salaryGeneratorMasterOptional.get());

            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    @Override
    public boolean makeSalaryPartiallyVisibleToEmployee(int year, int month) {
        try {
            String yearString = String.valueOf(year);
            String monthString = String.valueOf(month);

            Optional<SalaryGeneratorMaster> salaryGeneratorMasterOptional = salaryGeneratorMasterRepository.findSalaryGeneratorMastersByYearAndMonth(
                yearString,
                monthString
            );
            if (!salaryGeneratorMasterOptional.isPresent()) {
                throw new RuntimeException("Salary Not Found for the month" + Month.fromInteger(month) + ", " + year);
            }

            List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findEmployeeSalaryByYearAndMonth(
                year,
                Month.fromInteger(month)
            );

            salaryGeneratorMasterOptional.get().setVisibility(Visibility.PARTIALLY_VISIBLE);

            salaryGeneratorMasterRepository.save(salaryGeneratorMasterOptional.get());

            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    //note: Service methods to change Employee Salary visibility status are below

    public boolean makeEmployeeSalaryVisibleToEmployee(Employee employee, int year, Month month) {
        try {
            String monthString = String.valueOf(Month.fromEnum(month));

            Optional<SalaryGeneratorMaster> salaryGeneratorMasterOptional = salaryGeneratorMasterRepository.findSalaryGeneratorMastersByYearAndMonth(
                String.valueOf(year),
                monthString
            );
            if (!salaryGeneratorMasterOptional.isPresent()) {
                throw new RuntimeException("Salary Not Found for the month" + month + ", " + year);
            }

            Optional<EmployeeSalary> employeeSalaryOptional = employeeSalaryRepository.findEmployeeSalaryByEmployeeIdYearAndMonth(
                employee.getId(),
                year,
                month
            );
            if (!employeeSalaryOptional.isPresent()) {
                throw new RuntimeException("Salary of " + employee.getFullName() + "Not Found for the month" + month + ", " + year);
            }

            salaryGeneratorMasterOptional.get().setVisibility(Visibility.PARTIALLY_VISIBLE);

            employeeSalaryOptional.get().setIsVisibleToEmployee(true);

            salaryGeneratorMasterRepository.save(salaryGeneratorMasterOptional.get());
            employeeSalaryRepository.save(employeeSalaryOptional.get());

            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    public boolean makeEmployeeSalaryHideFromEmployee(Employee employee, int year, Month month) {
        try {
            String monthString = String.valueOf(Month.fromEnum(month));

            Optional<SalaryGeneratorMaster> salaryGeneratorMasterOptional = salaryGeneratorMasterRepository.findSalaryGeneratorMastersByYearAndMonth(
                String.valueOf(year),
                monthString
            );
            if (!salaryGeneratorMasterOptional.isPresent()) {
                throw new RuntimeException("Salary Not Found for the month" + month + ", " + year);
            }

            Optional<EmployeeSalary> employeeSalaryOptional = employeeSalaryRepository.findEmployeeSalaryByEmployeeIdYearAndMonth(
                employee.getId(),
                year,
                month
            );
            if (!employeeSalaryOptional.isPresent()) {
                throw new RuntimeException("Salary of " + employee.getFullName() + "Not Found for the month" + month + ", " + year);
            }

            salaryGeneratorMasterOptional.get().setVisibility(Visibility.PARTIALLY_VISIBLE);

            employeeSalaryOptional.get().setIsVisibleToEmployee(false);

            salaryGeneratorMasterRepository.save(salaryGeneratorMasterOptional.get());
            employeeSalaryRepository.save(employeeSalaryOptional.get());

            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }
}
