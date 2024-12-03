package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.FloorDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Floor} and its DTO {@link FloorDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserMapper.class, BuildingMapper.class })
public interface FloorMapper extends EntityMapper<FloorDTO, Floor> {
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    @Mapping(source = "building.id", target = "buildingId")
    @Mapping(source = "building.buildingName", target = "buildingName")
    FloorDTO toDto(Floor floor);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    @Mapping(source = "buildingId", target = "building")
    Floor toEntity(FloorDTO floorDTO);

    default Floor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Floor floor = new Floor();
        floor.setId(id);
        return floor;
    }
}
