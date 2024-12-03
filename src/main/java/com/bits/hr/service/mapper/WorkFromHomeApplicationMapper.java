package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.WorkFromHomeApplicationDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link WorkFromHomeApplication} and its DTO {@link WorkFromHomeApplicationDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserMapper.class, EmployeeMapper.class })
public interface WorkFromHomeApplicationMapper extends EntityMapper<WorkFromHomeApplicationDTO, WorkFromHomeApplication> {
    @Mapping(source = "appliedBy.id", target = "appliedById")
    @Mapping(source = "appliedBy.login", target = "appliedByLogin")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    @Mapping(source = "updatedBy.login", target = "updatedByLogin")
    @Mapping(source = "sanctionedBy.id", target = "sanctionedById")
    @Mapping(source = "sanctionedBy.login", target = "sanctionedByLogin")
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    @Mapping(source = "employee.band.bandName", target = "bandName")
    @Mapping(source = "employee.isAllowedToGiveOnlineAttendance", target = "isAllowedToGiveOnlineAttendance")
    WorkFromHomeApplicationDTO toDto(WorkFromHomeApplication workFromHomeApplication);

    @Mapping(source = "appliedById", target = "appliedBy")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    @Mapping(source = "sanctionedById", target = "sanctionedBy")
    @Mapping(source = "employeeId", target = "employee")
    WorkFromHomeApplication toEntity(WorkFromHomeApplicationDTO workFromHomeApplicationDTO);

    default WorkFromHomeApplication fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkFromHomeApplication workFromHomeApplication = new WorkFromHomeApplication();
        workFromHomeApplication.setId(id);
        return workFromHomeApplication;
    }
}
