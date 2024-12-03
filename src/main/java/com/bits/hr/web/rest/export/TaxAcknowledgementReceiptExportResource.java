package com.bits.hr.web.rest.export;

import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import com.bits.hr.service.xlExportHandling.taxAcknowledgementReceipt.TaxAcknowledgementExportService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common")
public class TaxAcknowledgementReceiptExportResource {

    @Autowired
    private TaxAcknowledgementExportService taxAcknowledgementExportService;

    @GetMapping("/export-tax-acknowledgement-year-wise/{aitConfigId}")
    public void exportTaxAcknowledgementReceiptYearWise(HttpServletResponse response, @PathVariable long aitConfigId) throws IOException {
        ExportXLPropertiesDTO result = taxAcknowledgementExportService.exportTaxAcknowledgementReceiptYearWise(aitConfigId);

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
