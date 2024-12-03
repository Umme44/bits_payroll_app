package com.bits.hr.service.mapper;

import com.bits.hr.domain.PfNominee;
import com.bits.hr.service.dto.PfNomineeDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link PfNominee} and its DTO {@link PfNomineeDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { PfAccountMapper.class, EmployeeMapper.class })
public interface PfNomineeMapper extends EntityMapper<PfNomineeDTO, PfNominee> {
    @Mapping(source = "pfAccount.id", target = "pfAccountId")
    @Mapping(source = "pfAccount.pin", target = "pin")
    @Mapping(source = "pfAccount.accHolderName", target = "accHolderName")
    @Mapping(source = "pfAccount.designationName", target = "designationName")
    @Mapping(source = "pfAccount.departmentName", target = "departmentName")
    @Mapping(source = "pfAccount.unitName", target = "unitName")
    @Mapping(source = "pfWitness.id", target = "pfWitnessId")
    @Mapping(source = "pfWitness.fullName", target = "pfWitnessFullName")
    @Mapping(source = "approvedBy.id", target = "approvedById")
    @Mapping(source = "approvedBy.fullName", target = "approvedByFullName")
    @Mapping(target = "pfNomineeImage", ignore = true)
    PfNomineeDTO toDto(PfNominee pfNominee);

    @Mapping(source = "pfAccountId", target = "pfAccount")
    @Mapping(source = "pfWitnessId", target = "pfWitness")
    @Mapping(source = "approvedById", target = "approvedBy")
    PfNominee toEntity(PfNomineeDTO pfNomineeDTO);

    default PfNominee fromId(Long id) {
        if (id == null) {
            return null;
        }
        PfNominee pfNominee = new PfNominee();
        pfNominee.setId(id);
        return pfNominee;
    }
}
