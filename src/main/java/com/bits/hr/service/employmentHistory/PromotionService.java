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
public class PromotionService {

    private final EmployeeService employeeService;
    private final BandService bandService;

    private final EmploymentHistoryService employmentHistoryService;

    public PromotionService(EmployeeService employeeService, BandService bandService, EmploymentHistoryService employmentHistoryService) {
        this.employeeService = employeeService;
        this.bandService = bandService;
        this.employmentHistoryService = employmentHistoryService;
    }

    @Transactional
    public EmploymentHistoryDTO createPromotion(EmploymentHistoryDTO employmentHistoryDTO) {
        // task-1: before a new promotion, get Employee following fields data to save into employmentHistoryDTO fields-
        //     a) gross b) designation c) department d) reporting to e) unit f) band
        // task-2: when a new promotion is initiated, update Employee's current -
        //     a) gross b) band c) mobile ceiling d) welfare fund e) designation
        // nb: it should be transactional

        // planning::
        // load employee from specified employee in Employment History dto
        // update every data from employee to EmploymentHistoryDTO except current gross , changed band , changed Designation
        // save employmentHistoryObject
        // update EmployeeDto object
        // save employee object

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

        if (employmentHistoryDTO.getChangedDesignationId() == null) {
            throw new RuntimeException("Designation is required!");
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
        employmentHistoryDTO.setEventType(EventType.PROMOTION);
        employmentHistoryDTO.setIsModifiable(true);

        // if effective date not given , keep current date as effective date
        if (employmentHistoryDTO.getEffectiveDate() == null) {
            employmentHistoryDTO.setEffectiveDate(LocalDate.now());
        }

        employmentHistoryDTO.setPreviousMainGrossSalary(employee.getMainGrossSalary());

        employmentHistoryDTO.setPreviousDesignationId(employee.getDesignationId());

        //no change in promotion
        employmentHistoryDTO.setPreviousDepartmentId(employee.getDepartmentId());
        employmentHistoryDTO.setChangedDepartmentId(employee.getDepartmentId());

        //no change in promotion
        employmentHistoryDTO.setPreviousReportingToId(employee.getReportingToId());
        employmentHistoryDTO.setChangedReportingToId(employee.getReportingToId());

        //no change in promotion
        employmentHistoryDTO.setPreviousUnitId(employee.getUnitId());
        employmentHistoryDTO.setChangedUnitId(employee.getUnitId());

        employmentHistoryDTO.setPreviousBandId(employee.getBandId());

        // save promotion
        EmploymentHistoryDTO createdEmploymentHistory = employmentHistoryService.save(employmentHistoryDTO);

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
        // a) gross b) band c) mobile ceiling d) welfare fund e) designation
        employee.setMainGrossSalary(createdEmploymentHistory.getCurrentMainGrossSalary());
        employee.setBandId(createdEmploymentHistory.getChangedBandId());
        employee.setMobileCelling((long) mobileCelling);
        employee.setWelfareFundDeduction(welfareFundDeduction);
        employee.setDesignationId(createdEmploymentHistory.getChangedDesignationId());
        employeeService.update(employee);

        return createdEmploymentHistory;
    }

    @Transactional
    public EmploymentHistoryDTO updatePromotion(EmploymentHistoryDTO employmentHistoryDTO) {
        // task-1: update a promotion, just update employmentHistoryDTO current fields-
        //     a) gross b) band c) designation
        // task-2: when update is complete, update Employee's current -
        //     a) gross b) band c) mobile ceiling d) welfare fund e) designation
        // nb: it should be transactional

        // load employee from specified employee in Employment History dto
        // update every data from employee to EmploymentHistoryDTO except current gross , changed band , changed Designation
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

        if (employmentHistoryDTO.getChangedDesignationId() == null) {
            throw new RuntimeException("Designation is required!");
        }
        Optional<EmploymentHistoryDTO> savedEmploymentHistoryDTO = employmentHistoryService.findOneByIdAndEvent(
            employmentHistoryDTO.getId(),
            EventType.PROMOTION
        );
        if (!savedEmploymentHistoryDTO.isPresent()) {
            throw new RuntimeException(String.format("Promotion data not found of EmploymentHistory ID: %s", employmentHistoryDTO.getId()));
        }

        if (savedEmploymentHistoryDTO.get().getEmployeeId() == null) {
            throw new RuntimeException("Employee ID is null in saved data. Failed to update promotion");
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

        // if effective date not given , keep current date as effective date
        if (employmentHistoryDTO.getEffectiveDate() == null) {
            employmentHistoryDTO.setEffectiveDate(LocalDate.now());
        }

        employmentHistoryDTO.setPin(employee.getPin());
        employmentHistoryDTO.setIsModifiable(true);

        // save promotion
        EmploymentHistoryDTO savedEmploymentHistory = employmentHistoryService.save(employmentHistoryDTO);

        double mobileCelling = 0;
        double welfareFundDeduction = 0;

        BandDTO changedBandDTO = bandService.findOne(savedEmploymentHistory.getChangedBandId()).get();

        if (changedBandDTO.getMobileCelling() != null) {
            mobileCelling = changedBandDTO.getMobileCelling();
        }
        if (changedBandDTO.getWelfareFund() != null) {
            welfareFundDeduction = changedBandDTO.getWelfareFund();
        }

        // update employee table fields
        // a) gross b) band c) mobile ceiling d) welfare fund e) designation
        employee.setMainGrossSalary(savedEmploymentHistory.getCurrentMainGrossSalary());
        employee.setBandId(savedEmploymentHistory.getChangedBandId());
        employee.setMobileCelling((long) mobileCelling);
        employee.setWelfareFundDeduction(welfareFundDeduction);
        employee.setDesignationId(savedEmploymentHistory.getChangedDesignationId());
        employeeService.update(employee);

        return employmentHistoryService.save(employmentHistoryDTO);
    }

    @Transactional
    public EmploymentHistoryDTO deletePromotion(Long id) {
        Optional<EmploymentHistoryDTO> savedEmploymentHistoryDTO = employmentHistoryService.findOneByIdAndEvent(id, EventType.PROMOTION);
        if (savedEmploymentHistoryDTO.isPresent() && savedEmploymentHistoryDTO.get().isIsModifiable()) {
            EmploymentHistoryDTO employmentHistoryDTO = savedEmploymentHistoryDTO.get();

            Optional<EmployeeDTO> employeeDTOOptional = employeeService.findOne(employmentHistoryDTO.getEmployeeId());
            if (employeeDTOOptional.isPresent()) {
                EmployeeDTO employeeDTO = employeeDTOOptional.get();

                // revert employee data to past form
                employeeDTO.setMainGrossSalary(employmentHistoryDTO.getPreviousMainGrossSalary());
                employeeDTO.setBandId(employmentHistoryDTO.getPreviousBandId());
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
                employeeDTO.setMobileCelling((long) mobileCelling);
                employeeDTO.setWelfareFundDeduction(welfareFundDeduction);
                employeeDTO.setDesignationId(employmentHistoryDTO.getPreviousDesignationId());

                // save employee
                employeeService.update(employeeDTO);

                // delete increment entry
                employmentHistoryService.delete(id);
                return employmentHistoryDTO;
            } else {
                throw new EmployeeNotFoundException(employmentHistoryDTO.getEmployeeId());
            }
        } else {
            throw new BadRequestAlertException("bad increment id", "employmentHistory", "mandatory field missing");
        }
    }
}
