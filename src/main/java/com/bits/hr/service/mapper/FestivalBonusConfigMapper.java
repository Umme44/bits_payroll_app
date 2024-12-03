package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.FestivalBonusConfigDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link FestivalBonusConfig} and its DTO {@link FestivalBonusConfigDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface FestivalBonusConfigMapper extends EntityMapper<FestivalBonusConfigDTO, FestivalBonusConfig> {
    default FestivalBonusConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        FestivalBonusConfig festivalBonusConfig = new FestivalBonusConfig();
        festivalBonusConfig.setId(id);
        return festivalBonusConfig;
    }
}
