package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.FlexScheduleApplicationDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link FlexScheduleApplication} and its DTO {@link FlexScheduleApplicationDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { EmployeeMapper.class, UserMapper.class, TimeSlotMapper.class }
)
public interface FlexScheduleApplicationMapper extends EntityMapper<FlexScheduleApplicationDTO, FlexScheduleApplication> {
    @Mapping(source = "requester.id", target = "requesterId")
    @Mapping(source = "requester.fullName", target = "fullName")
    @Mapping(source = "requester.pin", target = "pin")
    @Mapping(source = "requester.designation.designationName", target = "designationName")
    @Mapping(source = "sanctionedBy.id", target = "sanctionedById")
    @Mapping(source = "sanctionedBy.login", target = "sanctionedByLogin")
    @Mapping(source = "appliedBy.id", target = "appliedById")
    @Mapping(source = "appliedBy.login", target = "appliedByLogin")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    @Mapping(source = "timeSlot.id", target = "timeSlotId")
    @Mapping(source = "timeSlot.inTime", target = "inTime")
    @Mapping(source = "timeSlot.outTime", target = "outTime")
    FlexScheduleApplicationDTO toDto(FlexScheduleApplication flexScheduleApplication);

    @Mapping(source = "requesterId", target = "requester")
    @Mapping(source = "sanctionedById", target = "sanctionedBy")
    @Mapping(source = "appliedById", target = "appliedBy")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    @Mapping(source = "timeSlotId", target = "timeSlot")
    FlexScheduleApplication toEntity(FlexScheduleApplicationDTO flexScheduleApplicationDTO);

    default FlexScheduleApplication fromId(Long id) {
        if (id == null) {
            return null;
        }
        FlexScheduleApplication flexScheduleApplication = new FlexScheduleApplication();
        flexScheduleApplication.setId(id);
        return flexScheduleApplication;
    }
}
