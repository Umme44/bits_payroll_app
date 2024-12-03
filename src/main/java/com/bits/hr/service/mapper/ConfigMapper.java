package com.bits.hr.service.mapper;

import com.bits.hr.domain.Config;
import com.bits.hr.service.dto.ConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Config} and its DTO {@link ConfigDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface ConfigMapper extends EntityMapper<ConfigDTO, Config> {
    default Config fromId(Long id) {
        if (id == null) {
            return null;
        }
        Config config = new Config();
        config.setId(id);
        return config;
    }
}
