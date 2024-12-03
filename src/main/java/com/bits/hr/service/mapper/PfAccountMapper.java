package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.PfAccountDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link PfAccount} and its DTO {@link PfAccountDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface PfAccountMapper extends EntityMapper<PfAccountDTO, PfAccount> {
    default PfAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        PfAccount pfAccount = new PfAccount();
        pfAccount.setId(id);
        return pfAccount;
    }
}
