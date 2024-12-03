package com.bits.hr.service.incomeTaxManagement;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.*;
import com.bits.hr.service.finalSettlement.helperMethods.ResignationProcessingService;
import com.bits.hr.service.incomeTaxManagement.helperMethods.Arrear;
import com.bits.hr.service.incomeTaxManagement.helperMethods.CalculateMultiplier;
import com.bits.hr.service.incomeTaxManagement.helperMethods.CalculateSalarySum;
import com.bits.hr.service.incomeTaxManagement.helperMethods.CalculateTaxQueryConfig;
import com.bits.hr.service.incomeTaxManagement.model.IncomeTaxData;
import com.bits.hr.service.incomeTaxManagement.model.SalarySum;
import com.bits.hr.service.incomeTaxManagement.model.TaxQueryConfig;
import com.bits.hr.service.incomeTaxManagement.presentToFutureSalary.CalculateFutureIncomeService;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncomeTaxDataPreparationService {

    @Autowired
    private FestivalBonusDetailsRepository festivalBonusDetailsRepository;

    @Autowired
    private AitPaymentRepository aitPaymentRepository;

    @Autowired
    private CalculatePreviousIncomeService calculatePreviousIncomeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;

    @Autowired
    private IndividualArrearSalaryRepository individualArrearSalaryRepository;

    @Autowired
    private CalculateFutureIncomeService calculateFutureIncomeService;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private FestivalBonusConfigRepository festivalBonusConfigRepository;

    @Autowired
    private ResignationProcessingService resignationProcessingService;

    public IncomeTaxData getDataForTaxCalculation(
        Employee employee,
        int trackingMonth,
        int trackingYear,
        Optional<EmployeeSalary> employeeSalaryOptional
    ) {
        IncomeTaxData incomeTaxData = new IncomeTaxData();
        incomeTaxData.loadDefault(employee);

        final TaxQueryConfig taxQueryConfig = CalculateTaxQueryConfig.getConfig(trackingMonth, trackingYear);

        Optional<EmployeeResignation> employeeResignationOptional = resignationProcessingService.getResignation(employee.getId());

        List<IndividualArrearSalary> indvArrSalList = individualArrearSalaryRepository.getAllByEmployeeIdAndDateRange(
            employee.getId(),
            taxQueryConfig.getIncomeYearStartDate(),
            taxQueryConfig.getIncomeYearEndDate()
        );

        incomeTaxData.setIndividualArrearSalaryList(indvArrSalList);
        double individualArrearSalary = getIndividualArrearSalary(indvArrSalList);
        double individualArrearPf = getIndividualArrearPfDeduction(indvArrSalList);
        double individualArrearFestivalBonus = getIndividualArrearFestivalBonus(indvArrSalList);
        double individualArrearTaxDeduction = getIndividualArrearTaxDeduction(indvArrSalList);
        double individualArrearNetPay = getIndividualArrearNetPay(indvArrSalList);

        List<EmployeeSalary> previousEmployeeSalaryList = getPreviousSalaries(employee.getId(), taxQueryConfig);
        List<EmployeeSalary> presentToFutureSalaryList = calculateFutureIncomeService.getFutureSalaries(
            trackingMonth,
            trackingYear,
            employee,
            employeeSalaryOptional.get(),
            taxQueryConfig,
            employeeResignationOptional
        );

        SalarySum previousSalarySum = CalculateSalarySum.getSummationOfEmployeeSalaries(previousEmployeeSalaryList);

        SalarySum presentToFutureSalarySum = CalculateSalarySum.getSummationOfEmployeeSalaries(presentToFutureSalaryList);

        double mergedArrears = mergedArrears(previousEmployeeSalaryList, employeeSalaryOptional);

        SalarySum totalSalarySum = processEmployeeSalariesForTaxCalculation(
            previousSalarySum,
            presentToFutureSalarySum,
            mergedArrears + individualArrearSalary
        );

        double yearlyPF = MathRoundUtil.round(
            previousEmployeeSalaryList.stream().mapToDouble(EmployeeSalary::getPfContribution).sum() +
            presentToFutureSalaryList.stream().mapToDouble(EmployeeSalary::getPfContribution).sum()
        );

        double arrearPf = getArrearPf(previousEmployeeSalaryList, employeeSalaryOptional);

        double effectiveFestivalBonus = getEffectiveFestivalBonus(employee, taxQueryConfig);
        double ait = getAdvanceIncomeTaxPayments(employee, taxQueryConfig);

        incomeTaxData.setEmployeeResignationOptional(employeeResignationOptional);
        incomeTaxData.setYearlyBasic(totalSalarySum.getBasic());
        incomeTaxData.setYearlyHouseRent(totalSalarySum.getHouseRent());
        incomeTaxData.setYearlyMedical(totalSalarySum.getMedical());
        incomeTaxData.setYearlyConveyance(totalSalarySum.getConveyance());
        incomeTaxData.setEffectiveFestivalBonus(effectiveFestivalBonus);
        incomeTaxData.setAit(ait);
        incomeTaxData.setYearlyPf(yearlyPF);
        incomeTaxData.setArrearPf(arrearPf);

        incomeTaxData.setTaxQueryConfig(taxQueryConfig);

        // indv arr data
        incomeTaxData.setIndividualArrearSalary(individualArrearSalary);
        incomeTaxData.setIndividualArrearPf(individualArrearPf);
        incomeTaxData.setIndividualArrearFestivalBonus(individualArrearFestivalBonus);
        incomeTaxData.setTaxCutFromIndividualArrears(individualArrearTaxDeduction);
        incomeTaxData.setIndividualArrearNetPay(individualArrearNetPay);

        // rest of the data capture done for the report generations
        incomeTaxData.setPreviousSalaryList(previousEmployeeSalaryList);
        incomeTaxData.setPresentToFutureSalaryList(presentToFutureSalaryList);

        incomeTaxData.setMergedArrears(mergedArrears);
        incomeTaxData.setPresentToFutureSalarySum(presentToFutureSalarySum);

        return incomeTaxData;
    }

    private double getEffectiveFestivalBonus(Employee employee, TaxQueryConfig taxQueryConfig) {
        if (employee.getEmployeeCategory() != null && employee.getEmployeeCategory().equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
            // if regular employee ==> take from system
            return getEffectiveFbForRegularConfirmedEmployee(employee, taxQueryConfig);
        } else if (
            employee.getEmployeeCategory() != null && employee.getEmployeeCategory().equals(EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE)
        ) {
            // if probationary employee ==> assume festival bonus between doc -- first( resignation/income year end date)
            return getEffectiveFbForRegularProbationalEmployee(employee, taxQueryConfig);
        } else if (employee.getEmployeeCategory() != null && employee.getEmployeeCategory().equals(EmployeeCategory.CONTRACTUAL_EMPLOYEE)) {
            // if contractual employee ==> assume festival bonus ( where not exist ) between doj to min(dateOfResignation,income year end)
            // if fixed term contract employee ==> assume festival bonus ( where not exist ) between doj to min(contract end date, dateOfResignation,income year end)
            return getEffectiveFbForContractualEmployee(employee, taxQueryConfig);
        } else {
            return 0d;
        }
    }

    double getEffectiveFbForRegularConfirmedEmployee(Employee employee, TaxQueryConfig taxQueryConfig) {
        List<Employee> employeeList = employeeRefList(employee.getId());
        List<FestivalBonusDetails> festivalBonusDetailsList = new ArrayList<>();
        for (Employee sameEmployeeProfile : employeeList) {
            List<FestivalBonusDetails> fbDetails = festivalBonusDetailsRepository.getFbBonusByEmployeeIdBetweenTimeRange(
                sameEmployeeProfile.getId(),
                taxQueryConfig.getIncomeYearStartDate(),
                taxQueryConfig.getIncomeYearEndDate()
            );
            festivalBonusDetailsList.addAll(fbDetails);
        }
        Double festivalBonus = festivalBonusDetailsList
            .stream()
            .filter(x -> x.getBonusAmount() != null && x.getBonusAmount() > 0)
            .mapToDouble(FestivalBonusDetails::getBonusAmount)
            .sum();
        return MathRoundUtil.round(festivalBonus);
    }

    double getEffectiveFbForRegularProbationalEmployee(Employee employee, TaxQueryConfig taxQueryConfig) {
        // todo : earlier profile consideration
        LocalDate startDate = taxQueryConfig.getIncomeYearStartDate();
        LocalDate endDate = taxQueryConfig.getIncomeYearEndDate();

        LocalDate fbConsideredStartDate = startDate;
        LocalDate fbConsideredEndDate = endDate;

        // if faced any pro rata festival during probation period
        double proRata = festivalBonusDetailsRepository
            .getProRataFbBonusByEmployeeIdBetweenTimeRange(employee.getId(), startDate, taxQueryConfig.getIncomeYearEndDate())
            .stream()
            .filter(x -> x.getBonusAmount() != null && x.getBonusAmount() > 0)
            .mapToDouble(FestivalBonusDetails::getBonusAmount)
            .sum();

        // if doc is between income year doc = start date for future festival bonus assumption.
        // if probation period extended, doc = extended date
        // if employee is resigned within this income year endDate = last working day

        if (employee.getDateOfConfirmation() != null && DateUtil.isBetweenOrEqual(employee.getDateOfConfirmation(), startDate, endDate)) {
            fbConsideredStartDate = employee.getDateOfConfirmation();
        }
        if (
            employee.getProbationPeriodExtendedTo() != null &&
            DateUtil.isBetweenOrEqual(employee.getProbationPeriodExtendedTo(), startDate, endDate)
        ) {
            fbConsideredStartDate = employee.getProbationPeriodExtendedTo();
        }

        // if resigned
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
            employee.getId()
        );

        if (employeeResignationList.size() > 0) {
            LocalDate lwd = employeeResignationList.get(0).getLastWorkingDay();
            if (DateUtil.isBetweenOrEqual(lwd, startDate, endDate)) {
                fbConsideredEndDate = lwd;
            }
        }

        //        LocalDate doc = employee.getDateOfConfirmation();
        //        if (employee.isIsProbationaryPeriodExtended() != null
        //            && employee.getProbationPeriodExtendedTo() != null
        //            && employee.isIsProbationaryPeriodExtended()) {
        //            doc = employee.getProbationPeriodExtendedTo();
        //        }
        //        if (DateUtil.isBetweenOrEqual(doc, startDate, endDate)) {
        //            startDate = doc;
        //        } else if (doc.isAfter(endDate)) {
        //            return proRata;
        //        }

        List<Festival> festivalListOnThatIncomeYear = festivalRepository.getFestivalsBetweenDisbursementDate(
            fbConsideredStartDate,
            fbConsideredEndDate,
            employee.getReligion()
        );

        double bonusPercent = .60d;
        List<FestivalBonusConfig> bonusConfig = festivalBonusConfigRepository
            .findAll()
            .stream()
            .filter(x -> x.getEmployeeCategory().equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE))
            .collect(Collectors.toList());
        if (bonusConfig.size() > 0) {
            bonusPercent = bonusConfig.get(0).getPercentageFromGross() / 100d;
        }
        double fbPredicted = festivalListOnThatIncomeYear.size() * MathRoundUtil.round(employee.getMainGrossSalary() * bonusPercent);

        double festivalBonusPerTaxYear = fbPredicted + proRata;

        return festivalBonusPerTaxYear;
    }

    double getEffectiveFbForFixedTermContractEmployee(Employee employee, TaxQueryConfig taxQueryConfig) {
        // todo : earlier profile consideration [ if exist ]
        LocalDate startDate = taxQueryConfig.getIncomeYearStartDate();
        LocalDate endDate = taxQueryConfig.getIncomeYearEndDate();

        LocalDate doj = employee.getDateOfJoining();
        if (DateUtil.isBetweenOrEqual(doj, startDate, endDate)) {
            startDate = doj;
        }

        LocalDate contractPeriodEndDate = employee.getContractPeriodEndDate();
        if (employee.getContractPeriodExtendedTo() != null) {
            contractPeriodEndDate = employee.getContractPeriodExtendedTo();
        }

        endDate = contractPeriodEndDate;
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
            employee.getId()
        );

        if (employeeResignationList.size() > 0) {
            LocalDate lwd = employeeResignationList.get(0).getLastWorkingDay();
            if (DateUtil.isBetweenOrEqual(lwd, startDate, endDate)) {
                endDate = lwd;
            }
        }

        double bonusPercent = .50d;
        List<FestivalBonusConfig> bonusConfig = festivalBonusConfigRepository
            .findAll()
            .stream()
            .filter(x -> x.getEmployeeCategory().equals(EmployeeCategory.CONTRACTUAL_EMPLOYEE))
            .collect(Collectors.toList());
        if (bonusConfig.size() > 0) {
            bonusPercent = bonusConfig.get(0).getPercentageFromGross() / 100d;
        }

        List<Festival> festivalListOnThatIncomeYear = festivalRepository.getFestivalsBetweenDisbursementDate(
            startDate,
            endDate,
            employee.getReligion()
        );

        List<FestivalBonusDetails> festivalBonusDetailGenerated = festivalBonusDetailsRepository.getNonProRataFbBonusByEmployeeIdBetweenTimeRange(
            employee.getId(),
            startDate,
            endDate
        );

        // hashmap of all festival bonus
        HashMap<Long, Festival> allFestivalHashmap = new HashMap<>();
        for (Festival festival : festivalListOnThatIncomeYear) {
            allFestivalHashmap.put(festival.getId(), festival);
        }
        // hashmap of already generated festival bonus
        HashMap<Long, Festival> generatedFestivalHashmap = new HashMap<>();
        for (FestivalBonusDetails festivalBonusDetails : festivalBonusDetailGenerated) {
            generatedFestivalHashmap.put(festivalBonusDetails.getFestival().getId(), festivalBonusDetails.getFestival());
        }
        double assumedFestivalBonus = 0;
        for (Festival festival : festivalListOnThatIncomeYear) {
            // if generated continue
            // else assume
            if (generatedFestivalHashmap.containsKey(festival.getId())) {
                continue;
            } else {
                assumedFestivalBonus += MathRoundUtil.round(employee.getMainGrossSalary() * bonusPercent);
            }
        }
        double festivalBonusGenerated = festivalBonusDetailGenerated
            .stream()
            .filter(x -> x.getBonusAmount() != null && x.getBonusAmount() > 0)
            .mapToDouble(FestivalBonusDetails::getBonusAmount)
            .sum();

        double totalFb = assumedFestivalBonus + festivalBonusGenerated;

        return totalFb;
    }

    //todo: refactor for chaining reference
    double getEffectiveFbForContractualEmployee(Employee employee, TaxQueryConfig taxQueryConfig) {
        // today between income year means some extra calculation required
        if (!DateUtil.isBetweenOrEqual(LocalDate.now(), taxQueryConfig.getIncomeYearStartDate(), taxQueryConfig.getIncomeYearEndDate())) {
            // get all existing
            List<Employee> employeeList = employeeRefList(employee.getId());
            List<FestivalBonusDetails> festivalBonusDetailsList = new ArrayList<>();
            for (Employee sameEmployeeProfile : employeeList) {
                List<FestivalBonusDetails> fbDetails = festivalBonusDetailsRepository.getFbBonusByEmployeeIdBetweenTimeRange(
                    sameEmployeeProfile.getId(),
                    taxQueryConfig.getIncomeYearStartDate(),
                    taxQueryConfig.getIncomeYearEndDate()
                );
                festivalBonusDetailsList.addAll(fbDetails);
            }
            Double festivalBonus = festivalBonusDetailsList
                .stream()
                .filter(x -> x.getBonusAmount() != null && x.getBonusAmount() > 0)
                .mapToDouble(FestivalBonusDetails::getBonusAmount)
                .sum();
            return MathRoundUtil.round(festivalBonus);
        } else {
            // running tax year logic
            if (employee.getIsFixedTermContract() != null && employee.getIsFixedTermContract()) {
                return getEffectiveFbForFixedTermContractEmployee(employee, taxQueryConfig);
            } else {
                double bonusPercent = .50d;
                List<FestivalBonusConfig> bonusConfig = festivalBonusConfigRepository
                    .findAll()
                    .stream()
                    .filter(x -> x.getEmployeeCategory().equals(EmployeeCategory.CONTRACTUAL_EMPLOYEE))
                    .collect(Collectors.toList());
                if (bonusConfig.size() > 0) {
                    bonusPercent = bonusConfig.get(0).getPercentageFromGross() / 100d;
                }

                LocalDate startDate = taxQueryConfig.getIncomeYearStartDate();
                LocalDate endDate = taxQueryConfig.getIncomeYearEndDate();

                LocalDate doj = employee.getDateOfJoining();
                if (DateUtil.isBetweenOrEqual(doj, startDate, endDate)) {
                    startDate = doj;
                }

                List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
                    employee.getId()
                );

                if (employeeResignationList.size() > 0) {
                    LocalDate lwd = employeeResignationList.get(0).getLastWorkingDay();
                    if (DateUtil.isBetweenOrEqual(lwd, startDate, endDate)) {
                        endDate = lwd;
                    }
                }
                List<Festival> festivalListOnThatIncomeYear = festivalRepository.getFestivalsBetweenDisbursementDate(
                    startDate,
                    endDate,
                    employee.getReligion()
                );

                List<FestivalBonusDetails> festivalBonusDetailGenerated = festivalBonusDetailsRepository.getNonProRataFbBonusByEmployeeIdBetweenTimeRange(
                    employee.getId(),
                    startDate,
                    endDate
                );

                // hashmap of all festival bonus
                HashMap<Long, Festival> allFestivalHashmap = new HashMap<>();
                for (Festival festival : festivalListOnThatIncomeYear) {
                    allFestivalHashmap.put(festival.getId(), festival);
                }
                // hashmap of already generated festival bonus
                HashMap<Long, Festival> generatedFestivalHashmap = new HashMap<>();
                for (FestivalBonusDetails festivalBonusDetails : festivalBonusDetailGenerated) {
                    generatedFestivalHashmap.put(festivalBonusDetails.getFestival().getId(), festivalBonusDetails.getFestival());
                }
                double assumedFestivalBonus = 0;
                for (Festival festival : festivalListOnThatIncomeYear) {
                    // if generated continue
                    // else assume
                    if (generatedFestivalHashmap.containsKey(festival.getId())) {
                        continue;
                    } else {
                        assumedFestivalBonus += MathRoundUtil.round(employee.getMainGrossSalary() * bonusPercent);
                    }
                }
                double festivalBonusGenerated = festivalBonusDetailGenerated
                    .stream()
                    .filter(x -> x.getBonusAmount() != null && x.getBonusAmount() > 0)
                    .mapToDouble(FestivalBonusDetails::getBonusAmount)
                    .sum();

                double totalFb = 0d;
                if (employee.isIsFestivalBonusDisabled()) {
                    totalFb = festivalBonusGenerated;
                } else {
                    totalFb = assumedFestivalBonus + festivalBonusGenerated;
                }

                return totalFb;
            }
        }
    }

    private double getAdvanceIncomeTaxPayments(Employee employee, TaxQueryConfig taxQueryConfig) {
        // ait payments
        List<AitPayment> aitPaymentList = aitPaymentRepository.findAllBetween(
            employee.getId(),
            taxQueryConfig.getIncomeYearStartDate(),
            taxQueryConfig.getIncomeYearEndDate()
        );
        double aitPaymentSum = aitPaymentList.stream().mapToDouble(AitPayment::getAmount).sum();
        return aitPaymentSum;
    }

    public List<EmployeeSalary> getPreviousSalaries(long employeeId, TaxQueryConfig taxQueryConfig) {
        //ref id consideration
        HashSet<Long> empIds = new HashSet<>();
        List<Employee> employeeList = employeeRefList(employeeId);
        List<EmployeeSalary> previousEmployeeSalaryList = new ArrayList<>();
        for (Employee employee : employeeList) {
            if (empIds.contains(employee.getId())) {
                continue;
            }
            previousEmployeeSalaryList.addAll(calculatePreviousIncomeService.getAllPreviousSalaries(employee.getId(), taxQueryConfig));
            empIds.add(employee.getId());
        }

        // sorting Salaries
        previousEmployeeSalaryList.sort((s1, s2) -> {
            if (s1.getYear() - s2.getYear() == 0) {
                return s1.getMonth().ordinal() - s2.getMonth().ordinal();
            } else {
                return s1.getYear() - s2.getYear();
            }
        });

        return previousEmployeeSalaryList;
    }

    private SalarySum getPresentToFutureSalarySum(
        int trackingMonth,
        int trackingYear,
        Employee employee,
        TaxQueryConfig taxQueryConfig,
        EmployeeSalary employeeSalary,
        IncomeTaxData incomeTaxData,
        Optional<EmployeeResignation> employeeResignationOptional
    ) {
        return calculateFutureIncomeService.getFutureSalarySum(
            trackingMonth,
            trackingYear,
            employee,
            employeeSalary,
            taxQueryConfig,
            employeeResignationOptional,
            incomeTaxData
        );
        // old approach
        /*
        int multiplier = CalculateMultiplier.getMultiplier(employee, trackingMonth, trackingYear, taxQueryConfig);
        if(employeeSalaryOptional.isPresent() && isThisLastFullMonthForResignation(employeeSalaryOptional.get())){
            multiplier = 1;
        }

        SalarySum presentToIncomeYearEndSalary = CalculateSalarySum.salarySumMultiplier(employeeSalaryOptional, multiplier, grossOptional);
        return presentToIncomeYearEndSalary;
        */

    }

    private SalarySum getTotalSalarySumForFixedTermContactEmployee(
        Employee employee,
        TaxQueryConfig taxQueryConfig,
        int trackingMonth,
        int trackingYear
    ) {
        int multiplier = CalculateMultiplier.getMultiplier(employee, trackingMonth, trackingYear, taxQueryConfig);
        return CalculateSalarySum.salarySumMultiplier(Optional.empty(), multiplier, Optional.of(employee.getMainGrossSalary()));
    }

    private SalarySum processEmployeeSalariesForTaxCalculation(
        SalarySum previousSalarySum,
        SalarySum presentToFutureSalarySum,
        double totalGivenArrearSalary
    ) {
        ArrayList<SalarySum> salarySumList = new ArrayList<>();
        salarySumList.add(previousSalarySum);
        salarySumList.add(presentToFutureSalarySum);

        SalarySum yearlySalaryForTax = CalculateSalarySum.getSummationOfSalarySum(salarySumList);
        yearlySalaryForTax = Arrear.balanceArrear(yearlySalaryForTax, totalGivenArrearSalary);
        return yearlySalaryForTax;
    }

    private double mergedArrears(List<EmployeeSalary> previousEmployeeSalaryList, Optional<EmployeeSalary> presentSalary) {
        // 1. arrear salary table
        // 2. arrear payment table
        double currMonthArrear = 0;
        if (presentSalary.isPresent() && presentSalary.get().getArrearSalary() != null) {
            currMonthArrear = presentSalary.get().getArrearSalary();
        }
        double previousArrear = 0;
        for (EmployeeSalary es : previousEmployeeSalaryList) {
            if (es.getArrearSalary() != null) {
                previousArrear += es.getArrearSalary();
            }
        }

        double totalArrear = currMonthArrear + previousArrear;

        return totalArrear;
        /* todo : solve point of conflict here
        List<ArrearPayment> arrearPayments = arrearPaymentRepository.getMergableArrears(employee.getId(),taxQueryConfig.getIncomeYearStartDate(),taxQueryConfig.getIncomeYearEndDate());
        double totalMergableArrearPayments = arrearPayments.stream().mapToDouble(ArrearPayment::getDisbursementAmount).sum();
        return totalArrear + totalMergableArrearPayments;
        */
        //        SalarySum yearlySalary = Arrear.balanceArrear(yearlySalaryForTax, totalArrear);
    }

    private double getArrearPf(List<EmployeeSalary> previousEmployeeSalaryList, Optional<EmployeeSalary> presentSalary) {
        double sum = 0;
        if (presentSalary.isPresent() && presentSalary.get().getProvidentFundArrear() != null) {
            for (EmployeeSalary es : previousEmployeeSalaryList) {
                if (es.getProvidentFundArrear() != null) {
                    sum += es.getProvidentFundArrear();
                }
            }
            return sum + presentSalary.get().getProvidentFundArrear();
        } else {
            return sum;
        }
    }

    private double getIndividualArrearSalary(List<IndividualArrearSalary> individualArrearSalaryList) {
        double totalIndividualArrearSalary = 0;
        for (IndividualArrearSalary iv : individualArrearSalaryList) {
            if (iv.getArrearSalary() != null) {
                totalIndividualArrearSalary += iv.getArrearSalary();
            }
        }
        return totalIndividualArrearSalary;
    }

    private double getIndividualArrearPfDeduction(List<IndividualArrearSalary> individualArrearSalaryList) {
        double totalIndividualArrearPf = 0;
        for (IndividualArrearSalary iv : individualArrearSalaryList) {
            if (iv.getArrearPfDeduction() != null) {
                totalIndividualArrearPf += iv.getArrearPfDeduction();
            }
        }
        return totalIndividualArrearPf;
    }

    private double getIndividualArrearFestivalBonus(List<IndividualArrearSalary> individualArrearSalaryList) {
        double totalIndividualArrearFestivalBonus = 0;
        for (IndividualArrearSalary iv : individualArrearSalaryList) {
            if (iv.getFestivalBonus() != null) {
                totalIndividualArrearFestivalBonus += iv.getFestivalBonus();
            }
        }
        return totalIndividualArrearFestivalBonus;
    }

    private double getIndividualArrearTaxDeduction(List<IndividualArrearSalary> individualArrearSalaryList) {
        double totalTaxDeduction = 0;
        for (IndividualArrearSalary iv : individualArrearSalaryList) {
            if (iv.getTaxDeduction() != null) {
                totalTaxDeduction += iv.getTaxDeduction();
            }
        }
        return totalTaxDeduction;
    }

    private double getIndividualArrearNetPay(List<IndividualArrearSalary> individualArrearSalaryList) {
        double totalNetPay = 0;
        for (IndividualArrearSalary iv : individualArrearSalaryList) {
            if (iv.getTaxDeduction() != null) {
                totalNetPay += iv.getNetPay();
            }
        }
        return totalNetPay;
    }

    private List<Employee> employeeRefList(long currentId) {
        List<Employee> employeeList = new ArrayList<>();
        Optional<Employee> employee = employeeRepository.findById(currentId);
        if (employee.isPresent()) {
            employeeList.add(employee.get());
        }
        int maxItr = 5;
        int i = 0;
        while (employee.isPresent() && employee.get().getReferenceId() != null) {
            i++;
            if (i >= maxItr) {
                break;
            }
            employee = employeeRepository.findEmployeeByPin(employee.get().getReferenceId().trim());
            if (employee.isPresent()) {
                employeeList.add(employee.get());
            } else {
                break;
            }
        }
        return employeeList;
    }

    @Deprecated
    private boolean isThisLastFullMonthForResignation(EmployeeSalary employeeSalary) {
        // 0 tax for resigning employee
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository
            .findEmployeeResignationByEmployeeId(employeeSalary.getEmployee().getId())
            .stream()
            .filter(x -> x.getLastWorkingDay() != null && x.getApprovalStatus() == Status.APPROVED)
            .collect(Collectors.toList());

        if (!employeeResignationList.isEmpty()) {
            EmployeeResignation employeeResignation = employeeResignationList.get(0);

            boolean isResignationOnBrokenMonth =
                (employeeResignation.getLastWorkingDay().lengthOfMonth() != employeeResignation.getLastWorkingDay().getDayOfMonth());
            int resignationMonth = employeeResignation.getLastWorkingDay().getMonth().getValue();
            int salaryMonth = employeeSalary.getMonth().ordinal() + 1;

            int resignationYear = employeeResignation.getLastWorkingDay().getYear();
            int salYear = employeeSalary.getYear();

            // if this is the broken month return true else return false
            if (resignationMonth == salaryMonth && resignationYear == salYear && isResignationOnBrokenMonth == false) {
                return true;
            }
            // if next month is broken month
            else if (isResignationOnBrokenMonth) {
                int rm2 = employeeResignation.getLastWorkingDay().minusMonths(1).getMonth().getValue();
                int ry2 = employeeResignation.getLastWorkingDay().minusMonths(1).getYear();
                if (rm2 == salaryMonth && ry2 == salYear) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }
}
