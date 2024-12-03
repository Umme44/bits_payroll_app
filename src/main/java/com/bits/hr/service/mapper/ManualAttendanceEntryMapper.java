package com.bits.hr.service.mapper;

import com.bits.hr.domain.*;
import com.bits.hr.service.dto.ManualAttendanceEntryDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link ManualAttendanceEntry} and its DTO {@link ManualAttendanceEntryDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface ManualAttendanceEntryMapper extends EntityMapper<ManualAttendanceEntryDTO, ManualAttendanceEntry> {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.pin", target = "pin")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.designation.designationName", target = "designationName")
    @Mapping(source = "employee.department.departmentName", target = "departmentName")
    @Mapping(source = "employee.unit.unitName", target = "unitName")
    ManualAttendanceEntryDTO toDto(ManualAttendanceEntry manualAttendanceEntry);

    @Mapping(source = "employeeId", target = "employee")
    ManualAttendanceEntry toEntity(ManualAttendanceEntryDTO manualAttendanceEntryDTO);

    default ManualAttendanceEntry fromId(Long id) {
        if (id == null) {
            return null;
        }
        ManualAttendanceEntry manualAttendanceEntry = new ManualAttendanceEntry();
        manualAttendanceEntry.setId(id);
        return manualAttendanceEntry;
    }
}
