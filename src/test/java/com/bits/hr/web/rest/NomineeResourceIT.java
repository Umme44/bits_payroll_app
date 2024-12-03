package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Nominee;
import com.bits.hr.domain.enumeration.IdentityType;
import com.bits.hr.domain.enumeration.IdentityType;
import com.bits.hr.domain.enumeration.NomineeType;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.NomineeRepository;
import com.bits.hr.service.dto.NomineeDTO;
import com.bits.hr.service.mapper.NomineeMapper;
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
 * Integration tests for the {@link NomineeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NomineeResourceIT {

    private static final String DEFAULT_NOMINEE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NOMINEE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRESENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PRESENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_RELATIONSHIP_WITH_EMPLOYEE = "AAAAAAAAAA";
    private static final String UPDATED_RELATIONSHIP_WITH_EMPLOYEE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final Double DEFAULT_SHARE_PERCENTAGE = 1D;
    private static final Double UPDATED_SHARE_PERCENTAGE = 2D;

    private static final String DEFAULT_IMAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PATH = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.APPROVED;

    private static final String DEFAULT_GUARDIAN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_FATHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_FATHER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_SPOUSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_SPOUSE_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_GUARDIAN_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GUARDIAN_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_GUARDIAN_PRESENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_PRESENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_RELATIONSHIP_WITH = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_RELATIONSHIP_WITH = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_IMAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_IMAGE_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_LOCKED = false;
    private static final Boolean UPDATED_IS_LOCKED = true;

    private static final LocalDate DEFAULT_NOMINATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NOMINATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PERMANENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PERMANENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_GUARDIAN_PERMANENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_PERMANENT_ADDRESS = "BBBBBBBBBB";

    private static final NomineeType DEFAULT_NOMINEE_TYPE = NomineeType.GRATUITY_FUND;
    private static final NomineeType UPDATED_NOMINEE_TYPE = NomineeType.GENERAL;

    private static final IdentityType DEFAULT_IDENTITY_TYPE = IdentityType.NID;
    private static final IdentityType UPDATED_IDENTITY_TYPE = IdentityType.PASSPORT;

    private static final String DEFAULT_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_NID_VERIFIED = false;
    private static final Boolean UPDATED_IS_NID_VERIFIED = true;

    private static final IdentityType DEFAULT_GUARDIAN_IDENTITY_TYPE = IdentityType.NID;
    private static final IdentityType UPDATED_GUARDIAN_IDENTITY_TYPE = IdentityType.PASSPORT;

    private static final String DEFAULT_GUARDIAN_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_GUARDIAN_ID_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_GUARDIAN_NID_VERIFIED = false;
    private static final Boolean UPDATED_IS_GUARDIAN_NID_VERIFIED = true;

    private static final String ENTITY_API_URL = "/api/nominees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NomineeRepository nomineeRepository;

    @Autowired
    private NomineeMapper nomineeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNomineeMockMvc;

    private Nominee nominee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nominee createEntity(EntityManager em) {
        Nominee nominee = new Nominee()
            .nomineeName(DEFAULT_NOMINEE_NAME)
            .presentAddress(DEFAULT_PRESENT_ADDRESS)
            .relationshipWithEmployee(DEFAULT_RELATIONSHIP_WITH_EMPLOYEE)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .age(DEFAULT_AGE)
            .sharePercentage(DEFAULT_SHARE_PERCENTAGE)
            .imagePath(DEFAULT_IMAGE_PATH)
            .status(DEFAULT_STATUS)
            .guardianName(DEFAULT_GUARDIAN_NAME)
            .guardianFatherName(DEFAULT_GUARDIAN_FATHER_NAME)
            .guardianSpouseName(DEFAULT_GUARDIAN_SPOUSE_NAME)
            .guardianDateOfBirth(DEFAULT_GUARDIAN_DATE_OF_BIRTH)
            .guardianPresentAddress(DEFAULT_GUARDIAN_PRESENT_ADDRESS)
            .guardianDocumentName(DEFAULT_GUARDIAN_DOCUMENT_NAME)
            .guardianRelationshipWith(DEFAULT_GUARDIAN_RELATIONSHIP_WITH)
            .guardianImagePath(DEFAULT_GUARDIAN_IMAGE_PATH)
            .isLocked(DEFAULT_IS_LOCKED)
            .nominationDate(DEFAULT_NOMINATION_DATE)
            .permanentAddress(DEFAULT_PERMANENT_ADDRESS)
            .guardianPermanentAddress(DEFAULT_GUARDIAN_PERMANENT_ADDRESS)
            .nomineeType(DEFAULT_NOMINEE_TYPE)
            .identityType(DEFAULT_IDENTITY_TYPE)
            .documentName(DEFAULT_DOCUMENT_NAME)
            .idNumber(DEFAULT_ID_NUMBER)
            .isNidVerified(DEFAULT_IS_NID_VERIFIED)
            .guardianIdentityType(DEFAULT_GUARDIAN_IDENTITY_TYPE)
            .guardianIdNumber(DEFAULT_GUARDIAN_ID_NUMBER)
            .isGuardianNidVerified(DEFAULT_IS_GUARDIAN_NID_VERIFIED);
        return nominee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nominee createUpdatedEntity(EntityManager em) {
        Nominee nominee = new Nominee()
            .nomineeName(UPDATED_NOMINEE_NAME)
            .presentAddress(UPDATED_PRESENT_ADDRESS)
            .relationshipWithEmployee(UPDATED_RELATIONSHIP_WITH_EMPLOYEE)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .age(UPDATED_AGE)
            .sharePercentage(UPDATED_SHARE_PERCENTAGE)
            .imagePath(UPDATED_IMAGE_PATH)
            .status(UPDATED_STATUS)
            .guardianName(UPDATED_GUARDIAN_NAME)
            .guardianFatherName(UPDATED_GUARDIAN_FATHER_NAME)
            .guardianSpouseName(UPDATED_GUARDIAN_SPOUSE_NAME)
            .guardianDateOfBirth(UPDATED_GUARDIAN_DATE_OF_BIRTH)
            .guardianPresentAddress(UPDATED_GUARDIAN_PRESENT_ADDRESS)
            .guardianDocumentName(UPDATED_GUARDIAN_DOCUMENT_NAME)
            .guardianRelationshipWith(UPDATED_GUARDIAN_RELATIONSHIP_WITH)
            .guardianImagePath(UPDATED_GUARDIAN_IMAGE_PATH)
            .isLocked(UPDATED_IS_LOCKED)
            .nominationDate(UPDATED_NOMINATION_DATE)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .guardianPermanentAddress(UPDATED_GUARDIAN_PERMANENT_ADDRESS)
            .nomineeType(UPDATED_NOMINEE_TYPE)
            .identityType(UPDATED_IDENTITY_TYPE)
            .documentName(UPDATED_DOCUMENT_NAME)
            .idNumber(UPDATED_ID_NUMBER)
            .isNidVerified(UPDATED_IS_NID_VERIFIED)
            .guardianIdentityType(UPDATED_GUARDIAN_IDENTITY_TYPE)
            .guardianIdNumber(UPDATED_GUARDIAN_ID_NUMBER)
            .isGuardianNidVerified(UPDATED_IS_GUARDIAN_NID_VERIFIED);
        return nominee;
    }

    @BeforeEach
    public void initTest() {
        nominee = createEntity(em);
    }

    @Test
    @Transactional
    void createNominee() throws Exception {
        int databaseSizeBeforeCreate = nomineeRepository.findAll().size();
        // Create the Nominee
        NomineeDTO nomineeDTO = nomineeMapper.toDto(nominee);
        restNomineeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nomineeDTO)))
            .andExpect(status().isCreated());

        // Validate the Nominee in the database
        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeCreate + 1);
        Nominee testNominee = nomineeList.get(nomineeList.size() - 1);
        assertThat(testNominee.getNomineeName()).isEqualTo(DEFAULT_NOMINEE_NAME);
        assertThat(testNominee.getPresentAddress()).isEqualTo(DEFAULT_PRESENT_ADDRESS);
        assertThat(testNominee.getRelationshipWithEmployee()).isEqualTo(DEFAULT_RELATIONSHIP_WITH_EMPLOYEE);
        assertThat(testNominee.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testNominee.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testNominee.getSharePercentage()).isEqualTo(DEFAULT_SHARE_PERCENTAGE);
        assertThat(testNominee.getImagePath()).isEqualTo(DEFAULT_IMAGE_PATH);
        assertThat(testNominee.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testNominee.getGuardianName()).isEqualTo(DEFAULT_GUARDIAN_NAME);
        assertThat(testNominee.getGuardianFatherName()).isEqualTo(DEFAULT_GUARDIAN_FATHER_NAME);
        assertThat(testNominee.getGuardianSpouseName()).isEqualTo(DEFAULT_GUARDIAN_SPOUSE_NAME);
        assertThat(testNominee.getGuardianDateOfBirth()).isEqualTo(DEFAULT_GUARDIAN_DATE_OF_BIRTH);
        assertThat(testNominee.getGuardianPresentAddress()).isEqualTo(DEFAULT_GUARDIAN_PRESENT_ADDRESS);
        assertThat(testNominee.getGuardianDocumentName()).isEqualTo(DEFAULT_GUARDIAN_DOCUMENT_NAME);
        assertThat(testNominee.getGuardianRelationshipWith()).isEqualTo(DEFAULT_GUARDIAN_RELATIONSHIP_WITH);
        assertThat(testNominee.getGuardianImagePath()).isEqualTo(DEFAULT_GUARDIAN_IMAGE_PATH);
        assertThat(testNominee.getIsLocked()).isEqualTo(DEFAULT_IS_LOCKED);
        assertThat(testNominee.getNominationDate()).isEqualTo(DEFAULT_NOMINATION_DATE);
        assertThat(testNominee.getPermanentAddress()).isEqualTo(DEFAULT_PERMANENT_ADDRESS);
        assertThat(testNominee.getGuardianPermanentAddress()).isEqualTo(DEFAULT_GUARDIAN_PERMANENT_ADDRESS);
        assertThat(testNominee.getNomineeType()).isEqualTo(DEFAULT_NOMINEE_TYPE);
        assertThat(testNominee.getIdentityType()).isEqualTo(DEFAULT_IDENTITY_TYPE);
        assertThat(testNominee.getDocumentName()).isEqualTo(DEFAULT_DOCUMENT_NAME);
        assertThat(testNominee.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testNominee.getIsNidVerified()).isEqualTo(DEFAULT_IS_NID_VERIFIED);
        assertThat(testNominee.getGuardianIdentityType()).isEqualTo(DEFAULT_GUARDIAN_IDENTITY_TYPE);
        assertThat(testNominee.getGuardianIdNumber()).isEqualTo(DEFAULT_GUARDIAN_ID_NUMBER);
        assertThat(testNominee.getIsGuardianNidVerified()).isEqualTo(DEFAULT_IS_GUARDIAN_NID_VERIFIED);
    }

    @Test
    @Transactional
    void createNomineeWithExistingId() throws Exception {
        // Create the Nominee with an existing ID
        nominee.setId(1L);
        NomineeDTO nomineeDTO = nomineeMapper.toDto(nominee);

        int databaseSizeBeforeCreate = nomineeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNomineeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nomineeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Nominee in the database
        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomineeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = nomineeRepository.findAll().size();
        // set the field null
        nominee.setNomineeName(null);

        // Create the Nominee, which fails.
        NomineeDTO nomineeDTO = nomineeMapper.toDto(nominee);

        restNomineeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nomineeDTO)))
            .andExpect(status().isBadRequest());

        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPresentAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = nomineeRepository.findAll().size();
        // set the field null
        nominee.setPresentAddress(null);

        // Create the Nominee, which fails.
        NomineeDTO nomineeDTO = nomineeMapper.toDto(nominee);

        restNomineeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nomineeDTO)))
            .andExpect(status().isBadRequest());

        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSharePercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = nomineeRepository.findAll().size();
        // set the field null
        nominee.setSharePercentage(null);

        // Create the Nominee, which fails.
        NomineeDTO nomineeDTO = nomineeMapper.toDto(nominee);

        restNomineeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nomineeDTO)))
            .andExpect(status().isBadRequest());

        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = nomineeRepository.findAll().size();
        // set the field null
        nominee.setStatus(null);

        // Create the Nominee, which fails.
        NomineeDTO nomineeDTO = nomineeMapper.toDto(nominee);

        restNomineeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nomineeDTO)))
            .andExpect(status().isBadRequest());

        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPermanentAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = nomineeRepository.findAll().size();
        // set the field null
        nominee.setPermanentAddress(null);

        // Create the Nominee, which fails.
        NomineeDTO nomineeDTO = nomineeMapper.toDto(nominee);

        restNomineeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nomineeDTO)))
            .andExpect(status().isBadRequest());

        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNominees() throws Exception {
        // Initialize the database
        nomineeRepository.saveAndFlush(nominee);

        // Get all the nomineeList
        restNomineeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nominee.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomineeName").value(hasItem(DEFAULT_NOMINEE_NAME)))
            .andExpect(jsonPath("$.[*].presentAddress").value(hasItem(DEFAULT_PRESENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].relationshipWithEmployee").value(hasItem(DEFAULT_RELATIONSHIP_WITH_EMPLOYEE)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].sharePercentage").value(hasItem(DEFAULT_SHARE_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].imagePath").value(hasItem(DEFAULT_IMAGE_PATH)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].guardianName").value(hasItem(DEFAULT_GUARDIAN_NAME)))
            .andExpect(jsonPath("$.[*].guardianFatherName").value(hasItem(DEFAULT_GUARDIAN_FATHER_NAME)))
            .andExpect(jsonPath("$.[*].guardianSpouseName").value(hasItem(DEFAULT_GUARDIAN_SPOUSE_NAME)))
            .andExpect(jsonPath("$.[*].guardianDateOfBirth").value(hasItem(DEFAULT_GUARDIAN_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].guardianPresentAddress").value(hasItem(DEFAULT_GUARDIAN_PRESENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].guardianDocumentName").value(hasItem(DEFAULT_GUARDIAN_DOCUMENT_NAME)))
            .andExpect(jsonPath("$.[*].guardianRelationshipWith").value(hasItem(DEFAULT_GUARDIAN_RELATIONSHIP_WITH)))
            .andExpect(jsonPath("$.[*].guardianImagePath").value(hasItem(DEFAULT_GUARDIAN_IMAGE_PATH)))
            .andExpect(jsonPath("$.[*].isLocked").value(hasItem(DEFAULT_IS_LOCKED.booleanValue())))
            .andExpect(jsonPath("$.[*].nominationDate").value(hasItem(DEFAULT_NOMINATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].permanentAddress").value(hasItem(DEFAULT_PERMANENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].guardianPermanentAddress").value(hasItem(DEFAULT_GUARDIAN_PERMANENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].nomineeType").value(hasItem(DEFAULT_NOMINEE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].identityType").value(hasItem(DEFAULT_IDENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].documentName").value(hasItem(DEFAULT_DOCUMENT_NAME)))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].isNidVerified").value(hasItem(DEFAULT_IS_NID_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].guardianIdentityType").value(hasItem(DEFAULT_GUARDIAN_IDENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].guardianIdNumber").value(hasItem(DEFAULT_GUARDIAN_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].isGuardianNidVerified").value(hasItem(DEFAULT_IS_GUARDIAN_NID_VERIFIED.booleanValue())));
    }

    @Test
    @Transactional
    void getNominee() throws Exception {
        // Initialize the database
        nomineeRepository.saveAndFlush(nominee);

        // Get the nominee
        restNomineeMockMvc
            .perform(get(ENTITY_API_URL_ID, nominee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nominee.getId().intValue()))
            .andExpect(jsonPath("$.nomineeName").value(DEFAULT_NOMINEE_NAME))
            .andExpect(jsonPath("$.presentAddress").value(DEFAULT_PRESENT_ADDRESS))
            .andExpect(jsonPath("$.relationshipWithEmployee").value(DEFAULT_RELATIONSHIP_WITH_EMPLOYEE))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.sharePercentage").value(DEFAULT_SHARE_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.imagePath").value(DEFAULT_IMAGE_PATH))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.guardianName").value(DEFAULT_GUARDIAN_NAME))
            .andExpect(jsonPath("$.guardianFatherName").value(DEFAULT_GUARDIAN_FATHER_NAME))
            .andExpect(jsonPath("$.guardianSpouseName").value(DEFAULT_GUARDIAN_SPOUSE_NAME))
            .andExpect(jsonPath("$.guardianDateOfBirth").value(DEFAULT_GUARDIAN_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.guardianPresentAddress").value(DEFAULT_GUARDIAN_PRESENT_ADDRESS))
            .andExpect(jsonPath("$.guardianDocumentName").value(DEFAULT_GUARDIAN_DOCUMENT_NAME))
            .andExpect(jsonPath("$.guardianRelationshipWith").value(DEFAULT_GUARDIAN_RELATIONSHIP_WITH))
            .andExpect(jsonPath("$.guardianImagePath").value(DEFAULT_GUARDIAN_IMAGE_PATH))
            .andExpect(jsonPath("$.isLocked").value(DEFAULT_IS_LOCKED.booleanValue()))
            .andExpect(jsonPath("$.nominationDate").value(DEFAULT_NOMINATION_DATE.toString()))
            .andExpect(jsonPath("$.permanentAddress").value(DEFAULT_PERMANENT_ADDRESS))
            .andExpect(jsonPath("$.guardianPermanentAddress").value(DEFAULT_GUARDIAN_PERMANENT_ADDRESS))
            .andExpect(jsonPath("$.nomineeType").value(DEFAULT_NOMINEE_TYPE.toString()))
            .andExpect(jsonPath("$.identityType").value(DEFAULT_IDENTITY_TYPE.toString()))
            .andExpect(jsonPath("$.documentName").value(DEFAULT_DOCUMENT_NAME))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER))
            .andExpect(jsonPath("$.isNidVerified").value(DEFAULT_IS_NID_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.guardianIdentityType").value(DEFAULT_GUARDIAN_IDENTITY_TYPE.toString()))
            .andExpect(jsonPath("$.guardianIdNumber").value(DEFAULT_GUARDIAN_ID_NUMBER))
            .andExpect(jsonPath("$.isGuardianNidVerified").value(DEFAULT_IS_GUARDIAN_NID_VERIFIED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingNominee() throws Exception {
        // Get the nominee
        restNomineeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNominee() throws Exception {
        // Initialize the database
        nomineeRepository.saveAndFlush(nominee);

        int databaseSizeBeforeUpdate = nomineeRepository.findAll().size();

        // Update the nominee
        Nominee updatedNominee = nomineeRepository.findById(nominee.getId()).get();
        // Disconnect from session so that the updates on updatedNominee are not directly saved in db
        em.detach(updatedNominee);
        updatedNominee
            .nomineeName(UPDATED_NOMINEE_NAME)
            .presentAddress(UPDATED_PRESENT_ADDRESS)
            .relationshipWithEmployee(UPDATED_RELATIONSHIP_WITH_EMPLOYEE)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .age(UPDATED_AGE)
            .sharePercentage(UPDATED_SHARE_PERCENTAGE)
            .imagePath(UPDATED_IMAGE_PATH)
            .status(UPDATED_STATUS)
            .guardianName(UPDATED_GUARDIAN_NAME)
            .guardianFatherName(UPDATED_GUARDIAN_FATHER_NAME)
            .guardianSpouseName(UPDATED_GUARDIAN_SPOUSE_NAME)
            .guardianDateOfBirth(UPDATED_GUARDIAN_DATE_OF_BIRTH)
            .guardianPresentAddress(UPDATED_GUARDIAN_PRESENT_ADDRESS)
            .guardianDocumentName(UPDATED_GUARDIAN_DOCUMENT_NAME)
            .guardianRelationshipWith(UPDATED_GUARDIAN_RELATIONSHIP_WITH)
            .guardianImagePath(UPDATED_GUARDIAN_IMAGE_PATH)
            .isLocked(UPDATED_IS_LOCKED)
            .nominationDate(UPDATED_NOMINATION_DATE)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .guardianPermanentAddress(UPDATED_GUARDIAN_PERMANENT_ADDRESS)
            .nomineeType(UPDATED_NOMINEE_TYPE)
            .identityType(UPDATED_IDENTITY_TYPE)
            .documentName(UPDATED_DOCUMENT_NAME)
            .idNumber(UPDATED_ID_NUMBER)
            .isNidVerified(UPDATED_IS_NID_VERIFIED)
            .guardianIdentityType(UPDATED_GUARDIAN_IDENTITY_TYPE)
            .guardianIdNumber(UPDATED_GUARDIAN_ID_NUMBER)
            .isGuardianNidVerified(UPDATED_IS_GUARDIAN_NID_VERIFIED);
        NomineeDTO nomineeDTO = nomineeMapper.toDto(updatedNominee);

        restNomineeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nomineeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nomineeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Nominee in the database
        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeUpdate);
        Nominee testNominee = nomineeList.get(nomineeList.size() - 1);
        assertThat(testNominee.getNomineeName()).isEqualTo(UPDATED_NOMINEE_NAME);
        assertThat(testNominee.getPresentAddress()).isEqualTo(UPDATED_PRESENT_ADDRESS);
        assertThat(testNominee.getRelationshipWithEmployee()).isEqualTo(UPDATED_RELATIONSHIP_WITH_EMPLOYEE);
        assertThat(testNominee.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testNominee.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testNominee.getSharePercentage()).isEqualTo(UPDATED_SHARE_PERCENTAGE);
        assertThat(testNominee.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testNominee.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNominee.getGuardianName()).isEqualTo(UPDATED_GUARDIAN_NAME);
        assertThat(testNominee.getGuardianFatherName()).isEqualTo(UPDATED_GUARDIAN_FATHER_NAME);
        assertThat(testNominee.getGuardianSpouseName()).isEqualTo(UPDATED_GUARDIAN_SPOUSE_NAME);
        assertThat(testNominee.getGuardianDateOfBirth()).isEqualTo(UPDATED_GUARDIAN_DATE_OF_BIRTH);
        assertThat(testNominee.getGuardianPresentAddress()).isEqualTo(UPDATED_GUARDIAN_PRESENT_ADDRESS);
        assertThat(testNominee.getGuardianDocumentName()).isEqualTo(UPDATED_GUARDIAN_DOCUMENT_NAME);
        assertThat(testNominee.getGuardianRelationshipWith()).isEqualTo(UPDATED_GUARDIAN_RELATIONSHIP_WITH);
        assertThat(testNominee.getGuardianImagePath()).isEqualTo(UPDATED_GUARDIAN_IMAGE_PATH);
        assertThat(testNominee.getIsLocked()).isEqualTo(UPDATED_IS_LOCKED);
        assertThat(testNominee.getNominationDate()).isEqualTo(UPDATED_NOMINATION_DATE);
        assertThat(testNominee.getPermanentAddress()).isEqualTo(UPDATED_PERMANENT_ADDRESS);
        assertThat(testNominee.getGuardianPermanentAddress()).isEqualTo(UPDATED_GUARDIAN_PERMANENT_ADDRESS);
        assertThat(testNominee.getNomineeType()).isEqualTo(UPDATED_NOMINEE_TYPE);
        assertThat(testNominee.getIdentityType()).isEqualTo(UPDATED_IDENTITY_TYPE);
        assertThat(testNominee.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testNominee.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testNominee.getIsNidVerified()).isEqualTo(UPDATED_IS_NID_VERIFIED);
        assertThat(testNominee.getGuardianIdentityType()).isEqualTo(UPDATED_GUARDIAN_IDENTITY_TYPE);
        assertThat(testNominee.getGuardianIdNumber()).isEqualTo(UPDATED_GUARDIAN_ID_NUMBER);
        assertThat(testNominee.getIsGuardianNidVerified()).isEqualTo(UPDATED_IS_GUARDIAN_NID_VERIFIED);
    }

    @Test
    @Transactional
    void putNonExistingNominee() throws Exception {
        int databaseSizeBeforeUpdate = nomineeRepository.findAll().size();
        nominee.setId(count.incrementAndGet());

        // Create the Nominee
        NomineeDTO nomineeDTO = nomineeMapper.toDto(nominee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNomineeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nomineeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nomineeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nominee in the database
        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNominee() throws Exception {
        int databaseSizeBeforeUpdate = nomineeRepository.findAll().size();
        nominee.setId(count.incrementAndGet());

        // Create the Nominee
        NomineeDTO nomineeDTO = nomineeMapper.toDto(nominee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNomineeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nomineeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nominee in the database
        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNominee() throws Exception {
        int databaseSizeBeforeUpdate = nomineeRepository.findAll().size();
        nominee.setId(count.incrementAndGet());

        // Create the Nominee
        NomineeDTO nomineeDTO = nomineeMapper.toDto(nominee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNomineeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nomineeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nominee in the database
        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNomineeWithPatch() throws Exception {
        // Initialize the database
        nomineeRepository.saveAndFlush(nominee);

        int databaseSizeBeforeUpdate = nomineeRepository.findAll().size();

        // Update the nominee using partial update
        Nominee partialUpdatedNominee = new Nominee();
        partialUpdatedNominee.setId(nominee.getId());

        partialUpdatedNominee
            .nomineeName(UPDATED_NOMINEE_NAME)
            .presentAddress(UPDATED_PRESENT_ADDRESS)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .status(UPDATED_STATUS)
            .guardianName(UPDATED_GUARDIAN_NAME)
            .guardianFatherName(UPDATED_GUARDIAN_FATHER_NAME)
            .guardianSpouseName(UPDATED_GUARDIAN_SPOUSE_NAME)
            .guardianDateOfBirth(UPDATED_GUARDIAN_DATE_OF_BIRTH)
            .guardianRelationshipWith(UPDATED_GUARDIAN_RELATIONSHIP_WITH)
            .guardianImagePath(UPDATED_GUARDIAN_IMAGE_PATH)
            .isLocked(UPDATED_IS_LOCKED)
            .nominationDate(UPDATED_NOMINATION_DATE)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .guardianPermanentAddress(UPDATED_GUARDIAN_PERMANENT_ADDRESS)
            .nomineeType(UPDATED_NOMINEE_TYPE)
            .documentName(UPDATED_DOCUMENT_NAME)
            .isNidVerified(UPDATED_IS_NID_VERIFIED)
            .guardianIdentityType(UPDATED_GUARDIAN_IDENTITY_TYPE);

        restNomineeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNominee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNominee))
            )
            .andExpect(status().isOk());

        // Validate the Nominee in the database
        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeUpdate);
        Nominee testNominee = nomineeList.get(nomineeList.size() - 1);
        assertThat(testNominee.getNomineeName()).isEqualTo(UPDATED_NOMINEE_NAME);
        assertThat(testNominee.getPresentAddress()).isEqualTo(UPDATED_PRESENT_ADDRESS);
        assertThat(testNominee.getRelationshipWithEmployee()).isEqualTo(DEFAULT_RELATIONSHIP_WITH_EMPLOYEE);
        assertThat(testNominee.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testNominee.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testNominee.getSharePercentage()).isEqualTo(DEFAULT_SHARE_PERCENTAGE);
        assertThat(testNominee.getImagePath()).isEqualTo(DEFAULT_IMAGE_PATH);
        assertThat(testNominee.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNominee.getGuardianName()).isEqualTo(UPDATED_GUARDIAN_NAME);
        assertThat(testNominee.getGuardianFatherName()).isEqualTo(UPDATED_GUARDIAN_FATHER_NAME);
        assertThat(testNominee.getGuardianSpouseName()).isEqualTo(UPDATED_GUARDIAN_SPOUSE_NAME);
        assertThat(testNominee.getGuardianDateOfBirth()).isEqualTo(UPDATED_GUARDIAN_DATE_OF_BIRTH);
        assertThat(testNominee.getGuardianPresentAddress()).isEqualTo(DEFAULT_GUARDIAN_PRESENT_ADDRESS);
        assertThat(testNominee.getGuardianDocumentName()).isEqualTo(DEFAULT_GUARDIAN_DOCUMENT_NAME);
        assertThat(testNominee.getGuardianRelationshipWith()).isEqualTo(UPDATED_GUARDIAN_RELATIONSHIP_WITH);
        assertThat(testNominee.getGuardianImagePath()).isEqualTo(UPDATED_GUARDIAN_IMAGE_PATH);
        assertThat(testNominee.getIsLocked()).isEqualTo(UPDATED_IS_LOCKED);
        assertThat(testNominee.getNominationDate()).isEqualTo(UPDATED_NOMINATION_DATE);
        assertThat(testNominee.getPermanentAddress()).isEqualTo(UPDATED_PERMANENT_ADDRESS);
        assertThat(testNominee.getGuardianPermanentAddress()).isEqualTo(UPDATED_GUARDIAN_PERMANENT_ADDRESS);
        assertThat(testNominee.getNomineeType()).isEqualTo(UPDATED_NOMINEE_TYPE);
        assertThat(testNominee.getIdentityType()).isEqualTo(DEFAULT_IDENTITY_TYPE);
        assertThat(testNominee.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testNominee.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testNominee.getIsNidVerified()).isEqualTo(UPDATED_IS_NID_VERIFIED);
        assertThat(testNominee.getGuardianIdentityType()).isEqualTo(UPDATED_GUARDIAN_IDENTITY_TYPE);
        assertThat(testNominee.getGuardianIdNumber()).isEqualTo(DEFAULT_GUARDIAN_ID_NUMBER);
        assertThat(testNominee.getIsGuardianNidVerified()).isEqualTo(DEFAULT_IS_GUARDIAN_NID_VERIFIED);
    }

    @Test
    @Transactional
    void fullUpdateNomineeWithPatch() throws Exception {
        // Initialize the database
        nomineeRepository.saveAndFlush(nominee);

        int databaseSizeBeforeUpdate = nomineeRepository.findAll().size();

        // Update the nominee using partial update
        Nominee partialUpdatedNominee = new Nominee();
        partialUpdatedNominee.setId(nominee.getId());

        partialUpdatedNominee
            .nomineeName(UPDATED_NOMINEE_NAME)
            .presentAddress(UPDATED_PRESENT_ADDRESS)
            .relationshipWithEmployee(UPDATED_RELATIONSHIP_WITH_EMPLOYEE)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .age(UPDATED_AGE)
            .sharePercentage(UPDATED_SHARE_PERCENTAGE)
            .imagePath(UPDATED_IMAGE_PATH)
            .status(UPDATED_STATUS)
            .guardianName(UPDATED_GUARDIAN_NAME)
            .guardianFatherName(UPDATED_GUARDIAN_FATHER_NAME)
            .guardianSpouseName(UPDATED_GUARDIAN_SPOUSE_NAME)
            .guardianDateOfBirth(UPDATED_GUARDIAN_DATE_OF_BIRTH)
            .guardianPresentAddress(UPDATED_GUARDIAN_PRESENT_ADDRESS)
            .guardianDocumentName(UPDATED_GUARDIAN_DOCUMENT_NAME)
            .guardianRelationshipWith(UPDATED_GUARDIAN_RELATIONSHIP_WITH)
            .guardianImagePath(UPDATED_GUARDIAN_IMAGE_PATH)
            .isLocked(UPDATED_IS_LOCKED)
            .nominationDate(UPDATED_NOMINATION_DATE)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .guardianPermanentAddress(UPDATED_GUARDIAN_PERMANENT_ADDRESS)
            .nomineeType(UPDATED_NOMINEE_TYPE)
            .identityType(UPDATED_IDENTITY_TYPE)
            .documentName(UPDATED_DOCUMENT_NAME)
            .idNumber(UPDATED_ID_NUMBER)
            .isNidVerified(UPDATED_IS_NID_VERIFIED)
            .guardianIdentityType(UPDATED_GUARDIAN_IDENTITY_TYPE)
            .guardianIdNumber(UPDATED_GUARDIAN_ID_NUMBER)
            .isGuardianNidVerified(UPDATED_IS_GUARDIAN_NID_VERIFIED);

        restNomineeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNominee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNominee))
            )
            .andExpect(status().isOk());

        // Validate the Nominee in the database
        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeUpdate);
        Nominee testNominee = nomineeList.get(nomineeList.size() - 1);
        assertThat(testNominee.getNomineeName()).isEqualTo(UPDATED_NOMINEE_NAME);
        assertThat(testNominee.getPresentAddress()).isEqualTo(UPDATED_PRESENT_ADDRESS);
        assertThat(testNominee.getRelationshipWithEmployee()).isEqualTo(UPDATED_RELATIONSHIP_WITH_EMPLOYEE);
        assertThat(testNominee.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testNominee.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testNominee.getSharePercentage()).isEqualTo(UPDATED_SHARE_PERCENTAGE);
        assertThat(testNominee.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testNominee.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNominee.getGuardianName()).isEqualTo(UPDATED_GUARDIAN_NAME);
        assertThat(testNominee.getGuardianFatherName()).isEqualTo(UPDATED_GUARDIAN_FATHER_NAME);
        assertThat(testNominee.getGuardianSpouseName()).isEqualTo(UPDATED_GUARDIAN_SPOUSE_NAME);
        assertThat(testNominee.getGuardianDateOfBirth()).isEqualTo(UPDATED_GUARDIAN_DATE_OF_BIRTH);
        assertThat(testNominee.getGuardianPresentAddress()).isEqualTo(UPDATED_GUARDIAN_PRESENT_ADDRESS);
        assertThat(testNominee.getGuardianDocumentName()).isEqualTo(UPDATED_GUARDIAN_DOCUMENT_NAME);
        assertThat(testNominee.getGuardianRelationshipWith()).isEqualTo(UPDATED_GUARDIAN_RELATIONSHIP_WITH);
        assertThat(testNominee.getGuardianImagePath()).isEqualTo(UPDATED_GUARDIAN_IMAGE_PATH);
        assertThat(testNominee.getIsLocked()).isEqualTo(UPDATED_IS_LOCKED);
        assertThat(testNominee.getNominationDate()).isEqualTo(UPDATED_NOMINATION_DATE);
        assertThat(testNominee.getPermanentAddress()).isEqualTo(UPDATED_PERMANENT_ADDRESS);
        assertThat(testNominee.getGuardianPermanentAddress()).isEqualTo(UPDATED_GUARDIAN_PERMANENT_ADDRESS);
        assertThat(testNominee.getNomineeType()).isEqualTo(UPDATED_NOMINEE_TYPE);
        assertThat(testNominee.getIdentityType()).isEqualTo(UPDATED_IDENTITY_TYPE);
        assertThat(testNominee.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testNominee.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testNominee.getIsNidVerified()).isEqualTo(UPDATED_IS_NID_VERIFIED);
        assertThat(testNominee.getGuardianIdentityType()).isEqualTo(UPDATED_GUARDIAN_IDENTITY_TYPE);
        assertThat(testNominee.getGuardianIdNumber()).isEqualTo(UPDATED_GUARDIAN_ID_NUMBER);
        assertThat(testNominee.getIsGuardianNidVerified()).isEqualTo(UPDATED_IS_GUARDIAN_NID_VERIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingNominee() throws Exception {
        int databaseSizeBeforeUpdate = nomineeRepository.findAll().size();
        nominee.setId(count.incrementAndGet());

        // Create the Nominee
        NomineeDTO nomineeDTO = nomineeMapper.toDto(nominee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNomineeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nomineeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nomineeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nominee in the database
        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNominee() throws Exception {
        int databaseSizeBeforeUpdate = nomineeRepository.findAll().size();
        nominee.setId(count.incrementAndGet());

        // Create the Nominee
        NomineeDTO nomineeDTO = nomineeMapper.toDto(nominee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNomineeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nomineeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nominee in the database
        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNominee() throws Exception {
        int databaseSizeBeforeUpdate = nomineeRepository.findAll().size();
        nominee.setId(count.incrementAndGet());

        // Create the Nominee
        NomineeDTO nomineeDTO = nomineeMapper.toDto(nominee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNomineeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nomineeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nominee in the database
        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNominee() throws Exception {
        // Initialize the database
        nomineeRepository.saveAndFlush(nominee);

        int databaseSizeBeforeDelete = nomineeRepository.findAll().size();

        // Delete the nominee
        restNomineeMockMvc
            .perform(delete(ENTITY_API_URL_ID, nominee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Nominee> nomineeList = nomineeRepository.findAll();
        assertThat(nomineeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
