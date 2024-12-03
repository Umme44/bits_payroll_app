package com.bits.hr.service.xlExportHandling.genericXlsxExport;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.xlExportHandling.ReportConstants;
import com.bits.hr.util.MathRoundUtil;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Log4j2
public class ExportXL {

    private final XSSFWorkbook workbook;

    String sheetName = "";
    List<String> titleList;
    List<String> subTitleList;
    List<Object> tablPreHeaderList;
    List<String> tableHeaderList;
    List<List<Object>> tableDataListOfList;
    boolean hasSummation;

    private XSSFSheet sheet;

    private int rowCount = 0;

    private int autoSizeColumnUpTo = 0;

    public ExportXL(
        String sheetName,
        List<String> tableHeaderList,
        List<List<Object>> tableDataListOfList,
        boolean hasAutoSummation,
        int autoSizeColumnUpTo
    ) {
        this.sheetName = sheetName;
        this.tableHeaderList = tableHeaderList;
        this.tableDataListOfList = tableDataListOfList;
        this.hasSummation = hasAutoSummation;
        this.autoSizeColumnUpTo = autoSizeColumnUpTo;
        workbook = new XSSFWorkbook();
    }

    public ExportXL(
        String sheetName,
        List<String> titleList,
        List<String> subTitleList,
        List<String> tableHeaderList,
        List<List<Object>> tableDataListOfList,
        boolean hasAutoSummation,
        int autoSizeColumnUpTo
    ) {
        this.sheetName = sheetName;
        this.titleList = titleList;
        this.subTitleList = subTitleList;
        this.tableHeaderList = tableHeaderList;
        this.tableDataListOfList = tableDataListOfList;
        this.hasSummation = hasAutoSummation;
        this.autoSizeColumnUpTo = autoSizeColumnUpTo;
        workbook = new XSSFWorkbook();
    }

    public ExportXL(
        String sheetName,
        List<String> titleList,
        List<String> subTitleList,
        List<Object> tablPreHeaderList,
        List<String> tableHeaderList,
        List<List<Object>> tableDataListOfList,
        boolean hasAutoSummation,
        int autoSizeColumnUpTo
    ) {
        this.sheetName = sheetName;
        this.titleList = titleList;
        this.subTitleList = subTitleList;
        this.tablPreHeaderList = tablPreHeaderList;
        this.tableHeaderList = tableHeaderList;
        this.tableDataListOfList = tableDataListOfList;
        this.hasSummation = hasAutoSummation;
        this.autoSizeColumnUpTo = autoSizeColumnUpTo;
        workbook = new XSSFWorkbook();
    }

    public ExportXL(
        String sheetName,
        List<String> titleList,
        List<String> tableHeaderList,
        List<List<Object>> tableDataListOfList,
        boolean hasAutoSummation,
        int autoSizeColumnUpTo
    ) {
        this.sheetName = sheetName;
        this.titleList = titleList;
        this.tableHeaderList = tableHeaderList;
        this.tableDataListOfList = tableDataListOfList;
        this.hasSummation = hasAutoSummation;
        this.autoSizeColumnUpTo = autoSizeColumnUpTo;
        workbook = new XSSFWorkbook();
    }

    private void writeTitles() {
        Row row = sheet.createRow(rowCount);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(13);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        style.setWrapText(true);
        int columnCount = 0;
        sheet.addMergedRegion(CellRangeAddress.valueOf("A1:D1"));
        createCellWithStyle(row, columnCount, ReportConstants.COMPANY_NAME, style);
        ++rowCount;

        for (String title : titleList) {
            Row titleRow = sheet.createRow(rowCount);
            sheet.addMergedRegion(CellRangeAddress.valueOf("A" + (rowCount + 1) + ":D" + (rowCount + 1)));
            createCellWithStyle(titleRow, columnCount, title, style);
            ++rowCount;
        }
        ++rowCount;
    }

    private void writeSubTitles() {
        Row row = sheet.createRow(rowCount);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(10);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        style.setWrapText(true);
        int columnCount = 0;

        for (String subTitle : subTitleList) {
            Row subTitleRow = sheet.createRow(rowCount);
            sheet.addMergedRegion(CellRangeAddress.valueOf("A" + (rowCount + 1) + ":D" + (rowCount + 1)));
            createCellWithStyle(subTitleRow, columnCount, subTitle, style);
            ++rowCount;
        }
        ++rowCount;
    }

