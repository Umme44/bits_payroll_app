package com.bits.hr.service.mapper;

import com.bits.hr.domain.EmployeePin;
import com.bits.hr.service.dto.EmployeePinDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link EmployeePin} and its DTO {@link EmployeePinDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { DepartmentMapper.class, DesignationMapper.class, UnitMapper.class, UserMapper.class, RecruitmentRequisitionFormMapper.class }
)
public interface EmployeePinMapper extends EntityMapper<EmployeePinDTO, EmployeePin> {
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "designation.id", target = "designationId")
    @Mapping(source = "unit.id", target = "unitId")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    @Mapping(source = "department.departmentName", target = "departmentName")
    @Mapping(source = "designation.designationName", target = "designationName")
    @Mapping(source = "unit.unitName", target = "unitName")
    @Mapping(source = "recruitmentRequisitionForm.id", target = "recruitmentRequisitionFormId")
    @Mapping(source = "recruitmentRequisitionForm.rrfNumber", target = "rrfNumber")
    EmployeePinDTO toDto(EmployeePin employeePin);

    @Mapping(source = "departmentId", target = "department")
    @Mapping(source = "designationId", target = "designation")
    @Mapping(source = "unitId", target = "unit")
    @Mapping(source = "updatedById", target = "updatedBy")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "recruitmentRequisitionFormId", target = "recruitmentRequisitionForm")
    EmployeePin toEntity(EmployeePinDTO employeePinDTO);

    default EmployeePin fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmployeePin employeePin = new EmployeePin();
        employeePin.setId(id);
        return employeePin;
    }
}
