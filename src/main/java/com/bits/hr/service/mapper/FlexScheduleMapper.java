package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.FlexScheduleDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link FlexSchedule} and its DTO {@link FlexScheduleDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class, UserMapper.class })
public interface FlexScheduleMapper extends EntityMapper<FlexScheduleDTO, FlexSchedule> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    FlexScheduleDTO toDto(FlexSchedule flexSchedule);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "createdById", target = "createdBy")
    FlexSchedule toEntity(FlexScheduleDTO flexScheduleDTO);

    default FlexSchedule fromId(Long id) {
        if (id == null) {
            return null;
        }
        FlexSchedule flexSchedule = new FlexSchedule();
        flexSchedule.setId(id);
        return flexSchedule;
    }
}
