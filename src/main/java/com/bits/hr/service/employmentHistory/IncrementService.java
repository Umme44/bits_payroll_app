package com.bits.hr.service.employmentHistory;

import com.bits.hr.domain.enumeration.EventType;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.EmployeeNotFoundException;
import com.bits.hr.service.BandService;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.EmploymentHistoryService;
import com.bits.hr.service.dto.BandDTO;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.dto.EmploymentHistoryDTO;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IncrementService {

    private final EmployeeService employeeService;

    private final EmploymentHistoryService employmentHistoryService;
    private final BandService bandService;

    public IncrementService(EmployeeService employeeService, EmploymentHistoryService employmentHistoryService, BandService bandService) {
        this.employeeService = employeeService;
        this.employmentHistoryService = employmentHistoryService;
        this.bandService = bandService;
    }

    @Transactional
    public EmploymentHistoryDTO createIncrement(EmploymentHistoryDTO employmentHistoryDTO) {
        // task-1: before a new increment, get Employee field data to save into employmentHistoryDTO fields-
        //     a) gross b) designation c) department d) reporting to e) unit f) band
        // task-2: when an increment is initiated, update Employee's current -
        //     a) main gross b) band c) mobile ceiling d) welfare fund
        // nb: it should be transactional

        // load employee from specified employee in Employment History dto
        // update every data from employee to EmploymentHistoryDTO except current gross
        // update EmployeeDto object
        // save employee object
        // save employmentHistoryObject

        // validation
        if (employmentHistoryDTO.getEmployeeId() == null) {
            throw new RuntimeException("Employee ID is required!");
        }

        if (employmentHistoryDTO.getCurrentMainGrossSalary() == null) {
            throw new RuntimeException("Gross salary is required!");
        }

        if (employmentHistoryDTO.getChangedBandId() == null) {
            throw new RuntimeException("Changed Band is required!");
        }

        Optional<EmployeeDTO> employeeOptional = employeeService.findOne(employmentHistoryDTO.getEmployeeId());
        if (!employeeOptional.isPresent()) {
            throw new EmployeeNotFoundException(employmentHistoryDTO.getEmployeeId());
        }

        // main logic
        EmployeeDTO employee = employeeOptional.get();

        if (employee.getReferenceId() != null) {
            employmentHistoryDTO.setReferenceId(employee.getReferenceId());
        }
        employmentHistoryDTO.setPin(employee.getPin());
        employmentHistoryDTO.setEventType(EventType.INCREMENT);
        employmentHistoryDTO.setIsModifiable(true);

        // if effective date not given , keep current date as effective date
        if (employmentHistoryDTO.getEffectiveDate() == null) {
            employmentHistoryDTO.setEffectiveDate(LocalDate.now());
        }

        employmentHistoryDTO.setPreviousMainGrossSalary(employee.getMainGrossSalary());

        // no change in increment
        employmentHistoryDTO.setPreviousDesignationId(employee.getDesignationId());
        employmentHistoryDTO.setChangedDesignationId(employee.getDesignationId());

        //no change in increment
        employmentHistoryDTO.setPreviousDepartmentId(employee.getDepartmentId());
        employmentHistoryDTO.setChangedDepartmentId(employee.getDepartmentId());

        //no change in increment
        employmentHistoryDTO.setPreviousReportingToId(employee.getReportingToId());
        employmentHistoryDTO.setChangedReportingToId(employee.getReportingToId());

        //no change in increment
        employmentHistoryDTO.setPreviousUnitId(employee.getUnitId());
        employmentHistoryDTO.setChangedUnitId(employee.getUnitId());

        //Changes in increment
        employmentHistoryDTO.setPreviousBandId(employee.getBandId());

        // create new history
        EmploymentHistoryDTO createdEmploymentHistory = employmentHistoryService.save(employmentHistoryDTO);

        // --- prepare changes in Employee ---
        double mobileCelling = 0;
        double welfareFundDeduction = 0;

        BandDTO changedBandDTO = bandService.findOne(createdEmploymentHistory.getChangedBandId()).get();

        if (changedBandDTO.getMobileCelling() != null) {
            mobileCelling = changedBandDTO.getMobileCelling();
        }
        if (changedBandDTO.getWelfareFund() != null) {
            welfareFundDeduction = changedBandDTO.getWelfareFund();
        }

        // update employee table fields
        // a) gross b) band c) mobile ceiling d) welfare fund
        employee.setMainGrossSalary(createdEmploymentHistory.getCurrentMainGrossSalary());
        employee.setBandId(createdEmploymentHistory.getChangedBandId());
        employee.setMobileCelling((long) mobileCelling);
        employee.setWelfareFundDeduction(welfareFundDeduction);
        employeeService.update(employee);

        return createdEmploymentHistory;
    }

    @Transactional
    public EmploymentHistoryDTO updateIncrement(EmploymentHistoryDTO employmentHistoryDTO) {
        // task-1: update an increment, just update employmentHistoryDTO current fields-
        //     a) gross b) band c) designation
        // task-2: when update is complete, update Employee's current -
        //     a) gross b) band c) mobile ceiling d) welfare fund e) designation
        // nb: it should be transactional

        // load employee from specified employee in Employment History dto
        // update every data from employee to EmploymentHistoryDTO except current gross
        // update EmployeeDto object
        // save employee object
        // save employmentHistoryObject

        // validation
        if (employmentHistoryDTO.getEmployeeId() == null) {
            throw new RuntimeException("Employee ID is required!");
        }

        if (employmentHistoryDTO.getCurrentMainGrossSalary() == null) {
            throw new RuntimeException("Gross is required!");
        }

        if (employmentHistoryDTO.getChangedBandId() == null) {
            throw new RuntimeException("Band is required!");
        }

        Optional<EmploymentHistoryDTO> savedEmploymentHistoryDTO = employmentHistoryService.findOneByIdAndEvent(
            employmentHistoryDTO.getId(),
            EventType.INCREMENT
        );
        if (!savedEmploymentHistoryDTO.isPresent()) {
            throw new RuntimeException(String.format("Increment data not found of EmploymentHistory ID: %s", employmentHistoryDTO.getId()));
        }

        if (savedEmploymentHistoryDTO.get().getEmployeeId() == null) {
            throw new RuntimeException("Employee ID is null in saved data. Failed to update increment");
        }

        if (!savedEmploymentHistoryDTO.get().getEmployeeId().equals(employmentHistoryDTO.getEmployeeId())) {
            throw new RuntimeException(
                String.format(
                    "Employee ID(%s ~ %s) is mismatched!",
                    employmentHistoryDTO.getEmployeeId(),
                    savedEmploymentHistoryDTO.get().getEmployeeId()
                )
            );
        }

        Optional<EmployeeDTO> employeeOptional = employeeService.findOne(employmentHistoryDTO.getEmployeeId());
        if (!employeeOptional.isPresent()) {
            throw new EmployeeNotFoundException(employmentHistoryDTO.getEmployeeId());
        }

        // main logic
        EmployeeDTO employee = employeeOptional.get();

        if (employee.getReferenceId() != null) {
            employmentHistoryDTO.setReferenceId(employee.getReferenceId());
        }
        employmentHistoryDTO.setPin(employee.getPin());
        employmentHistoryDTO.setEventType(EventType.INCREMENT);
        employmentHistoryDTO.setIsModifiable(true);
        // if effective date not given , keep current date as effective date
        if (employmentHistoryDTO.getEffectiveDate() == null) {
            employmentHistoryDTO.setEffectiveDate(LocalDate.now());
        }

        // update history
        EmploymentHistoryDTO updatedEmploymentHistory = employmentHistoryService.save(employmentHistoryDTO);

        // in increment current main gross salary gets changed
        employee.setMainGrossSalary(employmentHistoryDTO.getCurrentMainGrossSalary());
        employee.setBandId(employmentHistoryDTO.getChangedBandId());

        double mobileCelling = 0;
        double welfareFundDeduction = 0;

        BandDTO changedBandDTO = bandService.findOne(updatedEmploymentHistory.getChangedBandId()).get();

        if (changedBandDTO.getMobileCelling() != null) {
            mobileCelling = changedBandDTO.getMobileCelling();
        }
        if (changedBandDTO.getWelfareFund() != null) {
            welfareFundDeduction = changedBandDTO.getWelfareFund();
        }

        // update employee table fields
        // a) gross b) band c) mobile ceiling d) welfare fund
        employee.setMainGrossSalary(updatedEmploymentHistory.getCurrentMainGrossSalary());
        employee.setBandId(updatedEmploymentHistory.getChangedBandId());
        employee.setMobileCelling((long) mobileCelling);
        employee.setWelfareFundDeduction(welfareFundDeduction);
        employeeService.update(employee);

        return updatedEmploymentHistory;
    }

    public EmploymentHistoryDTO deleteAndRevertIncrement(long id) {
        Optional<EmploymentHistoryDTO> savedEmploymentHistoryDTO = employmentHistoryService.findOneByIdAndEvent(id, EventType.INCREMENT);

        if (savedEmploymentHistoryDTO.isPresent()) {
            EmploymentHistoryDTO employmentHistoryDTO = savedEmploymentHistoryDTO.get();

            Optional<EmployeeDTO> employeeDTOOptional = employeeService.findOne(employmentHistoryDTO.getEmployeeId());
            if (employeeDTOOptional.isPresent()) {
                EmployeeDTO employeeDTO = employeeDTOOptional.get();

                double mobileCelling = 0;
                double welfareFundDeduction = 0;

                Optional<BandDTO> bandDTOOptional = bandService.findOne(employmentHistoryDTO.getPreviousBandId());

                if (bandDTOOptional.isPresent()) {
                    BandDTO band = bandDTOOptional.get();

                    if (band.getMobileCelling() != null) {
                        mobileCelling = band.getMobileCelling();
                    }
                    if (band.getWelfareFund() != null) {
                        welfareFundDeduction = band.getWelfareFund();
                    }
                }

                // revert employee data to past form
                // a) gross b) band c) mobile ceiling d) welfare fund
                employeeDTO.setMainGrossSalary(employmentHistoryDTO.getPreviousMainGrossSalary());
                employeeDTO.setBandId(employmentHistoryDTO.getPreviousBandId());
                employeeDTO.setMobileCelling((long) mobileCelling);
                employeeDTO.setWelfareFundDeduction(welfareFundDeduction);

                // save employee
                employeeService.update(employeeDTO);

                // delete increment entry
                employmentHistoryService.delete(id);

                return employmentHistoryDTO;
            } else {
                throw new BadRequestAlertException("Employee Must be specified", "incrementService", "mandatory field missing");
            }
        } else {
            throw new BadRequestAlertException("bad increment id", "incrementService", "mandatory field missing");
        }
    }
}
