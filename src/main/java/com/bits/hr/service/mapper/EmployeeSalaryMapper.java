package com.bits.hr.service.mapper;

import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link EmployeeSalary} and its DTO {@link EmployeeSalaryDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface EmployeeSalaryMapper extends EntityMapper<EmployeeSalaryDTO, EmployeeSalary> {
    @Mapping(source = "employee.fullName", target = "employeeName")
    @Mapping(source = "employee.designation.designationName", target = "designation")
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.employmentStatus", target = "employmentStatus")
    EmployeeSalaryDTO toDto(EmployeeSalary employeeSalary);

    @Mapping(source = "employeeId", target = "employee")
    EmployeeSalary toEntity(EmployeeSalaryDTO employeeSalaryDTO);

    default EmployeeSalary fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmployeeSalary employeeSalary = new EmployeeSalary();
        employeeSalary.setId(id);
        return employeeSalary;
    }
}
