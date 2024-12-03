package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.OrganizationDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Organization} and its DTO {@link OrganizationDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface OrganizationMapper extends EntityMapper<OrganizationDTO, Organization> {
    default Organization fromId(Long id) {
        if (id == null) {
            return null;
        }
        Organization organization = new Organization();
        organization.setId(id);
        return organization;
    }
}
