package com.bits.hr.service.xlExportHandling;

import com.bits.hr.domain.Holidays;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class HolidaysExportXlsxExportUtil {

    private final XSSFWorkbook workbook;
    private final List<Holidays> holidaysList; //= new ArrayList<EmployeeSalary>();
    private XSSFSheet sheet;

    public HolidaysExportXlsxExportUtil(List<Holidays> holidaysList) {
        this.holidaysList = holidaysList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("HolidaySheet");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(8);
        style.setFont(font);

        int columnCount = 0;
        createCell(row, columnCount++, "Holidays", style);
        createCell(row, columnCount++, "Type", style);
        createCell(row, columnCount++, "Start Date", style);
        createCell(row, columnCount++, "End Date", style);
        createCell(row, columnCount++, "Duration", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue(value.toString());
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(8);
        style.setFont(font);

        for (Holidays holiday : holidaysList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, holiday.getDescription(), style);
            createCell(row, columnCount++, holiday.getHolidayType().toString(), style);
            createCell(row, columnCount++, holiday.getStartDate(), style);
            createCell(row, columnCount++, holiday.getEndDate(), style);
            long daysBetween = ChronoUnit.DAYS.between(holiday.getStartDate(), holiday.getEndDate()) + 1;
            createCell(row, columnCount++, Long.toString(daysBetween), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
