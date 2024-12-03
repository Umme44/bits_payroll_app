package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.IncomeTaxChallanDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link IncomeTaxChallan} and its DTO {@link IncomeTaxChallanDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { AitConfigMapper.class })
public interface IncomeTaxChallanMapper extends EntityMapper<IncomeTaxChallanDTO, IncomeTaxChallan> {
    @Mapping(source = "aitConfig.id", target = "aitConfigId")
    @Mapping(source = "aitConfig.startDate", target = "startDate")
    @Mapping(source = "aitConfig.endDate", target = "endDate")
    IncomeTaxChallanDTO toDto(IncomeTaxChallan incomeTaxChallan);

    @Mapping(source = "aitConfigId", target = "aitConfig")
    IncomeTaxChallan toEntity(IncomeTaxChallanDTO incomeTaxChallanDTO);

    default IncomeTaxChallan fromId(Long id) {
        if (id == null) {
            return null;
        }
        IncomeTaxChallan incomeTaxChallan = new IncomeTaxChallan();
        incomeTaxChallan.setId(id);
        return incomeTaxChallan;
    }
}
