package com.bits.hr.service.mapper;

import com.bits.hr.domain.EmployeeNOC;
import com.bits.hr.service.dto.EmployeeNOCDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link EmployeeNOC} and its DTO {@link EmployeeNOCDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class, UserMapper.class })
public interface EmployeeNOCMapper extends EntityMapper<EmployeeNOCDTO, EmployeeNOC> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "employeePin")
    @Mapping(source = "employee.fullName", target = "employeeName")
    @Mapping(source = "employee.surName", target = "employeeSurName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    @Mapping(source = "employee.dateOfJoining", target = "dateOfJoining")
    @Mapping(source = "employee.gender", target = "employeeGender")
    @Mapping(source = "signatoryPerson.id", target = "signatoryPersonId")
    @Mapping(source = "signatoryPerson.pin", target = "signatoryPersonPin")
    @Mapping(source = "signatoryPerson.fullName", target = "signatoryPersonName")
    @Mapping(source = "signatoryPerson.department.departmentName", target = "signatoryPersonDepartment")
    @Mapping(source = "signatoryPerson.designation.designationName", target = "signatoryPersonDesignation")
    @Mapping(source = "signatoryPerson.unit.unitName", target = "signatoryPersonUnit")
    @Mapping(source = "signatoryPerson.officialEmail", target = "signatoryPersonEmail")
    @Mapping(source = "signatoryPerson.officialContactNo", target = "signatoryPersonCell")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    @Mapping(source = "generatedBy.id", target = "generatedById")
    @Mapping(source = "generatedBy.login", target = "generatedByLogin")
    EmployeeNOCDTO toDto(EmployeeNOC employeeNOC);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "signatoryPersonId", target = "signatoryPerson")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    @Mapping(source = "generatedById", target = "generatedBy")
    EmployeeNOC toEntity(EmployeeNOCDTO employeeNOCDTO);

    default EmployeeNOC fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmployeeNOC employeeNOC = new EmployeeNOC();
        employeeNOC.setId(id);
        return employeeNOC;
    }
}
