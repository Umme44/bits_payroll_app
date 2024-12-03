package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.RecruitmentRequisitionForm;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.RecruitmentNature;
import com.bits.hr.domain.enumeration.RequisitionResourceType;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.RecruitmentRequisitionBudgetRepository;
import com.bits.hr.repository.RecruitmentRequisitionFormRepository;
import com.bits.hr.service.*;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.config.DTO.RRFApprovalDTO;
import com.bits.hr.service.config.GetRrfConfigService;
import com.bits.hr.service.dto.RecruitmentRequisitionBudgetDTO;
import com.bits.hr.service.dto.RecruitmentRequisitionFormDTO;
import com.bits.hr.service.mapper.EmployeeMapper;
import com.bits.hr.service.mapper.RecruitmentRequisitionFormMapper;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RecruitmentRequisitionForm}.
 */
@Log4j2
@Service
@Transactional
public class RecruitmentRequisitionFormServiceImpl implements RecruitmentRequisitionFormService {

    @Autowired
    private RecruitmentRequisitionFormRepository recruitmentRequisitionFormRepository;

    @Autowired
    private RecruitmentRequisitionFormMapper recruitmentRequisitionFormMapper;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private GetRrfConfigService getRrfConfigService;

    @Autowired
    private RecruitmentRequisitionBudgetService recruitmentRequisitionBudgetService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeePinService employeePinService;

