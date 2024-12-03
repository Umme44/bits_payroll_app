package com.bits.hr.service.xlExportHandling.festivalBonus;

import com.bits.hr.domain.Festival;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.service.dto.FestivalBonusDetailsDTO;
import com.bits.hr.service.xlExportHandling.ReportConstants;
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
public class FestivalBonusExportUtil {

    private final XSSFWorkbook workbook;
    private final Festival festival;
    private final List<FestivalBonusDetailsDTO> festivalBonusDetailsDTOList;
    private XSSFSheet sheet;

    private int rowCount = 0;

    public FestivalBonusExportUtil(Festival festival, List<FestivalBonusDetailsDTO> festivalBonusDetailsDTOList) {
        this.festival = festival;
        this.festivalBonusDetailsDTOList = festivalBonusDetailsDTOList;
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
                Date date = Date.from( // Convert from modern java.time class to troublesome old legacy class.  DO NOT DO THIS unless you must, to interoperate with old code not yet updated for java.time.
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

        if (festivalBonusDetailsDTOList.size() > 0) {
            Row row02 = sheet.createRow(rowCount);

            String str = festival.getTitle();
            sheet.addMergedRegion(CellRangeAddress.valueOf("A2:G2"));
            font.setFontHeight(12);
            style.setFont(font);
            createCellWithStyle(row02, columnCount, str, style);
            ++rowCount;

            str =
                "Bonus Disbursement Date : " +
                DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(festival.getBonusDisbursementDate());

            Row row03 = sheet.createRow(rowCount);
            sheet.addMergedRegion(CellRangeAddress.valueOf("A3:D3"));
            createCellWithStyle(row03, 0, str, style);
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

        createCellWithStyle(row, columnCount++, "S/L", style);
        createCellWithStyle(row, columnCount++, "PIN", style);
        createCellWithStyle(row, columnCount++, "Employee Name", style);
        createCellWithStyle(row, columnCount++, "Joining Date", style);
        createCellWithStyle(row, columnCount++, "Confirmation Date", style);

        createCellWithStyle(row, columnCount++, "Employment Status", style);

        createCellWithStyle(row, columnCount++, "HC", style);

        createCellWithStyle(row, columnCount++, "Band", style);
        createCellWithStyle(row, columnCount++, "Unit", style);
        createCellWithStyle(row, columnCount++, "Department", style);

        createCellWithStyle(row, columnCount++, "Gross", style);
        createCellWithStyle(row, columnCount++, "Basic", style);
        createCellWithStyle(row, columnCount++, "Festival Bonus", style);
        createCellWithStyle(row, columnCount++, "Remarks", style);
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

        int serial = 1;
        for (FestivalBonusDetailsDTO fbd : festivalBonusDetailsDTOList) {
            XSSFRow row = sheet.createRow(rowCount);
            int columnCount = 0;

            createCell(row, columnCount++, serial++, style);
            createCell(row, columnCount++, fbd.getPin(), style);
            createCell(row, columnCount++, fbd.getFullName(), style);
            createCellWithStyle(row, columnCount++, fbd.getDoj(), dateStyle);

            if (fbd.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
                createCellWithStyle(row, columnCount++, fbd.getDoc(), dateStyle);
                createCell(row, columnCount++, "Regular", style);
            } else {
                LocalDate contactEnd = LocalDate.MIN;
                if (fbd.getContractPeriodEndDate() != null) {
                    contactEnd = fbd.getContractPeriodEndDate();
                }
                if (fbd.getContractPeriodExtendedTo() != null) {
                    contactEnd = fbd.getContractPeriodExtendedTo();
                }

                createCellWithStyle(row, columnCount++, contactEnd, dateStyle);
                createCell(row, columnCount++, "Contractual", style);
            }

            createCell(row, columnCount++, 1, style);

            createCell(row, columnCount++, fbd.getBandName(), style);
            createCell(row, columnCount++, fbd.getUnitName(), style);
            createCell(row, columnCount++, fbd.getDepartmentName(), style);

            createCell(row, columnCount++, fbd.getGross(), style);
            createCell(row, columnCount++, fbd.getBasic(), style);
            createCell(row, columnCount++, fbd.getBonusAmount(), style);
            if (fbd.getRemarks() != null) {
                createCell(row, columnCount++, fbd.getRemarks(), style);
            } else {
                createCell(row, columnCount++, " ", style);
            }
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
        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);

        createCellWithStyle(row, columnCount++, festivalBonusDetailsDTOList.size(), style);

        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);

        double totalGrossSalary = festivalBonusDetailsDTOList.stream().mapToDouble(FestivalBonusDetailsDTO::getGross).sum();
        double totalBasicSalary = festivalBonusDetailsDTOList.stream().mapToDouble(FestivalBonusDetailsDTO::getBasic).sum();
        double totalBonus = festivalBonusDetailsDTOList.stream().mapToDouble(FestivalBonusDetailsDTO::getBonusAmount).sum();

        createCellWithStyle(row, columnCount++, totalGrossSalary, style);
        createCellWithStyle(row, columnCount++, totalBasicSalary, style);
        createCellWithStyle(row, columnCount++, totalBonus, style);

        createCellWithStyle(row, columnCount++, " ", style);
        ++rowCount;
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        sheet = workbook.createSheet("festival-bonus");
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
