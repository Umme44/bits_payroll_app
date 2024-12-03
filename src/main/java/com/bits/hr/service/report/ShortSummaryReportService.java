package com.bits.hr.service.report;

import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.service.report.helperObject.ShortSalarySummary;
import com.bits.hr.util.ObjectConversationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortSummaryReportService {

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    private GetConfigValueByKeyService getConfigValueByKeyService;

    public ShortSalarySummary getSummaryReport(int year, int month) {
        ShortSalarySummary shortSalarySummary = new ShortSalarySummary();

        // total
        shortSalarySummary.setTotalSalary(employeeSalaryRepository.getSumOfTotalSalary(year, Month.fromInteger(month).name()));
        // hold
        shortSalarySummary.setHoldForFinalPayment(employeeSalaryRepository.getSumOfHoldAmount(year, Month.fromInteger(month).name()));
        // cash
        shortSalarySummary.setHoldForFinalPayment(employeeSalaryRepository.getSumOfCashPayment(year, Month.fromInteger(month).name()));

        // section B
        // allowance 01
        shortSalarySummary.setTotalAllowance01(employeeSalaryRepository.getSumOfAllowance01(year, Month.fromInteger(month).name()));

        shortSalarySummary.setTotalAllowance02(employeeSalaryRepository.getSumOfAllowance02(year, Month.fromInteger(month).name()));
        shortSalarySummary.setTotalAllowance03(employeeSalaryRepository.getSumOfAllowance03(year, Month.fromInteger(month).name()));
        shortSalarySummary.setTotalAllowance04(employeeSalaryRepository.getSumOfAllowance04(year, Month.fromInteger(month).name()));
        shortSalarySummary.setTotalAllowance05(
            ObjectConversationUtil.asDouble(employeeSalaryRepository.getSumOfAllowance05(year, Month.fromInteger(month).name()))
        );
        shortSalarySummary.setTotalAllowance06(employeeSalaryRepository.getSumOfAllowance06(year, Month.fromInteger(month).name()));

        shortSalarySummary.setAllowance01Name(getConfigValueByKeyService.getAllowance01Name());
        shortSalarySummary.setAllowance02Name(getConfigValueByKeyService.getAllowance02Name());
        shortSalarySummary.setAllowance03Name(getConfigValueByKeyService.getAllowance03Name());
        shortSalarySummary.setAllowance04Name(getConfigValueByKeyService.getAllowance04Name());
        shortSalarySummary.setAllowance05Name(getConfigValueByKeyService.getAllowance05Name());
        shortSalarySummary.setAllowance06Name(getConfigValueByKeyService.getAllowance06Name());

        shortSalarySummary.setHujur(getConfigValueByKeyService.getHujurHadia());

        shortSalarySummary.setTotalBracBankTransfers(
            employeeSalaryRepository.getTotalBracBankTransfers(year, Month.fromInteger(month).name())
        );

        shortSalarySummary.setTotalOtherBankTransfers(
            employeeSalaryRepository.getTotalOtherBankTransfers(year, Month.fromInteger(month).name())
        );
        return shortSalarySummary;
    }
}
