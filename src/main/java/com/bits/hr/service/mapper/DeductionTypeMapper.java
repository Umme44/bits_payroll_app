package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.DeductionTypeDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link DeductionType} and its DTO {@link DeductionTypeDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface DeductionTypeMapper extends EntityMapper<DeductionTypeDTO, DeductionType> {
    default DeductionType fromId(Long id) {
        if (id == null) {
            return null;
        }
        DeductionType deductionType = new DeductionType();
        deductionType.setId(id);
        return deductionType;
    }
}
