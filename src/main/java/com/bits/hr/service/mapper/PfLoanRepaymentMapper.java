package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.PfLoanRepaymentDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link PfLoanRepayment} and its DTO {@link PfLoanRepaymentDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { PfLoanMapper.class })
public interface PfLoanRepaymentMapper extends EntityMapper<PfLoanRepaymentDTO, PfLoanRepayment> {
    @Mapping(source = "pfLoan.id", target = "pfLoanId")
    @Mapping(source = "pfLoan.pfAccount.id", target = "pfAccountId")
    @Mapping(source = "pfLoan.pfAccount.pfCode", target = "pfCode")
    @Mapping(source = "pfLoan.pfAccount.designationName", target = "designationName")
    @Mapping(source = "pfLoan.pfAccount.departmentName", target = "departmentName")
    @Mapping(source = "pfLoan.pfAccount.unitName", target = "unitName")
    @Mapping(source = "pfLoan.pfAccount.accHolderName", target = "accHolderName")
    @Mapping(source = "pfLoan.pfAccount.pin", target = "pin")
    PfLoanRepaymentDTO toDto(PfLoanRepayment pfLoanRepayment);

    @Mapping(source = "pfLoanId", target = "pfLoan")
    PfLoanRepayment toEntity(PfLoanRepaymentDTO pfLoanRepaymentDTO);

    default PfLoanRepayment fromId(Long id) {
        if (id == null) {
            return null;
        }
        PfLoanRepayment pfLoanRepayment = new PfLoanRepayment();
        pfLoanRepayment.setId(id);
        return pfLoanRepayment;
    }
}
