package com.bits.hr.service.incomeTaxManagement;

import com.bits.hr.domain.AitConfig;
import com.bits.hr.domain.Employee;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.AitConfigRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.incomeTaxManagement.TaxReportGeneration.IncomeTaxReportService;
import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.IncomeTaxReportDTO;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class IncomeTaxReportExportExcelService {

    @Autowired
    private IncomeTaxReportService incomeTaxReportService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AitConfigRepository aitConfigRepository;

    public ExportXLPropertiesDTO exportIncomeTaxReport(Long aitConfigId) {
        try {
            AitConfig aitConfig = aitConfigRepository.findById(aitConfigId).get();

            String sheetName = "income_tax_report";

            List<String> titleList = new ArrayList<>();
            LocalDate today = LocalDate.now();
            titleList.add(
                "Income Tax Report For Income Year " + aitConfig.getStartDate().getYear() + "-" + aitConfig.getEndDate().getYear()
            );
            titleList.add("Report Generated On " + today.toString());

            List<String> tableHeaderList = new ArrayList<>();
            tableHeaderList.add("PIN");
            tableHeaderList.add("Name Of The Employee");
            tableHeaderList.add("Total Income");
            tableHeaderList.add("Exemption");
            tableHeaderList.add("Taxable Income");
            tableHeaderList.add("Total Tax Liability");
            tableHeaderList.add("Rebate");
            tableHeaderList.add("Net Tax Liability (Less Rebate)");
            tableHeaderList.add("Last Year Adjustment(Approved Documents From Nbr)/AIT of Vehicles Or Others");
            tableHeaderList.add("Actual Tax Paid And Duty Deducted");
            tableHeaderList.add("Refundable/Payable");

            List<List<Object>> dataList = new ArrayList<>();

            // Pseudo-code
            // 1. get all active employees who have salaries within the income year.
            // 2. get income tax report dto for the income year.
            // 3. set required data for each employee.
            // 3. Export in excel.

            List<Employee> employeeList = employeeRepository.getAllActiveEmployeeInFiscalYear(
                aitConfig.getStartDate().getYear(),
                aitConfig.getEndDate().getYear()
            );

            for (int i = 0; i < employeeList.size(); i++) {
                IncomeTaxReportDTO incomeTaxReportDto = incomeTaxReportService.generateTaxReport(employeeList.get(i), aitConfigId);

                List<Object> dataRow = new ArrayList<>();

                dataRow.add(employeeList.get(i).getPin());
                dataRow.add(employeeList.get(i).getFullName());
                dataRow.add(incomeTaxReportDto.getTotalSalaryIncome().getSalary());
                dataRow.add(incomeTaxReportDto.getTotalSalaryIncome().getExemption());
                dataRow.add(incomeTaxReportDto.getTotalSalaryIncome().getTaxableIncome());
                dataRow.add(incomeTaxReportDto.getTotalTaxLiabilities());
                dataRow.add(incomeTaxReportDto.getRebate());
                dataRow.add(incomeTaxReportDto.getNetTaxLiability());
                dataRow.add(incomeTaxReportDto.getLastYearAdjustment());
                dataRow.add(incomeTaxReportDto.getDeductedAmount());
                dataRow.add(incomeTaxReportDto.getRefundable());

                dataList.add(dataRow);
            }

            ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

            exportXLPropertiesDTO.setSheetName(sheetName);
            exportXLPropertiesDTO.setTitleList(titleList);
            exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
            exportXLPropertiesDTO.setTableDataListOfList(dataList);
            exportXLPropertiesDTO.setAutoSizeColumnUpTo(38);
            exportXLPropertiesDTO.setHasAutoSummation(false);

            return exportXLPropertiesDTO;
        } catch (Exception e) {
            log.error(e);
            throw new BadRequestAlertException("Something Went Wrong!", "IncomeTaxReportDto", "internalServerError");
        }
    }
}
