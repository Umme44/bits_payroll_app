package com.bits.hr.service.xlExportHandling.genericXlsxExport.dto;

import java.util.List;
import lombok.Data;

@Data
public class ExportXLPropertiesDTO {

    private String sheetName;
    List<String> titleList;
    List<String> subTitleList;
    List<Object> tablePreHeaderList;
    List<String> tableHeaderList;
    List<List<Object>> tableDataListOfList;
    boolean hasAutoSummation;
    int autoSizeColumnUpTo;
}
