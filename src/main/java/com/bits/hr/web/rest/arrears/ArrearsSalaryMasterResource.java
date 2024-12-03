package com.bits.hr.web.rest.arrears;

import com.bits.hr.service.arrears.ArrearsSalaryMasterService;
import com.bits.hr.service.dto.ArrearSalaryMasterDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/payroll-mgt/arrears")
public class ArrearsSalaryMasterResource {

    private final Logger log = LoggerFactory.getLogger(ArrearsSalaryMasterResource.class);

    @Autowired
    private ArrearsSalaryMasterService arrearsSalaryMasterService;

    @GetMapping("/salary-master")
    public ResponseEntity<List<ArrearSalaryMasterDTO>> getAllArrearsSalaryMaster(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all active arrears salary master");
        Page<ArrearSalaryMasterDTO> page = arrearsSalaryMasterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
