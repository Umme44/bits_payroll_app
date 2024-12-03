package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.OfficeNoticesDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link OfficeNotices} and its DTO {@link OfficeNoticesDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface OfficeNoticesMapper extends EntityMapper<OfficeNoticesDTO, OfficeNotices> {
    default OfficeNotices fromId(Long id) {
        if (id == null) {
            return null;
        }
        OfficeNotices officeNotices = new OfficeNotices();
        officeNotices.setId(id);
        return officeNotices;
    }
}
