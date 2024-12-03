package com.bits.hr.service.importXL.flexSchedule;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.*;
import com.bits.hr.service.TimeSlotService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.service.mapper.TimeSlotMapper;
import com.bits.hr.util.PinUtil;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class FlexScheduleImportXLService {

    public static final int Max_COLUMN_SIZE = 9;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private FlexScheduleApplicationRepository flexScheduleApplicationRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private TimeSlotMapper timeSlotMapper;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";

    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static SecureRandom random = new SecureRandom();

    @Transactional
    public void importFile(MultipartFile file) throws Exception {
        List<FlexScheduleApplication> flexScheduleApplicationList = new ArrayList<>();
        List<ArrayList<String>> data = genericUploadService.upload(file);
        List<String> header = data.remove(0);
        if (header.size() != Max_COLUMN_SIZE) {
            throw new RuntimeException("Bad Data Format!");
        }
        Instant inTime;
        Instant outTime;
        String timeSlotTitle;
        String weekEnds;

        // 0 -> SL\NO
        // 1 -> pin
        // 2 -> Effective start date
        // 3 -> Effective end date
        // 4 -> In Time
        // 5 -> Out Time
        // 6 -> TimeSlotTitle
        // 7 -> Weekends

        // created by ...........
        User currentUser = currentEmployeeService.getCurrentUser().get();
        LocalDate currentDate = LocalDate.now();
        Instant currentInstant = Instant.now();
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

            LocalDate effectiveStartDate = doubleToDate(Double.parseDouble(dataItems.get(3)));
            LocalDate effectiveEndDate = doubleToDate(Double.parseDouble(dataItems.get(4)));
            //inTime = DateUtil.xlStringToDateTime(dataItems.get(5));
            //outTime = DateUtil.xlStringToDateTime(dataItems.get(6));
            timeSlotTitle = dataItems.get(7);
            //weekEnds = dataItems.get(8);

            Optional<TimeSlot> timeSlot = timeSlotRepository.findByTitle(timeSlotTitle);
            if (!timeSlot.isPresent()) {
                throw new RuntimeException("TimeSlot Not Found with " + timeSlotTitle + " for PIN:" + employeeOptional.get().getPin());
            }

            FlexScheduleApplication flexScheduleApplication = new FlexScheduleApplication();
            flexScheduleApplication.setEffectiveFrom(effectiveStartDate);
            flexScheduleApplication.setEffectiveTo(effectiveEndDate);
            flexScheduleApplication.setStatus(Status.APPROVED);
            //flexScheduleApplication.setAppliedBy(currentUser);
            flexScheduleApplication.setCreatedBy(currentUser);
            //flexScheduleApplication.setSanctionedBy(currentUser);
            flexScheduleApplication.setRequester(employeeOptional.get());
            flexScheduleApplication.setAppliedAt(currentDate);
            flexScheduleApplication.setCreatedAt(currentInstant);
            flexScheduleApplication.setSanctionedAt(currentInstant);

            // Find TimeSlot by Title & set to application
            flexScheduleApplication.setTimeSlot(timeSlot.get());

            // add to flexScheduleApplication List
            flexScheduleApplicationList.add(flexScheduleApplication);
        }
        // save flexScheduleApplication List to system
        flexScheduleApplicationRepository.saveAll(flexScheduleApplicationList);
    }

    LocalDate doubleToDate(Double d) {
        Date javaDate = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(d);
        LocalDate date = javaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return date;
    }

    public static String generateRandomString(int length) {
        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            // debug
            System.out.format("%d\t:\t%c%n", rndCharAt, rndChar);

            sb.append(rndChar);
        }

        return sb.toString();
    }
}
