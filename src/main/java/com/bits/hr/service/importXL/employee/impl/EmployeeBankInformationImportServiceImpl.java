package com.bits.hr.service.importXL.employee.impl;

import com.bits.hr.domain.BankBranch;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.CardType;
import com.bits.hr.domain.enumeration.DisbursementMethod;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.service.importXL.XLImportCommonService;
import com.bits.hr.service.importXL.employee.helperMethods.entityHelper.GetOrCreateBankBranch;
import com.bits.hr.util.EnumUtil;
import com.bits.hr.util.PinUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class EmployeeBankInformationImportServiceImpl {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private GetOrCreateBankBranch getOrCreateBankBranch;

    public boolean importFile(MultipartFile file) {
        List<Employee> employeeList = new ArrayList<>();
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header = data.remove(0);

            for (List<String> dataItems : data) {
                if (!XLImportCommonService.isXLRowValid(dataItems)) {
                    continue;
                }

                String pin = PinUtil.formatPin(dataItems.get(0));
                Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(pin);
                if (!employeeOptional.isPresent()) {
                    continue;
                }
                Employee employee = employeeOptional.get();

                //  0       1                       2           3                       4               5               6                       7               8               9           10              11          12
                //  PIN	    DisbursementMethod	    BankName	BankAccountNumber	    BkashNumber	    BankBranch	    BranchRoutingNumber	    Card1type	    Card1Number	    Card2type	Card2Number	    Card3type	Card3Number

                DisbursementMethod disbursementMethod = EnumUtil.getDisbursementMethodFromString(dataItems.get(1));
                String bankName = dataItems.get(2);
                String bankAccountNumber = PinUtil.formatPin(dataItems.get(3));
                String bkashNumber = dataItems.get(4);

                String bankBranch = dataItems.get(5);
                String branchRoutingNumber = dataItems.get(6);

                BankBranch bankBranchEntity = getOrCreateBankBranch.get(bankBranch);

                CardType card1type = EnumUtil.getCardTypeFromString(dataItems.get(7));
                String card1Number = dataItems.get(8);
                CardType card2type = EnumUtil.getCardTypeFromString(dataItems.get(9));
                String card2Number = dataItems.get(10);
                CardType card3type = EnumUtil.getCardTypeFromString(dataItems.get(11));
                String card3Number = dataItems.get(12);

                if (disbursementMethod != null) employee.setDisbursementMethod(disbursementMethod);
                if (bankName != "0") employee.setBankName(bankName);
                if (bankAccountNumber != "0") employee.setBankAccountNo(bankAccountNumber);
                if (bkashNumber != "0") employee.setBkashNumber(bkashNumber);
                if (bkashNumber != "0") employee.setBkashNumber(bkashNumber);
                if (bankBranch != "0") employee.setBankBranch(bankBranchEntity);

                if (card1type != null) employee.setCardType(card1type);
                if (card2type != null) employee.setCardType02(card2type);
                if (card3type != null) employee.setCardType03(card3type);

                if (card1Number != "0") employee.setCardNumber(card1Number);
                if (card2Number != "0") employee.setCardNumber(card2Number);
                if (card3Number != "0") employee.setCardNumber(card3Number);

                employeeList.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        save(employeeList);
        return true;
    }

    public void save(List<Employee> employeeist) {
        for (Employee employee : employeeist) {
            employee.setUpdatedAt(LocalDateTime.now());
            employeeRepository.save(employee);
        }
    }
}
