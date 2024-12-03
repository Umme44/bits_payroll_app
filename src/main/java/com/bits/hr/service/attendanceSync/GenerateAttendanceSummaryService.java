package com.bits.hr.service.attendanceSync;

import com.bits.hr.domain.SalaryGeneratorMaster;
import com.bits.hr.repository.SalaryGeneratorMasterRepository;
import com.bits.hr.service.attendanceSync.helperService.AttendanceSummaryGenerationService;
import com.bits.hr.service.attendanceSync.helperService.SaveAttendanceSummaryService;
import com.bits.hr.service.config.DTO.AttendanceRegularizationMethod;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.service.dto.AttendanceSummaryDTO;
import com.bits.hr.util.SalaryGenerationMasterService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GenerateAttendanceSummaryService {

    @Autowired
    private SaveAttendanceSummaryService saveAttendanceSummaryService;

    @Autowired
    private AttendanceSummaryGenerationService attendanceSummaryGenerationService;

    @Autowired
    private GetConfigValueByKeyService getConfigValueByKeyService;

    @Autowired
    private SalaryGenerationMasterService salaryGenerationMasterService;

    @Autowired
    private SalaryGeneratorMasterRepository salaryGeneratorMasterRepository;

    public boolean generateAndSave(int year, int month) {
        AttendanceRegularizationMethod attendanceRegularizationMethod = getConfigValueByKeyService.getAttendanceRegularizationMethod();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());
        if (attendanceRegularizationMethod == AttendanceRegularizationMethod.FIXED_DAY) {
            int fixedDay = getConfigValueByKeyService.getMonthlyAttendanceRegularisationDay();
            // 21 to 20
            endDate = LocalDate.of(year, month, fixedDay);
            startDate = endDate.minusMonths(1).plusDays(1);
        }
        List<AttendanceSummaryDTO> attendanceSummaryDTOList = attendanceSummaryGenerationService.generate(startDate, endDate);

        boolean isSaved = saveAttendanceSummaryService.save(attendanceSummaryDTOList);

        if (isSaved) {
            // create salary generation master entry that attendance summary imported / auto generated
            SalaryGeneratorMaster salaryGeneratorMaster = salaryGenerationMasterService.getOrCreateSalaryGeneratorMaster(year, month);
            salaryGeneratorMaster.setIsAttendanceImported(true);
            salaryGeneratorMasterRepository.save(salaryGeneratorMaster);
        }
        return isSaved;
    }
}
