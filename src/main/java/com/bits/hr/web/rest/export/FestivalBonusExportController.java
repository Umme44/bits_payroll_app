package com.bits.hr.web.rest.export;

import com.bits.hr.domain.Festival;
import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.repository.FestivalBonusDetailsRepository;
import com.bits.hr.repository.FestivalRepository;
import com.bits.hr.service.festivalBonus.FestivalBonusExportExcelService;
import com.bits.hr.service.mapper.FestivalBonusDetailsMapper;
import com.bits.hr.service.mapper.FestivalMapper;
import com.bits.hr.service.xlExportHandling.festivalBonus.FBProRataReportDataPrepareService;
import com.bits.hr.service.xlExportHandling.festivalBonus.FBReportDataPrepareService;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class FestivalBonusExportController {

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    FestivalBonusDetailsRepository festivalBonusDetailsRepository;

    @Autowired
    FestivalMapper festivalMapper;

    @Autowired
    FBReportDataPrepareService fbReportDataPrepareService;

    @Autowired
    FBProRataReportDataPrepareService fbProRataReportDataPrepareService;

    @Autowired
    FestivalBonusDetailsMapper festivalBonusDetailsMapper;

    @Autowired
    private FestivalBonusExportExcelService festivalBonusExportExcelService;

    @GetMapping("/api/payroll-mgt/fb-gen-rce-export/{festivalId}")
    public void exportGeneralRCE(@PathVariable long festivalId, HttpServletResponse response) throws IOException {
        // prepare data and send to export util
        Festival festival = festivalRepository.findById(festivalId).get();
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.getByFestivalRCE(festivalId);

        ExportXLPropertiesDTO result = festivalBonusExportExcelService.exportFbReport(
            festival,
            festivalBonusDetailsList,
            EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE
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

    @GetMapping("/api/payroll-mgt/fb-gen-ce-export/{festivalId}")
    public void exportGeneralCE(@PathVariable long festivalId, HttpServletResponse response) throws IOException {
        // prepare data and send to export util
        Festival festival = festivalRepository.findById(festivalId).get();
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.getByFestivalCE(festivalId);

        ExportXLPropertiesDTO result = festivalBonusExportExcelService.exportFbReport(
            festival,
            festivalBonusDetailsList,
            EmployeeCategory.CONTRACTUAL_EMPLOYEE
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

    @GetMapping("/api/payroll-mgt/fb-gen-summary-export/{festivalId}")
    public void exportGeneralSummaryReport(@PathVariable long festivalId, HttpServletResponse response) throws IOException {
        // prepare data and send to export util
        ExportXLPropertiesDTO result = fbReportDataPrepareService.exportSummaryReport(festivalId);
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

    @GetMapping("/api/payroll-mgt/fb-gen-pro-rata-export/{festivalId}")
    public void exportProRataReport(@PathVariable long festivalId, HttpServletResponse response) throws IOException {
        // prepare data and send to export util
        ExportXLPropertiesDTO result = fbProRataReportDataPrepareService.exportProRataReport(festivalId);
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