    private void writeTablePreHeader() {
        if (rowCount == 0) {
            rowCount++;
        }

        Row row = sheet.createRow(rowCount);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(10);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);

        int columnCount = 0;

        LocalDate previousDate = null;
        boolean margeCell = false;
        //        CellRangeAddress existingMergedRegion = null;

        int startColumn = 0;
        int endColumn = 0;

        for (Object header : tablPreHeaderList) {
            if (header instanceof LocalDate == false) {
                createCellWithStyle(row, columnCount++, header.toString(), style);
                continue;
            }

            if (header instanceof LocalDate) {
                LocalDate currentDate = (LocalDate) header;

                if (previousDate == null) {
                    startColumn = columnCount;
                    endColumn = columnCount;
                }

                if (previousDate != null && previousDate.equals(currentDate)) {
                    margeCell = true;
                    endColumn++;
                } else {
                    margeCell = false;

                    if (startColumn != endColumn) {
                        CellRangeAddress mergedRegion = new CellRangeAddress(rowCount, rowCount, startColumn - 1, endColumn - 1);
                        sheet.addMergedRegion(mergedRegion);
                        createCellWithStyle(row, startColumn - 1, previousDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")), style);
                    } else if (previousDate != null) {
                        createCellWithStyle(row, startColumn - 1, previousDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")), style);
                    }

                    // Reset start and end columns
                    startColumn = endColumn + 1;
                    endColumn = startColumn;
                }
                previousDate = currentDate;
            }
            columnCount++;
        }

        // Merge cells if the last range is consecutive same dates
        if (startColumn != endColumn) {
            CellRangeAddress mergedRegion = new CellRangeAddress(rowCount, rowCount, startColumn - 1, endColumn - 1);
            sheet.addMergedRegion(mergedRegion);
            createCellWithStyle(row, startColumn - 1, previousDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")), style);
        }

        ++rowCount;
    }

    private void writeTableHeader() {
        Row row = sheet.createRow(rowCount);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(10);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        style.setWrapText(true);
        int columnCount = 0;

        for (String header : tableHeaderList) {
            createCellWithStyle(row, columnCount++, header, style);
        }
        ++rowCount;
    }

    private void writeTableData() {
        XSSFFont font = workbook.createFont();
        font.setFontHeight(8);
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd mmm, yyyy"));

        for (List<Object> rowData : tableDataListOfList) {
            XSSFRow row = sheet.createRow(rowCount);
            int columnCount = 0;
            for (Object obj : rowData) {
                createCell(row, columnCount++, obj);
            }
            ++rowCount;
        }
    }

    private void writeSummationData() {
        Row row = sheet.createRow(rowCount);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(10);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        style.setWrapText(true);
        int columnCount = 0;

        List<Integer> summationIndex = new ArrayList<>();

        // determining whose summation to be calculated
        List<Object> firstRow = tableDataListOfList.get(0);

        int index = -1;
        for (Object obj : firstRow) {
            ++index;
            if (obj instanceof Integer) {
                summationIndex.add(index);
                continue;
            } else if (obj instanceof Double) {
                summationIndex.add(index);
                continue;
            } else if (obj instanceof Long) {
                summationIndex.add(index);
                continue;
            } else if (obj instanceof Float) {
                summationIndex.add(index);
                continue;
            } else {
                continue;
            }
        }

        Hashtable<Integer, Double> totalSet = new Hashtable<>();
        for (int i : summationIndex) {
            totalSet.put(i, 0d);
        }

        // full data set
        for (List<Object> objectList : tableDataListOfList) {
            // column
            for (int i : summationIndex) {
                double value;
                try {
                    value = asDouble(objectList.get(i));
                } catch (Exception ex) {
                    value = 0;
                }
                double hashSetValue = totalSet.getOrDefault(i, 0d);
                totalSet.put(i, value + hashSetValue);
            }
        }

        int numberOfColumn = tableDataListOfList.get(0).size();
        for (int i = 0; i < numberOfColumn; i++) {
            if (totalSet.containsKey(i)) {
                double value = MathRoundUtil.round(totalSet.getOrDefault(i, 0d));
                createCellWithStyle(row, columnCount++, value, style);
            } else {
                createCellWithStyle(row, columnCount++, " ", style);
            }
        }
        ++rowCount;
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        if (validationCheck()) {
            sheet = workbook.createSheet(sheetName);
            if (titleList != null && titleList.size() > 0) {
                writeTitles();
            }
            if (subTitleList != null && subTitleList.size() > 0) {
                writeSubTitles();
            }

            if (tablPreHeaderList != null && tablPreHeaderList.size() > 0) {
                writeTablePreHeader();
            }

            writeTableHeader();
            writeTableData();
            if (hasSummation) {
                writeSummationData();
            } else {
                for (int i = 0; i < autoSizeColumnUpTo; i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } else {
            throw new BadRequestAlertException("No data found to export in excel.", "ExportXL", "noDataFoundToExportInExcel");
        }
    }

    // helper methods
    private void createCell(Row row, int columnCount, Object value) {
        Cell cell = row.createCell(columnCount);
        try {
            // sheet.autoSizeColumn(columnCount);
            if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            } else if (value instanceof LocalDate) {
                CellStyle dateStyle = workbook.createCellStyle();
                CreationHelper createHelper = workbook.getCreationHelper();
                dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd mmm, yyyy"));

                LocalDate localDate = (LocalDate) value;
                Date date = Date.from( // Convert from modern java.time class to troublesome old legacy class.  DO NOT DO THIS unless you must, to interoperate with old code not yet updated for java.time.
                    localDate // `LocalDate` class represents a date-only, without time-of-day and without time zone nor offset-from-UTC.
                        .atStartOfDay( // Let java.time determine the first moment of the day on that date in that zone. Never assume the day starts at 00:00:00.
                            ZoneId.of("Asia/Dhaka") // Specify time zone using proper name in `continent/region` format, never 3-4 letter pseudo-zones such as “PST”, “CST”, “IST”.
                        ) // Produce a `ZonedDateTime` object.
                        .toInstant() // Extract an `Instant` object, a moment always in UTC.
                );
                cell.setCellValue(date);
                cell.setCellStyle(dateStyle);
            } else if (value instanceof Double) {
                cell.setCellValue((Double) value);
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            } else if (value instanceof Instant) {
                CellStyle dateTimeStyle = workbook.createCellStyle();
                CreationHelper createHelper = workbook.getCreationHelper();
                short dateTimeFormat = createHelper.createDataFormat().getFormat("m/d/yy h:mm:ss");
                dateTimeStyle.setDataFormat(dateTimeFormat);
                cell.setCellValue(Date.from((Instant) value));
                cell.setCellStyle(dateTimeStyle);
            }
        } catch (Exception ex) {
            log.error(ex);
            cell.setCellValue(" ");
        }
        // cell.setCellStyle(style);
    }

    private void createCellWithStyle(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        try {
            sheet.autoSizeColumn(columnCount);
            if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            } else if (value instanceof LocalDate) {
                LocalDate localDate = (LocalDate) value;
                Date date = Date.from( // Convert from modern java.time class to troublesome old legacy class.  DO NOT DO THIS unless you must, to inter operate with old code not yet updated for java.time.
                    localDate // `LocalDate` class represents a date-only, without time-of-day and without time zone nor offset-from-UTC.
                        .atStartOfDay( // Let java.time determine the first moment of the day on that date in that zone. Never assume the day starts at 00:00:00.
                            ZoneId.of("Asia/Dhaka") // Specify time zone using proper name in `continent/region` format, never 3-4 letter pseudo-zones such as “PST”, “CST”, “IST”.
                        ) // Produce a `ZonedDateTime` object.
                        .toInstant() // Extract an `Instant` object, a moment always in UTC.
                );
                cell.setCellValue(date);
                cell.setCellStyle(style);
            } else if (value instanceof Double) {
                cell.setCellValue((Double) value);
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            }
            cell.setCellStyle(style);
        } catch (Exception ex) {
            log.error(ex);
            cell.setCellValue(" ");
        }
    }

    Double asDouble(Object o) {
        try {
            Double val = null;
            if (o instanceof Number) {
                val = ((Number) o).doubleValue();
            }
            return val;
        } catch (Exception ex) {
            log.info(ex);
            return 0d;
        }
    }

    private boolean validationCheck() {
        if (tableDataListOfList.size() <= 0) {
            return false;
        } else {
            return true;
        }
    }
}
