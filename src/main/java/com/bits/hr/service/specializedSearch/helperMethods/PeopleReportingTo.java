package com.bits.hr.service.specializedSearch.helperMethods;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeResignationRepository;
import com.bits.hr.service.specializedSearch.dto.EmployeeSpecializedSearch;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PeopleReportingTo {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    MapEmployeeToResult mapEmployeeToResult;

    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;

    public List<EmployeeSpecializedSearch> get(long employeeId) {
        List<Employee> peopleReportingTo = employeeRepository.getMyTeamByReportingToId(employeeId);
        List<EmployeeSpecializedSearch> result = new ArrayList<>();

        for (Employee employee : peopleReportingTo) {
            boolean willInclude = willEmployeeIncludeIn(employee);
            if (willInclude) {
                result.add(mapEmployeeToResult.map(employee));
            }
        }
        return result;
    }

    /**
     * Filter Employee by Category, Employment Status, Last Working Day, Contract Period End Date
     * @param subordinate
     * @return
     */
    private boolean willEmployeeIncludeIn(Employee subordinate) {
        LocalDate today = LocalDate.now();
        // exclude resigned and last working day crossed employee
        if (subordinate.getEmploymentStatus() != null && subordinate.getEmploymentStatus().equals(EmploymentStatus.RESIGNED)) {
            if (
                (
                    subordinate.getEmployeeCategory() != null &&
                    subordinate.getEmployeeCategory().equals(EmployeeCategory.CONTRACTUAL_EMPLOYEE)
                ) ||
                (subordinate.getEmployeeCategory() != null && subordinate.getEmployeeCategory().equals(EmployeeCategory.INTERN))
            ) {
                if (subordinate.getContractPeriodExtendedTo() != null && (subordinate.getContractPeriodEndDate().isBefore(today))) {
                    return false;
                } else if (subordinate.getContractPeriodEndDate() != null && (subordinate.getContractPeriodEndDate().isBefore(today))) {
                    return false;
                } else {
                    List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
                        subordinate.getId()
                    );
                    boolean anyLastWorkingDayCrossed = false;

                    for (EmployeeResignation employeeResignation : employeeResignationList) {
                        if (
                            employeeResignation.getLastWorkingDay() != null && ((employeeResignation.getLastWorkingDay().isBefore(today)))
                        ) {
                            anyLastWorkingDayCrossed = true;
                            break;
                        }
                    }
                    if (!anyLastWorkingDayCrossed) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
                    subordinate.getId()
                );
                boolean anyLastWorkingDayCrossed = false;

                for (EmployeeResignation employeeResignation : employeeResignationList) {
                    if (employeeResignation.getLastWorkingDay() != null && ((employeeResignation.getLastWorkingDay().isBefore(today)))) {
                        anyLastWorkingDayCrossed = true;
                        break;
                    }
                }
                if (!anyLastWorkingDayCrossed) {
                    return true;
                } else {
                    return false;
                }
            }
        } else if (
            (
                subordinate.getEmployeeCategory() != null && subordinate.getEmployeeCategory().equals(EmployeeCategory.CONTRACTUAL_EMPLOYEE)
            ) ||
            (subordinate.getEmployeeCategory() != null && subordinate.getEmployeeCategory().equals(EmployeeCategory.INTERN))
        ) {
            if (subordinate.getContractPeriodExtendedTo() != null && (subordinate.getContractPeriodExtendedTo().isBefore(today))) {
                return false;
            } else if (subordinate.getContractPeriodEndDate() != null && (subordinate.getContractPeriodEndDate().isBefore(today))) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
