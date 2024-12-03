package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.PfLoanApplicationDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link PfLoanApplication} and its DTO {@link PfLoanApplicationDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class, PfAccountMapper.class })
public interface PfLoanApplicationMapper extends EntityMapper<PfLoanApplicationDTO, PfLoanApplication> {
    @Mapping(source = "recommendedBy.id", target = "recommendedById")
    @Mapping(source = "approvedBy.id", target = "approvedById")
    @Mapping(source = "approvedBy.fullName", target = "approvedByFullName")
    @Mapping(source = "rejectedBy.id", target = "rejectedById")
    @Mapping(source = "rejectedBy.fullName", target = "rejectedByFullName")
    @Mapping(source = "pfAccount.id", target = "pfAccountId")
    @Mapping(source = "pfAccount.pfCode", target = "pfCode")
    @Mapping(source = "pfAccount.status", target = "accountStatus")
    @Mapping(source = "pfAccount.designationName", target = "designationName")
    @Mapping(source = "pfAccount.departmentName", target = "departmentName")
    @Mapping(source = "pfAccount.unitName", target = "unitName")
    @Mapping(source = "pfAccount.accHolderName", target = "accHolderName")
    @Mapping(source = "pfAccount.pin", target = "pin")
    PfLoanApplicationDTO toDto(PfLoanApplication pfLoanApplication);

    @Mapping(source = "recommendedById", target = "recommendedBy")
    @Mapping(source = "approvedById", target = "approvedBy")
    @Mapping(source = "rejectedById", target = "rejectedBy")
    @Mapping(source = "pfAccountId", target = "pfAccount")
    PfLoanApplication toEntity(PfLoanApplicationDTO pfLoanApplicationDTO);

    default PfLoanApplication fromId(Long id) {
        if (id == null) {
            return null;
        }
        PfLoanApplication pfLoanApplication = new PfLoanApplication();
        pfLoanApplication.setId(id);
        return pfLoanApplication;
    }
}
