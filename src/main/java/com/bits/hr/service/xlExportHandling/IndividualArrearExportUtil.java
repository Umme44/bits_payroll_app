package com.bits.hr.service.xlExportHandling;

import com.bits.hr.service.dto.IndividualArrearSalaryDTO;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
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
public class IndividualArrearExportUtil {

    private final XSSFWorkbook workbook;
    private final List<IndividualArrearSalaryDTO> individualArrearSalaryDTOList;
    private XSSFSheet sheet;

    private int rowCount = 0;

    public IndividualArrearExportUtil(List<IndividualArrearSalaryDTO> individualArrearSalaryDTOList) {
        this.individualArrearSalaryDTOList = individualArrearSalaryDTOList;
        workbook = new XSSFWorkbook();
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        try {
            // sheet.autoSizeColumn(columnCount);
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

    private void writeHeader01() {
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

        if (individualArrearSalaryDTOList.size() > 0) {
            Row row02 = sheet.createRow(rowCount);

            String str = individualArrearSalaryDTOList.get(0).getTitle();
            sheet.addMergedRegion(CellRangeAddress.valueOf("A2:G2"));
            font.setFontHeight(12);
            style.setFont(font);
            createCellWithStyle(row02, columnCount, str, style);
            ++rowCount;

            String effectiveFromDate = DateTimeFormatter
                .ofLocalizedDate(FormatStyle.LONG)
                .format(LocalDate.parse(individualArrearSalaryDTOList.get(0).getTitleEffectiveFrom()));
            Row row03 = sheet.createRow(rowCount);
            // sheet.addMergedRegion(CellRangeAddress.valueOf("A3:D3"));
            createCellWithStyle(row03, 0, "Effective From", style);
            createCellWithStyle(row03, 1, effectiveFromDate, style);
            ++rowCount;

            String effectiveDate = DateTimeFormatter
                .ofLocalizedDate(FormatStyle.LONG)
                .format(individualArrearSalaryDTOList.get(0).getEffectiveDate());
            Row row04 = sheet.createRow(rowCount);
            // sheet.addMergedRegion(CellRangeAddress.valueOf("A3:D3"));
            createCellWithStyle(row04, 0, "Effective Date", style);
            createCellWithStyle(row04, 1, effectiveDate, style);
            ++rowCount;
        }
        ++rowCount;
    }

    private void writeHeaderLine() {
        Row row = sheet.createRow(rowCount);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(10);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        style.setWrapText(true);
        int columnCount = 0;

        String arrearRemarks = individualArrearSalaryDTOList.get(0).getArrearRemarks();
        createCellWithStyle(row, columnCount++, "PIN", style);
        createCellWithStyle(row, columnCount++, "Name of the \n Employees", style);
        //        createCellWithStyle(row, columnCount++, "Effective Date", style);
        createCellWithStyle(row, columnCount++, "HC", style);
        createCellWithStyle(row, columnCount++, "Existing \n Band", style);
        createCellWithStyle(row, columnCount++, "New \n Band", style);
        createCellWithStyle(row, columnCount++, "Unit", style);
        createCellWithStyle(row, columnCount++, "Department", style);
        createCellWithStyle(row, columnCount++, "Existing Gross", style);
        createCellWithStyle(row, columnCount++, "New Gross", style);
        createCellWithStyle(row, columnCount++, "Increment", style);
        createCellWithStyle(row, columnCount++, arrearRemarks, style);
        createCellWithStyle(row, columnCount++, "Arrear Festival Bonus", style);
        createCellWithStyle(row, columnCount++, "Arrear-PF \n Deduction", style);
        createCellWithStyle(row, columnCount++, "Tax \n Deduction", style);
        createCellWithStyle(row, columnCount++, "Net Pay", style);
        createCellWithStyle(row, columnCount++, "PF Contribution", style);
        ++rowCount;
    }

    private void writeDataLines() {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(8);
        style.setFont(font);
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd mmm, yyyy"));

        // int i=0;
        for (IndividualArrearSalaryDTO individualArrearSalaryDTO : individualArrearSalaryDTOList) {
            XSSFRow row = sheet.createRow(rowCount);
            int columnCount = 0;

            createCell(row, columnCount++, individualArrearSalaryDTO.getPin(), style);
            createCell(row, columnCount++, individualArrearSalaryDTO.getFullName(), style);
            //            createCell(row, columnCount++, individualArrearSalaryDTO.getEffectiveDate().toString(), style);
            createCell(row, columnCount++, 1, style); // HC

            createCell(row, columnCount++, individualArrearSalaryDTO.getExistingBand(), style);
            createCell(row, columnCount++, individualArrearSalaryDTO.getNewBand(), style);

            createCell(row, columnCount++, individualArrearSalaryDTO.getUnitName(), style);
            createCell(row, columnCount++, individualArrearSalaryDTO.getDepartmentName(), style);

            createCell(row, columnCount++, individualArrearSalaryDTO.getExistingGross(), style);
            createCell(row, columnCount++, individualArrearSalaryDTO.getNewGross(), style);

            createCell(row, columnCount++, individualArrearSalaryDTO.getIncrement(), style);

            createCell(row, columnCount++, individualArrearSalaryDTO.getArrearSalary(), style);
            createCell(row, columnCount++, individualArrearSalaryDTO.getFestivalBonus(), style);

            createCell(row, columnCount++, individualArrearSalaryDTO.getArrearPfDeduction(), style);
            createCell(row, columnCount++, individualArrearSalaryDTO.getTaxDeduction(), style);
            createCell(row, columnCount++, individualArrearSalaryDTO.getNetPay(), style);
            createCell(row, columnCount++, individualArrearSalaryDTO.getPfContribution(), style);

            ++rowCount;
        }
    }

    private void writeFooterLine() {
        Row row = sheet.createRow(rowCount);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(10);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        style.setWrapText(true);
        int columnCount = 0;

        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);
        //        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, individualArrearSalaryDTOList.size(), style);
        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);

        double totalExistingGross = individualArrearSalaryDTOList.stream().mapToDouble(IndividualArrearSalaryDTO::getExistingGross).sum();
        double totalNewGross = individualArrearSalaryDTOList.stream().mapToDouble(IndividualArrearSalaryDTO::getNewGross).sum();
        double totalIncrement = individualArrearSalaryDTOList.stream().mapToDouble(IndividualArrearSalaryDTO::getIncrement).sum();

        double totalArrear = individualArrearSalaryDTOList.stream().mapToDouble(IndividualArrearSalaryDTO::getArrearSalary).sum();
        double totalFestivalBonus = individualArrearSalaryDTOList.stream().mapToDouble(IndividualArrearSalaryDTO::getFestivalBonus).sum();

        double totalArrearPfDeduction = individualArrearSalaryDTOList
            .stream()
            .mapToDouble(IndividualArrearSalaryDTO::getArrearPfDeduction)
            .sum();
        double totalTaxDeduction = individualArrearSalaryDTOList.stream().mapToDouble(IndividualArrearSalaryDTO::getTaxDeduction).sum();

        double totalNetPay = individualArrearSalaryDTOList.stream().mapToDouble(IndividualArrearSalaryDTO::getNetPay).sum();

        double totalPfContribution = individualArrearSalaryDTOList.stream().mapToDouble(IndividualArrearSalaryDTO::getPfContribution).sum();

        createCellWithStyle(row, columnCount++, totalExistingGross, style);
        createCellWithStyle(row, columnCount++, totalNewGross, style);
        createCellWithStyle(row, columnCount++, totalIncrement, style);
        createCellWithStyle(row, columnCount++, totalArrear, style);
        createCellWithStyle(row, columnCount++, totalFestivalBonus, style);
        createCellWithStyle(row, columnCount++, totalArrearPfDeduction, style);
        createCellWithStyle(row, columnCount++, totalTaxDeduction, style);
        createCellWithStyle(row, columnCount++, totalNetPay, style);
        createCellWithStyle(row, columnCount++, totalPfContribution, style);

        ++rowCount;
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        sheet = workbook.createSheet("individual_arrear");
        writeHeader01();
        writeHeaderLine();
        writeDataLines();
        writeFooterLine();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
