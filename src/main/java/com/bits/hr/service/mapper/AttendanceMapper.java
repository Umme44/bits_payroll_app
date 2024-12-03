package com.bits.hr.service.mapper;

import com.bits.hr.domain.Attendance;
import com.bits.hr.service.dto.AttendanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Attendance} and its DTO {@link AttendanceDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { EmployeeMapper.class })
public interface AttendanceMapper extends EntityMapper<AttendanceDTO, Attendance> {
    @Mapping(source = "employee.id", target = "employeeId")
    AttendanceDTO toDto(Attendance attendance);

    @Mapping(source = "employeeId", target = "employee")
    Attendance toEntity(AttendanceDTO attendanceDTO);

    default Attendance fromId(Long id) {
        if (id == null) {
            return null;
        }
        Attendance attendance = new Attendance();
        attendance.setId(id);
        return attendance;
    }
}
