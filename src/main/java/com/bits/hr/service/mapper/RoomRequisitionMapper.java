package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.RoomRequisitionDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link RoomRequisition} and its DTO {@link RoomRequisitionDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { UserMapper.class, EmployeeMapper.class, RoomMapper.class }
)
public interface RoomRequisitionMapper extends EntityMapper<RoomRequisitionDTO, RoomRequisition> {
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    @Mapping(source = "sanctionedBy.id", target = "sanctionedById")
    @Mapping(source = "sanctionedBy.login", target = "sanctionedByLogin")
    @Mapping(source = "requester.id", target = "requesterId")
    @Mapping(source = "requester.fullName", target = "fullName")
    @Mapping(source = "requester.pin", target = "pin")
    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "room.roomName", target = "roomName")
    @Mapping(source = "room.building.buildingName", target = "buildingName")
    @Mapping(source = "room.floor.floorName", target = "floorName")
    @Mapping(source = "room.roomType.typeName", target = "typeName")
    RoomRequisitionDTO toDto(RoomRequisition roomRequisition);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    @Mapping(source = "sanctionedById", target = "sanctionedBy")
    @Mapping(source = "requesterId", target = "requester")
    @Mapping(source = "roomId", target = "room")
    RoomRequisition toEntity(RoomRequisitionDTO roomRequisitionDTO);

    default RoomRequisition fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoomRequisition roomRequisition = new RoomRequisition();
        roomRequisition.setId(id);
        return roomRequisition;
    }
}
