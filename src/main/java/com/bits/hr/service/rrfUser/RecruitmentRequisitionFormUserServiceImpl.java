package com.bits.hr.service.rrfUser;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.RecruitmentRequisitionForm;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.RecruitmentRequisitionFormRepository;
import com.bits.hr.service.RecruitmentRequisitionBudgetService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.config.DTO.RRFApprovalDTO;
import com.bits.hr.service.config.GetRrfConfigService;
import com.bits.hr.service.dto.RecruitmentRequisitionBudgetDTO;
import com.bits.hr.service.dto.RecruitmentRequisitionFormDTO;
import com.bits.hr.service.dto.RrfRaiseValidityDTO;
import com.bits.hr.service.mapper.RecruitmentRequisitionFormMapper;
import java.time.LocalDate;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RecruitmentRequisitionFormUserServiceImpl implements RecruitmentRequisitionFormUserService {

    @Autowired
    private RecruitmentRequisitionFormRepository recruitmentRequisitionFormRepository;

    @Autowired
    private RecruitmentRequisitionFormMapper recruitmentRequisitionFormMapper;

    @Autowired
    private GetRrfConfigService getRrfConfigService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private RecruitmentRequisitionBudgetService recruitmentRequisitionBudgetService;


    @Override
    public Optional<RecruitmentRequisitionFormDTO> getById(Long id, Employee currentEmployee) {
        Optional<RecruitmentRequisitionForm> recruitmentRequisitionForm = recruitmentRequisitionFormRepository.findById(id);
        if (!recruitmentRequisitionForm.isPresent()){
            return Optional.empty();
        }

        if (recruitmentRequisitionForm.get().isIsDeleted()){
            return Optional.empty();
        }

        long currentEmployeeId = currentEmployee.getId();

        boolean areCurrentEmployeeAndRequesterSame = recruitmentRequisitionForm.get().getRequester() != null && recruitmentRequisitionForm.get().getRequester().getId().equals(currentEmployeeId);
        boolean canCurrentEmployeeRaiseRRFOnBehalf = currentEmployee.getCanRaiseRrfOnBehalf() != null && currentEmployee.getCanRaiseRrfOnBehalf();
        boolean areCurrentEmployeeAndRecBy01Same = recruitmentRequisitionForm.get().getRecommendedBy01() != null && recruitmentRequisitionForm.get().getRecommendedBy01().getId().equals(currentEmployeeId);
        boolean areCurrentEmployeeAndRecBy02Same = recruitmentRequisitionForm.get().getRecommendedBy02() != null &&
            recruitmentRequisitionForm.get().getRecommendedBy02().getId().equals(currentEmployeeId);

        boolean areCurrentEmployeeAndRecBy03Same = recruitmentRequisitionForm.get().getRecommendedBy03() != null &&
            recruitmentRequisitionForm.get().getRecommendedBy03().getId().equals(currentEmployeeId);

        boolean areCurrentEmployeeAndRecBy04Same = recruitmentRequisitionForm.get().getRecommendedBy04() != null &&
            recruitmentRequisitionForm.get().getRecommendedBy04().getId().equals(currentEmployeeId);

        if (areCurrentEmployeeAndRequesterSame || canCurrentEmployeeRaiseRRFOnBehalf ||
            areCurrentEmployeeAndRecBy01Same || areCurrentEmployeeAndRecBy02Same ||
            areCurrentEmployeeAndRecBy03Same || areCurrentEmployeeAndRecBy04Same)
        {
            return recruitmentRequisitionForm.map(recruitmentRequisitionFormMapper::toDto);
        }else {
            return Optional.empty();
        }
    }

    @Override
    public RecruitmentRequisitionFormDTO create(RecruitmentRequisitionFormDTO rrf, Employee currentEmployee) {
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
                rrf.setDateOfRequisition(LocalDate.now());
                if (currentEmployee.getDepartment() != null) {
                    rrf.setDepartmentId(currentEmployee.getDepartment().getId());
                }

                if (currentEmployee.getUnit() != null) {
                    rrf.setUnitId(currentEmployee.getUnit().getId());
                }
                rrf.setRrfNumber(genRRF());

                if (rrf.getPreferredEducationType() != null && rrf.getPreferredEducationType().trim().length() >= 2) {
                    rrf.setPreferredEducationType(rrf.getPreferredEducationType().trim());
                } else {
                    rrf.setPreferredEducationType(null);
                }

                rrf.setRequesterId(currentEmployee.getId());
                rrf.setRequestedDate(LocalDate.now());

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
                    if(currentManpower >= rrf.getNumberOfVacancies()){
                        budget.setRemainingManpower(currentManpower - rrf.getNumberOfVacancies());
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
    public Optional<RecruitmentRequisitionFormDTO> update(RecruitmentRequisitionFormDTO rrf) {

        Optional<RecruitmentRequisitionForm> rrfSaved = recruitmentRequisitionFormRepository
            .findByIdAndEmployeeId(rrf.getId(), rrf.getRequesterId());

        log.debug("Request to update RecruitmentRequisitionForm : {}", rrf);

        // if user is HoD, check manpower from budget table
        Optional<RecruitmentRequisitionBudgetDTO> userBudget = recruitmentRequisitionBudgetService.findByEmployeeAndYearAndDepartmentValues(rrf.getRequesterId(), Long.valueOf(rrf.getDateOfRequisition().getYear()), rrf.getDepartmentId());

        if (userBudget.isPresent()){
            // check if vacancy < manpower ; save if true
            RecruitmentRequisitionBudgetDTO budget = userBudget.get();
            Long currentManpower = budget.getRemainingManpower();
            Long totalManpower = currentManpower + rrf.getNumberOfVacancies();

            if(totalManpower >= rrf.getNumberOfVacancies()){
                if(rrf.getNumberOfVacancies() > rrfSaved.get().getNumberOfVacancies()){
                    Integer diff = rrf.getNumberOfVacancies() - rrfSaved.get().getNumberOfVacancies();
                    budget.setRemainingManpower(currentManpower - Long.valueOf(diff));
                }
                else if (rrf.getNumberOfVacancies() < rrfSaved.get().getNumberOfVacancies()){
                    Integer diff =  rrfSaved.get().getNumberOfVacancies() - rrf.getNumberOfVacancies();
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

        if (!rrfSaved.isPresent()) {
            throw new BadRequestAlertException("You are not authorize to update", "RRF", "updateAuthorizeError");
        } else {
            // check saved application status, if not in pending throw Exception
            if (rrfSaved.get().getRequisitionStatus() != RequisitionStatus.PENDING) {
                throw new BadRequestAlertException("Update Failed! Application on Progress", "RRF", "updateAuthorizeError");
            }
        }

        if (
            rrfSaved.get().getRecommendationDate01() == null &&
                rrfSaved.get().getRecommendationDate02() == null &&
                rrfSaved.get().getRecommendationDate03() == null &&
                rrfSaved.get().getRecommendationDate04() == null
        ) {
            if (rrf.getPreferredEducationType() != null && rrf.getPreferredEducationType().trim().length() >= 2) {
                rrf.setPreferredEducationType(rrf.getPreferredEducationType().trim());
            } else {
                rrf.setPreferredEducationType(null);
            }

            loadApprovalFlow(rrf);

            RecruitmentRequisitionForm savedInDB = recruitmentRequisitionFormRepository.save(
                recruitmentRequisitionFormMapper.toEntity(rrf)
            );
            return Optional.of(recruitmentRequisitionFormMapper.toDto(savedInDB));
        } else {
            return Optional.empty();
        }

    }


    @Override
    public boolean delete(Long id) {
        Optional<RecruitmentRequisitionForm> rrfSaved =
            recruitmentRequisitionFormRepository
                .findById(id);
        if (!rrfSaved.isPresent()) return false;

        if (rrfSaved.get().getRecommendationDate01() == null &&
            rrfSaved.get().getRecommendationDate02() == null &&
            rrfSaved.get().getRecommendationDate03() == null &&
            rrfSaved.get().getRecommendationDate04() == null)
        {
            rrfSaved.get().setIsDeleted(true);
            rrfSaved.get().setDeletedBy(currentEmployeeService.getCurrentUser().get());

            Optional<RecruitmentRequisitionBudgetDTO> budgetDTO = recruitmentRequisitionBudgetService.findByEmployeeAndYearAndDepartmentValues(rrfSaved.get().getRequester().getId(), Long.valueOf(rrfSaved.get().getDateOfRequisition().getYear()), rrfSaved.get().getDepartment().getId());
            if(budgetDTO.isPresent()){
                Long currentManpower = budgetDTO.get().getRemainingManpower();
                budgetDTO.get().setRemainingManpower(currentManpower + rrfSaved.get().getNumberOfVacancies() );
                recruitmentRequisitionBudgetService.save(budgetDTO.get());
            }

            recruitmentRequisitionFormRepository.save(rrfSaved.get());
            return true;
        } else {
            return false;
        }
    }


    @Override
    public Page<RecruitmentRequisitionFormDTO> findAllByRequester(Employee employee, RequisitionStatus requisitionStatus, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return recruitmentRequisitionFormRepository.getAllCreatedByEmployee(employee.getId(), requisitionStatus, startDate, endDate, pageable)
            .map(recruitmentRequisitionFormMapper::toDto);
    }


    private String genRRF() {
        String rrfNumber = "";
        for (int i = 0; i <= 1000; i++) {
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

    @Deprecated
    private String generateRRFNumber(int itr) {

        LocalDate yearStart = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate yearEnd = LocalDate.of(LocalDate.now().getYear(), 12, 31);

        int number = recruitmentRequisitionFormRepository.countByYearRange(yearStart, yearEnd) + itr;

        // get number of rrf submitted this year
        // generates a number
        // if number exist , number + 1 and regenerate
        // RREQ2022-0003
        String start = "RREQ" + LocalDate.now().getYear() + "-";

        String rrfNumber = start + rrfNumberFormatter(number);

        if (recruitmentRequisitionFormRepository.findByRrfNumber(rrfNumber).size() > 0) {
            generateRRFNumber(itr + 1);
        }

        //List<String> listOfGeneratedRRFNumber = new ArrayList<>();
        //listOfGeneratedRRFNumber.add(rrfNumber);


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
                    employeeRepository.findById(rrfDTO.getRequesterId()).get(),
                    rrfDTO.getDepartmentId()
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

    /**
     * Except Intern, all employee can raise RRF own.
     * If getCanRaiseRRFOnBehalf true in @Employee => RRF can be raised on behalf
     * @param employee
     * @return
     */
    @Override
    public RrfRaiseValidityDTO canRaiseRRF(Employee employee) {
        RrfRaiseValidityDTO rrfRaiseValidityDTO = new RrfRaiseValidityDTO();
        if (employee.getEmployeeCategory() != null &&
            !employee.getEmployeeCategory().equals(EmployeeCategory.INTERN)){
            rrfRaiseValidityDTO.setCanRaiseRRFOwn(true);
        }else {
            rrfRaiseValidityDTO.setCanRaiseRRFOwn(false);
        }

        if (employee.getCanRaiseRrfOnBehalf() != null){
            rrfRaiseValidityDTO.setCanRaiseRRFOnBehalf(employee.getCanRaiseRrfOnBehalf());
        }else {
            rrfRaiseValidityDTO.setCanRaiseRRFOnBehalf(false);
        }
        return rrfRaiseValidityDTO;
    }
}
