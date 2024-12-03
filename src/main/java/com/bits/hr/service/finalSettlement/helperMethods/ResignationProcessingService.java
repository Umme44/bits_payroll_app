package com.bits.hr.service.finalSettlement.helperMethods;

import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.repository.EmployeeResignationRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResignationProcessingService {

    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;

    public Optional<LocalDate> getLastWorkingDay(long employeeId) {
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
            employeeId
        );

        if (employeeResignationList.size() < 1) {
            return Optional.ofNullable(null);
        } else {
            return Optional.of(employeeResignationList.get(0).getLastWorkingDay());
        }
    }

    public Optional<EmployeeResignation> getResignation(long employeeId) {
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
            employeeId
        );

        if (employeeResignationList.size() < 1) {
            return Optional.ofNullable(null);
        } else {
            return Optional.of(employeeResignationList.get(0));
        }
    }
}
