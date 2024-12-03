package com.bits.hr.web.rest.fixedAllowance;

import com.bits.hr.service.AllowanceNameService;
import com.bits.hr.service.dto.AllowanceNameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee-mgt")
public class FixedAllowanceNameResource {

    @Autowired
    private AllowanceNameService allowanceNameService;

    @GetMapping("/allowance-name")
    public AllowanceNameDTO getAllowanceNames() throws Exception {
        return allowanceNameService.getAllowanceName();
    }
}
