package com.bits.hr.web.rest.export;

import com.bits.hr.domain.Holidays;
import com.bits.hr.repository.HolidaysRepository;
import com.bits.hr.service.xlExportHandling.HolidaysExportXlsxExportUtil;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee-mgt")
public class HolidaysExportXlsxController {

    @Autowired
    private HolidaysRepository holidaysRepository;

    //XLSX report for Holidays by year
    @GetMapping(value = { "/export/Holidays/", "/export/Holidays/{year}" })
    public void exportToExcel(@PathVariable(name = "year", required = false) Integer year, HttpServletResponse response)
        throws IOException {
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=HolidaySheet" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        //List<EmployeeSalary> listUsers = employeeSalaryRepository.findAll();

        LocalDate satrt = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        List<Holidays> listHolidays = holidaysRepository.findHolidaysStartDateBetweenDates(satrt, end);

        HolidaysExportXlsxExportUtil excelExporter = new HolidaysExportXlsxExportUtil(listHolidays);

        excelExporter.export(response);
    }
}
