package com.bits.hr.service.attendanceSync.helperService;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeResignationRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonthLengthCalculationService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;

    public int getDaysInMonth(long employeeId, int year, int month) {
        Employee employee = employeeRepository.findById(employeeId).get();
        // if DOJ is after start date && DOR is before end date ==> regenerate date list;
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());

        LocalDate newStartDate = startDate;
        LocalDate newEndDate = endDate;

        if (employee.getDateOfJoining().isAfter(startDate)) newStartDate = employee.getDateOfJoining();
        // Month = resigning month and payable gross != mainGross // not full month //
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository
            .findEmployeeResignationByEmployeeId(employee.getId())
            .stream()
            .filter(x -> x.getLastWorkingDay() != null)
            .filter(x -> x.getApprovalStatus() == Status.APPROVED)
            .collect(Collectors.toList());
        if (!employeeResignationList.isEmpty()) {
            LocalDate lastWorkingDay = employeeResignationList.get(0).getLastWorkingDay();
            if (lastWorkingDay.isBefore(endDate)) {
                newEndDate = lastWorkingDay;
            }
        }

        return (int) ChronoUnit.DAYS.between(newStartDate, newEndDate);
    }
}
