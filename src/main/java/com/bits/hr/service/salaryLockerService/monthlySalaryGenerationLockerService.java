package com.bits.hr.service.salaryLockerService;

import com.bits.hr.domain.SalaryGeneratorMaster;
import com.bits.hr.repository.SalaryGeneratorMasterRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 *
 *
 * */
@Service
@Transactional
@Log4j2
public class monthlySalaryGenerationLockerService {

    @Autowired
    SalaryGeneratorMasterRepository salaryGeneratorMasterRepository;

    public boolean batchLock(List<Long> ids) {
        try {
            for (Long id : ids) {
                Optional<SalaryGeneratorMaster> salaryGeneratorMaster = salaryGeneratorMasterRepository.findById(id);
                if (salaryGeneratorMaster.isPresent()) {
                    lockMonthlySalary(salaryGeneratorMaster.get());
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage() + ex.getStackTrace());
            return false;
        }
    }

    public boolean batchUnlock(List<Long> ids) {
        try {
            for (Long id : ids) {
                Optional<SalaryGeneratorMaster> salaryGeneratorMaster = salaryGeneratorMasterRepository.findById(id);
                if (salaryGeneratorMaster.isPresent()) {
                    lockMonthlySalary(salaryGeneratorMaster.get());
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage() + ex.getStackTrace());
            return false;
        }
    }

    public void lockMonthlySalary(SalaryGeneratorMaster salaryGeneratorMaster) {
        if (salaryGeneratorMaster.getIsFinalized() == false) {
            salaryGeneratorMaster.setIsFinalized(true);
            salaryGeneratorMasterRepository.save(salaryGeneratorMaster);
        }
    }

    public void unlockMonthlySalary(SalaryGeneratorMaster salaryGeneratorMaster) {
        if (salaryGeneratorMaster.getIsFinalized() == true) {
            salaryGeneratorMaster.setIsFinalized(false);
            salaryGeneratorMasterRepository.save(salaryGeneratorMaster);
        }
    }
}
