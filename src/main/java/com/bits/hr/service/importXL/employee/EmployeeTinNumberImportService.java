package com.bits.hr.service.importXL.employee;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.service.importXL.XLImportCommonService;
import com.bits.hr.util.PinUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class EmployeeTinNumberImportService {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private EmployeeRepository employeeRepository;

    public boolean importFile(MultipartFile file) throws Exception {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header = data.remove(0);

            for (List<String> dataItems : data) {
                if (!XLImportCommonService.isXLRowValid(dataItems)) {
                    continue;
                }

                String employeePin = PinUtil.formatPin(dataItems.get(0).trim());
                Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(employeePin);

                // Employee Not available
                if (!employeeOptional.isPresent()) {
                    continue;
                }

                Employee employee = employeeOptional.get();
                String tinNumber = dataItems.get(1).trim();
                String taxesCircle = dataItems.get(2).trim();
                String taxesZone = dataItems.get(3).trim();
                employee.setTinNumber(tinNumber);
                employee.setTaxesCircle(taxesCircle);
                employee.setTaxesZone(taxesZone);
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);
            }
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
