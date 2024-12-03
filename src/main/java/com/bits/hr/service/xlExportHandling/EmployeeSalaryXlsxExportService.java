package com.bits.hr.service.xlExportHandling;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.service.dto.AllowanceNameDTO;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
public class EmployeeSalaryXlsxExportService {

    private final XSSFWorkbook workbook;
    private final List<EmployeeSalaryDTO> employeeSalaryList; //= new ArrayList<EmployeeSalary>();
    private final AllowanceNameDTO allowanceNameDTO;
    private XSSFSheet sheet;

    private int rowCount = 0;

    public EmployeeSalaryXlsxExportService(List<EmployeeSalaryDTO> employeeSalaryList, AllowanceNameDTO allowanceNameDTO) {
        this.employeeSalaryList = employeeSalaryList;
        this.allowanceNameDTO = allowanceNameDTO;
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
        createCellWithStyle(row, columnCount, "BRAC IT Services Limited", style);
        ++rowCount;

        if (employeeSalaryList.size() > 0) {
            Row row02 = sheet.createRow(rowCount);
            String mon = "";
            int yr = 0;
            if (employeeSalaryList.get(0).getMonth() != null) {
                Month month = employeeSalaryList.get(0).getMonth();
                mon = month.toString();
            }
            if (employeeSalaryList.get(0).getYear() != null) {
                int year = employeeSalaryList.get(0).getYear();
                yr = year;
            }

            String str = "Salary Statement for the month of " + mon + ", " + yr;
            sheet.addMergedRegion(CellRangeAddress.valueOf("A2:G2"));
            font.setFontHeight(12);
            style.setFont(font);
            createCellWithStyle(row02, columnCount, str, style);
            ++rowCount;

            str = "Generation Date: ";
            LocalDate genDate = employeeSalaryList.get(0).getSalaryGenerationDate();
            if (genDate != null) {
                Row row03 = sheet.createRow(rowCount);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
                String formattedString = genDate.format(formatter);
                str += formattedString;
                sheet.addMergedRegion(CellRangeAddress.valueOf("A3:D3"));
                createCellWithStyle(row03, 0, str, style);
                ++rowCount;
            }
        }
        // blank line
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
        style.setRotation((short) 90);
        style.setWrapText(true);
        int columnCount = 0;
        // createCellWithStyle(row, columnCount++, "Month", style);
        // createCellWithStyle(row, columnCount++, "Year", style);
        // createCellWithStyle(row, columnCount++, "Salary Generation Date", style);
        createCellWithStyle(row, columnCount++, "Ref Pin", style);
        createCellWithStyle(row, columnCount++, "Pin", style);
        createCellWithStyle(row, columnCount++, "Name of the \n Employees", style);
        createCellWithStyle(row, columnCount++, "Joining \n Date", style);
        createCellWithStyle(row, columnCount++, "Confirmation \n Date", style);
        createCellWithStyle(row, columnCount++, "Employee \n Category", style);
        createCellWithStyle(row, columnCount++, "HC", style);
        createCellWithStyle(row, columnCount++, "Unit", style);
        createCellWithStyle(row, columnCount++, "Department", style);
        createCellWithStyle(row, columnCount++, "Main Gross \n Salary", style);
        createCellWithStyle(row, columnCount++, "Basic", style);
        createCellWithStyle(row, columnCount++, "House Rent", style);
        createCellWithStyle(row, columnCount++, "Medical", style);
        createCellWithStyle(row, columnCount++, "Conveyance", style);
        createCellWithStyle(row, columnCount++, "Absent Days", style);
        createCellWithStyle(row, columnCount++, "Fraction Days", style);
        createCellWithStyle(row, columnCount++, "Payable Gross \n Salary", style);
        createCellWithStyle(row, columnCount++, "Basic", style);
        createCellWithStyle(row, columnCount++, "House Rent", style);
        createCellWithStyle(row, columnCount++, "Medical", style);
        createCellWithStyle(row, columnCount++, "Conveyance", style);
        createCellWithStyle(row, columnCount++, "Arrear Salary", style);
        createCellWithStyle(row, columnCount++, "Pf Deduction", style);
        createCellWithStyle(row, columnCount++, "Tax Deduction", style);
        createCellWithStyle(row, columnCount++, "HAF", style);
        createCellWithStyle(row, columnCount++, "Mobile Bill \n Deduction", style);
        createCellWithStyle(row, columnCount++, "Other \n Deduction", style);
        createCellWithStyle(row, columnCount++, "Total \n Deduction", style);
        createCellWithStyle(row, columnCount++, "Net Pay", style);
        createCellWithStyle(row, columnCount++, "Remarks", style);
        createCellWithStyle(row, columnCount++, "PF Contribution", style);
        createCellWithStyle(row, columnCount++, "Gratuity Fund \n Contribution", style);
        createCellWithStyle(row, columnCount++, "Provision For \n Festival Bonus", style);
        createCellWithStyle(row, columnCount++, "Provision For \n Leave Encashment", style);
        createCellWithStyle(row, columnCount++, "Provision For \n Project Bonus", style);

        if (!(allowanceNameDTO.getAllowance01Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance01Name().equals(""))) {
            createCellWithStyle(row, columnCount++, allowanceNameDTO.getAllowance01Name(), style);
        }
        if (!(allowanceNameDTO.getAllowance02Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance02Name().equals(""))) {
            createCellWithStyle(row, columnCount++, allowanceNameDTO.getAllowance02Name(), style);
        }
        if (!(allowanceNameDTO.getAllowance03Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance03Name().equals(""))) {
            createCellWithStyle(row, columnCount++, allowanceNameDTO.getAllowance03Name(), style);
        }
        if (!(allowanceNameDTO.getAllowance04Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance04Name().equals(""))) {
            createCellWithStyle(row, columnCount++, allowanceNameDTO.getAllowance04Name(), style);
        }
        if (!(allowanceNameDTO.getAllowance05Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance05Name().equals(""))) {
            createCellWithStyle(row, columnCount++, allowanceNameDTO.getAllowance05Name(), style);
        }
        if (!(allowanceNameDTO.getAllowance06Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance06Name().equals(""))) {
            createCellWithStyle(row, columnCount++, allowanceNameDTO.getAllowance06Name(), style);
        }

        ++rowCount;
    }

