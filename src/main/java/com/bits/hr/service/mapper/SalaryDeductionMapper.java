package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.SalaryDeductionDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link SalaryDeduction} and its DTO {@link SalaryDeductionDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { DeductionTypeMapper.class, EmployeeMapper.class }
)
public interface SalaryDeductionMapper extends EntityMapper<SalaryDeductionDTO, SalaryDeduction> {
    @Mapping(source = "deductionType.id", target = "deductionTypeId")
    @Mapping(source = "deductionType.name", target = "deductionTypeName")
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    SalaryDeductionDTO toDto(SalaryDeduction salaryDeduction);

    @Mapping(source = "deductionTypeId", target = "deductionType")
    @Mapping(source = "employeeId", target = "employee")
    SalaryDeduction toEntity(SalaryDeductionDTO salaryDeductionDTO);

    default SalaryDeduction fromId(Long id) {
        if (id == null) {
            return null;
        }
        SalaryDeduction salaryDeduction = new SalaryDeduction();
        salaryDeduction.setId(id);
        return salaryDeduction;
    }
}
