package com.bits.hr.service.salaryGenerationFractional;

import com.bits.hr.domain.PfLoanRepayment;
import com.bits.hr.domain.SalaryDeduction;
import com.bits.hr.repository.PfLoanRepaymentRepository;
import com.bits.hr.repository.SalaryDeductionRepository;
import com.bits.hr.service.PfLoanService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtherDeductionsService {

    @Autowired
    private SalaryDeductionRepository salaryDeductionRepository;

    @Autowired
    private PfLoanService pfLoanService;

    @Autowired
    private PfLoanRepaymentRepository pfLoanRepaymentRepository;

    public double getOtherDeductions(long employeeId, int year, int month) {
        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAllByEmployeeIdAndYearAndMonth(employeeId, year, month);
        Optional<PfLoanRepayment> pfLoanRepaymentOptional = pfLoanService.getEmployeeMonthlyRepaymentAmount(employeeId, year, month);
        boolean pfLoanInXL = false;
        double otherDeduction = 0;

        for (SalaryDeduction salaryDeduction : salaryDeductionList) {
            String deductionType = salaryDeduction.getDeductionType().getName();

            if (deductionType.contains("Pf Loan Repayments")) {
                pfLoanInXL = true;
                if (pfLoanRepaymentOptional.isPresent()) {
                    PfLoanRepayment pfLoanRepayment = pfLoanRepaymentOptional.get();
                    pfLoanRepayment.setAmount(salaryDeduction.getDeductionAmount());
                    pfLoanRepaymentRepository.save(pfLoanRepayment);
                }
                // get pf loan repayment and update from here with this value.
                // data in deduction table will have the highest priority.
                // if multiple pf loan found then TODO: clear business logic
                otherDeduction += salaryDeduction.getDeductionAmount();
            } else {
                otherDeduction += salaryDeduction.getDeductionAmount();
            }
        }

        if (pfLoanInXL == false && pfLoanRepaymentOptional.isPresent()) {
            PfLoanRepayment pfLoanRepayment = pfLoanRepaymentOptional.get();
            pfLoanRepaymentRepository.save(pfLoanRepayment);
            otherDeduction += pfLoanRepayment.getAmount();
        }

        return otherDeduction;
    }

    public String getOtherDeductionsRemarks(long employeeId, int year, int month) {
        List<SalaryDeduction> salaryDeductionList = salaryDeductionRepository.findAllByEmployeeIdAndYearAndMonth(employeeId, year, month);

        StringBuilder stringBuilder = new StringBuilder();

        double totalOtherDeductions = 0;

        for (SalaryDeduction salaryDeduction : salaryDeductionList) {
            String deductionType = salaryDeduction.getDeductionType().getName();
            double amount = salaryDeduction.getDeductionAmount();
            stringBuilder.append(deductionType + " = " + amount + ",");
            totalOtherDeductions += salaryDeduction.getDeductionAmount();
        }
        if (salaryDeductionList.size() > 0) {
            stringBuilder.append("Total" + " = " + totalOtherDeductions + "");
        }
        return stringBuilder.toString();
    }
}
