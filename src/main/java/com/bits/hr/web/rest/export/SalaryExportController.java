package com.bits.hr.web.rest.export;

import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.service.AllowanceNameService;
import com.bits.hr.service.EmployeeSalaryService;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.dto.SalarySummary;
import com.bits.hr.service.mapper.EmployeeSalaryMapper;
import com.bits.hr.service.report.ShortSummaryReportBuilderService;
import com.bits.hr.service.xlExportHandling.CorpnetXlsxExportService;
import com.bits.hr.service.xlExportHandling.EmployeeSalaryXlsxExportService;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import com.bits.hr.web.rest.util.CopyStreams;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class SalaryExportController {

    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    private EmployeeSalaryMapper employeeSalaryMapper;

    @Autowired
    private ShortSummaryReportBuilderService shortSummaryReportBuilderService;

    @Autowired
    private AllowanceNameService allowanceNameService;

    @Autowired
    private CorpnetXlsxExportService corpnetXlsxExportService;

    @GetMapping("/api/payroll-mgt/export/employee-salary/{year}/{month}/all")
    public void ExportToExcel(
        @PathVariable(name = "year") Integer year,
        @PathVariable(name = "month") Integer month,
        HttpServletResponse response
    ) throws IOException {
        List<EmployeeSalary> employeeSalaries = employeeSalaryRepository.findEmployeeSalaryByYearAndMonth(year, Month.fromInteger(month));
        List<EmployeeSalaryDTO> listUsers = employeeSalaryMapper.toDto(employeeSalaries);
        EmployeeSalaryXlsxExportService excelExporter = new EmployeeSalaryXlsxExportService(
            listUsers,
            allowanceNameService.getAllowanceName()
        );
        excelExporter.export(response);
    }

    @GetMapping("/api/payroll-mgt/export/employee-salary/{year}/{month}/regular")
    public void regularExportToExcel(
        @PathVariable(name = "year") Integer year,
        @PathVariable(name = "month") Integer month,
        HttpServletResponse response
    ) throws IOException {
        List<EmployeeSalary> employeeSalaries = employeeSalaryRepository.getRegularSalary(year, Month.fromInteger(month));
        List<EmployeeSalaryDTO> listUsers = employeeSalaryMapper.toDto(employeeSalaries);
        EmployeeSalaryXlsxExportService excelExporter = new EmployeeSalaryXlsxExportService(
            listUsers,
            allowanceNameService.getAllowanceName()
        );
        excelExporter.export(response);
    }

    @GetMapping("/api/payroll-mgt/export/employee-salary/{year}/{month}/contractual")
    public void contractualExportToExcel(
        @PathVariable(name = "year") Integer year,
        @PathVariable(name = "month") Integer month,
        HttpServletResponse response
    ) throws IOException {
        List<EmployeeSalary> employeeSalaries = employeeSalaryRepository.getContractualSalary(year, Month.fromInteger(month));
        List<EmployeeSalaryDTO> listUsers = employeeSalaryMapper.toDto(employeeSalaries);
        EmployeeSalaryXlsxExportService excelExporter = new EmployeeSalaryXlsxExportService(
            listUsers,
            allowanceNameService.getAllowanceName()
        );
        excelExporter.export(response);
    }

    @GetMapping("/api/payroll-mgt/export/employee-salary/{year}/{month}/intern")
    public void internExportToExcel(
        @PathVariable(name = "year") Integer year,
        @PathVariable(name = "month") Integer month,
        HttpServletResponse response
    ) throws IOException {
        List<EmployeeSalary> employeeSalaries = employeeSalaryRepository.getInternSalary(year, Month.fromInteger(month));
        List<EmployeeSalaryDTO> listUsers = employeeSalaryMapper.toDto(employeeSalaries);
        EmployeeSalaryXlsxExportService excelExporter = new EmployeeSalaryXlsxExportService(
            listUsers,
            allowanceNameService.getAllowanceName()
        );
        excelExporter.export(response);
    }

    // Generic XLSX Salary Export Service
    @GetMapping("/api/payroll-mgt/export/employee-salary/{year}/{month}/summary")
    public void exportSalarySummaryCSV(HttpServletResponse response, @PathVariable String month, @PathVariable String year)
        throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue =
            "attachment; filename=Salary_summary_" + Month.fromInteger((int) Double.parseDouble(month)) + "_" + year + "_" + ".csv";
        response.setHeader(headerKey, headerValue);
        List<Object[]> objectList = employeeSalaryRepository.getSalarySummary(
            Month.fromInteger(Integer.parseInt(month)).toString(),
            (int) Double.parseDouble(year)
        );
        List<SalarySummary> salarySummaryList = objectList.stream().map(SalarySummary::new).collect(Collectors.toList());
        try {
            String stringWriter = "";
            SalarySummary salarySummaryHeader = new SalarySummary();
            stringWriter += salarySummaryHeader.getcommaSeperatedHeader();
            for (SalarySummary salarySummary : salarySummaryList) {
                stringWriter += salarySummary.getAsCommaSeperatedValue();
            }
            response.getOutputStream();
            InputStream targetStream = new ByteArrayInputStream(stringWriter.getBytes(StandardCharsets.UTF_8));
            CopyStreams.copy(targetStream, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            log.error(e);
        }
    }

    // Generic XLSX Salary Export Service
    @GetMapping("/api/payroll-mgt/export/employee-salary/{year}/{month}/short-summary")
    public void exportShortSummaryCSV(HttpServletResponse response, @PathVariable String month, @PathVariable String year)
        throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue =
            "attachment; filename=Salary_short_summary_" + Month.fromInteger((int) Double.parseDouble(month)) + "_" + year + "_" + ".csv";
        response.setHeader(headerKey, headerValue);
        try {
            String stringWriter = shortSummaryReportBuilderService.getReport(Integer.parseInt(year), (int) Double.parseDouble(month));
            response.getOutputStream();
            InputStream targetStream = new ByteArrayInputStream(stringWriter.getBytes(StandardCharsets.UTF_8));
            CopyStreams.copy(targetStream, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            log.error(e);
        }
    }

    @GetMapping("/api/payroll-mgt/export/employee-salary/{year}/{month}/salary-disbursement")
    public void salaryDisbursementExportToExcel(
        @PathVariable(name = "year") Integer year,
        @PathVariable(name = "month") Integer month,
        HttpServletResponse response
    ) throws IOException {
        ExportXLPropertiesDTO result = corpnetXlsxExportService.exportSalaryDisbursement(year, month);

        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getSubTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );
        exportXL.export(response);
    }

    @GetMapping("/api/payroll-mgt/export/employee-salary/{year}/{month}/living-allowance")
    public void livingAllowanceExportToExcel(
        @PathVariable(name = "year") Integer year,
        @PathVariable(name = "month") Integer month,
        HttpServletResponse response
    ) throws IOException {
        ExportXLPropertiesDTO result = corpnetXlsxExportService.exportLivingAllowance(year, month);

        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getSubTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );
        exportXL.export(response);
    }

    @GetMapping("/api/payroll-mgt/export/employee-salary/{year}/{month}/car-allowance")
    public void carAllowanceExportToExcel(
        @PathVariable(name = "year") Integer year,
        @PathVariable(name = "month") Integer month,
        HttpServletResponse response
    ) throws IOException {
        ExportXLPropertiesDTO result = corpnetXlsxExportService.exportCarAllowance(year, month);

        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getSubTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );
        exportXL.export(response);
    }

    @GetMapping("/api/payroll-mgt/export/employee-salary/{year}/{month}/company-secretary-allowance")
    public void companySecretaryAllowanceExportToExcel(
        @PathVariable(name = "year") Integer year,
        @PathVariable(name = "month") Integer month,
        HttpServletResponse response
    ) throws IOException {
        ExportXLPropertiesDTO result = corpnetXlsxExportService.exportCSAllowance(year, month);

        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getSubTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );
        exportXL.export(response);
    }

    @GetMapping("/api/payroll-mgt/export/employee-salary/{year}/{month}/house-rent-allowance")
    public void houseRentAllowanceExportToExcel(
        @PathVariable(name = "year") Integer year,
        @PathVariable(name = "month") Integer month,
        HttpServletResponse response
    ) throws IOException {
        ExportXLPropertiesDTO result = corpnetXlsxExportService.exportHouseRentAllowance(year, month);

        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getSubTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );
        exportXL.export(response);
    }

    @GetMapping("/api/payroll-mgt/export/employee-salary/yearly-tax-return-submission")
    public void yearlyTaxReturnSubmissionExportToExcel(
        @RequestParam Integer startYear,
        @RequestParam Integer startMonth,
        @RequestParam Integer endYear,
        @RequestParam Integer endMonth,
        HttpServletResponse response
    ) throws IOException {
        ExportXLPropertiesDTO result = corpnetXlsxExportService.exportYearlyTaxReturnSubmission(startYear, startMonth, endYear, endMonth);

        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getSubTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );
        exportXL.export(response);
    }

    @GetMapping("/api/employee-mgt/employee-salary-between-date-range")
    public void salaryBetweenDateRangeExportToExcel(
        @RequestParam Integer startYear,
        @RequestParam Integer startMonth,
        @RequestParam Integer endYear,
        @RequestParam Integer endMonth,
        HttpServletResponse response
    ) throws IOException {
        ExportXLPropertiesDTO result = employeeSalaryService.prepareDataForSalaryExport(startYear, startMonth, endYear, endMonth);

        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getSubTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );
        exportXL.export(response);
    }
}
