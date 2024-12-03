package com.bits.hr.service.scheduler;

import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.domain.AttendanceSyncCache;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.IntegratedAttendance;
import com.bits.hr.domain.enumeration.AttendanceDeviceOrigin;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.AttendanceEntryRepository;
import com.bits.hr.repository.AttendanceServer;
import com.bits.hr.repository.AttendanceSyncCacheRepository;
import com.bits.hr.repository.EmployeeRepository;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Profile("enable-attendance-integration")
@EnableAsync
@Service
public class AttendanceIntegrationScheduler {

    private static final Logger log = LoggerFactory.getLogger(AttendanceIntegrationScheduler.class);

    private final EmployeeRepository employeeRepository;
    private final AttendanceServer attendanceServer;
    private final AttendanceSyncCacheRepository attendanceSyncCacheRepository;
    private final AttendanceEntryRepository attendanceEntryRepository;

    @Value("${spring.application.attendance-server.server-ip}")
    private String ATTENDANCE_SYNC_IP;
    @Value("${spring.application.attendance-server.server-port}")
    private String ATTENDANCE_SYNC_PORT;
    @Value("${spring.application.attendance-server.username}")
    private String ATTENDANCE_SYNC_USERNAME;
    @Value("${spring.application.attendance-server.password}")
    private String ATTENDANCE_SYNC_PASSWORD;

    @Autowired
    private RestTemplate restTemplate;

    public AttendanceIntegrationScheduler(
        EmployeeRepository employeeRepository,
        AttendanceServer attendanceServer,
        AttendanceSyncCacheRepository attendanceSyncCacheRepository,
        AttendanceEntryRepository attendanceEntryRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.attendanceServer = attendanceServer;
        this.attendanceSyncCacheRepository = attendanceSyncCacheRepository;
        this.attendanceEntryRepository = attendanceEntryRepository;
    }

    @Async
    @Scheduled(
        fixedDelayString = "${spring.application.attendance-server.interval}",
        initialDelayString = "${spring.application.attendance-server.start-delay}"
    )
    public void scheduleIntegration() {
        log.debug("Executing scheduler");

        Long last = 0L;
        Optional<AttendanceSyncCache> lastAttendance = attendanceSyncCacheRepository.findTopByOrderByIdDesc();
        if (lastAttendance.isPresent()) last = lastAttendance.get().getId();

//        List<IntegratedAttendance> apiData = apiData(last);
//        if (apiData != null){
            List<IntegratedAttendance> fetchedAttendances = attendanceServer.getData(last);
            if (fetchedAttendances != null) {
                updateAttendanceSyncCache(fetchedAttendances);

                List<AttendanceSyncCache> saved = attendanceSyncCacheRepository.findAllByIdGreaterThanOrderByTimestampAsc(last);
                log.debug("Size: " + saved.size());
                saved.forEach(attendanceSyncCache -> {
                    Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(attendanceSyncCache.getEmployeePin());
                    if (employeeOptional.isPresent()) {
                        Employee employee = employeeOptional.get();
                        LocalDate date = LocalDateTime
                            .ofInstant(attendanceSyncCache.getTimestamp(), ZoneId.of(ZoneId.SHORT_IDS.get("BST")))
                            .toLocalDate();

                        AttendanceEntry attendanceEntry = null;
                        Optional<AttendanceEntry> attendanceEntryOptional = attendanceEntryRepository.findByEmployeeIdAndDate(
                            employee.getId(),
                            date
                        );
                        if (attendanceEntryOptional.isPresent()) {
                            attendanceEntry = attendanceEntryOptional.get();
                            attendanceEntry.setOutTime(attendanceSyncCache.getTimestamp());
                            attendanceEntry.setOutNote("System");
                            attendanceEntry.setPunchOutDeviceOrigin(AttendanceDeviceOrigin.DEVICE);
                        } else {
                            attendanceEntry = new AttendanceEntry();
                            attendanceEntry.setInTime(attendanceSyncCache.getTimestamp());
                            attendanceEntry.setInNote("System");
                            attendanceEntry.setEmployee(employee);
                            attendanceEntry.setDate(date);
                            attendanceEntry.setStatus(Status.APPROVED);
                            attendanceEntry.setPunchInDeviceOrigin(AttendanceDeviceOrigin.DEVICE);
                            attendanceEntry.setPunchOutDeviceOrigin(null);
                        }
                        attendanceEntryRepository.save(attendanceEntry);
                    }
                });
            }
//        }
    }

    @Async
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void databaseCleanup() {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusMonths(2);
        localDateTime = localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        Instant instant = localDateTime.toInstant(ZoneOffset.ofHours(0));
        log.debug("Deleting before time: " + instant.toString());
        attendanceSyncCacheRepository.deleteByTimestampLessThan(instant);
    }

    private List<IntegratedAttendance> apiData(Long last){
        String serverUrl =  ATTENDANCE_SYNC_IP + ":" + ATTENDANCE_SYNC_PORT;
        String url = serverUrl + "/api/attendance?last="+last;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(ATTENDANCE_SYNC_USERNAME, ATTENDANCE_SYNC_PASSWORD);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<List<IntegratedAttendance>> responseEntity;
        try{
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<IntegratedAttendance>>() {});
            return responseEntity.getBody();
        }
        catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    public void updateAttendanceSyncCache(List<IntegratedAttendance> integratedAttendanceList){
        List<AttendanceSyncCache> attendanceSyncCaches = integratedAttendanceList
            .stream()
            .map(attendance ->
                new AttendanceSyncCache(
                    attendance.getId(),
                    attendance.getEmployeePin(),
                    attendance.getTimestamp(),
                    attendance.getTerminal()
                )
            )
            .collect(Collectors.toList());
        attendanceSyncCacheRepository.saveAll(attendanceSyncCaches);
        log.debug(attendanceSyncCaches.toString());
    }
}
