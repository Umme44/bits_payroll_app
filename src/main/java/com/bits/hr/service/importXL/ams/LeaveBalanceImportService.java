package com.bits.hr.service.importXL.ams;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.LeaveBalance;
import com.bits.hr.domain.enumeration.LeaveAmountType;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.LeaveBalanceRepository;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.util.PinUtil;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class LeaveBalanceImportService {

    @Autowired
    LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    GenericUploadService genericUploadService;

    @Autowired
    EmployeeRepository employeeRepository;

    public boolean importFile(MultipartFile file) {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);

            // get year from row-1,col-2 => 0,1

            List<String> header1 = data.remove(0);
            List<String> header2 = data.remove(0); // second row will be in 0  if first row get's deleted
            int year = (int) Math.round(Double.parseDouble(header1.get(1)));
            for (List<String> dataItems : data) {
                // if no pin , continue
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                LeaveBalance annualLeaveBalance = new LeaveBalance();
                LeaveBalance casualLeaveBalance = new LeaveBalance();
                LeaveBalance compensatoryLeaveBalance = new LeaveBalance();
                LeaveBalance paternityLeaveBalance = new LeaveBalance();
                LeaveBalance maternityLeaveBalance = new LeaveBalance();
                LeaveBalance pandemicLeaveBalance = new LeaveBalance();

                // 0      1       2                 3                   4                   5                       6               7                   8                   9
                // pin	name	Annual_Leave_CF	    Annual_Leave	    Casual_Leave        compensatory_leave	    paternity	    maternity_leave	    pandemic_leave      update?
                // if employee not available , discard and continue

                if (employeeRepository.findEmployeeByPin(PinUtil.formatPin(dataItems.get(0))).isPresent()) {
                    Employee employee = employeeRepository.findEmployeeByPin(PinUtil.formatPin(dataItems.get(0))).get();

                    annualLeaveBalance = createLeaveBalanceObject(employee, year, LeaveType.MENTIONABLE_ANNUAL_LEAVE);
                    casualLeaveBalance = createLeaveBalanceObject(employee, year, LeaveType.MENTIONABLE_CASUAL_LEAVE);
                    compensatoryLeaveBalance = createLeaveBalanceObject(employee, year, LeaveType.NON_MENTIONABLE_COMPENSATORY_LEAVE);
                    paternityLeaveBalance = createLeaveBalanceObject(employee, year, LeaveType.NON_MENTIONABLE_PATERNITY_LEAVE);
                    maternityLeaveBalance = createLeaveBalanceObject(employee, year, LeaveType.NON_MENTIONABLE_MATERNITY_LEAVE);
                    pandemicLeaveBalance = createLeaveBalanceObject(employee, year, LeaveType.NON_MENTIONABLE_PANDEMIC_LEAVE);
                } else {
                    continue;
                }

                // annual leave
                // Opening balance = carry forwarded balance
                int annualLeaveOpeningBalance = (int) Math.round(Double.parseDouble(dataItems.get(2)));
                annualLeaveBalance.setOpeningBalance(annualLeaveOpeningBalance);

                int annualLeaveBalanceThisYear = (int) Math.round(Double.parseDouble(dataItems.get(3)));
                annualLeaveBalance.setAmount(annualLeaveBalanceThisYear);

                // casual leave
                int casualLeaveAllocated = (int) Math.round(Double.parseDouble(dataItems.get(4)));
                casualLeaveBalance.setAmount(casualLeaveAllocated);

                // compensatory leave
                int compensatoryLeaveEarned = (int) Math.round(Double.parseDouble(dataItems.get(5)));
                compensatoryLeaveBalance.setAmount(compensatoryLeaveEarned);

                // paternity leave
                int paternityLeaveAmount = (int) Math.round(Double.parseDouble(dataItems.get(6)));
                paternityLeaveBalance.setAmount(paternityLeaveAmount);

                // maternity leave
                int maternityLeaveAmount = (int) Math.round(Double.parseDouble(dataItems.get(7)));
                maternityLeaveBalance.setAmount(maternityLeaveAmount);

                // pandemic leave
                int pandemicLeaveAmount = (int) Math.round(Double.parseDouble(dataItems.get(8)));
                pandemicLeaveBalance.setAmount(pandemicLeaveAmount);

                // update or skip
                if (dataItems.get(8).equals("yes")) {
                    saveOrUpdate(annualLeaveBalance);
                    saveOrUpdate(casualLeaveBalance);
                    saveOrUpdate(compensatoryLeaveBalance);
                    saveOrUpdate(paternityLeaveBalance);
                    saveOrUpdate(maternityLeaveBalance);
                    saveOrUpdate(pandemicLeaveBalance);
                }
                // skip if exist
                else {
                    saveOrSkip(annualLeaveBalance);
                    saveOrSkip(casualLeaveBalance);
                    saveOrSkip(compensatoryLeaveBalance);
                    saveOrSkip(paternityLeaveBalance);
                    saveOrSkip(maternityLeaveBalance);
                    saveOrSkip(pandemicLeaveBalance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    LocalDate doubleToDate(Double d) {
        Date javaDate = DateUtil.getJavaDate(d);
        LocalDate date = javaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return date;
    }

    private void saveOrUpdate(LeaveBalance leaveBalance) {
        if (leaveBalance.getOpeningBalance() == null) leaveBalance.setOpeningBalance(0);
        if (leaveBalance.getAmount() == null) leaveBalance.setAmount(0);
        if (leaveBalance.getAmount() == 0 && leaveBalance.getOpeningBalance() == 0) {
            return;
        }

        if (
            !leaveBalanceRepository.existsByEmployeeIdAndLeaveTypeAndYear(
                leaveBalance.getEmployee().getId(),
                leaveBalance.getLeaveType(),
                leaveBalance.getYear()
            )
        ) {
            leaveBalanceRepository.save(leaveBalance);
        } else {
            LeaveBalance leaveBalanceOld = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeAndYear(
                    leaveBalance.getEmployee().getId(),
                    leaveBalance.getLeaveType(),
                    leaveBalance.getYear()
                )
                .get();
            leaveBalance.setId(leaveBalanceOld.getId()); // updating old entity
            leaveBalanceRepository.save(leaveBalance);
        }
    }

    private void saveOrSkip(LeaveBalance leaveBalance) {
        if (leaveBalance.getOpeningBalance() == null) leaveBalance.setOpeningBalance(0);
        if (leaveBalance.getAmount() == null) leaveBalance.setAmount(0);

        if (leaveBalance.getAmount() == 0 && leaveBalance.getOpeningBalance() == 0) {
            return; // skip zero value
        }

        if (
            !leaveBalanceRepository.existsByEmployeeIdAndLeaveTypeAndYear(
                leaveBalance.getEmployee().getId(),
                leaveBalance.getLeaveType(),
                leaveBalance.getYear()
            )
        ) {
            leaveBalanceRepository.save(leaveBalance);
        }
    }

    private LeaveBalance createLeaveBalanceObject(Employee employee, int year, LeaveType leaveType) {
        LeaveBalance leaveBalance = new LeaveBalance();
        leaveBalance.setYear(year);
        leaveBalance.setLeaveType(leaveType);
        leaveBalance.setOpeningBalance(0);
        leaveBalance.setClosingBalance(0);
        leaveBalance.setConsumedDuringYear(0);
        leaveBalance.setLeaveAmountType(LeaveAmountType.Day);
        leaveBalance.setEmployee(employee);
        return leaveBalance;
    }
}
