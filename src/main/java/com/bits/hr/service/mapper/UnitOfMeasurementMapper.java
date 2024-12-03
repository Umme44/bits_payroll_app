package com.bits.hr.service.mapper;

import com.bits.hr.domain.UnitOfMeasurement;
import com.bits.hr.service.dto.UnitOfMeasurementDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link UnitOfMeasurement} and its DTO {@link UnitOfMeasurementDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserMapper.class })
public interface UnitOfMeasurementMapper extends EntityMapper<UnitOfMeasurementDTO, UnitOfMeasurement> {
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    UnitOfMeasurementDTO toDto(UnitOfMeasurement unitOfMeasurement);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    UnitOfMeasurement toEntity(UnitOfMeasurementDTO unitOfMeasurementDTO);

    default UnitOfMeasurement fromId(Long id) {
        if (id == null) {
            return null;
        }
        UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement();
        unitOfMeasurement.setId(id);
        return unitOfMeasurement;
    }
}
