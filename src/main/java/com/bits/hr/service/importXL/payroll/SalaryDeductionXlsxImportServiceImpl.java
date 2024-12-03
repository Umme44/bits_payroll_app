package com.bits.hr.service.importXL.payroll;

import com.bits.hr.domain.DeductionType;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.SalaryDeduction;
import com.bits.hr.domain.SalaryGeneratorMaster;
import com.bits.hr.repository.DeductionTypeRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.SalaryDeductionRepository;
import com.bits.hr.repository.SalaryGeneratorMasterRepository;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.service.importXL.XlsxImportService;
import com.bits.hr.util.PinUtil;
import com.bits.hr.util.SalaryGenerationMasterService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SalaryDeductionXlsxImportServiceImpl implements XlsxImportService {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private SalaryDeductionRepository salaryDeductionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalaryGenerationMasterService salaryGenerationMasterService;

    @Autowired
    private SalaryGeneratorMasterRepository salaryGeneratorMasterRepository;

    @Autowired
    private DeductionTypeRepository deductionTypeRepository;

    @Override
    public boolean importFile(MultipartFile file, int year, int month) {
        try {
            // if exist , delete previous records
            // set existing isMobileBillUpload to false.
            // update salary generation master
            SalaryGeneratorMaster salaryGeneratorMaster;
            if (salaryGenerationMasterService.isExistSalaryGeneratorMaster(year, month)) {
                salaryDeductionRepository.deleteAllByMonthAndYear(year, month);
                salaryGeneratorMaster = salaryGenerationMasterService.getOrCreateSalaryGeneratorMaster(year, month);
                salaryGeneratorMaster.setIsSalaryDeductionImported(false);
                salaryGeneratorMasterRepository.save(salaryGeneratorMaster);
            }

            // delete previous records
            salaryDeductionRepository.deleteAllByMonthAndYear(year, month);

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

                SalaryDeduction salaryDeduction = new SalaryDeduction();

                //  0   PIN
                //  1   Name
                //  2   deduction type
                //  3   amount
                Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(PinUtil.formatPin(dataItems.get(0)));
                if (!employeeOptional.isPresent()) {
                    continue;
                }
                salaryDeduction.setEmployee(employeeOptional.get());
                salaryDeduction.setDeductionMonth(month);
                salaryDeduction.setDeductionYear(year);
                salaryDeduction.setDeductionAmount(Double.parseDouble(dataItems.get(3)));

                DeductionType deductionType = getOrCreateDeductionType(dataItems.get(2));
                salaryDeduction.setDeductionType(deductionType);
                save(salaryDeduction);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        SalaryGeneratorMaster salaryGeneratorMaster;
        salaryGeneratorMaster = salaryGenerationMasterService.getOrCreateSalaryGeneratorMaster(year, month);
        salaryGeneratorMaster.setIsSalaryDeductionImported(true);
        salaryGeneratorMasterRepository.save(salaryGeneratorMaster);

        return true;
    }

    public void save(SalaryDeduction salaryDeduction) {
        salaryDeductionRepository.save(salaryDeduction);
    }

    DeductionType getOrCreateDeductionType(String deductionName) {
        Optional<DeductionType> deductionTypeOptional = deductionTypeRepository.findDeductionTypeByName(deductionName);
        if (deductionTypeOptional.isPresent()) {
            return deductionTypeOptional.get();
        } else {
            DeductionType deductionType = new DeductionType();
            deductionType.setName(deductionName.trim());
            return deductionTypeRepository.save(deductionType);
        }
    }
}
