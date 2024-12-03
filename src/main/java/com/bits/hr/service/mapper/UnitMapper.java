package com.bits.hr.service.mapper;

import com.bits.hr.domain.Unit;
import com.bits.hr.service.dto.UnitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Unit} and its DTO {@link UnitDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface UnitMapper extends EntityMapper<UnitDTO, Unit> {
    default Unit fromId(Long id) {
        if (id == null) {
            return null;
        }
        Unit unit = new Unit();
        unit.setId(id);
        return unit;
    }
}
