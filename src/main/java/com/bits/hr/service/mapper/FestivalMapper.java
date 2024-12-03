package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.EmployeeStaticFileDTO;
import com.bits.hr.service.dto.FestivalDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Festival} and its DTO {@link FestivalDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface FestivalMapper extends EntityMapper<FestivalDTO, Festival> {
    @Mapping(target = "numberOfBonus", ignore = true)
    FestivalDTO toDto(Festival festival);

    default Festival fromId(Long id) {
        if (id == null) {
            return null;
        }
        Festival festival = new Festival();
        festival.setId(id);
        return festival;
    }
}
