package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.EmployeeResignationDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link EmployeeResignation} and its DTO {@link EmployeeResignationDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface EmployeeResignationMapper extends EntityMapper<EmployeeResignationDTO, EmployeeResignation> {
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "uodatedBy.id", target = "uodatedById")
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "name")
    @Mapping(source = "employee.designation.designationName", target = "designation")
    @Mapping(source = "employee.department.departmentName", target = "department")
    @Mapping(source = "employee.unit.unitName", target = "unit")
    EmployeeResignationDTO toDto(EmployeeResignation employeeResignation);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "uodatedById", target = "uodatedBy")
    @Mapping(source = "employeeId", target = "employee")
    EmployeeResignation toEntity(EmployeeResignationDTO employeeResignationDTO);

    default EmployeeResignation fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmployeeResignation employeeResignation = new EmployeeResignation();
        employeeResignation.setId(id);
        return employeeResignation;
    }
}
