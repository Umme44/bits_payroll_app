package com.bits.hr.service.importXL.employee.helperMethods.entityHelper;

import com.bits.hr.domain.Department;
import com.bits.hr.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetOrCreateDepartment {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Department get(String str) {
        return departmentRepository
            .findDepartmentByDepartmentNameIgnoreCase(str)
            .orElseGet(() -> {
                Department d = new Department();
                d.setDepartmentName(str);
                return departmentRepository.save(d);
            });
    }
}
