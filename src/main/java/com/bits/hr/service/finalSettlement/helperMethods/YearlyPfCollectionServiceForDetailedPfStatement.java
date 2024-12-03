package com.bits.hr.service.finalSettlement.helperMethods;

import com.bits.hr.domain.IndividualArrearSalary;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfCollection;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.IndividualArrearSalaryRepository;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.service.finalSettlement.dto.pf.*;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YearlyPfCollectionServiceForDetailedPfStatement {

    @Autowired
    private PfCollectionRepository pfCollectionRepository;

    @Autowired
    private IndividualArrearSalaryRepository individualArrearSalaryRepository;

    @Autowired
    private SalaryToPfCollectionMapperService salaryToPfCollectionMapperService;

    List<YearlyPfCollectionForDetailedPfStatement> get(
        PfAccount pfAccount,
        int startingYear,
        int startingMonth,
        int endingYear,
        int endingMonth
    ) {
        List<YearlyPfCollectionForDetailedPfStatement> yearlyPfCollectionList = new ArrayList<>();

        for (int i = startingYear; i <= endingYear; i++) {
            YearlyPfCollectionForDetailedPfStatement yearlyPfCollection = new YearlyPfCollectionForDetailedPfStatement();

            yearlyPfCollection.setYear(i);

            ArrayList<PfCollection> pfCollectionList = new ArrayList<>();
            HashMap<String, List<IndividualArrearSalary>> arrearSalaryListListHashMap = new HashMap<>();

            if (startingYear == endingYear) {
                pfCollectionList = getPfCollectionBetweenYearAndMonth(pfAccount.getId(), i, startingMonth, endingMonth);
                arrearSalaryListListHashMap =
                    getIndividualArrearSalaryBetweenYearAndMonth(pfAccount.getPin(), i, startingMonth, endingMonth);
            } else if (i == startingYear) {
                pfCollectionList = getPfCollectionBetweenYearAndMonth(pfAccount.getId(), i, startingMonth, 0);
                arrearSalaryListListHashMap = getIndividualArrearSalaryBetweenYearAndMonth(pfAccount.getPin(), i, startingMonth, 0);
            } else if (i == endingYear) {
                pfCollectionList = getPfCollectionBetweenYearAndMonth(pfAccount.getId(), i, 0, endingMonth);
                arrearSalaryListListHashMap = getIndividualArrearSalaryBetweenYearAndMonth(pfAccount.getPin(), i, 0, endingMonth);
            } else {
                pfCollectionList = getPfCollectionBetweenYearAndMonth(pfAccount.getId(), i, 0, 0);
                arrearSalaryListListHashMap = getIndividualArrearSalaryBetweenYearAndMonth(pfAccount.getPin(), i, 0, 0);
            }

            yearlyPfCollection.setMonthlyContributionList(mapToMonthlyContribution(pfCollectionList, arrearSalaryListListHashMap));

            yearlyPfCollection.setYearlyTotalContribution(getYearlyTotalContribution(pfCollectionList));
            yearlyPfCollection.setYearlyTotalArrearPfDeduction(getYearlyTotalArrearPfDeduction(arrearSalaryListListHashMap));
            yearlyPfCollection.setYearlyTotalInterest(getYearlyTotalInterest(pfCollectionList));
            yearlyPfCollectionList.add(yearlyPfCollection);
        }
        return yearlyPfCollectionList;
    }

    private ArrayList<PfCollection> getPfCollectionBetweenYearAndMonth(Long pfAccountId, int year, int startingMonth, int endingMonth) {
        return new ArrayList<PfCollection>(
            pfCollectionRepository.getPfCollectionBetweenYearAndMonth(pfAccountId, year, startingMonth, endingMonth)
        );
    }

    private HashMap<String, List<IndividualArrearSalary>> getIndividualArrearSalaryBetweenYearAndMonth(
        String pin,
        int year,
        int startingMonth,
        int endingMonth
    ) {
        HashMap<String, List<IndividualArrearSalary>> individualArrearSalaryHashMap = new HashMap<>();

        LocalDate startDate;
        LocalDate endDate;

        if (startingMonth == 0 && endingMonth == 0) {
            startDate = LocalDate.of(year, 1, 1);
            endDate = LocalDate.of(year, 12, 31);
        } else if (startingMonth == 0) {
            startDate = LocalDate.of(year, 1, 1);
            endDate = LocalDate.of(year, endingMonth, 1).withDayOfMonth(1).minusDays(1);
        } else if (endingMonth == 0) {
            startDate = LocalDate.of(year, startingMonth, 1);
            endDate = LocalDate.of(year, 12, 31);
        } else {
            startDate = LocalDate.of(year, startingMonth, 1);
            endDate = LocalDate.of(year, endingMonth, 1).withDayOfMonth(1).minusDays(1);
        }

        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.getAllArrearSalaryByPinAndDateRange(
            pin,
            startDate,
            endDate
        );

        for (IndividualArrearSalary individualArrearSalary : individualArrearSalaryList) {
            String key =
                individualArrearSalary.getEffectiveDate().getMonthValue() + "-" + individualArrearSalary.getEffectiveDate().getYear();

            if (individualArrearSalaryHashMap.get(key) != null) {
                List<IndividualArrearSalary> existingList = individualArrearSalaryHashMap.get(key);
                existingList.add(individualArrearSalary);
            } else {
                List<IndividualArrearSalary> newList = new ArrayList<>();
                newList.add(individualArrearSalary);
                individualArrearSalaryHashMap.put(key, newList);
            }
        }

        return individualArrearSalaryHashMap;
    }

    YearlyTotalContribution getYearlyTotalContribution(List<PfCollection> pfCollectionList) {
        YearlyTotalContribution total = new YearlyTotalContribution();
        double totalEmployeeContribution = pfCollectionList.stream().mapToDouble(PfCollection::getEmployeeContribution).sum();
        double totalEmployerContribution = pfCollectionList.stream().mapToDouble(PfCollection::getEmployerContribution).sum();

        total.setTotalEmployeeContributionInYear(totalEmployeeContribution);
        total.setTotalEmployerContributionInYear(totalEmployerContribution);

        return total;
    }

    YearlyTotalArrearPfDeduction getYearlyTotalArrearPfDeduction(
        HashMap<String, List<IndividualArrearSalary>> individualArrearSalaryHashMap
    ) {
        YearlyTotalArrearPfDeduction total = new YearlyTotalArrearPfDeduction();

        double totalEmployeeArrearPfDeduction = individualArrearSalaryHashMap
            .values()
            .stream()
            .flatMap(List::stream)
            .mapToDouble(IndividualArrearSalary::getArrearPfDeduction)
            .sum();
        double totalEmployerArrearPfDeduction = totalEmployeeArrearPfDeduction;

        total.setTotalEmployeeArrearPfDeduction(totalEmployeeArrearPfDeduction);
        total.setTotalEmployerArrearPfDeduction(totalEmployerArrearPfDeduction);

        return total;
    }

    YearlyTotalInterest getYearlyTotalInterest(List<PfCollection> pfCollectionList) {
        YearlyTotalInterest total = new YearlyTotalInterest();

        double totalEmployeeInterest = pfCollectionList.stream().mapToDouble(PfCollection::getEmployeeInterest).sum();
        double totalEmployerInterest = pfCollectionList.stream().mapToDouble(PfCollection::getEmployerInterest).sum();

        total.setTotalEmployeeInterestInYear(totalEmployeeInterest);
        total.setTotalEmployerInterestInYear(totalEmployerInterest);

        return total;
    }

    MonthlyContribution mapToMonthlyContribution(
        PfCollection pfCollection,
        HashMap<String, List<IndividualArrearSalary>> individualArrearSalaryHashMap
    ) {
        MonthlyContribution monthlyContribution = new MonthlyContribution();
        monthlyContribution.setMonth(Month.fromInteger(pfCollection.getMonth()).toString());

        monthlyContribution.setEmployeeInterest(pfCollection.getEmployeeInterest());
        monthlyContribution.setEmployeeContribution(pfCollection.getEmployeeContribution());

        monthlyContribution.setEmployerInterest(pfCollection.getEmployerInterest());
        monthlyContribution.setEmployerContribution(pfCollection.getEmployerContribution());

        //        double basic = MathRoundUtil.round(pfCollection.getEmployeeContribution() * 10d);
        //        double gross = MathRoundUtil.round( pfCollection.getEmployeeContribution() * 10d * (100d / 60d));

        double gross = pfCollection.getGross();
        double basic = pfCollection.getBasic();

        monthlyContribution.setBasic(basic);
        monthlyContribution.setGrossSalary(gross);

        // Arrear PF calculation
        String key = pfCollection.getMonth() + "-" + pfCollection.getYear();
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryHashMap.get(key);

        double arrearPf = 0;

        if (individualArrearSalaryList != null) {
            arrearPf = individualArrearSalaryList.stream().mapToDouble(IndividualArrearSalary::getArrearPfDeduction).sum();
        }

        monthlyContribution.setEmployeePfArrear(MathRoundUtil.round(arrearPf));
        monthlyContribution.setEmployerPfArrear(MathRoundUtil.round(arrearPf));

        return monthlyContribution;
    }

    ArrayList<MonthlyContribution> mapToMonthlyContribution(
        List<PfCollection> pfCollectionList,
        HashMap<String, List<IndividualArrearSalary>> individualArrearSalaryHashMap
    ) {
        ArrayList<MonthlyContribution> monthlyContributionList = new ArrayList<>();
        for (PfCollection pfCollection : pfCollectionList) {
            monthlyContributionList.add(mapToMonthlyContribution(pfCollection, individualArrearSalaryHashMap));
        }
        return monthlyContributionList;
    }
}
