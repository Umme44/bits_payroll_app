package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.InsuranceConfigurationDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link InsuranceConfiguration} and its DTO {@link InsuranceConfigurationDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface InsuranceConfigurationMapper extends EntityMapper<InsuranceConfigurationDTO, InsuranceConfiguration> {
    default InsuranceConfiguration fromId(Long id) {
        if (id == null) {
            return null;
        }
        InsuranceConfiguration insuranceConfiguration = new InsuranceConfiguration();
        insuranceConfiguration.setId(id);
        return insuranceConfiguration;
    }
}
