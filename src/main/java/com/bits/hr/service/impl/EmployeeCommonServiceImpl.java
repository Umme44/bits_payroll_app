package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EmployeeCommonService;
import com.bits.hr.service.FileService;
import com.bits.hr.service.dto.EmployeeCommonDTO;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.UserEditAccountDTO;
import com.bits.hr.service.mapper.EmployeeCommonMapper;
import com.bits.hr.service.mapper.EmployeeMapper;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class EmployeeCommonServiceImpl implements EmployeeCommonService {

    private final Logger log = LoggerFactory.getLogger(EmployeeCommonServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeCommonMapper employeeCommonMapper;
    private final EmployeeMinimalMapper employeeMinimalMapper;
    private final FileService fileService;

    public EmployeeCommonServiceImpl(
        EmployeeRepository employeeRepository,
        EmployeeMapper employeeMapper,
        EmployeeCommonMapper employeeCommonMapper,
        EmployeeMinimalMapper employeeMinimalMapper,
        FileService fileService
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.employeeCommonMapper = employeeCommonMapper;
        this.employeeMinimalMapper = employeeMinimalMapper;
        this.fileService = fileService;
    }

    @Override
    public Page<EmployeeCommonDTO> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(employeeCommonMapper::toDto);
    }

    @Override
    public List<EmployeeMinimalDTO> findAllMinimal() {
        return employeeRepository.findAll().stream().map(employeeMinimalMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeMinimalDTO> findAllMinimalEmploymentActive() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        int monthLong = currentDate.lengthOfMonth();
        LocalDate monthStartDate = LocalDate.of(currentYear, currentMonth, 1);
        LocalDate monthEndDate = LocalDate.of(currentYear, currentMonth, monthLong);
        return employeeRepository
            .getEligibleEmployeeForSalaryGeneration_v2(monthStartDate, monthEndDate)
            .stream()
            .map(employeeMinimalMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<EmployeeCommonDTO> findOne(Long id) {
        return employeeRepository.findById(id).map(employeeCommonMapper::toDto);
    }

    @Override
    public UserEditAccountDTO update(UserEditAccountDTO userEditAccountDTO) {
        try {
            Employee dbEmployee = employeeRepository
                .findById(userEditAccountDTO.getId())
                .orElseThrow(() -> new RuntimeException("Could not update employee"));

            /*required field validating*/
            /*1st check !null*/
            /*2nd check length != 0*/

            if (
                userEditAccountDTO.getMaritalStatus() != null ||
                userEditAccountDTO.getPresentAddress() != null ||
                userEditAccountDTO.getPermanentAddress() != null ||
                userEditAccountDTO.getEmergencyContactPersonName() != null
            ) {
                if (
                    userEditAccountDTO.getPermanentAddress().trim().length() == 0 ||
                    userEditAccountDTO.getPresentAddress().trim().length() == 0 ||
                    userEditAccountDTO.getEmergencyContactPersonName().trim().length() == 0
                ) {
                    throw new Exception("Required Field is missing");
                }
            } else {
                throw new Exception("Required Field is missing");
            }
            dbEmployee
                .updatedAt(LocalDateTime.now())
                .maritalStatus(userEditAccountDTO.getMaritalStatus())
                .dateOfMarriage(userEditAccountDTO.getDateOfMarriage())
                .spouseName(userEditAccountDTO.getSpouseName())
                .presentAddress(userEditAccountDTO.getPresentAddress())
                .permanentAddress(userEditAccountDTO.getPermanentAddress())
                .personalContactNo(userEditAccountDTO.getPersonalContactNo())
                .skypeId(userEditAccountDTO.getSkypeId())
                .whatsappId(userEditAccountDTO.getWhatsappId())
                .personalEmail(userEditAccountDTO.getPersonalEmail())
                .emergencyContactPersonName(userEditAccountDTO.getEmergencyContactPersonName())
                .emergencyContactPersonRelationshipWithEmployee(userEditAccountDTO.getEmergencyContactPersonRelationshipWithEmployee())
                .emergencyContactPersonContactNumber(userEditAccountDTO.getEmergencyContactPersonContactNumber());
            employeeRepository.save(dbEmployee);
            return userEditAccountDTO;
        } catch (Exception e) {
            return userEditAccountDTO;
        }
    }

    @Transactional
    @Override
    public String uploadPhoto(MultipartFile file, String pin) {
        Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(pin);
        Employee employee = employeeOptional.get();
        String prevFileName = employee.getPicture();
        fileService.delete(prevFileName);
        String fileName = fileService.save(file, pin);
        employee.setPicture(fileName);
        employee.setUpdatedAt(LocalDateTime.now());
        Employee saved = employeeRepository.save(employee);
        return saved.getPicture();
    }

    @Override
    public Path getPhoto(String pin) {
        Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(pin);
        if (!employeeOptional.isPresent()) return null;
        Employee employee = employeeOptional.get();

        String filename = employee.getPicture();
        if (filename == null) return null;

        return fileService.load(filename);
    }

    @Override
    public UserEditAccountDTO findEmployeeByPin(String pin) {
        Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(pin);
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }

        Employee employee = employeeOptional.get();

        UserEditAccountDTO userEditAccountDTO = new UserEditAccountDTO();
        userEditAccountDTO.setId(employee.getId());
        userEditAccountDTO.setPIN(employee.getPin().trim());

        if (employee.getDesignation() != null) {
            userEditAccountDTO.setDesignationName(employee.getDesignation().getDesignationName());
        }
        if (employee.getDepartment() != null) {
            userEditAccountDTO.setDepartmentName(employee.getDepartment().getDepartmentName());
        }
        if (employee.getUnit() != null) {
            userEditAccountDTO.setUnitName(employee.getUnit().getUnitName());
        }
        if (employee.getBand() != null) {
            userEditAccountDTO.setBandName(employee.getBand().getBandName());
        }
        if (employee.getBand() != null) {
            userEditAccountDTO.setBandName(employee.getBand().getBandName());
        }
        if (employee.getReportingTo() != null) {
            userEditAccountDTO.setReportingToName(employee.getReportingTo().getFullName());
        }

        userEditAccountDTO
            .fullName(employee.getFullName())
            .presentAddress(employee.getPresentAddress())
            .permanentAddress(employee.getPermanentAddress())
            .personalContactNo(employee.getPersonalContactNo())
            .whatsappId(employee.getWhatsappId())
            .personalEmail(employee.getPersonalEmail())
            .skypeId(employee.getSkypeId())
            .maritalStatus(employee.getMaritalStatus())
            .dateOfMarriage(employee.getDateOfMarriage())
            .spouseName(employee.getSpouseName())
            .emergencyContactPersonName(employee.getEmergencyContactPersonName())
            .emergencyContactPersonContactNumber(employee.getEmergencyContactPersonContactNumber())
            .emergencyContactPersonRelationshipWithEmployee(employee.getEmergencyContactPersonRelationshipWithEmployee());

        return userEditAccountDTO;
    }

    @Override
    public Set<String> getSuggestions() {
        try {
            Set<String> suggestionSet = new HashSet<>();
            Set<Employee> employeeSet = employeeRepository.getEligibleEmployeeSetForSalaryGeneration();
            employeeSet.forEach(employee -> {
                if (
                    employee.getEmployeeCategory() != null &&
                    (
                        employee.getEmployeeCategory().equals(EmployeeCategory.INTERN) ||
                        employee.getEmployeeCategory().equals(EmployeeCategory.CONTRACTUAL_EMPLOYEE)
                    )
                ) {
                    if (
                        employee.getContractPeriodExtendedTo() == null &&
                        employee.getContractPeriodEndDate() != null &&
                        employee.getContractPeriodEndDate().isBefore(LocalDate.now())
                    ) {
                        return;
                    } else if (
                        employee.getContractPeriodExtendedTo() != null && employee.getContractPeriodExtendedTo().isBefore(LocalDate.now())
                    ) {
                        return;
                    }
                }
                suggestionSet.add(employee.getFullName());
                suggestionSet.add(employee.getPin());
                suggestionSet.add(employee.getDesignation().getDesignationName());
                suggestionSet.add(employee.getDepartment().getDepartmentName());
                suggestionSet.add(employee.getDesignation().getDesignationName());
                suggestionSet.add(employee.getUnit().getUnitName());
                suggestionSet.add(employee.getOfficialEmail());
            });
            List<String> suggestionList = new ArrayList<>();
            suggestionList.remove(null);
            suggestionList.addAll(suggestionSet);
            return suggestionSet;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HashSet<>();
        }
    }
}
