package com.bits.hr.service.salaryGenerationFractional;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmploymentHistory;
import com.bits.hr.domain.enumeration.EventType;
import com.bits.hr.repository.EmploymentHistoryRepository;
import com.bits.hr.repository.MobileBillRepository;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcessCellBillService {

    @Autowired
    private MobileBillRepository mobileBillRepository;

    @Autowired
    private EmploymentHistoryRepository employmentHistoryRepository;

    public double getExcessCellBillDeduction(Employee employee, int year, int month) {
        try {
            double mobileLimit = 0d;
            double mobileSpent = 0d;
            if (!mobileBillRepository.findByYearAndMonthAndAndEmployeePin(year, month, employee.getPin()).isEmpty()) {
                mobileSpent = mobileBillRepository.findByYearAndMonthAndAndEmployeePin(year, month, employee.getPin()).get(0).getAmount();
            }
            // if effective promotion / increment in this month
            // take first promotion / increment entry
            // get previous band and changed band
            // if ( current band mobile celling and employee mobile celling match )
            //      take mobile limit from previous band
            // else
            //      specified mobile celling in employee profile will have higher priority

            LocalDate monthStart = LocalDate.of(year, month, 01);
            LocalDate monthEnd = LocalDate.of(year, month, monthStart.lengthOfMonth());

            List<EmploymentHistory> employmentHistoryList = employmentHistoryRepository
                .findAllByEmployeeAndEffectiveDateBetween(employee, monthStart, monthEnd)
                .stream()
                .filter(x -> x.getEventType() == EventType.INCREMENT || x.getEventType() == EventType.PROMOTION)
                .collect(Collectors.toList());

            if (employmentHistoryList.size() > 0) {
                EmploymentHistory employmentHistory = employmentHistoryList.get(0);
                // if mobile celling are set exactly as band then taking consideration of previous band value
                // else mentioned value in employee will take place
                if (
                    employee.getMobileCelling() != null &&
                    employmentHistory.getPreviousBand() != null &&
                    employmentHistory.getChangedBand() != null &&
                    employmentHistory.getPreviousBand().getMobileCelling() != null &&
                    employmentHistory.getChangedBand().getMobileCelling() != null &&
                    employee.getMobileCelling().doubleValue() == employmentHistory.getChangedBand().getMobileCelling()
                ) {
                    mobileLimit = employmentHistory.getPreviousBand().getMobileCelling();
                } else {
                    mobileLimit = employee.getMobileCelling();
                }
            } else {
                if (employee.getMobileCelling() != null) {
                    mobileLimit = employee.getMobileCelling();
                }
            }
            if (mobileLimit >= mobileSpent) {
                return 0.0;
            } else {
                return MathRoundUtil.round(mobileSpent - mobileLimit);
            }
        } catch (Exception ex) {
            return 0d;
        }
    }
}
