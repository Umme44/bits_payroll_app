package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.RoomTypeDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link RoomType} and its DTO {@link RoomTypeDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserMapper.class })
public interface RoomTypeMapper extends EntityMapper<RoomTypeDTO, RoomType> {
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    RoomTypeDTO toDto(RoomType roomType);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    RoomType toEntity(RoomTypeDTO roomTypeDTO);

    default RoomType fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoomType roomType = new RoomType();
        roomType.setId(id);
        return roomType;
    }
}
