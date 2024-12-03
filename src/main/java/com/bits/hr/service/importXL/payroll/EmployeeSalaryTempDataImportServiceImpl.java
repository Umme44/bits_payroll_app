package com.bits.hr.service.importXL.payroll;

import static com.bits.hr.service.importXL.payroll.rowConstants.EmployeeSalaryTempDataImportServiceImplRowConstants.*;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalaryTempData;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeSalaryTempDataRepository;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.util.PinUtil;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmployeeSalaryTempDataImportServiceImpl {

    @Autowired
    GenericUploadService genericUploadService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeSalaryTempDataRepository employeeSalaryTempDataRepository;

    public Map<String, String> importFile(MultipartFile file) {
        try {
            Map<String, String> resultSet = new Hashtable<>();
            int employeeMissing = 0;
            int blankRow = 0;
            int success = 0;

            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header01 = data.remove(0); // BRAC IT services limited
            List<String> header02 = data.remove(0); // <"Month" : <month> <year>

            Month month = Month.valueOf(header02.get(1).toUpperCase(Locale.ROOT).trim());
            int year = (int) Double.parseDouble(header02.get(2));

            List<String> header03 = data.remove(0); // <sal-gen-date>
            List<String> header04 = data.remove(0); // <row headers>
            for (List<String> dataItems : data) {
                if (dataItems.isEmpty()) {
                    blankRow++;
                    continue;
                }
                if (dataItems.get(0).equals("0")) {
                    blankRow++;
                    continue;
                }
                if (dataItems.get(0).equals("")) {
                    blankRow++;
                    continue;
                }
                EmployeeSalaryTempData salaryTempData = new EmployeeSalaryTempData();
                salaryTempData.setMonth(month);
                salaryTempData.setYear(year);
                // double employee :: Employee
                // double month :: Month enum:: JANUARY,FEBRUARY,MARCH,APRIL,MAY,JUNE,JULY,AUGUST,SEPTEMBER,OCTOBER,NOVEMBER,DECEMBER
                // double year :: Integer

                String refPin = PinUtil.formatPin(dataItems.get(SAL_TMP_DTA_RC_REF_PIN));
                String pin = PinUtil.formatPin(dataItems.get(SAL_TMP_DTA_RC_PIN));

                Optional<Employee> employee = employeeRepository.findEmployeeByPin(pin);

                if (!employee.isPresent()) {
                    employeeMissing++;
                    continue;
                }
                // get by month year and employee
                List<EmployeeSalaryTempData> previousRecord = employeeSalaryTempDataRepository.findEmployeeSalaryByEmployeeAndYearAndMonth(
                    employee.get().getId(),
                    year,
                    month
                );
                // update if already exists
                if (!previousRecord.isEmpty()) {
                    salaryTempData = previousRecord.get(0);
                }

                salaryTempData.setEmployee(employee.get());

                double mainGrossSalary = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_TOTAL_GROSS_SALARY));
                salaryTempData.setMainGrossSalary(mainGrossSalary);

                double mainGrossBasicSalary = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_BASIC));
                salaryTempData.setMainGrossBasicSalary(mainGrossBasicSalary);

                double mainGrossHouseRent = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_HOUSE_RENT));
                salaryTempData.setMainGrossHouseRent(mainGrossHouseRent);

                double mainGrossMedicalAllowance = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_MEDICAL_ALLOWANCE));
                salaryTempData.setMainGrossMedicalAllowance(mainGrossMedicalAllowance);

                double mainGrossConveyanceAllowance = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_CONVEYANCE_ALLOWANCE));
                salaryTempData.setMainGrossConveyanceAllowance(mainGrossConveyanceAllowance);

                int absentDays = (int) Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_ABSENT_DAYS));
                salaryTempData.setAbsentDays(absentDays);

                int fractionDays = (int) Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_FRACTION_DAYS));
                salaryTempData.setFractionDays(fractionDays);

                double payableGrossSalary = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_PAYABLE_TOTAL_GROSS_SALARY));
                salaryTempData.setPayableGrossSalary(payableGrossSalary);

                double payableGrossBasicSalary = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_PAYABLE_BASIC));
                salaryTempData.setPayableGrossBasicSalary(payableGrossBasicSalary);

                double payableGrossHouseRent = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_PAYABLE_HOUSE_RENT));
                salaryTempData.setPayableGrossHouseRent(payableGrossHouseRent);

                double payableGrossMedicalAllowance = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_PAYABLE_MEDICAL_ALLOWANCE));
                salaryTempData.setPayableGrossMedicalAllowance(payableGrossMedicalAllowance);

                double payableGrossConveyanceAllowance = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_PAYABLE_CONVEYANCE_ALLOWANCE));
                salaryTempData.setPayableGrossConveyanceAllowance(payableGrossConveyanceAllowance);

                double arrearSalary = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_ARREAR_SALARY));
                salaryTempData.setArrearSalary(arrearSalary);

                double pfDeduction = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_PF_DEDUCTION));
                salaryTempData.setPfDeduction(pfDeduction);

                double taxDeduction = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_TAX_DEDUCTION));
                salaryTempData.setTaxDeduction(taxDeduction);

                double welfareFundDeduction = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_WELFARE_FUND_DEDUCTION));
                salaryTempData.setWelfareFundDeduction(welfareFundDeduction);

                double mobileBillDeduction = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_MOBILE_BILL_DEDUCTION));
                salaryTempData.setMobileBillDeduction(mobileBillDeduction);

                double otherDeduction = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_OTHER_DEDUCTION));
                salaryTempData.setOtherDeduction(otherDeduction);

                double totalDeduction = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_TOTAL_DEDUCTIONS));
                salaryTempData.setTotalDeduction(totalDeduction);

                double netPay = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_NET_PAY));
                salaryTempData.setNetPay(netPay);

                String remarks = dataItems.get(SAL_TMP_DTA_RC_REMARKS);
                if (remarks == "0") remarks = "";
                salaryTempData.setRemarks(remarks);

                double pfContribution = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_PF_CONTRIBUTION));
                salaryTempData.setPfContribution(pfContribution);

                double gfContribution = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_GRATUITY_FUND_CONTRIBUTION));
                salaryTempData.gfContribution(gfContribution);

                double provisionForFestivalBonus = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_PROVISION_FOR_FESTIVAL_BONUS));
                salaryTempData.setProvisionForFestivalBonus(provisionForFestivalBonus);

                double provisionForLeaveEncashment = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_PROVISION_FOR_LEAVE_ENCASHMENT));
                salaryTempData.setProvisionForLeaveEncashment(provisionForLeaveEncashment);

                double provisionForProjectBonus = Double.parseDouble(dataItems.get(SAL_TMP_DTA_RC_PROVISION_FOR_PROJECT_BONUS));
                salaryTempData.setProvishionForProjectBonus(provisionForProjectBonus);

                double entertainment = 0.0d;
                salaryTempData.setEntertainment(entertainment);

                double utility = 0.0d;
                salaryTempData.setUtility(utility);

                double livingAllowance = 0.0d;
                salaryTempData.setLivingAllowance(livingAllowance);

                double otherAddition = 0.0d;
                salaryTempData.otherAddition(otherAddition);

                double salaryAdjustment = 0.0d;
                salaryTempData.setSalaryAdjustment(salaryAdjustment);

                double providentFundArrear = 0.0d;
                salaryTempData.providentFundArrear(providentFundArrear);
                employeeSalaryTempDataRepository.save(salaryTempData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Hashtable<String, String> error = new Hashtable<String, String>();
            error.put("exception occurred:", e.toString());
            return error;
        }

        Hashtable<String, String> error = new Hashtable<String, String>();
        error.put("error:", "Unknown error");
        return error;
    }
}
