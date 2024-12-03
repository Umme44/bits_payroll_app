package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.ReferencesDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link References} and its DTO {@link ReferencesDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface ReferencesMapper extends EntityMapper<ReferencesDTO, References> {
    @Mapping(source = "employee.id", target = "employeeId")
    ReferencesDTO toDto(References references);

    @Mapping(source = "employeeId", target = "employee")
    References toEntity(ReferencesDTO referencesDTO);

    default References fromId(Long id) {
        if (id == null) {
            return null;
        }
        References references = new References();
        references.setId(id);
        return references;
    }
}
