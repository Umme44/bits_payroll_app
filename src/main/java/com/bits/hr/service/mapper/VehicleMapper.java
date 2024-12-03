package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.VehicleDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Vehicle} and its DTO {@link VehicleDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserMapper.class, EmployeeMapper.class })
public interface VehicleMapper extends EntityMapper<VehicleDTO, Vehicle> {
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    @Mapping(source = "approvedBy.id", target = "approvedById")
    @Mapping(source = "approvedBy.login", target = "approvedByLogin")
    @Mapping(source = "assignedDriver.id", target = "assignedDriverId")
    VehicleDTO toDto(Vehicle vehicle);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    @Mapping(source = "approvedById", target = "approvedBy")
    @Mapping(source = "assignedDriverId", target = "assignedDriver")
    Vehicle toEntity(VehicleDTO vehicleDTO);

    default Vehicle fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        return vehicle;
    }
}
