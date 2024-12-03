package com.bits.hr.service.mapper;

import com.bits.hr.domain.SalaryCertificate;
import com.bits.hr.service.dto.SalaryCertificateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link SalaryCertificate} and its DTO {@link SalaryCertificateDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, EmployeeMapper.class})
public interface SalaryCertificateMapper extends EntityMapper<SalaryCertificateDTO, SalaryCertificate> {
    @Mapping(source = "employee.fullName", target = "employeeName")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    @Mapping(source = "sanctionBy.id", target = "sanctionById")
    @Mapping(source = "sanctionBy.login", target = "sanctionByLogin")
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "signatoryPerson.id", target = "signatoryPersonId")
    @Mapping(source = "signatoryPerson.fullName", target = "signatoryPersonName")
    @Mapping(source = "signatoryPerson.designation.designationName", target = "signatoryPersonDesignation")
    SalaryCertificateDTO toDto(SalaryCertificate salaryCertificate);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    @Mapping(source = "sanctionById", target = "sanctionBy")
    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "signatoryPersonId", target = "signatoryPerson")
    SalaryCertificate toEntity(SalaryCertificateDTO salaryCertificateDTO);

    default SalaryCertificate fromId(Long id) {
        if (id == null) {
            return null;
        }
        SalaryCertificate salaryCertificate = new SalaryCertificate();
        salaryCertificate.setId(id);
        return salaryCertificate;
    }
}
