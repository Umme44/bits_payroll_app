package com.bits.hr.service.attendanceSync.helperService;

import com.bits.hr.service.attendanceTimeSheet.MonthlyAttendanceTimeSheetDTO;
import com.bits.hr.service.attendanceTimeSheet.MonthlyAttendanceTimeSheetGenerationService;
import com.bits.hr.service.dto.AttendanceSummaryDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AttendanceSummaryGenerationService {

    private final ATSGenForAttendanceSummaryService atsGenForAttendanceSummaryService;

    private final LeaveSummaryProcessingService leaveSummaryProcessingService;

    private final MonthlyAttendanceTimeSheetGenerationService monthlyAttendanceTimeSheetGenerationService;

    public AttendanceSummaryGenerationService(
        ATSGenForAttendanceSummaryService atsGenForAttendanceSummaryService,
        LeaveSummaryProcessingService leaveSummaryProcessingService,
        MonthlyAttendanceTimeSheetGenerationService monthlyAttendanceTimeSheetGenerationService
    ) {
        this.atsGenForAttendanceSummaryService = atsGenForAttendanceSummaryService;
        this.leaveSummaryProcessingService = leaveSummaryProcessingService;
        this.monthlyAttendanceTimeSheetGenerationService = monthlyAttendanceTimeSheetGenerationService;
    }

    public List<AttendanceSummaryDTO> generate(LocalDate startDate, LocalDate endDate) {
        List<MonthlyAttendanceTimeSheetDTO> monthlyAttendanceTimeSheetDTOList = monthlyAttendanceTimeSheetGenerationService.getAllEmployeeATS(
            startDate,
            endDate
        );
        //            = atsGenForAttendanceSummaryService
        //            .getATS(startDate, endDate);

        int month = endDate.getMonth().getValue();
        int year = endDate.getYear();

        List<AttendanceSummaryDTO> attendanceSummaryDTOList = new ArrayList<>();

        for (MonthlyAttendanceTimeSheetDTO monthlyAttendanceTimeSheetDTO : monthlyAttendanceTimeSheetDTOList) {
            attendanceSummaryDTOList.add(
                leaveSummaryProcessingService.processDTO(monthlyAttendanceTimeSheetDTO, month, year, startDate, endDate)
            );
        }
        return attendanceSummaryDTOList;
    }
}
