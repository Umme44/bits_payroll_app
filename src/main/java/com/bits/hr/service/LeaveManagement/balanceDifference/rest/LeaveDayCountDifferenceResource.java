package com.bits.hr.service.LeaveManagement.balanceDifference.rest;

import com.bits.hr.service.LeaveManagement.balanceDifference.LeaveDayCountDiffObj;
import com.bits.hr.service.LeaveManagement.balanceDifference.LeaveDayCountDifferenceService;
import com.bits.hr.service.dto.DateRangeDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance-mgt")
public class LeaveDayCountDifferenceResource {

    @Autowired
    private LeaveDayCountDifferenceService leaveDayCountDifferenceService;

    @PostMapping("/leave-application-difference")
    public ResponseEntity<List<LeaveDayCountDiffObj>> getDifference(@RequestBody DateRangeDTO dateRangeDTO) {
        List<LeaveDayCountDiffObj> result = leaveDayCountDifferenceService.getDifference(
            dateRangeDTO.getStartDate(),
            dateRangeDTO.getEndDate()
        );
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/leave-application-difference/correction-all")
    public ResponseEntity<Boolean> correctDurationInDays(@RequestBody DateRangeDTO dateRangeDTO) {
        boolean result = leaveDayCountDifferenceService.correctDurationInDays(dateRangeDTO.getStartDate(), dateRangeDTO.getEndDate());
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/leave-application-difference/correction/{leaveApplicationId}")
    public ResponseEntity<Boolean> correctSingle(@PathVariable Long leaveApplicationId) {
        boolean result = leaveDayCountDifferenceService.correctSingle(leaveApplicationId);
        return ResponseEntity.ok().body(result);
    }
}
