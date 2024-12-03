package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Organization;
import com.bits.hr.domain.enumeration.OrganizationFileType;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.OrganizationRepository;
import com.bits.hr.service.OrganizationService;
import com.bits.hr.service.dto.FileDetailsDTO;
import com.bits.hr.service.dto.OrganizationDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.fileService.OrganizationFileService;
import com.bits.hr.service.mapper.OrganizationMapper;
import com.bits.hr.util.ImageBase64Utils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link Organization}.
 */
@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper organizationMapper;

    private final OrganizationFileService organizationFileService;

    private final FileOperationService fileOperationService;

    @Autowired
    private EmployeeRepository employeeRepository;

    public OrganizationServiceImpl(
        OrganizationRepository organizationRepository,
        OrganizationMapper organizationMapper,
        OrganizationFileService organizationFileService,
        FileOperationService fileOperationService
    ) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
        this.organizationFileService = organizationFileService;
        this.fileOperationService = fileOperationService;
    }

    @Override
    public OrganizationDTO save(OrganizationDTO organizationDTO) {
        log.debug("Request to save Organization : {}", organizationDTO);
        Organization organization = organizationMapper.toEntity(organizationDTO);
        organization = organizationRepository.save(organization);
        return organizationMapper.toDto(organization);
    }

    @Override
    public OrganizationDTO createAlongMultipart(OrganizationDTO organizationDTO, List<MultipartFile> fileList) {
        log.debug("Request to save Organization : {}", organizationDTO);
        List<OrganizationFileType> organizationFileTypes = organizationDTO.getOrganizationFileTypeList();
        for (int i = 0; i < organizationFileTypes.size(); i++) {
            OrganizationFileType organizationFileType = organizationFileTypes.get(i);
            MultipartFile multipartFile = fileList.get(i);
            File file = organizationFileService.save(multipartFile);

            if (organizationFileType == OrganizationFileType.LOGO) {
                organizationDTO.setLogo(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.DOCUMENT_LETTER_HEAD) {
                organizationDTO.setDocumentLetterHead(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.NOMINEE_LETTER_HEAD) {
                organizationDTO.setNomineeLetterHead(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.TAX_STATEMENT_LETTER_HEAD) {
                organizationDTO.setTaxStatementLetterHead(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.PF_STATEMENT_LETTER_HEAD) {
                organizationDTO.setPfStatementLetterHead(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.SALARY_PAYSLIP_LETTER_HEAD) {
                organizationDTO.setSalaryPayslipLetterHead(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD) {
                organizationDTO.setFestivalBonusPayslipLetterHead(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.RECRUITMENT_REQUISITION_LETTER_HEAD) {
                organizationDTO.setRecruitmentRequisitionLetterHead(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.ORGANIZATION_STAMP) {
                organizationDTO.setOrganizationStamp(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.FINANCE_MANAGER_SIGNATURE) {
                organizationDTO.setFinanceManagerSignature(file.getAbsolutePath());
            }
        }
        Organization organization = organizationMapper.toEntity(organizationDTO);
        organization = organizationRepository.save(organization);
        return organizationMapper.toDto(organization);
    }

    @Override
    public OrganizationDTO updateAlongMultipart(OrganizationDTO organizationDTO, List<MultipartFile> fileList) {
        log.debug("Request to save Organization : {}", organizationDTO);
        OrganizationDTO savedOrganization = findOne(organizationDTO.getId()).get();
        List<OrganizationFileType> organizationFileTypes = organizationDTO.getOrganizationFileTypeList();
        for (int i = 0; i < organizationFileTypes.size(); i++) {
            OrganizationFileType organizationFileType = organizationFileTypes.get(i);
            MultipartFile multipartFile = fileList.get(i);
            File file = organizationFileService.save(multipartFile);

            if (organizationFileType == OrganizationFileType.LOGO) {
                if (savedOrganization.getLogo() != null) {
                    fileOperationService.delete(savedOrganization.getLogo());
                }
                organizationDTO.setLogo(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.DOCUMENT_LETTER_HEAD) {
                if (savedOrganization.getDocumentLetterHead() != null) {
                    fileOperationService.delete(savedOrganization.getDocumentLetterHead());
                }
                organizationDTO.setDocumentLetterHead(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.PF_STATEMENT_LETTER_HEAD) {
                if (savedOrganization.getPfStatementLetterHead() != null) {
                    fileOperationService.delete(savedOrganization.getPfStatementLetterHead());
                }
                organizationDTO.setPfStatementLetterHead(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.TAX_STATEMENT_LETTER_HEAD) {
                if (savedOrganization.getTaxStatementLetterHead() != null) {
                    fileOperationService.delete(savedOrganization.getTaxStatementLetterHead());
                }
                organizationDTO.setTaxStatementLetterHead(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.NOMINEE_LETTER_HEAD) {
                if (savedOrganization.getNomineeLetterHead() != null) {
                    fileOperationService.delete(savedOrganization.getNomineeLetterHead());
                }
                organizationDTO.setNomineeLetterHead(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.SALARY_PAYSLIP_LETTER_HEAD) {
                if (savedOrganization.getSalaryPayslipLetterHead() != null) {
                    fileOperationService.delete(savedOrganization.getSalaryPayslipLetterHead());
                }
                organizationDTO.setSalaryPayslipLetterHead(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD) {
                if (savedOrganization.getFestivalBonusPayslipLetterHead() != null) {
                    fileOperationService.delete(savedOrganization.getFestivalBonusPayslipLetterHead());
                }
                organizationDTO.setFestivalBonusPayslipLetterHead(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.RECRUITMENT_REQUISITION_LETTER_HEAD) {
                if (savedOrganization.getRecruitmentRequisitionLetterHead() != null) {
                    fileOperationService.delete(savedOrganization.getRecruitmentRequisitionLetterHead());
                }
                organizationDTO.setRecruitmentRequisitionLetterHead(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.ORGANIZATION_STAMP) {
                if (savedOrganization.getOrganizationStamp() != null) {
                    fileOperationService.delete(savedOrganization.getOrganizationStamp());
                }
                organizationDTO.setOrganizationStamp(file.getAbsolutePath());
            } else if (organizationFileType == OrganizationFileType.FINANCE_MANAGER_SIGNATURE) {
                if (savedOrganization.getFinanceManagerSignature() != null) {
                    fileOperationService.delete(savedOrganization.getFinanceManagerSignature());
                }
                organizationDTO.setFinanceManagerSignature(file.getAbsolutePath());
            }
        }
        Organization organization = organizationMapper.toEntity(organizationDTO);
        organization = organizationRepository.save(organization);
        return organizationMapper.toDto(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Organizations");
        return organizationRepository.findAll(pageable).map(organizationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationDTO> findOne(Long id) {
        log.debug("Request to get Organization : {}", id);
        return organizationRepository.findById(id).map(organizationMapper::toDto);
    }

    public OrganizationDTO defaultConfiguration() {
        OrganizationDTO organizationDTO = new OrganizationDTO();
        organizationDTO.setFullName("BRAC IT Services Limited");
        organizationDTO.setShortName("BRAC IT");
        organizationDTO.setSlogan("");
        organizationDTO.setDomainName("bracits.com");
        organizationDTO.setEmailAddress("info@bracits.com");
        return organizationDTO;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Organization : {}", id);
        organizationRepository.deleteById(id);
    }

    @Override
    public String getOrganizationFilePath(OrganizationFileType organizationFileType) {
        Page<Organization> organizations = organizationRepository.findAll(PageRequest.of(0, 1, Sort.by("id").ascending()));
        if (organizations.getContent().size() > 0) {
            if (organizationFileType == OrganizationFileType.LOGO) {
                return organizations.getContent().get(0).getLogo();
            } else if (organizationFileType == OrganizationFileType.DOCUMENT_LETTER_HEAD) {
                return organizations.getContent().get(0).getDocumentLetterHead();
            } else if (organizationFileType == OrganizationFileType.PF_STATEMENT_LETTER_HEAD) {
                return organizations.getContent().get(0).getPfStatementLetterHead();
            } else if (organizationFileType == OrganizationFileType.TAX_STATEMENT_LETTER_HEAD) {
                return organizations.getContent().get(0).getTaxStatementLetterHead();
            } else if (organizationFileType == OrganizationFileType.NOMINEE_LETTER_HEAD) {
                return organizations.getContent().get(0).getNomineeLetterHead();
            } else if (organizationFileType == OrganizationFileType.SALARY_PAYSLIP_LETTER_HEAD) {
                return organizations.getContent().get(0).getSalaryPayslipLetterHead();
            } else if (organizationFileType == OrganizationFileType.FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD) {
                return organizations.getContent().get(0).getFestivalBonusPayslipLetterHead();
            } else if (organizationFileType == OrganizationFileType.RECRUITMENT_REQUISITION_LETTER_HEAD) {
                return organizations.getContent().get(0).getRecruitmentRequisitionLetterHead();
            } else if (organizationFileType == OrganizationFileType.FINANCE_MANAGER_SIGNATURE) {
                return organizations.getContent().get(0).getFinanceManagerSignature();
            } else if (organizationFileType == OrganizationFileType.ORGANIZATION_STAMP) {
                return organizations.getContent().get(0).getOrganizationStamp();
            } else {
                throw new BadRequestAlertException("File Type Unknown", "Organization", "");
            }
        } else {
            return "";
        }
    }

    @Override
    public FileDetailsDTO getOrganizationFile(OrganizationFileType organizationFileType, String placeHolderImagePath) throws IOException {
        String path = this.getOrganizationFilePath(organizationFileType);
        FileDetailsDTO fileDetailsDTO = new FileDetailsDTO();
        if (path != null && path.length() > 0) {
            try {
                byte[] bytes = fileOperationService.loadAsByte(path);
                String base64 = ImageBase64Utils.getImageBase64Prefix(path);
                fileDetailsDTO.setBase64DataType(base64);
                fileDetailsDTO.setByteStream(bytes);
            } catch (Exception ex) {
                // placeholder image
                byte[] bytes = IOUtils.toByteArray(getClass().getResourceAsStream(placeHolderImagePath));
                String base64 = ImageBase64Utils.getImageBase64Prefix(placeHolderImagePath);
                fileDetailsDTO.setBase64DataType(base64);
                fileDetailsDTO.setByteStream(bytes);
            }
        } else {
            // placeholder image
            byte[] bytes = IOUtils.toByteArray(getClass().getResourceAsStream(placeHolderImagePath));
            String base64 = ImageBase64Utils.getImageBase64Prefix(placeHolderImagePath);
            fileDetailsDTO.setBase64DataType(base64);
            fileDetailsDTO.setByteStream(bytes);
        }
        return fileDetailsDTO;
    }

    @Override
    public OrganizationDTO getOrganizationBasic() {
        Page<Organization> organizations = organizationRepository.findAll(PageRequest.of(0, 1, Sort.by("id").ascending()));

        OrganizationDTO organizationDTO = new OrganizationDTO();
        if (organizations.getContent().size() > 0) {
            Organization savedOrganization = organizations.getContent().get(0);
            organizationDTO.setShortName(savedOrganization.getShortName());
            organizationDTO.setFullName(savedOrganization.getFullName());
            organizationDTO.setDomainName(organizationDTO.getDomainName());
            organizationDTO.setEmailAddress(savedOrganization.getEmailAddress());
            organizationDTO.setContactNumber(savedOrganization.getContactNumber());
            organizationDTO.setLinkedin(savedOrganization.getLinkedin());
            organizationDTO.setFacebook(savedOrganization.getFacebook());
            organizationDTO.setYoutube(savedOrganization.getYoutube());
            organizationDTO.setInstagram(savedOrganization.getInstagram());

            organizationDTO = organizationMapper.toDto(savedOrganization);
            Optional<Employee> financeManagerOptional = employeeRepository.findByPin(savedOrganization.getFinanceManagerPIN());
            if(financeManagerOptional.isPresent()){
                Employee financeManager = financeManagerOptional.get();
                organizationDTO.setFinanceManagerName(financeManager.getFullName());
                organizationDTO.setFinanceManagerDesignation(financeManager.getDesignation().getDesignationName());
                organizationDTO.setFinanceManagerUnit(financeManager.getUnit().getUnitName());
            }
            else {
                organizationDTO.setFinanceManagerName("Palash Kumar Paul");
                organizationDTO.setFinanceManagerDesignation("Sr. Manager");
                organizationDTO.setFinanceManagerUnit("Finance & Accounts");
            }
        } else {
            organizationDTO.setShortName("BRAC IT");
            organizationDTO.setFullName("BRAC IT Services Limited");
            organizationDTO.setDomainName("https://bracits.com/");
            organizationDTO.setEmailAddress("info@bracits.com");
            organizationDTO.setContactNumber("+88028836332");
            organizationDTO.setLinkedin("https://www.linkedin.com/company/brac-it");
            organizationDTO.setFacebook("https://www.facebook.com/bracits");
            organizationDTO.setYoutube("https://www.youtube.com/channel/UCOcBJBGPyqV3-fGHWcEdYBg");
            organizationDTO.setInstagram("https://www.instagram.com/bracit_/");
            organizationDTO.setFinanceManagerName("Palash Kumar Paul");
            organizationDTO.setFinanceManagerDesignation("Sr. Manager");
            organizationDTO.setFinanceManagerUnit("Finance & Accounts");

        }
        return organizationDTO;
    }

    @Override
    public OrganizationDTO getOrganizationDetails() {
        Page<Organization> organizations = organizationRepository.findAll(PageRequest.of(0, 1, Sort.by("id").ascending()));

        OrganizationDTO organizationDTO = new OrganizationDTO();

        if (!organizations.getContent().isEmpty()) {
            Organization savedOrganization = organizations.getContent().get(0);
            organizationDTO = organizationMapper.toDto(savedOrganization);
            if (savedOrganization.getFinanceManagerPIN() != null) {
                Optional<Employee> financeManagerOptional = employeeRepository.findByPin(savedOrganization.getFinanceManagerPIN());
                if (financeManagerOptional.isPresent()) {
                    Employee financeManager = financeManagerOptional.get();
                    organizationDTO.setFinanceManagerName(financeManager.getFullName());
                    organizationDTO.setFinanceManagerDesignation(financeManager.getDesignation().getDesignationName());
                }
            }
        } else {
            organizationDTO.setShortName("BRAC IT");
            organizationDTO.setFullName("BRAC IT Services Limited");
            organizationDTO.setDomainName("https://bracits.com/");
            organizationDTO.setEmailAddress("info@bracits.com");
            organizationDTO.setContactNumber("+88028836332");
            organizationDTO.setLinkedin("https://www.linkedin.com/company/brac-it");
            organizationDTO.setFacebook("https://www.facebook.com/bracits");
            organizationDTO.setYoutube("https://www.youtube.com/channel/UCOcBJBGPyqV3-fGHWcEdYBg");
            organizationDTO.setInstagram("https://www.instagram.com/bracit_/");
            organizationDTO.setFinanceManagerName("Palash Kumar Paul");
            organizationDTO.setFinanceManagerDesignation("Sr. Manager, Finance & Accounts");
            organizationDTO.setFinanceManagerSignature("files/organizations/finance-manager-signature");
        }

        return organizationDTO;
    }
}
