package com.bits.hr.service.mapper;

import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.service.dto.AttendanceEntryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link AttendanceEntry} and its DTO {@link AttendanceEntryDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface AttendanceEntryMapper extends EntityMapper<AttendanceEntryDTO, AttendanceEntry> {
    @Mapping(source = "employeeId", target = "employee")
    AttendanceEntry toEntity(AttendanceEntryDTO attendanceEntryDTO);

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    AttendanceEntryDTO toDto(AttendanceEntry attendanceEntry);

    default AttendanceEntry fromId(Long id) {
        if (id == null) {
            return null;
        }
        AttendanceEntry attendanceEntry = new AttendanceEntry();
        attendanceEntry.setId(id);
        return attendanceEntry;
    }
}
