package com.bits.hr.service.attendanceSync.helperService;

import com.bits.hr.domain.AttendanceSummary;
import com.bits.hr.repository.AttendanceSummaryRepository;
import com.bits.hr.service.AttendanceSummaryService;
import com.bits.hr.service.dto.AttendanceSummaryDTO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SaveAttendanceSummaryService {

    private final AttendanceSummaryRepository attendanceSummaryRepository;

    private final AttendanceSummaryService attendanceSummaryService;

    public SaveAttendanceSummaryService(
        AttendanceSummaryRepository attendanceSummaryRepository,
        AttendanceSummaryService attendanceSummaryService
    ) {
        this.attendanceSummaryRepository = attendanceSummaryRepository;
        this.attendanceSummaryService = attendanceSummaryService;
    }

    public boolean save(List<AttendanceSummaryDTO> attendanceSummaryDTOList) {
        try {
            for (AttendanceSummaryDTO attendanceSummaryDTO : attendanceSummaryDTOList) {
                List<AttendanceSummary> summaryList = attendanceSummaryRepository.findByYearAndMonthAndAndEmployeeId(
                    attendanceSummaryDTO.getEmployeeId(),
                    attendanceSummaryDTO.getYear(),
                    attendanceSummaryDTO.getMonth()
                );
                if (summaryList.isEmpty()) {
                    // save
                    attendanceSummaryService.save(attendanceSummaryDTO);
                } else {
                    // delete and save new
                    for (AttendanceSummary attendanceSummary : summaryList) {
                        attendanceSummaryService.delete(attendanceSummary.getId());
                    }
                    attendanceSummaryService.save(attendanceSummaryDTO);
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
