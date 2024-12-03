package com.bits.hr.service.report;

import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.service.report.helperObject.ShortSalarySummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortSummaryReportBuilderService {

    @Autowired
    private ShortSummaryReportService shortSummaryReportService;

    public String getReport(int year, int month) {
        String result = "";
        ShortSalarySummary shortSalarySummary = shortSummaryReportService.getSummaryReport(year, month);

        String date = Month.fromInteger(month).toString() + "-" + year;

        // header
        result += "Sl., Particulars , Calculation , Total Net Pay \n";
        // section A sub header
        result +=
            "A.,  Salary                              ,               ,                                                            \n";
        // section A
        result +=
            "1 , Regular + Probation + Contractual    ," + date + "   ," + shortSalarySummary.getTotalSalary() + "                 \n";
        result +=
            "2 , Salary Hold for Final Payment        ," + date + "   ," + shortSalarySummary.getHoldForFinalPayment() + "         \n";
        result +=
            "3 , Cash Payment                         ," + date + "   ," + shortSalarySummary.getCashPayment() + "                 \n";
        result +=
            "4 , Total Bank Transfer                  , [1 - (2 + 3)] ," + shortSalarySummary.getTotalBankTransfer() + "           \n";
        // section B sub header
        result +=
            "B.,    Allowances                                 ,            ,                                                      \n";
        result +=
            "5 ," + shortSalarySummary.getAllowance01Name() + "," + date + "," + shortSalarySummary.getTotalAllowance01() + "      \n";
        result +=
            "6 ," + shortSalarySummary.getAllowance02Name() + "," + date + "," + shortSalarySummary.getTotalAllowance02() + "      \n";
        result +=
            "7 ," + shortSalarySummary.getAllowance03Name() + "," + date + "," + shortSalarySummary.getTotalAllowance03() + "      \n";
        result +=
            "8 ," + shortSalarySummary.getAllowance04Name() + "," + date + "," + shortSalarySummary.getTotalAllowance04() + "      \n";
        result += "9 ," + " Hujur " + "," + date + "," + shortSalarySummary.getHujur() + "                 \n";
        result += "10, Sub-total : Allowances, (5 to 9) ," + shortSalarySummary.getSubTotalAllowance() + "     \n";
        result += "11, Grand Total " + date + ", (4 + 10) ," + shortSalarySummary.getGrandTotal() + "            \n ";
        result += "  , BRAC Bank Transfer " + " ,          ," + shortSalarySummary.getTotalBracBankTransfers() + "\n";
        result += "  , Other Bank Transfer (BEFTN) " + " ,          ," + shortSalarySummary.getTotalOtherBankTransfers() + "\n";

        return result;
    }
}
