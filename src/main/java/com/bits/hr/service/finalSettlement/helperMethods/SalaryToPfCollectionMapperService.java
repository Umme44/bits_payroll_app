package com.bits.hr.service.finalSettlement.helperMethods;

import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfCollection;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.PfCollectionType;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class SalaryToPfCollectionMapperService {

    @Autowired
    private GetConfigValueByKeyService getConfigValueByKeyService;

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    private PfCollectionRepository pfCollectionRepository;

    public void map(PfAccount pfAccount) {
        boolean isMultiplePfAccountSupported = getConfigValueByKeyService.isMultiplePfAccountSupported();
        if (isMultiplePfAccountSupported) {
            log.debug(" Multiple PF account not supported and thus crosschecking salary to pf collection stopped ");
            return;
        }
        // get all salaries by pin
        // check pf collection exist or not
        // if exist but employee / employer contribution not match change data and save
        // if not exist save new entry

        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.getAllByEmployeePin(pfAccount.getPin());

        List<PfCollection> pfCollectionList = pfCollectionRepository.getPfMonthlyCollectionByPfAccount(pfAccount.getId());

        for (EmployeeSalary employeeSalary : employeeSalaryList) {
            int year = employeeSalary.getYear();
            int month = Month.fromEnum(employeeSalary.getMonth());

            Optional<PfCollection> pfCollectionOptional = getPfCollection(pfCollectionList, year, month);
            if (pfCollectionOptional.isPresent()) {
                boolean needRefresh = false;
                PfCollection pfCollection = pfCollectionOptional.get();
                if (pfCollection.getEmployerContribution() != employeeSalary.getPfContribution()) {
                    pfCollection.setEmployerContribution(employeeSalary.getPfContribution());
                    needRefresh = true;
                }
                if (pfCollection.getEmployeeContribution() != employeeSalary.getPfDeduction()) {
                    pfCollection.setEmployeeContribution(employeeSalary.getPfDeduction());
                    needRefresh = true;
                }
                if (pfCollection.getEmployerInterest() == null) {
                    pfCollection.setEmployerInterest(0d);
                    needRefresh = true;
                }
                if (pfCollection.getEmployeeInterest() == null) {
                    pfCollection.setEmployeeInterest(0d);
                    needRefresh = true;
                }
                if (needRefresh) {
                    pfCollectionRepository.save(pfCollection);
                }
            } else {
                // create new and save
                PfCollection pfCollection = new PfCollection();
                pfCollection.setCollectionType(PfCollectionType.MONTHLY);
                pfCollection.setYear(year);
                pfCollection.setMonth(month);
                pfCollection.setPfAccount(pfAccount);
                pfCollection.setEmployeeContribution(employeeSalary.getPfDeduction());
                pfCollection.setEmployerContribution(employeeSalary.getPfContribution());
                pfCollection.setEmployeeInterest(0d);
                pfCollection.setEmployerInterest(0d);
                pfCollectionRepository.save(pfCollection);
            }
        }
    }

    private Optional<PfCollection> getPfCollection(List<PfCollection> pfCollectionList, int year, int month) {
        List<PfCollection> pfCollections = pfCollectionList
            .stream()
            .filter(x -> x.getYear() == year && x.getMonth() == month)
            .collect(Collectors.toList());

        if (pfCollections.size() < 1) {
            return Optional.ofNullable(null);
        } else {
            return Optional.of(pfCollections.get(0));
        }
    }
}
