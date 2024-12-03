package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.EmployeePinConfigurationDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link EmployeePinConfiguration} and its DTO {@link EmployeePinConfigurationDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserMapper.class })
public interface EmployeePinConfigurationMapper extends EntityMapper<EmployeePinConfigurationDTO, EmployeePinConfiguration> {
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    EmployeePinConfigurationDTO toDto(EmployeePinConfiguration employeePinConfiguration);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    EmployeePinConfiguration toEntity(EmployeePinConfigurationDTO employeePinConfigurationDTO);

    default EmployeePinConfiguration fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmployeePinConfiguration employeePinConfiguration = new EmployeePinConfiguration();
        employeePinConfiguration.setId(id);
        return employeePinConfiguration;
    }
}
