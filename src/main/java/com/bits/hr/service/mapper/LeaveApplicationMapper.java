package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link LeaveApplication} and its DTO {@link LeaveApplicationDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class, UserMapper.class })
public interface LeaveApplicationMapper extends EntityMapper<LeaveApplicationDTO, LeaveApplication> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    @Mapping(source = "sanctionedBy.id", target = "sanctionedById")
    @Mapping(source = "sanctionedBy.login", target = "sanctionedByLogin")
    LeaveApplicationDTO toDto(LeaveApplication leaveApplication);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "sanctionedById", target = "sanctionedBy")
    LeaveApplication toEntity(LeaveApplicationDTO leaveApplicationDTO);

    default LeaveApplication fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeaveApplication leaveApplication = new LeaveApplication();
        leaveApplication.setId(id);
        return leaveApplication;
    }
}
