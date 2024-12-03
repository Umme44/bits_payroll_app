package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.SalaryGeneratorMaster;
import com.bits.hr.repository.SalaryGeneratorMasterRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * is finalized = null ===> false
 * is finalized = true ===> locked
 * is finalized = false ===> unlocked
 * */

@Service
@Log4j2
public class SalaryLockService {

    @Autowired
    SalaryGeneratorMasterRepository salaryGeneratorMasterRepository;

    public List<SalaryGeneratorMaster> findAll() {
        dataTuneForLock();
        log.debug("Get all SalaryGeneratorMasters");
        List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAllByOrderByYearDescMonthDesc();
        salaryGeneratorMasterList.sort((s1, s2) -> {
            if (s1.yearAsInt() - s2.yearAsInt() == 0) {
                return s2.monthAsInt() - s1.monthAsInt();
            } else {
                return s2.yearAsInt() - s1.yearAsInt();
            }
        });
        return salaryGeneratorMasterList;
    }

    public boolean lockSelected(List<Long> selectedIds) {
        try {
            dataTuneForLock();
            for (Long id : selectedIds) {
                if (id != null) {
                    Optional<SalaryGeneratorMaster> salaryGeneratorMasterOptional = salaryGeneratorMasterRepository.findById(id);
                    if (salaryGeneratorMasterOptional.isPresent()) {
                        SalaryGeneratorMaster salaryGeneratorMaster = salaryGeneratorMasterOptional.get();
                        if (salaryGeneratorMaster.getIsGenerated() != null && salaryGeneratorMaster.getIsGenerated() == true) {
                            salaryGeneratorMaster.setIsFinalized(true);
                            salaryGeneratorMasterRepository.save(salaryGeneratorMaster);
                        }
                    }
                } else {
                    continue;
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }

    public boolean unlockSelected(List<Long> selectedIds) {
        try {
            dataTuneForLock();
            for (Long id : selectedIds) {
                if (id != null) {
                    Optional<SalaryGeneratorMaster> salaryGeneratorMasterOptional = salaryGeneratorMasterRepository.findById(id);
                    if (salaryGeneratorMasterOptional.isPresent()) {
                        SalaryGeneratorMaster salaryGeneratorMaster = salaryGeneratorMasterOptional.get();
                        if (salaryGeneratorMaster.getIsGenerated() != null && salaryGeneratorMaster.getIsGenerated() == true) {
                            salaryGeneratorMaster.setIsFinalized(false);
                            salaryGeneratorMasterRepository.save(salaryGeneratorMaster);
                        }
                    }
                } else {
                    continue;
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }

    public boolean lockAll() {
        try {
            dataTuneForLock();
            List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository
                .findAll()
                .stream()
                .filter(x -> x.getIsFinalized() != null && x.getIsFinalized() == false)
                .collect(Collectors.toList());
            for (SalaryGeneratorMaster salaryGeneratorMaster : salaryGeneratorMasterList) {
                salaryGeneratorMaster.setIsFinalized(true);
                salaryGeneratorMasterRepository.save(salaryGeneratorMaster);
            }
            return true;
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }

    public boolean unlockAll() {
        try {
            dataTuneForLock();
            // get all which is generated
            // unlock all which is not finalized ( not finalized )
            // this list will be very small and for 20 years there will be only 20*12 = 240 entries
            // please do not filter through java stream API where dataset would be big.
            List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository
                .findAll()
                .stream()
                .filter(x -> x.getIsFinalized() != null && x.getIsFinalized() != false)
                .collect(Collectors.toList());
            for (SalaryGeneratorMaster salaryGeneratorMaster : salaryGeneratorMasterList) {
                salaryGeneratorMaster.setIsFinalized(false);
                salaryGeneratorMasterRepository.save(salaryGeneratorMaster);
            }
            return true;
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }

    public boolean isLocked(String year, String month) {
        dataTuneForLock();
        Optional<SalaryGeneratorMaster> salaryGeneratorMaster = salaryGeneratorMasterRepository.findSalaryGeneratorMastersByYearAndMonth(
            year,
            month
        );
        if (!salaryGeneratorMaster.isPresent()) {
            return false;
        } else if (salaryGeneratorMaster.get().getIsFinalized() == null) {
            return false;
        } else if (salaryGeneratorMaster.get().getIsFinalized() != null && salaryGeneratorMaster.get().getIsFinalized()) {
            return true;
        } else {
            return false;
        }
    }

    private void dataTuneForLock() {
        try {
            List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAll();
            for (SalaryGeneratorMaster salaryGeneratorMaster : salaryGeneratorMasterList) {
                boolean isDataChanged = false;
                if (salaryGeneratorMaster.getIsGenerated() == null) {
                    salaryGeneratorMaster.setIsGenerated(false);
                    isDataChanged = true;
                }
                if (salaryGeneratorMaster.getIsFinalized() == null) {
                    salaryGeneratorMaster.setIsFinalized(false);
                    isDataChanged = true;
                }
                if (isDataChanged) {
                    salaryGeneratorMasterRepository.save(salaryGeneratorMaster);
                }
            }
        } catch (Exception ex) {
            log.error("Exception occurred in data tune");
            log.error(ex);
        }
    }
}
