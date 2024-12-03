package com.bits.hr.service.festivalBonus.generator;

import static com.bits.hr.service.salaryGenerationFractional.SalaryConstants.BASIC_PERCENT;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.FestivalBonusDetailsRepository;
import com.bits.hr.repository.HoldFbDisbursementRepository;
import com.bits.hr.service.festivalBonus.FBDataPreparationServiceImpl;
import com.bits.hr.service.festivalBonus.FBService;
import com.bits.hr.service.festivalBonus.model.FestivalBonusData;
import com.bits.hr.util.MathRoundUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FBRegularServiceImpl implements FBService {

    @Autowired
    private FBDataPreparationServiceImpl festivalBonusDataPreparationService;

    @Autowired
    private BonusConfigService bonusConfigService;

    @Autowired
    private FestivalBonusDetailsRepository festivalBonusDetailsRepository;

    @Autowired
    private HoldFbDisbursementRepository holdFbDisbursementRepository;

    @Override
    public List<FestivalBonusDetails> generate(Festival festival) {
        List<FestivalBonusDetails> festivalBonusDetailsList = new ArrayList<>();
        HashMap<EmployeeCategory, Double> config = bonusConfigService.getConfig();
        List<FestivalBonusData> festivalBonusDataList = festivalBonusDataPreparationService.getDataForFestivalBonusGeneration(festival);
        for (FestivalBonusData festivalBonusData : festivalBonusDataList) {
            Employee employee = festivalBonusData.getEmployee();
            double effectiveGross = festivalBonusData.getEffectiveGrossOnThatTime();
            double bonusPercent = 0;
            if (employee.getEmployeeCategory() != null) {
                bonusPercent = config.getOrDefault(employee.getEmployeeCategory(), 0d);
            }

            if (bonusPercent > 0) {
                FestivalBonusDetails festivalBonusDetails = new FestivalBonusDetails();
                festivalBonusDetails.setBasic((double) MathRoundUtil.round(effectiveGross * BASIC_PERCENT));
                festivalBonusDetails.setGross(effectiveGross);

                festivalBonusDetails.setEmployee(employee);
                double bonusAmount = MathRoundUtil.round(effectiveGross * (bonusPercent / 100d));
                festivalBonusDetails.setBonusAmount(bonusAmount);
                festivalBonusDetails.setFestival(festival);

                boolean isHold = festivalBonusData.isHold();
                String remarks = " - ";
                if (isHold) {
                    remarks = "Hold FnF";
                }

                festivalBonusDetails.setIsHold(isHold);
                festivalBonusDetails.setRemarks(remarks);

                festivalBonusDetailsList.add(festivalBonusDetails);
            }
        }
        return festivalBonusDetailsList;
    }

    @Override
    public List<FestivalBonusDetails> generateAndSave(Festival festival) {
        List<FestivalBonusDetails> festivalBonusDetailsList = generate(festival);
        List<FestivalBonusDetails> result = new ArrayList<>();
        removePreviousData(festival.getId());
        for (FestivalBonusDetails fbd : festivalBonusDetailsList) {
            result.add(festivalBonusDetailsRepository.save(fbd));
        }
        return result;
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
