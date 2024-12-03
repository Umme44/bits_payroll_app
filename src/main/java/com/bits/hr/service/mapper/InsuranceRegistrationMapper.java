package com.bits.hr.service.mapper;

import com.bits.hr.domain.InsuranceRegistration;
import com.bits.hr.service.dto.InsuranceRegistrationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link InsuranceRegistration} and its DTO {@link InsuranceRegistrationDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class, UserMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InsuranceRegistrationMapper extends EntityMapper<InsuranceRegistrationDTO, InsuranceRegistration> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "employeePin")
    @Mapping(source = "employee.fullName", target = "employeeName")
    @Mapping(source = "employee.employeeCategory", target = "employeeCategory")
    @Mapping(source = "employee.dateOfConfirmation", target = "dateOfConfirmation")
    @Mapping(source = "employee.dateOfJoining", target = "dateOfJoining")
    @Mapping(source = "approvedBy.id", target = "approvedById")
    @Mapping(source = "approvedBy.login", target = "approvedByLogin")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    InsuranceRegistrationDTO toDto(InsuranceRegistration insuranceRegistration);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "approvedById", target = "approvedBy")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    InsuranceRegistration toEntity(InsuranceRegistrationDTO insuranceRegistrationDTO);

    default InsuranceRegistration fromId(Long id) {
        if (id == null) {
            return null;
        }
        InsuranceRegistration insuranceRegistration = new InsuranceRegistration();
        insuranceRegistration.setId(id);
        return insuranceRegistration;
    }
}
