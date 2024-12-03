package com.bits.hr.web.rest.importXL;

import com.bits.hr.service.importXL.employee.EmployeePinImportService;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employee-mgt")
public class EmployeePinImportController {

    @Autowired
    private EmployeePinImportService employeePinImportService;

    @PostMapping("/employee-pin/import-employee-pin-xlsx")
    public ResponseEntity<Boolean> importEmployeePins(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = employeePinImportService.importFile(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/employee-pin/import-employee-reference-pin-xlsx")
    public ResponseEntity<Boolean> importEmployeeReferencePins(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = employeePinImportService.importReferencePin(file);
        return ResponseEntity.ok(hasDone);
    }

    @GetMapping("/employee-pin/export-employee-pin-xlsx")
    public void exportEmployeePins(HttpServletResponse response) throws IOException {
        ExportXLPropertiesDTO result = employeePinImportService.exportEmployeePin();
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

    @GetMapping("/employee-pin/export-employee-reference-pin-xlsx")
    public void exportEmployeeReferencePins(HttpServletResponse response) throws IOException {
        ExportXLPropertiesDTO result = employeePinImportService.exportEmployeeReferencePin();
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
