package com.bits.hr.web.rest.export;

import com.bits.hr.domain.IndividualArrearSalary;
import com.bits.hr.repository.IndividualArrearSalaryRepository;
import com.bits.hr.service.dto.IndividualArrearSalaryDTO;
import com.bits.hr.service.mapper.IndividualArrearSalaryMapper;
import com.bits.hr.service.xlExportHandling.IndividualArrearExportUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class IndividualArrearSalaryExportController {

    @Autowired
    private IndividualArrearSalaryRepository individualArrearSalaryRepository;

    @Autowired
    private IndividualArrearSalaryMapper individualArrearSalaryMapper;

    @GetMapping("/api/payroll-mgt/individual-arrear-salary-export")
    public void export(@RequestParam(name = "title") String title, HttpServletResponse response) throws IOException {
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.getAllByTitle(title.trim());
        List<IndividualArrearSalaryDTO> individualArrearSalaryDTOList = individualArrearSalaryMapper.toDto(individualArrearSalaryList);
        IndividualArrearExportUtil excelExporter = new IndividualArrearExportUtil(individualArrearSalaryDTOList);
        excelExporter.export(response);
    }
}
