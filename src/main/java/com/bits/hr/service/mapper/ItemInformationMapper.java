package com.bits.hr.service.mapper;

import com.bits.hr.domain.ItemInformation;
import com.bits.hr.service.dto.ItemInformationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link ItemInformation} and its DTO {@link ItemInformationDTO}.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { DepartmentMapper.class, UnitOfMeasurementMapper.class, UserMapper.class }
)
public interface ItemInformationMapper extends EntityMapper<ItemInformationDTO, ItemInformation> {
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.departmentName", target = "departmentName")
    @Mapping(source = "unitOfMeasurement.id", target = "unitOfMeasurementId")
    @Mapping(source = "unitOfMeasurement.name", target = "unitOfMeasurementName")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    ItemInformationDTO toDto(ItemInformation itemInformation);

    @Mapping(source = "departmentId", target = "department")
    @Mapping(source = "unitOfMeasurementId", target = "unitOfMeasurement")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    ItemInformation toEntity(ItemInformationDTO itemInformationDTO);

    default ItemInformation fromId(Long id) {
        if (id == null) {
            return null;
        }
        ItemInformation itemInformation = new ItemInformation();
        itemInformation.setId(id);
        return itemInformation;
    }
}
