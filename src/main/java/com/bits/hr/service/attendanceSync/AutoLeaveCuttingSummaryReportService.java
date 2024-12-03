package com.bits.hr.service.attendanceSync;

import com.bits.hr.service.attendanceSync.helperObjects.LeaveCutCSV;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.util.GenericCSV.ObjectToCSVGenerator;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AutoLeaveCuttingSummaryReportService {

    @Autowired
    private AutoLeaveCuttingSummaryService autoLeaveCuttingSummaryService;

    @Autowired
    private ObjectToCSVGenerator objectToCSVGenerator;

    public Optional<File> generateCSV(LocalDate startDate, LocalDate endDate) throws InvocationTargetException, IllegalAccessException {
        try {
            List<LeaveApplicationDTO> leaveApplicationDTOList = autoLeaveCuttingSummaryService.GenerateSummary(startDate, endDate);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

            List<LeaveCutCSV> leaveCutCSVList = new ArrayList<>();
            for (LeaveApplicationDTO leaveApplicationDTO : leaveApplicationDTOList) {
                LeaveCutCSV leaveCutCSV = new LeaveCutCSV(
                    leaveApplicationDTO.getPin(),
                    leaveApplicationDTO.getFullName(),
                    leaveApplicationDTO.getDesignationName(),
                    leaveApplicationDTO.getDepartmentName(),
                    leaveApplicationDTO.getUnitName(),
                    leaveApplicationDTO.getLeaveType().name(),
                    leaveApplicationDTO.getStartDate().format(formatter),
                    leaveApplicationDTO.getEndDate().format(formatter),
                    leaveApplicationDTO.getDurationInDay()
                );
                leaveCutCSVList.add(leaveCutCSV);
            }

            return Optional.of(objectToCSVGenerator.generateCSV(leaveCutCSVList.toArray()));
        } catch (Exception ex) {
            log.error("Failed To Generate Reports");
            log.error(ex);
            return Optional.empty();
        }
    }
}
