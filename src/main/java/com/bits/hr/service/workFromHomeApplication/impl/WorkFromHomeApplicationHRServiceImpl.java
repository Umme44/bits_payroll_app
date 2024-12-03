package com.bits.hr.service.workFromHomeApplication.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.WorkFromHomeApplication;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.WorkFromHomeApplicationRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.WorkFromHomeApplicationDTO;
import com.bits.hr.service.event.ApprovalVia;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.WorkFromHomeApplicationEvent;
import com.bits.hr.service.importXL.batchOperations.employmentActions.GenericXlsxImportService;
import com.bits.hr.service.mapper.WorkFromHomeApplicationMapper;
import com.bits.hr.service.workFromHomeApplication.WorkFromHomeApplicationHRService;
import com.bits.hr.service.workFromHomeApplication.WorkFromHomeRefreshService;
import com.bits.hr.util.DateUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class WorkFromHomeApplicationHRServiceImpl implements WorkFromHomeApplicationHRService {

    private final Logger log = LoggerFactory.getLogger(WorkFromHomeApplicationHRService.class);
    private final EmployeeRepository employeeRepository;

    private final WorkFromHomeApplicationRepository workFromHomeApplicationRepository;

    private final WorkFromHomeApplicationMapper workFromHomeApplicationMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final WorkFromHomeRefreshService workFromHomeRefreshService;

    private final CurrentEmployeeService currentEmployeeService;

    private  final EmployeeService employeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    private static final String RESOURCE_NAME = "WorkFromHomeApplicationResource";

    @Autowired
    GenericXlsxImportService genericXlsxImportService;


    public WorkFromHomeApplicationHRServiceImpl(
        EmployeeRepository employeeRepository,
        WorkFromHomeApplicationRepository workFromHomeApplicationRepository,
        WorkFromHomeApplicationMapper workFromHomeApplicationMapper,
        ApplicationEventPublisher applicationEventPublisher,
        WorkFromHomeRefreshService workFromHomeRefreshService,
        CurrentEmployeeService currentEmployeeService,
        EmployeeService employeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.employeeRepository = employeeRepository;
        this.workFromHomeApplicationRepository = workFromHomeApplicationRepository;
        this.workFromHomeApplicationMapper = workFromHomeApplicationMapper;
        this.applicationEventPublisher = applicationEventPublisher;
        this.workFromHomeRefreshService = workFromHomeRefreshService;
        this.currentEmployeeService = currentEmployeeService;
        this.employeeService = employeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    // HR Pending Requests
    @Override
    public Page<WorkFromHomeApplicationDTO> getAllPendingApplicationsForHr(String searchText, Pageable pageable) {
        Page<WorkFromHomeApplication> workFromHomeApplications = workFromHomeApplicationRepository.findAllPendingApplications(
            searchText,
            pageable
        );
        List<WorkFromHomeApplicationDTO> workFromHomeApplicationDTOS = workFromHomeApplicationMapper.toDto(
            workFromHomeApplications.getContent()
        );
        List<WorkFromHomeApplicationDTO> finalWorkFromHomeApplicationDTOList = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();
        for (WorkFromHomeApplicationDTO workFromHomeApplicationDTO : workFromHomeApplicationDTOS) {
            List<WorkFromHomeApplication> futureApprovedWFHApplication = workFromHomeApplicationRepository.findFutureApprovedWFHApplication(
                workFromHomeApplicationDTO.getEmployeeId(),
                currentDate,
                PageRequest.of(0, 1)
            );
            if (futureApprovedWFHApplication.size() > 0) {
                workFromHomeApplicationDTO.setApprovedStartDate(futureApprovedWFHApplication.get(0).getStartDate());
            } else {
                workFromHomeApplicationDTO.setApprovedStartDate(null);
            }
            finalWorkFromHomeApplicationDTOList.add(workFromHomeApplicationDTO);
        }
        return new PageImpl<>(finalWorkFromHomeApplicationDTOList, pageable, workFromHomeApplications.getTotalElements());
    }

    // HR All Requests
    @Override
    public Page<WorkFromHomeApplicationDTO> getAllAppliedApplicationsHr(String searchText, Boolean onlineAttendance, Pageable pageable) {
        Page<Employee> employeeList = null;
        if (onlineAttendance == null) {
            employeeList = employeeRepository.findAllEmployeesByPinOrFullName(searchText, pageable);
        } else if (onlineAttendance != null) {
            employeeList = employeeRepository.findAllEmployeesByPinOrFullNameAndOnlineAttendance(searchText, onlineAttendance, pageable);
        } else {
            employeeList = null;
        }
        LocalDate currentDate = LocalDate.now();

        List<WorkFromHomeApplicationDTO> workFromHomeApplicationDTOList = new ArrayList<>();

        for (Employee employee1 : employeeList.getContent()) {
            WorkFromHomeApplicationDTO workFromHomeApplicationDTO = new WorkFromHomeApplicationDTO();
            workFromHomeApplicationDTO.setEmployeeId(employee1.getId());
            workFromHomeApplicationDTO.setFullName(employee1.getFullName());
            workFromHomeApplicationDTO.setDesignationName(employee1.getDesignation().getDesignationName());
            workFromHomeApplicationDTO.setPin(employee1.getPin());
            workFromHomeApplicationDTO.setDepartmentName(employee1.getDepartment().getDepartmentName());
            workFromHomeApplicationDTO.setBandName(employee1.getBand().getBandName());
            workFromHomeApplicationDTO.setIsAllowedToGiveOnlineAttendance(employee1.getIsAllowedToGiveOnlineAttendance());
            List<WorkFromHomeApplication> futureApprovedWFHApplication = workFromHomeApplicationRepository.findFutureApprovedWFHApplication(
                employee1.getId(),
                currentDate,
                PageRequest.of(0, 1)
            );
            if (futureApprovedWFHApplication.size() > 0) {
                workFromHomeApplicationDTO.setApprovedStartDate(futureApprovedWFHApplication.get(0).getStartDate());
            } else {
                workFromHomeApplicationDTO.setApprovedStartDate(null);
            }
            workFromHomeApplicationDTOList.add(workFromHomeApplicationDTO);
        }

        return getWorkFromHomeApplicationDTOS(workFromHomeApplicationDTOList, employeeList, pageable);
    }

    //HR All Inactive online employees
    @Override
    public Page<WorkFromHomeApplicationDTO> getAllInactiveAppliedApplicationsHr(String searchText, Pageable pageable) {
        List<WorkFromHomeApplicationDTO> workFromHomeApplicationDTOList = new ArrayList<>();
        Page<Employee> employeeList = employeeRepository.getEmployeeWhereOnlineAttendanceDisable(searchText, pageable);

        for (Employee employee1 : employeeList.getContent()) {
            WorkFromHomeApplicationDTO workFromHomeApplicationDTO = new WorkFromHomeApplicationDTO();
            workFromHomeApplicationDTO.setEmployeeId(employee1.getId());
            workFromHomeApplicationDTO.setFullName(employee1.getFullName());
            workFromHomeApplicationDTO.setDesignationName(employee1.getDesignation().getDesignationName());
            workFromHomeApplicationDTO.setPin(employee1.getPin());
            workFromHomeApplicationDTO.setDepartmentName(employee1.getDepartment().getDepartmentName());
            workFromHomeApplicationDTO.setBandName(employee1.getBand().getBandName());
            workFromHomeApplicationDTO.setIsAllowedToGiveOnlineAttendance(employee1.getIsAllowedToGiveOnlineAttendance());

            LocalDate currentDate = LocalDate.now();

            List<WorkFromHomeApplication> futureApprovedWFHApplication = workFromHomeApplicationRepository.findFutureApprovedWFHApplication(
                employee1.getId(),
                currentDate,
                PageRequest.of(0, 1)
            );
            if (futureApprovedWFHApplication.size() > 0) {
                workFromHomeApplicationDTO.setApprovedStartDate(futureApprovedWFHApplication.get(0).getStartDate());
            } else {
                workFromHomeApplicationDTO.setApprovedStartDate(null);
            }
            workFromHomeApplicationDTOList.add(workFromHomeApplicationDTO);
        }

        return getWorkFromHomeApplicationDTOS(workFromHomeApplicationDTOList, employeeList, pageable);
    }

    //HR All Active online employees
    @Override
    public Page<WorkFromHomeApplicationDTO> getAllActiveAppliedApplicationsHr(String searchText, Pageable pageable) {
        Page<Employee> employeeList = employeeRepository.getEmployeeWhereOnlineAttendanceEnable(searchText, pageable);
        List<WorkFromHomeApplicationDTO> workFromHomeApplicationDTOList = new ArrayList<>();
        return getWorkFromHomeApplicationDTOS(workFromHomeApplicationDTOList, employeeList, pageable);
    }

    @Override
    public boolean enableSelectedHR(List<Long> selectedIds, User currentUser) {
        LocalDate approvalDate = LocalDate.now();
        try {
            for (long id : selectedIds) {
                Optional<WorkFromHomeApplication> workFromHomeApplicationOptional = workFromHomeApplicationRepository.findById(id);

                if (workFromHomeApplicationOptional.isPresent()) {
                    WorkFromHomeApplication workFromHomeApplication = workFromHomeApplicationOptional.get();

                    if (workFromHomeApplication.getStatus().equals(Status.PENDING)) {
                        workFromHomeApplication.setStatus(Status.APPROVED);
                        workFromHomeApplication.setSanctionedBy(currentUser);
                        workFromHomeApplication.setSanctionedAt(Instant.now());
                    }
                    workFromHomeApplicationRepository.save(workFromHomeApplication);

                    // refresh web-punch
                    Employee employee = employeeRepository.findById(workFromHomeApplication.getEmployee().getId()).get();
                    workFromHomeRefreshService.refreshWFH(approvalDate, employee);
                    publishEvent(workFromHomeApplication, EventType.APPROVED, ApprovalVia.HR);
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage() + "    " + Arrays.toString(ex.getStackTrace()));
            return true;
        }
    }

    @Override
    public boolean disableSelectedHR(List<Long> selectedIds, User currentUser) {
        LocalDate rejectionDate = LocalDate.now();
        try {
            for (long id : selectedIds) {
                Optional<WorkFromHomeApplication> workFromHomeApplicationOptional = workFromHomeApplicationRepository.findById(id);

                if (workFromHomeApplicationOptional.isPresent()) {
                    WorkFromHomeApplication workFromHomeApplication = workFromHomeApplicationOptional.get();
                    workFromHomeApplication.setStatus(Status.NOT_APPROVED);
                    workFromHomeApplication.setSanctionedBy(currentUser);
                    workFromHomeApplication.setSanctionedAt(Instant.now());
                    workFromHomeApplicationRepository.save(workFromHomeApplication);

                    // refresh web-punch
                    Employee employee = employeeRepository.findById(workFromHomeApplication.getEmployee().getId()).get();
                    workFromHomeRefreshService.refreshWFH(rejectionDate, employee);
                    publishEvent(workFromHomeApplication, EventType.REJECTED, ApprovalVia.HR);
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage() + "    " + Arrays.toString(ex.getStackTrace()));
            return true;
        }
    }

    @Override
    public int totalWorkFromHomeActiveEmployees() {
        return employeeRepository.totalOnlineAttendanceEnabled();
    }

    @Override
    public int totalWorkFromHomeInActiveEmployees() {
        return employeeRepository.totalOnlineAttendanceDisabled();
    }

    private Page<WorkFromHomeApplicationDTO> getWorkFromHomeApplicationDTOS(
        List<WorkFromHomeApplicationDTO> workFromHomeApplicationDTOList,
        Page<Employee> employeeList,
        Pageable pageable
    ) {
        List<WorkFromHomeApplicationDTO> newWorkFromHomeList = new ArrayList<>();
        for (Employee employee : employeeList) {
            WorkFromHomeApplicationDTO workFromHomeApplicationDTO = new WorkFromHomeApplicationDTO();
            workFromHomeApplicationDTO.setEmployeeId(employee.getId());
            workFromHomeApplicationDTO.setFullName(employee.getFullName());
            workFromHomeApplicationDTO.setDesignationName(employee.getDesignation().getDesignationName());
            workFromHomeApplicationDTO.setPin(employee.getPin());
            workFromHomeApplicationDTO.setDepartmentName(employee.getDepartment().getDepartmentName());
            workFromHomeApplicationDTO.setBandName(employee.getBand().getBandName());
            workFromHomeApplicationDTO.setIsAllowedToGiveOnlineAttendance(employee.getIsAllowedToGiveOnlineAttendance());
            newWorkFromHomeList.add(workFromHomeApplicationDTO);
        }
        return new PageImpl<>(newWorkFromHomeList, pageable, employeeList.getTotalElements());
    }

    @Override
    public Page<WorkFromHomeApplicationDTO> findWorkFromApplicationBetweenDates(
        Long employeeId,
        LocalDate startDate,
        LocalDate endDate,
        Status status,
        Pageable pageable
    ) {
        Page<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findWorkFromApplicationBetweenDates(
            employeeId,
            startDate,
            endDate,
            status,
            pageable
        );

        return new PageImpl<>(
            workFromHomeApplicationMapper.toDto(workFromHomeApplicationList.getContent()),
            pageable,
            workFromHomeApplicationList.getTotalElements()
        );
    }

    @Override
    public WorkFromHomeApplicationDTO create(WorkFromHomeApplicationDTO workFromHomeApplicationDTO) {
        boolean result =
            this.checkPreviousApplicationBetweenDateRangeOnCreate(
                    workFromHomeApplicationDTO.getEmployeeId(),
                    workFromHomeApplicationDTO.getStartDate(),
                    workFromHomeApplicationDTO.getEndDate()
                );

        if (result) {
            throw new RuntimeException("You have already applied for the duration");
        }

        return this.save(workFromHomeApplicationDTO);
    }

    @Override
    public WorkFromHomeApplicationDTO update(WorkFromHomeApplicationDTO workFromHomeApplicationDTO) {
        boolean result =
            this.checkPreviousApplicationBetweenDateRangeOnUpdate(
                    workFromHomeApplicationDTO.getEmployeeId(),
                    workFromHomeApplicationDTO.getId(),
                    workFromHomeApplicationDTO.getStartDate(),
                    workFromHomeApplicationDTO.getEndDate()
                );
        if (result) {
            throw new RuntimeException("You have already applied for the duration");
        }

        return this.save(workFromHomeApplicationDTO);
    }

    private WorkFromHomeApplicationDTO save(WorkFromHomeApplicationDTO workFromHomeApplicationDTO) {
        log.debug("Request to save WorkFromHomeApplication : {}", workFromHomeApplicationDTO);
        LocalDate today = LocalDate.now();

        long range = ChronoUnit.DAYS.between(workFromHomeApplicationDTO.getStartDate(), workFromHomeApplicationDTO.getEndDate()) + 1;
        workFromHomeApplicationDTO.setDuration(Integer.parseInt(Long.toString(range)));

        Employee employee = employeeRepository.findById(workFromHomeApplicationDTO.getEmployeeId()).get();

        WorkFromHomeApplication savedWorkFromHome = workFromHomeApplicationRepository.save(
            workFromHomeApplicationMapper.toEntity(workFromHomeApplicationDTO)
        );

        workFromHomeRefreshService.refreshWFH(today, employee);

        //publishEvent(saveWorkFromHome, EventType.APPROVED);
        return workFromHomeApplicationMapper.toDto(savedWorkFromHome);
    }

    @Override
    public Optional<WorkFromHomeApplicationDTO> findOne(Long id) {
        log.debug("Request to get WorkFromHomeApplication : {}", id);
        return workFromHomeApplicationRepository.findById(id).map(workFromHomeApplicationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkFromHomeApplication : {}", id);
        WorkFromHomeApplicationDTO workFromHomeApplication = this.findOne(id).get();
        workFromHomeApplicationRepository.deleteById(id);

        // refresh wfh
        LocalDate today = LocalDate.now();
        Employee employee = employeeRepository.findById(workFromHomeApplication.getEmployeeId()).get();
        workFromHomeRefreshService.refreshWFH(today, employee);
    }

    @Override
    public boolean checkPreviousApplicationBetweenDateRangeOnCreate(Long employeeId, LocalDate bookingStartDate, LocalDate bookingEndDate) {
        return workFromHomeApplicationRepository.checkIsAppliedV2(employeeId, bookingStartDate, bookingEndDate);
    }

    @Override
    public boolean checkPreviousApplicationBetweenDateRangeOnUpdate(
        Long employeeId,
        Long workApplicationId,
        LocalDate bookingStartDate,
        LocalDate bookingEndDate
    ) {
        return workFromHomeApplicationRepository.checkIsAppliedForUpdate(employeeId, workApplicationId, bookingStartDate, bookingEndDate);
    }

    private void publishEvent(WorkFromHomeApplication workFromHomeApplication, EventType event, ApprovalVia approvalVia) {
        log.debug("publishing WorkFromHomeApplication event with event: " + event);
        WorkFromHomeApplicationEvent workFromHomeApplicationEvent = new WorkFromHomeApplicationEvent(
            this,
            workFromHomeApplication,
            event,
            approvalVia
        );
        applicationEventPublisher.publishEvent(workFromHomeApplicationEvent);
    }

    @Override
    public boolean workFromHomeBatchUploadByAdmin(MultipartFile file) throws Exception {
        Optional<User> user = currentEmployeeService.getCurrentUser();
        if(!user.isPresent()){
            throw new BadRequestAlertException("User Not Fount", RESOURCE_NAME, "internalServerError");
        }

        // import excel and process
        List<ArrayList<String>> data = genericXlsxImportService.importXlsx(file);

        // take first row as header and remove from data
        List<String> header = data.remove(0);

        for (List<String> dataItems : data) {
            // empty row handling
            if (dataItems.get(0).equals("0")) {
                continue;
            }
            // taking necessary data in local variable
            Optional<Employee> employee = employeeService.findEmployeeByPin(String.valueOf(Double.valueOf(dataItems.get(0)).intValue()));

            if(employee.isPresent()){

                Long employeeId = employee.get().getId();
                LocalDate startDate = DateUtil.doubleToDate(Double.parseDouble(dataItems.get(1)));
                LocalDate endDate = DateUtil.doubleToDate(Double.parseDouble(dataItems.get(2)));

                WorkFromHomeApplicationDTO workFromHomeApplicationDTO = new WorkFromHomeApplicationDTO();

                workFromHomeApplicationDTO.setEmployeeId(employeeId);
                workFromHomeApplicationDTO.setCreatedById(user.get().getId());
                workFromHomeApplicationDTO.setAppliedById(user.get().getId());
                workFromHomeApplicationDTO.setAppliedAt(LocalDate.now());
                workFromHomeApplicationDTO.setCreatedAt(Instant.now());
                workFromHomeApplicationDTO.setStatus(Status.APPROVED);
                workFromHomeApplicationDTO.setSanctionedById(user.get().getId());
                workFromHomeApplicationDTO.setSanctionedAt(Instant.now());
                workFromHomeApplicationDTO.setStartDate(startDate);
                workFromHomeApplicationDTO.setEndDate(endDate);
                workFromHomeApplicationDTO.setReason("Work From Home Application Batch Operation");


                boolean alreadyAppliedInDateRange =
                    this.checkPreviousApplicationBetweenDateRangeOnCreate(
                        workFromHomeApplicationDTO.getEmployeeId(),
                        workFromHomeApplicationDTO.getStartDate(),
                        workFromHomeApplicationDTO.getEndDate()
                    );

                if(!alreadyAppliedInDateRange){
                    WorkFromHomeApplicationDTO result = save(workFromHomeApplicationDTO);
                    eventLoggingPublisher.publishEvent(user.get(), result, RequestMethod.POST, RESOURCE_NAME);
                }
            }
        }
        return true;
    }
}
