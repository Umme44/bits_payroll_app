package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.ArrearSalaryMasterDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link ArrearSalaryMaster} and its DTO {@link ArrearSalaryMasterDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface ArrearSalaryMasterMapper extends EntityMapper<ArrearSalaryMasterDTO, ArrearSalaryMaster> {
    default ArrearSalaryMaster fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArrearSalaryMaster arrearSalaryMaster = new ArrearSalaryMaster();
        arrearSalaryMaster.setId(id);
        return arrearSalaryMaster;
    }
}
