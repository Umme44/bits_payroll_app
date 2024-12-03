package com.bits.hr.web.rest;

import com.bits.hr.service.ItemInformationService;
import com.bits.hr.service.dto.DepartmentItemsDTO;
import com.bits.hr.service.dto.ItemInformationDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common")
public class ItemInformationCommonResource {

    @Autowired
    private ItemInformationService itemInformationService;

    @GetMapping("/item-information/get-by-department-id/{departmentId}")
    public ResponseEntity<List<ItemInformationDTO>> getByDepartmentId(@PathVariable long departmentId) {
        List<ItemInformationDTO> itemInformationList = itemInformationService.findByDepartmentId(departmentId);
        return ResponseEntity.ok(itemInformationList);
    }

    @GetMapping("/item-information/departments-and-items-mapping-list")
    public ResponseEntity<List<DepartmentItemsDTO>> getDepartmentsAndItemsMapping() {
        return ResponseEntity.ok().body(itemInformationService.findDepartmentsAndItemsMapping());
    }
}
