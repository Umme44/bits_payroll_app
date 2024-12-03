package com.bits.hr.web.rest.export;

import com.bits.hr.service.incomeTaxManagement.IncomeTaxReportExportExcelService;
import com.bits.hr.service.incomeTaxManagement.TaxReportGeneration.IncomeTaxReportService;
import com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO.IncomeTaxDropDownMenuDto;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payroll-mgt")
public class IncomeTaxReportExcelExportResource {

    @Autowired
    private IncomeTaxReportExportExcelService incomeTaxReportExportExcelService;

    @Autowired
    private IncomeTaxReportService incomeTaxReportService;

    @GetMapping("/get-income-tax-year-list")
    public List<IncomeTaxDropDownMenuDto> getDropDownMenuYears() {
        return incomeTaxReportService.getAitConfigYears();
    }

    @GetMapping("/get-income-tax-report/{id}")
    public void exportIncomeTaxReport(@PathVariable Long id, HttpServletResponse response) throws IOException {
        ExportXLPropertiesDTO result = incomeTaxReportExportExcelService.exportIncomeTaxReport(id);

        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );
        exportXL.export(response);
    }
}
