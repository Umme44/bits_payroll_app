package com.bits.hr.service.importXL.employee.helperMethods.entityHelper;

import com.bits.hr.domain.Nationality;
import com.bits.hr.repository.NationalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetOrCreateNationality {

    @Autowired
    NationalityRepository nationalityRepository;

    Nationality getOrCreateNationality(String s) {
        return nationalityRepository
            .findNationalityByNationalityName(s.trim())
            .orElseGet(() -> {
                Nationality d = new Nationality();
                d.setNationalityName(s);
                return nationalityRepository.save(d);
            });
    }
}
