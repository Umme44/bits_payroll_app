package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.EmploymentCertificateDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link EmploymentCertificate} and its DTO {@link EmploymentCertificateDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class, UserMapper.class })
public interface EmploymentCertificateMapper extends EntityMapper<EmploymentCertificateDTO, EmploymentCertificate> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "employeePin")
    @Mapping(source = "employee.fullName", target = "employeeName")
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
    EmploymentCertificateDTO toDto(EmploymentCertificate employmentCertificate);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "signatoryPersonId", target = "signatoryPerson")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    @Mapping(source = "generatedById", target = "generatedBy")
    EmploymentCertificate toEntity(EmploymentCertificateDTO employmentCertificateDTO);

    default EmploymentCertificate fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmploymentCertificate employmentCertificate = new EmploymentCertificate();
        employmentCertificate.setId(id);
        return employmentCertificate;
    }
}
