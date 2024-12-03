package com.bits.hr.service.impl;

import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfLoan;
import com.bits.hr.domain.PfLoanRepayment;
import com.bits.hr.domain.enumeration.PfLoanStatus;
import com.bits.hr.domain.enumeration.PfRepaymentStatus;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfLoanRepaymentRepository;
import com.bits.hr.repository.PfLoanRepository;
import com.bits.hr.service.PfLoanRepaymentService;
import com.bits.hr.service.PfLoanService;
import com.bits.hr.service.dto.PfLoanDTO;
import com.bits.hr.service.mapper.PfLoanMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PfLoan}.
 */
@Service
@Transactional
public class PfLoanServiceImpl implements PfLoanService {

    private final Logger log = LoggerFactory.getLogger(PfLoanServiceImpl.class);

    private final PfLoanRepository pfLoanRepository;

    private final PfLoanRepaymentRepository pfLoanRepaymentRepository;

    private final PfLoanRepaymentService pfLoanRepaymentService;

    private final PfLoanMapper pfLoanMapper;

    private final PfAccountRepository pfAccountRepository;

    private final EmployeeRepository employeeRepository;

    public PfLoanServiceImpl(
        PfLoanRepository pfLoanRepository,
        PfLoanMapper pfLoanMapper,
        PfLoanRepaymentRepository pfLoanRepaymentRepository,
        PfLoanRepaymentService pfLoanRepaymentService,
        PfAccountRepository pfAccountRepository,
        EmployeeRepository employeeRepository
    ) {
        this.pfLoanRepository = pfLoanRepository;
        this.pfLoanMapper = pfLoanMapper;
        this.pfLoanRepaymentRepository = pfLoanRepaymentRepository;
        this.pfLoanRepaymentService = pfLoanRepaymentService;
        this.pfAccountRepository = pfAccountRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public PfLoanDTO save(PfLoanDTO pfLoanDTO) {
        log.debug("Request to save PfLoan : {}", pfLoanDTO);
        PfLoan pfLoan = pfLoanMapper.toEntity(pfLoanDTO);
        pfLoan = pfLoanRepository.save(pfLoan);
        return pfLoanMapper.toDto(pfLoan);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PfLoanDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PfLoans");
        return pfLoanRepository.findAll(pageable).map(pfLoanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PfLoanDTO> findOne(Long id) {
        log.debug("Request to get PfLoan : {}", id);
        return pfLoanRepository.findById(id).map(pfLoanMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PfLoan : {}", id);
        pfLoanRepository.deleteById(id);
    }

    @Override
    public Optional<PfLoanRepayment> getEmployeeMonthlyRepaymentAmount(long employeeId, int year, int month) {
        String pin = employeeRepository.getPinByEmployeeId(employeeId);
        // check pf account , if not available return 0;
        List<PfAccount> pfAccountList = pfAccountRepository.getPfAccountsByPin(pin);
        if (pfAccountList.isEmpty()) {
            return Optional.empty();
        }

        //get 1st pf account to save in pf loan
        PfAccount pfAccount = pfAccountList.get(0);

        LocalDate monthFirstDate = LocalDate.of(year, month, 1);
        List<PfLoan> pfLoanList = pfLoanRepository.findPfLoanByPinInstallmentYearAndMonth(pin, monthFirstDate);

        if (pfLoanList.isEmpty()) {
            return Optional.empty();
        }

        // check all pf loan and update loan status to repaying to paid off if pf loan repayment paid in full amount
        List<PfLoan> pfLoanRepayingList = new ArrayList<>();
        for (PfLoan pfLoan : pfLoanList) {
            double paidAmount = this.getTotalPfLoanRepaid(pfLoan.getId());
            double anyDue = pfLoan.getDisbursementAmount() - paidAmount;
            if (anyDue <= 0) {
                // update if status change.
                pfLoan.setStatus(PfLoanStatus.PAID_OFF);
                pfLoanRepository.save(pfLoan);
            } else {
                pfLoan.setStatus(PfLoanStatus.OPEN_REPAYING);
                pfLoanRepository.save(pfLoan);
                pfLoanRepayingList.add(pfLoan);
            }
        }
        // get list of repaying pf loan
        if (pfLoanRepayingList.isEmpty()) {
            return Optional.empty();
        }

        //List 1st PfLoan, status = 'REPAYING'
        PfLoan pfLoan = pfLoanRepayingList.get(0);

        Long pfLoanId = pfLoan.getId();

        double disbursementAmount = pfLoan.getDisbursementAmount();
        double paidRepaymentAmount = getTotalPfLoanRepaid(pfLoanId);

        //remaining repayments
        double remainingRepaymentAmount = disbursementAmount - paidRepaymentAmount;

        double monthlyRepayment = 0d;

        if (pfLoan.getInstallmentAmount() <= remainingRepaymentAmount) {
            monthlyRepayment = pfLoan.getInstallmentAmount();
        } else {
            monthlyRepayment = remainingRepaymentAmount;
            pfLoan.setStatus(PfLoanStatus.PAID_OFF);
            pfLoanRepository.save(pfLoan);
        }

        //check this month repayment done already (if done, then update the amount)
        Optional<PfLoanRepayment> monthWisePfLoanRepayment = pfLoanRepaymentRepository.findPfLoanRepaymentByYearAndMonth(month, year);
        if (monthWisePfLoanRepayment.isPresent()) {
            PfLoanRepayment existingPfLoanRepayment = monthWisePfLoanRepayment.get();
            existingPfLoanRepayment.setAmount(monthlyRepayment);
            pfLoanRepaymentRepository.save(existingPfLoanRepayment);
            return Optional.of(existingPfLoanRepayment);
        }

        //save pf loan repayment
        PfLoanRepayment pfLoanRepayment = mapToPfLoanRepayment(pfLoan, monthlyRepayment, month, year);
        return Optional.of(pfLoanRepaymentRepository.save(pfLoanRepayment));
    }

    private PfLoanRepayment mapToPfLoanRepayment(PfLoan pfLoan, double repaymentAmount, int month, int year) {
        PfLoanRepayment pfLoanRepayment = new PfLoanRepayment();
        pfLoanRepayment.setPfLoan(pfLoan);
        pfLoanRepayment.setAmount(repaymentAmount);
        pfLoanRepayment.setDeductionMonth(month);
        pfLoanRepayment.setDeductionYear(year);
        pfLoanRepayment.setStatus(PfRepaymentStatus.SUBMITTED);
        pfLoanRepayment.setDeductionDate(LocalDate.now());

        return pfLoanRepayment;
    }

    private double getTotalPfLoanRepaid(Long pfLoanId) {
        //find total done repayment (except this month and year)  amount for this pf loan
        List<PfLoanRepayment> pfLoanRepayments = pfLoanRepaymentRepository.findAllPfLoanRepaymentByPfLoanId(pfLoanId);

        double totalCompletedRepayments = pfLoanRepayments.stream().mapToDouble(PfLoanRepayment::getAmount).sum();
        return totalCompletedRepayments;
    }
}
