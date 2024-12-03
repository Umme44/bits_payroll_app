package com.bits.hr.service.importXL.payroll;

import com.bits.hr.domain.AttendanceSummary;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.SalaryGeneratorMaster;
import com.bits.hr.repository.AttendanceSummaryRepository;
import com.bits.hr.repository.SalaryGeneratorMasterRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.importXL.GenericUploadServiceImpl;
import com.bits.hr.util.PinUtil;
import com.bits.hr.util.SalaryGenerationMasterService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LeaveAttandanceImportServiceImpl implements LeaveAttandanceImportService {

    @Autowired
    private SalaryGeneratorMasterRepository salaryGeneratorMasterRepository;

    @Autowired
    private SalaryGenerationMasterService salaryGenerationMasterService;

    @Autowired
    private GenericUploadServiceImpl genericUploadService;

    @Autowired
    private AttendanceSummaryRepository attendanceSummaryRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public boolean importFile(MultipartFile file, int year, int month) throws Exception {
        try {
            // if exist , delete previous records
            // set existing isMobileBillUpload to false.
            // update salary generation master
            SalaryGeneratorMaster salaryGeneratorMaster;
            if (salaryGenerationMasterService.isExistSalaryGeneratorMaster(year, month)) {
                attendanceSummaryRepository.deleteAllByYearAndMonth(year, month);
                salaryGeneratorMaster = salaryGenerationMasterService.getOrCreateSalaryGeneratorMaster(year, month);
                salaryGeneratorMaster.setIsAttendanceImported(false);
                salaryGeneratorMasterRepository.save(salaryGeneratorMaster);
            }

            // calculating days
            int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth();

            // 1. take the data
            List<ArrayList<String>> data = genericUploadService.upload(file);

            // 2. remove xlsx headers
            data.remove(0);

            for (ArrayList<String> dataItems : data) {
                // if pin empty , skip
                if (dataItems.isEmpty()) {
                    continue;
                }
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                if (dataItems.get(0).equals("")) {
                    continue;
                }

                AttendanceSummary att = new AttendanceSummary();
                att.setMonth(month);
                att.setYear(year);
                att.setTotalLeaveDays(0);

                int fractionDays = (int) Math.round(Double.parseDouble(dataItems.get(1)));
                if (fractionDays > daysInMonth) fractionDays = daysInMonth;
                int absentDays = daysInMonth - fractionDays;

                att.setTotalAbsentDays(absentDays);
                att.setTotalFractionDays(fractionDays);
                att.setTotalWorkingDays(0);
                Optional<Employee> employeeOptional = employeeService.findEmployeeByPin(PinUtil.formatPin(dataItems.get(0)));
                att.setEmployee(
                    employeeOptional.orElseThrow(() ->
                        new RuntimeException("No Employee found by this PIN" + PinUtil.formatPin(dataItems.get(0)))
                    )
                );
                save(att);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // if new , create  and set true isMobileBillUploaded
        // if old , get     and set true isMobileBillUploaded

        SalaryGeneratorMaster salaryGeneratorMaster;
        salaryGeneratorMaster = salaryGenerationMasterService.getOrCreateSalaryGeneratorMaster(year, month);
        salaryGeneratorMaster.setIsAttendanceImported(true);
        salaryGeneratorMasterRepository.save(salaryGeneratorMaster);

        return true;
    }

    public void save(AttendanceSummary a) {
        attendanceSummaryRepository.save(a);
    }
}
