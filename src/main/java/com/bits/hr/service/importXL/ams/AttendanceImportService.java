package com.bits.hr.service.importXL.ams;

import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.AttendanceEntryService;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.service.mapper.AttendanceEntryMapper;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.PinUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class AttendanceImportService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private AttendanceEntryService attendanceEntryService;

    @Autowired
    private AttendanceEntryMapper attendanceEntryMapper;

    public boolean importFile(MultipartFile file) {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            // 0 -> SL\NO
            // 1 -> overwrite On Exist ( true/false )
            // 2 -> pin
            // 3 -> date
            // 4 -> Take In/Out from system defaults (true/false)
            // 5 -> in time
            // 6 -> out time
            List<String> header1 = data.remove(0);

            for (List<String> dataItems : data) {
                // if no SL/NO , continue
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                // if overwrite on exist false -> skip
                // todo : check existence.
                if (dataItems.get(1).trim().toLowerCase(Locale.ROOT).equals("false")) {
                    continue;
                }

                String pin = PinUtil.formatPin(dataItems.get(2));
                Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(pin);
                if (!employeeOptional.isPresent()) {
                    log.info("employee not present, PIN : " + pin);
                    continue;
                }

                LocalDate date = DateUtil.xlStringToDate(dataItems.get(3));
                boolean isSystemDefaults = dataItems.get(4).trim().toLowerCase(Locale.ROOT).equals("true");

                if (isSystemDefaults) {
                    Instant inTime = Instant.now();
                    inTime = inTime.atZone(ZoneOffset.systemDefault()).withHour(10).withMinute(0).withSecond(0).withNano(0).toInstant();

                    Instant outTime = Instant.now();
                    outTime = outTime.atZone(ZoneOffset.systemDefault()).withHour(18).withMinute(0).withSecond(0).withNano(0).toInstant();

                    AttendanceEntry attendanceEntry = new AttendanceEntry();
                    attendanceEntry.employee(employeeOptional.get()).date(date).inTime(inTime).outTime(outTime);

                    attendanceEntryService.createOrUpdate(attendanceEntryMapper.toDto(attendanceEntry));
                } else {
                    Instant inTime = DateUtil.xlStringToDateTime(dataItems.get(5));
                    Instant outTime;
                    try {
                        outTime = DateUtil.xlStringToDateTime(dataItems.get(6));
                    } catch (Exception ex) {
                        outTime = null;
                    }
                    AttendanceEntry attendanceEntry = new AttendanceEntry();
                    attendanceEntry.employee(employeeOptional.get()).date(date).inTime(inTime).outTime(outTime);
                    attendanceEntryService.createOrUpdate(attendanceEntryMapper.toDto(attendanceEntry));
                }
            }

            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }
}
