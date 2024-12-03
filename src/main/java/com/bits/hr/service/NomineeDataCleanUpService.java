package com.bits.hr.service;

import com.bits.hr.domain.Nominee;
import com.bits.hr.domain.PfNominee;
import com.bits.hr.repository.NomineeRepository;
import com.bits.hr.repository.PfNomineeRepository;
import com.bits.hr.service.fileOperations.FileOperationService;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class NomineeDataCleanUpService {

    @Autowired
    private PfNomineeRepository pfNomineeRepository;

    @Autowired
    private NomineeRepository nomineeRepository;

    @Autowired
    private FileOperationService fileOperationService;

    public boolean cleanUpPfNomineeData() {
        try {
            List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();

            for (int i = 0; i < pfNomineeList.size(); i++) {
                if (pfNomineeList.get(i).getPhoto() != null) {
                    String imagePath = pfNomineeList.get(i).getPhoto();
                    Boolean isImageExist = fileOperationService.isExist(imagePath);

                    if (isImageExist) {
                        fileOperationService.delete(imagePath);
                    }
                }
                pfNomineeRepository.delete(pfNomineeList.get(i));
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    public boolean cleanUpGfNomineeData() {
        try {
            List<Nominee> gfNomineeList = nomineeRepository.getAllGfNominee();

            for (int i = 0; i < gfNomineeList.size(); i++) {
                if (gfNomineeList.get(i).getImagePath() != null) {
                    String imagePath = gfNomineeList.get(i).getImagePath();
                    Boolean isImageExist = fileOperationService.isExist(imagePath);

                    if (isImageExist) {
                        fileOperationService.delete(imagePath);
                    }
                }
                nomineeRepository.delete(gfNomineeList.get(i));
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    public boolean cleanUpGeneralNomineeData() {
        try {
            List<Nominee> generalNomineeList = nomineeRepository.getAllGeneralNominee();

            for (int i = 0; i < generalNomineeList.size(); i++) {
                if (generalNomineeList.get(i).getImagePath() != null) {
                    String imagePath = generalNomineeList.get(i).getImagePath();
                    Boolean isImageExist = fileOperationService.isExist(imagePath);

                    if (isImageExist) {
                        fileOperationService.delete(imagePath);
                    }
                }
                nomineeRepository.delete(generalNomineeList.get(i));
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }
}
