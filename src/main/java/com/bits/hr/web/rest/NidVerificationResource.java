package com.bits.hr.web.rest;

import com.bits.hr.service.communication.NID.IdentificationDTO;
import com.bits.hr.service.communication.NID.NIDVerificationService;
import com.bits.hr.service.communication.NID.PorichoyRequestBodyDTO;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common/nid-verify")
public class NidVerificationResource {

    @Autowired
    private NIDVerificationService nidVerificationService;

    @PostMapping("")
    public IdentificationDTO verifyNID(@RequestBody PorichoyRequestBodyDTO porichoyRequestBodyDTO) throws URISyntaxException {
        return nidVerificationService.verifyNID(porichoyRequestBodyDTO).get();
    }
}
