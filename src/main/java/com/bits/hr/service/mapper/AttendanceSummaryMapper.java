package com.bits.hr.service.mapper;

import com.bits.hr.domain.AttendanceSummary;
import com.bits.hr.service.dto.AttendanceSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link AttendanceSummary} and its DTO {@link AttendanceSummaryDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface AttendanceSummaryMapper extends EntityMapper<AttendanceSummaryDTO, AttendanceSummary> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    AttendanceSummaryDTO toDto(AttendanceSummary attendanceSummary);

    @Mapping(source = "employeeId", target = "employee")
    AttendanceSummary toEntity(AttendanceSummaryDTO attendanceSummaryDTO);

    default AttendanceSummary fromId(Long id) {
        if (id == null) {
            return null;
        }
        AttendanceSummary attendanceSummary = new AttendanceSummary();
        attendanceSummary.setId(id);
        return attendanceSummary;
    }
}
