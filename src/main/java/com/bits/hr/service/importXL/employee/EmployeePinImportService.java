package com.bits.hr.service.importXL.employee;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmployeePinStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.*;
import com.bits.hr.service.EmployeePinConfigurationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import com.bits.hr.util.PinUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class EmployeePinImportService {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private EmployeePinRepository employeePinRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private EmployeePinConfigurationService employeePinConfigurationService;

    @Autowired
    private EmployeePinConfigurationRepository employeePinConfigurationRepository;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;

    public ExportXLPropertiesDTO exportEmployeePin() {
        List<Employee> employeeList = employeeRepository.findAll();

        String sheetName = "Employee Pins";

        List<String> titleList = new ArrayList<>();
        List<String> subTitleList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        subTitleList.add("Employee Pins");
        subTitleList.add("Report generated on :" + today.toString());

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("PIN");
        tableHeaderList.add("Name");
        tableHeaderList.add("Employee Category");
        tableHeaderList.add("Department");
        tableHeaderList.add("Designation");
        tableHeaderList.add("unit");

        List<List<Object>> dataList = new ArrayList<>();
        for (Employee employee : employeeList) {
            List<Object> data = new ArrayList<>();

            data.add(employee.getPin());
            data.add(employee.getFullName());
            data.add(employee.getEmployeeCategory().name());

            data.add(employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : "-");
            data.add(employee.getDesignation() != null ? employee.getDesignation().getDesignationName() : "-");
            data.add(employee.getUnit() != null ? employee.getUnit().getUnitName() : "-");

            dataList.add(data);
        }

        ExportXLPropertiesDTO dto = new ExportXLPropertiesDTO();
        dto.setSheetName(sheetName);
        dto.setTitleList(titleList);
        dto.setSubTitleList(subTitleList);
        dto.setTableHeaderList(tableHeaderList);
        dto.setTableDataListOfList(dataList);
        dto.setHasAutoSummation(false);
        dto.setAutoSizeColumnUpTo(38);

        return dto;
    }

    public boolean importReferencePin(MultipartFile file) throws Exception {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header = data.remove(0);

            List<EmployeePin> employeePinList = new ArrayList<>();

            for (List<String> dataItems : data) {
                if (dataItems.isEmpty()) {
                    continue;
                }
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                if (dataItems.get(0).equals("")) {
                    continue;
                }

                // ref    pin    name    category    department    designation   unit
                //  0      1      2         3             4             5         6

                Optional<Employee> employeeOptional = employeeRepository.findByPin(dataItems.get(1));

                if (!employeeOptional.isPresent()) {
                    continue;
                } else {
                    employeeOptional.get().setReferenceId(dataItems.get(0).trim());
                    employeeRepository.save(employeeOptional.get());
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    public ExportXLPropertiesDTO exportEmployeeReferencePin() {
        List<Employee> employeeList = employeeRepository.getAllReferencePin();

        String sheetName = "Employee Reference Pins";

        List<String> titleList = new ArrayList<>();
        List<String> subTitleList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        subTitleList.add("Employee Pins");
        subTitleList.add("Report generated on :" + today.toString());

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("Ref.");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Name");
        tableHeaderList.add("Employee Category");
        tableHeaderList.add("Department");
        tableHeaderList.add("Designation");
        tableHeaderList.add("unit");

        List<List<Object>> dataList = new ArrayList<>();
        for (Employee employee : employeeList) {
            List<Object> data = new ArrayList<>();

            data.add(employee.getReferenceId());
            data.add(employee.getPin());
            data.add(employee.getFullName());
            data.add(employee.getEmployeeCategory().name());

            data.add(employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : "-");
            data.add(employee.getDesignation() != null ? employee.getDesignation().getDesignationName() : "-");
            data.add(employee.getUnit() != null ? employee.getUnit().getUnitName() : "-");

            dataList.add(data);
        }

        ExportXLPropertiesDTO dto = new ExportXLPropertiesDTO();
        dto.setSheetName(sheetName);
        dto.setTitleList(titleList);
        dto.setSubTitleList(subTitleList);
        dto.setTableHeaderList(tableHeaderList);
        dto.setTableDataListOfList(dataList);
        dto.setHasAutoSummation(false);
        dto.setAutoSizeColumnUpTo(38);

        return dto;
    }

    public boolean importFile(MultipartFile file) throws Exception {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header = data.remove(0);

            List<EmployeePin> employeePinList = new ArrayList<>();

            for (List<String> dataItems : data) {
                if (dataItems.isEmpty()) {
                    continue;
                }
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                if (dataItems.get(0).equals("")) {
                    continue;
                }

                // pin    Name    EmployeeCategory    department    designation    unit   overwriteIfExist
                //  0      1             2                3              4           5           6

                EmployeePin employeePin = createNewEmployeePinFromExcelData(dataItems);
                saveEmployeePin(employeePin, dataItems.get(6).trim());
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    void saveEmployeePin(EmployeePin employeePin, String overwriteIfExist) {
        Optional<EmployeePin> existingEmployeePinOptional = employeePinRepository.findByPin(employeePin.getPin());
        List<EmployeePinConfiguration> configurations = employeePinConfigurationRepository.findByEmployeeCategory(
            employeePin.getEmployeeCategory()
        );

        if (configurations.size() == 0) {
            throw new BadRequestAlertException("PIN Configurations is missing", "Employee PIN", "");
        }

        if (existingEmployeePinOptional.isPresent() && overwriteIfExist.equalsIgnoreCase("true")) {
            employeePin.setId(existingEmployeePinOptional.get().getId());
        } else if (existingEmployeePinOptional.isPresent() && !overwriteIfExist.equalsIgnoreCase("true")) {
            return;
        }
        employeePinRepository.save(employeePin);

        Long sequenceStart = Long.parseLong(configurations.get(0).getSequenceStart().trim());
        Long sequenceEnd = Long.parseLong(configurations.get(0).getSequenceEnd().trim());

        if (Long.parseLong(employeePin.getPin()) >= sequenceStart && Long.parseLong(employeePin.getPin()) <= sequenceEnd) {
            employeePinConfigurationService.updatePinConfigurationInformation(configurations.get(0));
        }
    }

    //    EmployeePin createNewEmployeePin(Employee employee){
    //        EmployeePin employeePin = new EmployeePin();
    //        employeePin.setPin(employee.getPin());
    //        employeePin.setFullName(employee.getFullName());
    //        employeePin.setEmployeeCategory(employee.getEmployeeCategory());
    //        employeePin.setEmployeePinStatus(getEmployeePinStatus(employee));
    //        employeePin.setDepartment(employee.getDepartment());
    //        employeePin.setDesignation(employee.getDesignation());
    //        employeePin.setUnit(employee.getUnit());
    //
    //        LocalDate createdAt = employee.getDateOfJoining();
    //        employeePin.setCreatedAt(createdAt.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    //
    //        employeePin.setCreatedBy(currentEmployeeService.getCurrentUser().get());
    //
    //        return employeePin;
    //    }

    EmployeePin createNewEmployeePinFromExcelData(List<String> dataItems) {
        // pin    Name    EmployeeCategory    department    designation    unit   overwriteIfExist
        //  0      1             2                3              4           5           6

        EmployeePin employeePin = new EmployeePin();

        employeePin.setPin(PinUtil.formatPin(dataItems.get(0).trim()));
        employeePin.setFullName(dataItems.get(1).trim());
        employeePin.setEmployeeCategory(getEmployeeCategory(dataItems.get(2).trim()));
        employeePin.setEmployeePinStatus(getEmployeePinStatus(dataItems.get(2).trim(), dataItems.get(0)));
        employeePin.setDepartment(getDepartment(dataItems.get(3).trim()));
        employeePin.setDesignation(getDesignationName(dataItems.get(4).trim()));
        employeePin.setUnit(getUnit(dataItems.get(5).trim()));
        employeePin.setCreatedAt(Instant.now());
        employeePin.setCreatedBy(currentEmployeeService.getCurrentUser().get());

        return employeePin;
    }

    EmployeePinStatus getEmployeePinStatus(String employeeCategory, String pin) {
        Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(pin.trim());
        if (employeeOptional.isPresent()) {
            List<EmployeeResignation> employeeResignation = employeeResignationRepository.findResignedEmployeeByPin(
                employeeOptional.get().getPin(),
                LocalDate.now()
            );

            if (employeeResignation.size() > 0) {
                if (
                    employeeCategory.equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) ||
                    employeeCategory.equals(EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE)
                ) {
                    return EmployeePinStatus.RESIGNED;
                } else {
                    return EmployeePinStatus.CONTRACT_END;
                }
            } else {
                return EmployeePinStatus.JOINED;
            }
        } else {
            if (
                employeeCategory.equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) ||
                employeeCategory.equals(EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE)
            ) {
                return EmployeePinStatus.RESIGNED;
            } else {
                return EmployeePinStatus.CONTRACT_END;
            }
        }
    }

    EmployeePinStatus getEmployeePinStatusForPreviousEmployee(String employeeCategory) {
        EmployeeCategory category = getEmployeeCategory(employeeCategory);
        if (category.equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
            return EmployeePinStatus.RESIGNED;
        } else {
            return EmployeePinStatus.CONTRACT_END;
        }
    }

    EmployeeCategory getEmployeeCategory(String employeeCategory) {
        switch (employeeCategory) {
            case "REGULAR_CONFIRMED_EMPLOYEE":
            case "REGULAR_PROVISIONAL_EMPLOYEE":
            case "Regular Confirmed Employee":
            case "Confirmed":
            case "confirmed":
            case "Confirm":
                return EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE;
            case "CONTRACTUAL_EMPLOYEE":
            case "Contractual Employee":
            case "Contractual":
            case "contractual":
            case "by Contract":
            case "by contract":
                return EmployeeCategory.CONTRACTUAL_EMPLOYEE;
            default:
                return EmployeeCategory.INTERN;
        }
    }

    Department getDepartment(String departmentName) {
        try {
            Optional<Department> departmentOptional = departmentRepository.findDepartmentByDepartmentNameIgnoreCase(departmentName);

            if (departmentOptional.isPresent()) {
                return departmentOptional.get();
            } else {
                Department department = new Department();
                department.setDepartmentName(departmentName);
                return departmentRepository.save(department);
            }
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException();
        }
    }

    Designation getDesignationName(String designationName) {
        try {
            Optional<Designation> designationOptional = designationRepository.findDesignationByDesignationName(designationName);

            if (designationOptional.isPresent()) {
                return designationOptional.get();
            } else {
                Designation designation = new Designation();
                designation.setDesignationName(designationName);
                return designationRepository.save(designation);
            }
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException();
        }
    }

    Unit getUnit(String unitName) {
        try {
            Optional<Unit> unitOptional = unitRepository.findUnitByUnitNameIgnoreCase(unitName);

            if (unitOptional.isPresent()) {
                return unitOptional.get();
            } else {
                Unit unit = new Unit();
                unit.setUnitName(unitName);
                return unitRepository.save(unit);
            }
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException();
        }
    }
}
