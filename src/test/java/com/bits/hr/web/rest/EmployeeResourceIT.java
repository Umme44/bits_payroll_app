package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.BloodGroup;
import com.bits.hr.domain.enumeration.CardType;
import com.bits.hr.domain.enumeration.CardType;
import com.bits.hr.domain.enumeration.CardType;
import com.bits.hr.domain.enumeration.DisbursementMethod;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.domain.enumeration.MaritalStatus;
import com.bits.hr.domain.enumeration.PayType;
import com.bits.hr.domain.enumeration.Religion;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.mapper.EmployeeMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResourceIT {

    private static final String DEFAULT_REFERENCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PIN = "AAAAAAAAAA";
    private static final String UPDATED_PIN = "BBBBBBBBBB";

    private static final String DEFAULT_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONAL_ID_NO = "AAAAAAAAAA";
    private static final String UPDATED_NATIONAL_ID_NO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PLACE_OF_BIRTH = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_OF_BIRTH = "BBBBBBBBBB";

    private static final String DEFAULT_FATHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FATHER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOTHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MOTHER_NAME = "BBBBBBBBBB";

    private static final BloodGroup DEFAULT_BLOOD_GROUP = BloodGroup.A_POSITIVE;
    private static final BloodGroup UPDATED_BLOOD_GROUP = BloodGroup.A_NEGATIVE;

    private static final String DEFAULT_PRESENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PRESENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PERMANENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PERMANENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PERSONAL_CONTACT_NO = "AAAAAAAAAA";
    private static final String UPDATED_PERSONAL_CONTACT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_PERSONAL_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_PERSONAL_EMAIL = "BBBBBBBBBB";

    private static final Religion DEFAULT_RELIGION = Religion.ISLAM;
    private static final Religion UPDATED_RELIGION = Religion.HINDU;

    private static final MaritalStatus DEFAULT_MARITAL_STATUS = MaritalStatus.SINGLE;
    private static final MaritalStatus UPDATED_MARITAL_STATUS = MaritalStatus.MARRIED;

    private static final LocalDate DEFAULT_DATE_OF_MARRIAGE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_MARRIAGE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SPOUSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SPOUSE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICIAL_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_OFFICIAL_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICIAL_CONTACT_NO = "AAAAAAAAAA";
    private static final String UPDATED_OFFICIAL_CONTACT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICE_PHONE_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_OFFICE_PHONE_EXTENSION = "BBBBBBBBBB";

    private static final String DEFAULT_WHATSAPP_ID = "AAAAAAAAAA";
    private static final String UPDATED_WHATSAPP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SKYPE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SKYPE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EMERGENCY_CONTACT_PERSON_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMERGENCY_CONTACT_PERSON_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE = "AAAAAAAAAA";
    private static final String UPDATED_EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE = "BBBBBBBBBB";

    private static final String DEFAULT_EMERGENCY_CONTACT_PERSON_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_EMERGENCY_CONTACT_PERSON_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final Double DEFAULT_MAIN_GROSS_SALARY = 1D;
    private static final Double UPDATED_MAIN_GROSS_SALARY = 2D;

    private static final EmployeeCategory DEFAULT_EMPLOYEE_CATEGORY = EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE;
    private static final EmployeeCategory UPDATED_EMPLOYEE_CATEGORY = EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_JOINING = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_JOINING = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_OF_CONFIRMATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_CONFIRMATION = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_PROBATIONARY_PERIOD_EXTENDED = false;
    private static final Boolean UPDATED_IS_PROBATIONARY_PERIOD_EXTENDED = true;

    private static final LocalDate DEFAULT_PROBATION_PERIOD_EXTENDED_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PROBATION_PERIOD_EXTENDED_TO = LocalDate.now(ZoneId.systemDefault());

    private static final PayType DEFAULT_PAY_TYPE = PayType.UNPAID;
    private static final PayType UPDATED_PAY_TYPE = PayType.MONTHLY;

    private static final DisbursementMethod DEFAULT_DISBURSEMENT_METHOD = DisbursementMethod.BANK;
    private static final DisbursementMethod UPDATED_DISBURSEMENT_METHOD = DisbursementMethod.CASH;

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ACCOUNT_NO = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ACCOUNT_NO = "BBBBBBBBBB";

    private static final Long DEFAULT_MOBILE_CELLING = 1L;
    private static final Long UPDATED_MOBILE_CELLING = 2L;

    private static final String DEFAULT_BKASH_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BKASH_NUMBER = "BBBBBBBBBB";

    private static final CardType DEFAULT_CARD_TYPE = CardType.DEBIT_CARD;
    private static final CardType UPDATED_CARD_TYPE = CardType.CREDIT_CARD;

    private static final String DEFAULT_CARD_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CARD_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TIN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TIN_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PASSPORT_NO = "AAAAAAAAAA";
    private static final String UPDATED_PASSPORT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_PASSPORT_PLACE_OF_ISSUE = "AAAAAAAAAA";
    private static final String UPDATED_PASSPORT_PLACE_OF_ISSUE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PASSPORT_ISSUED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PASSPORT_ISSUED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PASSPORT_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PASSPORT_EXPIRY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Double DEFAULT_WELFARE_FUND_DEDUCTION = 1D;
    private static final Double UPDATED_WELFARE_FUND_DEDUCTION = 2D;

    private static final EmploymentStatus DEFAULT_EMPLOYMENT_STATUS = EmploymentStatus.ACTIVE;
    private static final EmploymentStatus UPDATED_EMPLOYMENT_STATUS = EmploymentStatus.HOLD;

    private static final Boolean DEFAULT_HAS_DISABLED_CHILD = false;
    private static final Boolean UPDATED_HAS_DISABLED_CHILD = true;

    private static final Boolean DEFAULT_IS_FIRST_TIME_AIT_GIVER = false;
    private static final Boolean UPDATED_IS_FIRST_TIME_AIT_GIVER = true;

    private static final Boolean DEFAULT_IS_SALARY_HOLD = false;
    private static final Boolean UPDATED_IS_SALARY_HOLD = true;

    private static final Boolean DEFAULT_IS_FESTIVAL_BONUS_HOLD = false;
    private static final Boolean UPDATED_IS_FESTIVAL_BONUS_HOLD = true;

    private static final Boolean DEFAULT_IS_PHYSICALLY_DISABLED = false;
    private static final Boolean UPDATED_IS_PHYSICALLY_DISABLED = true;

    private static final Boolean DEFAULT_IS_FREEDOM_FIGHTER = false;
    private static final Boolean UPDATED_IS_FREEDOM_FIGHTER = true;

    private static final Boolean DEFAULT_HAS_OVER_TIME = false;
    private static final Boolean UPDATED_HAS_OVER_TIME = true;

    private static final LocalDate DEFAULT_PROBATION_PERIOD_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PROBATION_PERIOD_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CONTRACT_PERIOD_EXTENDED_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CONTRACT_PERIOD_EXTENDED_TO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CONTRACT_PERIOD_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CONTRACT_PERIOD_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final CardType DEFAULT_CARD_TYPE_02 = CardType.DEBIT_CARD;
    private static final CardType UPDATED_CARD_TYPE_02 = CardType.CREDIT_CARD;

    private static final String DEFAULT_CARD_NUMBER_02 = "AAAAAAAAAA";
    private static final String UPDATED_CARD_NUMBER_02 = "BBBBBBBBBB";

    private static final CardType DEFAULT_CARD_TYPE_03 = CardType.DEBIT_CARD;
    private static final CardType UPDATED_CARD_TYPE_03 = CardType.CREDIT_CARD;

    private static final String DEFAULT_CARD_NUMBER_03 = "AAAAAAAAAA";
    private static final String UPDATED_CARD_NUMBER_03 = "BBBBBBBBBB";

    private static final Double DEFAULT_ALLOWANCE_01 = 0D;
    private static final Double UPDATED_ALLOWANCE_01 = 1D;

    private static final LocalDate DEFAULT_ALLOWANCE_01_EFFECTIVE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ALLOWANCE_01_EFFECTIVE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ALLOWANCE_01_EFFECTIVE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ALLOWANCE_01_EFFECTIVE_TO = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_ALLOWANCE_02 = 0D;
    private static final Double UPDATED_ALLOWANCE_02 = 1D;

    private static final LocalDate DEFAULT_ALLOWANCE_02_EFFECTIVE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ALLOWANCE_02_EFFECTIVE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ALLOWANCE_02_EFFECTIVE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ALLOWANCE_02_EFFECTIVE_TO = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_ALLOWANCE_03 = 0D;
    private static final Double UPDATED_ALLOWANCE_03 = 1D;

    private static final LocalDate DEFAULT_ALLOWANCE_03_EFFECTIVE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ALLOWANCE_03_EFFECTIVE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ALLOWANCE_03_EFFECTIVE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ALLOWANCE_03_EFFECTIVE_TO = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_ALLOWANCE_04 = 0D;
    private static final Double UPDATED_ALLOWANCE_04 = 1D;

    private static final LocalDate DEFAULT_ALLOWANCE_04_EFFECTIVE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ALLOWANCE_04_EFFECTIVE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ALLOWANCE_04_EFFECTIVE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ALLOWANCE_04_EFFECTIVE_TO = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_ALLOWANCE_05 = 0D;
    private static final Double UPDATED_ALLOWANCE_05 = 1D;

    private static final LocalDate DEFAULT_ALLOWANCE_05_EFFECTIVE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ALLOWANCE_05_EFFECTIVE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ALLOWANCE_05_EFFECTIVE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ALLOWANCE_05_EFFECTIVE_TO = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_ALLOWANCE_06 = 0D;
    private static final Double UPDATED_ALLOWANCE_06 = 1D;

    private static final LocalDate DEFAULT_ALLOWANCE_06_EFFECTIVE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ALLOWANCE_06_EFFECTIVE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ALLOWANCE_06_EFFECTIVE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ALLOWANCE_06_EFFECTIVE_TO = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_TAX_PAID_BY_ORGANISATION = false;
    private static final Boolean UPDATED_IS_TAX_PAID_BY_ORGANISATION = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ALLOWED_TO_GIVE_ONLINE_ATTENDANCE = false;
    private static final Boolean UPDATED_IS_ALLOWED_TO_GIVE_ONLINE_ATTENDANCE = true;

    private static final Integer DEFAULT_NOTICE_PERIOD_IN_DAYS = 1;
    private static final Integer UPDATED_NOTICE_PERIOD_IN_DAYS = 2;

    private static final Boolean DEFAULT_IS_FIXED_TERM_CONTRACT = false;
    private static final Boolean UPDATED_IS_FIXED_TERM_CONTRACT = true;

    private static final Instant DEFAULT_CURRENT_IN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CURRENT_IN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CURRENT_OUT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CURRENT_OUT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ONLINE_ATTENDANCE_SANCTIONED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ONLINE_ATTENDANCE_SANCTIONED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_NID_VERIFIED = false;
    private static final Boolean UPDATED_IS_NID_VERIFIED = true;

    private static final Boolean DEFAULT_CAN_RAISE_RRF_ON_BEHALF = false;
    private static final Boolean UPDATED_CAN_RAISE_RRF_ON_BEHALF = true;

    private static final String DEFAULT_TAXES_CIRCLE = "AAAAAAAAAA";
    private static final String UPDATED_TAXES_CIRCLE = "BBBBBBBBBB";

    private static final String DEFAULT_TAXES_ZONE = "AAAAAAAAAA";
    private static final String UPDATED_TAXES_ZONE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CAN_MANAGE_TAX_ACKNOWLEDGEMENT_RECEIPT = false;
    private static final Boolean UPDATED_CAN_MANAGE_TAX_ACKNOWLEDGEMENT_RECEIPT = true;

    private static final Boolean DEFAULT_IS_ELIGIBLE_FOR_AUTOMATED_ATTENDANCE = false;
    private static final Boolean UPDATED_IS_ELIGIBLE_FOR_AUTOMATED_ATTENDANCE = true;

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Boolean DEFAULT_IS_BILLABLE_RESOURCE = false;
    private static final Boolean UPDATED_IS_BILLABLE_RESOURCE = true;

    private static final Boolean DEFAULT_IS_AUGMENTED_RESOURCE = false;
    private static final Boolean UPDATED_IS_AUGMENTED_RESOURCE = true;

    private static final LocalDate DEFAULT_LAST_WORKING_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_WORKING_DAY = LocalDate.now(ZoneId.systemDefault());

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeRepository employeeRepositoryMock;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Mock
    private EmployeeService employeeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .referenceId(DEFAULT_REFERENCE_ID)
            .pin(DEFAULT_PIN)
            .picture(DEFAULT_PICTURE)
            .fullName(DEFAULT_FULL_NAME)
            .surName(DEFAULT_SUR_NAME)
            .nationalIdNo(DEFAULT_NATIONAL_ID_NO)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .placeOfBirth(DEFAULT_PLACE_OF_BIRTH)
            .fatherName(DEFAULT_FATHER_NAME)
            .motherName(DEFAULT_MOTHER_NAME)
            .bloodGroup(DEFAULT_BLOOD_GROUP)
            .presentAddress(DEFAULT_PRESENT_ADDRESS)
            .permanentAddress(DEFAULT_PERMANENT_ADDRESS)
            .personalContactNo(DEFAULT_PERSONAL_CONTACT_NO)
            .personalEmail(DEFAULT_PERSONAL_EMAIL)
            .religion(DEFAULT_RELIGION)
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .dateOfMarriage(DEFAULT_DATE_OF_MARRIAGE)
            .spouseName(DEFAULT_SPOUSE_NAME)
            .officialEmail(DEFAULT_OFFICIAL_EMAIL)
            .officialContactNo(DEFAULT_OFFICIAL_CONTACT_NO)
            .officePhoneExtension(DEFAULT_OFFICE_PHONE_EXTENSION)
            .whatsappId(DEFAULT_WHATSAPP_ID)
            .skypeId(DEFAULT_SKYPE_ID)
            .emergencyContactPersonName(DEFAULT_EMERGENCY_CONTACT_PERSON_NAME)
            .emergencyContactPersonRelationshipWithEmployee(DEFAULT_EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE)
            .emergencyContactPersonContactNumber(DEFAULT_EMERGENCY_CONTACT_PERSON_CONTACT_NUMBER)
            .mainGrossSalary(DEFAULT_MAIN_GROSS_SALARY)
            .employeeCategory(DEFAULT_EMPLOYEE_CATEGORY)
            .location(DEFAULT_LOCATION)
            .dateOfJoining(DEFAULT_DATE_OF_JOINING)
            .dateOfConfirmation(DEFAULT_DATE_OF_CONFIRMATION)
            .isProbationaryPeriodExtended(DEFAULT_IS_PROBATIONARY_PERIOD_EXTENDED)
            .probationPeriodExtendedTo(DEFAULT_PROBATION_PERIOD_EXTENDED_TO)
            .payType(DEFAULT_PAY_TYPE)
            .disbursementMethod(DEFAULT_DISBURSEMENT_METHOD)
            .bankName(DEFAULT_BANK_NAME)
            .bankAccountNo(DEFAULT_BANK_ACCOUNT_NO)
            .mobileCelling(DEFAULT_MOBILE_CELLING)
            .bkashNumber(DEFAULT_BKASH_NUMBER)
            .cardType(DEFAULT_CARD_TYPE)
            .cardNumber(DEFAULT_CARD_NUMBER)
            .tinNumber(DEFAULT_TIN_NUMBER)
            .passportNo(DEFAULT_PASSPORT_NO)
            .passportPlaceOfIssue(DEFAULT_PASSPORT_PLACE_OF_ISSUE)
            .passportIssuedDate(DEFAULT_PASSPORT_ISSUED_DATE)
            .passportExpiryDate(DEFAULT_PASSPORT_EXPIRY_DATE)
            .gender(DEFAULT_GENDER)
            .welfareFundDeduction(DEFAULT_WELFARE_FUND_DEDUCTION)
            .employmentStatus(DEFAULT_EMPLOYMENT_STATUS)
            .hasDisabledChild(DEFAULT_HAS_DISABLED_CHILD)
            .isFirstTimeAitGiver(DEFAULT_IS_FIRST_TIME_AIT_GIVER)
            .isSalaryHold(DEFAULT_IS_SALARY_HOLD)
            .isFestivalBonusHold(DEFAULT_IS_FESTIVAL_BONUS_HOLD)
            .isPhysicallyDisabled(DEFAULT_IS_PHYSICALLY_DISABLED)
            .isFreedomFighter(DEFAULT_IS_FREEDOM_FIGHTER)
            .hasOverTime(DEFAULT_HAS_OVER_TIME)
            .probationPeriodEndDate(DEFAULT_PROBATION_PERIOD_END_DATE)
            .contractPeriodExtendedTo(DEFAULT_CONTRACT_PERIOD_EXTENDED_TO)
            .contractPeriodEndDate(DEFAULT_CONTRACT_PERIOD_END_DATE)
            .cardType02(DEFAULT_CARD_TYPE_02)
            .cardNumber02(DEFAULT_CARD_NUMBER_02)
            .cardType03(DEFAULT_CARD_TYPE_03)
            .cardNumber03(DEFAULT_CARD_NUMBER_03)
            .allowance01(DEFAULT_ALLOWANCE_01)
            .allowance01EffectiveFrom(DEFAULT_ALLOWANCE_01_EFFECTIVE_FROM)
            .allowance01EffectiveTo(DEFAULT_ALLOWANCE_01_EFFECTIVE_TO)
            .allowance02(DEFAULT_ALLOWANCE_02)
            .allowance02EffectiveFrom(DEFAULT_ALLOWANCE_02_EFFECTIVE_FROM)
            .allowance02EffectiveTo(DEFAULT_ALLOWANCE_02_EFFECTIVE_TO)
            .allowance03(DEFAULT_ALLOWANCE_03)
            .allowance03EffectiveFrom(DEFAULT_ALLOWANCE_03_EFFECTIVE_FROM)
            .allowance03EffectiveTo(DEFAULT_ALLOWANCE_03_EFFECTIVE_TO)
            .allowance04(DEFAULT_ALLOWANCE_04)
            .allowance04EffectiveFrom(DEFAULT_ALLOWANCE_04_EFFECTIVE_FROM)
            .allowance04EffectiveTo(DEFAULT_ALLOWANCE_04_EFFECTIVE_TO)
            .allowance05(DEFAULT_ALLOWANCE_05)
            .allowance05EffectiveFrom(DEFAULT_ALLOWANCE_05_EFFECTIVE_FROM)
            .allowance05EffectiveTo(DEFAULT_ALLOWANCE_05_EFFECTIVE_TO)
            .allowance06(DEFAULT_ALLOWANCE_06)
            .allowance06EffectiveFrom(DEFAULT_ALLOWANCE_06_EFFECTIVE_FROM)
            .allowance06EffectiveTo(DEFAULT_ALLOWANCE_06_EFFECTIVE_TO)
            .isTaxPaidByOrganisation(DEFAULT_IS_TAX_PAID_BY_ORGANISATION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT)
            .isAllowedToGiveOnlineAttendance(DEFAULT_IS_ALLOWED_TO_GIVE_ONLINE_ATTENDANCE)
            .noticePeriodInDays(DEFAULT_NOTICE_PERIOD_IN_DAYS)
            .isFixedTermContract(DEFAULT_IS_FIXED_TERM_CONTRACT)
            .currentInTime(DEFAULT_CURRENT_IN_TIME)
            .currentOutTime(DEFAULT_CURRENT_OUT_TIME)
            .onlineAttendanceSanctionedAt(DEFAULT_ONLINE_ATTENDANCE_SANCTIONED_AT)
            .isNidVerified(DEFAULT_IS_NID_VERIFIED)
            .canRaiseRrfOnBehalf(DEFAULT_CAN_RAISE_RRF_ON_BEHALF)
            .taxesCircle(DEFAULT_TAXES_CIRCLE)
            .taxesZone(DEFAULT_TAXES_ZONE)
            .canManageTaxAcknowledgementReceipt(DEFAULT_CAN_MANAGE_TAX_ACKNOWLEDGEMENT_RECEIPT)
            .isEligibleForAutomatedAttendance(DEFAULT_IS_ELIGIBLE_FOR_AUTOMATED_ATTENDANCE)
            .isBillableResource(DEFAULT_IS_BILLABLE_RESOURCE)
            .isAugmentedResource(DEFAULT_IS_AUGMENTED_RESOURCE)
            .lastWorkingDay(DEFAULT_LAST_WORKING_DAY);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .referenceId(UPDATED_REFERENCE_ID)
            .pin(UPDATED_PIN)
            .picture(UPDATED_PICTURE)
            .fullName(UPDATED_FULL_NAME)
            .surName(UPDATED_SUR_NAME)
            .nationalIdNo(UPDATED_NATIONAL_ID_NO)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH)
            .fatherName(UPDATED_FATHER_NAME)
            .motherName(UPDATED_MOTHER_NAME)
            .bloodGroup(UPDATED_BLOOD_GROUP)
            .presentAddress(UPDATED_PRESENT_ADDRESS)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .personalContactNo(UPDATED_PERSONAL_CONTACT_NO)
            .personalEmail(UPDATED_PERSONAL_EMAIL)
            .religion(UPDATED_RELIGION)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .dateOfMarriage(UPDATED_DATE_OF_MARRIAGE)
            .spouseName(UPDATED_SPOUSE_NAME)
            .officialEmail(UPDATED_OFFICIAL_EMAIL)
            .officialContactNo(UPDATED_OFFICIAL_CONTACT_NO)
            .officePhoneExtension(UPDATED_OFFICE_PHONE_EXTENSION)
            .whatsappId(UPDATED_WHATSAPP_ID)
            .skypeId(UPDATED_SKYPE_ID)
            .emergencyContactPersonName(UPDATED_EMERGENCY_CONTACT_PERSON_NAME)
            .emergencyContactPersonRelationshipWithEmployee(UPDATED_EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE)
            .emergencyContactPersonContactNumber(UPDATED_EMERGENCY_CONTACT_PERSON_CONTACT_NUMBER)
            .mainGrossSalary(UPDATED_MAIN_GROSS_SALARY)
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .location(UPDATED_LOCATION)
            .dateOfJoining(UPDATED_DATE_OF_JOINING)
            .dateOfConfirmation(UPDATED_DATE_OF_CONFIRMATION)
            .isProbationaryPeriodExtended(UPDATED_IS_PROBATIONARY_PERIOD_EXTENDED)
            .probationPeriodExtendedTo(UPDATED_PROBATION_PERIOD_EXTENDED_TO)
            .payType(UPDATED_PAY_TYPE)
            .disbursementMethod(UPDATED_DISBURSEMENT_METHOD)
            .bankName(UPDATED_BANK_NAME)
            .bankAccountNo(UPDATED_BANK_ACCOUNT_NO)
            .mobileCelling(UPDATED_MOBILE_CELLING)
            .bkashNumber(UPDATED_BKASH_NUMBER)
            .cardType(UPDATED_CARD_TYPE)
            .cardNumber(UPDATED_CARD_NUMBER)
            .tinNumber(UPDATED_TIN_NUMBER)
            .passportNo(UPDATED_PASSPORT_NO)
            .passportPlaceOfIssue(UPDATED_PASSPORT_PLACE_OF_ISSUE)
            .passportIssuedDate(UPDATED_PASSPORT_ISSUED_DATE)
            .passportExpiryDate(UPDATED_PASSPORT_EXPIRY_DATE)
            .gender(UPDATED_GENDER)
            .welfareFundDeduction(UPDATED_WELFARE_FUND_DEDUCTION)
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .hasDisabledChild(UPDATED_HAS_DISABLED_CHILD)
            .isFirstTimeAitGiver(UPDATED_IS_FIRST_TIME_AIT_GIVER)
            .isSalaryHold(UPDATED_IS_SALARY_HOLD)
            .isFestivalBonusHold(UPDATED_IS_FESTIVAL_BONUS_HOLD)
            .isPhysicallyDisabled(UPDATED_IS_PHYSICALLY_DISABLED)
            .isFreedomFighter(UPDATED_IS_FREEDOM_FIGHTER)
            .hasOverTime(UPDATED_HAS_OVER_TIME)
            .probationPeriodEndDate(UPDATED_PROBATION_PERIOD_END_DATE)
            .contractPeriodExtendedTo(UPDATED_CONTRACT_PERIOD_EXTENDED_TO)
            .contractPeriodEndDate(UPDATED_CONTRACT_PERIOD_END_DATE)
            .cardType02(UPDATED_CARD_TYPE_02)
            .cardNumber02(UPDATED_CARD_NUMBER_02)
            .cardType03(UPDATED_CARD_TYPE_03)
            .cardNumber03(UPDATED_CARD_NUMBER_03)
            .allowance01(UPDATED_ALLOWANCE_01)
            .allowance01EffectiveFrom(UPDATED_ALLOWANCE_01_EFFECTIVE_FROM)
            .allowance01EffectiveTo(UPDATED_ALLOWANCE_01_EFFECTIVE_TO)
            .allowance02(UPDATED_ALLOWANCE_02)
            .allowance02EffectiveFrom(UPDATED_ALLOWANCE_02_EFFECTIVE_FROM)
            .allowance02EffectiveTo(UPDATED_ALLOWANCE_02_EFFECTIVE_TO)
            .allowance03(UPDATED_ALLOWANCE_03)
            .allowance03EffectiveFrom(UPDATED_ALLOWANCE_03_EFFECTIVE_FROM)
            .allowance03EffectiveTo(UPDATED_ALLOWANCE_03_EFFECTIVE_TO)
            .allowance04(UPDATED_ALLOWANCE_04)
            .allowance04EffectiveFrom(UPDATED_ALLOWANCE_04_EFFECTIVE_FROM)
            .allowance04EffectiveTo(UPDATED_ALLOWANCE_04_EFFECTIVE_TO)
            .allowance05(UPDATED_ALLOWANCE_05)
            .allowance05EffectiveFrom(UPDATED_ALLOWANCE_05_EFFECTIVE_FROM)
            .allowance05EffectiveTo(UPDATED_ALLOWANCE_05_EFFECTIVE_TO)
            .allowance06(UPDATED_ALLOWANCE_06)
            .allowance06EffectiveFrom(UPDATED_ALLOWANCE_06_EFFECTIVE_FROM)
            .allowance06EffectiveTo(UPDATED_ALLOWANCE_06_EFFECTIVE_TO)
            .isTaxPaidByOrganisation(UPDATED_IS_TAX_PAID_BY_ORGANISATION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .isAllowedToGiveOnlineAttendance(UPDATED_IS_ALLOWED_TO_GIVE_ONLINE_ATTENDANCE)
            .noticePeriodInDays(UPDATED_NOTICE_PERIOD_IN_DAYS)
            .isFixedTermContract(UPDATED_IS_FIXED_TERM_CONTRACT)
            .currentInTime(UPDATED_CURRENT_IN_TIME)
            .currentOutTime(UPDATED_CURRENT_OUT_TIME)
            .onlineAttendanceSanctionedAt(UPDATED_ONLINE_ATTENDANCE_SANCTIONED_AT)
            .isNidVerified(UPDATED_IS_NID_VERIFIED)
            .canRaiseRrfOnBehalf(UPDATED_CAN_RAISE_RRF_ON_BEHALF)
            .taxesCircle(UPDATED_TAXES_CIRCLE)
            .taxesZone(UPDATED_TAXES_ZONE)
            .canManageTaxAcknowledgementReceipt(UPDATED_CAN_MANAGE_TAX_ACKNOWLEDGEMENT_RECEIPT)
            .isEligibleForAutomatedAttendance(UPDATED_IS_ELIGIBLE_FOR_AUTOMATED_ATTENDANCE)
            .isBillableResource(UPDATED_IS_BILLABLE_RESOURCE)
            .isAugmentedResource(UPDATED_IS_AUGMENTED_RESOURCE)
            .lastWorkingDay(UPDATED_LAST_WORKING_DAY);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();
        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getReferenceId()).isEqualTo(DEFAULT_REFERENCE_ID);
        assertThat(testEmployee.getPin()).isEqualTo(DEFAULT_PIN);
        assertThat(testEmployee.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testEmployee.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testEmployee.getSurName()).isEqualTo(DEFAULT_SUR_NAME);
        assertThat(testEmployee.getNationalIdNo()).isEqualTo(DEFAULT_NATIONAL_ID_NO);
        assertThat(testEmployee.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testEmployee.getPlaceOfBirth()).isEqualTo(DEFAULT_PLACE_OF_BIRTH);
        assertThat(testEmployee.getFatherName()).isEqualTo(DEFAULT_FATHER_NAME);
        assertThat(testEmployee.getMotherName()).isEqualTo(DEFAULT_MOTHER_NAME);
        assertThat(testEmployee.getBloodGroup()).isEqualTo(DEFAULT_BLOOD_GROUP);
        assertThat(testEmployee.getPresentAddress()).isEqualTo(DEFAULT_PRESENT_ADDRESS);
        assertThat(testEmployee.getPermanentAddress()).isEqualTo(DEFAULT_PERMANENT_ADDRESS);
        assertThat(testEmployee.getPersonalContactNo()).isEqualTo(DEFAULT_PERSONAL_CONTACT_NO);
        assertThat(testEmployee.getPersonalEmail()).isEqualTo(DEFAULT_PERSONAL_EMAIL);
        assertThat(testEmployee.getReligion()).isEqualTo(DEFAULT_RELIGION);
        assertThat(testEmployee.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testEmployee.getDateOfMarriage()).isEqualTo(DEFAULT_DATE_OF_MARRIAGE);
        assertThat(testEmployee.getSpouseName()).isEqualTo(DEFAULT_SPOUSE_NAME);
        assertThat(testEmployee.getOfficialEmail()).isEqualTo(DEFAULT_OFFICIAL_EMAIL);
        assertThat(testEmployee.getOfficialContactNo()).isEqualTo(DEFAULT_OFFICIAL_CONTACT_NO);
        assertThat(testEmployee.getOfficePhoneExtension()).isEqualTo(DEFAULT_OFFICE_PHONE_EXTENSION);
        assertThat(testEmployee.getWhatsappId()).isEqualTo(DEFAULT_WHATSAPP_ID);
        assertThat(testEmployee.getSkypeId()).isEqualTo(DEFAULT_SKYPE_ID);
        assertThat(testEmployee.getEmergencyContactPersonName()).isEqualTo(DEFAULT_EMERGENCY_CONTACT_PERSON_NAME);
        assertThat(testEmployee.getEmergencyContactPersonRelationshipWithEmployee())
            .isEqualTo(DEFAULT_EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE);
        assertThat(testEmployee.getEmergencyContactPersonContactNumber()).isEqualTo(DEFAULT_EMERGENCY_CONTACT_PERSON_CONTACT_NUMBER);
        assertThat(testEmployee.getMainGrossSalary()).isEqualTo(DEFAULT_MAIN_GROSS_SALARY);
        assertThat(testEmployee.getEmployeeCategory()).isEqualTo(DEFAULT_EMPLOYEE_CATEGORY);
        assertThat(testEmployee.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testEmployee.getDateOfJoining()).isEqualTo(DEFAULT_DATE_OF_JOINING);
        assertThat(testEmployee.getDateOfConfirmation()).isEqualTo(DEFAULT_DATE_OF_CONFIRMATION);
        assertThat(testEmployee.getIsProbationaryPeriodExtended()).isEqualTo(DEFAULT_IS_PROBATIONARY_PERIOD_EXTENDED);
        assertThat(testEmployee.getProbationPeriodExtendedTo()).isEqualTo(DEFAULT_PROBATION_PERIOD_EXTENDED_TO);
        assertThat(testEmployee.getPayType()).isEqualTo(DEFAULT_PAY_TYPE);
        assertThat(testEmployee.getDisbursementMethod()).isEqualTo(DEFAULT_DISBURSEMENT_METHOD);
        assertThat(testEmployee.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
        assertThat(testEmployee.getBankAccountNo()).isEqualTo(DEFAULT_BANK_ACCOUNT_NO);
        assertThat(testEmployee.getMobileCelling()).isEqualTo(DEFAULT_MOBILE_CELLING);
        assertThat(testEmployee.getBkashNumber()).isEqualTo(DEFAULT_BKASH_NUMBER);
        assertThat(testEmployee.getCardType()).isEqualTo(DEFAULT_CARD_TYPE);
        assertThat(testEmployee.getCardNumber()).isEqualTo(DEFAULT_CARD_NUMBER);
        assertThat(testEmployee.getTinNumber()).isEqualTo(DEFAULT_TIN_NUMBER);
        assertThat(testEmployee.getPassportNo()).isEqualTo(DEFAULT_PASSPORT_NO);
        assertThat(testEmployee.getPassportPlaceOfIssue()).isEqualTo(DEFAULT_PASSPORT_PLACE_OF_ISSUE);
        assertThat(testEmployee.getPassportIssuedDate()).isEqualTo(DEFAULT_PASSPORT_ISSUED_DATE);
        assertThat(testEmployee.getPassportExpiryDate()).isEqualTo(DEFAULT_PASSPORT_EXPIRY_DATE);
        assertThat(testEmployee.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testEmployee.getWelfareFundDeduction()).isEqualTo(DEFAULT_WELFARE_FUND_DEDUCTION);
        assertThat(testEmployee.getEmploymentStatus()).isEqualTo(DEFAULT_EMPLOYMENT_STATUS);
        assertThat(testEmployee.getHasDisabledChild()).isEqualTo(DEFAULT_HAS_DISABLED_CHILD);
        assertThat(testEmployee.getIsFirstTimeAitGiver()).isEqualTo(DEFAULT_IS_FIRST_TIME_AIT_GIVER);
        assertThat(testEmployee.getIsSalaryHold()).isEqualTo(DEFAULT_IS_SALARY_HOLD);
        assertThat(testEmployee.getIsFestivalBonusHold()).isEqualTo(DEFAULT_IS_FESTIVAL_BONUS_HOLD);
        assertThat(testEmployee.getIsPhysicallyDisabled()).isEqualTo(DEFAULT_IS_PHYSICALLY_DISABLED);
        assertThat(testEmployee.getIsFreedomFighter()).isEqualTo(DEFAULT_IS_FREEDOM_FIGHTER);
        assertThat(testEmployee.getHasOverTime()).isEqualTo(DEFAULT_HAS_OVER_TIME);
        assertThat(testEmployee.getProbationPeriodEndDate()).isEqualTo(DEFAULT_PROBATION_PERIOD_END_DATE);
        assertThat(testEmployee.getContractPeriodExtendedTo()).isEqualTo(DEFAULT_CONTRACT_PERIOD_EXTENDED_TO);
        assertThat(testEmployee.getContractPeriodEndDate()).isEqualTo(DEFAULT_CONTRACT_PERIOD_END_DATE);
        assertThat(testEmployee.getCardType02()).isEqualTo(DEFAULT_CARD_TYPE_02);
        assertThat(testEmployee.getCardNumber02()).isEqualTo(DEFAULT_CARD_NUMBER_02);
        assertThat(testEmployee.getCardType03()).isEqualTo(DEFAULT_CARD_TYPE_03);
        assertThat(testEmployee.getCardNumber03()).isEqualTo(DEFAULT_CARD_NUMBER_03);
        assertThat(testEmployee.getAllowance01()).isEqualTo(DEFAULT_ALLOWANCE_01);
        assertThat(testEmployee.getAllowance01EffectiveFrom()).isEqualTo(DEFAULT_ALLOWANCE_01_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance01EffectiveTo()).isEqualTo(DEFAULT_ALLOWANCE_01_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance02()).isEqualTo(DEFAULT_ALLOWANCE_02);
        assertThat(testEmployee.getAllowance02EffectiveFrom()).isEqualTo(DEFAULT_ALLOWANCE_02_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance02EffectiveTo()).isEqualTo(DEFAULT_ALLOWANCE_02_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance03()).isEqualTo(DEFAULT_ALLOWANCE_03);
        assertThat(testEmployee.getAllowance03EffectiveFrom()).isEqualTo(DEFAULT_ALLOWANCE_03_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance03EffectiveTo()).isEqualTo(DEFAULT_ALLOWANCE_03_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance04()).isEqualTo(DEFAULT_ALLOWANCE_04);
        assertThat(testEmployee.getAllowance04EffectiveFrom()).isEqualTo(DEFAULT_ALLOWANCE_04_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance04EffectiveTo()).isEqualTo(DEFAULT_ALLOWANCE_04_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance05()).isEqualTo(DEFAULT_ALLOWANCE_05);
        assertThat(testEmployee.getAllowance05EffectiveFrom()).isEqualTo(DEFAULT_ALLOWANCE_05_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance05EffectiveTo()).isEqualTo(DEFAULT_ALLOWANCE_05_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance06()).isEqualTo(DEFAULT_ALLOWANCE_06);
        assertThat(testEmployee.getAllowance06EffectiveFrom()).isEqualTo(DEFAULT_ALLOWANCE_06_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance06EffectiveTo()).isEqualTo(DEFAULT_ALLOWANCE_06_EFFECTIVE_TO);
        assertThat(testEmployee.getIsTaxPaidByOrganisation()).isEqualTo(DEFAULT_IS_TAX_PAID_BY_ORGANISATION);
        assertThat(testEmployee.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testEmployee.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEmployee.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testEmployee.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testEmployee.getIsAllowedToGiveOnlineAttendance()).isEqualTo(DEFAULT_IS_ALLOWED_TO_GIVE_ONLINE_ATTENDANCE);
        assertThat(testEmployee.getNoticePeriodInDays()).isEqualTo(DEFAULT_NOTICE_PERIOD_IN_DAYS);
        assertThat(testEmployee.getIsFixedTermContract()).isEqualTo(DEFAULT_IS_FIXED_TERM_CONTRACT);
        assertThat(testEmployee.getCurrentInTime()).isEqualTo(DEFAULT_CURRENT_IN_TIME);
        assertThat(testEmployee.getCurrentOutTime()).isEqualTo(DEFAULT_CURRENT_OUT_TIME);
        assertThat(testEmployee.getOnlineAttendanceSanctionedAt()).isEqualTo(DEFAULT_ONLINE_ATTENDANCE_SANCTIONED_AT);
        assertThat(testEmployee.getIsNidVerified()).isEqualTo(DEFAULT_IS_NID_VERIFIED);
        assertThat(testEmployee.getCanRaiseRrfOnBehalf()).isEqualTo(DEFAULT_CAN_RAISE_RRF_ON_BEHALF);
        assertThat(testEmployee.getTaxesCircle()).isEqualTo(DEFAULT_TAXES_CIRCLE);
        assertThat(testEmployee.getTaxesZone()).isEqualTo(DEFAULT_TAXES_ZONE);
        assertThat(testEmployee.getCanManageTaxAcknowledgementReceipt()).isEqualTo(DEFAULT_CAN_MANAGE_TAX_ACKNOWLEDGEMENT_RECEIPT);
        assertThat(testEmployee.getIsEligibleForAutomatedAttendance()).isEqualTo(DEFAULT_IS_ELIGIBLE_FOR_AUTOMATED_ATTENDANCE);
        assertThat(testEmployee.getIsBillableResource()).isEqualTo(DEFAULT_IS_BILLABLE_RESOURCE);
        assertThat(testEmployee.getIsAugmentedResource()).isEqualTo(DEFAULT_IS_AUGMENTED_RESOURCE);
        assertThat(testEmployee.getLastWorkingDay()).isEqualTo(DEFAULT_LAST_WORKING_DAY);
    }

    @Test
    @Transactional
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPinIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setPin(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].referenceId").value(hasItem(DEFAULT_REFERENCE_ID)))
            .andExpect(jsonPath("$.[*].pin").value(hasItem(DEFAULT_PIN)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].surName").value(hasItem(DEFAULT_SUR_NAME)))
            .andExpect(jsonPath("$.[*].nationalIdNo").value(hasItem(DEFAULT_NATIONAL_ID_NO)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].placeOfBirth").value(hasItem(DEFAULT_PLACE_OF_BIRTH)))
            .andExpect(jsonPath("$.[*].fatherName").value(hasItem(DEFAULT_FATHER_NAME)))
            .andExpect(jsonPath("$.[*].motherName").value(hasItem(DEFAULT_MOTHER_NAME)))
            .andExpect(jsonPath("$.[*].bloodGroup").value(hasItem(DEFAULT_BLOOD_GROUP.toString())))
            .andExpect(jsonPath("$.[*].presentAddress").value(hasItem(DEFAULT_PRESENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].permanentAddress").value(hasItem(DEFAULT_PERMANENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].personalContactNo").value(hasItem(DEFAULT_PERSONAL_CONTACT_NO)))
            .andExpect(jsonPath("$.[*].personalEmail").value(hasItem(DEFAULT_PERSONAL_EMAIL)))
            .andExpect(jsonPath("$.[*].religion").value(hasItem(DEFAULT_RELIGION.toString())))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateOfMarriage").value(hasItem(DEFAULT_DATE_OF_MARRIAGE.toString())))
            .andExpect(jsonPath("$.[*].spouseName").value(hasItem(DEFAULT_SPOUSE_NAME)))
            .andExpect(jsonPath("$.[*].officialEmail").value(hasItem(DEFAULT_OFFICIAL_EMAIL)))
            .andExpect(jsonPath("$.[*].officialContactNo").value(hasItem(DEFAULT_OFFICIAL_CONTACT_NO)))
            .andExpect(jsonPath("$.[*].officePhoneExtension").value(hasItem(DEFAULT_OFFICE_PHONE_EXTENSION)))
            .andExpect(jsonPath("$.[*].whatsappId").value(hasItem(DEFAULT_WHATSAPP_ID)))
            .andExpect(jsonPath("$.[*].skypeId").value(hasItem(DEFAULT_SKYPE_ID)))
            .andExpect(jsonPath("$.[*].emergencyContactPersonName").value(hasItem(DEFAULT_EMERGENCY_CONTACT_PERSON_NAME)))
            .andExpect(
                jsonPath("$.[*].emergencyContactPersonRelationshipWithEmployee")
                    .value(hasItem(DEFAULT_EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE))
            )
            .andExpect(
                jsonPath("$.[*].emergencyContactPersonContactNumber").value(hasItem(DEFAULT_EMERGENCY_CONTACT_PERSON_CONTACT_NUMBER))
            )
            .andExpect(jsonPath("$.[*].mainGrossSalary").value(hasItem(DEFAULT_MAIN_GROSS_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].employeeCategory").value(hasItem(DEFAULT_EMPLOYEE_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].dateOfJoining").value(hasItem(DEFAULT_DATE_OF_JOINING.toString())))
            .andExpect(jsonPath("$.[*].dateOfConfirmation").value(hasItem(DEFAULT_DATE_OF_CONFIRMATION.toString())))
            .andExpect(
                jsonPath("$.[*].isProbationaryPeriodExtended").value(hasItem(DEFAULT_IS_PROBATIONARY_PERIOD_EXTENDED.booleanValue()))
            )
            .andExpect(jsonPath("$.[*].probationPeriodExtendedTo").value(hasItem(DEFAULT_PROBATION_PERIOD_EXTENDED_TO.toString())))
            .andExpect(jsonPath("$.[*].payType").value(hasItem(DEFAULT_PAY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].disbursementMethod").value(hasItem(DEFAULT_DISBURSEMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].bankAccountNo").value(hasItem(DEFAULT_BANK_ACCOUNT_NO)))
            .andExpect(jsonPath("$.[*].mobileCelling").value(hasItem(DEFAULT_MOBILE_CELLING.intValue())))
            .andExpect(jsonPath("$.[*].bkashNumber").value(hasItem(DEFAULT_BKASH_NUMBER)))
            .andExpect(jsonPath("$.[*].cardType").value(hasItem(DEFAULT_CARD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER)))
            .andExpect(jsonPath("$.[*].tinNumber").value(hasItem(DEFAULT_TIN_NUMBER)))
            .andExpect(jsonPath("$.[*].passportNo").value(hasItem(DEFAULT_PASSPORT_NO)))
            .andExpect(jsonPath("$.[*].passportPlaceOfIssue").value(hasItem(DEFAULT_PASSPORT_PLACE_OF_ISSUE)))
            .andExpect(jsonPath("$.[*].passportIssuedDate").value(hasItem(DEFAULT_PASSPORT_ISSUED_DATE.toString())))
            .andExpect(jsonPath("$.[*].passportExpiryDate").value(hasItem(DEFAULT_PASSPORT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].welfareFundDeduction").value(hasItem(DEFAULT_WELFARE_FUND_DEDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].employmentStatus").value(hasItem(DEFAULT_EMPLOYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].hasDisabledChild").value(hasItem(DEFAULT_HAS_DISABLED_CHILD.booleanValue())))
            .andExpect(jsonPath("$.[*].isFirstTimeAitGiver").value(hasItem(DEFAULT_IS_FIRST_TIME_AIT_GIVER.booleanValue())))
            .andExpect(jsonPath("$.[*].isSalaryHold").value(hasItem(DEFAULT_IS_SALARY_HOLD.booleanValue())))
            .andExpect(jsonPath("$.[*].isFestivalBonusHold").value(hasItem(DEFAULT_IS_FESTIVAL_BONUS_HOLD.booleanValue())))
            .andExpect(jsonPath("$.[*].isPhysicallyDisabled").value(hasItem(DEFAULT_IS_PHYSICALLY_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].isFreedomFighter").value(hasItem(DEFAULT_IS_FREEDOM_FIGHTER.booleanValue())))
            .andExpect(jsonPath("$.[*].hasOverTime").value(hasItem(DEFAULT_HAS_OVER_TIME.booleanValue())))
            .andExpect(jsonPath("$.[*].probationPeriodEndDate").value(hasItem(DEFAULT_PROBATION_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].contractPeriodExtendedTo").value(hasItem(DEFAULT_CONTRACT_PERIOD_EXTENDED_TO.toString())))
            .andExpect(jsonPath("$.[*].contractPeriodEndDate").value(hasItem(DEFAULT_CONTRACT_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].cardType02").value(hasItem(DEFAULT_CARD_TYPE_02.toString())))
            .andExpect(jsonPath("$.[*].cardNumber02").value(hasItem(DEFAULT_CARD_NUMBER_02)))
            .andExpect(jsonPath("$.[*].cardType03").value(hasItem(DEFAULT_CARD_TYPE_03.toString())))
            .andExpect(jsonPath("$.[*].cardNumber03").value(hasItem(DEFAULT_CARD_NUMBER_03)))
            .andExpect(jsonPath("$.[*].allowance01").value(hasItem(DEFAULT_ALLOWANCE_01.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance01EffectiveFrom").value(hasItem(DEFAULT_ALLOWANCE_01_EFFECTIVE_FROM.toString())))
            .andExpect(jsonPath("$.[*].allowance01EffectiveTo").value(hasItem(DEFAULT_ALLOWANCE_01_EFFECTIVE_TO.toString())))
            .andExpect(jsonPath("$.[*].allowance02").value(hasItem(DEFAULT_ALLOWANCE_02.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance02EffectiveFrom").value(hasItem(DEFAULT_ALLOWANCE_02_EFFECTIVE_FROM.toString())))
            .andExpect(jsonPath("$.[*].allowance02EffectiveTo").value(hasItem(DEFAULT_ALLOWANCE_02_EFFECTIVE_TO.toString())))
            .andExpect(jsonPath("$.[*].allowance03").value(hasItem(DEFAULT_ALLOWANCE_03.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance03EffectiveFrom").value(hasItem(DEFAULT_ALLOWANCE_03_EFFECTIVE_FROM.toString())))
            .andExpect(jsonPath("$.[*].allowance03EffectiveTo").value(hasItem(DEFAULT_ALLOWANCE_03_EFFECTIVE_TO.toString())))
            .andExpect(jsonPath("$.[*].allowance04").value(hasItem(DEFAULT_ALLOWANCE_04.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance04EffectiveFrom").value(hasItem(DEFAULT_ALLOWANCE_04_EFFECTIVE_FROM.toString())))
            .andExpect(jsonPath("$.[*].allowance04EffectiveTo").value(hasItem(DEFAULT_ALLOWANCE_04_EFFECTIVE_TO.toString())))
            .andExpect(jsonPath("$.[*].allowance05").value(hasItem(DEFAULT_ALLOWANCE_05.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance05EffectiveFrom").value(hasItem(DEFAULT_ALLOWANCE_05_EFFECTIVE_FROM.toString())))
            .andExpect(jsonPath("$.[*].allowance05EffectiveTo").value(hasItem(DEFAULT_ALLOWANCE_05_EFFECTIVE_TO.toString())))
            .andExpect(jsonPath("$.[*].allowance06").value(hasItem(DEFAULT_ALLOWANCE_06.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance06EffectiveFrom").value(hasItem(DEFAULT_ALLOWANCE_06_EFFECTIVE_FROM.toString())))
            .andExpect(jsonPath("$.[*].allowance06EffectiveTo").value(hasItem(DEFAULT_ALLOWANCE_06_EFFECTIVE_TO.toString())))
            .andExpect(jsonPath("$.[*].isTaxPaidByOrganisation").value(hasItem(DEFAULT_IS_TAX_PAID_BY_ORGANISATION.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(
                jsonPath("$.[*].isAllowedToGiveOnlineAttendance")
                    .value(hasItem(DEFAULT_IS_ALLOWED_TO_GIVE_ONLINE_ATTENDANCE.booleanValue()))
            )
            .andExpect(jsonPath("$.[*].noticePeriodInDays").value(hasItem(DEFAULT_NOTICE_PERIOD_IN_DAYS)))
            .andExpect(jsonPath("$.[*].isFixedTermContract").value(hasItem(DEFAULT_IS_FIXED_TERM_CONTRACT.booleanValue())))
            .andExpect(jsonPath("$.[*].currentInTime").value(hasItem(DEFAULT_CURRENT_IN_TIME.toString())))
            .andExpect(jsonPath("$.[*].currentOutTime").value(hasItem(DEFAULT_CURRENT_OUT_TIME.toString())))
            .andExpect(jsonPath("$.[*].onlineAttendanceSanctionedAt").value(hasItem(DEFAULT_ONLINE_ATTENDANCE_SANCTIONED_AT.toString())))
            .andExpect(jsonPath("$.[*].isNidVerified").value(hasItem(DEFAULT_IS_NID_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].canRaiseRrfOnBehalf").value(hasItem(DEFAULT_CAN_RAISE_RRF_ON_BEHALF.booleanValue())))
            .andExpect(jsonPath("$.[*].taxesCircle").value(hasItem(DEFAULT_TAXES_CIRCLE)))
            .andExpect(jsonPath("$.[*].taxesZone").value(hasItem(DEFAULT_TAXES_ZONE)))
            .andExpect(
                jsonPath("$.[*].canManageTaxAcknowledgementReceipt")
                    .value(hasItem(DEFAULT_CAN_MANAGE_TAX_ACKNOWLEDGEMENT_RECEIPT.booleanValue()))
            )
            .andExpect(
                jsonPath("$.[*].isEligibleForAutomatedAttendance")
                    .value(hasItem(DEFAULT_IS_ELIGIBLE_FOR_AUTOMATED_ATTENDANCE.booleanValue()))
            )
            .andExpect(jsonPath("$.[*].isBillableResource").value(hasItem(DEFAULT_IS_BILLABLE_RESOURCE.booleanValue())))
            .andExpect(jsonPath("$.[*].isAugmentedResource").value(hasItem(DEFAULT_IS_AUGMENTED_RESOURCE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastWorkingDay").value(hasItem(DEFAULT_LAST_WORKING_DAY.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeesWithEagerRelationshipsIsEnabled() throws Exception {
        when(employeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(employeeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.referenceId").value(DEFAULT_REFERENCE_ID))
            .andExpect(jsonPath("$.pin").value(DEFAULT_PIN))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.surName").value(DEFAULT_SUR_NAME))
            .andExpect(jsonPath("$.nationalIdNo").value(DEFAULT_NATIONAL_ID_NO))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.placeOfBirth").value(DEFAULT_PLACE_OF_BIRTH))
            .andExpect(jsonPath("$.fatherName").value(DEFAULT_FATHER_NAME))
            .andExpect(jsonPath("$.motherName").value(DEFAULT_MOTHER_NAME))
            .andExpect(jsonPath("$.bloodGroup").value(DEFAULT_BLOOD_GROUP.toString()))
            .andExpect(jsonPath("$.presentAddress").value(DEFAULT_PRESENT_ADDRESS))
            .andExpect(jsonPath("$.permanentAddress").value(DEFAULT_PERMANENT_ADDRESS))
            .andExpect(jsonPath("$.personalContactNo").value(DEFAULT_PERSONAL_CONTACT_NO))
            .andExpect(jsonPath("$.personalEmail").value(DEFAULT_PERSONAL_EMAIL))
            .andExpect(jsonPath("$.religion").value(DEFAULT_RELIGION.toString()))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
            .andExpect(jsonPath("$.dateOfMarriage").value(DEFAULT_DATE_OF_MARRIAGE.toString()))
            .andExpect(jsonPath("$.spouseName").value(DEFAULT_SPOUSE_NAME))
            .andExpect(jsonPath("$.officialEmail").value(DEFAULT_OFFICIAL_EMAIL))
            .andExpect(jsonPath("$.officialContactNo").value(DEFAULT_OFFICIAL_CONTACT_NO))
            .andExpect(jsonPath("$.officePhoneExtension").value(DEFAULT_OFFICE_PHONE_EXTENSION))
            .andExpect(jsonPath("$.whatsappId").value(DEFAULT_WHATSAPP_ID))
            .andExpect(jsonPath("$.skypeId").value(DEFAULT_SKYPE_ID))
            .andExpect(jsonPath("$.emergencyContactPersonName").value(DEFAULT_EMERGENCY_CONTACT_PERSON_NAME))
            .andExpect(
                jsonPath("$.emergencyContactPersonRelationshipWithEmployee")
                    .value(DEFAULT_EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE)
            )
            .andExpect(jsonPath("$.emergencyContactPersonContactNumber").value(DEFAULT_EMERGENCY_CONTACT_PERSON_CONTACT_NUMBER))
            .andExpect(jsonPath("$.mainGrossSalary").value(DEFAULT_MAIN_GROSS_SALARY.doubleValue()))
            .andExpect(jsonPath("$.employeeCategory").value(DEFAULT_EMPLOYEE_CATEGORY.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.dateOfJoining").value(DEFAULT_DATE_OF_JOINING.toString()))
            .andExpect(jsonPath("$.dateOfConfirmation").value(DEFAULT_DATE_OF_CONFIRMATION.toString()))
            .andExpect(jsonPath("$.isProbationaryPeriodExtended").value(DEFAULT_IS_PROBATIONARY_PERIOD_EXTENDED.booleanValue()))
            .andExpect(jsonPath("$.probationPeriodExtendedTo").value(DEFAULT_PROBATION_PERIOD_EXTENDED_TO.toString()))
            .andExpect(jsonPath("$.payType").value(DEFAULT_PAY_TYPE.toString()))
            .andExpect(jsonPath("$.disbursementMethod").value(DEFAULT_DISBURSEMENT_METHOD.toString()))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME))
            .andExpect(jsonPath("$.bankAccountNo").value(DEFAULT_BANK_ACCOUNT_NO))
            .andExpect(jsonPath("$.mobileCelling").value(DEFAULT_MOBILE_CELLING.intValue()))
            .andExpect(jsonPath("$.bkashNumber").value(DEFAULT_BKASH_NUMBER))
            .andExpect(jsonPath("$.cardType").value(DEFAULT_CARD_TYPE.toString()))
            .andExpect(jsonPath("$.cardNumber").value(DEFAULT_CARD_NUMBER))
            .andExpect(jsonPath("$.tinNumber").value(DEFAULT_TIN_NUMBER))
            .andExpect(jsonPath("$.passportNo").value(DEFAULT_PASSPORT_NO))
            .andExpect(jsonPath("$.passportPlaceOfIssue").value(DEFAULT_PASSPORT_PLACE_OF_ISSUE))
            .andExpect(jsonPath("$.passportIssuedDate").value(DEFAULT_PASSPORT_ISSUED_DATE.toString()))
            .andExpect(jsonPath("$.passportExpiryDate").value(DEFAULT_PASSPORT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.welfareFundDeduction").value(DEFAULT_WELFARE_FUND_DEDUCTION.doubleValue()))
            .andExpect(jsonPath("$.employmentStatus").value(DEFAULT_EMPLOYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.hasDisabledChild").value(DEFAULT_HAS_DISABLED_CHILD.booleanValue()))
            .andExpect(jsonPath("$.isFirstTimeAitGiver").value(DEFAULT_IS_FIRST_TIME_AIT_GIVER.booleanValue()))
            .andExpect(jsonPath("$.isSalaryHold").value(DEFAULT_IS_SALARY_HOLD.booleanValue()))
            .andExpect(jsonPath("$.isFestivalBonusHold").value(DEFAULT_IS_FESTIVAL_BONUS_HOLD.booleanValue()))
            .andExpect(jsonPath("$.isPhysicallyDisabled").value(DEFAULT_IS_PHYSICALLY_DISABLED.booleanValue()))
            .andExpect(jsonPath("$.isFreedomFighter").value(DEFAULT_IS_FREEDOM_FIGHTER.booleanValue()))
            .andExpect(jsonPath("$.hasOverTime").value(DEFAULT_HAS_OVER_TIME.booleanValue()))
            .andExpect(jsonPath("$.probationPeriodEndDate").value(DEFAULT_PROBATION_PERIOD_END_DATE.toString()))
            .andExpect(jsonPath("$.contractPeriodExtendedTo").value(DEFAULT_CONTRACT_PERIOD_EXTENDED_TO.toString()))
            .andExpect(jsonPath("$.contractPeriodEndDate").value(DEFAULT_CONTRACT_PERIOD_END_DATE.toString()))
            .andExpect(jsonPath("$.cardType02").value(DEFAULT_CARD_TYPE_02.toString()))
            .andExpect(jsonPath("$.cardNumber02").value(DEFAULT_CARD_NUMBER_02))
            .andExpect(jsonPath("$.cardType03").value(DEFAULT_CARD_TYPE_03.toString()))
            .andExpect(jsonPath("$.cardNumber03").value(DEFAULT_CARD_NUMBER_03))
            .andExpect(jsonPath("$.allowance01").value(DEFAULT_ALLOWANCE_01.doubleValue()))
            .andExpect(jsonPath("$.allowance01EffectiveFrom").value(DEFAULT_ALLOWANCE_01_EFFECTIVE_FROM.toString()))
            .andExpect(jsonPath("$.allowance01EffectiveTo").value(DEFAULT_ALLOWANCE_01_EFFECTIVE_TO.toString()))
            .andExpect(jsonPath("$.allowance02").value(DEFAULT_ALLOWANCE_02.doubleValue()))
            .andExpect(jsonPath("$.allowance02EffectiveFrom").value(DEFAULT_ALLOWANCE_02_EFFECTIVE_FROM.toString()))
            .andExpect(jsonPath("$.allowance02EffectiveTo").value(DEFAULT_ALLOWANCE_02_EFFECTIVE_TO.toString()))
            .andExpect(jsonPath("$.allowance03").value(DEFAULT_ALLOWANCE_03.doubleValue()))
            .andExpect(jsonPath("$.allowance03EffectiveFrom").value(DEFAULT_ALLOWANCE_03_EFFECTIVE_FROM.toString()))
            .andExpect(jsonPath("$.allowance03EffectiveTo").value(DEFAULT_ALLOWANCE_03_EFFECTIVE_TO.toString()))
            .andExpect(jsonPath("$.allowance04").value(DEFAULT_ALLOWANCE_04.doubleValue()))
            .andExpect(jsonPath("$.allowance04EffectiveFrom").value(DEFAULT_ALLOWANCE_04_EFFECTIVE_FROM.toString()))
            .andExpect(jsonPath("$.allowance04EffectiveTo").value(DEFAULT_ALLOWANCE_04_EFFECTIVE_TO.toString()))
            .andExpect(jsonPath("$.allowance05").value(DEFAULT_ALLOWANCE_05.doubleValue()))
            .andExpect(jsonPath("$.allowance05EffectiveFrom").value(DEFAULT_ALLOWANCE_05_EFFECTIVE_FROM.toString()))
            .andExpect(jsonPath("$.allowance05EffectiveTo").value(DEFAULT_ALLOWANCE_05_EFFECTIVE_TO.toString()))
            .andExpect(jsonPath("$.allowance06").value(DEFAULT_ALLOWANCE_06.doubleValue()))
            .andExpect(jsonPath("$.allowance06EffectiveFrom").value(DEFAULT_ALLOWANCE_06_EFFECTIVE_FROM.toString()))
            .andExpect(jsonPath("$.allowance06EffectiveTo").value(DEFAULT_ALLOWANCE_06_EFFECTIVE_TO.toString()))
            .andExpect(jsonPath("$.isTaxPaidByOrganisation").value(DEFAULT_IS_TAX_PAID_BY_ORGANISATION.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.isAllowedToGiveOnlineAttendance").value(DEFAULT_IS_ALLOWED_TO_GIVE_ONLINE_ATTENDANCE.booleanValue()))
            .andExpect(jsonPath("$.noticePeriodInDays").value(DEFAULT_NOTICE_PERIOD_IN_DAYS))
            .andExpect(jsonPath("$.isFixedTermContract").value(DEFAULT_IS_FIXED_TERM_CONTRACT.booleanValue()))
            .andExpect(jsonPath("$.currentInTime").value(DEFAULT_CURRENT_IN_TIME.toString()))
            .andExpect(jsonPath("$.currentOutTime").value(DEFAULT_CURRENT_OUT_TIME.toString()))
            .andExpect(jsonPath("$.onlineAttendanceSanctionedAt").value(DEFAULT_ONLINE_ATTENDANCE_SANCTIONED_AT.toString()))
            .andExpect(jsonPath("$.isNidVerified").value(DEFAULT_IS_NID_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.canRaiseRrfOnBehalf").value(DEFAULT_CAN_RAISE_RRF_ON_BEHALF.booleanValue()))
            .andExpect(jsonPath("$.taxesCircle").value(DEFAULT_TAXES_CIRCLE))
            .andExpect(jsonPath("$.taxesZone").value(DEFAULT_TAXES_ZONE))
            .andExpect(
                jsonPath("$.canManageTaxAcknowledgementReceipt").value(DEFAULT_CAN_MANAGE_TAX_ACKNOWLEDGEMENT_RECEIPT.booleanValue())
            )
            .andExpect(jsonPath("$.isEligibleForAutomatedAttendance").value(DEFAULT_IS_ELIGIBLE_FOR_AUTOMATED_ATTENDANCE.booleanValue()))
            .andExpect(jsonPath("$.isBillableResource").value(DEFAULT_IS_BILLABLE_RESOURCE.booleanValue()))
            .andExpect(jsonPath("$.isAugmentedResource").value(DEFAULT_IS_AUGMENTED_RESOURCE.booleanValue()))
            .andExpect(jsonPath("$.lastWorkingDay").value(DEFAULT_LAST_WORKING_DAY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .referenceId(UPDATED_REFERENCE_ID)
            .pin(UPDATED_PIN)
            .picture(UPDATED_PICTURE)
            .fullName(UPDATED_FULL_NAME)
            .surName(UPDATED_SUR_NAME)
            .nationalIdNo(UPDATED_NATIONAL_ID_NO)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH)
            .fatherName(UPDATED_FATHER_NAME)
            .motherName(UPDATED_MOTHER_NAME)
            .bloodGroup(UPDATED_BLOOD_GROUP)
            .presentAddress(UPDATED_PRESENT_ADDRESS)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .personalContactNo(UPDATED_PERSONAL_CONTACT_NO)
            .personalEmail(UPDATED_PERSONAL_EMAIL)
            .religion(UPDATED_RELIGION)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .dateOfMarriage(UPDATED_DATE_OF_MARRIAGE)
            .spouseName(UPDATED_SPOUSE_NAME)
            .officialEmail(UPDATED_OFFICIAL_EMAIL)
            .officialContactNo(UPDATED_OFFICIAL_CONTACT_NO)
            .officePhoneExtension(UPDATED_OFFICE_PHONE_EXTENSION)
            .whatsappId(UPDATED_WHATSAPP_ID)
            .skypeId(UPDATED_SKYPE_ID)
            .emergencyContactPersonName(UPDATED_EMERGENCY_CONTACT_PERSON_NAME)
            .emergencyContactPersonRelationshipWithEmployee(UPDATED_EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE)
            .emergencyContactPersonContactNumber(UPDATED_EMERGENCY_CONTACT_PERSON_CONTACT_NUMBER)
            .mainGrossSalary(UPDATED_MAIN_GROSS_SALARY)
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .location(UPDATED_LOCATION)
            .dateOfJoining(UPDATED_DATE_OF_JOINING)
            .dateOfConfirmation(UPDATED_DATE_OF_CONFIRMATION)
            .isProbationaryPeriodExtended(UPDATED_IS_PROBATIONARY_PERIOD_EXTENDED)
            .probationPeriodExtendedTo(UPDATED_PROBATION_PERIOD_EXTENDED_TO)
            .payType(UPDATED_PAY_TYPE)
            .disbursementMethod(UPDATED_DISBURSEMENT_METHOD)
            .bankName(UPDATED_BANK_NAME)
            .bankAccountNo(UPDATED_BANK_ACCOUNT_NO)
            .mobileCelling(UPDATED_MOBILE_CELLING)
            .bkashNumber(UPDATED_BKASH_NUMBER)
            .cardType(UPDATED_CARD_TYPE)
            .cardNumber(UPDATED_CARD_NUMBER)
            .tinNumber(UPDATED_TIN_NUMBER)
            .passportNo(UPDATED_PASSPORT_NO)
            .passportPlaceOfIssue(UPDATED_PASSPORT_PLACE_OF_ISSUE)
            .passportIssuedDate(UPDATED_PASSPORT_ISSUED_DATE)
            .passportExpiryDate(UPDATED_PASSPORT_EXPIRY_DATE)
            .gender(UPDATED_GENDER)
            .welfareFundDeduction(UPDATED_WELFARE_FUND_DEDUCTION)
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .hasDisabledChild(UPDATED_HAS_DISABLED_CHILD)
            .isFirstTimeAitGiver(UPDATED_IS_FIRST_TIME_AIT_GIVER)
            .isSalaryHold(UPDATED_IS_SALARY_HOLD)
            .isFestivalBonusHold(UPDATED_IS_FESTIVAL_BONUS_HOLD)
            .isPhysicallyDisabled(UPDATED_IS_PHYSICALLY_DISABLED)
            .isFreedomFighter(UPDATED_IS_FREEDOM_FIGHTER)
            .hasOverTime(UPDATED_HAS_OVER_TIME)
            .probationPeriodEndDate(UPDATED_PROBATION_PERIOD_END_DATE)
            .contractPeriodExtendedTo(UPDATED_CONTRACT_PERIOD_EXTENDED_TO)
            .contractPeriodEndDate(UPDATED_CONTRACT_PERIOD_END_DATE)
            .cardType02(UPDATED_CARD_TYPE_02)
            .cardNumber02(UPDATED_CARD_NUMBER_02)
            .cardType03(UPDATED_CARD_TYPE_03)
            .cardNumber03(UPDATED_CARD_NUMBER_03)
            .allowance01(UPDATED_ALLOWANCE_01)
            .allowance01EffectiveFrom(UPDATED_ALLOWANCE_01_EFFECTIVE_FROM)
            .allowance01EffectiveTo(UPDATED_ALLOWANCE_01_EFFECTIVE_TO)
            .allowance02(UPDATED_ALLOWANCE_02)
            .allowance02EffectiveFrom(UPDATED_ALLOWANCE_02_EFFECTIVE_FROM)
            .allowance02EffectiveTo(UPDATED_ALLOWANCE_02_EFFECTIVE_TO)
            .allowance03(UPDATED_ALLOWANCE_03)
            .allowance03EffectiveFrom(UPDATED_ALLOWANCE_03_EFFECTIVE_FROM)
            .allowance03EffectiveTo(UPDATED_ALLOWANCE_03_EFFECTIVE_TO)
            .allowance04(UPDATED_ALLOWANCE_04)
            .allowance04EffectiveFrom(UPDATED_ALLOWANCE_04_EFFECTIVE_FROM)
            .allowance04EffectiveTo(UPDATED_ALLOWANCE_04_EFFECTIVE_TO)
            .allowance05(UPDATED_ALLOWANCE_05)
            .allowance05EffectiveFrom(UPDATED_ALLOWANCE_05_EFFECTIVE_FROM)
            .allowance05EffectiveTo(UPDATED_ALLOWANCE_05_EFFECTIVE_TO)
            .allowance06(UPDATED_ALLOWANCE_06)
            .allowance06EffectiveFrom(UPDATED_ALLOWANCE_06_EFFECTIVE_FROM)
            .allowance06EffectiveTo(UPDATED_ALLOWANCE_06_EFFECTIVE_TO)
            .isTaxPaidByOrganisation(UPDATED_IS_TAX_PAID_BY_ORGANISATION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .isAllowedToGiveOnlineAttendance(UPDATED_IS_ALLOWED_TO_GIVE_ONLINE_ATTENDANCE)
            .noticePeriodInDays(UPDATED_NOTICE_PERIOD_IN_DAYS)
            .isFixedTermContract(UPDATED_IS_FIXED_TERM_CONTRACT)
            .currentInTime(UPDATED_CURRENT_IN_TIME)
            .currentOutTime(UPDATED_CURRENT_OUT_TIME)
            .onlineAttendanceSanctionedAt(UPDATED_ONLINE_ATTENDANCE_SANCTIONED_AT)
            .isNidVerified(UPDATED_IS_NID_VERIFIED)
            .canRaiseRrfOnBehalf(UPDATED_CAN_RAISE_RRF_ON_BEHALF)
            .taxesCircle(UPDATED_TAXES_CIRCLE)
            .taxesZone(UPDATED_TAXES_ZONE)
            .canManageTaxAcknowledgementReceipt(UPDATED_CAN_MANAGE_TAX_ACKNOWLEDGEMENT_RECEIPT)
            .isEligibleForAutomatedAttendance(UPDATED_IS_ELIGIBLE_FOR_AUTOMATED_ATTENDANCE)
            .isBillableResource(UPDATED_IS_BILLABLE_RESOURCE)
            .isAugmentedResource(UPDATED_IS_AUGMENTED_RESOURCE)
            .lastWorkingDay(UPDATED_LAST_WORKING_DAY);
        EmployeeDTO employeeDTO = employeeMapper.toDto(updatedEmployee);

        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getReferenceId()).isEqualTo(UPDATED_REFERENCE_ID);
        assertThat(testEmployee.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testEmployee.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testEmployee.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testEmployee.getSurName()).isEqualTo(UPDATED_SUR_NAME);
        assertThat(testEmployee.getNationalIdNo()).isEqualTo(UPDATED_NATIONAL_ID_NO);
        assertThat(testEmployee.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testEmployee.getPlaceOfBirth()).isEqualTo(UPDATED_PLACE_OF_BIRTH);
        assertThat(testEmployee.getFatherName()).isEqualTo(UPDATED_FATHER_NAME);
        assertThat(testEmployee.getMotherName()).isEqualTo(UPDATED_MOTHER_NAME);
        assertThat(testEmployee.getBloodGroup()).isEqualTo(UPDATED_BLOOD_GROUP);
        assertThat(testEmployee.getPresentAddress()).isEqualTo(UPDATED_PRESENT_ADDRESS);
        assertThat(testEmployee.getPermanentAddress()).isEqualTo(UPDATED_PERMANENT_ADDRESS);
        assertThat(testEmployee.getPersonalContactNo()).isEqualTo(UPDATED_PERSONAL_CONTACT_NO);
        assertThat(testEmployee.getPersonalEmail()).isEqualTo(UPDATED_PERSONAL_EMAIL);
        assertThat(testEmployee.getReligion()).isEqualTo(UPDATED_RELIGION);
        assertThat(testEmployee.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testEmployee.getDateOfMarriage()).isEqualTo(UPDATED_DATE_OF_MARRIAGE);
        assertThat(testEmployee.getSpouseName()).isEqualTo(UPDATED_SPOUSE_NAME);
        assertThat(testEmployee.getOfficialEmail()).isEqualTo(UPDATED_OFFICIAL_EMAIL);
        assertThat(testEmployee.getOfficialContactNo()).isEqualTo(UPDATED_OFFICIAL_CONTACT_NO);
        assertThat(testEmployee.getOfficePhoneExtension()).isEqualTo(UPDATED_OFFICE_PHONE_EXTENSION);
        assertThat(testEmployee.getWhatsappId()).isEqualTo(UPDATED_WHATSAPP_ID);
        assertThat(testEmployee.getSkypeId()).isEqualTo(UPDATED_SKYPE_ID);
        assertThat(testEmployee.getEmergencyContactPersonName()).isEqualTo(UPDATED_EMERGENCY_CONTACT_PERSON_NAME);
        assertThat(testEmployee.getEmergencyContactPersonRelationshipWithEmployee())
            .isEqualTo(UPDATED_EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE);
        assertThat(testEmployee.getEmergencyContactPersonContactNumber()).isEqualTo(UPDATED_EMERGENCY_CONTACT_PERSON_CONTACT_NUMBER);
        assertThat(testEmployee.getMainGrossSalary()).isEqualTo(UPDATED_MAIN_GROSS_SALARY);
        assertThat(testEmployee.getEmployeeCategory()).isEqualTo(UPDATED_EMPLOYEE_CATEGORY);
        assertThat(testEmployee.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testEmployee.getDateOfJoining()).isEqualTo(UPDATED_DATE_OF_JOINING);
        assertThat(testEmployee.getDateOfConfirmation()).isEqualTo(UPDATED_DATE_OF_CONFIRMATION);
        assertThat(testEmployee.getIsProbationaryPeriodExtended()).isEqualTo(UPDATED_IS_PROBATIONARY_PERIOD_EXTENDED);
        assertThat(testEmployee.getProbationPeriodExtendedTo()).isEqualTo(UPDATED_PROBATION_PERIOD_EXTENDED_TO);
        assertThat(testEmployee.getPayType()).isEqualTo(UPDATED_PAY_TYPE);
        assertThat(testEmployee.getDisbursementMethod()).isEqualTo(UPDATED_DISBURSEMENT_METHOD);
        assertThat(testEmployee.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testEmployee.getBankAccountNo()).isEqualTo(UPDATED_BANK_ACCOUNT_NO);
        assertThat(testEmployee.getMobileCelling()).isEqualTo(UPDATED_MOBILE_CELLING);
        assertThat(testEmployee.getBkashNumber()).isEqualTo(UPDATED_BKASH_NUMBER);
        assertThat(testEmployee.getCardType()).isEqualTo(UPDATED_CARD_TYPE);
        assertThat(testEmployee.getCardNumber()).isEqualTo(UPDATED_CARD_NUMBER);
        assertThat(testEmployee.getTinNumber()).isEqualTo(UPDATED_TIN_NUMBER);
        assertThat(testEmployee.getPassportNo()).isEqualTo(UPDATED_PASSPORT_NO);
        assertThat(testEmployee.getPassportPlaceOfIssue()).isEqualTo(UPDATED_PASSPORT_PLACE_OF_ISSUE);
        assertThat(testEmployee.getPassportIssuedDate()).isEqualTo(UPDATED_PASSPORT_ISSUED_DATE);
        assertThat(testEmployee.getPassportExpiryDate()).isEqualTo(UPDATED_PASSPORT_EXPIRY_DATE);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getWelfareFundDeduction()).isEqualTo(UPDATED_WELFARE_FUND_DEDUCTION);
        assertThat(testEmployee.getEmploymentStatus()).isEqualTo(UPDATED_EMPLOYMENT_STATUS);
        assertThat(testEmployee.getHasDisabledChild()).isEqualTo(UPDATED_HAS_DISABLED_CHILD);
        assertThat(testEmployee.getIsFirstTimeAitGiver()).isEqualTo(UPDATED_IS_FIRST_TIME_AIT_GIVER);
        assertThat(testEmployee.getIsSalaryHold()).isEqualTo(UPDATED_IS_SALARY_HOLD);
        assertThat(testEmployee.getIsFestivalBonusHold()).isEqualTo(UPDATED_IS_FESTIVAL_BONUS_HOLD);
        assertThat(testEmployee.getIsPhysicallyDisabled()).isEqualTo(UPDATED_IS_PHYSICALLY_DISABLED);
        assertThat(testEmployee.getIsFreedomFighter()).isEqualTo(UPDATED_IS_FREEDOM_FIGHTER);
        assertThat(testEmployee.getHasOverTime()).isEqualTo(UPDATED_HAS_OVER_TIME);
        assertThat(testEmployee.getProbationPeriodEndDate()).isEqualTo(UPDATED_PROBATION_PERIOD_END_DATE);
        assertThat(testEmployee.getContractPeriodExtendedTo()).isEqualTo(UPDATED_CONTRACT_PERIOD_EXTENDED_TO);
        assertThat(testEmployee.getContractPeriodEndDate()).isEqualTo(UPDATED_CONTRACT_PERIOD_END_DATE);
        assertThat(testEmployee.getCardType02()).isEqualTo(UPDATED_CARD_TYPE_02);
        assertThat(testEmployee.getCardNumber02()).isEqualTo(UPDATED_CARD_NUMBER_02);
        assertThat(testEmployee.getCardType03()).isEqualTo(UPDATED_CARD_TYPE_03);
        assertThat(testEmployee.getCardNumber03()).isEqualTo(UPDATED_CARD_NUMBER_03);
        assertThat(testEmployee.getAllowance01()).isEqualTo(UPDATED_ALLOWANCE_01);
        assertThat(testEmployee.getAllowance01EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_01_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance01EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_01_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance02()).isEqualTo(UPDATED_ALLOWANCE_02);
        assertThat(testEmployee.getAllowance02EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_02_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance02EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_02_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance03()).isEqualTo(UPDATED_ALLOWANCE_03);
        assertThat(testEmployee.getAllowance03EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_03_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance03EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_03_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance04()).isEqualTo(UPDATED_ALLOWANCE_04);
        assertThat(testEmployee.getAllowance04EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_04_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance04EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_04_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance05()).isEqualTo(UPDATED_ALLOWANCE_05);
        assertThat(testEmployee.getAllowance05EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_05_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance05EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_05_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance06()).isEqualTo(UPDATED_ALLOWANCE_06);
        assertThat(testEmployee.getAllowance06EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_06_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance06EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_06_EFFECTIVE_TO);
        assertThat(testEmployee.getIsTaxPaidByOrganisation()).isEqualTo(UPDATED_IS_TAX_PAID_BY_ORGANISATION);
        assertThat(testEmployee.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmployee.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployee.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testEmployee.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmployee.getIsAllowedToGiveOnlineAttendance()).isEqualTo(UPDATED_IS_ALLOWED_TO_GIVE_ONLINE_ATTENDANCE);
        assertThat(testEmployee.getNoticePeriodInDays()).isEqualTo(UPDATED_NOTICE_PERIOD_IN_DAYS);
        assertThat(testEmployee.getIsFixedTermContract()).isEqualTo(UPDATED_IS_FIXED_TERM_CONTRACT);
        assertThat(testEmployee.getCurrentInTime()).isEqualTo(UPDATED_CURRENT_IN_TIME);
        assertThat(testEmployee.getCurrentOutTime()).isEqualTo(UPDATED_CURRENT_OUT_TIME);
        assertThat(testEmployee.getOnlineAttendanceSanctionedAt()).isEqualTo(UPDATED_ONLINE_ATTENDANCE_SANCTIONED_AT);
        assertThat(testEmployee.getIsNidVerified()).isEqualTo(UPDATED_IS_NID_VERIFIED);
        assertThat(testEmployee.getCanRaiseRrfOnBehalf()).isEqualTo(UPDATED_CAN_RAISE_RRF_ON_BEHALF);
        assertThat(testEmployee.getTaxesCircle()).isEqualTo(UPDATED_TAXES_CIRCLE);
        assertThat(testEmployee.getTaxesZone()).isEqualTo(UPDATED_TAXES_ZONE);
        assertThat(testEmployee.getCanManageTaxAcknowledgementReceipt()).isEqualTo(UPDATED_CAN_MANAGE_TAX_ACKNOWLEDGEMENT_RECEIPT);
        assertThat(testEmployee.getIsEligibleForAutomatedAttendance()).isEqualTo(UPDATED_IS_ELIGIBLE_FOR_AUTOMATED_ATTENDANCE);
        assertThat(testEmployee.getIsBillableResource()).isEqualTo(UPDATED_IS_BILLABLE_RESOURCE);
        assertThat(testEmployee.getIsAugmentedResource()).isEqualTo(UPDATED_IS_AUGMENTED_RESOURCE);
        assertThat(testEmployee.getLastWorkingDay()).isEqualTo(UPDATED_LAST_WORKING_DAY);
    }

    @Test
    @Transactional
    void putNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .pin(UPDATED_PIN)
            .picture(UPDATED_PICTURE)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH)
            .fatherName(UPDATED_FATHER_NAME)
            .motherName(UPDATED_MOTHER_NAME)
            .personalContactNo(UPDATED_PERSONAL_CONTACT_NO)
            .emergencyContactPersonRelationshipWithEmployee(UPDATED_EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE)
            .location(UPDATED_LOCATION)
            .dateOfJoining(UPDATED_DATE_OF_JOINING)
            .dateOfConfirmation(UPDATED_DATE_OF_CONFIRMATION)
            .isProbationaryPeriodExtended(UPDATED_IS_PROBATIONARY_PERIOD_EXTENDED)
            .probationPeriodExtendedTo(UPDATED_PROBATION_PERIOD_EXTENDED_TO)
            .payType(UPDATED_PAY_TYPE)
            .bkashNumber(UPDATED_BKASH_NUMBER)
            .cardNumber(UPDATED_CARD_NUMBER)
            .passportNo(UPDATED_PASSPORT_NO)
            .passportPlaceOfIssue(UPDATED_PASSPORT_PLACE_OF_ISSUE)
            .gender(UPDATED_GENDER)
            .isSalaryHold(UPDATED_IS_SALARY_HOLD)
            .probationPeriodEndDate(UPDATED_PROBATION_PERIOD_END_DATE)
            .cardNumber02(UPDATED_CARD_NUMBER_02)
            .cardType03(UPDATED_CARD_TYPE_03)
            .cardNumber03(UPDATED_CARD_NUMBER_03)
            .allowance01(UPDATED_ALLOWANCE_01)
            .allowance01EffectiveFrom(UPDATED_ALLOWANCE_01_EFFECTIVE_FROM)
            .allowance01EffectiveTo(UPDATED_ALLOWANCE_01_EFFECTIVE_TO)
            .allowance02(UPDATED_ALLOWANCE_02)
            .allowance03EffectiveFrom(UPDATED_ALLOWANCE_03_EFFECTIVE_FROM)
            .allowance04EffectiveFrom(UPDATED_ALLOWANCE_04_EFFECTIVE_FROM)
            .allowance05EffectiveFrom(UPDATED_ALLOWANCE_05_EFFECTIVE_FROM)
            .allowance05EffectiveTo(UPDATED_ALLOWANCE_05_EFFECTIVE_TO)
            .allowance06(UPDATED_ALLOWANCE_06)
            .allowance06EffectiveTo(UPDATED_ALLOWANCE_06_EFFECTIVE_TO)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY)
            .isAllowedToGiveOnlineAttendance(UPDATED_IS_ALLOWED_TO_GIVE_ONLINE_ATTENDANCE)
            .currentInTime(UPDATED_CURRENT_IN_TIME)
            .onlineAttendanceSanctionedAt(UPDATED_ONLINE_ATTENDANCE_SANCTIONED_AT)
            .taxesZone(UPDATED_TAXES_ZONE)
            .isBillableResource(UPDATED_IS_BILLABLE_RESOURCE)
            .isAugmentedResource(UPDATED_IS_AUGMENTED_RESOURCE);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getReferenceId()).isEqualTo(DEFAULT_REFERENCE_ID);
        assertThat(testEmployee.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testEmployee.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testEmployee.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testEmployee.getSurName()).isEqualTo(DEFAULT_SUR_NAME);
        assertThat(testEmployee.getNationalIdNo()).isEqualTo(DEFAULT_NATIONAL_ID_NO);
        assertThat(testEmployee.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testEmployee.getPlaceOfBirth()).isEqualTo(UPDATED_PLACE_OF_BIRTH);
        assertThat(testEmployee.getFatherName()).isEqualTo(UPDATED_FATHER_NAME);
        assertThat(testEmployee.getMotherName()).isEqualTo(UPDATED_MOTHER_NAME);
        assertThat(testEmployee.getBloodGroup()).isEqualTo(DEFAULT_BLOOD_GROUP);
        assertThat(testEmployee.getPresentAddress()).isEqualTo(DEFAULT_PRESENT_ADDRESS);
        assertThat(testEmployee.getPermanentAddress()).isEqualTo(DEFAULT_PERMANENT_ADDRESS);
        assertThat(testEmployee.getPersonalContactNo()).isEqualTo(UPDATED_PERSONAL_CONTACT_NO);
        assertThat(testEmployee.getPersonalEmail()).isEqualTo(DEFAULT_PERSONAL_EMAIL);
        assertThat(testEmployee.getReligion()).isEqualTo(DEFAULT_RELIGION);
        assertThat(testEmployee.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testEmployee.getDateOfMarriage()).isEqualTo(DEFAULT_DATE_OF_MARRIAGE);
        assertThat(testEmployee.getSpouseName()).isEqualTo(DEFAULT_SPOUSE_NAME);
        assertThat(testEmployee.getOfficialEmail()).isEqualTo(DEFAULT_OFFICIAL_EMAIL);
        assertThat(testEmployee.getOfficialContactNo()).isEqualTo(DEFAULT_OFFICIAL_CONTACT_NO);
        assertThat(testEmployee.getOfficePhoneExtension()).isEqualTo(DEFAULT_OFFICE_PHONE_EXTENSION);
        assertThat(testEmployee.getWhatsappId()).isEqualTo(DEFAULT_WHATSAPP_ID);
        assertThat(testEmployee.getSkypeId()).isEqualTo(DEFAULT_SKYPE_ID);
        assertThat(testEmployee.getEmergencyContactPersonName()).isEqualTo(DEFAULT_EMERGENCY_CONTACT_PERSON_NAME);
        assertThat(testEmployee.getEmergencyContactPersonRelationshipWithEmployee())
            .isEqualTo(UPDATED_EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE);
        assertThat(testEmployee.getEmergencyContactPersonContactNumber()).isEqualTo(DEFAULT_EMERGENCY_CONTACT_PERSON_CONTACT_NUMBER);
        assertThat(testEmployee.getMainGrossSalary()).isEqualTo(DEFAULT_MAIN_GROSS_SALARY);
        assertThat(testEmployee.getEmployeeCategory()).isEqualTo(DEFAULT_EMPLOYEE_CATEGORY);
        assertThat(testEmployee.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testEmployee.getDateOfJoining()).isEqualTo(UPDATED_DATE_OF_JOINING);
        assertThat(testEmployee.getDateOfConfirmation()).isEqualTo(UPDATED_DATE_OF_CONFIRMATION);
        assertThat(testEmployee.getIsProbationaryPeriodExtended()).isEqualTo(UPDATED_IS_PROBATIONARY_PERIOD_EXTENDED);
        assertThat(testEmployee.getProbationPeriodExtendedTo()).isEqualTo(UPDATED_PROBATION_PERIOD_EXTENDED_TO);
        assertThat(testEmployee.getPayType()).isEqualTo(UPDATED_PAY_TYPE);
        assertThat(testEmployee.getDisbursementMethod()).isEqualTo(DEFAULT_DISBURSEMENT_METHOD);
        assertThat(testEmployee.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
        assertThat(testEmployee.getBankAccountNo()).isEqualTo(DEFAULT_BANK_ACCOUNT_NO);
        assertThat(testEmployee.getMobileCelling()).isEqualTo(DEFAULT_MOBILE_CELLING);
        assertThat(testEmployee.getBkashNumber()).isEqualTo(UPDATED_BKASH_NUMBER);
        assertThat(testEmployee.getCardType()).isEqualTo(DEFAULT_CARD_TYPE);
        assertThat(testEmployee.getCardNumber()).isEqualTo(UPDATED_CARD_NUMBER);
        assertThat(testEmployee.getTinNumber()).isEqualTo(DEFAULT_TIN_NUMBER);
        assertThat(testEmployee.getPassportNo()).isEqualTo(UPDATED_PASSPORT_NO);
        assertThat(testEmployee.getPassportPlaceOfIssue()).isEqualTo(UPDATED_PASSPORT_PLACE_OF_ISSUE);
        assertThat(testEmployee.getPassportIssuedDate()).isEqualTo(DEFAULT_PASSPORT_ISSUED_DATE);
        assertThat(testEmployee.getPassportExpiryDate()).isEqualTo(DEFAULT_PASSPORT_EXPIRY_DATE);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getWelfareFundDeduction()).isEqualTo(DEFAULT_WELFARE_FUND_DEDUCTION);
        assertThat(testEmployee.getEmploymentStatus()).isEqualTo(DEFAULT_EMPLOYMENT_STATUS);
        assertThat(testEmployee.getHasDisabledChild()).isEqualTo(DEFAULT_HAS_DISABLED_CHILD);
        assertThat(testEmployee.getIsFirstTimeAitGiver()).isEqualTo(DEFAULT_IS_FIRST_TIME_AIT_GIVER);
        assertThat(testEmployee.getIsSalaryHold()).isEqualTo(UPDATED_IS_SALARY_HOLD);
        assertThat(testEmployee.getIsFestivalBonusHold()).isEqualTo(DEFAULT_IS_FESTIVAL_BONUS_HOLD);
        assertThat(testEmployee.getIsPhysicallyDisabled()).isEqualTo(DEFAULT_IS_PHYSICALLY_DISABLED);
        assertThat(testEmployee.getIsFreedomFighter()).isEqualTo(DEFAULT_IS_FREEDOM_FIGHTER);
        assertThat(testEmployee.getHasOverTime()).isEqualTo(DEFAULT_HAS_OVER_TIME);
        assertThat(testEmployee.getProbationPeriodEndDate()).isEqualTo(UPDATED_PROBATION_PERIOD_END_DATE);
        assertThat(testEmployee.getContractPeriodExtendedTo()).isEqualTo(DEFAULT_CONTRACT_PERIOD_EXTENDED_TO);
        assertThat(testEmployee.getContractPeriodEndDate()).isEqualTo(DEFAULT_CONTRACT_PERIOD_END_DATE);
        assertThat(testEmployee.getCardType02()).isEqualTo(DEFAULT_CARD_TYPE_02);
        assertThat(testEmployee.getCardNumber02()).isEqualTo(UPDATED_CARD_NUMBER_02);
        assertThat(testEmployee.getCardType03()).isEqualTo(UPDATED_CARD_TYPE_03);
        assertThat(testEmployee.getCardNumber03()).isEqualTo(UPDATED_CARD_NUMBER_03);
        assertThat(testEmployee.getAllowance01()).isEqualTo(UPDATED_ALLOWANCE_01);
        assertThat(testEmployee.getAllowance01EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_01_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance01EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_01_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance02()).isEqualTo(UPDATED_ALLOWANCE_02);
        assertThat(testEmployee.getAllowance02EffectiveFrom()).isEqualTo(DEFAULT_ALLOWANCE_02_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance02EffectiveTo()).isEqualTo(DEFAULT_ALLOWANCE_02_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance03()).isEqualTo(DEFAULT_ALLOWANCE_03);
        assertThat(testEmployee.getAllowance03EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_03_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance03EffectiveTo()).isEqualTo(DEFAULT_ALLOWANCE_03_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance04()).isEqualTo(DEFAULT_ALLOWANCE_04);
        assertThat(testEmployee.getAllowance04EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_04_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance04EffectiveTo()).isEqualTo(DEFAULT_ALLOWANCE_04_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance05()).isEqualTo(DEFAULT_ALLOWANCE_05);
        assertThat(testEmployee.getAllowance05EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_05_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance05EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_05_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance06()).isEqualTo(UPDATED_ALLOWANCE_06);
        assertThat(testEmployee.getAllowance06EffectiveFrom()).isEqualTo(DEFAULT_ALLOWANCE_06_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance06EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_06_EFFECTIVE_TO);
        assertThat(testEmployee.getIsTaxPaidByOrganisation()).isEqualTo(DEFAULT_IS_TAX_PAID_BY_ORGANISATION);
        assertThat(testEmployee.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmployee.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEmployee.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testEmployee.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testEmployee.getIsAllowedToGiveOnlineAttendance()).isEqualTo(UPDATED_IS_ALLOWED_TO_GIVE_ONLINE_ATTENDANCE);
        assertThat(testEmployee.getNoticePeriodInDays()).isEqualTo(DEFAULT_NOTICE_PERIOD_IN_DAYS);
        assertThat(testEmployee.getIsFixedTermContract()).isEqualTo(DEFAULT_IS_FIXED_TERM_CONTRACT);
        assertThat(testEmployee.getCurrentInTime()).isEqualTo(UPDATED_CURRENT_IN_TIME);
        assertThat(testEmployee.getCurrentOutTime()).isEqualTo(DEFAULT_CURRENT_OUT_TIME);
        assertThat(testEmployee.getOnlineAttendanceSanctionedAt()).isEqualTo(UPDATED_ONLINE_ATTENDANCE_SANCTIONED_AT);
        assertThat(testEmployee.getIsNidVerified()).isEqualTo(DEFAULT_IS_NID_VERIFIED);
        assertThat(testEmployee.getCanRaiseRrfOnBehalf()).isEqualTo(DEFAULT_CAN_RAISE_RRF_ON_BEHALF);
        assertThat(testEmployee.getTaxesCircle()).isEqualTo(DEFAULT_TAXES_CIRCLE);
        assertThat(testEmployee.getTaxesZone()).isEqualTo(UPDATED_TAXES_ZONE);
        assertThat(testEmployee.getCanManageTaxAcknowledgementReceipt()).isEqualTo(DEFAULT_CAN_MANAGE_TAX_ACKNOWLEDGEMENT_RECEIPT);
        assertThat(testEmployee.getIsEligibleForAutomatedAttendance()).isEqualTo(DEFAULT_IS_ELIGIBLE_FOR_AUTOMATED_ATTENDANCE);
        assertThat(testEmployee.getIsBillableResource()).isEqualTo(UPDATED_IS_BILLABLE_RESOURCE);
        assertThat(testEmployee.getIsAugmentedResource()).isEqualTo(UPDATED_IS_AUGMENTED_RESOURCE);
        assertThat(testEmployee.getLastWorkingDay()).isEqualTo(DEFAULT_LAST_WORKING_DAY);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .referenceId(UPDATED_REFERENCE_ID)
            .pin(UPDATED_PIN)
            .picture(UPDATED_PICTURE)
            .fullName(UPDATED_FULL_NAME)
            .surName(UPDATED_SUR_NAME)
            .nationalIdNo(UPDATED_NATIONAL_ID_NO)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH)
            .fatherName(UPDATED_FATHER_NAME)
            .motherName(UPDATED_MOTHER_NAME)
            .bloodGroup(UPDATED_BLOOD_GROUP)
            .presentAddress(UPDATED_PRESENT_ADDRESS)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .personalContactNo(UPDATED_PERSONAL_CONTACT_NO)
            .personalEmail(UPDATED_PERSONAL_EMAIL)
            .religion(UPDATED_RELIGION)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .dateOfMarriage(UPDATED_DATE_OF_MARRIAGE)
            .spouseName(UPDATED_SPOUSE_NAME)
            .officialEmail(UPDATED_OFFICIAL_EMAIL)
            .officialContactNo(UPDATED_OFFICIAL_CONTACT_NO)
            .officePhoneExtension(UPDATED_OFFICE_PHONE_EXTENSION)
            .whatsappId(UPDATED_WHATSAPP_ID)
            .skypeId(UPDATED_SKYPE_ID)
            .emergencyContactPersonName(UPDATED_EMERGENCY_CONTACT_PERSON_NAME)
            .emergencyContactPersonRelationshipWithEmployee(UPDATED_EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE)
            .emergencyContactPersonContactNumber(UPDATED_EMERGENCY_CONTACT_PERSON_CONTACT_NUMBER)
            .mainGrossSalary(UPDATED_MAIN_GROSS_SALARY)
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .location(UPDATED_LOCATION)
            .dateOfJoining(UPDATED_DATE_OF_JOINING)
            .dateOfConfirmation(UPDATED_DATE_OF_CONFIRMATION)
            .isProbationaryPeriodExtended(UPDATED_IS_PROBATIONARY_PERIOD_EXTENDED)
            .probationPeriodExtendedTo(UPDATED_PROBATION_PERIOD_EXTENDED_TO)
            .payType(UPDATED_PAY_TYPE)
            .disbursementMethod(UPDATED_DISBURSEMENT_METHOD)
            .bankName(UPDATED_BANK_NAME)
            .bankAccountNo(UPDATED_BANK_ACCOUNT_NO)
            .mobileCelling(UPDATED_MOBILE_CELLING)
            .bkashNumber(UPDATED_BKASH_NUMBER)
            .cardType(UPDATED_CARD_TYPE)
            .cardNumber(UPDATED_CARD_NUMBER)
            .tinNumber(UPDATED_TIN_NUMBER)
            .passportNo(UPDATED_PASSPORT_NO)
            .passportPlaceOfIssue(UPDATED_PASSPORT_PLACE_OF_ISSUE)
            .passportIssuedDate(UPDATED_PASSPORT_ISSUED_DATE)
            .passportExpiryDate(UPDATED_PASSPORT_EXPIRY_DATE)
            .gender(UPDATED_GENDER)
            .welfareFundDeduction(UPDATED_WELFARE_FUND_DEDUCTION)
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .hasDisabledChild(UPDATED_HAS_DISABLED_CHILD)
            .isFirstTimeAitGiver(UPDATED_IS_FIRST_TIME_AIT_GIVER)
            .isSalaryHold(UPDATED_IS_SALARY_HOLD)
            .isFestivalBonusHold(UPDATED_IS_FESTIVAL_BONUS_HOLD)
            .isPhysicallyDisabled(UPDATED_IS_PHYSICALLY_DISABLED)
            .isFreedomFighter(UPDATED_IS_FREEDOM_FIGHTER)
            .hasOverTime(UPDATED_HAS_OVER_TIME)
            .probationPeriodEndDate(UPDATED_PROBATION_PERIOD_END_DATE)
            .contractPeriodExtendedTo(UPDATED_CONTRACT_PERIOD_EXTENDED_TO)
            .contractPeriodEndDate(UPDATED_CONTRACT_PERIOD_END_DATE)
            .cardType02(UPDATED_CARD_TYPE_02)
            .cardNumber02(UPDATED_CARD_NUMBER_02)
            .cardType03(UPDATED_CARD_TYPE_03)
            .cardNumber03(UPDATED_CARD_NUMBER_03)
            .allowance01(UPDATED_ALLOWANCE_01)
            .allowance01EffectiveFrom(UPDATED_ALLOWANCE_01_EFFECTIVE_FROM)
            .allowance01EffectiveTo(UPDATED_ALLOWANCE_01_EFFECTIVE_TO)
            .allowance02(UPDATED_ALLOWANCE_02)
            .allowance02EffectiveFrom(UPDATED_ALLOWANCE_02_EFFECTIVE_FROM)
            .allowance02EffectiveTo(UPDATED_ALLOWANCE_02_EFFECTIVE_TO)
            .allowance03(UPDATED_ALLOWANCE_03)
            .allowance03EffectiveFrom(UPDATED_ALLOWANCE_03_EFFECTIVE_FROM)
            .allowance03EffectiveTo(UPDATED_ALLOWANCE_03_EFFECTIVE_TO)
            .allowance04(UPDATED_ALLOWANCE_04)
            .allowance04EffectiveFrom(UPDATED_ALLOWANCE_04_EFFECTIVE_FROM)
            .allowance04EffectiveTo(UPDATED_ALLOWANCE_04_EFFECTIVE_TO)
            .allowance05(UPDATED_ALLOWANCE_05)
            .allowance05EffectiveFrom(UPDATED_ALLOWANCE_05_EFFECTIVE_FROM)
            .allowance05EffectiveTo(UPDATED_ALLOWANCE_05_EFFECTIVE_TO)
            .allowance06(UPDATED_ALLOWANCE_06)
            .allowance06EffectiveFrom(UPDATED_ALLOWANCE_06_EFFECTIVE_FROM)
            .allowance06EffectiveTo(UPDATED_ALLOWANCE_06_EFFECTIVE_TO)
            .isTaxPaidByOrganisation(UPDATED_IS_TAX_PAID_BY_ORGANISATION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .isAllowedToGiveOnlineAttendance(UPDATED_IS_ALLOWED_TO_GIVE_ONLINE_ATTENDANCE)
            .noticePeriodInDays(UPDATED_NOTICE_PERIOD_IN_DAYS)
            .isFixedTermContract(UPDATED_IS_FIXED_TERM_CONTRACT)
            .currentInTime(UPDATED_CURRENT_IN_TIME)
            .currentOutTime(UPDATED_CURRENT_OUT_TIME)
            .onlineAttendanceSanctionedAt(UPDATED_ONLINE_ATTENDANCE_SANCTIONED_AT)
            .isNidVerified(UPDATED_IS_NID_VERIFIED)
            .canRaiseRrfOnBehalf(UPDATED_CAN_RAISE_RRF_ON_BEHALF)
            .taxesCircle(UPDATED_TAXES_CIRCLE)
            .taxesZone(UPDATED_TAXES_ZONE)
            .canManageTaxAcknowledgementReceipt(UPDATED_CAN_MANAGE_TAX_ACKNOWLEDGEMENT_RECEIPT)
            .isEligibleForAutomatedAttendance(UPDATED_IS_ELIGIBLE_FOR_AUTOMATED_ATTENDANCE)
            .isBillableResource(UPDATED_IS_BILLABLE_RESOURCE)
            .isAugmentedResource(UPDATED_IS_AUGMENTED_RESOURCE)
            .lastWorkingDay(UPDATED_LAST_WORKING_DAY);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getReferenceId()).isEqualTo(UPDATED_REFERENCE_ID);
        assertThat(testEmployee.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testEmployee.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testEmployee.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testEmployee.getSurName()).isEqualTo(UPDATED_SUR_NAME);
        assertThat(testEmployee.getNationalIdNo()).isEqualTo(UPDATED_NATIONAL_ID_NO);
        assertThat(testEmployee.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testEmployee.getPlaceOfBirth()).isEqualTo(UPDATED_PLACE_OF_BIRTH);
        assertThat(testEmployee.getFatherName()).isEqualTo(UPDATED_FATHER_NAME);
        assertThat(testEmployee.getMotherName()).isEqualTo(UPDATED_MOTHER_NAME);
        assertThat(testEmployee.getBloodGroup()).isEqualTo(UPDATED_BLOOD_GROUP);
        assertThat(testEmployee.getPresentAddress()).isEqualTo(UPDATED_PRESENT_ADDRESS);
        assertThat(testEmployee.getPermanentAddress()).isEqualTo(UPDATED_PERMANENT_ADDRESS);
        assertThat(testEmployee.getPersonalContactNo()).isEqualTo(UPDATED_PERSONAL_CONTACT_NO);
        assertThat(testEmployee.getPersonalEmail()).isEqualTo(UPDATED_PERSONAL_EMAIL);
        assertThat(testEmployee.getReligion()).isEqualTo(UPDATED_RELIGION);
        assertThat(testEmployee.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testEmployee.getDateOfMarriage()).isEqualTo(UPDATED_DATE_OF_MARRIAGE);
        assertThat(testEmployee.getSpouseName()).isEqualTo(UPDATED_SPOUSE_NAME);
        assertThat(testEmployee.getOfficialEmail()).isEqualTo(UPDATED_OFFICIAL_EMAIL);
        assertThat(testEmployee.getOfficialContactNo()).isEqualTo(UPDATED_OFFICIAL_CONTACT_NO);
        assertThat(testEmployee.getOfficePhoneExtension()).isEqualTo(UPDATED_OFFICE_PHONE_EXTENSION);
        assertThat(testEmployee.getWhatsappId()).isEqualTo(UPDATED_WHATSAPP_ID);
        assertThat(testEmployee.getSkypeId()).isEqualTo(UPDATED_SKYPE_ID);
        assertThat(testEmployee.getEmergencyContactPersonName()).isEqualTo(UPDATED_EMERGENCY_CONTACT_PERSON_NAME);
        assertThat(testEmployee.getEmergencyContactPersonRelationshipWithEmployee())
            .isEqualTo(UPDATED_EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE);
        assertThat(testEmployee.getEmergencyContactPersonContactNumber()).isEqualTo(UPDATED_EMERGENCY_CONTACT_PERSON_CONTACT_NUMBER);
        assertThat(testEmployee.getMainGrossSalary()).isEqualTo(UPDATED_MAIN_GROSS_SALARY);
        assertThat(testEmployee.getEmployeeCategory()).isEqualTo(UPDATED_EMPLOYEE_CATEGORY);
        assertThat(testEmployee.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testEmployee.getDateOfJoining()).isEqualTo(UPDATED_DATE_OF_JOINING);
        assertThat(testEmployee.getDateOfConfirmation()).isEqualTo(UPDATED_DATE_OF_CONFIRMATION);
        assertThat(testEmployee.getIsProbationaryPeriodExtended()).isEqualTo(UPDATED_IS_PROBATIONARY_PERIOD_EXTENDED);
        assertThat(testEmployee.getProbationPeriodExtendedTo()).isEqualTo(UPDATED_PROBATION_PERIOD_EXTENDED_TO);
        assertThat(testEmployee.getPayType()).isEqualTo(UPDATED_PAY_TYPE);
        assertThat(testEmployee.getDisbursementMethod()).isEqualTo(UPDATED_DISBURSEMENT_METHOD);
        assertThat(testEmployee.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testEmployee.getBankAccountNo()).isEqualTo(UPDATED_BANK_ACCOUNT_NO);
        assertThat(testEmployee.getMobileCelling()).isEqualTo(UPDATED_MOBILE_CELLING);
        assertThat(testEmployee.getBkashNumber()).isEqualTo(UPDATED_BKASH_NUMBER);
        assertThat(testEmployee.getCardType()).isEqualTo(UPDATED_CARD_TYPE);
        assertThat(testEmployee.getCardNumber()).isEqualTo(UPDATED_CARD_NUMBER);
        assertThat(testEmployee.getTinNumber()).isEqualTo(UPDATED_TIN_NUMBER);
        assertThat(testEmployee.getPassportNo()).isEqualTo(UPDATED_PASSPORT_NO);
        assertThat(testEmployee.getPassportPlaceOfIssue()).isEqualTo(UPDATED_PASSPORT_PLACE_OF_ISSUE);
        assertThat(testEmployee.getPassportIssuedDate()).isEqualTo(UPDATED_PASSPORT_ISSUED_DATE);
        assertThat(testEmployee.getPassportExpiryDate()).isEqualTo(UPDATED_PASSPORT_EXPIRY_DATE);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getWelfareFundDeduction()).isEqualTo(UPDATED_WELFARE_FUND_DEDUCTION);
        assertThat(testEmployee.getEmploymentStatus()).isEqualTo(UPDATED_EMPLOYMENT_STATUS);
        assertThat(testEmployee.getHasDisabledChild()).isEqualTo(UPDATED_HAS_DISABLED_CHILD);
        assertThat(testEmployee.getIsFirstTimeAitGiver()).isEqualTo(UPDATED_IS_FIRST_TIME_AIT_GIVER);
        assertThat(testEmployee.getIsSalaryHold()).isEqualTo(UPDATED_IS_SALARY_HOLD);
        assertThat(testEmployee.getIsFestivalBonusHold()).isEqualTo(UPDATED_IS_FESTIVAL_BONUS_HOLD);
        assertThat(testEmployee.getIsPhysicallyDisabled()).isEqualTo(UPDATED_IS_PHYSICALLY_DISABLED);
        assertThat(testEmployee.getIsFreedomFighter()).isEqualTo(UPDATED_IS_FREEDOM_FIGHTER);
        assertThat(testEmployee.getHasOverTime()).isEqualTo(UPDATED_HAS_OVER_TIME);
        assertThat(testEmployee.getProbationPeriodEndDate()).isEqualTo(UPDATED_PROBATION_PERIOD_END_DATE);
        assertThat(testEmployee.getContractPeriodExtendedTo()).isEqualTo(UPDATED_CONTRACT_PERIOD_EXTENDED_TO);
        assertThat(testEmployee.getContractPeriodEndDate()).isEqualTo(UPDATED_CONTRACT_PERIOD_END_DATE);
        assertThat(testEmployee.getCardType02()).isEqualTo(UPDATED_CARD_TYPE_02);
        assertThat(testEmployee.getCardNumber02()).isEqualTo(UPDATED_CARD_NUMBER_02);
        assertThat(testEmployee.getCardType03()).isEqualTo(UPDATED_CARD_TYPE_03);
        assertThat(testEmployee.getCardNumber03()).isEqualTo(UPDATED_CARD_NUMBER_03);
        assertThat(testEmployee.getAllowance01()).isEqualTo(UPDATED_ALLOWANCE_01);
        assertThat(testEmployee.getAllowance01EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_01_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance01EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_01_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance02()).isEqualTo(UPDATED_ALLOWANCE_02);
        assertThat(testEmployee.getAllowance02EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_02_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance02EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_02_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance03()).isEqualTo(UPDATED_ALLOWANCE_03);
        assertThat(testEmployee.getAllowance03EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_03_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance03EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_03_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance04()).isEqualTo(UPDATED_ALLOWANCE_04);
        assertThat(testEmployee.getAllowance04EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_04_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance04EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_04_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance05()).isEqualTo(UPDATED_ALLOWANCE_05);
        assertThat(testEmployee.getAllowance05EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_05_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance05EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_05_EFFECTIVE_TO);
        assertThat(testEmployee.getAllowance06()).isEqualTo(UPDATED_ALLOWANCE_06);
        assertThat(testEmployee.getAllowance06EffectiveFrom()).isEqualTo(UPDATED_ALLOWANCE_06_EFFECTIVE_FROM);
        assertThat(testEmployee.getAllowance06EffectiveTo()).isEqualTo(UPDATED_ALLOWANCE_06_EFFECTIVE_TO);
        assertThat(testEmployee.getIsTaxPaidByOrganisation()).isEqualTo(UPDATED_IS_TAX_PAID_BY_ORGANISATION);
        assertThat(testEmployee.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmployee.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployee.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testEmployee.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmployee.getIsAllowedToGiveOnlineAttendance()).isEqualTo(UPDATED_IS_ALLOWED_TO_GIVE_ONLINE_ATTENDANCE);
        assertThat(testEmployee.getNoticePeriodInDays()).isEqualTo(UPDATED_NOTICE_PERIOD_IN_DAYS);
        assertThat(testEmployee.getIsFixedTermContract()).isEqualTo(UPDATED_IS_FIXED_TERM_CONTRACT);
        assertThat(testEmployee.getCurrentInTime()).isEqualTo(UPDATED_CURRENT_IN_TIME);
        assertThat(testEmployee.getCurrentOutTime()).isEqualTo(UPDATED_CURRENT_OUT_TIME);
        assertThat(testEmployee.getOnlineAttendanceSanctionedAt()).isEqualTo(UPDATED_ONLINE_ATTENDANCE_SANCTIONED_AT);
        assertThat(testEmployee.getIsNidVerified()).isEqualTo(UPDATED_IS_NID_VERIFIED);
        assertThat(testEmployee.getCanRaiseRrfOnBehalf()).isEqualTo(UPDATED_CAN_RAISE_RRF_ON_BEHALF);
        assertThat(testEmployee.getTaxesCircle()).isEqualTo(UPDATED_TAXES_CIRCLE);
        assertThat(testEmployee.getTaxesZone()).isEqualTo(UPDATED_TAXES_ZONE);
        assertThat(testEmployee.getCanManageTaxAcknowledgementReceipt()).isEqualTo(UPDATED_CAN_MANAGE_TAX_ACKNOWLEDGEMENT_RECEIPT);
        assertThat(testEmployee.getIsEligibleForAutomatedAttendance()).isEqualTo(UPDATED_IS_ELIGIBLE_FOR_AUTOMATED_ATTENDANCE);
        assertThat(testEmployee.getIsBillableResource()).isEqualTo(UPDATED_IS_BILLABLE_RESOURCE);
        assertThat(testEmployee.getIsAugmentedResource()).isEqualTo(UPDATED_IS_AUGMENTED_RESOURCE);
        assertThat(testEmployee.getLastWorkingDay()).isEqualTo(UPDATED_LAST_WORKING_DAY);
    }

    @Test
    @Transactional
    void patchNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        restEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
