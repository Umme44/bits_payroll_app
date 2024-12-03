package com.bits.hr.service.importXL.employee.helperMethods.entityHelper;

import com.bits.hr.domain.Unit;
import com.bits.hr.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetOrCreateUnit {

    @Autowired
    private UnitRepository unitRepository;

    public Unit get(String str) {
        return unitRepository
            .findUnitByUnitNameIgnoreCase(str.trim())
            .orElseGet(() -> {
                Unit u = new Unit();
                u.setUnitName(str);
                return unitRepository.save(u);
            });
    }
}
