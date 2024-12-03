package com.bits.hr.service.mapper;

import com.bits.hr.domain.BankBranch;
import com.bits.hr.service.dto.BankBranchDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link BankBranch} and its DTO {@link BankBranchDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface BankBranchMapper extends EntityMapper<BankBranchDTO, BankBranch> {
    default BankBranch fromId(Long id) {
        if (id == null) {
            return null;
        }
        BankBranch bankBranch = new BankBranch();
        bankBranch.setId(id);
        return bankBranch;
    }
}
