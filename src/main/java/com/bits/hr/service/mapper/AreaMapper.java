package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.AreaDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Area} and its DTO {@link AreaDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface AreaMapper extends EntityMapper<AreaDTO, Area> {
    default Area fromId(Long id) {
        if (id == null) {
            return null;
        }
        Area area = new Area();
        area.setId(id);
        return area;
    }
}
