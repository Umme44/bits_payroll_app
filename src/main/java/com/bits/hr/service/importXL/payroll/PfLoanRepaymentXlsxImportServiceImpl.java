package com.bits.hr.service.importXL.payroll;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfLoanRepayment;
import com.bits.hr.domain.SalaryGeneratorMaster;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfLoanRepaymentRepository;
import com.bits.hr.repository.SalaryGeneratorMasterRepository;
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
public class PfLoanRepaymentXlsxImportServiceImpl implements XlsxImportService {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private PfLoanRepaymentRepository pfLoanRepaymentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalaryGenerationMasterService salaryGenerationMasterService;

    @Autowired
    private SalaryGeneratorMasterRepository salaryGeneratorMasterRepository;

    @Override
    public boolean importFile(MultipartFile file, int year, int month) {
        try {
            // if exist , delete previous records
            // set existing isMobileBillUpload to false.
            // update salary generation master
            SalaryGeneratorMaster salaryGeneratorMaster;
            if (salaryGenerationMasterService.isExistSalaryGeneratorMaster(year, month)) {
                pfLoanRepaymentRepository.deleteAllByDeductionMonthAndDeductionYear(year, month);
                salaryGeneratorMaster = salaryGenerationMasterService.getOrCreateSalaryGeneratorMaster(year, month);
                salaryGeneratorMaster.setIsPFLoanRepaymentImported(false); // if failed false will be false
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

                PfLoanRepayment pfLoanRepayment = new PfLoanRepayment();

                pfLoanRepayment.setAmount(Double.parseDouble(dataItems.get(1)));

                pfLoanRepayment.setDeductionMonth(month);
                pfLoanRepayment.setDeductionYear(year);
                Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(PinUtil.formatPin(dataItems.get(0)));
                //  TODO: FIX urgent
                // get or create pf account
                //  pfLoanRepayment.setEmployee(employeeOptional.orElseThrow(() -> new RuntimeException("No employee found by this id" + dataItems.get(0))));
                save(pfLoanRepayment);
            }
        } catch (Exception e) {
            log.error(e);
            return false;
        }
        // if new , create  and set true isMobileBillUploaded
        // if old , get     and set true isMobileBillUploaded

        SalaryGeneratorMaster salaryGeneratorMaster;
        salaryGeneratorMaster = salaryGenerationMasterService.getOrCreateSalaryGeneratorMaster(year, month);
        salaryGeneratorMaster.setIsPFLoanRepaymentImported(true);
        salaryGeneratorMasterRepository.save(salaryGeneratorMaster);

        return true;
    }

    public void save(PfLoanRepayment pfLoanRepayment) {
        pfLoanRepaymentRepository.save(pfLoanRepayment);
    }
}
