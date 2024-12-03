package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.ArrearSalaryDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link ArrearSalary} and its DTO {@link ArrearSalaryDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface ArrearSalaryMapper extends EntityMapper<ArrearSalaryDTO, ArrearSalary> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    ArrearSalaryDTO toDto(ArrearSalary arrearSalary);

    @Mapping(source = "employeeId", target = "employee")
    ArrearSalary toEntity(ArrearSalaryDTO arrearSalaryDTO);

    default ArrearSalary fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArrearSalary arrearSalary = new ArrearSalary();
        arrearSalary.setId(id);
        return arrearSalary;
    }
}
