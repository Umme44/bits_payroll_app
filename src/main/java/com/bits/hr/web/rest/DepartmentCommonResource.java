package com.bits.hr.web.rest;

import com.bits.hr.service.DepartmentService;
import com.bits.hr.service.dto.DepartmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/common/departments")
public class DepartmentCommonResource {

    @Autowired
    private DepartmentService departmentService;

    private final Logger log = LoggerFactory.getLogger(DepartmentCommonResource.class);

    @GetMapping("")
    public List<DepartmentDTO> getAllDepartments() {
        log.debug("REST request to get all Departments");
        return departmentService.findAll();
    }
}
