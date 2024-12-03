package com.bits.hr.service.salaryGenerationFractional;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmploymentHistory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FractionService {

    public List<Fraction> getFractions(
        Employee employee,
        LocalDate startRange,
        LocalDate endRange,
        List<EmploymentHistory> employmentHistories
    ) {
        List<Fraction> fractionList = new ArrayList<>();
        // single fraction if no employment Histories available
        if (employmentHistories.size() == 0) {
            Fraction fraction = new Fraction();
            fraction.setEffectiveGross(employee.getMainGrossSalary());
            fraction.setStartDate(startRange);
            fraction.setEndDate(endRange);
            fractionList.add(fraction);
            return fractionList;
        }
        // one increment or promotion , most as usual scenario
        else if (employmentHistories.size() == 1) {
            Fraction fraction1 = new Fraction();
            fraction1.setEffectiveGross(employmentHistories.get(0).getPreviousMainGrossSalary());
            fraction1.setStartDate(startRange);
            fraction1.setEndDate(employmentHistories.get(0).getEffectiveDate().minusDays(1));
            fractionList.add(fraction1);

            Fraction fraction2 = new Fraction();
            fraction2.setEffectiveGross(employmentHistories.get(0).getCurrentMainGrossSalary());
            fraction2.setStartDate(employmentHistories.get(0).getEffectiveDate());
            fraction2.setEndDate(endRange);
            fractionList.add(fraction2);

            return fractionList;
        }
        // multiple employment histories, unusual case but must be handled carefully if occurs.
        else if (employmentHistories.size() > 1) {
            int lastIndex = employmentHistories.size() - 1;

            Fraction fractionFirst = new Fraction();
            fractionFirst.setStartDate(startRange);
            fractionFirst.setEndDate(employmentHistories.get(0).getEffectiveDate().minusDays(1));
            fractionFirst.setEffectiveGross(employmentHistories.get(0).getPreviousMainGrossSalary());
            fractionList.add(fractionFirst);

            for (int i = 1; i <= lastIndex; i++) {
                Fraction fraction = new Fraction();
                fraction.setStartDate(employmentHistories.get(i - 1).getEffectiveDate());
                fraction.setEndDate(employmentHistories.get(i).getEffectiveDate().minusDays(1));
                fraction.setEffectiveGross(employmentHistories.get(i - 1).getCurrentMainGrossSalary());
                fractionList.add(fraction);
            }

            Fraction fractionLast = new Fraction();
            // Last employment history is in index of (size-1) , index start's from 0

            fractionLast.setStartDate(employmentHistories.get(lastIndex).getEffectiveDate());
            fractionLast.setEndDate(endRange);
            fractionLast.setEffectiveGross(employmentHistories.get(lastIndex).getCurrentMainGrossSalary());
            fractionList.add(fractionLast);
            return fractionList;
        }

        return null;
    }
}
