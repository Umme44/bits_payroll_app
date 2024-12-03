package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.MovementEntryDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link MovementEntry} and its DTO {@link MovementEntryDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class, UserMapper.class })
public interface MovementEntryMapper extends EntityMapper<MovementEntryDTO, MovementEntry> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "employee.fullName", target = "employeeName")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    @Mapping(source = "sanctionBy.id", target = "sanctionById")
    @Mapping(source = "sanctionBy.login", target = "sanctionByLogin")
    MovementEntryDTO toDto(MovementEntry movementEntry);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    @Mapping(source = "sanctionById", target = "sanctionBy")
    MovementEntry toEntity(MovementEntryDTO movementEntryDTO);

    default MovementEntry fromId(Long id) {
        if (id == null) {
            return null;
        }
        MovementEntry movementEntry = new MovementEntry();
        movementEntry.setId(id);
        return movementEntry;
    }
}
