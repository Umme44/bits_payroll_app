package com.bits.hr.service.finalSettlement.helperMethods;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.DTO.LeaveBalanceDetailViewDTO;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.LeaveBalanceDetailViewService;
import com.bits.hr.service.finalSettlement.dto.AbsentDaysAdjustment;
import com.bits.hr.service.finalSettlement.dto.LeaveEncashment;
import com.bits.hr.service.finalSettlement.dto.salary.TimeDuration;
import com.bits.hr.service.finalSettlement.util.ServiceTenure;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveProcessForFinalSettlementService {

    @Autowired
    private LeaveBalanceDetailViewService leaveBalanceDetailViewService;

    public AbsentDaysAdjustment calculateAbsentDaysAdjustment(Employee employee, LocalDate lastWorkingDay) {
        int year = lastWorkingDay.getYear();
        List<LeaveBalanceDetailViewDTO> leaveBalanceDetailViewDTOList = leaveBalanceDetailViewService.getYearlyProcessedLeaveBalance(
            year,
            employee.getId()
        );
        int excessAnnualLeaveTaken = 0;
        int excessCasualLeaveTaken = 0;

        for (LeaveBalanceDetailViewDTO leaveBalanceDetailViewDTO : leaveBalanceDetailViewDTOList) {
            if (leaveBalanceDetailViewDTO.getLeaveType() == LeaveType.MENTIONABLE_ANNUAL_LEAVE) {
                excessAnnualLeaveTaken = excessConsumedAnnualLeave(leaveBalanceDetailViewDTO, year, lastWorkingDay);
            }

            if (leaveBalanceDetailViewDTO.getLeaveType() == LeaveType.MENTIONABLE_CASUAL_LEAVE) {
                excessCasualLeaveTaken = excessConsumedCasualLeave(leaveBalanceDetailViewDTO, year, lastWorkingDay);
            }
        }

        int numberOfDays = excessAnnualLeaveTaken + excessCasualLeaveTaken;
        double amountToDeduct = MathRoundUtil.round((double) numberOfDays * (employee.getMainGrossSalary() / 30d));

        AbsentDaysAdjustment absentDaysAdjustment = new AbsentDaysAdjustment();

        absentDaysAdjustment.setNumberOfDays(numberOfDays);
        absentDaysAdjustment.setAmountToDeduct(amountToDeduct);
        return absentDaysAdjustment;
    }

    private int excessConsumedAnnualLeave(LeaveBalanceDetailViewDTO leaveBalanceDetailViewDTO, int year, LocalDate lastWorkingDay) {
        // for annual leave
        // get carry forwarded
        // get consumed this year
        // if this year consumption exceed carry forwarded(OB) + supposed to consume
        // add amount for deduction in final settlement

        int consumed = leaveBalanceDetailViewDTO.getConsumedDuringYear();
        int supposedToConsume = 0;

        TimeDuration timeDuration = ServiceTenure.calculateTenure(LocalDate.of(year, 1, 1), lastWorkingDay);
        if (timeDuration.getMonth() > 0) {
            supposedToConsume += timeDuration.getMonth() * 2;
        }
        if (timeDuration.getDay() >= 15) {
            supposedToConsume += 1;
        }
        int excessAnnualLeaveTaken = (consumed - (supposedToConsume + leaveBalanceDetailViewDTO.getOpeningBalance()));
        return Math.max(excessAnnualLeaveTaken, 0);
    }

    private int excessConsumedCasualLeave(LeaveBalanceDetailViewDTO leaveBalanceDetailViewDTO, int year, LocalDate lastWorkingDay) {
        // for casual leave
        // get consumed leave
        // get leave supposed to consume
        // if consumed more than supposed to , add to deduction days.

        int consumed = leaveBalanceDetailViewDTO.getConsumedDuringYear();
        double supposedToConsume = 0;

        TimeDuration timeDuration = ServiceTenure.calculateTenure(LocalDate.of(year, 1, 1), lastWorkingDay);
        if (timeDuration.getMonth() > 0) {
            supposedToConsume += ((double) timeDuration.getMonth() * (14d / 12d));
        }

        int excessCasualLeaveTaken = (consumed - (int) MathRoundUtil.round(supposedToConsume));
        return Math.max(excessCasualLeaveTaken, 0);
    }

    public LeaveEncashment getLeaveEncashment(Employee employee, LocalDate lastWorkingDay) {
        int year = lastWorkingDay.getYear();
        List<LeaveBalanceDetailViewDTO> leaveBalanceDetailViewDTOList = leaveBalanceDetailViewService.getYearlyProcessedLeaveBalance(
            year,
            employee.getId()
        );

        int daysForLeaveEncashment = 0;

        for (LeaveBalanceDetailViewDTO leaveBalanceDetailViewDTO : leaveBalanceDetailViewDTOList) {
            if (leaveBalanceDetailViewDTO.getLeaveType() == LeaveType.MENTIONABLE_ANNUAL_LEAVE) {
                // if service tenure less than 1 year , Leave encashment = 0 ;
                int excessAnnualLeaveTaken = excessConsumedAnnualLeave(leaveBalanceDetailViewDTO, year, lastWorkingDay);

                if (ServiceTenure.calculateTenure(employee.getDateOfJoining(), lastWorkingDay).getYear() < 1) {
                    daysForLeaveEncashment = 0;
                } else if (excessAnnualLeaveTaken > 0) {
                    daysForLeaveEncashment = 0;
                } else {
                    // ( past encashment + allocated this year ) - consumed this year
                    // this year allocation = last working day month * 2 + ( 15 diner beshi hole aro 1 beshi
                    int thisYearAllocation = 0;
                    // full month worked
                    if (lastWorkingDay.getDayOfMonth() == lastWorkingDay.lengthOfMonth()) {
                        thisYearAllocation = ((lastWorkingDay.getMonthValue() - 1) * 2);
                    } else if (lastWorkingDay.getDayOfMonth() >= 15) {
                        thisYearAllocation = ((lastWorkingDay.getMonthValue() - 1) * 2) + 1;
                    } else {
                        thisYearAllocation = ((lastWorkingDay.getMonthValue() - 1) * 2);
                    }
                    daysForLeaveEncashment =
                        leaveBalanceDetailViewDTO.getOpeningBalance() +
                        thisYearAllocation -
                        leaveBalanceDetailViewDTO.getConsumedDuringYear();
                }
            }
        }

        LeaveEncashment leaveEncashment = new LeaveEncashment();

        if (daysForLeaveEncashment > 60) {
            daysForLeaveEncashment = 60;
        }
        leaveEncashment.setNumOfDays(daysForLeaveEncashment);
        leaveEncashment.setPerDayAmount(employee.getMainGrossSalary() / 30d);
        return leaveEncashment;
    }
}
