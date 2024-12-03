package com.bits.hr.service.employmentHistories;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.EmploymentHistory;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EventType;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeResignationRepository;
import com.bits.hr.repository.EmploymentHistoryRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmploymentHistoriesService {

    @Autowired
    private EmploymentHistoryRepository employmentHistoryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;

    public List<EmploymentHistory> getEmploymentHistories(long employeeId) {
        List<EmploymentHistory> employmentHistoryList = new ArrayList<>();

        if (employeeRepository.existsById(employeeId)) {
            Optional<Employee> employee = employeeRepository.findById(employeeId);
            employmentHistoryList = employmentHistoryRepository.getEmploymentHistoriesByEmployeeId(employeeId);

            createResignationEntry(employee.get(), employmentHistoryList);

            LocalDate today = LocalDate.now();
            LocalDate doj = employee.get().getDateOfJoining();
            LocalDate doc = LocalDate.now();

            if (employee.get().getDateOfConfirmation() != null) {
                doc = employee.get().getDateOfConfirmation();
            }

            // if DOJ is after today
            if (today.isAfter(doj)) {
                // and if joining not exist --> create joining
                if (
                    employmentHistoryList.stream().filter(x -> x.getEventType() == EventType.JOIN).collect(Collectors.toList()).size() == 0
                ) {
                    createJoiningEntry(employee.get());
                }
            }

            // if DOC is after today and employee is RE
            if (
                today.isAfter(doc) &&
                (
                    employee.get().getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE ||
                    employee.get().getEmployeeCategory() == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE
                )
            ) {
                // if Confirmation not exist --> create confirmation
                if (
                    employmentHistoryList.stream().filter(x -> x.getEventType() == EventType.CONFIRM).collect(Collectors.toList()).size() ==
                    0
                ) {
                    createConfirmationEntry(employee.get(), doc);
                }
            }

            // doc er pore doj -- many many case in BITS because of BRAC bank transfers
            if (
                doj.isAfter(doc) &&
                (
                    employee.get().getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE ||
                    employee.get().getEmployeeCategory() == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE
                )
            ) {
                List<EmploymentHistory> result = employmentHistoryRepository.getEmploymentHistoriesByEmployeeId(employeeId);
                result
                    .stream()
                    .forEach(x -> {
                        x.setPin(employee.get().getPin());
                    });
                Collections.swap(result, result.size() - 1, result.size() - 2);
                return result;
            }
            return employmentHistoryRepository.getEmploymentHistoriesByEmployeeId(employeeId);
        }

        return employmentHistoryRepository.getEmploymentHistoriesByEmployeeId(employeeId);
    }

    private void createJoiningEntry(Employee employee) {
        long employeeId = employee.getId();
        // effective salary on joining == first promotion / increment data
        List<EmploymentHistory> firstIncrementOrPromotion = employmentHistoryRepository.getFirstIncrementOrPromotion(employeeId);
        double effectiveSalary = employee.getMainGrossSalary();
        if (firstIncrementOrPromotion.size() > 0 && firstIncrementOrPromotion.get(0).getPreviousMainGrossSalary() != null) {
            effectiveSalary = firstIncrementOrPromotion.get(0).getPreviousMainGrossSalary();
        }
        EmploymentHistory employmentHistoryJoin = new EmploymentHistory();
        LocalDate doj = LocalDate.now();
        if (employee.getDateOfJoining() != null) {
            doj = employee.getDateOfJoining();
        }

        employmentHistoryJoin
            .employee(employee)
            .eventType(EventType.JOIN)
            .pin(employee.getPin())
            .previousMainGrossSalary(effectiveSalary)
            .currentMainGrossSalary(effectiveSalary)
            .effectiveDate(doj);

        employmentHistoryRepository.save(employmentHistoryJoin);
    }

    private void createConfirmationEntry(Employee employee, LocalDate doc) {
        EmploymentHistory employmentHistoryJoin = new EmploymentHistory();

        if (employee.getDateOfConfirmation() != null) {
            doc = employee.getDateOfConfirmation();
        }

        employmentHistoryJoin.employee(employee).pin(employee.getPin()).eventType(EventType.CONFIRM).effectiveDate(doc);

        employmentHistoryRepository.save(employmentHistoryJoin);
    }

    private void createResignationEntry(Employee employee, List<EmploymentHistory> employmentHistoryList) {
        // check if there are resignation entries
        // if confirmed resignation entries available
        // then check already resignation entry exists or not
        // if entry available in employment histories but resignation not available -> delete resignation
        // if entry available in employment histories and resignation available -> skip
        // if entry not available in employment histories and resignation available -> create new

        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
            employee.getId()
        );

        List<EmploymentHistory> employmentHistoryResignationList = employmentHistoryList
            .stream()
            .filter(x -> x.getEventType() == EventType.RESIGNATION)
            .collect(Collectors.toList());

        int resignationSize = employeeResignationList.size();
        int employmentHistoryResignationSize = employmentHistoryResignationList.size();

        // if entry available in employment histories but resignation not available -> delete resignation
        if (resignationSize == 0 && employmentHistoryResignationSize > 0) {
            for (EmploymentHistory eh : employmentHistoryResignationList) {
                employmentHistoryRepository.deleteById(eh.getId());
            }
        }
        // if entry available in employment histories and resignation available -> skip
        // if entry not available in employment histories and resignation available -> create new

        else if (resignationSize > 0 && employmentHistoryResignationSize == 0) {
            EmploymentHistory ehResignation = new EmploymentHistory();
            LocalDate lastWorkingDay = employeeResignationList.get(0).getLastWorkingDay();

            ehResignation.employee(employee).pin(employee.getPin()).eventType(EventType.RESIGNATION).effectiveDate(lastWorkingDay);
            employmentHistoryRepository.save(ehResignation);
        }
    }
}
