package com.bits.hr.service.mapper;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { DesignationMapper.class, DepartmentMapper.class }
)
public interface EmployeeMinimalMapper extends EntityMapper<EmployeeMinimalDTO, Employee> {
    @Mapping(source = "department.departmentName", target = "departmentName")
    @Mapping(source = "designation.designationName", target = "designationName")
    @Mapping(source = "unit.unitName", target = "unitName")
    @Mapping(source = "band.bandName", target = "bandName")
    @Mapping(source = "employee.employmentStatus", target = "employmentStatus")
    EmployeeMinimalDTO toDto(Employee employee);
}
