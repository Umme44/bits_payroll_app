package com.bits.hr.service.importXL.batchOperations.employmentActions;

import static com.bits.hr.service.importXL.batchOperations.employmentActions.RowConstantsTransfers.*;

import com.bits.hr.domain.Department;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmploymentHistory;
import com.bits.hr.domain.Unit;
import com.bits.hr.domain.enumeration.EventType;
import com.bits.hr.repository.DepartmentRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmploymentHistoryRepository;
import com.bits.hr.repository.UnitRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.EmploymentHistoryService;
import com.bits.hr.service.dto.EmploymentHistoryDTO;
import com.bits.hr.service.employmentHistory.TransferHistoryService;
import com.bits.hr.service.mapper.EmployeeMapper;
import com.bits.hr.service.mapper.EmploymentHistoryMapper;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.PinUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Qualifier("Transfer")
public class BatchTransfersViaXlsxImpl implements BatchOperationsViaXlsx {

    @Autowired
    GenericXlsxImportService genericXlsxImportService;

    @Autowired
    EmploymentHistoryRepository employmentHistoryRepository;

    @Autowired
    EmploymentHistoryService employmentHistoryService;

    @Autowired
    private TransferHistoryService transferHistoryService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EmploymentHistoryMapper employmentHistoryMapper;

    @Override
    public boolean batchOperations(MultipartFile file) throws Exception {
        try {
            List<ArrayList<String>> data = genericXlsxImportService.importXlsx(file);
            List<String> header = data.remove(0);

            // file validation
            if (header.get(0).trim().compareToIgnoreCase(FIRST_ROW_FIRST_COL.trim()) != 0) {
                return false;
            }

            for (List<String> dataItems : data) {
                // empty row handling
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                // taking necessary data in local variable
                LocalDate effectiveDate = DateUtil.doubleToDate(Double.parseDouble(dataItems.get(TRANSFER_EFFECTIVE_DATE)));

                String employeePin = PinUtil.formatPin(dataItems.get(EMPLOYEE_PIN));
                String reportingToPin = PinUtil.formatPin(dataItems.get(NEW_REPORTING_TO_PIN));
                String newUnitName = dataItems.get(NEW_UNIT_NAME);
                String newDepartmentName = dataItems.get(NEW_DEPARTMENT_NAME);

                // first delete duplicate data to rollback old employee data
                // removing duplicates
                Employee employee;
                if (employeeService.findEmployeeByPin(employeePin).isPresent()) {
                    employee = employeeService.findEmployeeByPin(employeePin).get();
                } else {
                    continue; // no employee found with same pin
                }
                List<EmploymentHistory> duplicateEmploymentHistoryList = employmentHistoryRepository.findDuplicates(
                    employee,
                    effectiveDate,
                    EventType.TRANSFER
                );
                for (EmploymentHistory employmentHistory1 : duplicateEmploymentHistoryList) {
                    transferHistoryService.deleteTransfer(employmentHistory1.getId());
                }

                EmploymentHistoryDTO employmentHistoryDTO = new EmploymentHistoryDTO();

                // load employee from specified employee in Employment History dto
                // update every data from employee to EmploymentHistoryDTO except current gross , changed band , changed Designation
                // update EmployeeDto object
                // save employee object
                // save employmentHistoryObject

                Optional<Employee> employeeOpitonal = employeeRepository.findEmployeeByPin(employeePin);
                if (employeeOpitonal.isPresent()) {
                    employee = employeeOpitonal.get();
                } else {
                    continue; // no employee found with same pin
                }

                Employee reportingTO;
                Optional<Employee> reportingToOptional = employeeRepository.findEmployeeByPin(reportingToPin);
                if (reportingToOptional.isPresent()) {
                    reportingTO = reportingToOptional.get();
                } else {
                    continue; // no reporting to found
                }

                Unit unit;
                Optional<Unit> unitOptional = unitRepository.findUnitByUnitNameIgnoreCase(newUnitName);
                if (unitOptional.isPresent()) {
                    unit = unitOptional.get();
                } else {
                    continue; // no unit found
                }

                Department department;
                Optional<Department> departmentOptional = departmentRepository.findDepartmentByDepartmentNameIgnoreCase(newDepartmentName);
                if (departmentOptional.isPresent()) {
                    department = departmentOptional.get();
                } else {
                    continue; // no department found
                }

                // until now if all data oK , put it into employment History DTO
                employmentHistoryDTO.setEmployeeId(employee.getId());
                employmentHistoryDTO.setEffectiveDate(effectiveDate);

                employmentHistoryDTO.setChangedReportingToId(reportingTO.getId());

                employmentHistoryDTO.setChangedUnitId(unit.getId());
                employmentHistoryDTO.setChangedDepartmentId(department.getId());

                employmentHistoryDTO.setReferenceId(employee.getReferenceId());
                employmentHistoryDTO.setPin(employee.getPin());
                employmentHistoryDTO.setEventType(EventType.TRANSFER);
                employmentHistoryDTO.setIsModifiable(true);
                // if effective date not given , keep current date as effective date
                if (employmentHistoryDTO.getEffectiveDate() == null) {
                    employmentHistoryDTO.setEffectiveDate(LocalDate.now());
                }

                employmentHistoryDTO.setPreviousDesignationId(employee.getDesignation().getId());
                //employmentHistoryDTO.setChangedDesignationId(employee.getDesignationId());

                employmentHistoryDTO.setPreviousDepartmentId(employee.getDepartment().getId());
                // employmentHistoryDTO.setChangedDepartmentId(employee.getDepartment().getId());

                //no change transfer
                if (employee.getReportingTo() != null) {
                    employmentHistoryDTO.setPreviousReportingToId(employee.getReportingTo().getId());
                    employmentHistoryDTO.setChangedReportingToId(employee.getReportingTo().getId());
                }

                //no change transfer
                employmentHistoryDTO.setPreviousUnitId(employee.getUnit().getId());
                // employmentHistoryDTO.setChangedUnitId(employee.getUnit().getId());

                // transfers reporting to salary, unit and Department gets changed

                employee.setMainGrossSalary(employmentHistoryDTO.getCurrentMainGrossSalary());
                employee.setReportingTo(reportingTO);
                employee.setUnit(unit);
                employee.setDepartment(department);
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);
                //EmploymentHistoryDTO result = employmentHistoryService.save(employmentHistoryDTO);
                save(employmentHistoryDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void save(EmploymentHistoryDTO employmentHistory) {
        employmentHistoryService.save(employmentHistory);
    }
}
