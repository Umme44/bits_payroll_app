package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.PfLoanDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link PfLoan} and its DTO {@link PfLoanDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { PfLoanApplicationMapper.class, PfAccountMapper.class }
)
public interface PfLoanMapper extends EntityMapper<PfLoanDTO, PfLoan> {
    @Mapping(source = "pfLoanApplication.id", target = "pfLoanApplicationId")
    @Mapping(source = "pfAccount.id", target = "pfAccountId")
    @Mapping(source = "pfAccount.pfCode", target = "pfCode")
    @Mapping(source = "pfAccount.status", target = "accountStatus")
    @Mapping(source = "pfAccount.designationName", target = "designationName")
    @Mapping(source = "pfAccount.departmentName", target = "departmentName")
    @Mapping(source = "pfAccount.unitName", target = "unitName")
    @Mapping(source = "pfAccount.accHolderName", target = "accHolderName")
    @Mapping(source = "pfAccount.pin", target = "pin")
    PfLoanDTO toDto(PfLoan pfLoan);

    @Mapping(source = "pfLoanApplicationId", target = "pfLoanApplication")
    @Mapping(source = "pfAccountId", target = "pfAccount")
    PfLoan toEntity(PfLoanDTO pfLoanDTO);

    default PfLoan fromId(Long id) {
        if (id == null) {
            return null;
        }
        PfLoan pfLoan = new PfLoan();
        pfLoan.setId(id);
        return pfLoan;
    }
}
