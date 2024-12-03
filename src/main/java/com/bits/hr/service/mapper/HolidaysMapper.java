package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.HolidaysDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Holidays} and its DTO {@link HolidaysDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface HolidaysMapper extends EntityMapper<HolidaysDTO, Holidays> {
    default Holidays fromId(Long id) {
        if (id == null) {
            return null;
        }
        Holidays holidays = new Holidays();
        holidays.setId(id);
        return holidays;
    }
}
