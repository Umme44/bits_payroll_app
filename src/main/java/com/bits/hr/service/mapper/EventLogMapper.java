package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.EventLogDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link EventLog} and its DTO {@link EventLogDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserMapper.class })
public interface EventLogMapper extends EntityMapper<EventLogDTO, EventLog> {
    @Mapping(source = "performedBy.id", target = "performedById")
    @Mapping(source = "performedBy.login", target = "performedByLogin")
    EventLogDTO toDto(EventLog eventLog);

    @Mapping(source = "performedById", target = "performedBy")
    EventLog toEntity(EventLogDTO eventLogDTO);

    default EventLog fromId(Long id) {
        if (id == null) {
            return null;
        }
        EventLog eventLog = new EventLog();
        eventLog.setId(id);
        return eventLog;
    }
}
