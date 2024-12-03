package com.bits.hr.service.finalSettlement.helperMethods;

import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfCollection;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.service.finalSettlement.dto.pf.MonthlyContribution;
import com.bits.hr.service.finalSettlement.dto.pf.YearlyPfCollection;
import com.bits.hr.service.finalSettlement.dto.pf.YearlyTotalContribution;
import com.bits.hr.util.MathRoundUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class YearlyPfCollectionService {

    @Autowired
    PfCollectionRepository pfCollectionRepository;

    @Autowired
    SalaryToPfCollectionMapperService salaryToPfCollectionMapperService;

    List<YearlyPfCollection> get(PfAccount pfAccount) {
        List<YearlyPfCollection> yearlyPfCollectionList = new ArrayList<>();

        // map pf collection with generated salaries if multiple pf account disabled.
        salaryToPfCollectionMapperService.map(pfAccount);

        // get list of years
        List<Integer> yearList = pfCollectionRepository.getListOfYears(pfAccount.getId());

        // get pf collections yearly basis

        for (int year : yearList) {
            YearlyPfCollection yearlyPfCollection = new YearlyPfCollection();

            yearlyPfCollection.setYear(year);

            ArrayList<PfCollection> pfCollectionList = new ArrayList<PfCollection>(
                pfCollectionRepository.getPfCollectionByYear(pfAccount.getId(), year)
            );
            yearlyPfCollection.setMonthlyContributionList(mapToMonthlyContribution(pfCollectionList));

            yearlyPfCollection.setYearlyTotalContribution(getTotal(pfCollectionList));
            yearlyPfCollectionList.add(yearlyPfCollection);
        }
        return yearlyPfCollectionList;
    }

    YearlyTotalContribution getTotal(List<PfCollection> pfCollectionList) {
        YearlyTotalContribution total = new YearlyTotalContribution();
        double totalEmployeeContribution = pfCollectionList.stream().mapToDouble(PfCollection::getEmployeeContribution).sum();
        double totalEmployerContribution = pfCollectionList.stream().mapToDouble(PfCollection::getEmployerContribution).sum();

        total.setTotalEmployeeContributionInYear(totalEmployeeContribution);
        total.setTotalEmployerContributionInYear(totalEmployerContribution);

        return total;
    }

    MonthlyContribution mapToMonthlyContribution(PfCollection pfCollection) {
        MonthlyContribution monthlyContribution = new MonthlyContribution();
        monthlyContribution.setMonth(Month.fromInteger(pfCollection.getMonth()).toString());
        monthlyContribution.setEmployeeContribution(pfCollection.getEmployeeContribution());
        monthlyContribution.setEmployerContribution(pfCollection.getEmployerContribution());

        double basic = MathRoundUtil.round(pfCollection.getEmployeeContribution() * 10d);
        double gross = MathRoundUtil.round(pfCollection.getEmployeeContribution() * 10d * (100d / 60d));

        monthlyContribution.setBasic(basic);
        monthlyContribution.setGrossSalary(gross);
        return monthlyContribution;
    }

    ArrayList<MonthlyContribution> mapToMonthlyContribution(List<PfCollection> pfCollectionList) {
        ArrayList<MonthlyContribution> monthlyContributionList = new ArrayList<>();
        for (PfCollection pfCollection : pfCollectionList) {
            monthlyContributionList.add(mapToMonthlyContribution(pfCollection));
        }
        return monthlyContributionList;
    }
}
