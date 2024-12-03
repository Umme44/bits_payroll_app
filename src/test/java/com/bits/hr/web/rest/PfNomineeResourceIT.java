package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.PfNominee;
import com.bits.hr.domain.enumeration.IdentityType;
import com.bits.hr.domain.enumeration.IdentityType;
import com.bits.hr.repository.PfNomineeRepository;
import com.bits.hr.service.dto.PfNomineeDTO;
import com.bits.hr.service.mapper.PfNomineeMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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

/**
 * Integration tests for the {@link PfNomineeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PfNomineeResourceIT {

    private static final LocalDate DEFAULT_NOMINATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NOMINATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRESENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PRESENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PERMANENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PERMANENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_RELATIONSHIP = "AAAAAAAAAA";
    private static final String UPDATED_RELATIONSHIP = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final Double DEFAULT_SHARE_PERCENTAGE = 1D;
    private static final Double UPDATED_SHARE_PERCENTAGE = 2D;

    private static final String DEFAULT_NID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NID_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_NID_VERIFIED = false;
    private static final Boolean UPDATED_IS_NID_VERIFIED = true;

    private static final String DEFAULT_PASSPORT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PASSPORT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BRN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BRN_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_FATHER_OR_SPOUSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_FATHER_OR_SPOUSE_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_GUARDIAN_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GUARDIAN_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_GUARDIAN_PRESENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_PRESENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_PERMANENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_PERMANENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_PROOF_OF_IDENTITY_OF_LEGAL_GUARDIAN = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_PROOF_OF_IDENTITY_OF_LEGAL_GUARDIAN = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_RELATIONSHIP_WITH_NOMINEE = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_RELATIONSHIP_WITH_NOMINEE = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_NID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_NID_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_BRN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_BRN_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_ID_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_GUARDIAN_NID_VERIFIED = false;
    private static final Boolean UPDATED_IS_GUARDIAN_NID_VERIFIED = true;

    private static final Boolean DEFAULT_IS_APPROVED = false;
    private static final Boolean UPDATED_IS_APPROVED = true;

    private static final IdentityType DEFAULT_IDENTITY_TYPE = IdentityType.NID;
    private static final IdentityType UPDATED_IDENTITY_TYPE = IdentityType.PASSPORT;

    private static final String DEFAULT_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final IdentityType DEFAULT_GUARDIAN_IDENTITY_TYPE = IdentityType.NID;
    private static final IdentityType UPDATED_GUARDIAN_IDENTITY_TYPE = IdentityType.PASSPORT;

    private static final String DEFAULT_GUARDIAN_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pf-nominees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PfNomineeRepository pfNomineeRepository;

    @Autowired
    private PfNomineeMapper pfNomineeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPfNomineeMockMvc;

    private PfNominee pfNominee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PfNominee createEntity(EntityManager em) {
        PfNominee pfNominee = new PfNominee()
            .nominationDate(DEFAULT_NOMINATION_DATE)
            .fullName(DEFAULT_FULL_NAME)
            .presentAddress(DEFAULT_PRESENT_ADDRESS)
            .permanentAddress(DEFAULT_PERMANENT_ADDRESS)
            .relationship(DEFAULT_RELATIONSHIP)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .age(DEFAULT_AGE)
            .sharePercentage(DEFAULT_SHARE_PERCENTAGE)
            .nidNumber(DEFAULT_NID_NUMBER)
            .isNidVerified(DEFAULT_IS_NID_VERIFIED)
            .passportNumber(DEFAULT_PASSPORT_NUMBER)
            .brnNumber(DEFAULT_BRN_NUMBER)
            .photo(DEFAULT_PHOTO)
            .guardianName(DEFAULT_GUARDIAN_NAME)
            .guardianFatherOrSpouseName(DEFAULT_GUARDIAN_FATHER_OR_SPOUSE_NAME)
            .guardianDateOfBirth(DEFAULT_GUARDIAN_DATE_OF_BIRTH)
            .guardianPresentAddress(DEFAULT_GUARDIAN_PRESENT_ADDRESS)
            .guardianPermanentAddress(DEFAULT_GUARDIAN_PERMANENT_ADDRESS)
            .guardianProofOfIdentityOfLegalGuardian(DEFAULT_GUARDIAN_PROOF_OF_IDENTITY_OF_LEGAL_GUARDIAN)
            .guardianRelationshipWithNominee(DEFAULT_GUARDIAN_RELATIONSHIP_WITH_NOMINEE)
            .guardianNidNumber(DEFAULT_GUARDIAN_NID_NUMBER)
            .guardianBrnNumber(DEFAULT_GUARDIAN_BRN_NUMBER)
            .guardianIdNumber(DEFAULT_GUARDIAN_ID_NUMBER)
            .isGuardianNidVerified(DEFAULT_IS_GUARDIAN_NID_VERIFIED)
            .isApproved(DEFAULT_IS_APPROVED)
            .identityType(DEFAULT_IDENTITY_TYPE)
            .idNumber(DEFAULT_ID_NUMBER)
            .documentName(DEFAULT_DOCUMENT_NAME)
            .guardianIdentityType(DEFAULT_GUARDIAN_IDENTITY_TYPE)
            .guardianDocumentName(DEFAULT_GUARDIAN_DOCUMENT_NAME);
        return pfNominee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PfNominee createUpdatedEntity(EntityManager em) {
        PfNominee pfNominee = new PfNominee()
            .nominationDate(UPDATED_NOMINATION_DATE)
            .fullName(UPDATED_FULL_NAME)
            .presentAddress(UPDATED_PRESENT_ADDRESS)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .relationship(UPDATED_RELATIONSHIP)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .age(UPDATED_AGE)
            .sharePercentage(UPDATED_SHARE_PERCENTAGE)
            .nidNumber(UPDATED_NID_NUMBER)
            .isNidVerified(UPDATED_IS_NID_VERIFIED)
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .brnNumber(UPDATED_BRN_NUMBER)
            .photo(UPDATED_PHOTO)
            .guardianName(UPDATED_GUARDIAN_NAME)
            .guardianFatherOrSpouseName(UPDATED_GUARDIAN_FATHER_OR_SPOUSE_NAME)
            .guardianDateOfBirth(UPDATED_GUARDIAN_DATE_OF_BIRTH)
            .guardianPresentAddress(UPDATED_GUARDIAN_PRESENT_ADDRESS)
            .guardianPermanentAddress(UPDATED_GUARDIAN_PERMANENT_ADDRESS)
            .guardianProofOfIdentityOfLegalGuardian(UPDATED_GUARDIAN_PROOF_OF_IDENTITY_OF_LEGAL_GUARDIAN)
            .guardianRelationshipWithNominee(UPDATED_GUARDIAN_RELATIONSHIP_WITH_NOMINEE)
            .guardianNidNumber(UPDATED_GUARDIAN_NID_NUMBER)
            .guardianBrnNumber(UPDATED_GUARDIAN_BRN_NUMBER)
            .guardianIdNumber(UPDATED_GUARDIAN_ID_NUMBER)
            .isGuardianNidVerified(UPDATED_IS_GUARDIAN_NID_VERIFIED)
            .isApproved(UPDATED_IS_APPROVED)
            .identityType(UPDATED_IDENTITY_TYPE)
            .idNumber(UPDATED_ID_NUMBER)
            .documentName(UPDATED_DOCUMENT_NAME)
            .guardianIdentityType(UPDATED_GUARDIAN_IDENTITY_TYPE)
            .guardianDocumentName(UPDATED_GUARDIAN_DOCUMENT_NAME);
        return pfNominee;
    }

    @BeforeEach
    public void initTest() {
        pfNominee = createEntity(em);
    }

    @Test
    @Transactional
    void createPfNominee() throws Exception {
        int databaseSizeBeforeCreate = pfNomineeRepository.findAll().size();
        // Create the PfNominee
        PfNomineeDTO pfNomineeDTO = pfNomineeMapper.toDto(pfNominee);
        restPfNomineeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfNomineeDTO)))
            .andExpect(status().isCreated());

        // Validate the PfNominee in the database
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();
        assertThat(pfNomineeList).hasSize(databaseSizeBeforeCreate + 1);
        PfNominee testPfNominee = pfNomineeList.get(pfNomineeList.size() - 1);
        assertThat(testPfNominee.getNominationDate()).isEqualTo(DEFAULT_NOMINATION_DATE);
        assertThat(testPfNominee.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testPfNominee.getPresentAddress()).isEqualTo(DEFAULT_PRESENT_ADDRESS);
        assertThat(testPfNominee.getPermanentAddress()).isEqualTo(DEFAULT_PERMANENT_ADDRESS);
        assertThat(testPfNominee.getRelationship()).isEqualTo(DEFAULT_RELATIONSHIP);
        assertThat(testPfNominee.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testPfNominee.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testPfNominee.getSharePercentage()).isEqualTo(DEFAULT_SHARE_PERCENTAGE);
        assertThat(testPfNominee.getNidNumber()).isEqualTo(DEFAULT_NID_NUMBER);
        assertThat(testPfNominee.getIsNidVerified()).isEqualTo(DEFAULT_IS_NID_VERIFIED);
        assertThat(testPfNominee.getPassportNumber()).isEqualTo(DEFAULT_PASSPORT_NUMBER);
        assertThat(testPfNominee.getBrnNumber()).isEqualTo(DEFAULT_BRN_NUMBER);
        assertThat(testPfNominee.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testPfNominee.getGuardianName()).isEqualTo(DEFAULT_GUARDIAN_NAME);
        assertThat(testPfNominee.getGuardianFatherOrSpouseName()).isEqualTo(DEFAULT_GUARDIAN_FATHER_OR_SPOUSE_NAME);
        assertThat(testPfNominee.getGuardianDateOfBirth()).isEqualTo(DEFAULT_GUARDIAN_DATE_OF_BIRTH);
        assertThat(testPfNominee.getGuardianPresentAddress()).isEqualTo(DEFAULT_GUARDIAN_PRESENT_ADDRESS);
        assertThat(testPfNominee.getGuardianPermanentAddress()).isEqualTo(DEFAULT_GUARDIAN_PERMANENT_ADDRESS);
        assertThat(testPfNominee.getGuardianProofOfIdentityOfLegalGuardian())
            .isEqualTo(DEFAULT_GUARDIAN_PROOF_OF_IDENTITY_OF_LEGAL_GUARDIAN);
        assertThat(testPfNominee.getGuardianRelationshipWithNominee()).isEqualTo(DEFAULT_GUARDIAN_RELATIONSHIP_WITH_NOMINEE);
        assertThat(testPfNominee.getGuardianNidNumber()).isEqualTo(DEFAULT_GUARDIAN_NID_NUMBER);
        assertThat(testPfNominee.getGuardianBrnNumber()).isEqualTo(DEFAULT_GUARDIAN_BRN_NUMBER);
        assertThat(testPfNominee.getGuardianIdNumber()).isEqualTo(DEFAULT_GUARDIAN_ID_NUMBER);
        assertThat(testPfNominee.getIsGuardianNidVerified()).isEqualTo(DEFAULT_IS_GUARDIAN_NID_VERIFIED);
        assertThat(testPfNominee.getIsApproved()).isEqualTo(DEFAULT_IS_APPROVED);
        assertThat(testPfNominee.getIdentityType()).isEqualTo(DEFAULT_IDENTITY_TYPE);
        assertThat(testPfNominee.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testPfNominee.getDocumentName()).isEqualTo(DEFAULT_DOCUMENT_NAME);
        assertThat(testPfNominee.getGuardianIdentityType()).isEqualTo(DEFAULT_GUARDIAN_IDENTITY_TYPE);
        assertThat(testPfNominee.getGuardianDocumentName()).isEqualTo(DEFAULT_GUARDIAN_DOCUMENT_NAME);
    }

    @Test
    @Transactional
    void createPfNomineeWithExistingId() throws Exception {
        // Create the PfNominee with an existing ID
        pfNominee.setId(1L);
        PfNomineeDTO pfNomineeDTO = pfNomineeMapper.toDto(pfNominee);

        int databaseSizeBeforeCreate = pfNomineeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPfNomineeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfNomineeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PfNominee in the database
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();
        assertThat(pfNomineeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentityTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pfNomineeRepository.findAll().size();
        // set the field null
        pfNominee.setIdentityType(null);

        // Create the PfNominee, which fails.
        PfNomineeDTO pfNomineeDTO = pfNomineeMapper.toDto(pfNominee);

        restPfNomineeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfNomineeDTO)))
            .andExpect(status().isBadRequest());

        List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();
        assertThat(pfNomineeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = pfNomineeRepository.findAll().size();
        // set the field null
        pfNominee.setIdNumber(null);

        // Create the PfNominee, which fails.
        PfNomineeDTO pfNomineeDTO = pfNomineeMapper.toDto(pfNominee);

        restPfNomineeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfNomineeDTO)))
            .andExpect(status().isBadRequest());

        List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();
        assertThat(pfNomineeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPfNominees() throws Exception {
        // Initialize the database
        pfNomineeRepository.saveAndFlush(pfNominee);

        // Get all the pfNomineeList
        restPfNomineeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pfNominee.getId().intValue())))
            .andExpect(jsonPath("$.[*].nominationDate").value(hasItem(DEFAULT_NOMINATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].presentAddress").value(hasItem(DEFAULT_PRESENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].permanentAddress").value(hasItem(DEFAULT_PERMANENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].relationship").value(hasItem(DEFAULT_RELATIONSHIP)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].sharePercentage").value(hasItem(DEFAULT_SHARE_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].nidNumber").value(hasItem(DEFAULT_NID_NUMBER)))
            .andExpect(jsonPath("$.[*].isNidVerified").value(hasItem(DEFAULT_IS_NID_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].passportNumber").value(hasItem(DEFAULT_PASSPORT_NUMBER)))
            .andExpect(jsonPath("$.[*].brnNumber").value(hasItem(DEFAULT_BRN_NUMBER)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.[*].guardianName").value(hasItem(DEFAULT_GUARDIAN_NAME)))
            .andExpect(jsonPath("$.[*].guardianFatherOrSpouseName").value(hasItem(DEFAULT_GUARDIAN_FATHER_OR_SPOUSE_NAME)))
            .andExpect(jsonPath("$.[*].guardianDateOfBirth").value(hasItem(DEFAULT_GUARDIAN_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].guardianPresentAddress").value(hasItem(DEFAULT_GUARDIAN_PRESENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].guardianPermanentAddress").value(hasItem(DEFAULT_GUARDIAN_PERMANENT_ADDRESS)))
            .andExpect(
                jsonPath("$.[*].guardianProofOfIdentityOfLegalGuardian")
                    .value(hasItem(DEFAULT_GUARDIAN_PROOF_OF_IDENTITY_OF_LEGAL_GUARDIAN))
            )
            .andExpect(jsonPath("$.[*].guardianRelationshipWithNominee").value(hasItem(DEFAULT_GUARDIAN_RELATIONSHIP_WITH_NOMINEE)))
            .andExpect(jsonPath("$.[*].guardianNidNumber").value(hasItem(DEFAULT_GUARDIAN_NID_NUMBER)))
            .andExpect(jsonPath("$.[*].guardianBrnNumber").value(hasItem(DEFAULT_GUARDIAN_BRN_NUMBER)))
            .andExpect(jsonPath("$.[*].guardianIdNumber").value(hasItem(DEFAULT_GUARDIAN_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].isGuardianNidVerified").value(hasItem(DEFAULT_IS_GUARDIAN_NID_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].isApproved").value(hasItem(DEFAULT_IS_APPROVED.booleanValue())))
            .andExpect(jsonPath("$.[*].identityType").value(hasItem(DEFAULT_IDENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].documentName").value(hasItem(DEFAULT_DOCUMENT_NAME)))
            .andExpect(jsonPath("$.[*].guardianIdentityType").value(hasItem(DEFAULT_GUARDIAN_IDENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].guardianDocumentName").value(hasItem(DEFAULT_GUARDIAN_DOCUMENT_NAME)));
    }

    @Test
    @Transactional
    void getPfNominee() throws Exception {
        // Initialize the database
        pfNomineeRepository.saveAndFlush(pfNominee);

        // Get the pfNominee
        restPfNomineeMockMvc
            .perform(get(ENTITY_API_URL_ID, pfNominee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pfNominee.getId().intValue()))
            .andExpect(jsonPath("$.nominationDate").value(DEFAULT_NOMINATION_DATE.toString()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.presentAddress").value(DEFAULT_PRESENT_ADDRESS))
            .andExpect(jsonPath("$.permanentAddress").value(DEFAULT_PERMANENT_ADDRESS))
            .andExpect(jsonPath("$.relationship").value(DEFAULT_RELATIONSHIP))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.sharePercentage").value(DEFAULT_SHARE_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.nidNumber").value(DEFAULT_NID_NUMBER))
            .andExpect(jsonPath("$.isNidVerified").value(DEFAULT_IS_NID_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.passportNumber").value(DEFAULT_PASSPORT_NUMBER))
            .andExpect(jsonPath("$.brnNumber").value(DEFAULT_BRN_NUMBER))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO))
            .andExpect(jsonPath("$.guardianName").value(DEFAULT_GUARDIAN_NAME))
            .andExpect(jsonPath("$.guardianFatherOrSpouseName").value(DEFAULT_GUARDIAN_FATHER_OR_SPOUSE_NAME))
            .andExpect(jsonPath("$.guardianDateOfBirth").value(DEFAULT_GUARDIAN_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.guardianPresentAddress").value(DEFAULT_GUARDIAN_PRESENT_ADDRESS))
            .andExpect(jsonPath("$.guardianPermanentAddress").value(DEFAULT_GUARDIAN_PERMANENT_ADDRESS))
            .andExpect(jsonPath("$.guardianProofOfIdentityOfLegalGuardian").value(DEFAULT_GUARDIAN_PROOF_OF_IDENTITY_OF_LEGAL_GUARDIAN))
            .andExpect(jsonPath("$.guardianRelationshipWithNominee").value(DEFAULT_GUARDIAN_RELATIONSHIP_WITH_NOMINEE))
            .andExpect(jsonPath("$.guardianNidNumber").value(DEFAULT_GUARDIAN_NID_NUMBER))
            .andExpect(jsonPath("$.guardianBrnNumber").value(DEFAULT_GUARDIAN_BRN_NUMBER))
            .andExpect(jsonPath("$.guardianIdNumber").value(DEFAULT_GUARDIAN_ID_NUMBER))
            .andExpect(jsonPath("$.isGuardianNidVerified").value(DEFAULT_IS_GUARDIAN_NID_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.isApproved").value(DEFAULT_IS_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.identityType").value(DEFAULT_IDENTITY_TYPE.toString()))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER))
            .andExpect(jsonPath("$.documentName").value(DEFAULT_DOCUMENT_NAME))
            .andExpect(jsonPath("$.guardianIdentityType").value(DEFAULT_GUARDIAN_IDENTITY_TYPE.toString()))
            .andExpect(jsonPath("$.guardianDocumentName").value(DEFAULT_GUARDIAN_DOCUMENT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPfNominee() throws Exception {
        // Get the pfNominee
        restPfNomineeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPfNominee() throws Exception {
        // Initialize the database
        pfNomineeRepository.saveAndFlush(pfNominee);

        int databaseSizeBeforeUpdate = pfNomineeRepository.findAll().size();

        // Update the pfNominee
        PfNominee updatedPfNominee = pfNomineeRepository.findById(pfNominee.getId()).get();
        // Disconnect from session so that the updates on updatedPfNominee are not directly saved in db
        em.detach(updatedPfNominee);
        updatedPfNominee
            .nominationDate(UPDATED_NOMINATION_DATE)
            .fullName(UPDATED_FULL_NAME)
            .presentAddress(UPDATED_PRESENT_ADDRESS)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .relationship(UPDATED_RELATIONSHIP)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .age(UPDATED_AGE)
            .sharePercentage(UPDATED_SHARE_PERCENTAGE)
            .nidNumber(UPDATED_NID_NUMBER)
            .isNidVerified(UPDATED_IS_NID_VERIFIED)
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .brnNumber(UPDATED_BRN_NUMBER)
            .photo(UPDATED_PHOTO)
            .guardianName(UPDATED_GUARDIAN_NAME)
            .guardianFatherOrSpouseName(UPDATED_GUARDIAN_FATHER_OR_SPOUSE_NAME)
            .guardianDateOfBirth(UPDATED_GUARDIAN_DATE_OF_BIRTH)
            .guardianPresentAddress(UPDATED_GUARDIAN_PRESENT_ADDRESS)
            .guardianPermanentAddress(UPDATED_GUARDIAN_PERMANENT_ADDRESS)
            .guardianProofOfIdentityOfLegalGuardian(UPDATED_GUARDIAN_PROOF_OF_IDENTITY_OF_LEGAL_GUARDIAN)
            .guardianRelationshipWithNominee(UPDATED_GUARDIAN_RELATIONSHIP_WITH_NOMINEE)
            .guardianNidNumber(UPDATED_GUARDIAN_NID_NUMBER)
            .guardianBrnNumber(UPDATED_GUARDIAN_BRN_NUMBER)
            .guardianIdNumber(UPDATED_GUARDIAN_ID_NUMBER)
            .isGuardianNidVerified(UPDATED_IS_GUARDIAN_NID_VERIFIED)
            .isApproved(UPDATED_IS_APPROVED)
            .identityType(UPDATED_IDENTITY_TYPE)
            .idNumber(UPDATED_ID_NUMBER)
            .documentName(UPDATED_DOCUMENT_NAME)
            .guardianIdentityType(UPDATED_GUARDIAN_IDENTITY_TYPE)
            .guardianDocumentName(UPDATED_GUARDIAN_DOCUMENT_NAME);
        PfNomineeDTO pfNomineeDTO = pfNomineeMapper.toDto(updatedPfNominee);

        restPfNomineeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pfNomineeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfNomineeDTO))
            )
            .andExpect(status().isOk());

        // Validate the PfNominee in the database
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();
        assertThat(pfNomineeList).hasSize(databaseSizeBeforeUpdate);
        PfNominee testPfNominee = pfNomineeList.get(pfNomineeList.size() - 1);
        assertThat(testPfNominee.getNominationDate()).isEqualTo(UPDATED_NOMINATION_DATE);
        assertThat(testPfNominee.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testPfNominee.getPresentAddress()).isEqualTo(UPDATED_PRESENT_ADDRESS);
        assertThat(testPfNominee.getPermanentAddress()).isEqualTo(UPDATED_PERMANENT_ADDRESS);
        assertThat(testPfNominee.getRelationship()).isEqualTo(UPDATED_RELATIONSHIP);
        assertThat(testPfNominee.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testPfNominee.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPfNominee.getSharePercentage()).isEqualTo(UPDATED_SHARE_PERCENTAGE);
        assertThat(testPfNominee.getNidNumber()).isEqualTo(UPDATED_NID_NUMBER);
        assertThat(testPfNominee.getIsNidVerified()).isEqualTo(UPDATED_IS_NID_VERIFIED);
        assertThat(testPfNominee.getPassportNumber()).isEqualTo(UPDATED_PASSPORT_NUMBER);
        assertThat(testPfNominee.getBrnNumber()).isEqualTo(UPDATED_BRN_NUMBER);
        assertThat(testPfNominee.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testPfNominee.getGuardianName()).isEqualTo(UPDATED_GUARDIAN_NAME);
        assertThat(testPfNominee.getGuardianFatherOrSpouseName()).isEqualTo(UPDATED_GUARDIAN_FATHER_OR_SPOUSE_NAME);
        assertThat(testPfNominee.getGuardianDateOfBirth()).isEqualTo(UPDATED_GUARDIAN_DATE_OF_BIRTH);
        assertThat(testPfNominee.getGuardianPresentAddress()).isEqualTo(UPDATED_GUARDIAN_PRESENT_ADDRESS);
        assertThat(testPfNominee.getGuardianPermanentAddress()).isEqualTo(UPDATED_GUARDIAN_PERMANENT_ADDRESS);
        assertThat(testPfNominee.getGuardianProofOfIdentityOfLegalGuardian())
            .isEqualTo(UPDATED_GUARDIAN_PROOF_OF_IDENTITY_OF_LEGAL_GUARDIAN);
        assertThat(testPfNominee.getGuardianRelationshipWithNominee()).isEqualTo(UPDATED_GUARDIAN_RELATIONSHIP_WITH_NOMINEE);
        assertThat(testPfNominee.getGuardianNidNumber()).isEqualTo(UPDATED_GUARDIAN_NID_NUMBER);
        assertThat(testPfNominee.getGuardianBrnNumber()).isEqualTo(UPDATED_GUARDIAN_BRN_NUMBER);
        assertThat(testPfNominee.getGuardianIdNumber()).isEqualTo(UPDATED_GUARDIAN_ID_NUMBER);
        assertThat(testPfNominee.getIsGuardianNidVerified()).isEqualTo(UPDATED_IS_GUARDIAN_NID_VERIFIED);
        assertThat(testPfNominee.getIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
        assertThat(testPfNominee.getIdentityType()).isEqualTo(UPDATED_IDENTITY_TYPE);
        assertThat(testPfNominee.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testPfNominee.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testPfNominee.getGuardianIdentityType()).isEqualTo(UPDATED_GUARDIAN_IDENTITY_TYPE);
        assertThat(testPfNominee.getGuardianDocumentName()).isEqualTo(UPDATED_GUARDIAN_DOCUMENT_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPfNominee() throws Exception {
        int databaseSizeBeforeUpdate = pfNomineeRepository.findAll().size();
        pfNominee.setId(count.incrementAndGet());

        // Create the PfNominee
        PfNomineeDTO pfNomineeDTO = pfNomineeMapper.toDto(pfNominee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPfNomineeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pfNomineeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfNomineeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfNominee in the database
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();
        assertThat(pfNomineeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPfNominee() throws Exception {
        int databaseSizeBeforeUpdate = pfNomineeRepository.findAll().size();
        pfNominee.setId(count.incrementAndGet());

        // Create the PfNominee
        PfNomineeDTO pfNomineeDTO = pfNomineeMapper.toDto(pfNominee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfNomineeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfNomineeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfNominee in the database
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();
        assertThat(pfNomineeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPfNominee() throws Exception {
        int databaseSizeBeforeUpdate = pfNomineeRepository.findAll().size();
        pfNominee.setId(count.incrementAndGet());

        // Create the PfNominee
        PfNomineeDTO pfNomineeDTO = pfNomineeMapper.toDto(pfNominee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfNomineeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfNomineeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PfNominee in the database
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();
        assertThat(pfNomineeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePfNomineeWithPatch() throws Exception {
        // Initialize the database
        pfNomineeRepository.saveAndFlush(pfNominee);

        int databaseSizeBeforeUpdate = pfNomineeRepository.findAll().size();

        // Update the pfNominee using partial update
        PfNominee partialUpdatedPfNominee = new PfNominee();
        partialUpdatedPfNominee.setId(pfNominee.getId());

        partialUpdatedPfNominee
            .fullName(UPDATED_FULL_NAME)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .age(UPDATED_AGE)
            .sharePercentage(UPDATED_SHARE_PERCENTAGE)
            .isNidVerified(UPDATED_IS_NID_VERIFIED)
            .brnNumber(UPDATED_BRN_NUMBER)
            .guardianName(UPDATED_GUARDIAN_NAME)
            .guardianDateOfBirth(UPDATED_GUARDIAN_DATE_OF_BIRTH)
            .guardianRelationshipWithNominee(UPDATED_GUARDIAN_RELATIONSHIP_WITH_NOMINEE)
            .guardianNidNumber(UPDATED_GUARDIAN_NID_NUMBER)
            .guardianBrnNumber(UPDATED_GUARDIAN_BRN_NUMBER)
            .guardianIdNumber(UPDATED_GUARDIAN_ID_NUMBER)
            .isGuardianNidVerified(UPDATED_IS_GUARDIAN_NID_VERIFIED)
            .documentName(UPDATED_DOCUMENT_NAME);

        restPfNomineeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPfNominee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPfNominee))
            )
            .andExpect(status().isOk());

        // Validate the PfNominee in the database
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();
        assertThat(pfNomineeList).hasSize(databaseSizeBeforeUpdate);
        PfNominee testPfNominee = pfNomineeList.get(pfNomineeList.size() - 1);
        assertThat(testPfNominee.getNominationDate()).isEqualTo(DEFAULT_NOMINATION_DATE);
        assertThat(testPfNominee.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testPfNominee.getPresentAddress()).isEqualTo(DEFAULT_PRESENT_ADDRESS);
        assertThat(testPfNominee.getPermanentAddress()).isEqualTo(UPDATED_PERMANENT_ADDRESS);
        assertThat(testPfNominee.getRelationship()).isEqualTo(DEFAULT_RELATIONSHIP);
        assertThat(testPfNominee.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testPfNominee.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPfNominee.getSharePercentage()).isEqualTo(UPDATED_SHARE_PERCENTAGE);
        assertThat(testPfNominee.getNidNumber()).isEqualTo(DEFAULT_NID_NUMBER);
        assertThat(testPfNominee.getIsNidVerified()).isEqualTo(UPDATED_IS_NID_VERIFIED);
        assertThat(testPfNominee.getPassportNumber()).isEqualTo(DEFAULT_PASSPORT_NUMBER);
        assertThat(testPfNominee.getBrnNumber()).isEqualTo(UPDATED_BRN_NUMBER);
        assertThat(testPfNominee.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testPfNominee.getGuardianName()).isEqualTo(UPDATED_GUARDIAN_NAME);
        assertThat(testPfNominee.getGuardianFatherOrSpouseName()).isEqualTo(DEFAULT_GUARDIAN_FATHER_OR_SPOUSE_NAME);
        assertThat(testPfNominee.getGuardianDateOfBirth()).isEqualTo(UPDATED_GUARDIAN_DATE_OF_BIRTH);
        assertThat(testPfNominee.getGuardianPresentAddress()).isEqualTo(DEFAULT_GUARDIAN_PRESENT_ADDRESS);
        assertThat(testPfNominee.getGuardianPermanentAddress()).isEqualTo(DEFAULT_GUARDIAN_PERMANENT_ADDRESS);
        assertThat(testPfNominee.getGuardianProofOfIdentityOfLegalGuardian())
            .isEqualTo(DEFAULT_GUARDIAN_PROOF_OF_IDENTITY_OF_LEGAL_GUARDIAN);
        assertThat(testPfNominee.getGuardianRelationshipWithNominee()).isEqualTo(UPDATED_GUARDIAN_RELATIONSHIP_WITH_NOMINEE);
        assertThat(testPfNominee.getGuardianNidNumber()).isEqualTo(UPDATED_GUARDIAN_NID_NUMBER);
        assertThat(testPfNominee.getGuardianBrnNumber()).isEqualTo(UPDATED_GUARDIAN_BRN_NUMBER);
        assertThat(testPfNominee.getGuardianIdNumber()).isEqualTo(UPDATED_GUARDIAN_ID_NUMBER);
        assertThat(testPfNominee.getIsGuardianNidVerified()).isEqualTo(UPDATED_IS_GUARDIAN_NID_VERIFIED);
        assertThat(testPfNominee.getIsApproved()).isEqualTo(DEFAULT_IS_APPROVED);
        assertThat(testPfNominee.getIdentityType()).isEqualTo(DEFAULT_IDENTITY_TYPE);
        assertThat(testPfNominee.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testPfNominee.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testPfNominee.getGuardianIdentityType()).isEqualTo(DEFAULT_GUARDIAN_IDENTITY_TYPE);
        assertThat(testPfNominee.getGuardianDocumentName()).isEqualTo(DEFAULT_GUARDIAN_DOCUMENT_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePfNomineeWithPatch() throws Exception {
        // Initialize the database
        pfNomineeRepository.saveAndFlush(pfNominee);

        int databaseSizeBeforeUpdate = pfNomineeRepository.findAll().size();

        // Update the pfNominee using partial update
        PfNominee partialUpdatedPfNominee = new PfNominee();
        partialUpdatedPfNominee.setId(pfNominee.getId());

        partialUpdatedPfNominee
            .nominationDate(UPDATED_NOMINATION_DATE)
            .fullName(UPDATED_FULL_NAME)
            .presentAddress(UPDATED_PRESENT_ADDRESS)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .relationship(UPDATED_RELATIONSHIP)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .age(UPDATED_AGE)
            .sharePercentage(UPDATED_SHARE_PERCENTAGE)
            .nidNumber(UPDATED_NID_NUMBER)
            .isNidVerified(UPDATED_IS_NID_VERIFIED)
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .brnNumber(UPDATED_BRN_NUMBER)
            .photo(UPDATED_PHOTO)
            .guardianName(UPDATED_GUARDIAN_NAME)
            .guardianFatherOrSpouseName(UPDATED_GUARDIAN_FATHER_OR_SPOUSE_NAME)
            .guardianDateOfBirth(UPDATED_GUARDIAN_DATE_OF_BIRTH)
            .guardianPresentAddress(UPDATED_GUARDIAN_PRESENT_ADDRESS)
            .guardianPermanentAddress(UPDATED_GUARDIAN_PERMANENT_ADDRESS)
            .guardianProofOfIdentityOfLegalGuardian(UPDATED_GUARDIAN_PROOF_OF_IDENTITY_OF_LEGAL_GUARDIAN)
            .guardianRelationshipWithNominee(UPDATED_GUARDIAN_RELATIONSHIP_WITH_NOMINEE)
            .guardianNidNumber(UPDATED_GUARDIAN_NID_NUMBER)
            .guardianBrnNumber(UPDATED_GUARDIAN_BRN_NUMBER)
            .guardianIdNumber(UPDATED_GUARDIAN_ID_NUMBER)
            .isGuardianNidVerified(UPDATED_IS_GUARDIAN_NID_VERIFIED)
            .isApproved(UPDATED_IS_APPROVED)
            .identityType(UPDATED_IDENTITY_TYPE)
            .idNumber(UPDATED_ID_NUMBER)
            .documentName(UPDATED_DOCUMENT_NAME)
            .guardianIdentityType(UPDATED_GUARDIAN_IDENTITY_TYPE)
            .guardianDocumentName(UPDATED_GUARDIAN_DOCUMENT_NAME);

        restPfNomineeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPfNominee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPfNominee))
            )
            .andExpect(status().isOk());

        // Validate the PfNominee in the database
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();
        assertThat(pfNomineeList).hasSize(databaseSizeBeforeUpdate);
        PfNominee testPfNominee = pfNomineeList.get(pfNomineeList.size() - 1);
        assertThat(testPfNominee.getNominationDate()).isEqualTo(UPDATED_NOMINATION_DATE);
        assertThat(testPfNominee.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testPfNominee.getPresentAddress()).isEqualTo(UPDATED_PRESENT_ADDRESS);
        assertThat(testPfNominee.getPermanentAddress()).isEqualTo(UPDATED_PERMANENT_ADDRESS);
        assertThat(testPfNominee.getRelationship()).isEqualTo(UPDATED_RELATIONSHIP);
        assertThat(testPfNominee.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testPfNominee.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPfNominee.getSharePercentage()).isEqualTo(UPDATED_SHARE_PERCENTAGE);
        assertThat(testPfNominee.getNidNumber()).isEqualTo(UPDATED_NID_NUMBER);
        assertThat(testPfNominee.getIsNidVerified()).isEqualTo(UPDATED_IS_NID_VERIFIED);
        assertThat(testPfNominee.getPassportNumber()).isEqualTo(UPDATED_PASSPORT_NUMBER);
        assertThat(testPfNominee.getBrnNumber()).isEqualTo(UPDATED_BRN_NUMBER);
        assertThat(testPfNominee.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testPfNominee.getGuardianName()).isEqualTo(UPDATED_GUARDIAN_NAME);
        assertThat(testPfNominee.getGuardianFatherOrSpouseName()).isEqualTo(UPDATED_GUARDIAN_FATHER_OR_SPOUSE_NAME);
        assertThat(testPfNominee.getGuardianDateOfBirth()).isEqualTo(UPDATED_GUARDIAN_DATE_OF_BIRTH);
        assertThat(testPfNominee.getGuardianPresentAddress()).isEqualTo(UPDATED_GUARDIAN_PRESENT_ADDRESS);
        assertThat(testPfNominee.getGuardianPermanentAddress()).isEqualTo(UPDATED_GUARDIAN_PERMANENT_ADDRESS);
        assertThat(testPfNominee.getGuardianProofOfIdentityOfLegalGuardian())
            .isEqualTo(UPDATED_GUARDIAN_PROOF_OF_IDENTITY_OF_LEGAL_GUARDIAN);
        assertThat(testPfNominee.getGuardianRelationshipWithNominee()).isEqualTo(UPDATED_GUARDIAN_RELATIONSHIP_WITH_NOMINEE);
        assertThat(testPfNominee.getGuardianNidNumber()).isEqualTo(UPDATED_GUARDIAN_NID_NUMBER);
        assertThat(testPfNominee.getGuardianBrnNumber()).isEqualTo(UPDATED_GUARDIAN_BRN_NUMBER);
        assertThat(testPfNominee.getGuardianIdNumber()).isEqualTo(UPDATED_GUARDIAN_ID_NUMBER);
        assertThat(testPfNominee.getIsGuardianNidVerified()).isEqualTo(UPDATED_IS_GUARDIAN_NID_VERIFIED);
        assertThat(testPfNominee.getIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
        assertThat(testPfNominee.getIdentityType()).isEqualTo(UPDATED_IDENTITY_TYPE);
        assertThat(testPfNominee.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testPfNominee.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testPfNominee.getGuardianIdentityType()).isEqualTo(UPDATED_GUARDIAN_IDENTITY_TYPE);
        assertThat(testPfNominee.getGuardianDocumentName()).isEqualTo(UPDATED_GUARDIAN_DOCUMENT_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPfNominee() throws Exception {
        int databaseSizeBeforeUpdate = pfNomineeRepository.findAll().size();
        pfNominee.setId(count.incrementAndGet());

        // Create the PfNominee
        PfNomineeDTO pfNomineeDTO = pfNomineeMapper.toDto(pfNominee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPfNomineeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pfNomineeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfNomineeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfNominee in the database
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();
        assertThat(pfNomineeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPfNominee() throws Exception {
        int databaseSizeBeforeUpdate = pfNomineeRepository.findAll().size();
        pfNominee.setId(count.incrementAndGet());

        // Create the PfNominee
        PfNomineeDTO pfNomineeDTO = pfNomineeMapper.toDto(pfNominee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfNomineeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfNomineeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfNominee in the database
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();
        assertThat(pfNomineeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPfNominee() throws Exception {
        int databaseSizeBeforeUpdate = pfNomineeRepository.findAll().size();
        pfNominee.setId(count.incrementAndGet());

        // Create the PfNominee
        PfNomineeDTO pfNomineeDTO = pfNomineeMapper.toDto(pfNominee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfNomineeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pfNomineeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PfNominee in the database
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();
        assertThat(pfNomineeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePfNominee() throws Exception {
        // Initialize the database
        pfNomineeRepository.saveAndFlush(pfNominee);

        int databaseSizeBeforeDelete = pfNomineeRepository.findAll().size();

        // Delete the pfNominee
        restPfNomineeMockMvc
            .perform(delete(ENTITY_API_URL_ID, pfNominee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PfNominee> pfNomineeList = pfNomineeRepository.findAll();
        assertThat(pfNomineeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
