package com.bits.hr.service.attendanceTimeSheet;

import com.bits.hr.domain.Config;
import com.bits.hr.repository.ConfigRepository;
import com.bits.hr.service.config.DefinedKeys;
import java.time.LocalDate;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AttendanceSharedService {

    private final ConfigRepository configRepository;

    public AttendanceSharedService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    //day
    public int getMaxAllowedPreviousDays() {
        Optional<Config> config = configRepository.findConfigByKey(DefinedKeys.max_allowed_previous_days_for_change_attendance_status);
        if (!config.isPresent()) {
            throw new RuntimeException("missing key = max_allowed_days_for_change_attendance_status");
        }

        try {
            return Integer.parseInt(config.get().getValue());
        } catch (NumberFormatException exception) {
            log.error("max_allowed_days_for_change_attendance_status = {}", config.get().getValue());
            throw new RuntimeException("failed to parse max_allowed_days_for_change_attendance_status into long");
        }
    }

    // date
    public LocalDate getMaxAllowedPreviousDate() {
        int days = this.getMaxAllowedPreviousDays();
        return LocalDate.now().minusDays(days);
    }

    public String getMaxAllowedPreviousDateCrossingValidationMessage() {
        return String.format("Only the previous %s-day(s) application is allowed.", this.getMaxAllowedPreviousDays());
    }
}
