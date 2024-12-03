package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.BillableAugmentedIntegrationService;
import com.bits.hr.service.importXL.GenericUploadServiceImpl;
import com.bits.hr.service.importXL.XLImportCommonService;
import com.bits.hr.util.PinUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BillableAugmentedIntegrationServiceImpl implements BillableAugmentedIntegrationService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private GenericUploadServiceImpl genericUploadService;

    @Override
    public boolean importBillableAugmentedIntegrationXl(MultipartFile file) {
        try {
            ArrayList<Employee> employeeArrayList = new ArrayList<>();
            // 1. take the data
            List<ArrayList<String>> data = genericUploadService.upload(file);

            Map<String, String> reportingTo = new HashMap<String, String>();
            // 2. remove xlsx headers
            data.remove(0);

            // 3. insert data into object

            for (ArrayList<String> dt : data) {
                /*
                A   0   pin
                C   2   isBillableResource
                D   3   isAugmentedResource
            */

                // if empty row skip
                if (!XLImportCommonService.isXLRowValid(dt)) {
                    continue;
                }

                Employee employee = new Employee();

                String pin = PinUtil.formatPin(dt.get(0));

                // if available , overwrite
                // if available and resigned / hold status then skip
                if (employeeRepository.findEmployeeByPin(pin).isPresent()) {
                    employee = employeeRepository.findEmployeeByPin(pin).get();
                    if (
                        employee.getEmploymentStatus() == EmploymentStatus.RESIGNED ||
                        employee.getEmploymentStatus() == EmploymentStatus.HOLD
                    ) {
                        continue;
                    }
                }
                employee.setPin(pin.trim());
                employee.setIsBillableResource(checkXlData(dt.get(2)));
                employee.setIsAugmentedResource(checkXlData(dt.get(3)));

                employeeArrayList.add(employee);
            }

            for (Employee e : employeeArrayList) {
                e.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private boolean checkXlData(String data) {
        try {
            if (data.toLowerCase().equals("yes")) {
                return true;
            } else if (data.toLowerCase().equals("no")) {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
