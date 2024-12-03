package com.bits.hr.web.rest.util;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.AttendanceSummaryRepository;
import com.bits.hr.repository.EmployeeRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payroll-mgt")
public class AttendanceSummery {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AttendanceSummaryRepository attendanceSummaryRepository;

    @GetMapping("/attendance-summery-check/{year}/{month}")
    public List<String> salaryGeneration(@PathVariable int year, @PathVariable int month) throws Exception {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = LocalDate.of(year, month, firstDayOfMonth.lengthOfMonth());
        Set<Employee> employeeSet = employeeRepository.getEligibleEmployeeSetForSalaryGeneration_v2(firstDayOfMonth, lastDayOfMonth);
        Set<Employee> attendanceSummaryEmployeeSet = attendanceSummaryRepository.findEmployeeSetByYearAndMonth(year, month);

        employeeSet.removeAll(attendanceSummaryEmployeeSet);

        List<Employee> employeeList = new ArrayList<>(employeeSet);

        List<String> result = new ArrayList<>();
        for (Employee employee : employeeList) {
            result.add(employee.getPin() + " - " + employee.getFullName() + " ");
        }
        return result;
    }
}
