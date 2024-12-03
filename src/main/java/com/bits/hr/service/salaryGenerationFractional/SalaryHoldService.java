package com.bits.hr.service.salaryGenerationFractional;

import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.repository.EmployeeResignationRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryHoldService {

    @Autowired
    EmployeeResignationRepository employeeResignationRepository;

    public boolean isSalaryHold(long employeeId, int year, int month) {
        List<EmployeeResignation> employeeResignations = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
            employeeId
        );

        if (employeeResignations.size() == 0) {
            return false;
        }
        if (employeeResignations.size() > 0) {
            EmployeeResignation employeeResignation = employeeResignations.get(0);

            int resignationYear = employeeResignation.getLastWorkingDay().getYear();
            int resignationMonth = employeeResignation.getLastWorkingDay().getMonth().getValue();
            boolean isBrokenMonth =
                employeeResignation.getLastWorkingDay().getDayOfMonth() < employeeResignation.getLastWorkingDay().lengthOfMonth();

            // normal use case >> block last month salary
            if (year == resignationYear && month == resignationMonth) {
                return true;
            }
            // special use case , if resigned broken month last-1 month salary will be hold too
            if (isBrokenMonth) {
                int resignationYearBefore = employeeResignation.getLastWorkingDay().minusMonths(1).getYear();
                int resignationMonthBefore = employeeResignation.getLastWorkingDay().minusMonths(1).getMonth().getValue();
                if (year == resignationYearBefore && month == resignationMonthBefore) {
                    return true;
                }
            }
        }

        return false;
    }
}
