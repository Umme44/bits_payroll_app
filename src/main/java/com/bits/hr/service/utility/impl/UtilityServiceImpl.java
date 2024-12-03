package com.bits.hr.service.utility.impl;

import com.bits.hr.service.AttendanceSummaryService;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.MobileBillService;
import com.bits.hr.service.PfLoanRepaymentService;
import com.bits.hr.service.utility.UtilityService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilityServiceImpl implements UtilityService {

    private final EmployeeService employeeService;
    private final AttendanceSummaryService attendanceSummaryService;
    private final MobileBillService mobileBillService;
    private final PfLoanRepaymentService pfLoanRepaymentService;

    @Override
    public Map<String, Boolean> hasUploadedXlsx(int year, int month) {
        return null;
    }
}
