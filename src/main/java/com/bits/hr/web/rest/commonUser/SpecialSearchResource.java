package com.bits.hr.web.rest.commonUser;

import com.bits.hr.service.specializedSearch.SpecialSearchService;
import com.bits.hr.service.specializedSearch.dto.EmployeeSpecializedSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/common")
public class SpecialSearchResource {

    @Autowired
    SpecialSearchService specialSearchService;

    @GetMapping("/special-search-detail/{employeeId}")
    public ResponseEntity<EmployeeSpecializedSearch> getPayslipForMonthYear(@PathVariable("employeeId") long employeeId) {
        return ResponseUtil.wrapOrNotFound(specialSearchService.getResult(employeeId));
    }
}
