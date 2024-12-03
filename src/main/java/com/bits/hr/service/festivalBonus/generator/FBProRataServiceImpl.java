package com.bits.hr.service.festivalBonus.generator;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Festival;
import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.FestivalBonusDetailsRepository;
import com.bits.hr.repository.FestivalRepository;
import com.bits.hr.repository.HoldFbDisbursementRepository;
import com.bits.hr.service.festivalBonus.FBDataPreparationServiceImpl;
import com.bits.hr.service.festivalBonus.FBService;
import com.bits.hr.service.festivalBonus.model.FestivalBonusData;
import com.bits.hr.service.salaryGenerationFractional.SalaryConstants;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class FBProRataServiceImpl implements FBService {

    @Autowired
    private FBDataPreparationServiceImpl fbDataPreparationService;

    @Autowired
    private FestivalBonusDetailsRepository festivalBonusDetailsRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private HoldFbDisbursementRepository holdFbDisbursementRepository;

    @Override
    public List<FestivalBonusDetails> generate(Festival festival) {
        /*
         * get employees whose Probation period is in between this year.
         * get num of total eligible bonus ( REL )
         * dayCount = if(doj this year) doj to 31'st dec
         *             else if ( doj in previous year ) jan 1st to 31'st dec
         *
         * bonus = ( ( dayCount / 365 ) * totalEligibleBonusPointFaced * effective basic on 31'st dec ) ) - ( bonus given this year )
         *
         *
         * */
        List<FestivalBonusDetails> festivalBonusDetailsList = new ArrayList<>();

        List<FestivalBonusData> festivalBonusDataList = fbDataPreparationService.getDataForFestivalBonusGeneration(festival);

        for (FestivalBonusData festivalBonusData : festivalBonusDataList) {
            FestivalBonusDetails festivalBonusDetails = new FestivalBonusDetails();

            // if doc is before doj just continue; for (declined to join) employee (temp solution)
            // todo: need permanent solution in future
            Employee employee = festivalBonusData.getEmployee();
            if (
                employee.getDateOfJoining() != null &&
                employee.getDateOfConfirmation() != null &&
                employee.getDateOfConfirmation().isBefore(employee.getDateOfJoining())
            ) {
                continue;
            }

            double dayCount = getDayCount(festivalBonusData.getEmployee(), festival.getBonusDisbursementDate());
            double totalEligibleBonusPointFaced = totalEligibleBonusPointFaced(festivalBonusData.getEmployee(), festival);
            double totalReceivedBonusPoint = totalReceivedBonusPoint(
                festivalBonusData.getEmployee(),
                festival.getBonusDisbursementDate().getYear()
            );

            if (totalEligibleBonusPointFaced == 0) {
                continue;
            }

            if (totalEligibleBonusPointFaced == totalReceivedBonusPoint) {
                continue;
            } else if (totalEligibleBonusPointFaced < totalReceivedBonusPoint) {
                continue;
                //                throw new RuntimeException(
                //                    String.format("Employee: %s ~ %s, " +
                //                        "total eligible festival: %s but total received: %s ",
                //                        festivalBonusData.getEmployee().getPin(),
                //                        festivalBonusData.getEmployee().getFullName(),
                //                        totalEligibleBonusPointFaced,
                //                        totalReceivedBonusPoint)
                //                );
            }
            double effectiveGross = festivalBonusData.getEffectiveGrossOnThatTime();
            double effectiveBasic = MathRoundUtil.round(festivalBonusData.getEffectiveGrossOnThatTime() * SalaryConstants.BASIC_PERCENT);
            double bonusAmountGivenThisYear = getBonusAmountGivenOnThatYear(
                festivalBonusData.getEmployee(),
                festival.getBonusDisbursementDate()
            );

            // BRAC IT Policy is maximum 02 festival will be disbursed in a calendar year for a Religion
            double effectiveTotalEligibleBonusPointFaced = Math.min(totalEligibleBonusPointFaced, 2);
            double proRataBonus = MathRoundUtil.round(
                ((dayCount / 365d) * effectiveTotalEligibleBonusPointFaced * effectiveBasic) - bonusAmountGivenThisYear
            );
            if (proRataBonus <= 0) {
                continue;
            }

            festivalBonusDetails.setEmployee(festivalBonusData.getEmployee());
            festivalBonusDetails.setFestival(festival);
            festivalBonusDetails.setBonusAmount(proRataBonus);

            festivalBonusDetails.setIsHold(false);
            festivalBonusDetails.setGross(effectiveGross);
            festivalBonusDetails.setBasic(effectiveBasic);
            festivalBonusDetails.setRemarks(" - ");

            festivalBonusDetailsList.add(festivalBonusDetails);
        }

        return festivalBonusDetailsList;
    }

    @Override
    public List<FestivalBonusDetails> generateAndSave(Festival festival) {
        List<FestivalBonusDetails> festivalBonusDetailsList = generate(festival);
        removePreviousData(festival.getId());
        List<FestivalBonusDetails> result = new ArrayList<>();
        for (FestivalBonusDetails fbd : festivalBonusDetailsList) {
            result.add(festivalBonusDetailsRepository.save(fbd));
        }
        return result;
    }

    double getDayCount(Employee employee, LocalDate bonusDisbursementDate) {
        if (employee.getDateOfJoining().getYear() == bonusDisbursementDate.getYear()) {
            long daysBetween = (ChronoUnit.DAYS.between(employee.getDateOfJoining(), bonusDisbursementDate)) + 1;
            return (double) daysBetween;
        } else {
            return 365d;
        }
    }

    // a service to calculate total received bonus from doc
    // if totalReceivedPoint == totalEligiblePoint => pro rata bonus is zero
    // if totalReceivedPoint < totalEligiblePoint => pro rata will be calculated
    // if totalReceivedPoint > totalEligiblePoint => invalid case
    double totalReceivedBonusPoint(Employee employee, int year) {
        // get bonus point between doc / jan 1st to 31st dec
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.getNonProRataFbBonusByEmployeeIdBetweenTimeRange(
            employee.getId(),
            startDate,
            endDate
        );
        return festivalBonusDetailsList.size();
    }

    double totalEligibleBonusPointFaced(Employee employee, Festival festival) {
        // get bonus point between doj / jan 1st to 31st dec
        // manual check one by one
        int year = festival.getBonusDisbursementDate().getYear();
        LocalDate yearStartDate = LocalDate.of(year, 1, 1);
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate doj = employee.getDateOfJoining();
        if (doj == null) {
            log.error("Date of joining is missing for {} - {}", employee.getPin(), employee.getFullName());
            return 0; // doj is missing
        }
        if (doj.isAfter(yearStartDate)) {
            startDate = doj;
        }
        LocalDate endDate = LocalDate.of(year, 12, 31);
        List<Festival> festivalList = festivalRepository.getFestivalsBetweenDatesAndApplicableReligionExcludeProRata(
            startDate,
            endDate,
            employee.getReligion()
        );
        return festivalList.size();
    }

    double getBonusAmountGivenOnThatYear(Employee employee, LocalDate bonusDisbursementDate) {
        int year = bonusDisbursementDate.getYear();
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.getNonProRataFbBonusByEmployeeIdBetweenTimeRange(
            employee.getId(),
            startDate,
            endDate
        );
        double total = festivalBonusDetailsList.stream().mapToDouble(FestivalBonusDetails::getBonusAmount).sum();
        return total;
    }

    public void removePreviousData(long festivalId) {
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.getPreviousGeneration(festivalId);
        for (FestivalBonusDetails fb : festivalBonusDetailsList) {
            try {
                holdFbDisbursementRepository.deleteAllByFestivalBonusDetails(fb);
                festivalBonusDetailsRepository.delete(fb);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new BadRequestAlertException("Failed to Delete Previous Data", "FestivalBonusDetails", "failedToDeletePreviousData");
            }
        }
    }
}
