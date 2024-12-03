package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.PfNominee;
import com.bits.hr.domain.enumeration.OrganizationFileType;
import com.bits.hr.repository.InsuranceRegistrationRepository;
import com.bits.hr.repository.NomineeRepository;
import com.bits.hr.repository.OfferRepository;
import com.bits.hr.repository.PfNomineeRepository;
import com.bits.hr.service.OrganizationService;
import com.bits.hr.service.dto.FileDetailsDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import java.io.IOException;
import java.util.Optional;
import lombok.Data;
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
public class OrganizationSharedFileViewResource {

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping(value = "/organizations/logo", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<byte[]> getLogoFile() throws IOException {
        String path = organizationService.getOrganizationFilePath(OrganizationFileType.LOGO);
        if (path != null && path.length() > 0) {
            try {
                String extension = StringUtils.getFilenameExtension(path);
                MediaType contentType = MediaType.IMAGE_JPEG;
                if (extension.equals("png")) {
                    contentType = MediaType.IMAGE_PNG;
                } else if (extension.equals("svg")) {
                    contentType = MediaType.valueOf("image/svg+xml");
                }
                byte[] result = fileOperationService.loadAsByte(path);
                return ResponseEntity.ok().contentType(contentType).body(result);
            } catch (Exception ex) {
                return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf("image/svg+xml"))
                    .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/logo-placeholder.svg")));
            }
        } else {
            return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf("image/svg+xml"))
                .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/logo-placeholder.svg")));
        }
    }

    @GetMapping(value = "/organizations/finance-manager-signature", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<byte[]> getFinanceManagerSignatureFile() throws IOException {
        String path = organizationService.getOrganizationFilePath(OrganizationFileType.FINANCE_MANAGER_SIGNATURE);
        if (path != null && path.length() > 0) {
            try {
                String extension = StringUtils.getFilenameExtension(path);
                MediaType contentType = MediaType.IMAGE_JPEG;
                if (extension.equals("png")) {
                    contentType = MediaType.IMAGE_PNG;
                } else if (extension.equals("svg")) {
                    contentType = MediaType.valueOf("image/svg+xml");
                }
                byte[] result = fileOperationService.loadAsByte(path);
                return ResponseEntity.ok().contentType(contentType).body(result);
            } catch (Exception ex) {
                return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/finance-manager-signature.png")));
            }
        } else {
            return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/finance-manager-signature.png")));
        }
    }

    @GetMapping(value = "/organizations/document-letter-head", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<byte[]> getDocumentLetterHeadFile() throws IOException {
        String path = organizationService.getOrganizationFilePath(OrganizationFileType.DOCUMENT_LETTER_HEAD);
        if (path != null && path.length() > 0) {
            try {
                String extension = StringUtils.getFilenameExtension(path);
                MediaType contentType = MediaType.IMAGE_JPEG;
                if (extension.equals("png")) {
                    contentType = MediaType.IMAGE_PNG;
                } else if (extension.equals("svg")) {
                    contentType = MediaType.valueOf("image/svg+xml");
                }
                byte[] result = fileOperationService.loadAsByte(path);
                return ResponseEntity.ok().contentType(contentType).body(result);
            } catch (Exception ex) {
                return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/letter-head-pad-new.png")));
            }
        } else {
            return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/letter-head-pad-new.png")));
        }
    }

    @GetMapping(value = "/organizations/pf-statement-letter-head", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<byte[]> getPfStatementLetterHeadFile() throws IOException {
        String path = organizationService.getOrganizationFilePath(OrganizationFileType.PF_STATEMENT_LETTER_HEAD);
        if (path != null && path.length() > 0) {
            try {
                String extension = StringUtils.getFilenameExtension(path);
                MediaType contentType = MediaType.IMAGE_JPEG;
                if (extension.equals("png")) {
                    contentType = MediaType.IMAGE_PNG;
                } else if (extension.equals("svg")) {
                    contentType = MediaType.valueOf("image/svg+xml");
                }
                byte[] result = fileOperationService.loadAsByte(path);
                return ResponseEntity.ok().contentType(contentType).body(result);
            } catch (Exception ex) {
                return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/letter-head-pad-new.png")));
            }
        } else {
            return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/letter-head-pad-new.png")));
        }
    }

    @GetMapping(value = "/organizations/tax-statement-letter-head", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<byte[]> getTaxStatementLetterHeadFile() throws IOException {
        String path = organizationService.getOrganizationFilePath(OrganizationFileType.TAX_STATEMENT_LETTER_HEAD);
        if (path != null && path.length() > 0) {
            try {
                String extension = StringUtils.getFilenameExtension(path);
                MediaType contentType = MediaType.IMAGE_JPEG;
                if (extension.equals("png")) {
                    contentType = MediaType.IMAGE_PNG;
                } else if (extension.equals("svg")) {
                    contentType = MediaType.valueOf("image/svg+xml");
                }
                byte[] result = fileOperationService.loadAsByte(path);
                return ResponseEntity.ok().contentType(contentType).body(result);
            } catch (Exception ex) {
                return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/letter-head-pad-new.png")));
            }
        } else {
            return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/letter-head-pad-new.png")));
        }
    }

    @GetMapping(value = "/organizations/nominee-letter-head", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<byte[]> getNomineeLetterHeadFile() throws IOException {
        String path = organizationService.getOrganizationFilePath(OrganizationFileType.NOMINEE_LETTER_HEAD);
        if (path != null && path.length() > 0) {
            try {
                String extension = StringUtils.getFilenameExtension(path);
                MediaType contentType = MediaType.IMAGE_JPEG;
                if (extension.equals("png")) {
                    contentType = MediaType.IMAGE_PNG;
                } else if (extension.equals("svg")) {
                    contentType = MediaType.valueOf("image/svg+xml");
                }
                byte[] result = fileOperationService.loadAsByte(path);
                return ResponseEntity.ok().contentType(contentType).body(result);
            } catch (Exception ex) {
                return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/letter-head-pad-new.png")));
            }
        } else {
            return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/letter-head-pad-new.png")));
        }
    }

    @GetMapping(value = "/organizations/salary-payslip-letter-head", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<byte[]> getSalaryPayslipLetterHeadFile() throws IOException {
        String path = organizationService.getOrganizationFilePath(OrganizationFileType.SALARY_PAYSLIP_LETTER_HEAD);
        if (path != null && path.length() > 0) {
            try {
                String extension = StringUtils.getFilenameExtension(path);
                MediaType contentType = MediaType.IMAGE_JPEG;
                if (extension.equals("png")) {
                    contentType = MediaType.IMAGE_PNG;
                } else if (extension.equals("svg")) {
                    contentType = MediaType.valueOf("image/svg+xml");
                }
                byte[] result = fileOperationService.loadAsByte(path);
                return ResponseEntity.ok().contentType(contentType).body(result);
            } catch (Exception ex) {
                return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf("image/svg+xml"))
                    .body(
                        IOUtils.toByteArray(
                            getClass().getResourceAsStream("/static/images/organization/letter-head-pad-with-organization-address.svg")
                        )
                    );
            }
        } else {
            return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf("image/svg+xml"))
                .body(
                    IOUtils.toByteArray(
                        getClass().getResourceAsStream("/static/images/organization/letter-head-pad-with-organization-address.svg")
                    )
                );
        }
    }

    @GetMapping(value = "/organizations/festival-bonus-payslip-letter-head", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<byte[]> getFestivalBonusPayslipLetterHeadFile() throws IOException {
        String path = organizationService.getOrganizationFilePath(OrganizationFileType.FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD);
        if (path != null && path.length() > 0) {
            try {
                String extension = StringUtils.getFilenameExtension(path);
                MediaType contentType = MediaType.IMAGE_JPEG;
                if (extension.equals("png")) {
                    contentType = MediaType.IMAGE_PNG;
                } else if (extension.equals("svg")) {
                    contentType = MediaType.valueOf("image/svg+xml");
                }
                byte[] result = fileOperationService.loadAsByte(path);
                return ResponseEntity.ok().contentType(contentType).body(result);
            } catch (Exception ex) {
                return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/letter-head-pad-new.png")));
            }
        } else {
            return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/letter-head-pad-new.png")));
        }
    }

    @GetMapping(value = "/organizations/recruitment-requisition-letter-head", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<byte[]> getRecruitmentRequisitionLetterHeadFile() throws IOException {
        String path = organizationService.getOrganizationFilePath(OrganizationFileType.RECRUITMENT_REQUISITION_LETTER_HEAD);
        if (path != null && path.length() > 0) {
            try {
                String extension = StringUtils.getFilenameExtension(path);
                MediaType contentType = MediaType.IMAGE_JPEG;
                if (extension.equals("png")) {
                    contentType = MediaType.IMAGE_PNG;
                } else if (extension.equals("svg")) {
                    contentType = MediaType.valueOf("image/svg+xml");
                }
                byte[] result = fileOperationService.loadAsByte(path);
                return ResponseEntity.ok().contentType(contentType).body(result);
            } catch (Exception ex) {
                return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf("image/svg+xml"))
                    .body(
                        IOUtils.toByteArray(
                            getClass().getResourceAsStream("/static/images/organization/letter-head-pad-with-organization-address.svg")
                        )
                    );
            }
        } else {
            return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf("image/svg+xml"))
                .body(
                    IOUtils.toByteArray(
                        getClass().getResourceAsStream("/static/images/organization/letter-head-pad-with-organization-address.svg")
                    )
                );
        }
    }

    @GetMapping(value = "/organizations/stamp", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<byte[]> getOrganizationStampFile() throws IOException {
        String path = organizationService.getOrganizationFilePath(OrganizationFileType.ORGANIZATION_STAMP);
        if (path != null && path.length() > 0) {
            try {
                String extension = StringUtils.getFilenameExtension(path);
                MediaType contentType = MediaType.IMAGE_JPEG;
                if (extension.equals("png")) {
                    contentType = MediaType.IMAGE_PNG;
                } else if (extension.equals("svg")) {
                    contentType = MediaType.valueOf("image/svg+xml");
                }
                byte[] result = fileOperationService.loadAsByte(path);
                return ResponseEntity.ok().contentType(contentType).body(result);
            } catch (Exception ex) {
                return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf("image/svg+xml"))
                    .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/organization-stamp.svg")));
            }
        } else {
            return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf("image/svg+xml"))
                .body(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/organization/organization-stamp.svg")));
        }
    }

    @GetMapping(value = "/organizations/stamp-preview")
    public ResponseEntity<FileDetailsDTO> getOrganizationStampFile3() throws IOException {
        FileDetailsDTO fileDetailsDTO = organizationService.getOrganizationFile(
            OrganizationFileType.ORGANIZATION_STAMP,
            "/static/images/organization/organization-stamp.svg"
        );
        return ResponseEntity.ok().body(fileDetailsDTO);
    }
}
