package com.bits.hr.service.festivalBonus;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.EmploymentHistory;
import com.bits.hr.domain.Festival;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Religion;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeResignationRepository;
import com.bits.hr.repository.EmploymentHistoryRepository;
import com.bits.hr.service.festivalBonus.model.FestivalBonusData;
import com.bits.hr.util.DateUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class FBDataPreparationServiceImpl {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;

    @Autowired
    private EmploymentHistoryRepository employmentHistoryRepository;

    public List<FestivalBonusData> getDataForFestivalBonusGeneration(Festival festival) {
        try {
            if (festival.getIsProRata() != null && festival.getIsProRata()) {
                // generate pro rata festival bonus for selected point of time
                return getDataForProRata(festival);
            } else {
                // generate festival bonus
                return getDataForRegularCase(festival);
            }
        } catch (Exception ex) {
            log.error(ex);
            return new ArrayList<>();
        }
    }

    private List<FestivalBonusData> getDataForRegularCase(Festival festival) {
        List<Employee> employeeList;
        if (festival.getReligion() == Religion.ALL) {
            employeeList = employeeRepository.findAll();
        } else {
            employeeList = employeeRepository.findAllByReligion(festival.getReligion());
        }

        List<FestivalBonusData> festivalBonusDataList = new ArrayList<>();
        for (Employee employee : employeeList) {
            boolean isEligible = isEligible(employee, festival.getBonusDisbursementDate());
            if (isEligible) {
                FestivalBonusData festivalBonusData = new FestivalBonusData();
                festivalBonusData.setEmployee(employee);
                festivalBonusData.setEffectiveGrossOnThatTime(getEffectiveGrossOfTimePoint(employee, festival.getBonusDisbursementDate()));
                festivalBonusData.setHold(isFestivalBonusHold(employee, festival));
                festivalBonusDataList.add(festivalBonusData);
            }
        }
        return festivalBonusDataList;
    }

    private List<FestivalBonusData> getDataForProRata(Festival festival) {
        List<FestivalBonusData> festivalBonusDataList = new ArrayList<>();

        int year = festival.getBonusDisbursementDate().getYear();
        LocalDate yearStart = LocalDate.of(year, 1, 1);
        LocalDate yearEnd = LocalDate.of(year, 12, 31);
        List<Employee> employeeList = employeeRepository.getEmployeeProbationBetweenTimeRange(yearStart, yearEnd);

        for (Employee e : employeeList) {
            // employee lwd before 31st dec will not count in pro rata
            // but if lwd == 31 dec will count in pro rata
            if (isResignedBeforeTimePoint(e, yearEnd)) {
                continue;
            }

            FestivalBonusData festivalBonusData = new FestivalBonusData();
            festivalBonusData.setEmployee(e);
            double effectiveGross = getEffectiveGrossOfTimePoint(e, festival.getBonusDisbursementDate());
            festivalBonusData.setEffectiveGrossOnThatTime(effectiveGross);
            festivalBonusDataList.add(festivalBonusData);
        }
        return festivalBonusDataList;
    }

    double getEffectiveGrossOfTimePoint(Employee employee, LocalDate festivePoint) {
        try {
            /*
             * case 1: no employment history of promotion and increment => return current gross of employee profile
             * case 2: employment history available =>
             *           sub-case : get first promotion / increment where effective date < festival Dis. Date
             *                       available => send data from employment history
             *                       else => get last promotion / increment and take previous salary
             * */

            long numberOfPromotionOrIncrement = employmentHistoryRepository.getNumberOfPromotionOrIncrementByEmployeeId(employee.getId());

            if (numberOfPromotionOrIncrement == 0) {
                return employee.getMainGrossSalary();
            } else {
                List<EmploymentHistory> immediateBeforePromotionOrIncrement = employmentHistoryRepository
                    .getLastEffectiveSalaryChange(employee.getId(), festivePoint, PageRequest.of(0, 1))
                    .get()
                    .collect(Collectors.toList());

                if (immediateBeforePromotionOrIncrement.size() > 0) {
                    return immediateBeforePromotionOrIncrement.get(0).getCurrentMainGrossSalary();
                } else {
                    List<EmploymentHistory> firstPromotionOrIncrement = employmentHistoryRepository
                        .getFirstEffectiveSalaryChange(employee.getId(), PageRequest.of(0, 1))
                        .get()
                        .collect(Collectors.toList());
                    return firstPromotionOrIncrement.size() > 0
                        ? firstPromotionOrIncrement.get(0).getPreviousMainGrossSalary()
                        : employee.getMainGrossSalary();
                }
            }
        } catch (Exception ex) {
            // no promotion or increment in this point
            return employee.getMainGrossSalary();
        }
    }

    boolean isEligible(Employee employee, LocalDate festivePoint) {
        if (employee.isIsFestivalBonusDisabled() != null && employee.isIsFestivalBonusDisabled()) {
            return false;
        }
        // if not RCE or RPE => return false
        if (employee.getDesignation().getDesignationName().equalsIgnoreCase("Intern")) {
            return false;
        }
        if (employee.getEmployeeCategory() == null) {
            return false;
        } else if (
            employee.getEmployeeCategory() == EmployeeCategory.INTERN ||
            employee.getEmployeeCategory() == EmployeeCategory.CONSULTANTS ||
            employee.getEmployeeCategory() == EmployeeCategory.PART_TIME
        ) {
            return false;
        } else if (isResignedAtThatTimePoint(employee, festivePoint)) {
            return false;
        } else {
            if (employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
                // doc check
                return docCheckRCE(employee, festivePoint);
            } else if (employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
                LocalDate contactStartDate = employee.getDateOfJoining();
                LocalDate contactEndDate = LocalDate.MIN;
                if (employee.getContractPeriodEndDate() != null) {
                    contactEndDate = employee.getContractPeriodEndDate();
                }
                if (employee.getContractPeriodExtendedTo() != null) {
                    contactEndDate = employee.getContractPeriodExtendedTo();
                }
                return DateUtil.isBetween(festivePoint, contactStartDate, contactEndDate);
            } else {
                return false;
            }
        }
    }

    private boolean docCheckRCE(Employee employee, LocalDate festivePoint) {
        if (employee.getDateOfConfirmation() == null) {
            return false;
        }
        if (employee.getDateOfConfirmation().isBefore(festivePoint) || employee.getDateOfConfirmation().isEqual(festivePoint)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isResignedAtThatTimePoint(Employee employee, LocalDate timePoint) {
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
            employee.getId()
        );
        if (employeeResignationList.size() == 0) {
            return false;
        } else {
            LocalDate lwd = employeeResignationList.get(0).getLastWorkingDay();
            if (lwd.isBefore(timePoint) || lwd.isEqual(timePoint)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean isResignedBeforeTimePoint(Employee employee, LocalDate timePoint) {
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
            employee.getId()
        );
        if (employeeResignationList.size() == 0) {
            return false;
        } else {
            LocalDate lwd = employeeResignationList.get(0).getLastWorkingDay();
            if (lwd.isBefore(timePoint)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isFestivalBonusHold(Employee employee, Festival festival) {
        try {
            if (festival.getIsProRata() != null && festival.getIsProRata() == true) {
                return isFestivalBonusHoldProRata(employee, festival);
            } else {
                return isFestivalBonusHoldGeneral(employee, festival);
            }
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isFestivalBonusHoldProRata(Employee employee, Festival festival) {
        return false;
    }

    private boolean isFestivalBonusHoldGeneral(Employee employee, Festival festival) {
        List<EmployeeResignation> employeeResignation = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
            employee.getId()
        );
        if (
            employeeResignation.size() > 0 &&
            (
                employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE ||
                employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE
            ) &&
            DateUtil.isBetween(
                festival.getBonusDisbursementDate(),
                employeeResignation.get(0).getResignationDate(),
                employeeResignation.get(0).getLastWorkingDay()
            )
        ) {
            return true;
        } else {
            return false;
        }
    }
}
