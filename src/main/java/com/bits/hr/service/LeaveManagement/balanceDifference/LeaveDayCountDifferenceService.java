package com.bits.hr.service.LeaveManagement.balanceDifference;

import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.service.LeaveManagement.LeaveDaysCount.LeaveDaysCalculationService;
import com.bits.hr.service.mapper.LeaveApplicationMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LeaveDayCountDifferenceService {

    @Autowired
    private LeaveDaysCalculationService leaveDaysCalculationService;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    @Lazy
    private LeaveApplicationMapper leaveApplicationMapper;

    public List<LeaveDayCountDiffObj> getDifference(LocalDate startDate, LocalDate endDate) {
        List<LeaveDayCountDiffObj> result = new ArrayList<>();

        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findLeaveApplicationBetweenDates(startDate, endDate);

        for (LeaveApplication leaveApplication : leaveApplicationList) {
            int existingCalculatedDuration = leaveApplication.getDurationInDay() != null ? leaveApplication.getDurationInDay() : 0;
            int newCalculatedDuration = leaveDaysCalculationService.leaveDaysCalculation(
                leaveApplication.getStartDate(),
                leaveApplication.getEndDate()
            );

            if (existingCalculatedDuration != newCalculatedDuration) {
                LeaveDayCountDiffObj leaveDayCountDiffObj = new LeaveDayCountDiffObj();
                leaveDayCountDiffObj.setLeaveApplication(leaveApplicationMapper.toDto(leaveApplication));
                leaveDayCountDiffObj.setNewCalculatedDurationInDays(newCalculatedDuration);
                result.add(leaveDayCountDiffObj);
            }
        }
        return result;
    }

    public boolean correctDurationInDays(LocalDate startDate, LocalDate endDate) {
        try {
            List<LeaveDayCountDiffObj> balanceDifferenceObjectList = getDifference(startDate, endDate);
            for (LeaveDayCountDiffObj leaveDayCountDiffObj : balanceDifferenceObjectList) {
                leaveDayCountDiffObj.getLeaveApplication().setDurationInDay(leaveDayCountDiffObj.getNewCalculatedDurationInDays());
                leaveApplicationRepository.save(leaveApplicationMapper.toEntity(leaveDayCountDiffObj.getLeaveApplication()));
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean correctSingle(Long leaveApplicationId) {
        Optional<LeaveApplication> leaveApplicationOptional = leaveApplicationRepository.findById(leaveApplicationId);
        if (!leaveApplicationOptional.isPresent()) {
            return false;
        } else {
            LeaveApplication leaveApplication = leaveApplicationOptional.get();
            int newCalculatedDays = leaveDaysCalculationService.leaveDaysCalculation(
                leaveApplication.getStartDate(),
                leaveApplication.getEndDate()
            );
            if (leaveApplication.getDurationInDay() != newCalculatedDays) {
                leaveApplication.setDurationInDay(newCalculatedDays);
                leaveApplicationRepository.save(leaveApplication);
                return true;
            } else {
                return false;
            }
        }
    }
}