    @Override
    public RecruitmentRequisitionFormDTO create(RecruitmentRequisitionFormDTO rrf) {
        log.debug("Request to create RecruitmentRequisitionForm : {}", rrf);
        try {
            if (rrf.getExpectedJoiningDate() != null &&
                rrf.getNumberOfVacancies() != null &&
                rrf.getEmploymentType() != null &&
                rrf.getFunctionalDesignationId() != null &&
                rrf.getBandId() != null
            ) {
                if (rrf.getProject() == null) {
                    rrf.setProject("");
                }
                if (rrf.getDateOfRequisition()==null){
                    rrf.setDateOfRequisition(LocalDate.now());
                }
                if (rrf.getRequestedDate()==null){
                    rrf.setRequestedDate(LocalDate.now());
                }
                rrf.setDepartmentId(rrf.getDepartmentId());
                rrf.setUnitId(rrf.getUnitId());

                rrf.setRrfNumber(genRRF());

                if (rrf.getPreferredEducationType() != null && rrf.getPreferredEducationType().trim().length() >= 2) {
                    rrf.setPreferredEducationType(rrf.getPreferredEducationType().trim());
                } else {
                    rrf.setPreferredEducationType(null);
                }

                rrf.setRequesterId(rrf.getRequesterId());
                rrf.setIsDeleted(false);
                this.loadApprovalFlow(rrf);

                if(rrf.getRequesterId() == rrf.getRecommendedBy02Id()){
                    rrf.setRecommendationDate02(LocalDate.now());
                    rrf.setRequisitionStatus(RequisitionStatus.HOD_APPROVED);
                }

                // if user is HoD, check manpower from budget table
                Optional<RecruitmentRequisitionBudgetDTO> userBudget = recruitmentRequisitionBudgetService.findByEmployeeAndYearAndDepartmentValues(rrf.getRequesterId(), Long.valueOf(rrf.getDateOfRequisition().getYear()), rrf.getDepartmentId());

                if (userBudget.isPresent()){
                    // check if vacancy < manpower ; save if true
                    RecruitmentRequisitionBudgetDTO budget = userBudget.get();
                    Long currentManpower = budget.getRemainingManpower();
//                    Long totalAllowedManpower = currentManpower + getMyTeam(rrf.getRequesterId()).size();
                    Long totalAllowedManpower = currentManpower;
                    if(totalAllowedManpower >= rrf.getNumberOfVacancies()){
                        budget.setRemainingManpower(totalAllowedManpower - rrf.getNumberOfVacancies());
                        recruitmentRequisitionBudgetService.save(budget);
                    }
                    else {
                        throw new BadRequestAlertException("Requested vacancy exceed allocated manpower for current year! Please contact HR", "RecruitmentRequisitionForm", "manpowerExceeded!");
                    }
                }

                return recruitmentRequisitionFormMapper
                    .toDto(
                        recruitmentRequisitionFormRepository
                            .save(
                                recruitmentRequisitionFormMapper.toEntity(rrf)
                            )
                    );

            } else {
                log.error(" Necessary data missing for creating a new RRF ");
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            log.error(ex);
            throw ex;
        }
    }

    @Override
    public RecruitmentRequisitionFormDTO update(RecruitmentRequisitionFormDTO rrf) {
        log.debug("Request to update RecruitmentRequisitionForm : {}", rrf);

        // if user is HoD, check manpower from budget table
        Optional<RecruitmentRequisitionBudgetDTO> userBudget = recruitmentRequisitionBudgetService.findByEmployeeAndYearAndDepartmentValues(rrf.getRequesterId(), Long.valueOf(rrf.getDateOfRequisition().getYear()), rrf.getDepartmentId());

        if (userBudget.isPresent()){
            // check if vacancy < manpower ; save if true
            RecruitmentRequisitionBudgetDTO budget = userBudget.get();
            Long currentManpower = budget.getRemainingManpower();
//            Long totalManpower = currentManpower + rrf.getNumberOfVacancies() + getMyTeam(rrf.getRequesterId()).size();
            Long totalManpower = currentManpower + rrf.getNumberOfVacancies();

            RecruitmentRequisitionFormDTO existingRRF = findOne(rrf.getId()).get();

            if(totalManpower >= rrf.getNumberOfVacancies()){
                if(rrf.getNumberOfVacancies() > existingRRF.getNumberOfVacancies()){
                    Integer diff = rrf.getNumberOfVacancies() - existingRRF.getNumberOfVacancies();
                    budget.setRemainingManpower(currentManpower - Long.valueOf(diff));
                }
                else if (rrf.getNumberOfVacancies() < existingRRF.getNumberOfVacancies()){
                    Integer diff =  existingRRF.getNumberOfVacancies() - rrf.getNumberOfVacancies();
                    budget.setRemainingManpower(currentManpower + Long.valueOf(diff));
                }
                else {

                }
                recruitmentRequisitionBudgetService.save(budget);
            }
            else {
                throw new BadRequestAlertException("Requested vacancy exceed allocated manpower for current year! Please contact HR", "RecruitmentRequisitionForm", "manpowerExceeded!");
            }
        }

        loadApprovalFlow(rrf);

        RecruitmentRequisitionForm savedInDB = recruitmentRequisitionFormRepository.save(
            recruitmentRequisitionFormMapper.toEntity(rrf)
        );
        return recruitmentRequisitionFormMapper.toDto(savedInDB);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecruitmentRequisitionFormDTO> findAll(
        Long requesterId,
        Long departmentId,
        LocalDate startDate,
        LocalDate endDate,
        RequisitionStatus requisitionStatus,
        Pageable pageable
    ) {
        log.debug("Request to get all RecruitmentRequisitionForms");
        return recruitmentRequisitionFormRepository.findAllRrfForAdmin(
                requesterId,
                departmentId,
                startDate,
                endDate,
                requisitionStatus,
                pageable
            )
            .map(recruitmentRequisitionFormMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecruitmentRequisitionFormDTO> findAllRaisedOnBehalf(User user, Employee requester, Pageable pageable) {
        log.debug("Request to get all RecruitmentRequisitionForms");
        return recruitmentRequisitionFormRepository.findAllRaisedOnBehalf(user, requester, pageable)
            .map(recruitmentRequisitionFormMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecruitmentRequisitionFormDTO> findAllRaisedByUser(
        Long employeeId,
        Long departmentId,
        LocalDate startDate,
        LocalDate endDate,
        RequisitionStatus requisitionStatus,
        Pageable pageable,
        User user,
        Employee requester
    ) {
        log.debug("Request to get all RecruitmentRequisitionForms");
        return recruitmentRequisitionFormRepository.findAllRaisedByUser(
                employeeId,
                departmentId,
                startDate,
                endDate,
                requisitionStatus,
                pageable,
                user,
                requester
            )
            .map(recruitmentRequisitionFormMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<RecruitmentRequisitionFormDTO> findOne(Long id) {
        log.debug("Request to get RecruitmentRequisitionForm : {}", id);
        Optional<RecruitmentRequisitionFormDTO> dtoOptional = recruitmentRequisitionFormRepository.findById(id)
            .map(recruitmentRequisitionFormMapper::toDto);

        if(!dtoOptional.isPresent()){
            return Optional.empty();
        } else if (dtoOptional.get().isIsDeleted()) {
            return Optional.empty();
        } else {
            return dtoOptional;
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RecruitmentRequisitionForm : {}", id);
        Optional<RecruitmentRequisitionForm> rrfSaved =
            recruitmentRequisitionFormRepository
                .findById(id);

        rrfSaved.get().setIsDeleted(true);
        rrfSaved.get().setDeletedBy(currentEmployeeService.getCurrentUser().get());

        Optional<RecruitmentRequisitionBudgetDTO> budgetDTO = recruitmentRequisitionBudgetService.findByEmployeeAndYearAndDepartmentValues(rrfSaved.get().getRequester().getId(), Long.valueOf(rrfSaved.get().getDateOfRequisition().getYear()), rrfSaved.get().getDepartment().getId());
        if(budgetDTO.isPresent()){
            Long currentManpower = budgetDTO.get().getRemainingManpower();
            budgetDTO.get().setRemainingManpower(currentManpower + rrfSaved.get().getNumberOfVacancies() );
            recruitmentRequisitionBudgetService.save(budgetDTO.get());
        }

        recruitmentRequisitionFormRepository.save(rrfSaved.get());
    }

    @Override
    public RecruitmentRequisitionFormDTO closeRRF(Long id, User user) {
        Optional<RecruitmentRequisitionForm> requisitionFormOptional = recruitmentRequisitionFormRepository.findById(id);
        if(!requisitionFormOptional.get().getRequisitionStatus().equals(RequisitionStatus.CEO_APPROVED)){
            throw new BadRequestAlertException("RRF status must be CEO Approved", "RecruitmentRequisitionForm", "internalServerError");
        }

        requisitionFormOptional.get().setRequisitionStatus(RequisitionStatus.CLOSED);
        requisitionFormOptional.get().setTotalOnboard(requisitionFormOptional.get().getNumberOfVacancies());
        requisitionFormOptional.get().setUpdatedAt(Instant.now());
        requisitionFormOptional.get().setUpdatedBy(user);

        return recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionFormRepository.save(requisitionFormOptional.get()));
    }

    @Override
    public RecruitmentRequisitionFormDTO closeRRFPartially(Long id, Integer totalOnboard, User user) {
        Optional<RecruitmentRequisitionForm> requisitionFormOptional = recruitmentRequisitionFormRepository.findById(id);
        if(!requisitionFormOptional.get().getRequisitionStatus().equals(RequisitionStatus.CEO_APPROVED)){
            throw new BadRequestAlertException("RRF status must be CEO approved", "RecruitmentRequisitionForm", "internalServerError");
        }

        requisitionFormOptional.get().setRequisitionStatus(RequisitionStatus.PARTIALLY_CLOSED);
        requisitionFormOptional.get().setTotalOnboard(totalOnboard);
        requisitionFormOptional.get().setUpdatedAt(Instant.now());
        requisitionFormOptional.get().setUpdatedBy(user);

        return recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionFormRepository.save(requisitionFormOptional.get()));
    }

//    @Override
//    public boolean changeRRFStatusFromClosedToOpen() {
//        try {
//            List<RecruitmentRequisitionForm> requisitionForms = recruitmentRequisitionFormRepository.findAll();
//
//            for(RecruitmentRequisitionForm rrf : requisitionForms){
//                if(rrf.getRequisitionStatus().equals(RequisitionStatus.CLOSED)){
//                    rrf.setRequisitionStatus(RequisitionStatus.OPEN);
//                    recruitmentRequisitionFormRepository.save(rrf);
//                }
//            }
//            return true;
//        } catch (Exception e){
//            log.error(e);
//            return false;
//        }
//    }

    @Override
    public ExportXLPropertiesDTO exportRRF(
        Long requesterId,
        Long departmentId,
        RequisitionStatus requisitionStatus,
        LocalDate startDate,
        LocalDate endDate,
        Pageable pageable
    ) {
        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAllBySearchParams(
            requesterId,
            departmentId,
            requisitionStatus,
            startDate,
            endDate
        );
//        List<EmployeeNomineeInfo> employeeNomineeInfoList = nomineeService.getEmployeeNomineeInfoList(employeeList);
//
        String sheetName = "Recruitment Requisition Dashboard";
//
        List<String> titleList = new ArrayList<>();
//
        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("SL.");
        tableHeaderList.add("RRF Number");
        tableHeaderList.add("Requisition Date");
        tableHeaderList.add("Requester");
        tableHeaderList.add("Resource Type");
        tableHeaderList.add("Department");
        tableHeaderList.add("Unit");
        tableHeaderList.add("Job Title");
        tableHeaderList.add("Project");
        tableHeaderList.add("Employment Type");
        tableHeaderList.add("Recruitment Nature");
        tableHeaderList.add("Expected Joining Date");
        tableHeaderList.add("Preferred Education");
        tableHeaderList.add("Preferred Skill");
        tableHeaderList.add("Number of Vacancy(s)");
//        tableHeaderList.add("Total Onboard");

        List<List<Object>> dataList = new ArrayList<>();

        for (int i = 0; i < recruitmentRequisitionFormList.size(); i++) {
            List<Object> data = new ArrayList<>();
            data.add(i + 1);
            data.add(recruitmentRequisitionFormList.get(i).getRrfNumber());
            data.add(recruitmentRequisitionFormList.get(i).getRequestedDate());
            data.add(recruitmentRequisitionFormList.get(i).getRequester().getFullName());
            data.add(RequisitionResourceType.requisitionResourceTypeToNaturalText(recruitmentRequisitionFormList.get(i).getResourceType()));
            data.add(recruitmentRequisitionFormList.get(i).getDepartment().getDepartmentName());
            data.add(recruitmentRequisitionFormList.get(i).getUnit().getUnitName());
            data.add(recruitmentRequisitionFormList.get(i).getFunctionalDesignation().getDesignationName());
            data.add(recruitmentRequisitionFormList.get(i).getProject());
            data.add(EmployeeCategory.employeeCategoryEnumToNaturalText(recruitmentRequisitionFormList.get(i).getEmploymentType()));
            data.add(RecruitmentNature.recruitmentNatureEnumToNaturalText(recruitmentRequisitionFormList.get(i).getRecruitmentNature()));
            data.add(recruitmentRequisitionFormList.get(i).getExpectedJoiningDate());
            data.add(recruitmentRequisitionFormList.get(i).getPreferredEducationType());
            data.add(recruitmentRequisitionFormList.get(i).getPreferredSkillType());
            data.add(recruitmentRequisitionFormList.get(i).getNumberOfVacancies());
//            data.add(recruitmentRequisitionFormList.get(i).getTotalOnboard());

            dataList.add(data);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(16);

        return exportXLPropertiesDTO;
    }

    @Override
    public Boolean isRRFAllowedToDelete(Long id) {
        Optional<RecruitmentRequisitionFormDTO> requisitionFormDTO = findOne(id);
        if(!requisitionFormDTO.isPresent()){
            throw new BadRequestAlertException("Recruitment Requisition Form Not Found", id.toString(), "Not Found");
        }

        String rrfNumber = requisitionFormDTO.get().getRrfNumber();

        // if employee is onboarded by RRF, it is not allowed to be deleted
        return !employeePinService.isEmployeeExistByRRF(rrfNumber);
    }

    private String genRRF() {
        String rrfNumber = "";
        for (int i = 1; i <= 1000; i++) {
            rrfNumber = generateRRFNumberNew(i);
            if ((recruitmentRequisitionFormRepository.findByRrfNumber(rrfNumber)).size() > 0) {
                continue;
            } else {
                break;
            }
        }
        return rrfNumber;
    }

    private String generateRRFNumberNew(int itr) {
        LocalDate yearStart = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate yearEnd = LocalDate.of(LocalDate.now().getYear(), 12, 31);
        int number = recruitmentRequisitionFormRepository.countByYearRange(yearStart, yearEnd) + itr;
        // get number of rrf submitted this year
        // generates a number
        // if number exist , number + 1 and regenerate
        // RREQ2022-0003
        String start = "RREQ" + LocalDate.now().getYear() + "-";
        String rrfNumber = start + rrfNumberFormatter(number);
        return rrfNumber;
    }

    private String rrfNumberFormatter(int number) {
        if (number < 10) {
            return "000" + number;
        }
        if (number < 100) {
            return "00" + number;
        }
        if (number < 1000) {
            return "0" + number;
        }
        if (number < 10000) {
            return String.valueOf(number);
        } else {
            return String.valueOf(number);
        }
    }

    public void loadApprovalFlow(RecruitmentRequisitionFormDTO rrfDTO) {
        try {
            RRFApprovalDTO rrfApprovalDTO = getRrfConfigService
                .getRRFApprovalFlow(
                    employeeRepository.findById(rrfDTO.getRequesterId()).get(),rrfDTO.getDepartmentId()
                );

            if (rrfApprovalDTO.getRecommendedByLM() != null) {
                rrfDTO.setRecommendedBy01Id(rrfApprovalDTO.getRecommendedByLM().getId());
            } else {
                rrfDTO.setRecommendedBy01Id(null);
            }

            if (rrfApprovalDTO.getRecommendedByHoD() != null) {
                rrfDTO.setRecommendedBy02Id(rrfApprovalDTO.getRecommendedByHoD().getId());
            } else {
                rrfDTO.setRecommendedBy02Id(null);
            }

            if (rrfApprovalDTO.getRecommendedByCTO() != null) {
                rrfDTO.setRecommendedBy03Id(rrfApprovalDTO.getRecommendedByCTO().getId());
            } else {
                rrfDTO.setRecommendedBy03Id(null);
            }


            if (rrfApprovalDTO.getRecommendedByHoHR() != null) {
                rrfDTO.setRecommendedBy04Id(rrfApprovalDTO.getRecommendedByHoHR().getId());
            } else {
                rrfDTO.setRecommendedBy04Id(null);
            }


            if (rrfApprovalDTO.getRecommendedByCEO() != null) {
                rrfDTO.setRecommendedBy05Id(rrfApprovalDTO.getRecommendedByCEO().getId());
            } else {
                rrfDTO.setRecommendedBy05Id(null);
            }


        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException();
        }
    }

    private List<Long> getMyTeam(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isPresent()){
            List<Long> teamAttendance = new ArrayList<>();
            List<Employee> team = employeeService.getAllReportingTo(employee.get());
            for (Employee member : team) {
                teamAttendance.add(member.getId());
            }

            return teamAttendance;
        }
        else return new ArrayList<>();
    }
}
