package com.bits.hr.service.importXL.employee.helperMethods.entityHelper;

import com.bits.hr.domain.BankBranch;
import com.bits.hr.repository.BankBranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetOrCreateBankBranch {

    @Autowired
    BankBranchRepository bankBranchRepository;

    public BankBranch get(String s) {
        return bankBranchRepository
            .findBankBranchByBranchName(s.trim())
            .orElseGet(() -> {
                BankBranch d = new BankBranch();
                d.setBranchName(s);
                return bankBranchRepository.save(d);
            });
    }
}
