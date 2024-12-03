package com.bits.hr.service.xlExportHandling.festivalBonus;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Festival;
import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.domain.enumeration.Religion;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.FestivalBonusDetailsRepository;
import com.bits.hr.repository.FestivalRepository;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FBProRataReportDataPrepareService {

    @Autowired
    private FestivalBonusDetailsRepository festivalBonusDetailsRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    private static final byte TOTAL_FESTIVAL_IN_BRAC_IT = 2; //TODO: MOVE IN A CONFIGURATION

    public ExportXLPropertiesDTO exportProRataReport(long festivalId) {
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findByFestivalIdOrderByEmployeePin(festivalId);
        Festival festival = festivalRepository.findById(festivalId).get();

        final int disburseYear = festival.getBonusDisbursementDate().getYear();

        LocalDate yearStartDate = LocalDate.of(festival.getBonusDisbursementDate().getYear(), 1, 1);
        LocalDate yearEndDate = LocalDate.of(festival.getBonusDisbursementDate().getYear(), 12, 31);
        List<Festival> yearWiseFestivalList = festivalRepository.getFestivalsBetweenFestivalDateExcludeProRata(yearStartDate, yearEndDate);

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();
        subTitleList.add(festival.getTitle());
        LocalDate disbursementDate = festival.getBonusDisbursementDate();
        subTitleList.add(
            "Disbursement Date: " + disbursementDate.getDayOfMonth() + " " + disbursementDate.getMonth() + ", " + disbursementDate.getYear()
        );

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("SL");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Employee Name");
        tableHeaderList.add("Unit");
        tableHeaderList.add("Department");
        tableHeaderList.add("Religion");
        tableHeaderList.add("DOJ");
        tableHeaderList.add("DOC");
        tableHeaderList.add("Basic");
        tableHeaderList.add("Year End/Last Working Date");
        tableHeaderList.add("No. of Days Worked in Calendar Year");

        yearWiseFestivalList.forEach(festival1 -> {
            tableHeaderList.add(festival1.getFestivalName() + " : " + festival1.getFestivalDate());
        });
        tableHeaderList.add("Pro-Rata Bonus Amount (Basic*2*No.Days/365)");
        tableHeaderList.add("Already Received one Festival Bonus Amount (2nd Festival)");
        tableHeaderList.add("Net Pro-Rata FB Payment (on " + festival.getBonusDisbursementDate() + ")");
        tableHeaderList.add("Remarks");

        List<List<Object>> tableDataListOfList = new ArrayList<>();

        long serialNoCount = 1;
        double totalBonus = 0;
        double totalDisbursed = 0;
        double totalNetProRata = 0;

        for (FestivalBonusDetails bonusDetails : festivalBonusDetailsList) {
            List<Object> objectList = new ArrayList<>();
            objectList.add(++serialNoCount);
            Employee employee = bonusDetails.getEmployee();
            objectList.add(employee.getPin());
            objectList.add(employee.getFullName());
            objectList.add(employee.getUnit().getUnitName());
            objectList.add(employee.getDepartment().getDepartmentName());

            // religion
            if (employee.getReligion() == null) {
                throw new BadRequestAlertException(
                    String.format(
                        "Religion is missing for %s-%s. Please update employee religion.",
                        employee.getPin(),
                        employee.getFullName()
                    ),
                    "FBProRataReportDataPrepareService",
                    "religionIsMissing"
                );
            }
            final Religion employeeReligion = employee.getReligion();
            objectList.add(this.processReligion(employeeReligion));

            //doj
            if (employee.getDateOfJoining() == null) {
                throw new BadRequestAlertException(
                    String.format(
                        "Date of joining is missing for %s-%s. Please update employee date of joining.",
                        employee.getPin(),
                        employee.getFullName()
                    ),
                    "FBProRataReportDataPrepareService",
                    "dojIsMissing"
                );
            }
            final LocalDate doj = employee.getDateOfJoining();
            objectList.add(doj);

            //doc (doc is required for (non-confirm/confirm) report generation)
            if (employee.getDateOfConfirmation() == null) {
                throw new BadRequestAlertException(
                    String.format(
                        "Date of confirmation is missing for %s-%s. Please update employee date of confirmation.",
                        employee.getPin(),
                        employee.getFullName()
                    ),
                    "fbProRataReport",
                    "docIsMissing"
                );
            }
            final LocalDate doc = employee.getDateOfConfirmation();
            objectList.add(doc);

            //basic (basic is required in pro rata calculation)
            if (bonusDetails.getBasic() == null) {
                throw new BadRequestAlertException(
                    String.format(
                        "Festival bonus basic amount is missing for " + "%s-%s. Please re-generate Pro Rata...",
                        employee.getPin(),
                        employee.getFullName()
                    ),
                    "FBProRataReportDataPrepareService",
                    "festivalBonusBasicIsMissing"
                );
            }
            objectList.add(bonusDetails.getBasic());

            //calendar year-end
            objectList.add(yearEndDate);

            //no of days worked
            //double calendarYearWorkingDays = (double) ChronoUnit.DAYS.between(yearStartDateOrDOJ, yearEndDate) + 1;
            double calendarYearWorkingDays = this.getCalendarYearWorkingDays(doj, disburseYear);
            objectList.add(calendarYearWorkingDays);

            //status on festivals
            for (Festival festival1 : yearWiseFestivalList) {
                String status = this.processStatus(festival1, employeeReligion, doc, doj);
                objectList.add(status);
            }

            //pro rata bonus
            double proRataBonusAmount =
                this.calculateProRataBonusAmount(doj, disburseYear, employeeReligion, calendarYearWorkingDays, bonusDetails.getBasic());
            objectList.add(proRataBonusAmount);
            totalBonus += proRataBonusAmount;

            //already received
            //pro rata - bonus received this year
            double alreadyReceived = festivalBonusDetailsRepository
                .getNonProRataFbBonusByEmployeeIdBetweenTimeRange(employee.getId(), yearStartDate, yearEndDate)
                .stream()
                .mapToDouble(FestivalBonusDetails::getBonusAmount)
                .sum();

            objectList.add(alreadyReceived);
            totalDisbursed += alreadyReceived;

            //net pro-rata
            double proRataPayment = bonusDetails.getBonusAmount();
            objectList.add(proRataPayment);
            totalNetProRata += proRataPayment;

            // validation
            double reportGeneratedBonus = proRataBonusAmount - alreadyReceived;
            if (reportGeneratedBonus != proRataPayment) {
                throw new BadRequestAlertException(
                    String.format(
                        "Pro Rata amount is mismatched for " +
                        "%s-%s. Already bonus generated amount = %s; In report bonus generated amount = %s ",
                        employee.getPin(),
                        employee.getFullName(),
                        proRataPayment,
                        reportGeneratedBonus
                    ),
                    "FBProRataReportDataPrepareService",
                    "proRataAmountMismatched"
                );
            }

            //remarks
            objectList.add("");

            tableDataListOfList.add(objectList);
        }
        // total amount row
        List<Object> totalAmountRow = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            totalAmountRow.add("");
        }

        yearWiseFestivalList.forEach(x -> {
            totalAmountRow.add("");
        });
        totalAmountRow.add("Total Amount");

        totalAmountRow.add(totalBonus);
        totalAmountRow.add(totalDisbursed);
        totalAmountRow.add(totalNetProRata);
        totalAmountRow.add("");
        tableDataListOfList.add(totalAmountRow);

        // less: hold/resigned row
        List<Object> lessAmountRow = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            lessAmountRow.add("");
        }
        yearWiseFestivalList.forEach(x -> {
            lessAmountRow.add("");
        });
        lessAmountRow.add("Less: Hold/Resigned (will be paid with Final Settlement)");
        lessAmountRow.add("");
        lessAmountRow.add("");
        lessAmountRow.add(0);
        tableDataListOfList.add(lessAmountRow);

        // bank disbursement row
        List<Object> bankDisburseAmountRow = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            bankDisburseAmountRow.add("");
        }
        yearWiseFestivalList.forEach(x -> {
            bankDisburseAmountRow.add("");
        });
        bankDisburseAmountRow.add("Bank Disbursement ");
        bankDisburseAmountRow.add("");
        bankDisburseAmountRow.add("");
        bankDisburseAmountRow.add(0);
        tableDataListOfList.add(bankDisburseAmountRow);

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName("Pro Rata Festival Bonus");
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setSubTitleList(subTitleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(tableDataListOfList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(16);
        return exportXLPropertiesDTO;
    }

    double calculateProRataBonusAmount(
        LocalDate doj,
        int disburseYear,
        Religion employeeReligion,
        double calendarYearWorkingDays,
        double basic
    ) {
        LocalDate yearStartDate = LocalDate.of(disburseYear, 1, 1);
        LocalDate yearEndDate = LocalDate.of(disburseYear, 12, 31);

        LocalDate effectiveDate = yearStartDate;

        // if employee DOJ year and disburse year is same DOJ will count or yearStartDate will count
        // todo: need to handle leap year
        if (doj.getYear() == disburseYear) {
            effectiveDate = doj;
        }
        int totalFestivalBonusFaced = festivalRepository
            .getFestivalsBetweenDatesAndApplicableReligionExcludeProRata(effectiveDate, yearEndDate, employeeReligion)
            .size();

        // BRAC IT Policy is maximum 02 festival will be disbursed in a calendar year for a Religion
        double effectiveTotalEligibleBonusPointFaced = Math.min(totalFestivalBonusFaced, 2);
        // todo: In leap year, will it count 365?
        return MathRoundUtil.round(basic * effectiveTotalEligibleBonusPointFaced * (calendarYearWorkingDays / 365d));
    }

    double getCalendarYearWorkingDays(LocalDate doj, int year) {
        LocalDate yearEndDate = LocalDate.of(year, 12, 31);
        if (doj.getYear() == year) {
            return ChronoUnit.DAYS.between(doj, yearEndDate) + 1;
        } else {
            return 365d; //todo: what will happen in leap-year
        }
    }

    private String processStatus(Festival festival, Religion employeeReligion, LocalDate doc, LocalDate doj) {
        if (festival.getReligion() == Religion.ALL || festival.getReligion() == employeeReligion) {
            // for confirm, compare between doc and festival date
            if (doc.isEqual(festival.getBonusDisbursementDate()) || doc.isBefore(festival.getBonusDisbursementDate())) {
                return "Confirm";
            }
            // for probation, compare between doj and festival date
            else if (doj.equals(festival.getFestivalDate()) || doj.isBefore(festival.getFestivalDate())) {
                return "Non-Confirm";
            } else {
                return "N/A";
            }
        } else {
            return "N/A";
        }
    }

    private String processReligion(Religion employeeReligion) {
        if (employeeReligion == Religion.ISLAM) {
            return "Islam";
        } else if (employeeReligion == Religion.HINDU) {
            return "Hinduism";
        } else if (employeeReligion == Religion.BUDDHA) {
            return "Buddhism";
        } else if (employeeReligion == Religion.CHRISTIAN) {
            return "Christianity";
        } else {
            return "Other";
        }
    }
}
