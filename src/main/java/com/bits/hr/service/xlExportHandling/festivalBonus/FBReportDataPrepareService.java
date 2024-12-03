package com.bits.hr.service.xlExportHandling.festivalBonus;

import com.bits.hr.domain.Festival;
import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.repository.EmployeeResignationRepository;
import com.bits.hr.repository.FestivalBonusDetailsRepository;
import com.bits.hr.repository.FestivalRepository;
import com.bits.hr.service.xlExportHandling.FBDeptWiseSummary;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FBReportDataPrepareService {

    @Autowired
    FestivalBonusDetailsRepository festivalBonusDetailsRepository;

    @Autowired
    EmployeeResignationRepository employeeResignationRepository;

    @Autowired
    FestivalRepository festivalRepository;

    public ExportXLPropertiesDTO exportSummaryReport(long festivalId) throws IOException {
        List<FestivalBonusDetails> festivalBonusDetails = festivalBonusDetailsRepository.findByFestivalId(festivalId);

        Hashtable<String, FBDeptWiseSummary> deptWiseObject = new Hashtable<>();

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();
        Festival festival = festivalBonusDetails.get(0).getFestival();
        subTitleList.add(festival.getTitle());
        LocalDate disbursementDate = festival.getBonusDisbursementDate();
        subTitleList.add(
            "As on " + disbursementDate.getMonth() + " " + disbursementDate.getDayOfMonth() + ", " + disbursementDate.getYear()
        );

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("Departments");
        tableHeaderList.add("HEAD Count");
        tableHeaderList.add("Regular-Bonus");
        tableHeaderList.add("Contractual-Bonus");
        tableHeaderList.add("Total Bonus for " + festival.getFestivalName());

        List<List<Object>> tableDataListOfList = new ArrayList<>();

        AtomicReference<Double> totalRegular = new AtomicReference<>((double) 0);
        AtomicReference<Double> totalContractual = new AtomicReference<>((double) 0);
        AtomicInteger totalHeadCount = new AtomicInteger();

        festivalBonusDetails
            .stream()
            .forEach(bonusDetails -> {
                String departmentName = bonusDetails.getEmployee().getDepartment().getDepartmentName();

                if (deptWiseObject.containsKey(departmentName)) {
                    FBDeptWiseSummary existingDept = deptWiseObject.get(departmentName);
                    existingDept.setHeadCount(existingDept.getHeadCount() + 1);

                    if (bonusDetails.getEmployee().getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
                        existingDept.setRegularBonus(existingDept.getRegularBonus() + bonusDetails.getBonusAmount());
                    } else if (bonusDetails.getEmployee().getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
                        existingDept.setContractualBonus(existingDept.getContractualBonus() + bonusDetails.getBonusAmount());
                    }

                    existingDept.setTotalBonus(existingDept.getContractualBonus() + existingDept.getRegularBonus());
                    deptWiseObject.put(departmentName, existingDept);
                } else {
                    FBDeptWiseSummary newDept = new FBDeptWiseSummary();
                    newDept.setHeadCount(1);
                    if (bonusDetails.getEmployee().getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
                        newDept.setRegularBonus(bonusDetails.getBonusAmount());
                    } else if (bonusDetails.getEmployee().getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
                        newDept.setContractualBonus(bonusDetails.getBonusAmount());
                    }
                    newDept.setTotalBonus(bonusDetails.getBonusAmount());
                    deptWiseObject.put(departmentName, newDept);
                }
            });

        deptWiseObject.forEach((dept, values) -> {
            List<Object> objectList = new ArrayList<>();
            objectList.add(dept);
            objectList.add(values.getHeadCount());
            objectList.add(values.getRegularBonus());
            objectList.add(values.getContractualBonus());
            objectList.add(values.getTotalBonus());

            tableDataListOfList.add(objectList);
            totalRegular.updateAndGet(v -> (v + values.getRegularBonus()));
            totalContractual.updateAndGet(v -> (v + values.getContractualBonus()));
            totalHeadCount.addAndGet(values.getHeadCount());
        });

        List<Object> objectListTotal = new ArrayList<>();
        objectListTotal.add("Total Festival Bonus");
        objectListTotal.add(totalHeadCount.get());
        objectListTotal.add(totalRegular.get());
        objectListTotal.add(totalContractual.get());
        objectListTotal.add(totalRegular.get() + totalContractual.get());
        tableDataListOfList.add(objectListTotal);

        List<Object> newLine = new ArrayList<>();
        objectListTotal.add("");
        objectListTotal.add("");
        objectListTotal.add("");
        objectListTotal.add("");
        objectListTotal.add("");
        tableDataListOfList.add(newLine);

        List<Object> objectListRegular = new ArrayList<>();
        objectListRegular.add("Regular");
        objectListRegular.add("");
        objectListRegular.add("");
        objectListRegular.add("");
        objectListRegular.add(totalRegular);
        tableDataListOfList.add(objectListRegular);

        List<Object> objectListContractual = new ArrayList<>();
        objectListContractual.add("Contractual");
        objectListContractual.add("");
        objectListContractual.add("");
        objectListContractual.add("");
        objectListContractual.add(totalContractual);
        tableDataListOfList.add(objectListContractual);

        List<Object> objectListGrandTotal = new ArrayList<>();
        objectListGrandTotal.add("Grand Total Festival Bonus");
        objectListGrandTotal.add("");
        objectListGrandTotal.add("");
        objectListGrandTotal.add("");

        double grandTotal = totalRegular.get() + totalContractual.get();
        objectListGrandTotal.add(grandTotal);
        tableDataListOfList.add(objectListGrandTotal);

        List<Object> objectLessCash = new ArrayList<>();
        objectLessCash.add("Less: Cash Disbursement");
        objectLessCash.add("");
        objectLessCash.add("");
        objectLessCash.add("");
        objectLessCash.add(0);
        tableDataListOfList.add(objectLessCash);

        List<Object> objectBankDisbursement = new ArrayList<>();
        objectBankDisbursement.add("Net Festival Bonus (Bank Disbursement)");
        objectBankDisbursement.add("");
        objectBankDisbursement.add("");
        objectBankDisbursement.add("");
        objectBankDisbursement.add(grandTotal);
        tableDataListOfList.add(objectBankDisbursement);

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();
        exportXLPropertiesDTO.setSheetName("Summary");
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setSubTitleList(subTitleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(tableDataListOfList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(5);
        return exportXLPropertiesDTO;
    }
}
