package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.service.PfAccountService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.PfAccountDTO;
import com.bits.hr.service.mapper.PfAccountMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PfAccount}.
 */
@Service
@Transactional
public class PfAccountServiceImpl implements PfAccountService {

    private final Logger log = LoggerFactory.getLogger(PfAccountServiceImpl.class);

    private final PfAccountRepository pfAccountRepository;

    private final EmployeeRepository employeeRepository;

    private final CurrentEmployeeService currentEmployeeService;

    private final PfAccountMapper pfAccountMapper;

    public PfAccountServiceImpl(
        PfAccountRepository pfAccountRepository,
        EmployeeRepository employeeRepository,
        CurrentEmployeeService currentEmployeeService,
        PfAccountMapper pfAccountMapper
    ) {
        this.pfAccountRepository = pfAccountRepository;
        this.employeeRepository = employeeRepository;
        this.currentEmployeeService = currentEmployeeService;
        this.pfAccountMapper = pfAccountMapper;
    }

    @Override
    public PfAccountDTO create(PfAccountDTO pfAccountDTO) {
        log.debug("Request to save PfAccount : {}", pfAccountDTO);

        Optional<PfAccount> pfAccountOptional = pfAccountRepository.getPfAccountByPfCode(pfAccountDTO.getPfCode());
        if (pfAccountOptional.isPresent()) {
            throw new BadRequestAlertException("PF Account is already exists", "PFAccount", "entryExists");
        }

        PfAccount pfAccount = pfAccountMapper.toEntity(pfAccountDTO);
        pfAccount = pfAccountRepository.save(pfAccount);
        return pfAccountMapper.toDto(pfAccount);
    }

    @Override
    public PfAccountDTO update(PfAccountDTO pfAccountDTO) {
        log.debug("Request to save PfAccount : {}", pfAccountDTO);

        Optional<PfAccount> pfAccountOptional = pfAccountRepository.getPfAccountByPfCode(pfAccountDTO.getPfCode());
        if (!pfAccountOptional.isPresent()) {
            throw new BadRequestAlertException("PF Account not found", "PFAccount", "idnull");
        }

        boolean isPfCodeSame = pfAccountDTO.getPfCode().equals(pfAccountOptional.get().getPfCode());
        boolean isPinSame = pfAccountDTO.getPin().equals(pfAccountOptional.get().getPin());

        boolean isValidPfAccount = isPfCodeSame && isPinSame;

        if (isValidPfAccount == false) {
            throw new BadRequestAlertException("Found duplicate PF account entry", "PFAccount", "entryExists");
        }

        PfAccount pfAccount = pfAccountMapper.toEntity(pfAccountDTO);
        pfAccount = pfAccountRepository.save(pfAccount);
        return pfAccountMapper.toDto(pfAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PfAccountDTO> findAll(String pin, Pageable pageable) {
        log.debug("Request to get all PfAccounts");
        return pfAccountRepository.findAllByPin(pin, pageable).map(pfAccountMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<PfAccountDTO> findAll() {
        log.debug("Request to get all PfAccounts");
        return pfAccountMapper.toDto(pfAccountRepository.findAll(Sort.by(Sort.Direction.ASC, "pin")));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PfAccountDTO> findOne(Long id) {
        log.debug("Request to get PfAccount : {}", id);
        return pfAccountRepository.findById(id).map(pfAccountMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PfAccount : {}", id);
        try {
            pfAccountRepository.deleteById(id);
        } catch (Exception e) {
            throw new BadRequestAlertException(
                "Unable to delete PF account. This account has one or more PF collection.",
                "PfAccount",
                "internalServerError"
            );
        }
    }

    @Override
    public PfAccount getPfAccountOfCurrentUser() {
        Optional<String> currentEmployeePin = currentEmployeeService.getCurrentEmployeePin();

        if (!currentEmployeePin.isPresent()) {
            throw new NoEmployeeProfileException();
        }

        final String pin = currentEmployeePin.get();

        //find employee by pin
        Optional<Employee> employeeOptional = employeeRepository.findByPin(pin);
        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("No Employee Profile", "employee", "noEmployee");
        }

        final Employee employee = employeeOptional.get();

        // find pf account by pin
        List<PfAccount> pfAccountList = pfAccountRepository.getPfAccountsByPin(pin);

        if (pfAccountList.size() == 0) {
            if (
                employee.getEmployeeCategory() != null &&
                !employee.getEmployeeCategory().equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)
            ) {
                throw new BadRequestAlertException("You have no PF Account", "pfNominee", "noPFAccount");
            } else {
                // create pf account, if no pf account is present for Regular Confirmed Employee
                PfAccount savedPfAccount = createAPfAccountUsingEmployeeInfo(employee);

                pfAccountList.add(savedPfAccount);
            }
        }

        // select 1st pf account
        PfAccount pfAccount = pfAccountList.get(0);

        return pfAccount;
    }

    @Override
    public long getPfAccountIdOfCurrentUser() {
        PfAccount pfAccount = getPfAccountOfCurrentUser();
        return pfAccount.getId();
    }

    private PfAccount createAPfAccountUsingEmployeeInfo(Employee employee) {
        PfAccount pfAccount = pfAccountRepository
            .save(
                new PfAccount()
                    .pin(employee.getPin())
                    .pfCode(employee.getPin())
                    .accHolderName(employee.getFullName())
                    .departmentName(employee.getDepartment().getDepartmentName())
            )
            .designationName(employee.getDesignation().getDesignationName())
            .unitName(employee.getUnit().getUnitName())
            .dateOfJoining(employee.getDateOfJoining())
            .dateOfConfirmation(employee.getDateOfConfirmation())
            .membershipStartDate(employee.getDateOfConfirmation());
        return pfAccount;
    }
}
