package com.bits.hr.service.importXL.employee.helperMethods.entityHelper;

import com.bits.hr.domain.Designation;
import com.bits.hr.repository.DesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetOrCreateDesignation {

    @Autowired
    private DesignationRepository designationRepository;

    public Designation get(String s) {
        return designationRepository
            .findDesignationByDesignationName(s.trim())
            .orElseGet(() -> {
                Designation d = new Designation();
                d.setDesignationName(s.trim());
                return designationRepository.save(d);
            });
    }
}
