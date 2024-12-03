package com.bits.hr.service.finalSettlement.helperMethods;

import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfCollection;
import com.bits.hr.repository.PfCollectionRepository;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PfArrearsCollectionService {

    @Autowired
    private PfCollectionRepository pfCollectionRepository;

    public double get(PfAccount pfAccount) {
        // get all arrars PF for following pf account
        List<PfCollection> arrearPfCollectionList = pfCollectionRepository.getArrearPfCollectionByPfAccount(pfAccount.getId());
        // sum all pf amount into single int and send back
        double sum = 0d;
        for (PfCollection pfCollection : arrearPfCollectionList) {
            if (pfCollection.getEmployeeContribution() != null) {
                sum += pfCollection.getEmployeeContribution();
            }
            if (pfCollection.getEmployerContribution() != null) {
                sum += pfCollection.getEmployerContribution();
            }
            if (pfCollection.getEmployeeInterest() != null) {
                sum += pfCollection.getEmployeeInterest();
            }
            if (pfCollection.getEmployerInterest() != null) {
                sum += pfCollection.getEmployerInterest();
            }
        }
        return sum;
    }
}
