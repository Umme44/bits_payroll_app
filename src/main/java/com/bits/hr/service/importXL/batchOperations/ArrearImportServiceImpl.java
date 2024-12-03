package com.bits.hr.service.importXL.batchOperations;

import com.bits.hr.domain.ArrearSalary;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfArrear;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.ArrearSalaryRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfArrearRepository;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.util.PinUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ArrearImportServiceImpl {

    @Autowired
    private ArrearSalaryRepository arrearSalaryRepository;

    @Autowired
    private PfArrearRepository pfArrearRepository;

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private EmployeeRepository employeeRepository;

    public boolean importFile(MultipartFile file) {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header1 = data.remove(0);

            for (List<String> dataItems : data) {
                ArrearSalary arrearSalary = new ArrearSalary();
                //  PIN	    NAME	ARREAR_MONTH	ARREAR_YEAR   Amount    ARREAR_TYPE     pf-arrear       pf-arrear-remarks
                //  0 	    1       2       	    3             4         5               6               7
                // if no pin , continue
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                // if employee not available , discard and continue
                Employee employee = new Employee();
                if (employeeRepository.findEmployeeByPin(PinUtil.formatPin(dataItems.get(0))).isPresent()) {
                    employee = employeeRepository.findEmployeeByPin(PinUtil.formatPin(dataItems.get(0))).get();
                    arrearSalary.setEmployee(employee);
                } else {
                    continue;
                }
                // no need name
                Month month = Month.valueOf(dataItems.get(2).toUpperCase(Locale.ROOT));
                int year = (int) Double.parseDouble(dataItems.get(3));

                boolean doesHaveSalaryArrear = false;
                if (!dataItems.get(4).equals("N/A")) {
                    doesHaveSalaryArrear = true;
                }
                if (doesHaveSalaryArrear) {
                    double arrearAmount = Double.parseDouble(dataItems.get(4));
                    String arrearType = dataItems.get(5);
                    if (arrearType.equals("N/A")) {
                        arrearType = "No Remarks Applicable";
                    }
                    arrearSalary.setMonth(month);
                    arrearSalary.setYear(year);
                    arrearSalary.setAmount(arrearAmount);
                    arrearSalary.setArrearType(arrearType);
                    saveOrUpdateArrearSalary(arrearSalary);
                }
                boolean doesHavePfArrear = false;
                if (!dataItems.get(6).equals("N/A")) {
                    doesHavePfArrear = true;
                }
                if (doesHavePfArrear) {
                    double pfArrearAmount = Double.parseDouble(dataItems.get(6));
                    String pfArrearRemarks = PinUtil.formatPin(dataItems.get(7));
                    if (pfArrearRemarks.equals("N/A")) {
                        pfArrearRemarks = " No Remarks Applicable";
                    }
                    PfArrear pfArrear = new PfArrear();
                    pfArrear.setEmployee(employee);
                    pfArrear.setAmount(pfArrearAmount);
                    pfArrear.setYear(year);
                    pfArrear.setMonth(month);
                    pfArrear.setRemarks(pfArrearRemarks);
                    saveOrUpdatePfArrear(pfArrear);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private ArrearSalary saveOrUpdateArrearSalary(ArrearSalary arrearSalary) {
        if (arrearSalary.getEmployee() != null && arrearSalary.getMonth() != null && arrearSalary.getYear() != null) {
            List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findByEmployeeIdAndYearAndMonth(
                arrearSalary.getEmployee().getId(),
                arrearSalary.getYear(),
                arrearSalary.getMonth()
            );
            // not empty ==> update
            if (!arrearSalaryList.isEmpty()) {
                ArrearSalary arrearSalaryPrevious = arrearSalaryList.get(0);
                arrearSalaryPrevious.setAmount(arrearSalary.getAmount());
                return arrearSalaryRepository.save(arrearSalaryPrevious);
            }
            // else save
            else {
                return arrearSalaryRepository.save(arrearSalary);
            }
        }
        return null;
    }

    private PfArrear saveOrUpdatePfArrear(PfArrear pfArrear) {
        if (pfArrear.getEmployee() != null && pfArrear.getMonth() != null && pfArrear.getYear() != null) {
            // -- handling duplicates
            List<PfArrear> pfArrearList = pfArrearRepository.findByEmployeeIdAndYearAndMonth(
                pfArrear.getEmployee().getId(),
                pfArrear.getYear(),
                pfArrear.getMonth()
            );
            for (PfArrear pfa : pfArrearList) {
                pfArrearRepository.delete(pfa);
            }
            return pfArrearRepository.save(pfArrear);
        }
        return null;
    }
}
