package com.bits.hr.service.importXL.employee.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.importXL.GenericUploadServiceImpl;
import com.bits.hr.service.importXL.XLImportCommonService;
import com.bits.hr.service.importXL.employee.helperMethods.entityHelper.GetOrCreateDepartment;
import com.bits.hr.service.importXL.employee.helperMethods.entityHelper.GetOrCreateDesignation;
import com.bits.hr.service.importXL.employee.helperMethods.entityHelper.GetOrCreateUnit;
import com.bits.hr.util.PinUtil;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class EmployeeDDUBatchUpdateServiceImpl {

    private static final String SERVICE_NAME = "Employee Batch Update";

    @Autowired
    private GenericUploadServiceImpl genericUploadService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private GetOrCreateDesignation getOrCreateDesignation;

    @Autowired
    private GetOrCreateDepartment getOrCreateDepartment;

    @Autowired
    private GetOrCreateUnit getOrCreateUnit;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    public boolean batchUpdate(MultipartFile file) {
        try {
            ArrayList<Employee> employeeArrayList = new ArrayList<>();
            List<ArrayList<String>> data = genericUploadService.upload(file);
            Map<String, String> reportingTo = new HashMap<String, String>();
            data.remove(0);

            for (ArrayList<String> dt : data) {
                // if empty row skip
                if (!XLImportCommonService.isXLRowValid(dt)) {
                    continue;
                }

                // pin  fullName    designation     department      unit    pin
                // 0    1           2               3               4       5

                String pin = PinUtil.formatPin(dt.get(0));

                Employee employee = new Employee();

                if (employeeRepository.findEmployeeByPin(pin).isPresent()) {
                    employee = employeeRepository.findEmployeeByPin(pin).get();
                } else {
                    continue;
                }

                if (!dt.get(2).contains("no_change")) {
                    employee.setDesignation(getOrCreateDesignation.get(dt.get(2)));
                }

                if (!dt.get(3).contains("no_change")) {
                    employee.setDepartment(getOrCreateDepartment.get(dt.get(3)));
                }

                if (!dt.get(4).contains("no_change")) {
                    employee.setUnit(getOrCreateUnit.get(dt.get(4)));
                }

                if (!dt.get(5).contains("no_change")) {
                    Optional<Employee> lineManager = employeeRepository.findEmployeeByPin(PinUtil.formatPin(dt.get(5)));
                    if (lineManager.isPresent()) {
                        employee.setReportingTo(lineManager.get());
                    }
                }
                employeeArrayList.add(employee);
            }

            User user = currentEmployeeService.getCurrentUser().get();
            for (Employee e : employeeArrayList) {
                e.setCreatedAt(Instant.now());
                e.setCreatedBy(user.getEmail());
                e.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(e);
                eventLoggingPublisher.publishEvent(user, e, RequestMethod.PUT, SERVICE_NAME);
            }
            return true;
        } catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }
}
