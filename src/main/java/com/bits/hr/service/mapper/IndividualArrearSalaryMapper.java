package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.IndividualArrearSalaryDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link IndividualArrearSalary} and its DTO {@link IndividualArrearSalaryDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface IndividualArrearSalaryMapper extends EntityMapper<IndividualArrearSalaryDTO, IndividualArrearSalary> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    IndividualArrearSalaryDTO toDto(IndividualArrearSalary individualArrearSalary);

    @Mapping(source = "employeeId", target = "employee")
    IndividualArrearSalary toEntity(IndividualArrearSalaryDTO individualArrearSalaryDTO);

    default IndividualArrearSalary fromId(Long id) {
        if (id == null) {
            return null;
        }
        IndividualArrearSalary individualArrearSalary = new IndividualArrearSalary();
        individualArrearSalary.setId(id);
        return individualArrearSalary;
    }
}
