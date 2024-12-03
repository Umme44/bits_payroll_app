package com.bits.hr.web.rest.export;

import com.bits.hr.service.InsuranceDataExportInExcelService;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/employee-mgt")
public class InsuranceDataExportExcelController {

    @Autowired
    private InsuranceDataExportInExcelService insuranceDataExportInExcelService;

    @GetMapping("/export-insurance-registration/inclusion-list")
    public void exportInsuranceRegistrationInclusionList(
        @RequestParam String searchText,
        @RequestParam Integer year,
        @RequestParam Integer month,
        HttpServletResponse response
    ) throws IOException {
        log.debug("Request to exportInsuranceRegistrationInclusionList");
        ExportXLPropertiesDTO result = insuranceDataExportInExcelService.exportInclusionList(searchText, year, month);

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

    @GetMapping("/export-insurance-registration/approved-list")
    public void exportInsuranceRegistrationApprovedList(
        @RequestParam String searchText,
        @RequestParam Integer year,
        @RequestParam Integer month,
        HttpServletResponse response
    ) throws IOException {
        log.debug("Request to export approved insurance registration list");
        ExportXLPropertiesDTO result = insuranceDataExportInExcelService.exportApprovedList(searchText, year, month);

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

    @GetMapping("/export-insurance-registration/excluded-list")
    public void exportInsuranceRegistrationExcludedList(
        @RequestParam String searchText,
        @RequestParam Integer year,
        @RequestParam Integer month,
        @RequestParam Boolean isCancelled,
        @RequestParam Boolean isSeperated,
        HttpServletResponse response
    ) throws IOException {
        log.debug("Request to export excluded insurance registration list");
        ExportXLPropertiesDTO result = insuranceDataExportInExcelService.exportExcludedList(
            searchText,
            year,
            month,
            isCancelled,
            isSeperated
        );

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

    @GetMapping("/export-all-approved-insurance-registrations")
    public void exportAllApprovedInsuranceRegistrations(HttpServletResponse response) throws IOException {
        log.debug("Request to export all approved insurance registrations");
        ExportXLPropertiesDTO result = insuranceDataExportInExcelService.exportAllApprovedInsuranceRegistrations();

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

    @GetMapping("/export-insurance-registrations")
    public void exportAllInsuranceRegistrations(HttpServletResponse response) throws IOException {
        log.debug("Request to export all insurance registrations");
        ExportXLPropertiesDTO result = insuranceDataExportInExcelService.exportInsuranceRegistrations();

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
