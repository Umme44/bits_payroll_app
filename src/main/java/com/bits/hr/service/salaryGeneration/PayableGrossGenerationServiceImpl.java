package com.bits.hr.service.salaryGeneration;

import com.bits.hr.service.UserService;
import java.time.YearMonth;
import org.springframework.stereotype.Service;

@Service
public class PayableGrossGenerationServiceImpl implements PayableGrossGenerationService {

    private final UserService userService;

    public PayableGrossGenerationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Double payableGross(Integer year, Integer month, Double mainGrossSalary, Integer fractionDays) {
        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        //double eCellBill = getExCellBillById(id);
        return mainGrossSalary / daysInMonth * fractionDays;
    }
}
