package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.TimeSlotDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link TimeSlot} and its DTO {@link TimeSlotDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface TimeSlotMapper extends EntityMapper<TimeSlotDTO, TimeSlot> {
    default TimeSlot fromId(Long id) {
        if (id == null) {
            return null;
        }
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(id);
        return timeSlot;
    }
}
