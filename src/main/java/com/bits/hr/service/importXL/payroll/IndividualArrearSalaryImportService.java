package com.bits.hr.service.importXL.payroll;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.IndividualArrearSalary;
import com.bits.hr.repository.IndividualArrearSalaryRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.dto.FileImportDetailsDTO;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.MathRoundUtil;
import com.bits.hr.util.PinUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class IndividualArrearSalaryImportService {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private IndividualArrearSalaryRepository individualArrearSalaryRepository;

    @Autowired
    private EmployeeService employeeService;

    public FileImportDetailsDTO importFile(MultipartFile file) {
        FileImportDetailsDTO fileImportDetailsDTO = new FileImportDetailsDTO();
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            /*
             *  header 1 = company name
             *  header 2 = performance appraisal of 2021
             *  header 3 = effective from Effective date
             * */
            List<String> header01 = data.remove(0);
            List<String> header02 = data.remove(0);
            List<String> header03 = data.remove(0);
            List<String> header04 = data.remove(0);
            List<String> header05 = data.remove(0);

            String title = header02.get(0);
            fileImportDetailsDTO.setTitle(title);
            LocalDate effectiveFrom = DateUtil.doubleToDate(Double.parseDouble(header03.get(1)));
            LocalDate effectiveDate = DateUtil.doubleToDate(Double.parseDouble(header04.get(1)));
            String arrearRemarks = header05.get(10);
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

                IndividualArrearSalary individualArrearSalary = new IndividualArrearSalary();

                individualArrearSalary.title(title);
                individualArrearSalary.titleEffectiveFrom(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(effectiveFrom));
                individualArrearSalary.arrearRemarks(arrearRemarks);
                individualArrearSalary.setEffectiveDate(effectiveDate);

                /*
                 *  PIN	 Name  HC   Existing Band   New Band  Unit  Department  Existing Gross  New Gross  Increment  "Arrear Salary Jan & Feb 22"  Festival Bonus  Arrear-PF Deduction  Tax Deduction  Net Pay	 PF Contribution
                 *  0     1    2       3               4       5        6              7            8           9                  10                      11                12                13          14           15
                 * */

                String pin;
                pin = PinUtil.formatPin(dataItems.get(0)).trim();
                pin = pin.trim();
                Optional<Employee> employeeOptional = employeeService.findEmployeeByPin(pin);
                if (!employeeOptional.isPresent()) {
                    continue; // skip non matching pin
                }
                Employee employee = employeeOptional.get();

                String existingBand = PinUtil.formatPin(dataItems.get(3));
                String newBand = PinUtil.formatPin(dataItems.get(4));

                double existingGross = MathRoundUtil.round(Double.parseDouble(dataItems.get(7)));
                double newGross = MathRoundUtil.round(Double.parseDouble(dataItems.get(8)));

                double increment = MathRoundUtil.round(Double.parseDouble(dataItems.get(9)));

                double arrearSalary = MathRoundUtil.round(Double.parseDouble(dataItems.get(10)));

                double festivalBonus = MathRoundUtil.round(Double.parseDouble(dataItems.get(11)));

                double pfDeduction = MathRoundUtil.round(Double.parseDouble(dataItems.get(12)));

                double taxDeduction = MathRoundUtil.round(Double.parseDouble(dataItems.get(13)));

                double netPay = MathRoundUtil.round(Double.parseDouble(dataItems.get(14)));

                double pfContribution = MathRoundUtil.round(Double.parseDouble(dataItems.get(15)));

                individualArrearSalary.setEmployee(employee);

                individualArrearSalary.setExistingBand(existingBand);
                individualArrearSalary.setNewBand(newBand);

                individualArrearSalary.setExistingGross(existingGross);
                individualArrearSalary.setNewGross(newGross);

                individualArrearSalary.setIncrement(increment);

                individualArrearSalary.setArrearSalary(arrearSalary);
                individualArrearSalary.setFestivalBonus(festivalBonus);

                individualArrearSalary.setArrearPfDeduction(pfDeduction);
                individualArrearSalary.setTaxDeduction(taxDeduction);

                individualArrearSalary.setNetPay(netPay);
                individualArrearSalary.setPfContribution(pfContribution);

                save(individualArrearSalary);
            }
        } catch (Exception e) {
            log.error(e);
            return new FileImportDetailsDTO();
        }
        fileImportDetailsDTO.setUploadSuccess(true);
        return fileImportDetailsDTO;
    }

    public void save(IndividualArrearSalary individualArrearSalary) {
        // checking duplicates
        // if same title with same employee data exists , remove that entry
        if (
            individualArrearSalaryRepository
                .getByEmployeeAndTitle(individualArrearSalary.getEmployee().getId(), individualArrearSalary.getTitle().trim())
                .size() >
            0
        ) {
            List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.getByEmployeeAndTitle(
                individualArrearSalary.getEmployee().getId(),
                individualArrearSalary.getTitle().trim()
            );
            individualArrearSalaryRepository.deleteAll(individualArrearSalaryList);
        }
        individualArrearSalaryRepository.save(individualArrearSalary);
    }
}
