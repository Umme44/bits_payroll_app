package com.bits.hr.service.importXL.payroll;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.MobileBill;
import com.bits.hr.domain.SalaryGeneratorMaster;
import com.bits.hr.repository.MobileBillRepository;
import com.bits.hr.repository.SalaryGeneratorMasterRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.service.importXL.XlsxImportService;
import com.bits.hr.util.PinUtil;
import com.bits.hr.util.SalaryGenerationMasterService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class MobileBillImportServiceImpl implements XlsxImportService {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private MobileBillRepository mobileBillRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SalaryGeneratorMasterRepository salaryGeneratorMasterRepository;

    @Autowired
    private SalaryGenerationMasterService salaryGenerationMasterService;

    @Override
    public boolean importFile(MultipartFile file, int year, int month) {
        try {
            // if exist , delete previous records
            // set existing isMobileBillUpload to false.
            // update salary generation master
            SalaryGeneratorMaster salaryGeneratorMaster;
            if (salaryGenerationMasterService.isExistSalaryGeneratorMaster(year, month)) {
                mobileBillRepository.deleteAllByYearAndMonth(year, month);
                salaryGeneratorMaster = salaryGenerationMasterService.getOrCreateSalaryGeneratorMaster(year, month);
                salaryGeneratorMaster.setIsMobileBillImported(false);
                salaryGeneratorMasterRepository.save(salaryGeneratorMaster);
            }

            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header = data.remove(0);
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

                MobileBill mobileBill = new MobileBill();
                mobileBill.setAmount(Double.parseDouble(dataItems.get(2)));
                mobileBill.setMonth(month);
                mobileBill.setYear(year);
                // todo fix , mobile number should be saved in mobile bill , bcz some employee have 2/3 sim
                //  String mobileNumber =  dataItems.get(1);

                String pin;
                pin = PinUtil.formatPin(dataItems.get(0)).trim();
                pin = pin.trim();
                Optional<Employee> employeeOptional = employeeService.findEmployeeByPin(pin);
                if (!employeeOptional.isPresent()) {
                    continue; // skip non matching pin
                }
                mobileBill.setEmployee(employeeOptional.get());
                save(mobileBill);
            }
        } catch (Exception e) {
            log.error(e);
            return false;
        }

        // if new , create  and set true isMobileBillUploaded
        // if old , get     and set true isMobileBillUploaded

        SalaryGeneratorMaster salaryGeneratorMaster;
        salaryGeneratorMaster = salaryGenerationMasterService.getOrCreateSalaryGeneratorMaster(year, month);
        salaryGeneratorMaster.setIsMobileBillImported(true);
        salaryGeneratorMasterRepository.save(salaryGeneratorMaster);

        return true;
    }

    public void save(MobileBill mobileBill) {
        mobileBillRepository.save(mobileBill);
    }
}
