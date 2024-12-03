package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.PfNominee;
import com.bits.hr.domain.enumeration.OrganizationFileType;
import com.bits.hr.repository.InsuranceRegistrationRepository;
import com.bits.hr.repository.NomineeRepository;
import com.bits.hr.repository.OfferRepository;
import com.bits.hr.repository.PfNomineeRepository;
import com.bits.hr.service.OrganizationService;
import com.bits.hr.service.fileOperations.FileOperationService;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/files")
public class SharedFileViewResource {

    @Autowired
    private PfNomineeRepository pfNomineeRepository;

    @Autowired
    private NomineeRepository nomineeRepository;

    @Autowired
    private InsuranceRegistrationRepository insuranceRegistrationRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping(value = "/nominee-image/{pfNomineeId}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public @ResponseBody byte[] getNomineeImageWithMediaType(@PathVariable("pfNomineeId") long pfNomineeId) {
        try {
            Optional<PfNominee> pfNominee = pfNomineeRepository.findById(pfNomineeId);
            if (pfNominee.isPresent() && pfNominee.get().getPhoto() != null) {
                return fileOperationService.loadAsByte(pfNomineeRepository.findById(pfNomineeId).get().getPhoto());
            } else {
                byte[] empty = new byte[0];
                return empty;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error(exception.getMessage());
            byte[] empty = new byte[0];
            return empty;
        }
    }

    @GetMapping(value = "/common/nominee-image/{gfNomineeId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getGfNomineeImageWithMediaType(@PathVariable("gfNomineeId") long gfNomineeId) {
        try {
            return fileOperationService.loadAsByte(nomineeRepository.findById(gfNomineeId).get().getImagePath());
        } catch (Exception exception) {
            log.debug(exception.getMessage());
            byte[] empty = new byte[0];
            return empty;
        }
    }

    @GetMapping(value = "/common/insurance-registration-image/{registrationId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getInsuranceRegistrationAccountImageWithMediaType(@PathVariable("registrationId") long registrationId) {
        try {
            return fileOperationService.loadAsByte(insuranceRegistrationRepository.findById(registrationId).get().getPhoto());
        } catch (Exception exception) {
            log.debug(exception.getMessage());
            byte[] empty = new byte[0];
            return empty;
        }
    }

    @GetMapping(value = "/offer-image/{offerId}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public @ResponseBody byte[] getOfferImageWithMediaType(@PathVariable("offerId") long offerId) {
        try {
            return fileOperationService.loadAsByte(offerRepository.findById(offerId).get().getImagePath());
        } catch (Exception exception) {
            log.debug(exception.getMessage());
            byte[] empty = new byte[0];
            return empty;
        }
    }
}
