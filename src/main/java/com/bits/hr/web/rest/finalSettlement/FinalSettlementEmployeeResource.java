package com.bits.hr.web.rest.finalSettlement;

import com.bits.hr.service.dto.EmployeeCommonDTO;
import com.bits.hr.service.search.EmployeeGeneralSearchService;
import com.bits.hr.service.search.FilterDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/payroll-mgt")
public class FinalSettlementEmployeeResource {

    @Autowired
    private EmployeeGeneralSearchService employeeGeneralSearchService;

    // Pending , Settled , ** ALL

    @PostMapping("/employee-search-final-settlement")
    public ResponseEntity<List<EmployeeCommonDTO>> searchForFinalSettlement(
        @RequestBody @Valid FilterDto filterDto,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) throws Exception {
        Page<EmployeeCommonDTO> page = employeeGeneralSearchService.employeeSearchForFinalSettlement(filterDto, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