    private void writeDataLines() {
        // int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(8);
        style.setFont(font);
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd mmm, yyyy"));

        // int i=0;
        for (EmployeeSalaryDTO salary : employeeSalaryList) {
            // i++;
            // StopWatch stopWatch = new StopWatch();
            // stopWatch.start();
            XSSFRow row = sheet.createRow(rowCount);
            // Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            //  if (salary.getMonth() != null) {
            //      createCell(row, columnCount++, salary.getMonth().toString(), style);
            //   } else columnCount++;
            //   createCell(row, columnCount++, salary.getYear(), style);
            //   if (salary.getSalaryGenerationDate() != null) {
            //      createCell(row, columnCount++, salary.getSalaryGenerationDate(), dateStyle);
            //   } else columnCount++;
            createCell(row, columnCount++, salary.getRefPin(), style);
            createCell(row, columnCount++, salary.getPin(), style);
            createCell(row, columnCount++, salary.getEmployeeName(), style);
            if (salary.getJoiningDate() != null) {
                createCell(row, columnCount++, salary.getJoiningDate(), dateStyle);
            } else columnCount++;
            if (salary.getConfirmationDate() != null) {
                createCell(row, columnCount++, salary.getConfirmationDate(), dateStyle);
            } else columnCount++;
            if (salary.getEmployeeCategory() != null) {
                String category = "";
                if (salary.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
                    category = "By Contract";
                }
                if (salary.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
                    category = "Confirmed";
                }
                if (salary.getEmployeeCategory() == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE) {
                    category = "Probation";
                }
                if (salary.getEmployeeCategory() == EmployeeCategory.INTERN) {
                    category = "Intern";
                }
                createCell(row, columnCount++, category, style);
            } else columnCount++;
            createCell(row, columnCount++, 1, style); // HC
            createCell(row, columnCount++, salary.getUnit(), style);
            createCell(row, columnCount++, salary.getDepartment(), style);
            createCell(row, columnCount++, salary.getMainGrossSalary(), style);
            createCell(row, columnCount++, salary.getMainGrossBasicSalary(), style);
            createCell(row, columnCount++, salary.getMainGrossHouseRent(), style);
            createCell(row, columnCount++, salary.getMainGrossMedicalAllowance(), style);
            createCell(row, columnCount++, salary.getMainGrossConveyanceAllowance(), style);
            createCell(row, columnCount++, salary.getAbsentDays(), style);
            createCell(row, columnCount++, salary.getFractionDays(), style);
            createCell(row, columnCount++, salary.getPayableGrossSalary(), style);
            createCell(row, columnCount++, salary.getPayableGrossBasicSalary(), style);
            createCell(row, columnCount++, salary.getPayableGrossHouseRent(), style);
            createCell(row, columnCount++, salary.getPayableGrossMedicalAllowance(), style);
            createCell(row, columnCount++, salary.getPayableGrossConveyanceAllowance(), style);
            createCell(row, columnCount++, salary.getArrearSalary(), style);
            createCell(row, columnCount++, salary.getPfDeduction(), style);
            createCell(row, columnCount++, salary.getTaxDeduction(), style);
            createCell(row, columnCount++, salary.getWelfareFundDeduction(), style);
            createCell(row, columnCount++, salary.getMobileBillDeduction(), style);
            createCell(row, columnCount++, salary.getOtherDeduction(), style);
            createCell(row, columnCount++, salary.getTotalDeduction(), style);
            createCell(row, columnCount++, salary.getNetPay(), style);
            createCell(row, columnCount++, salary.getRemarks(), style);
            createCell(row, columnCount++, salary.getPfContribution(), style);
            createCell(row, columnCount++, salary.getGfContribution(), style);
            createCell(row, columnCount++, salary.getProvisionForFestivalBonus(), style);
            createCell(row, columnCount++, salary.getProvisionForLeaveEncashment(), style);
            createCell(row, columnCount++, 0, style);

            if (!(allowanceNameDTO.getAllowance01Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance01Name().equals(""))) {
                createCell(row, columnCount++, salary.getAllowance01(), style);
            }
            if (!(allowanceNameDTO.getAllowance02Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance02Name().equals(""))) {
                createCell(row, columnCount++, salary.getAllowance02(), style);
            }
            if (!(allowanceNameDTO.getAllowance03Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance03Name().equals(""))) {
                createCell(row, columnCount++, salary.getAllowance03(), style);
            }
            if (!(allowanceNameDTO.getAllowance04Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance04Name().equals(""))) {
                createCell(row, columnCount++, salary.getAllowance04(), style);
            }
            if (!(allowanceNameDTO.getAllowance05Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance05Name().equals(""))) {
                createCell(row, columnCount++, salary.getAllowance05(), style);
            }
            if (!(allowanceNameDTO.getAllowance06Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance06Name().equals(""))) {
                createCell(row, columnCount++, salary.getAllowance06(), style);
            }

            // stopWatch.stop();
            // log.info(i+" : "+stopWatch.getTotalTimeMillis());
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
        //        style.setRotation((short) 90);
        style.setWrapText(true);
        int columnCount = 0;
        //        createCellWithStyle(row, columnCount++, " ", style);
        //        createCellWithStyle(row, columnCount++, " ", style);
        //        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, employeeSalaryList.size(), style);
        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);

        double totalMainGross = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getMainGrossSalary).sum();
        double totalBasic = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getMainGrossBasicSalary).sum();
        double totalHouseRent = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getMainGrossHouseRent).sum();
        double totalMedical = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getMainGrossMedicalAllowance).sum();
        double totalConveyance = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getMainGrossConveyanceAllowance).sum();

        createCellWithStyle(row, columnCount++, totalMainGross, style);
        createCellWithStyle(row, columnCount++, totalBasic, style);
        createCellWithStyle(row, columnCount++, totalHouseRent, style);
        createCellWithStyle(row, columnCount++, totalMedical, style);
        createCellWithStyle(row, columnCount++, totalConveyance, style);

        createCellWithStyle(row, columnCount++, " ", style);
        createCellWithStyle(row, columnCount++, " ", style);

        double totalPayableGross = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getPayableGrossSalary).sum();
        double totalPayableBasic = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getPayableGrossBasicSalary).sum();
        double totalPayableHouseRent = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getPayableGrossHouseRent).sum();
        double totalPayableMedical = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getPayableGrossMedicalAllowance).sum();
        double totalPayableConveyance = employeeSalaryList
            .stream()
            .mapToDouble(EmployeeSalaryDTO::getPayableGrossConveyanceAllowance)
            .sum();

        createCellWithStyle(row, columnCount++, totalPayableGross, style);
        createCellWithStyle(row, columnCount++, totalPayableBasic, style);
        createCellWithStyle(row, columnCount++, totalPayableHouseRent, style);
        createCellWithStyle(row, columnCount++, totalPayableMedical, style);
        createCellWithStyle(row, columnCount++, totalPayableConveyance, style);

        double totalArrear = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getArrearSalary).sum();
        double totalPF = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getPfDeduction).sum();
        double totalTax = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getTaxDeduction).sum();
        double totalHaf = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getWelfareFundDeduction).sum();
        double totalMobileBill = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getMobileBillDeduction).sum();
        double totalOtherDeduction = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getOtherDeduction).sum();
        double totalTotalDeduction = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getTotalDeduction).sum();
        double totalNetPay = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getNetPay).sum();

        createCellWithStyle(row, columnCount++, totalArrear, style);
        createCellWithStyle(row, columnCount++, totalPF, style);
        createCellWithStyle(row, columnCount++, totalTax, style);
        createCellWithStyle(row, columnCount++, totalHaf, style);
        createCellWithStyle(row, columnCount++, totalMobileBill, style);
        createCellWithStyle(row, columnCount++, totalOtherDeduction, style);
        createCellWithStyle(row, columnCount++, totalTotalDeduction, style);
        createCellWithStyle(row, columnCount++, totalNetPay, style);
        createCellWithStyle(row, columnCount++, " ", style);

        double totalPfCon = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getPfContribution).sum();
        double totalGF = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getGfContribution).sum();
        double totalProvisionForFestivalBonus = employeeSalaryList
            .stream()
            .mapToDouble(EmployeeSalaryDTO::getProvisionForFestivalBonus)
            .sum();
        double totalProvisionForLeaveEncashment = employeeSalaryList
            .stream()
            .mapToDouble(EmployeeSalaryDTO::getProvisionForLeaveEncashment)
            .sum();
        double totalProvisionForProjectBonus = employeeSalaryList
            .stream()
            .mapToDouble(EmployeeSalaryDTO::getProvisionForProjectBonus)
            .sum();

        createCellWithStyle(row, columnCount++, totalPfCon, style);
        createCellWithStyle(row, columnCount++, totalGF, style);
        createCellWithStyle(row, columnCount++, totalProvisionForFestivalBonus, style);
        createCellWithStyle(row, columnCount++, totalProvisionForLeaveEncashment, style);
        createCellWithStyle(row, columnCount++, totalProvisionForProjectBonus, style);

        if (!(allowanceNameDTO.getAllowance01Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance01Name().equals(""))) {
            double totalAllowance01 = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getAllowance01).sum();
            createCellWithStyle(row, columnCount++, totalAllowance01, style);
        }
        if (!(allowanceNameDTO.getAllowance02Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance02Name().equals(""))) {
            double totalAllowance02 = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getAllowance02).sum();
            createCellWithStyle(row, columnCount++, totalAllowance02, style);
        }
        if (!(allowanceNameDTO.getAllowance03Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance03Name().equals(""))) {
            double totalAllowance03 = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getAllowance03).sum();
            createCellWithStyle(row, columnCount++, totalAllowance03, style);
        }
        if (!(allowanceNameDTO.getAllowance04Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance04Name().equals(""))) {
            double totalAllowance04 = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getAllowance04).sum();
            createCellWithStyle(row, columnCount++, totalAllowance04, style);
        }
        if (!(allowanceNameDTO.getAllowance05Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance05Name().equals(""))) {
            double totalAllowance05 = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getAllowance05).sum();
            createCellWithStyle(row, columnCount++, totalAllowance05, style);
        }
        if (!(allowanceNameDTO.getAllowance06Name().equals("NOT_APPLICABLE") || allowanceNameDTO.getAllowance06Name().equals(""))) {
            double totalAllowance06 = employeeSalaryList.stream().mapToDouble(EmployeeSalaryDTO::getAllowance06).sum();
            createCellWithStyle(row, columnCount++, totalAllowance06, style);
        }

        ++rowCount;
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        sheet = workbook.createSheet("SalarySheet");
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
