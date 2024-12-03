package com.bits.hr.service.employmentHistory;

import com.bits.hr.domain.enumeration.EventType;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.EmploymentHistoryService;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.dto.EmploymentHistoryDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferHistoryService {

    @Autowired
    private EmploymentHistoryService employmentHistoryService;

    @Autowired
    private EmployeeService employeeService;

    public EmploymentHistoryDTO createTransfer(EmploymentHistoryDTO dto) {
        // load employee from specified employee in Employment History dto
        // update every data from employee to EmploymentHistoryDTO except current gross , changed band , changed Designation
        // update EmployeeDto object
        // save employee object
        // save employmentHistoryObject

        Optional<EmployeeDTO> employeeDTOOptional = employeeService.findOne(dto.getEmployeeId());
        if (!employeeDTOOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Must be specified", "Transfer", "mandatory field missing");
        }

        if (employeeDTOOptional.get().getReferenceId() != null) {
            dto.setReferenceId(employeeDTOOptional.get().getReferenceId());
        }

        dto.setPin(employeeDTOOptional.get().getPin());
        dto.setEventType(EventType.TRANSFER);
        dto.setIsModifiable(true);
        // if effective date not given , keep current date as effective date
        if (dto.getEffectiveDate() == null) {
            dto.setEffectiveDate(LocalDate.now());
        }

        dto.setPreviousMainGrossSalary(employeeDTOOptional.get().getMainGrossSalary());
        dto.setCurrentMainGrossSalary(employeeDTOOptional.get().getMainGrossSalary());
        // Can change in transfer
        dto.setPreviousDesignationId(employeeDTOOptional.get().getDesignationId());
        //will change in transfer
        dto.setPreviousDepartmentId(employeeDTOOptional.get().getDepartmentId());
        //will change in transfer
        dto.setPreviousReportingToId(employeeDTOOptional.get().getReportingToId());
        //will change in transfer
        dto.setPreviousUnitId(employeeDTOOptional.get().getUnitId());
        //no change in transfer
        dto.setPreviousBandId(employeeDTOOptional.get().getBandId());
        dto.setChangedBandId(employeeDTOOptional.get().getBandId());

        // in transfer => reporting to unit department gets changed
        employeeDTOOptional.get().setReportingToId(dto.getChangedReportingToId());
        employeeDTOOptional.get().setUnitId(dto.getChangedUnitId());
        employeeDTOOptional.get().setDepartmentId(dto.getChangedDepartmentId());
        employeeDTOOptional.get().setDesignationId(dto.getChangedDesignationId());

        employeeService.update(employeeDTOOptional.get());
        EmploymentHistoryDTO result = employmentHistoryService.save(dto);
        return result;
    }

    public EmploymentHistoryDTO updateTransfer(EmploymentHistoryDTO dto) {
        // load employee from specified employee in Employment History dto
        // update every data from employee to EmploymentHistoryDTO except current gross , changed band , changed Designation
        // update EmployeeDto object
        // save employee object
        // save employmentHistoryObject

        Optional<EmployeeDTO> employeeDTOOptional = employeeService.findOne(dto.getEmployeeId());
        if (!employeeDTOOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Must be specified", "Transfer", "mandatory field missing");
        }

        if (employeeDTOOptional.get().getReferenceId() != null) {
            dto.setReferenceId(employeeDTOOptional.get().getReferenceId());
        }

        dto.setPin(employeeDTOOptional.get().getPin());
        dto.setEventType(EventType.TRANSFER);
        dto.setIsModifiable(true);

        // if effective date not given , keep current date as effective date
        if (dto.getEffectiveDate() == null) {
            dto.setEffectiveDate(LocalDate.now());
        }

        dto.setPreviousMainGrossSalary(employeeDTOOptional.get().getMainGrossSalary());
        dto.setCurrentMainGrossSalary(employeeDTOOptional.get().getMainGrossSalary());
        // can change in transfer
        dto.setPreviousDesignationId(employeeDTOOptional.get().getDesignationId());
        //will change in transfer
        dto.setPreviousDepartmentId(employeeDTOOptional.get().getDepartmentId());
        //will change in transfer
        dto.setPreviousReportingToId(employeeDTOOptional.get().getReportingToId());
        //will change in transfer
        dto.setPreviousUnitId(employeeDTOOptional.get().getUnitId());
        //no change in transfer
        dto.setPreviousBandId(employeeDTOOptional.get().getBandId());
        dto.setChangedBandId(employeeDTOOptional.get().getBandId());

        // in transfer => reporting to unit department gets changed
        employeeDTOOptional.get().setReportingToId(dto.getChangedReportingToId());
        employeeDTOOptional.get().setUnitId(dto.getChangedUnitId());
        employeeDTOOptional.get().setDepartmentId(dto.getChangedDepartmentId());
        employeeDTOOptional.get().setDesignationId(dto.getChangedDesignationId());

        employeeService.update(employeeDTOOptional.get());

        EmploymentHistoryDTO result = employmentHistoryService.save(dto);
        return result;
    }

    //    public Page<EmploymentHistoryDTO> getAllTransfers(EventType eventType, Pageable pageable) {
    //        return employmentHistoryService.findAllByEventType(eventType, pageable);
    //    }
    //
    //    public Optional<EmploymentHistoryDTO> getEmploymentHistory(Long id) {
    //        Optional<EmploymentHistoryDTO> employmentHistoryDTO = employmentHistoryService.findOne(id);
    //        if(!employmentHistoryDTO.isPresent()){
    //            throw new BadRequestAlertException("Employee Transfer Not Found", "Transfer", "notFound");
    //        }
    //        return employmentHistoryDTO;
    //    }

    public List<EmploymentHistoryDTO> getAllTransferHistoriesByEmployee(Long id) {
        Optional<EmployeeDTO> employeeDTOOptional = employeeService.findOne(id);
        if (!employeeDTOOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Must be specified", "Transfer", "mandatory field missing");
        }

        return employmentHistoryService
            .getEmploymentHistoryByEmployeePin(employeeDTOOptional.get().getPin(), EventType.TRANSFER)
            .stream()
            .collect(Collectors.toList());
    }

    public List<EmploymentHistoryDTO> getAllTransferHistoriesByEmployeeBetweenTwoDates(Long id, LocalDate startDate, LocalDate endDate) {
        Optional<EmployeeDTO> employeeDTOOptional = employeeService.findOne(id);
        if (!employeeDTOOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Must be specified", "Transfer", "mandatory field missing");
        }

        return employmentHistoryService
            .getEmploymentHistoryByEmployeePinBetweenTwoDates(employeeDTOOptional.get().getPin(), startDate, endDate, EventType.TRANSFER)
            .stream()
            .collect(Collectors.toList());
    }

    public List<EmploymentHistoryDTO> getAllTransferHistoriesBetweenTwoDates(LocalDate startDate, LocalDate endDate) {
        return employmentHistoryService
            .getEmploymentHistoryBetweenTwoDates(startDate, endDate, EventType.TRANSFER)
            .stream()
            .collect(Collectors.toList());
    }

    //    public boolean upload(MultipartFile file) throws Exception {
    //        return batchOperationsViaXlsx.batchOperations(file);
    //    }

    public void deleteTransfer(Long id) {
        Optional<EmploymentHistoryDTO> dto = employmentHistoryService.findOneByIdAndEvent(id, EventType.TRANSFER);

        if (dto.isPresent() && dto.get().isIsModifiable()) {
            Optional<EmploymentHistoryDTO> employmentHistoryDTOOptional = employmentHistoryService.findOne(id); // refactor

            Optional<EmployeeDTO> employeeDTOOptional = employeeService.findOne(employmentHistoryDTOOptional.get().getEmployeeId());

            if (employeeDTOOptional.isPresent()) {
                // revert employee data to past form
                employeeDTOOptional.get().setReportingToId(employmentHistoryDTOOptional.get().getPreviousReportingToId());
                employeeDTOOptional.get().setUnitId(employmentHistoryDTOOptional.get().getPreviousUnitId());
                employeeDTOOptional.get().setDepartmentId(employmentHistoryDTOOptional.get().getPreviousDepartmentId());
                employeeDTOOptional.get().setDesignationId(employmentHistoryDTOOptional.get().getChangedDesignationId());

                // save employee
                employeeService.update(employeeDTOOptional.get());

                // delete increment entry
                employmentHistoryService.delete(id);
            } else {
                throw new BadRequestAlertException("Employee Must be specified", "Transfer", "mandatory field missing");
            }
        } else {
            throw new BadRequestAlertException("bad increment id", "Transfer", "mandatory field missing");
        }
    }
}
