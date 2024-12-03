package com.bits.hr.service.mapper;

import com.bits.hr.domain.CalenderYear;
import com.bits.hr.service.dto.CalenderYearDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link CalenderYear} and its DTO {@link CalenderYearDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface CalenderYearMapper extends EntityMapper<CalenderYearDTO, CalenderYear> {
    default CalenderYear fromId(Long id) {
        if (id == null) {
            return null;
        }
        CalenderYear calenderYear = new CalenderYear();
        calenderYear.setId(id);
        return calenderYear;
    }
}
