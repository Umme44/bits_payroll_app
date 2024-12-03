package com.bits.hr.web.rest.export;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.xlExportHandling.EmployeeTinNumberExportService;
import com.bits.hr.web.rest.util.CopyStreams;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping
public class EmployeeTinNumberExportResource {

    @Autowired
    private EmployeeTinNumberExportService employeeTinNumberExportService;

    @GetMapping("/api/employee-mgt/export-tin-number-csv")
    public void exportEmployeeTinNumberExportCSV(HttpServletResponse response) {
        try {
            Optional<File> result = employeeTinNumberExportService.csvExportEmployeeTinNumber();

            InputStream targetStream = new FileInputStream(result.get());

            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Employee_TIN_Number" + ".csv");
            response.getOutputStream();
            CopyStreams.copy(targetStream, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error(e);
            throw new BadRequestAlertException("Something went wrong", "EmployeeTinNumberExportResource", "internalServerError");
        }
    }
}
