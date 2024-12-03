package com.bits.hr.service.importXL.pf;

import static com.bits.hr.service.importXL.pf.rowConstants.PfLoanRowConstants.*;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfLoan;
import com.bits.hr.domain.enumeration.PfLoanStatus;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfLoanRepository;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.service.importXL.pf.helperMethods.PfHelperService;
import com.bits.hr.util.PinUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Log4j2
public class PfLoansImportService implements ImportService {

    @Autowired
    private GetConfigValueByKeyService getConfigValueByKeyService;

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private PfAccountRepository pfAccountRepository;

    @Autowired
    private PfHelperService pfHelperService;

    @Autowired
    private PfLoanRepository pfLoanRepository;

    @Override
    public boolean importFile(MultipartFile file) {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);

            // removing header
            data.remove(0);
            for (ArrayList<String> dataItems : data) {
                // 0  -> SL/NO
                // 1  -> overwrite on exist
                // 2  -> employee Pin
                // 3  -> PF Code
                // 4  -> Disbursement Amount
                // 5  -> Disbursement Date
                // 6  -> Bank Name
                // 7  -> Bank Branch
                // 8  -> Bank Acc. Number
                // 9  -> Cheque Number
                // 10 -> Installment Number
                // 11 -> Installment Amount
                // 12 -> Installment Start From
                // 13 -> Loan Status

                if (dataItems.isEmpty()) {
                    continue;
                }
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                if (dataItems.get(0).equals("")) {
                    continue;
                }

                String pfCode = PinUtil.formatPin(dataItems.get(PF_LN_PF_CODE));
                String pin = PinUtil.formatPin(dataItems.get(PF_LN_PIN));

                PfAccount pfAccount = new PfAccount();
                Optional<PfAccount> pfAccountOptional = pfAccountRepository.getPfAccountByPinAndPfCode(pin, pfCode);
                boolean isPfAccExist = pfAccountOptional.isPresent();
                if (isPfAccExist) {
                    pfAccount = pfAccountOptional.get();
                }
                // if pf account not exist AND isMultiplePfAccountSupported == false
                // ==> create new pf account
                // if pf account not exist AND isMultiplePfAccountSupported == true
                // ==> continue and log no pf account found
                if (isPfAccExist == false) {
                    // create new pf account
                    if (pfHelperService.crateNewPfAccountFromSysDataAndSave(pfCode, pin).isPresent()) {
                        pfAccount = pfHelperService.crateNewPfAccountFromSysDataAndSave(pfCode, pin).get();
                    } else {
                        log.error(
                            "Employee does not exist for the following pin: " +
                            pin +
                            ", pfCode: " +
                            pfCode +
                            ", you might need to create PF Accounts separately."
                        );
                        continue;
                    }
                }
                boolean overwriteIfExist = dataItems.get(PF_LN_OVERWRITE_ON_EXIST).toLowerCase(Locale.ROOT).equals("true");

                double disbursementAmount = Double.parseDouble(dataItems.get(PF_LN_DISBURSEMENT_AMOUNT));
                LocalDate disbursementDate = pfHelperService.stringToDate(dataItems.get(PF_LN_DISBURSEMENT_DATE));

                String bankName = dataItems.get(PF_LN_BANK_NAME).equals("0") ? "" : dataItems.get(PF_LN_BANK_NAME);
                String bankBranch = dataItems.get(PF_LN_BANK_BRANCH).equals("0") ? "" : dataItems.get(PF_LN_BANK_BRANCH);
                String bankAccountNumber = dataItems.get(PF_LN_BANK_ACC_NUMBER).equals("0") ? "" : dataItems.get(PF_LN_BANK_ACC_NUMBER);
                String chequeNumber = dataItems.get(PF_LN_CHEQUE_NUMBER).equals("0") ? "" : dataItems.get(PF_LN_CHEQUE_NUMBER);
                String installmentNumber = dataItems.get(PF_LN_INSTALLMENT_NUMBER);
                double installmentAmount = Double.parseDouble(dataItems.get(PF_LN_INSTALLMENT_AMOUNT));

                LocalDate installmentStartForm = pfHelperService.stringToDate(dataItems.get(PF_LN_INSTALLMENT_START_FROM));
                PfLoanStatus pfLoanStatus = pfHelperService.getPfLoanStatusEnumFromString(dataItems.get(PF_LN_LOAN_STATUS));

                // if pf loan exist && overwriteOnExist == true ==> overwrite
                // if pf loan exist && overwriteOnExist == false ==> continue;
                // if pf collection not exist ==> save new

                Optional<PfLoan> pfLoanOptional = pfLoanRepository.findDuplicatePfLoan(disbursementAmount, disbursementDate, pin, pfCode);
                boolean isPfLoanExist = pfLoanOptional.isPresent();
                if (isPfLoanExist == true && overwriteIfExist == true) {
                    PfLoan pfLoan = pfLoanOptional.get();
                    pfLoan =
                        pfLoanSetValue(
                            pfLoan,
                            pfAccount,
                            disbursementAmount,
                            disbursementDate,
                            bankAccountNumber,
                            bankBranch,
                            bankName,
                            chequeNumber,
                            installmentNumber,
                            installmentAmount,
                            installmentStartForm,
                            pfLoanStatus
                        );
                    pfLoanRepository.save(pfLoan);
                    continue;
                }
                if (isPfLoanExist == true && overwriteIfExist == false) {
                    continue;
                }
                if (!isPfLoanExist) {
                    PfLoan pfLoan = new PfLoan();
                    pfLoan =
                        pfLoanSetValue(
                            pfLoan,
                            pfAccount,
                            disbursementAmount,
                            disbursementDate,
                            bankAccountNumber,
                            bankBranch,
                            bankName,
                            chequeNumber,
                            installmentNumber,
                            installmentAmount,
                            installmentStartForm,
                            pfLoanStatus
                        );
                    pfLoanRepository.save(pfLoan);
                    continue;
                }
            }
            return true;
        } catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    private PfLoan pfLoanSetValue(
        PfLoan pfLoan,
        PfAccount pfAccount,
        double disbursementAmount,
        LocalDate disbursementDate,
        String bankAccountNumber,
        String bankBranch,
        String bankName,
        String chequeNumber,
        String installmentNumber,
        Double installmentAmount,
        LocalDate installmentStartForm,
        PfLoanStatus pfLoanStatus
    ) {
        pfLoan.setPfAccount(pfAccount);
        pfLoan.setDisbursementAmount(disbursementAmount);
        pfLoan.setDisbursementDate(disbursementDate);
        pfLoan.setBankAccountNumber(bankAccountNumber);
        pfLoan.setBankBranch(bankBranch);
        pfLoan.setBankName(bankName);
        pfLoan.setChequeNumber(chequeNumber);
        pfLoan.setInstalmentNumber(installmentNumber);
        pfLoan.setInstallmentAmount(installmentAmount);
        pfLoan.setInstalmentStartFrom(installmentStartForm);
        pfLoan.setStatus(pfLoanStatus);

        return pfLoan;
    }
}
