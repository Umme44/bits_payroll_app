package com.bits.hr.service.mapper;

import com.bits.hr.domain.WorkingExperience;
import com.bits.hr.service.dto.WorkingExperienceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link WorkingExperience} and its DTO {@link WorkingExperienceDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface WorkingExperienceMapper extends EntityMapper<WorkingExperienceDTO, WorkingExperience> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "employeeName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    WorkingExperienceDTO toDto(WorkingExperience workingExperience);

    @Mapping(source = "employeeId", target = "employee")
    WorkingExperience toEntity(WorkingExperienceDTO workingExperienceDTO);

    default WorkingExperience fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkingExperience workingExperience = new WorkingExperience();
        workingExperience.setId(id);
        return workingExperience;
    }
}
