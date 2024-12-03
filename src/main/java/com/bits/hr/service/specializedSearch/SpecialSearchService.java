package com.bits.hr.service.specializedSearch;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.specializedSearch.dto.EmployeeSpecializedSearch;
import com.bits.hr.service.specializedSearch.helperMethods.MapEmployeeToResult;
import com.bits.hr.service.specializedSearch.helperMethods.OrganizationalHierarchy;
import com.bits.hr.service.specializedSearch.helperMethods.PeopleReportingTo;
import java.nio.file.OpenOption;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecialSearchService {

    @Autowired
    MapEmployeeToResult mapEmployeeToResult;

    @Autowired
    OrganizationalHierarchy organizationalHierarchy;

    @Autowired
    PeopleReportingTo peopleReportingTo;

    @Autowired
    EmployeeRepository employeeRepository;

    public Optional<EmployeeSpecializedSearch> getResult(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(RuntimeException::new);
        EmployeeSpecializedSearch result = mapEmployeeToResult.map(employee);
        result.setOrganizationalHierarchy(organizationalHierarchy.get(employeeId));
        result.setPeopleReportingTo(peopleReportingTo.get(employeeId));

        return Optional.of(result);
    }
}
