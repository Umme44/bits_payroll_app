package com.bits.hr.service.importXL.employee;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.service.importXL.XLImportCommonService;
import com.bits.hr.util.PinUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
public class LfaImportService {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

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

                String employeePin = PinUtil.formatPin(dataItems.get(1).trim());
                Optional<Employee> employee = employeeRepository.findEmployeeByPin(employeePin);

                // Employee Not available
                if (!employee.isPresent()) {
                    continue;
                }

                int year = (int) Math.round(Double.parseDouble(dataItems.get(3).trim()));
                Month month = getMonth(dataItems.get(4));

                List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findByEmployeeIdAndYearAndMonth(
                    employee.get().getId(),
                    year,
                    month
                );

                // Employee salary not found
                if (employeeSalaryList.size() == 0) {
                    continue;
                }

                double lfaAmount = Math.round(Double.parseDouble(dataItems.get(5).trim()));

                EmployeeSalary employeeSalary = employeeSalaryList.get(0);

                employeeSalary.setAllowance01(lfaAmount);

                employeeSalaryRepository.save(employeeSalary);
            }
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Month getMonth(String month) {
        int monthNumber = (int) Math.round(Double.parseDouble(month.trim()));
        return Month.fromInteger(monthNumber);
    }
}
