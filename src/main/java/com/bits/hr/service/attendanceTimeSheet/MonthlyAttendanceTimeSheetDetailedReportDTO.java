package com.bits.hr.service.attendanceTimeSheet;

import java.util.List;
import lombok.Data;

@Data
public class MonthlyAttendanceTimeSheetDetailedReportDTO {

    private String pin;
    private String name;
    private String departmentName;
    private String designationName;
    private String unitName;
    private List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList;
}
