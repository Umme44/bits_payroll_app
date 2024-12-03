package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Organization;
import com.bits.hr.repository.OrganizationRepository;
import com.bits.hr.service.dto.OrganizationDTO;
import com.bits.hr.service.mapper.OrganizationMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link OrganizationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganizationResourceIT {

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLOGAN = "AAAAAAAAAA";
    private static final String UPDATED_SLOGAN = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAIN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_HR_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_HR_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_NO_REPLY_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_NO_REPLY_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FINANCE_MANAGER_PIN = "AAAAAAAAAA";
    private static final String UPDATED_FINANCE_MANAGER_PIN = "BBBBBBBBBB";

    private static final String DEFAULT_FINANCE_MANAGER_SIGNATURE = "AAAAAAAAAA";
    private static final String UPDATED_FINANCE_MANAGER_SIGNATURE = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_LETTER_HEAD = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_LETTER_HEAD = "BBBBBBBBBB";

    private static final String DEFAULT_PF_STATEMENT_LETTER_HEAD = "AAAAAAAAAA";
    private static final String UPDATED_PF_STATEMENT_LETTER_HEAD = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_STATEMENT_LETTER_HEAD = "AAAAAAAAAA";
    private static final String UPDATED_TAX_STATEMENT_LETTER_HEAD = "BBBBBBBBBB";

    private static final String DEFAULT_NOMINEE_LETTER_HEAD = "AAAAAAAAAA";
    private static final String UPDATED_NOMINEE_LETTER_HEAD = "BBBBBBBBBB";

    private static final String DEFAULT_SALARY_PAYSLIP_LETTER_HEAD = "AAAAAAAAAA";
    private static final String UPDATED_SALARY_PAYSLIP_LETTER_HEAD = "BBBBBBBBBB";

    private static final String DEFAULT_FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD = "AAAAAAAAAA";
    private static final String UPDATED_FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD = "BBBBBBBBBB";

    private static final String DEFAULT_RECRUITMENT_REQUISITION_LETTER_HEAD = "AAAAAAAAAA";
    private static final String UPDATED_RECRUITMENT_REQUISITION_LETTER_HEAD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HAS_ORGANIZATION_STAMP = false;
    private static final Boolean UPDATED_HAS_ORGANIZATION_STAMP = true;

    private static final String DEFAULT_ORGANIZATION_STAMP = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZATION_STAMP = "BBBBBBBBBB";

    private static final String DEFAULT_LINKEDIN = "AAAAAAAAAA";
    private static final String UPDATED_LINKEDIN = "BBBBBBBBBB";

    private static final String DEFAULT_TWITTER = "AAAAAAAAAA";
    private static final String UPDATED_TWITTER = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOK = "AAAAAAAAAA";
    private static final String UPDATED_FACEBOOK = "BBBBBBBBBB";

    private static final String DEFAULT_YOUTUBE = "AAAAAAAAAA";
    private static final String UPDATED_YOUTUBE = "BBBBBBBBBB";

    private static final String DEFAULT_INSTAGRAM = "AAAAAAAAAA";
    private static final String UPDATED_INSTAGRAM = "BBBBBBBBBB";

    private static final String DEFAULT_WHATSAPP = "AAAAAAAAAA";
    private static final String UPDATED_WHATSAPP = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/organizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationMockMvc;

    private Organization organization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createEntity(EntityManager em) {
        Organization organization = new Organization()
            .shortName(DEFAULT_SHORT_NAME)
            .fullName(DEFAULT_FULL_NAME)
            .slogan(DEFAULT_SLOGAN)
            .domainName(DEFAULT_DOMAIN_NAME)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .hrEmailAddress(DEFAULT_HR_EMAIL_ADDRESS)
            .noReplyEmailAddress(DEFAULT_NO_REPLY_EMAIL_ADDRESS)
            .contactNumber(DEFAULT_CONTACT_NUMBER)
            .financeManagerPIN(DEFAULT_FINANCE_MANAGER_PIN)
            .financeManagerSignature(DEFAULT_FINANCE_MANAGER_SIGNATURE)
            .logo(DEFAULT_LOGO)
            .documentLetterHead(DEFAULT_DOCUMENT_LETTER_HEAD)
            .pfStatementLetterHead(DEFAULT_PF_STATEMENT_LETTER_HEAD)
            .taxStatementLetterHead(DEFAULT_TAX_STATEMENT_LETTER_HEAD)
            .nomineeLetterHead(DEFAULT_NOMINEE_LETTER_HEAD)
            .salaryPayslipLetterHead(DEFAULT_SALARY_PAYSLIP_LETTER_HEAD)
            .festivalBonusPayslipLetterHead(DEFAULT_FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD)
            .recruitmentRequisitionLetterHead(DEFAULT_RECRUITMENT_REQUISITION_LETTER_HEAD)
            .hasOrganizationStamp(DEFAULT_HAS_ORGANIZATION_STAMP)
            .organizationStamp(DEFAULT_ORGANIZATION_STAMP)
            .linkedin(DEFAULT_LINKEDIN)
            .twitter(DEFAULT_TWITTER)
            .facebook(DEFAULT_FACEBOOK)
            .youtube(DEFAULT_YOUTUBE)
            .instagram(DEFAULT_INSTAGRAM)
            .whatsapp(DEFAULT_WHATSAPP);
        return organization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createUpdatedEntity(EntityManager em) {
        Organization organization = new Organization()
            .shortName(UPDATED_SHORT_NAME)
            .fullName(UPDATED_FULL_NAME)
            .slogan(UPDATED_SLOGAN)
            .domainName(UPDATED_DOMAIN_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .hrEmailAddress(UPDATED_HR_EMAIL_ADDRESS)
            .noReplyEmailAddress(UPDATED_NO_REPLY_EMAIL_ADDRESS)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .financeManagerPIN(UPDATED_FINANCE_MANAGER_PIN)
            .financeManagerSignature(UPDATED_FINANCE_MANAGER_SIGNATURE)
            .logo(UPDATED_LOGO)
            .documentLetterHead(UPDATED_DOCUMENT_LETTER_HEAD)
            .pfStatementLetterHead(UPDATED_PF_STATEMENT_LETTER_HEAD)
            .taxStatementLetterHead(UPDATED_TAX_STATEMENT_LETTER_HEAD)
            .nomineeLetterHead(UPDATED_NOMINEE_LETTER_HEAD)
            .salaryPayslipLetterHead(UPDATED_SALARY_PAYSLIP_LETTER_HEAD)
            .festivalBonusPayslipLetterHead(UPDATED_FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD)
            .recruitmentRequisitionLetterHead(UPDATED_RECRUITMENT_REQUISITION_LETTER_HEAD)
            .hasOrganizationStamp(UPDATED_HAS_ORGANIZATION_STAMP)
            .organizationStamp(UPDATED_ORGANIZATION_STAMP)
            .linkedin(UPDATED_LINKEDIN)
            .twitter(UPDATED_TWITTER)
            .facebook(UPDATED_FACEBOOK)
            .youtube(UPDATED_YOUTUBE)
            .instagram(UPDATED_INSTAGRAM)
            .whatsapp(UPDATED_WHATSAPP);
        return organization;
    }

    @BeforeEach
    public void initTest() {
        organization = createEntity(em);
    }

    @Test
    @Transactional
    void createOrganization() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();
        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);
        restOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate + 1);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testOrganization.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testOrganization.getSlogan()).isEqualTo(DEFAULT_SLOGAN);
        assertThat(testOrganization.getDomainName()).isEqualTo(DEFAULT_DOMAIN_NAME);
        assertThat(testOrganization.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testOrganization.getHrEmailAddress()).isEqualTo(DEFAULT_HR_EMAIL_ADDRESS);
        assertThat(testOrganization.getNoReplyEmailAddress()).isEqualTo(DEFAULT_NO_REPLY_EMAIL_ADDRESS);
        assertThat(testOrganization.getContactNumber()).isEqualTo(DEFAULT_CONTACT_NUMBER);
        assertThat(testOrganization.getFinanceManagerPIN()).isEqualTo(DEFAULT_FINANCE_MANAGER_PIN);
        assertThat(testOrganization.getFinanceManagerSignature()).isEqualTo(DEFAULT_FINANCE_MANAGER_SIGNATURE);
        assertThat(testOrganization.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testOrganization.getDocumentLetterHead()).isEqualTo(DEFAULT_DOCUMENT_LETTER_HEAD);
        assertThat(testOrganization.getPfStatementLetterHead()).isEqualTo(DEFAULT_PF_STATEMENT_LETTER_HEAD);
        assertThat(testOrganization.getTaxStatementLetterHead()).isEqualTo(DEFAULT_TAX_STATEMENT_LETTER_HEAD);
        assertThat(testOrganization.getNomineeLetterHead()).isEqualTo(DEFAULT_NOMINEE_LETTER_HEAD);
        assertThat(testOrganization.getSalaryPayslipLetterHead()).isEqualTo(DEFAULT_SALARY_PAYSLIP_LETTER_HEAD);
        assertThat(testOrganization.getFestivalBonusPayslipLetterHead()).isEqualTo(DEFAULT_FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD);
        assertThat(testOrganization.getRecruitmentRequisitionLetterHead()).isEqualTo(DEFAULT_RECRUITMENT_REQUISITION_LETTER_HEAD);
        assertThat(testOrganization.getHasOrganizationStamp()).isEqualTo(DEFAULT_HAS_ORGANIZATION_STAMP);
        assertThat(testOrganization.getOrganizationStamp()).isEqualTo(DEFAULT_ORGANIZATION_STAMP);
        assertThat(testOrganization.getLinkedin()).isEqualTo(DEFAULT_LINKEDIN);
        assertThat(testOrganization.getTwitter()).isEqualTo(DEFAULT_TWITTER);
        assertThat(testOrganization.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testOrganization.getYoutube()).isEqualTo(DEFAULT_YOUTUBE);
        assertThat(testOrganization.getInstagram()).isEqualTo(DEFAULT_INSTAGRAM);
        assertThat(testOrganization.getWhatsapp()).isEqualTo(DEFAULT_WHATSAPP);
    }

    @Test
    @Transactional
    void createOrganizationWithExistingId() throws Exception {
        // Create the Organization with an existing ID
        organization.setId(1L);
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        int databaseSizeBeforeCreate = organizationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkShortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setShortName(null);

        // Create the Organization, which fails.
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        restOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setFullName(null);

        // Create the Organization, which fails.
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        restOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDomainNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setDomainName(null);

        // Create the Organization, which fails.
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        restOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setEmailAddress(null);

        // Create the Organization, which fails.
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        restOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrganizations() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].slogan").value(hasItem(DEFAULT_SLOGAN)))
            .andExpect(jsonPath("$.[*].domainName").value(hasItem(DEFAULT_DOMAIN_NAME)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].hrEmailAddress").value(hasItem(DEFAULT_HR_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].noReplyEmailAddress").value(hasItem(DEFAULT_NO_REPLY_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].financeManagerPIN").value(hasItem(DEFAULT_FINANCE_MANAGER_PIN)))
            .andExpect(jsonPath("$.[*].financeManagerSignature").value(hasItem(DEFAULT_FINANCE_MANAGER_SIGNATURE.toString())))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO.toString())))
            .andExpect(jsonPath("$.[*].documentLetterHead").value(hasItem(DEFAULT_DOCUMENT_LETTER_HEAD.toString())))
            .andExpect(jsonPath("$.[*].pfStatementLetterHead").value(hasItem(DEFAULT_PF_STATEMENT_LETTER_HEAD.toString())))
            .andExpect(jsonPath("$.[*].taxStatementLetterHead").value(hasItem(DEFAULT_TAX_STATEMENT_LETTER_HEAD.toString())))
            .andExpect(jsonPath("$.[*].nomineeLetterHead").value(hasItem(DEFAULT_NOMINEE_LETTER_HEAD.toString())))
            .andExpect(jsonPath("$.[*].salaryPayslipLetterHead").value(hasItem(DEFAULT_SALARY_PAYSLIP_LETTER_HEAD.toString())))
            .andExpect(
                jsonPath("$.[*].festivalBonusPayslipLetterHead").value(hasItem(DEFAULT_FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD.toString()))
            )
            .andExpect(
                jsonPath("$.[*].recruitmentRequisitionLetterHead").value(hasItem(DEFAULT_RECRUITMENT_REQUISITION_LETTER_HEAD.toString()))
            )
            .andExpect(jsonPath("$.[*].hasOrganizationStamp").value(hasItem(DEFAULT_HAS_ORGANIZATION_STAMP.booleanValue())))
            .andExpect(jsonPath("$.[*].organizationStamp").value(hasItem(DEFAULT_ORGANIZATION_STAMP.toString())))
            .andExpect(jsonPath("$.[*].linkedin").value(hasItem(DEFAULT_LINKEDIN)))
            .andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].youtube").value(hasItem(DEFAULT_YOUTUBE)))
            .andExpect(jsonPath("$.[*].instagram").value(hasItem(DEFAULT_INSTAGRAM)))
            .andExpect(jsonPath("$.[*].whatsapp").value(hasItem(DEFAULT_WHATSAPP)));
    }

    @Test
    @Transactional
    void getOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get the organization
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL_ID, organization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organization.getId().intValue()))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.slogan").value(DEFAULT_SLOGAN))
            .andExpect(jsonPath("$.domainName").value(DEFAULT_DOMAIN_NAME))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.hrEmailAddress").value(DEFAULT_HR_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.noReplyEmailAddress").value(DEFAULT_NO_REPLY_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER))
            .andExpect(jsonPath("$.financeManagerPIN").value(DEFAULT_FINANCE_MANAGER_PIN))
            .andExpect(jsonPath("$.financeManagerSignature").value(DEFAULT_FINANCE_MANAGER_SIGNATURE.toString()))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO.toString()))
            .andExpect(jsonPath("$.documentLetterHead").value(DEFAULT_DOCUMENT_LETTER_HEAD.toString()))
            .andExpect(jsonPath("$.pfStatementLetterHead").value(DEFAULT_PF_STATEMENT_LETTER_HEAD.toString()))
            .andExpect(jsonPath("$.taxStatementLetterHead").value(DEFAULT_TAX_STATEMENT_LETTER_HEAD.toString()))
            .andExpect(jsonPath("$.nomineeLetterHead").value(DEFAULT_NOMINEE_LETTER_HEAD.toString()))
            .andExpect(jsonPath("$.salaryPayslipLetterHead").value(DEFAULT_SALARY_PAYSLIP_LETTER_HEAD.toString()))
            .andExpect(jsonPath("$.festivalBonusPayslipLetterHead").value(DEFAULT_FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD.toString()))
            .andExpect(jsonPath("$.recruitmentRequisitionLetterHead").value(DEFAULT_RECRUITMENT_REQUISITION_LETTER_HEAD.toString()))
            .andExpect(jsonPath("$.hasOrganizationStamp").value(DEFAULT_HAS_ORGANIZATION_STAMP.booleanValue()))
            .andExpect(jsonPath("$.organizationStamp").value(DEFAULT_ORGANIZATION_STAMP.toString()))
            .andExpect(jsonPath("$.linkedin").value(DEFAULT_LINKEDIN))
            .andExpect(jsonPath("$.twitter").value(DEFAULT_TWITTER))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK))
            .andExpect(jsonPath("$.youtube").value(DEFAULT_YOUTUBE))
            .andExpect(jsonPath("$.instagram").value(DEFAULT_INSTAGRAM))
            .andExpect(jsonPath("$.whatsapp").value(DEFAULT_WHATSAPP));
    }

    @Test
    @Transactional
    void getNonExistingOrganization() throws Exception {
        // Get the organization
        restOrganizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization
        Organization updatedOrganization = organizationRepository.findById(organization.getId()).get();
        // Disconnect from session so that the updates on updatedOrganization are not directly saved in db
        em.detach(updatedOrganization);
        updatedOrganization
            .shortName(UPDATED_SHORT_NAME)
            .fullName(UPDATED_FULL_NAME)
            .slogan(UPDATED_SLOGAN)
            .domainName(UPDATED_DOMAIN_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .hrEmailAddress(UPDATED_HR_EMAIL_ADDRESS)
            .noReplyEmailAddress(UPDATED_NO_REPLY_EMAIL_ADDRESS)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .financeManagerPIN(UPDATED_FINANCE_MANAGER_PIN)
            .financeManagerSignature(UPDATED_FINANCE_MANAGER_SIGNATURE)
            .logo(UPDATED_LOGO)
            .documentLetterHead(UPDATED_DOCUMENT_LETTER_HEAD)
            .pfStatementLetterHead(UPDATED_PF_STATEMENT_LETTER_HEAD)
            .taxStatementLetterHead(UPDATED_TAX_STATEMENT_LETTER_HEAD)
            .nomineeLetterHead(UPDATED_NOMINEE_LETTER_HEAD)
            .salaryPayslipLetterHead(UPDATED_SALARY_PAYSLIP_LETTER_HEAD)
            .festivalBonusPayslipLetterHead(UPDATED_FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD)
            .recruitmentRequisitionLetterHead(UPDATED_RECRUITMENT_REQUISITION_LETTER_HEAD)
            .hasOrganizationStamp(UPDATED_HAS_ORGANIZATION_STAMP)
            .organizationStamp(UPDATED_ORGANIZATION_STAMP)
            .linkedin(UPDATED_LINKEDIN)
            .twitter(UPDATED_TWITTER)
            .facebook(UPDATED_FACEBOOK)
            .youtube(UPDATED_YOUTUBE)
            .instagram(UPDATED_INSTAGRAM)
            .whatsapp(UPDATED_WHATSAPP);
        OrganizationDTO organizationDTO = organizationMapper.toDto(updatedOrganization);

        restOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testOrganization.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testOrganization.getSlogan()).isEqualTo(UPDATED_SLOGAN);
        assertThat(testOrganization.getDomainName()).isEqualTo(UPDATED_DOMAIN_NAME);
        assertThat(testOrganization.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testOrganization.getHrEmailAddress()).isEqualTo(UPDATED_HR_EMAIL_ADDRESS);
        assertThat(testOrganization.getNoReplyEmailAddress()).isEqualTo(UPDATED_NO_REPLY_EMAIL_ADDRESS);
        assertThat(testOrganization.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testOrganization.getFinanceManagerPIN()).isEqualTo(UPDATED_FINANCE_MANAGER_PIN);
        assertThat(testOrganization.getFinanceManagerSignature()).isEqualTo(UPDATED_FINANCE_MANAGER_SIGNATURE);
        assertThat(testOrganization.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testOrganization.getDocumentLetterHead()).isEqualTo(UPDATED_DOCUMENT_LETTER_HEAD);
        assertThat(testOrganization.getPfStatementLetterHead()).isEqualTo(UPDATED_PF_STATEMENT_LETTER_HEAD);
        assertThat(testOrganization.getTaxStatementLetterHead()).isEqualTo(UPDATED_TAX_STATEMENT_LETTER_HEAD);
        assertThat(testOrganization.getNomineeLetterHead()).isEqualTo(UPDATED_NOMINEE_LETTER_HEAD);
        assertThat(testOrganization.getSalaryPayslipLetterHead()).isEqualTo(UPDATED_SALARY_PAYSLIP_LETTER_HEAD);
        assertThat(testOrganization.getFestivalBonusPayslipLetterHead()).isEqualTo(UPDATED_FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD);
        assertThat(testOrganization.getRecruitmentRequisitionLetterHead()).isEqualTo(UPDATED_RECRUITMENT_REQUISITION_LETTER_HEAD);
        assertThat(testOrganization.getHasOrganizationStamp()).isEqualTo(UPDATED_HAS_ORGANIZATION_STAMP);
        assertThat(testOrganization.getOrganizationStamp()).isEqualTo(UPDATED_ORGANIZATION_STAMP);
        assertThat(testOrganization.getLinkedin()).isEqualTo(UPDATED_LINKEDIN);
        assertThat(testOrganization.getTwitter()).isEqualTo(UPDATED_TWITTER);
        assertThat(testOrganization.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testOrganization.getYoutube()).isEqualTo(UPDATED_YOUTUBE);
        assertThat(testOrganization.getInstagram()).isEqualTo(UPDATED_INSTAGRAM);
        assertThat(testOrganization.getWhatsapp()).isEqualTo(UPDATED_WHATSAPP);
    }

    @Test
    @Transactional
    void putNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganizationWithPatch() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization using partial update
        Organization partialUpdatedOrganization = new Organization();
        partialUpdatedOrganization.setId(organization.getId());

        partialUpdatedOrganization
            .shortName(UPDATED_SHORT_NAME)
            .domainName(UPDATED_DOMAIN_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .financeManagerPIN(UPDATED_FINANCE_MANAGER_PIN)
            .logo(UPDATED_LOGO)
            .pfStatementLetterHead(UPDATED_PF_STATEMENT_LETTER_HEAD)
            .nomineeLetterHead(UPDATED_NOMINEE_LETTER_HEAD)
            .hasOrganizationStamp(UPDATED_HAS_ORGANIZATION_STAMP)
            .linkedin(UPDATED_LINKEDIN)
            .twitter(UPDATED_TWITTER)
            .youtube(UPDATED_YOUTUBE)
            .whatsapp(UPDATED_WHATSAPP);

        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganization))
            )
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testOrganization.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testOrganization.getSlogan()).isEqualTo(DEFAULT_SLOGAN);
        assertThat(testOrganization.getDomainName()).isEqualTo(UPDATED_DOMAIN_NAME);
        assertThat(testOrganization.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testOrganization.getHrEmailAddress()).isEqualTo(DEFAULT_HR_EMAIL_ADDRESS);
        assertThat(testOrganization.getNoReplyEmailAddress()).isEqualTo(DEFAULT_NO_REPLY_EMAIL_ADDRESS);
        assertThat(testOrganization.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testOrganization.getFinanceManagerPIN()).isEqualTo(UPDATED_FINANCE_MANAGER_PIN);
        assertThat(testOrganization.getFinanceManagerSignature()).isEqualTo(DEFAULT_FINANCE_MANAGER_SIGNATURE);
        assertThat(testOrganization.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testOrganization.getDocumentLetterHead()).isEqualTo(DEFAULT_DOCUMENT_LETTER_HEAD);
        assertThat(testOrganization.getPfStatementLetterHead()).isEqualTo(UPDATED_PF_STATEMENT_LETTER_HEAD);
        assertThat(testOrganization.getTaxStatementLetterHead()).isEqualTo(DEFAULT_TAX_STATEMENT_LETTER_HEAD);
        assertThat(testOrganization.getNomineeLetterHead()).isEqualTo(UPDATED_NOMINEE_LETTER_HEAD);
        assertThat(testOrganization.getSalaryPayslipLetterHead()).isEqualTo(DEFAULT_SALARY_PAYSLIP_LETTER_HEAD);
        assertThat(testOrganization.getFestivalBonusPayslipLetterHead()).isEqualTo(DEFAULT_FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD);
        assertThat(testOrganization.getRecruitmentRequisitionLetterHead()).isEqualTo(DEFAULT_RECRUITMENT_REQUISITION_LETTER_HEAD);
        assertThat(testOrganization.getHasOrganizationStamp()).isEqualTo(UPDATED_HAS_ORGANIZATION_STAMP);
        assertThat(testOrganization.getOrganizationStamp()).isEqualTo(DEFAULT_ORGANIZATION_STAMP);
        assertThat(testOrganization.getLinkedin()).isEqualTo(UPDATED_LINKEDIN);
        assertThat(testOrganization.getTwitter()).isEqualTo(UPDATED_TWITTER);
        assertThat(testOrganization.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testOrganization.getYoutube()).isEqualTo(UPDATED_YOUTUBE);
        assertThat(testOrganization.getInstagram()).isEqualTo(DEFAULT_INSTAGRAM);
        assertThat(testOrganization.getWhatsapp()).isEqualTo(UPDATED_WHATSAPP);
    }

    @Test
    @Transactional
    void fullUpdateOrganizationWithPatch() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization using partial update
        Organization partialUpdatedOrganization = new Organization();
        partialUpdatedOrganization.setId(organization.getId());

        partialUpdatedOrganization
            .shortName(UPDATED_SHORT_NAME)
            .fullName(UPDATED_FULL_NAME)
            .slogan(UPDATED_SLOGAN)
            .domainName(UPDATED_DOMAIN_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .hrEmailAddress(UPDATED_HR_EMAIL_ADDRESS)
            .noReplyEmailAddress(UPDATED_NO_REPLY_EMAIL_ADDRESS)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .financeManagerPIN(UPDATED_FINANCE_MANAGER_PIN)
            .financeManagerSignature(UPDATED_FINANCE_MANAGER_SIGNATURE)
            .logo(UPDATED_LOGO)
            .documentLetterHead(UPDATED_DOCUMENT_LETTER_HEAD)
            .pfStatementLetterHead(UPDATED_PF_STATEMENT_LETTER_HEAD)
            .taxStatementLetterHead(UPDATED_TAX_STATEMENT_LETTER_HEAD)
            .nomineeLetterHead(UPDATED_NOMINEE_LETTER_HEAD)
            .salaryPayslipLetterHead(UPDATED_SALARY_PAYSLIP_LETTER_HEAD)
            .festivalBonusPayslipLetterHead(UPDATED_FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD)
            .recruitmentRequisitionLetterHead(UPDATED_RECRUITMENT_REQUISITION_LETTER_HEAD)
            .hasOrganizationStamp(UPDATED_HAS_ORGANIZATION_STAMP)
            .organizationStamp(UPDATED_ORGANIZATION_STAMP)
            .linkedin(UPDATED_LINKEDIN)
            .twitter(UPDATED_TWITTER)
            .facebook(UPDATED_FACEBOOK)
            .youtube(UPDATED_YOUTUBE)
            .instagram(UPDATED_INSTAGRAM)
            .whatsapp(UPDATED_WHATSAPP);

        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganization))
            )
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testOrganization.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testOrganization.getSlogan()).isEqualTo(UPDATED_SLOGAN);
        assertThat(testOrganization.getDomainName()).isEqualTo(UPDATED_DOMAIN_NAME);
        assertThat(testOrganization.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testOrganization.getHrEmailAddress()).isEqualTo(UPDATED_HR_EMAIL_ADDRESS);
        assertThat(testOrganization.getNoReplyEmailAddress()).isEqualTo(UPDATED_NO_REPLY_EMAIL_ADDRESS);
        assertThat(testOrganization.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testOrganization.getFinanceManagerPIN()).isEqualTo(UPDATED_FINANCE_MANAGER_PIN);
        assertThat(testOrganization.getFinanceManagerSignature()).isEqualTo(UPDATED_FINANCE_MANAGER_SIGNATURE);
        assertThat(testOrganization.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testOrganization.getDocumentLetterHead()).isEqualTo(UPDATED_DOCUMENT_LETTER_HEAD);
        assertThat(testOrganization.getPfStatementLetterHead()).isEqualTo(UPDATED_PF_STATEMENT_LETTER_HEAD);
        assertThat(testOrganization.getTaxStatementLetterHead()).isEqualTo(UPDATED_TAX_STATEMENT_LETTER_HEAD);
        assertThat(testOrganization.getNomineeLetterHead()).isEqualTo(UPDATED_NOMINEE_LETTER_HEAD);
        assertThat(testOrganization.getSalaryPayslipLetterHead()).isEqualTo(UPDATED_SALARY_PAYSLIP_LETTER_HEAD);
        assertThat(testOrganization.getFestivalBonusPayslipLetterHead()).isEqualTo(UPDATED_FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD);
        assertThat(testOrganization.getRecruitmentRequisitionLetterHead()).isEqualTo(UPDATED_RECRUITMENT_REQUISITION_LETTER_HEAD);
        assertThat(testOrganization.getHasOrganizationStamp()).isEqualTo(UPDATED_HAS_ORGANIZATION_STAMP);
        assertThat(testOrganization.getOrganizationStamp()).isEqualTo(UPDATED_ORGANIZATION_STAMP);
        assertThat(testOrganization.getLinkedin()).isEqualTo(UPDATED_LINKEDIN);
        assertThat(testOrganization.getTwitter()).isEqualTo(UPDATED_TWITTER);
        assertThat(testOrganization.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testOrganization.getYoutube()).isEqualTo(UPDATED_YOUTUBE);
        assertThat(testOrganization.getInstagram()).isEqualTo(UPDATED_INSTAGRAM);
        assertThat(testOrganization.getWhatsapp()).isEqualTo(UPDATED_WHATSAPP);
    }

    @Test
    @Transactional
    void patchNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organizationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeDelete = organizationRepository.findAll().size();

        // Delete the organization
        restOrganizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, organization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
