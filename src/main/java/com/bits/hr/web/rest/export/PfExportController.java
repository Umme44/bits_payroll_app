package com.bits.hr.web.rest.export;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.xlExportHandling.PfExportService;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import com.bits.hr.web.rest.util.CopyStreams;
import io.undertow.util.BadRequestException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class PfExportController {

    @Autowired
    private PfExportService pfExportService;

    @GetMapping("/api/pf-mgt/overall-pf-amount-report/get-all-years")
    public ResponseEntity<List<Integer>> getAllYearsForOverallPfAmountReport() {
        List<Integer> years = pfExportService.getAllYearsForOverallPfAmountReport();
        return ResponseEntity.ok().body(years);
    }

    // CSV Reports

    @GetMapping("/api/pf-mgt/export-monthly-pf-collection-csv")
    public void exportMonthlyPfCollectionCSV(HttpServletResponse response) throws IOException {
        try {
            Optional<File> result = pfExportService.csvExportMonthlyPfCollections();

            InputStream targetStream = new FileInputStream(result.get());

            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Monthly_PF_Collection" + ".csv");
            response.getOutputStream();
            CopyStreams.copy(targetStream, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error(e);
            throw new BadRequestAlertException("Something went wrong", "PfCollection", "internalServerError");
        }
    }

    @GetMapping("/api/pf-mgt/export-pf-collection-interest-csv")
    public void exportPfCollectionInterestCSV(HttpServletResponse response) throws IOException {
        try {
            Optional<File> result = pfExportService.csvExportPfCollectionInterests();

            InputStream targetStream = new FileInputStream(result.get());

            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "PF_Collection_Interest" + ".csv");
            response.getOutputStream();
            CopyStreams.copy(targetStream, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error(e);
            throw new BadRequestAlertException("Something went wrong", "PfCollection", "internalServerError");
        }
    }

    @GetMapping("/api/pf-mgt/export-pf-opening-balance-csv")
    public void exportPfOpeningBalanceCSV(HttpServletResponse response) throws IOException {
        try {
            Optional<File> result = pfExportService.csvExportPfOpeningBalance();

            InputStream targetStream = new FileInputStream(result.get());

            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "PF_Collection_Opening_Balance" + ".csv");
            response.getOutputStream();
            CopyStreams.copy(targetStream, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error(e);
            throw new BadRequestAlertException("Something went wrong", "PfCollection", "internalServerError");
        }
    }

    @GetMapping("/api/pf-mgt/export-detailed-pf-contribution-report-csv")
    public void exportDetailedPfContributionReport(HttpServletResponse response) throws IOException {
        try {
            Optional<File> result = pfExportService.csvExportDetailedPfContributionReport();

            InputStream targetStream = new FileInputStream(result.get());

            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "PF_Combined_report" + ".csv");
            response.getOutputStream();
            CopyStreams.copy(targetStream, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error(e);
            throw new BadRequestAlertException("Something went wrong", "PfCollection", "internalServerError");
        }
    }

    @GetMapping("/api/pf-mgt/export-detailed-pf-interest-report-csv")
    public void exportDetailedPfInterestReport(HttpServletResponse response) throws IOException {
        try {
            Optional<File> result = pfExportService.csvExportDetailedPfInterestReport();

            InputStream targetStream = new FileInputStream(result.get());

            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "PF_Combined_report" + ".csv");
            response.getOutputStream();
            CopyStreams.copy(targetStream, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error(e);
            throw new BadRequestAlertException("Something went wrong", "PfCollection", "internalServerError");
        }
    }

    // Excel Report
    @GetMapping("/api/pf-mgt/export-annual-pf-report/{year}")
    public void exportPfAnnualReport(@PathVariable int year, HttpServletResponse response) throws IOException {
        ExportXLPropertiesDTO result = pfExportService.xlExportAnnualPfReport(year);

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

    @GetMapping("/api/pf-mgt/pf-collections-between-date-range")
    public void exportPfDateRangeReport(
        @RequestParam Integer startYear,
        @RequestParam Integer startMonth,
        @RequestParam Integer endYear,
        @RequestParam Integer endMonth,
        HttpServletResponse response
    ) throws IOException {
        ExportXLPropertiesDTO result = pfExportService.exportMonthlyPfCollectionsBetweenDateRange(startYear, startMonth, endYear, endMonth);

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

    @GetMapping("/api/pf-mgt/export-pf-statement-report")
    public void exportPfStatement(HttpServletResponse response, @RequestParam(name = "selectedDate", required = true) LocalDate localDate)
        throws IOException {
        try {
            ExportXLPropertiesDTO result = pfExportService.xlExportPfStatementReport(localDate);

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
        } catch (Exception e) {
            log.error(e);
            throw new BadRequestAlertException("Something went wrong", "PfStatementReport", "internalServerError");
        }
    }
}
