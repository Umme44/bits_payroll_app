package com.bits.hr.service.mapper;

import com.bits.hr.domain.Location;
import com.bits.hr.service.dto.LocationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.locationName", target = "parentName")
    @Mapping(source = "parent.locationCode", target = "parentCode")
    LocationDTO toDto(Location location);

    @Mapping(source = "parentId", target = "parent")
    Location toEntity(LocationDTO locationDTO);

    default Location fromId(Long id) {
        if (id == null) {
            return null;
        }
        Location location = new Location();
        location.setId(id);
        return location;
    }
}
