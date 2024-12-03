package com.bits.hr.service.xlExportHandling;

import com.bits.hr.domain.Department;
import com.bits.hr.domain.Designation;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Unit;
import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EmployeeCommonExportUtil {

    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Employee> employeeList = new ArrayList<Employee>();
    private int rowCount = 0;

    public EmployeeCommonExportUtil(List<Employee> employeeList) {
        this.employeeList = employeeList;
        workbook = new XSSFWorkbook();
    }

    private void writeAbout() {
        sheet = workbook.createSheet("Employee-sheet");
        Row row = sheet.createRow(rowCount++);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        int columnCount = 0;
        createCell(row, columnCount++, "Employee Details", style);
    }

    private void writeTimeStamp() {
        //      sheet = workbook.createSheet("Employee-sheet");
        Row row = sheet.createRow(rowCount++);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(6);
        style.setFont(font);
        int columnCount = 0;

        Locale locale = new Locale("fr", "FR");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String currDate = dateFormat.format(new Date());

        createCell(row, columnCount++, "Document Generated On -" + currDate, style);
    }

    private void writeHeaderLine() {
        //        sheet = workbook.createSheet("SalarySheet");

        Row row = sheet.createRow(rowCount++);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(8);
        style.setFont(font);

        int columnCount = 0;
        createCell(row, columnCount++, "PIN", style);
        createCell(row, columnCount++, "Name", style);
        createCell(row, columnCount++, "Unit", style);
        createCell(row, columnCount++, "Designation", style);
        createCell(row, columnCount++, "Department", style);
        createCell(row, columnCount++, "Mobile", style);
        createCell(row, columnCount++, "WhatsApp", style);
        createCell(row, columnCount++, "Skype", style);
        createCell(row, columnCount++, "Email", style);
        createCell(row, columnCount++, "Emergency Contact", style);
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
            value = Optional.ofNullable(value).orElse("-");
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        //        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(8);
        style.setFont(font);

        for (Employee employee : employeeList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, employee.getPin(), style);
            createCell(row, columnCount++, employee.getFullName(), style);
            createCell(row, columnCount++, Optional.ofNullable(employee.getUnit()).map(Unit::getUnitName).orElse(""), style);
            createCell(
                row,
                columnCount++,
                Optional.ofNullable(employee.getDesignation()).map(Designation::getDesignationName).orElse(""),
                style
            );
            createCell(
                row,
                columnCount++,
                Optional.ofNullable(employee.getDepartment()).map(Department::getDepartmentName).orElse(""),
                style
            );
            createCell(row, columnCount++, employee.getOfficialContactNo(), style);
            createCell(row, columnCount++, employee.getWhatsappId(), style);
            createCell(row, columnCount++, employee.getSkypeId(), style);
            createCell(row, columnCount++, employee.getOfficialEmail(), style);
            createCell(row, columnCount++, employee.getEmergencyContactPersonContactNumber(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeAbout();
        writeHeaderLine();
        writeDataLines();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
