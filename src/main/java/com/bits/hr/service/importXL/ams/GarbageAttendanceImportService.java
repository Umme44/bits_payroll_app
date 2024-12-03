package com.bits.hr.service.importXL.ams;

import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.domain.Employee;
import com.bits.hr.repository.AttendanceEntryRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.AttendanceEntryService;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.PinUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class GarbageAttendanceImportService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private AttendanceEntryRepository attendanceEntryRepository;

    @Autowired
    private AttendanceEntryService attendanceEntryService;

    public boolean importFile(MultipartFile file) {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            // 0 -> SL\NO
            // 1 -> pin
            // 2 -> date
            // 3 -> in time
            // 4 -> out time
            List<String> header1 = data.remove(0);

            for (List<String> dataItems : data) {
                // if no SL/NO , continue
                if (dataItems.get(0).equals("0")) {
                    continue;
                }

                String pin = PinUtil.formatPin(dataItems.get(1));

                Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(pin);
                if (!employeeOptional.isPresent()) {
                    log.info("employee not present, PIN : " + pin);
                    continue;
                }

                LocalDate date = DateUtil.xlStringToDate(dataItems.get(2));

                Optional<AttendanceEntry> attendanceEntryOptional = attendanceEntryRepository.findAttendanceEntryByDateAndEmployee(
                    date,
                    employeeOptional.get().getId()
                );

                if (attendanceEntryOptional.isPresent()) {
                    attendanceEntryService.delete(attendanceEntryOptional.get().getId());
                } else {
                    continue;
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }
}
