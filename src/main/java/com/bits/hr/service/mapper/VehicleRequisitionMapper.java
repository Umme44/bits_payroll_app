package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.VehicleRequisitionDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link VehicleRequisition} and its DTO {@link VehicleRequisitionDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { UserMapper.class, EmployeeMapper.class, VehicleMapper.class }
)
public interface VehicleRequisitionMapper extends EntityMapper<VehicleRequisitionDTO, VehicleRequisition> {
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    @Mapping(source = "approvedBy.id", target = "approvedById")
    @Mapping(source = "approvedBy.login", target = "approvedByLogin")
    @Mapping(source = "requester.id", target = "requesterId")
    @Mapping(source = "vehicle.id", target = "vehicleId")
    VehicleRequisitionDTO toDto(VehicleRequisition vehicleRequisition);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    @Mapping(source = "approvedById", target = "approvedBy")
    @Mapping(source = "requesterId", target = "requester")
    @Mapping(source = "vehicleId", target = "vehicle")
    VehicleRequisition toEntity(VehicleRequisitionDTO vehicleRequisitionDTO);

    default VehicleRequisition fromId(Long id) {
        if (id == null) {
            return null;
        }
        VehicleRequisition vehicleRequisition = new VehicleRequisition();
        vehicleRequisition.setId(id);
        return vehicleRequisition;
    }
}
