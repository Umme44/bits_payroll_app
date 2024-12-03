package com.bits.hr.service.attendanceTimeSheet;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthlyAttendanceTimeSheetListDTO {

    public List<MonthlyAttendanceTimeSheetDTO> monthlyAttendanceTimeSheetList;

    public MonthlyAttendanceTimeSheetListDTO() {}

    public MonthlyAttendanceTimeSheetListDTO(List<MonthlyAttendanceTimeSheetDTO> monthlyAttendanceTimeSheetDTOList) {
        this.monthlyAttendanceTimeSheetList = monthlyAttendanceTimeSheetDTOList;
    }
}
