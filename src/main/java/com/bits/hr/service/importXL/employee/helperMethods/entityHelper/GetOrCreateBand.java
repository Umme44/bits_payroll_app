package com.bits.hr.service.importXL.employee.helperMethods.entityHelper;

import com.bits.hr.domain.Band;
import com.bits.hr.repository.BandRepository;
import com.bits.hr.util.PinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetOrCreateBand {

    @Autowired
    BandRepository bandRepository;

    Band getOrCreateBand(String s) {
        final String band = PinUtil.formatPin(s).trim();
        return bandRepository
            .findBandByBandName(band)
            .orElseGet(() -> {
                Band d = new Band();
                d.setBandName(band);
                d.setMaxSalary(0.0);
                d.setMinSalary(0.0);
                d.setMobileCelling(0.0);
                d.setWelfareFund(0.0);

                return bandRepository.save(d);
            });
    }
}
