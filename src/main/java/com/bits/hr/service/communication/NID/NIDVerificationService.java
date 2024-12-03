package com.bits.hr.service.communication.NID;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Nominee;
import com.bits.hr.domain.PfNominee;
import com.bits.hr.domain.enumeration.IdentityType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class NIDVerificationService {

    @Autowired
    private RestTemplate template;

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private final String VERIFICATION_URI;

    private final String URI_IS_LIVE;

    @Value("${spring.application.nid-server.security-header-name}")
    private String SECURITY_HEADER_NAME;

    @Value("${spring.application.nid-server.security-header-value}")
    private String SECURITY_HEADER_VALUE;

    public NIDVerificationService(
        @Value("${spring.application.nid-server.verification-uri}") String verificationUri,
        @Value("${spring.application.nid-server.verification-uri}") String uriIsLive
    ) {
        this.VERIFICATION_URI = UriComponentsBuilder.fromHttpUrl(verificationUri).queryParam("source", "2").encode().toUriString();
        this.URI_IS_LIVE = UriComponentsBuilder.fromHttpUrl(uriIsLive).queryParam("source", "2").encode().toUriString();
    }

    public Optional<IdentificationDTO> verifyNID(PorichoyRequestBodyDTO porichoyRequestBodyDTO) {
        try {
            if (!isLiveUrlOn()) {
                turnOnLiveUrl();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(SECURITY_HEADER_NAME, SECURITY_HEADER_VALUE);

            HttpEntity<PorichoyRequestBodyDTO> entity = new HttpEntity<>(porichoyRequestBodyDTO, headers);
            Optional<IdentificationDTO> identificationDTOOptional = Optional.of(
                template.postForObject(VERIFICATION_URI, entity, IdentificationDTO.class)
            );
            return identificationDTOOptional;
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    private boolean isLiveUrlOn() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(SECURITY_HEADER_NAME, SECURITY_HEADER_VALUE);

            return template.getForObject(URI_IS_LIVE, Boolean.class);
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean turnOnLiveUrl() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(SECURITY_HEADER_NAME, SECURITY_HEADER_VALUE);
            HttpEntity<Boolean> entity = new HttpEntity<>(true, headers);
            return template.postForObject(URI_IS_LIVE, entity, boolean.class);
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean turnOffLiveUrl() {
        /*try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(SECURITY_HEADER_NAME, SECURITY_HEADER_VALUE);

            HttpEntity<Boolean> entity = new HttpEntity<>(false, headers);
            return template.postForObject(URI_IS_LIVE, entity, boolean.class);
        } catch (Exception ex) {
            return false;
        }*/
        return true;
    }

    public boolean verifyEmployeeNID(Employee employee) {
        // Validate information
        // create porichoyRequestBodyDTO from employee data
        // verify from verification server
        // if verified => update Employee flag , << Do Not execute Save from this service >>
        // return true false based on verification
        try {
            if (
                employee.getDateOfBirth() != null && employee.getNationalIdNo() != null && verifyNIDNumberSyntax(employee.getNationalIdNo())
            ) {
                LocalDate dob = employee.getDateOfBirth();
                String nidNumber = employee.getNationalIdNo();

                PorichoyRequestBodyDTO porichoyRequestBodyDTO = new PorichoyRequestBodyDTO();
                porichoyRequestBodyDTO.setDateOfBirth(dob.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
                porichoyRequestBodyDTO.setEnglishTranslation(true);
                porichoyRequestBodyDTO.setNidNumber(nidNumber);
                Optional<IdentificationDTO> identificationDtoOptional = this.verifyNID(porichoyRequestBodyDTO);

                if (!identificationDtoOptional.isPresent()) {
                    // not verified
                    // change employee flag
                    employee.setIsNidVerified(false);
                    return false;
                } else {
                    // validation passed
                    employee.setIsNidVerified(true);
                    return true;
                }
            } else {
                // validation failed
                // change employee flag and save
                employee.setIsNidVerified(false);
                return false;
            }
        } catch (Exception ex) {
            // Exception occurred
            // not validated
            employee.setIsNidVerified(false);
            return false;
        }
    }

    /*
     * Applicable only for PF Nominee
     * */
    public boolean isPFNomineeNIDVerified(PfNominee pfNominee) {
        try {
            if (
                pfNominee.getDateOfBirth() != null &&
                pfNominee.getIdentityType() == IdentityType.NID &&
                pfNominee.getIdNumber() != null &&
                verifyNIDNumberSyntax(pfNominee.getIdNumber())
            ) {
                PorichoyRequestBodyDTO porichoyRequestBodyDTO = new PorichoyRequestBodyDTO();
                porichoyRequestBodyDTO.setNidNumber(pfNominee.getIdNumber().trim());
                porichoyRequestBodyDTO.setEnglishTranslation(true);
                porichoyRequestBodyDTO.setDateOfBirth(pfNominee.getDateOfBirth().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
                Optional<IdentificationDTO> identificationDTOOptional = verifyNID(porichoyRequestBodyDTO);
                if (identificationDTOOptional.isPresent()) {
                    pfNominee.setIsNidVerified(true);
                    return true;
                } else {
                    pfNominee.setIsNidVerified(false);
                    return false;
                }
            } else {
                pfNominee.setIsNidVerified(false);
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    /*
     * Applicable only for PF Nominee
     * */
    public boolean isPFNomineeGuardianNIDVerified(PfNominee pfNominee) {
        try {
            if (
                pfNominee.getGuardianDateOfBirth() != null &&
                pfNominee.getGuardianIdentityType() == IdentityType.NID &&
                pfNominee.getGuardianIdNumber() != null &&
                verifyNIDNumberSyntax(pfNominee.getIdNumber())
            ) {
                PorichoyRequestBodyDTO porichoyRequestBodyDTO = new PorichoyRequestBodyDTO();
                porichoyRequestBodyDTO.setNidNumber(pfNominee.getGuardianIdNumber().trim());
                porichoyRequestBodyDTO.setEnglishTranslation(true);
                porichoyRequestBodyDTO.setDateOfBirth(pfNominee.getGuardianDateOfBirth().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
                Optional<IdentificationDTO> identificationDTOOptional = verifyNID(porichoyRequestBodyDTO);
                if (identificationDTOOptional.isPresent()) {
                    pfNominee.setIsNidVerified(true);
                    return true;
                } else {
                    pfNominee.setIsNidVerified(false);
                    return false;
                }
            } else {
                pfNominee.setIsNidVerified(false);
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    /*
     * Applicable only for General and GF Nominee
     * */
    public boolean isNomineeNidVerified(Nominee nominee) {
        try {
            if (
                nominee.getDateOfBirth() != null &&
                nominee.getIdentityType() == IdentityType.NID &&
                nominee.getIdNumber() != null &&
                verifyNIDNumberSyntax(nominee.getIdNumber())
            ) {
                PorichoyRequestBodyDTO porichoyRequestBodyDTO = new PorichoyRequestBodyDTO();
                porichoyRequestBodyDTO.setNidNumber(nominee.getIdNumber().trim());
                porichoyRequestBodyDTO.setEnglishTranslation(true);
                porichoyRequestBodyDTO.setDateOfBirth(nominee.getDateOfBirth().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
                Optional<IdentificationDTO> identificationDTOOptional = verifyNID(porichoyRequestBodyDTO);
                if (identificationDTOOptional.isPresent()) {
                    nominee.setIsNidVerified(true);
                    return true;
                } else {
                    nominee.setIsNidVerified(false);
                    return false;
                }
            } else {
                nominee.setIsNidVerified(false);
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    /*
     * Applicable only for General and GF Nominee
     * */
    public boolean isNomineeGuardianNidVerified(Nominee nominee) {
        try {
            if (
                nominee.getGuardianDateOfBirth() != null &&
                nominee.getGuardianIdentityType() == IdentityType.NID &&
                nominee.getGuardianIdNumber() != null &&
                verifyNIDNumberSyntax(nominee.getIdNumber())
            ) {
                PorichoyRequestBodyDTO porichoyRequestBodyDTO = new PorichoyRequestBodyDTO();
                porichoyRequestBodyDTO.setNidNumber(nominee.getGuardianIdNumber().trim());
                porichoyRequestBodyDTO.setEnglishTranslation(true);
                porichoyRequestBodyDTO.setDateOfBirth(nominee.getGuardianDateOfBirth().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
                Optional<IdentificationDTO> identificationDTOOptional = verifyNID(porichoyRequestBodyDTO);
                if (identificationDTOOptional.isPresent()) {
                    nominee.setIsNidVerified(true);
                    return true;
                } else {
                    nominee.setIsNidVerified(false);
                    return false;
                }
            } else {
                nominee.setIsNidVerified(false);
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean verifyNIDNumberSyntax(String nidNumber) {
        nidNumber = nidNumber.trim().toLowerCase(Locale.ROOT);
        if ((nidNumber.length() == 10 || nidNumber.length() == 13 || nidNumber.length() == 17) && isNumeric(nidNumber)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isNumeric(String string) {
        double doubleValue;
        if (string == null || string.equals("")) {
            return false;
        }
        try {
            doubleValue = Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
