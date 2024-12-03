package com.bits.hr.service.workFromHomeApplication;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.service.dto.WorkFromHomeApplicationDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface WorkFromHomeApplicationHRService {
    Page<WorkFromHomeApplicationDTO> getAllPendingApplicationsForHr(String searchText, Pageable pageable);
    Page<WorkFromHomeApplicationDTO> getAllAppliedApplicationsHr(String searchText, Boolean onlineAttendance, Pageable pageable);

    Page<WorkFromHomeApplicationDTO> getAllInactiveAppliedApplicationsHr(String searchText, Pageable pageable);

    Page<WorkFromHomeApplicationDTO> getAllActiveAppliedApplicationsHr(String searchText, Pageable pageable);

    boolean enableSelectedHR(List<Long> selectedIds, User currentUser);

    boolean disableSelectedHR(List<Long> selectedIds, User currentUser);

    int totalWorkFromHomeActiveEmployees();

    int totalWorkFromHomeInActiveEmployees();
    Page<WorkFromHomeApplicationDTO> findWorkFromApplicationBetweenDates(
        Long employeeId,
        LocalDate startDate,
        LocalDate endDate,
        Status status,
        Pageable pageable
    );

    WorkFromHomeApplicationDTO create(WorkFromHomeApplicationDTO workFromHomeApplicationDTO);

    WorkFromHomeApplicationDTO update(WorkFromHomeApplicationDTO workFromHomeApplicationDTO);

    Optional<WorkFromHomeApplicationDTO> findOne(Long id);

    void delete(Long id);

    boolean checkPreviousApplicationBetweenDateRangeOnCreate(Long employeeId, LocalDate bookingStartDate, LocalDate bookingEndDate);

    boolean checkPreviousApplicationBetweenDateRangeOnUpdate(
        Long employeeId,
        Long workApplicationId,
        LocalDate bookingStartDate,
        LocalDate bookingEndDate
    );

    boolean workFromHomeBatchUploadByAdmin(MultipartFile file) throws Exception;
}
