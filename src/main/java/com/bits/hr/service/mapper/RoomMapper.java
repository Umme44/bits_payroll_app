package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.RoomDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Room} and its DTO {@link RoomDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { UserMapper.class, BuildingMapper.class, FloorMapper.class, RoomTypeMapper.class },
    unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface RoomMapper extends EntityMapper<RoomDTO, Room> {
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    @Mapping(source = "building.id", target = "buildingId")
    @Mapping(source = "building.buildingName", target = "buildingName")
    @Mapping(source = "floor.id", target = "floorId")
    @Mapping(source = "floor.floorName", target = "floorName")
    @Mapping(source = "roomType.id", target = "roomTypeId")
    @Mapping(source = "roomType.typeName", target = "roomTypeName")
    @Mapping(source = "capacity", target = "capacity")
    RoomDTO toDto(Room room);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    @Mapping(source = "buildingId", target = "building")
    @Mapping(source = "floorId", target = "floor")
    @Mapping(source = "roomTypeId", target = "roomType")
    Room toEntity(RoomDTO roomDTO);

    default Room fromId(Long id) {
        if (id == null) {
            return null;
        }
        Room room = new Room();
        room.setId(id);
        return room;
    }
}
