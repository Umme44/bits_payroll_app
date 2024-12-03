package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.InsuranceClaimDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link InsuranceClaim} and its DTO {@link InsuranceClaimDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { InsuranceRegistrationMapper.class })
public interface InsuranceClaimMapper extends EntityMapper<InsuranceClaimDTO, InsuranceClaim> {
    @Mapping(source = "insuranceRegistration.id", target = "insuranceRegistrationId")
    @Mapping(source = "insuranceRegistration.insuranceId", target = "insuranceCardId")
    @Mapping(source = "insuranceRegistration.employee.pin", target = "policyHolderPin")
    @Mapping(source = "insuranceRegistration.employee.fullName", target = "policyHolderName")
    @Mapping(source = "insuranceRegistration.name", target = "registrationName")
    @Mapping(source = "insuranceRegistration.insuranceRelation.", target = "relation")
    InsuranceClaimDTO toDto(InsuranceClaim insuranceClaim);

    @Mapping(source = "insuranceRegistrationId", target = "insuranceRegistration")
    InsuranceClaim toEntity(InsuranceClaimDTO insuranceClaimDTO);

    default InsuranceClaim fromId(Long id) {
        if (id == null) {
            return null;
        }
        InsuranceClaim insuranceClaim = new InsuranceClaim();
        insuranceClaim.setId(id);
        return insuranceClaim;
    }
}
