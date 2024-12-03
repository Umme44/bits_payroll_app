package com.bits.hr.service.importXL.ams;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.FlexSchedule;
import com.bits.hr.domain.TimeSlot;
import com.bits.hr.domain.User;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.FlexScheduleRepository;
import com.bits.hr.repository.UserRepository;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.PinUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class FlexScheduleImportService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlexScheduleRepository flexScheduleRepository;

    public boolean importFile(MultipartFile file) {
        try {
            List<FlexScheduleImportDTO> flexScheduleImportDTOList = new ArrayList<>();

            List<ArrayList<String>> data = genericUploadService.upload(file);
            // 0 -> SL\NO
            // 1 -> pin
            // 2 -> Effective date
            // 3 -> in time
            // 4 -> out time
            List<String> header1 = data.remove(0);

            for (List<String> dataItems : data) {
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                String pin = PinUtil.formatPin(dataItems.get(1));
                Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(pin);
                if (!employeeOptional.isPresent()) {
                    log.info("employee not present, PIN : " + pin);
                    continue;
                }
                TimeSlot timeSlot = new TimeSlot();
                LocalDate effectiveDate = DateUtil.xlStringToDate(dataItems.get(2));
                LocalDate effectiveEndDate = null;
                Instant inTime = DateUtil.xlStringToDateTime(dataItems.get(3));
                Instant outTime = DateUtil.xlStringToDateTime(dataItems.get(4));
                FlexScheduleImportDTO flexScheduleImportDTO = new FlexScheduleImportDTO(
                    employeeOptional.get(),
                    effectiveDate,
                    effectiveEndDate,
                    inTime,
                    outTime,
                    timeSlot
                );
                flexScheduleImportDTOList.add(flexScheduleImportDTO);
            }

            // cleanup code
            for (FlexScheduleImportDTO flexScheduleImportDTO : flexScheduleImportDTOList) {
                cleanAllFlexSchedulePreviousData(flexScheduleImportDTO.getEmployee());
            }

            for (FlexScheduleImportDTO flexScheduleImportDTO : flexScheduleImportDTOList) {
                applyFlexSchedule(flexScheduleImportDTO);
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    private boolean cleanAllFlexSchedulePreviousData(Employee employee) {
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAllByEmployeeId(employee.getId());
        flexScheduleRepository.deleteAll(flexScheduleList);
        log.info(
            "============= data clean for flex schedule for employee ::" +
            employee.getPin() +
            " - " +
            employee.getFullName() +
            "============="
        );
        return true;
    }

    private boolean applyFlexSchedule(FlexScheduleImportDTO flexScheduleImportDTO) {
        try {
            Employee employee = flexScheduleImportDTO.getEmployee();
            LocalDate effectiveDate = flexScheduleImportDTO.getEffectiveDate();
            Instant inTime = flexScheduleImportDTO.getInTime();
            Instant outTime = flexScheduleImportDTO.getOutTime();

            // todo : 1. update time in employee information
            // todo : 2. insert data in flex schedule
            // todo : 3. don't forget to clear garbage previous data first.
            employee.setCurrentInTime(inTime);
            employee.setCurrentOutTime(outTime);
            employee.setUpdatedAt(LocalDateTime.now());
            employeeRepository.save(employee);

            Optional<User> userOptional = userRepository.findByEmail("system@localhost");
            FlexSchedule flexSchedule = new FlexSchedule();

            flexSchedule.effectiveDate(effectiveDate).inTime(inTime).outTime(outTime).employee(employee);

            if (userOptional.isPresent()) {
                flexSchedule.createdBy(userOptional.orElse(null));
            }
            flexScheduleRepository.save(flexSchedule);
            return true;
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }
}
