package com.bits.hr.service.dto;

import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for Team member Attendance List
 */

@Getter
@Setter
public class MyTeamMemberAttendancesDTO {

    EmployeeMinimalDTO employee;

    List<AttendanceTimeSheetDTO> attendances;
}
