package com.bits.hr.service.mapper;

import com.bits.hr.domain.AitConfig;
import com.bits.hr.service.dto.AitConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link AitConfig} and its DTO {@link AitConfigDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface AitConfigMapper extends EntityMapper<AitConfigDTO, AitConfig> {
    default AitConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        AitConfig aitConfig = new AitConfig();
        aitConfig.setId(id);
        return aitConfig;
    }
}
