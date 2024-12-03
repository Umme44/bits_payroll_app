package com.bits.hr.service.salaryGenerationFractional;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfCollection;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.PfAccountStatus;
import com.bits.hr.domain.enumeration.PfCollectionType;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.mapper.EmployeeMapper;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PfCollectionService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PfCollectionRepository pfCollectionRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private PfAccountRepository pfAccountRepository;

    /***
     * there are chance if mistakenly employee salary generated as Regular employee
     * then mistake corrected and generated salary as probationary employee then pf collection entry will stay will stay.
     * pf collection is highly coupled with employee salary module and thus,
     * for every type of employee salary hange there should be a proper reflection in pf collection.,
     *
     * common norms to follow in employee_salary db operations
     * 1. Never use Repository directly, use service methods for crud operation instead.
     * 2. in every service call a method from this service to ensure data consistency.
     ***/

    public void createOrUpdatePfCollections(
        EmployeeSalary employeeSalary,
        Employee employee,
        double employeeContribution,
        double employerContribution,
        int month,
        int year,
        double gross,
        double basic
    ) {
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        Optional<PfCollection> pfCollectionOptional = pfCollectionRepository.getMonthlyPfCollection(employeeDTO.getPin(), year, month);

        /**
         * if employee contribution and employer contribution both 0 ; delete previous entry if exists
         * if(RCE) then create or update Pf Collection alongside pf Account if required
         ***/

        // employee category might change in various conditions , to sync with salary, compare with salary
        boolean isRCE = employeeSalary.getEmployeeCategory().equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE);

        // no pf collection when employee status is not  regular confirmed employee
        if (pfCollectionOptional.isPresent() && !isRCE) {
            pfCollectionRepository.delete(pfCollectionOptional.get());
            return;
        }

        if (isRCE) {
            PfAccount pfAccount = getOrCreatePfAccount(employeeDTO);
            // if present do not overwrite existing data except amount
            // overwriting may result loosing pf interests data
            if (pfCollectionOptional.isPresent()) {
                PfCollection pfCollection = pfCollectionOptional.get();
                pfCollection
                    .employeeContribution(employeeContribution)
                    .employerContribution(employerContribution)
                    .pfAccount(pfAccount)
                    .month(month)
                    .year(year)
                    .gross(gross)
                    .basic(basic)
                    .transactionDate(LocalDate.now())
                    .collectionType(PfCollectionType.MONTHLY);

                if (pfCollection.getEmployerInterest() == null) {
                    pfCollection.setEmployerInterest(0.0);
                }
                if (pfCollection.getEmployeeInterest() == null) {
                    pfCollection.setEmployeeInterest(0.0);
                }
                save(pfCollection);
            } else {
                PfCollection pfCollection = new PfCollection();
                pfCollection
                    .employeeContribution(employeeContribution)
                    .employerContribution(employerContribution)
                    .pfAccount(pfAccount)
                    .month(month)
                    .year(year)
                    .gross(gross)
                    .basic(basic)
                    .transactionDate(LocalDate.now())
                    .collectionType(PfCollectionType.MONTHLY)
                    .employeeInterest(0.0)
                    .employerInterest(0.0);
                save(pfCollection);
            }
        }
    }

    public void alignPfCollectionWithEmployeeSalary(EmployeeSalary employeeSalary) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeSalary.getEmployee().getId());

        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Not Found", "Employee", "idnull");
        }

        createOrUpdatePfCollections(
            employeeSalary,
            employeeOptional.get(),
            employeeSalary.getPfDeduction(),
            employeeSalary.getPfContribution(),
            Month.fromEnum(employeeSalary.getMonth()),
            employeeSalary.getYear(),
            employeeSalary.getMainGrossSalary(),
            employeeSalary.getMainGrossBasicSalary()
        );
    }

    private void save(PfCollection pfCollection) {
        pfCollectionRepository.save(pfCollection);
    }

    public PfAccount getOrCreatePfAccount(EmployeeDTO employeeDTO) {
        Optional<PfAccount> pfAccountOptional = pfAccountRepository.getPfAccountByPinAndPfCode(employeeDTO.getPin(), employeeDTO.getPin());
        if (pfAccountOptional.isPresent()) {
            PfAccount pfAccount = pfAccountOptional.get();
            boolean needRefresh = false;
            if (!pfAccount.getPin().equals(employeeDTO.getPin().trim())) {
                pfAccount.setPin(employeeDTO.getPin().trim());
                needRefresh = true;
            }
            if (!pfAccount.getAccHolderName().equals(employeeDTO.getFullName())) {
                pfAccount.setAccHolderName(employeeDTO.getFullName());
                needRefresh = true;
            }
            if (!pfAccount.getDesignationName().equals(employeeDTO.getDesignationName())) {
                pfAccount.setDesignationName(employeeDTO.getDesignationName());
                needRefresh = true;
            }
            if (!pfAccount.getDepartmentName().equals(employeeDTO.getDepartmentName())) {
                pfAccount.setDepartmentName(employeeDTO.getDepartmentName());
                needRefresh = true;
            }
            if (!pfAccount.getUnitName().equals(employeeDTO.getUnitName())) {
                pfAccount.setUnitName(employeeDTO.getUnitName());
                needRefresh = true;
            }
            if (!pfAccount.getMembershipStartDate().equals(employeeDTO.getDateOfConfirmation())) {
                pfAccount.setMembershipStartDate(employeeDTO.getDateOfConfirmation());
                needRefresh = true;
            }

            if (pfAccount.getStatus() != PfAccountStatus.ACTIVE) {
                pfAccount.setStatus(PfAccountStatus.ACTIVE);
                needRefresh = true;
            }

            if (needRefresh) {
                return pfAccountRepository.save(pfAccount);
            } else {
                return pfAccountOptional.get();
            }
        } else {
            PfAccount pfAccount = new PfAccount();
            pfAccount
                .pfCode(employeeDTO.getPin().trim())
                .pin(employeeDTO.getPin().trim())
                .accHolderName(employeeDTO.getFullName())
                .designationName(employeeDTO.getDesignationName())
                .departmentName(employeeDTO.getDepartmentName())
                .unitName(employeeDTO.getUnitName())
                .membershipStartDate(employeeDTO.getDateOfConfirmation())
                .dateOfJoining(employeeDTO.getDateOfJoining())
                .dateOfConfirmation(employeeDTO.getDateOfConfirmation())
                .status(PfAccountStatus.ACTIVE);
            return pfAccountRepository.save(pfAccount);
        }
    }
}
