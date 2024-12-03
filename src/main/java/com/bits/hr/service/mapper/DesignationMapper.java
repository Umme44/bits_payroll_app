package com.bits.hr.service.mapper;

import com.bits.hr.domain.Designation;
import com.bits.hr.service.dto.DesignationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Designation} and its DTO {@link DesignationDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface DesignationMapper extends EntityMapper<DesignationDTO, Designation> {
    default Designation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Designation designation = new Designation();
        designation.setId(id);
        return designation;
    }
}
