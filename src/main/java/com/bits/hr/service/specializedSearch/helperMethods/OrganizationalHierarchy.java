package com.bits.hr.service.specializedSearch.helperMethods;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeResignationRepository;
import com.bits.hr.service.specializedSearch.dto.EmployeeSpecializedSearch;
import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationalHierarchy {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MapEmployeeToResult mapEmployeeToResult;

    public List<EmployeeSpecializedSearch> get(long employeeId) {
        List<EmployeeSpecializedSearch> result = new ArrayList<>();

        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()) {
            result.add(mapEmployeeToResult.map(employee.get()));
        }
        Set<Long> restrictedIds = new HashSet<>();
        restrictedIds.add(employeeId);

        for (int i = 0; i <= 20; i++) {
            // empty
            if (employee.get().getReportingTo() == null) {
                break;
            }

            // already on the list
            if (restrictedIds.contains(employee.get().getReportingTo().getId())) {
                break;
            }

            employee = employeeRepository.findById(employee.get().getReportingTo().getId());
            if (employee.isPresent()) {
                result.add(mapEmployeeToResult.map(employee.get()));
                restrictedIds.add(employee.get().getId());
            }
        }
        Collections.reverse(result);
        return result;
    }
}
