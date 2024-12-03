package com.bits.hr.service.mapper;

import com.bits.hr.domain.Nationality;
import com.bits.hr.service.dto.NationalityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Nationality} and its DTO {@link NationalityDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface NationalityMapper extends EntityMapper<NationalityDTO, Nationality> {
    default Nationality fromId(Long id) {
        if (id == null) {
            return null;
        }
        Nationality nationality = new Nationality();
        nationality.setId(id);
        return nationality;
    }
}
