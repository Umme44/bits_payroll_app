package com.bits.hr.service.importXL.ams;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.EnumUtil;
import com.bits.hr.util.PinUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LeaveApplicationsImportService {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private EmployeeService employeeService;

    public static final int Max_COLUMN_SIZE = 9;

    public boolean importFile(MultipartFile file) {
        try {
            List<LeaveApplication> leaveApplicationList = new ArrayList<>();
            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header = data.remove(0);

            if (header.size() != Max_COLUMN_SIZE) {
                return false;
            }
            for (List<String> dataItems : data) {
                if (dataItems.isEmpty()) continue;
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                //  0       1           2           3           4                       5                           6           7                    8
                //  PIN	    Leave Type  Start Date  End Date    Duration (In Days)	    Phone Number On Leave	    Reason	    Address on leave	 is approved ?

                LeaveApplication leaveApplication = new LeaveApplication();

                String pin = PinUtil.formatPin(dataItems.get(0)).trim();
                Optional<Employee> employeeOptional = employeeService.findEmployeeByPin(pin);
                if (!employeeOptional.isPresent()) {
                    continue; // skip non matching pin
                }
                leaveApplication.setEmployee(employeeOptional.get());
                LeaveType leaveType = EnumUtil.getLeaveTypeFromString(dataItems.get(1));
                LocalDate startDate = DateUtil.xlStringToDate(dataItems.get(2));
                LocalDate endDate = DateUtil.xlStringToDate(dataItems.get(3));
                int durationInDays = (int) Double.parseDouble(dataItems.get(4));
                String phoneNumberOnLeave = getPhoneNumberOnLeave(employeeOptional.get(), dataItems.get(5));
                String reason = dataItems.get(6);
                String addressOnLeave = dataItems.get(7);
                boolean isApproved = EnumUtil.getBooleanFromString(dataItems.get(8));

                leaveApplication.setLeaveType(leaveType);
                leaveApplication.setStartDate(startDate);
                leaveApplication.setEndDate(endDate);
                leaveApplication.setDurationInDay(durationInDays);
                leaveApplication.setPhoneNumberOnLeave(phoneNumberOnLeave);
                leaveApplication.setReason(reason);

                leaveApplication.setDescription("");
                leaveApplication.setAddressOnLeave(addressOnLeave);
                leaveApplication.setIsRejected(false);
                leaveApplication.setRejectionComment("");

                leaveApplication.setIsHRApproved(isApproved);
                leaveApplication.setIsLineManagerApproved(isApproved);

                leaveApplication.setApplicationDate(LocalDate.now());

                leaveApplicationList.add(leaveApplication);
                //save(leaveApplication);
            }
            for (LeaveApplication leaveApplication : leaveApplicationList) {
                save(leaveApplication);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getPhoneNumberOnLeave(Employee employee, String phoneNumber) {
        if (phoneNumber.equals("PERSONAL_NUMBER") || phoneNumber.length() >= 14) {
            return employee.getPersonalContactNo();
        } else if (phoneNumber.equals("PERSONAL_NUMBER")) {
            return employee.getOfficialEmail();
        } else {
            return formatCellNumber(phoneNumber);
        }
    }

    private String formatCellNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replace("-", "");

        if (phoneNumber.length() >= 10) {
            if (phoneNumber.contains("E")) {
                phoneNumber = BigDecimal.valueOf(Double.parseDouble(phoneNumber)).toPlainString();
            }

            if (!phoneNumber.substring(0, 4).equals("+880")) {
                phoneNumber = "+880" + phoneNumber;
            }
        }
        return phoneNumber;
    }

    public void save(LeaveApplication leaveApplication) {
        try {
            // check for duplicate entry
            // if exist by same employee , start date and end date -- skip
            List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.checkDuplicates(
                leaveApplication.getEmployee().getId(),
                leaveApplication.getStartDate(),
                leaveApplication.getEndDate()
            );
            if (!leaveApplicationList.isEmpty()) {
                for (LeaveApplication la : leaveApplicationList) {
                    leaveApplicationRepository.deleteById(la.getId());
                }
            }
            leaveApplicationRepository.save(leaveApplication);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
