package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.SpecialShiftTimingDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link SpecialShiftTiming} and its DTO {@link SpecialShiftTimingDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { TimeSlotMapper.class, UserMapper.class })
public interface SpecialShiftTimingMapper extends EntityMapper<SpecialShiftTimingDTO, SpecialShiftTiming> {
    @Mapping(source = "timeSlot.id", target = "timeSlotId")
    @Mapping(source = "timeSlot.inTime", target = "inTime")
    @Mapping(source = "timeSlot.outTime", target = "outTime")
    @Mapping(source = "timeSlot.title", target = "timeSlotTitle")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    SpecialShiftTimingDTO toDto(SpecialShiftTiming specialShiftTiming);

    @Mapping(source = "timeSlotId", target = "timeSlot")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    SpecialShiftTiming toEntity(SpecialShiftTimingDTO specialShiftTimingDTO);

    default SpecialShiftTiming fromId(Long id) {
        if (id == null) {
            return null;
        }
        SpecialShiftTiming specialShiftTiming = new SpecialShiftTiming();
        specialShiftTiming.setId(id);
        return specialShiftTiming;
    }
}
