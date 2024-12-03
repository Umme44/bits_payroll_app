package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.mapper.EmployeeSalaryMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link EmployeeSalaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeSalaryResourceIT {

    private static final Month DEFAULT_MONTH = Month.JANUARY;
    private static final Month UPDATED_MONTH = Month.FEBRUARY;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final LocalDate DEFAULT_SALARY_GENERATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SALARY_GENERATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REF_PIN = "AAAAAAAAAA";
    private static final String UPDATED_REF_PIN = "BBBBBBBBBB";

    private static final String DEFAULT_PIN = "AAAAAAAAAA";
    private static final String UPDATED_PIN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_JOINING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_JOINING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CONFIRMATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CONFIRMATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final EmployeeCategory DEFAULT_EMPLOYEE_CATEGORY = EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE;
    private static final EmployeeCategory UPDATED_EMPLOYEE_CATEGORY = EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE;

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final Double DEFAULT_MAIN_GROSS_SALARY = 1D;
    private static final Double UPDATED_MAIN_GROSS_SALARY = 2D;

    private static final Double DEFAULT_MAIN_GROSS_BASIC_SALARY = 1D;
    private static final Double UPDATED_MAIN_GROSS_BASIC_SALARY = 2D;

    private static final Double DEFAULT_MAIN_GROSS_HOUSE_RENT = 1D;
    private static final Double UPDATED_MAIN_GROSS_HOUSE_RENT = 2D;

    private static final Double DEFAULT_MAIN_GROSS_MEDICAL_ALLOWANCE = 1D;
    private static final Double UPDATED_MAIN_GROSS_MEDICAL_ALLOWANCE = 2D;

    private static final Double DEFAULT_MAIN_GROSS_CONVEYANCE_ALLOWANCE = 1D;
    private static final Double UPDATED_MAIN_GROSS_CONVEYANCE_ALLOWANCE = 2D;

    private static final Integer DEFAULT_ABSENT_DAYS = 1;
    private static final Integer UPDATED_ABSENT_DAYS = 2;

    private static final Integer DEFAULT_FRACTION_DAYS = 1;
    private static final Integer UPDATED_FRACTION_DAYS = 2;

    private static final Double DEFAULT_PAYABLE_GROSS_SALARY = 1D;
    private static final Double UPDATED_PAYABLE_GROSS_SALARY = 2D;

    private static final Double DEFAULT_PAYABLE_GROSS_BASIC_SALARY = 1D;
    private static final Double UPDATED_PAYABLE_GROSS_BASIC_SALARY = 2D;

    private static final Double DEFAULT_PAYABLE_GROSS_HOUSE_RENT = 1D;
    private static final Double UPDATED_PAYABLE_GROSS_HOUSE_RENT = 2D;

    private static final Double DEFAULT_PAYABLE_GROSS_MEDICAL_ALLOWANCE = 1D;
    private static final Double UPDATED_PAYABLE_GROSS_MEDICAL_ALLOWANCE = 2D;

    private static final Double DEFAULT_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE = 1D;
    private static final Double UPDATED_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE = 2D;

    private static final Double DEFAULT_ARREAR_SALARY = 1D;
    private static final Double UPDATED_ARREAR_SALARY = 2D;

    private static final Double DEFAULT_PF_DEDUCTION = 1D;
    private static final Double UPDATED_PF_DEDUCTION = 2D;

    private static final Double DEFAULT_TAX_DEDUCTION = 1D;
    private static final Double UPDATED_TAX_DEDUCTION = 2D;

    private static final Double DEFAULT_WELFARE_FUND_DEDUCTION = 1D;
    private static final Double UPDATED_WELFARE_FUND_DEDUCTION = 2D;

    private static final Double DEFAULT_MOBILE_BILL_DEDUCTION = 1D;
    private static final Double UPDATED_MOBILE_BILL_DEDUCTION = 2D;

    private static final Double DEFAULT_OTHER_DEDUCTION = 1D;
    private static final Double UPDATED_OTHER_DEDUCTION = 2D;

    private static final Double DEFAULT_TOTAL_DEDUCTION = 1D;
    private static final Double UPDATED_TOTAL_DEDUCTION = 2D;

    private static final Double DEFAULT_NET_PAY = 1D;
    private static final Double UPDATED_NET_PAY = 2D;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Double DEFAULT_PF_CONTRIBUTION = 1D;
    private static final Double UPDATED_PF_CONTRIBUTION = 2D;

    private static final Double DEFAULT_GF_CONTRIBUTION = 1D;
    private static final Double UPDATED_GF_CONTRIBUTION = 2D;

    private static final Double DEFAULT_PROVISION_FOR_FESTIVAL_BONUS = 1D;
    private static final Double UPDATED_PROVISION_FOR_FESTIVAL_BONUS = 2D;

    private static final Double DEFAULT_PROVISION_FOR_LEAVE_ENCASHMENT = 1D;
    private static final Double UPDATED_PROVISION_FOR_LEAVE_ENCASHMENT = 2D;

    private static final Boolean DEFAULT_IS_FINALIZED = false;
    private static final Boolean UPDATED_IS_FINALIZED = true;

    private static final Boolean DEFAULT_IS_DISPATCHED = false;
    private static final Boolean UPDATED_IS_DISPATCHED = true;

    private static final Double DEFAULT_ENTERTAINMENT = 0D;
    private static final Double UPDATED_ENTERTAINMENT = 1D;

    private static final Double DEFAULT_UTILITY = 0D;
    private static final Double UPDATED_UTILITY = 1D;

    private static final Double DEFAULT_OTHER_ADDITION = 1D;
    private static final Double UPDATED_OTHER_ADDITION = 2D;

    private static final Double DEFAULT_SALARY_ADJUSTMENT = -10000000D;
    private static final Double UPDATED_SALARY_ADJUSTMENT = -9999999D;

    private static final Double DEFAULT_PROVIDENT_FUND_ARREAR = 0D;
    private static final Double UPDATED_PROVIDENT_FUND_ARREAR = 1D;

    private static final Double DEFAULT_ALLOWANCE_01 = 0D;
    private static final Double UPDATED_ALLOWANCE_01 = 1D;

    private static final Double DEFAULT_ALLOWANCE_02 = 0D;
    private static final Double UPDATED_ALLOWANCE_02 = 1D;

    private static final Double DEFAULT_ALLOWANCE_03 = 0D;
    private static final Double UPDATED_ALLOWANCE_03 = 1D;

    private static final Double DEFAULT_ALLOWANCE_04 = 0D;
    private static final Double UPDATED_ALLOWANCE_04 = 1D;

    private static final Double DEFAULT_ALLOWANCE_05 = 0D;
    private static final Double UPDATED_ALLOWANCE_05 = 1D;

    private static final Double DEFAULT_ALLOWANCE_06 = 0D;
    private static final Double UPDATED_ALLOWANCE_06 = 1D;

    private static final Double DEFAULT_PROVISION_FOR_PROJECT_BONUS = 0D;
    private static final Double UPDATED_PROVISION_FOR_PROJECT_BONUS = 1D;

    private static final Boolean DEFAULT_IS_HOLD = false;
    private static final Boolean UPDATED_IS_HOLD = true;

    private static final LocalDate DEFAULT_ATTENDANCE_REGULARISATION_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ATTENDANCE_REGULARISATION_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ATTENDANCE_REGULARISATION_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ATTENDANCE_REGULARISATION_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_VISIBLE_TO_EMPLOYEE = false;
    private static final Boolean UPDATED_IS_VISIBLE_TO_EMPLOYEE = true;

    private static final Double DEFAULT_PF_ARREAR = 1D;
    private static final Double UPDATED_PF_ARREAR = 2D;

    private static final String DEFAULT_TAX_CALCULATION_SNAPSHOT = "AAAAAAAAAA";
    private static final String UPDATED_TAX_CALCULATION_SNAPSHOT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employee-salaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    private EmployeeSalaryMapper employeeSalaryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeSalaryMockMvc;

    private EmployeeSalary employeeSalary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeSalary createEntity(EntityManager em) {
        EmployeeSalary employeeSalary = new EmployeeSalary()
            .month(DEFAULT_MONTH)
            .year(DEFAULT_YEAR)
            .salaryGenerationDate(DEFAULT_SALARY_GENERATION_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT)
            .refPin(DEFAULT_REF_PIN)
            .pin(DEFAULT_PIN)
            .joiningDate(DEFAULT_JOINING_DATE)
            .confirmationDate(DEFAULT_CONFIRMATION_DATE)
            .employeeCategory(DEFAULT_EMPLOYEE_CATEGORY)
            .unit(DEFAULT_UNIT)
            .department(DEFAULT_DEPARTMENT)
            .mainGrossSalary(DEFAULT_MAIN_GROSS_SALARY)
            .mainGrossBasicSalary(DEFAULT_MAIN_GROSS_BASIC_SALARY)
            .mainGrossHouseRent(DEFAULT_MAIN_GROSS_HOUSE_RENT)
            .mainGrossMedicalAllowance(DEFAULT_MAIN_GROSS_MEDICAL_ALLOWANCE)
            .mainGrossConveyanceAllowance(DEFAULT_MAIN_GROSS_CONVEYANCE_ALLOWANCE)
            .absentDays(DEFAULT_ABSENT_DAYS)
            .fractionDays(DEFAULT_FRACTION_DAYS)
            .payableGrossSalary(DEFAULT_PAYABLE_GROSS_SALARY)
            .payableGrossBasicSalary(DEFAULT_PAYABLE_GROSS_BASIC_SALARY)
            .payableGrossHouseRent(DEFAULT_PAYABLE_GROSS_HOUSE_RENT)
            .payableGrossMedicalAllowance(DEFAULT_PAYABLE_GROSS_MEDICAL_ALLOWANCE)
            .payableGrossConveyanceAllowance(DEFAULT_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE)
            .arrearSalary(DEFAULT_ARREAR_SALARY)
            .pfDeduction(DEFAULT_PF_DEDUCTION)
            .taxDeduction(DEFAULT_TAX_DEDUCTION)
            .welfareFundDeduction(DEFAULT_WELFARE_FUND_DEDUCTION)
            .mobileBillDeduction(DEFAULT_MOBILE_BILL_DEDUCTION)
            .otherDeduction(DEFAULT_OTHER_DEDUCTION)
            .totalDeduction(DEFAULT_TOTAL_DEDUCTION)
            .netPay(DEFAULT_NET_PAY)
            .remarks(DEFAULT_REMARKS)
            .pfContribution(DEFAULT_PF_CONTRIBUTION)
            .gfContribution(DEFAULT_GF_CONTRIBUTION)
            .provisionForFestivalBonus(DEFAULT_PROVISION_FOR_FESTIVAL_BONUS)
            .provisionForLeaveEncashment(DEFAULT_PROVISION_FOR_LEAVE_ENCASHMENT)
            .isFinalized(DEFAULT_IS_FINALIZED)
            .isDispatched(DEFAULT_IS_DISPATCHED)
            .entertainment(DEFAULT_ENTERTAINMENT)
            .utility(DEFAULT_UTILITY)
            .otherAddition(DEFAULT_OTHER_ADDITION)
            .salaryAdjustment(DEFAULT_SALARY_ADJUSTMENT)
            .providentFundArrear(DEFAULT_PROVIDENT_FUND_ARREAR)
            .allowance01(DEFAULT_ALLOWANCE_01)
            .allowance02(DEFAULT_ALLOWANCE_02)
            .allowance03(DEFAULT_ALLOWANCE_03)
            .allowance04(DEFAULT_ALLOWANCE_04)
            .allowance05(DEFAULT_ALLOWANCE_05)
            .allowance06(DEFAULT_ALLOWANCE_06)
            .provisionForProjectBonus(DEFAULT_PROVISION_FOR_PROJECT_BONUS)
            .isHold(DEFAULT_IS_HOLD)
            .attendanceRegularisationStartDate(DEFAULT_ATTENDANCE_REGULARISATION_START_DATE)
            .attendanceRegularisationEndDate(DEFAULT_ATTENDANCE_REGULARISATION_END_DATE)
            .title(DEFAULT_TITLE)
            .isVisibleToEmployee(DEFAULT_IS_VISIBLE_TO_EMPLOYEE)
            .pfArrear(DEFAULT_PF_ARREAR)
            .taxCalculationSnapshot(DEFAULT_TAX_CALCULATION_SNAPSHOT);
        return employeeSalary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeSalary createUpdatedEntity(EntityManager em) {
        EmployeeSalary employeeSalary = new EmployeeSalary()
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .salaryGenerationDate(UPDATED_SALARY_GENERATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .refPin(UPDATED_REF_PIN)
            .pin(UPDATED_PIN)
            .joiningDate(UPDATED_JOINING_DATE)
            .confirmationDate(UPDATED_CONFIRMATION_DATE)
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .unit(UPDATED_UNIT)
            .department(UPDATED_DEPARTMENT)
            .mainGrossSalary(UPDATED_MAIN_GROSS_SALARY)
            .mainGrossBasicSalary(UPDATED_MAIN_GROSS_BASIC_SALARY)
            .mainGrossHouseRent(UPDATED_MAIN_GROSS_HOUSE_RENT)
            .mainGrossMedicalAllowance(UPDATED_MAIN_GROSS_MEDICAL_ALLOWANCE)
            .mainGrossConveyanceAllowance(UPDATED_MAIN_GROSS_CONVEYANCE_ALLOWANCE)
            .absentDays(UPDATED_ABSENT_DAYS)
            .fractionDays(UPDATED_FRACTION_DAYS)
            .payableGrossSalary(UPDATED_PAYABLE_GROSS_SALARY)
            .payableGrossBasicSalary(UPDATED_PAYABLE_GROSS_BASIC_SALARY)
            .payableGrossHouseRent(UPDATED_PAYABLE_GROSS_HOUSE_RENT)
            .payableGrossMedicalAllowance(UPDATED_PAYABLE_GROSS_MEDICAL_ALLOWANCE)
            .payableGrossConveyanceAllowance(UPDATED_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE)
            .arrearSalary(UPDATED_ARREAR_SALARY)
            .pfDeduction(UPDATED_PF_DEDUCTION)
            .taxDeduction(UPDATED_TAX_DEDUCTION)
            .welfareFundDeduction(UPDATED_WELFARE_FUND_DEDUCTION)
            .mobileBillDeduction(UPDATED_MOBILE_BILL_DEDUCTION)
            .otherDeduction(UPDATED_OTHER_DEDUCTION)
            .totalDeduction(UPDATED_TOTAL_DEDUCTION)
            .netPay(UPDATED_NET_PAY)
            .remarks(UPDATED_REMARKS)
            .pfContribution(UPDATED_PF_CONTRIBUTION)
            .gfContribution(UPDATED_GF_CONTRIBUTION)
            .provisionForFestivalBonus(UPDATED_PROVISION_FOR_FESTIVAL_BONUS)
            .provisionForLeaveEncashment(UPDATED_PROVISION_FOR_LEAVE_ENCASHMENT)
            .isFinalized(UPDATED_IS_FINALIZED)
            .isDispatched(UPDATED_IS_DISPATCHED)
            .entertainment(UPDATED_ENTERTAINMENT)
            .utility(UPDATED_UTILITY)
            .otherAddition(UPDATED_OTHER_ADDITION)
            .salaryAdjustment(UPDATED_SALARY_ADJUSTMENT)
            .providentFundArrear(UPDATED_PROVIDENT_FUND_ARREAR)
            .allowance01(UPDATED_ALLOWANCE_01)
            .allowance02(UPDATED_ALLOWANCE_02)
            .allowance03(UPDATED_ALLOWANCE_03)
            .allowance04(UPDATED_ALLOWANCE_04)
            .allowance05(UPDATED_ALLOWANCE_05)
            .allowance06(UPDATED_ALLOWANCE_06)
            .provisionForProjectBonus(UPDATED_PROVISION_FOR_PROJECT_BONUS)
            .isHold(UPDATED_IS_HOLD)
            .attendanceRegularisationStartDate(UPDATED_ATTENDANCE_REGULARISATION_START_DATE)
            .attendanceRegularisationEndDate(UPDATED_ATTENDANCE_REGULARISATION_END_DATE)
            .title(UPDATED_TITLE)
            .isVisibleToEmployee(UPDATED_IS_VISIBLE_TO_EMPLOYEE)
            .pfArrear(UPDATED_PF_ARREAR)
            .taxCalculationSnapshot(UPDATED_TAX_CALCULATION_SNAPSHOT);
        return employeeSalary;
    }

    @BeforeEach
    public void initTest() {
        employeeSalary = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeeSalary() throws Exception {
        int databaseSizeBeforeCreate = employeeSalaryRepository.findAll().size();
        // Create the EmployeeSalary
        EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryMapper.toDto(employeeSalary);
        restEmployeeSalaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeSalaryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EmployeeSalary in the database
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAll();
        assertThat(employeeSalaryList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeSalary testEmployeeSalary = employeeSalaryList.get(employeeSalaryList.size() - 1);
        assertThat(testEmployeeSalary.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testEmployeeSalary.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testEmployeeSalary.getSalaryGenerationDate()).isEqualTo(DEFAULT_SALARY_GENERATION_DATE);
        assertThat(testEmployeeSalary.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testEmployeeSalary.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEmployeeSalary.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testEmployeeSalary.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testEmployeeSalary.getRefPin()).isEqualTo(DEFAULT_REF_PIN);
        assertThat(testEmployeeSalary.getPin()).isEqualTo(DEFAULT_PIN);
        assertThat(testEmployeeSalary.getJoiningDate()).isEqualTo(DEFAULT_JOINING_DATE);
        assertThat(testEmployeeSalary.getConfirmationDate()).isEqualTo(DEFAULT_CONFIRMATION_DATE);
        assertThat(testEmployeeSalary.getEmployeeCategory()).isEqualTo(DEFAULT_EMPLOYEE_CATEGORY);
        assertThat(testEmployeeSalary.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testEmployeeSalary.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testEmployeeSalary.getMainGrossSalary()).isEqualTo(DEFAULT_MAIN_GROSS_SALARY);
        assertThat(testEmployeeSalary.getMainGrossBasicSalary()).isEqualTo(DEFAULT_MAIN_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalary.getMainGrossHouseRent()).isEqualTo(DEFAULT_MAIN_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalary.getMainGrossMedicalAllowance()).isEqualTo(DEFAULT_MAIN_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalary.getMainGrossConveyanceAllowance()).isEqualTo(DEFAULT_MAIN_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalary.getAbsentDays()).isEqualTo(DEFAULT_ABSENT_DAYS);
        assertThat(testEmployeeSalary.getFractionDays()).isEqualTo(DEFAULT_FRACTION_DAYS);
        assertThat(testEmployeeSalary.getPayableGrossSalary()).isEqualTo(DEFAULT_PAYABLE_GROSS_SALARY);
        assertThat(testEmployeeSalary.getPayableGrossBasicSalary()).isEqualTo(DEFAULT_PAYABLE_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalary.getPayableGrossHouseRent()).isEqualTo(DEFAULT_PAYABLE_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalary.getPayableGrossMedicalAllowance()).isEqualTo(DEFAULT_PAYABLE_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalary.getPayableGrossConveyanceAllowance()).isEqualTo(DEFAULT_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalary.getArrearSalary()).isEqualTo(DEFAULT_ARREAR_SALARY);
        assertThat(testEmployeeSalary.getPfDeduction()).isEqualTo(DEFAULT_PF_DEDUCTION);
        assertThat(testEmployeeSalary.getTaxDeduction()).isEqualTo(DEFAULT_TAX_DEDUCTION);
        assertThat(testEmployeeSalary.getWelfareFundDeduction()).isEqualTo(DEFAULT_WELFARE_FUND_DEDUCTION);
        assertThat(testEmployeeSalary.getMobileBillDeduction()).isEqualTo(DEFAULT_MOBILE_BILL_DEDUCTION);
        assertThat(testEmployeeSalary.getOtherDeduction()).isEqualTo(DEFAULT_OTHER_DEDUCTION);
        assertThat(testEmployeeSalary.getTotalDeduction()).isEqualTo(DEFAULT_TOTAL_DEDUCTION);
        assertThat(testEmployeeSalary.getNetPay()).isEqualTo(DEFAULT_NET_PAY);
        assertThat(testEmployeeSalary.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testEmployeeSalary.getPfContribution()).isEqualTo(DEFAULT_PF_CONTRIBUTION);
        assertThat(testEmployeeSalary.getGfContribution()).isEqualTo(DEFAULT_GF_CONTRIBUTION);
        assertThat(testEmployeeSalary.getProvisionForFestivalBonus()).isEqualTo(DEFAULT_PROVISION_FOR_FESTIVAL_BONUS);
        assertThat(testEmployeeSalary.getProvisionForLeaveEncashment()).isEqualTo(DEFAULT_PROVISION_FOR_LEAVE_ENCASHMENT);
        assertThat(testEmployeeSalary.getIsFinalized()).isEqualTo(DEFAULT_IS_FINALIZED);
        assertThat(testEmployeeSalary.getIsDispatched()).isEqualTo(DEFAULT_IS_DISPATCHED);
        assertThat(testEmployeeSalary.getEntertainment()).isEqualTo(DEFAULT_ENTERTAINMENT);
        assertThat(testEmployeeSalary.getUtility()).isEqualTo(DEFAULT_UTILITY);
        assertThat(testEmployeeSalary.getOtherAddition()).isEqualTo(DEFAULT_OTHER_ADDITION);
        assertThat(testEmployeeSalary.getSalaryAdjustment()).isEqualTo(DEFAULT_SALARY_ADJUSTMENT);
        assertThat(testEmployeeSalary.getProvidentFundArrear()).isEqualTo(DEFAULT_PROVIDENT_FUND_ARREAR);
        assertThat(testEmployeeSalary.getAllowance01()).isEqualTo(DEFAULT_ALLOWANCE_01);
        assertThat(testEmployeeSalary.getAllowance02()).isEqualTo(DEFAULT_ALLOWANCE_02);
        assertThat(testEmployeeSalary.getAllowance03()).isEqualTo(DEFAULT_ALLOWANCE_03);
        assertThat(testEmployeeSalary.getAllowance04()).isEqualTo(DEFAULT_ALLOWANCE_04);
        assertThat(testEmployeeSalary.getAllowance05()).isEqualTo(DEFAULT_ALLOWANCE_05);
        assertThat(testEmployeeSalary.getAllowance06()).isEqualTo(DEFAULT_ALLOWANCE_06);
        assertThat(testEmployeeSalary.getProvisionForProjectBonus()).isEqualTo(DEFAULT_PROVISION_FOR_PROJECT_BONUS);
        assertThat(testEmployeeSalary.getIsHold()).isEqualTo(DEFAULT_IS_HOLD);
        assertThat(testEmployeeSalary.getAttendanceRegularisationStartDate()).isEqualTo(DEFAULT_ATTENDANCE_REGULARISATION_START_DATE);
        assertThat(testEmployeeSalary.getAttendanceRegularisationEndDate()).isEqualTo(DEFAULT_ATTENDANCE_REGULARISATION_END_DATE);
        assertThat(testEmployeeSalary.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEmployeeSalary.getIsVisibleToEmployee()).isEqualTo(DEFAULT_IS_VISIBLE_TO_EMPLOYEE);
        assertThat(testEmployeeSalary.getPfArrear()).isEqualTo(DEFAULT_PF_ARREAR);
        assertThat(testEmployeeSalary.getTaxCalculationSnapshot()).isEqualTo(DEFAULT_TAX_CALCULATION_SNAPSHOT);
    }

    @Test
    @Transactional
    void createEmployeeSalaryWithExistingId() throws Exception {
        // Create the EmployeeSalary with an existing ID
        employeeSalary.setId(1L);
        EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryMapper.toDto(employeeSalary);

        int databaseSizeBeforeCreate = employeeSalaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeSalaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSalary in the database
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAll();
        assertThat(employeeSalaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployeeSalaries() throws Exception {
        // Initialize the database
        employeeSalaryRepository.saveAndFlush(employeeSalary);

        // Get all the employeeSalaryList
        restEmployeeSalaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeSalary.getId().intValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].salaryGenerationDate").value(hasItem(DEFAULT_SALARY_GENERATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].refPin").value(hasItem(DEFAULT_REF_PIN)))
            .andExpect(jsonPath("$.[*].pin").value(hasItem(DEFAULT_PIN)))
            .andExpect(jsonPath("$.[*].joiningDate").value(hasItem(DEFAULT_JOINING_DATE.toString())))
            .andExpect(jsonPath("$.[*].confirmationDate").value(hasItem(DEFAULT_CONFIRMATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].employeeCategory").value(hasItem(DEFAULT_EMPLOYEE_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].mainGrossSalary").value(hasItem(DEFAULT_MAIN_GROSS_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].mainGrossBasicSalary").value(hasItem(DEFAULT_MAIN_GROSS_BASIC_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].mainGrossHouseRent").value(hasItem(DEFAULT_MAIN_GROSS_HOUSE_RENT.doubleValue())))
            .andExpect(jsonPath("$.[*].mainGrossMedicalAllowance").value(hasItem(DEFAULT_MAIN_GROSS_MEDICAL_ALLOWANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].mainGrossConveyanceAllowance").value(hasItem(DEFAULT_MAIN_GROSS_CONVEYANCE_ALLOWANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].absentDays").value(hasItem(DEFAULT_ABSENT_DAYS)))
            .andExpect(jsonPath("$.[*].fractionDays").value(hasItem(DEFAULT_FRACTION_DAYS)))
            .andExpect(jsonPath("$.[*].payableGrossSalary").value(hasItem(DEFAULT_PAYABLE_GROSS_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].payableGrossBasicSalary").value(hasItem(DEFAULT_PAYABLE_GROSS_BASIC_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].payableGrossHouseRent").value(hasItem(DEFAULT_PAYABLE_GROSS_HOUSE_RENT.doubleValue())))
            .andExpect(jsonPath("$.[*].payableGrossMedicalAllowance").value(hasItem(DEFAULT_PAYABLE_GROSS_MEDICAL_ALLOWANCE.doubleValue())))
            .andExpect(
                jsonPath("$.[*].payableGrossConveyanceAllowance").value(hasItem(DEFAULT_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE.doubleValue()))
            )
            .andExpect(jsonPath("$.[*].arrearSalary").value(hasItem(DEFAULT_ARREAR_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].pfDeduction").value(hasItem(DEFAULT_PF_DEDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].taxDeduction").value(hasItem(DEFAULT_TAX_DEDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].welfareFundDeduction").value(hasItem(DEFAULT_WELFARE_FUND_DEDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].mobileBillDeduction").value(hasItem(DEFAULT_MOBILE_BILL_DEDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].otherDeduction").value(hasItem(DEFAULT_OTHER_DEDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].totalDeduction").value(hasItem(DEFAULT_TOTAL_DEDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].netPay").value(hasItem(DEFAULT_NET_PAY.doubleValue())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].pfContribution").value(hasItem(DEFAULT_PF_CONTRIBUTION.doubleValue())))
            .andExpect(jsonPath("$.[*].gfContribution").value(hasItem(DEFAULT_GF_CONTRIBUTION.doubleValue())))
            .andExpect(jsonPath("$.[*].provisionForFestivalBonus").value(hasItem(DEFAULT_PROVISION_FOR_FESTIVAL_BONUS.doubleValue())))
            .andExpect(jsonPath("$.[*].provisionForLeaveEncashment").value(hasItem(DEFAULT_PROVISION_FOR_LEAVE_ENCASHMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].isFinalized").value(hasItem(DEFAULT_IS_FINALIZED.booleanValue())))
            .andExpect(jsonPath("$.[*].isDispatched").value(hasItem(DEFAULT_IS_DISPATCHED.booleanValue())))
            .andExpect(jsonPath("$.[*].entertainment").value(hasItem(DEFAULT_ENTERTAINMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].utility").value(hasItem(DEFAULT_UTILITY.doubleValue())))
            .andExpect(jsonPath("$.[*].otherAddition").value(hasItem(DEFAULT_OTHER_ADDITION.doubleValue())))
            .andExpect(jsonPath("$.[*].salaryAdjustment").value(hasItem(DEFAULT_SALARY_ADJUSTMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].providentFundArrear").value(hasItem(DEFAULT_PROVIDENT_FUND_ARREAR.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance01").value(hasItem(DEFAULT_ALLOWANCE_01.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance02").value(hasItem(DEFAULT_ALLOWANCE_02.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance03").value(hasItem(DEFAULT_ALLOWANCE_03.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance04").value(hasItem(DEFAULT_ALLOWANCE_04.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance05").value(hasItem(DEFAULT_ALLOWANCE_05.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance06").value(hasItem(DEFAULT_ALLOWANCE_06.doubleValue())))
            .andExpect(jsonPath("$.[*].provisionForProjectBonus").value(hasItem(DEFAULT_PROVISION_FOR_PROJECT_BONUS.doubleValue())))
            .andExpect(jsonPath("$.[*].isHold").value(hasItem(DEFAULT_IS_HOLD.booleanValue())))
            .andExpect(
                jsonPath("$.[*].attendanceRegularisationStartDate").value(hasItem(DEFAULT_ATTENDANCE_REGULARISATION_START_DATE.toString()))
            )
            .andExpect(
                jsonPath("$.[*].attendanceRegularisationEndDate").value(hasItem(DEFAULT_ATTENDANCE_REGULARISATION_END_DATE.toString()))
            )
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].isVisibleToEmployee").value(hasItem(DEFAULT_IS_VISIBLE_TO_EMPLOYEE.booleanValue())))
            .andExpect(jsonPath("$.[*].pfArrear").value(hasItem(DEFAULT_PF_ARREAR.doubleValue())))
            .andExpect(jsonPath("$.[*].taxCalculationSnapshot").value(hasItem(DEFAULT_TAX_CALCULATION_SNAPSHOT.toString())));
    }

    @Test
    @Transactional
    void getEmployeeSalary() throws Exception {
        // Initialize the database
        employeeSalaryRepository.saveAndFlush(employeeSalary);

        // Get the employeeSalary
        restEmployeeSalaryMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeSalary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeSalary.getId().intValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.salaryGenerationDate").value(DEFAULT_SALARY_GENERATION_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.refPin").value(DEFAULT_REF_PIN))
            .andExpect(jsonPath("$.pin").value(DEFAULT_PIN))
            .andExpect(jsonPath("$.joiningDate").value(DEFAULT_JOINING_DATE.toString()))
            .andExpect(jsonPath("$.confirmationDate").value(DEFAULT_CONFIRMATION_DATE.toString()))
            .andExpect(jsonPath("$.employeeCategory").value(DEFAULT_EMPLOYEE_CATEGORY.toString()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.mainGrossSalary").value(DEFAULT_MAIN_GROSS_SALARY.doubleValue()))
            .andExpect(jsonPath("$.mainGrossBasicSalary").value(DEFAULT_MAIN_GROSS_BASIC_SALARY.doubleValue()))
            .andExpect(jsonPath("$.mainGrossHouseRent").value(DEFAULT_MAIN_GROSS_HOUSE_RENT.doubleValue()))
            .andExpect(jsonPath("$.mainGrossMedicalAllowance").value(DEFAULT_MAIN_GROSS_MEDICAL_ALLOWANCE.doubleValue()))
            .andExpect(jsonPath("$.mainGrossConveyanceAllowance").value(DEFAULT_MAIN_GROSS_CONVEYANCE_ALLOWANCE.doubleValue()))
            .andExpect(jsonPath("$.absentDays").value(DEFAULT_ABSENT_DAYS))
            .andExpect(jsonPath("$.fractionDays").value(DEFAULT_FRACTION_DAYS))
            .andExpect(jsonPath("$.payableGrossSalary").value(DEFAULT_PAYABLE_GROSS_SALARY.doubleValue()))
            .andExpect(jsonPath("$.payableGrossBasicSalary").value(DEFAULT_PAYABLE_GROSS_BASIC_SALARY.doubleValue()))
            .andExpect(jsonPath("$.payableGrossHouseRent").value(DEFAULT_PAYABLE_GROSS_HOUSE_RENT.doubleValue()))
            .andExpect(jsonPath("$.payableGrossMedicalAllowance").value(DEFAULT_PAYABLE_GROSS_MEDICAL_ALLOWANCE.doubleValue()))
            .andExpect(jsonPath("$.payableGrossConveyanceAllowance").value(DEFAULT_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE.doubleValue()))
            .andExpect(jsonPath("$.arrearSalary").value(DEFAULT_ARREAR_SALARY.doubleValue()))
            .andExpect(jsonPath("$.pfDeduction").value(DEFAULT_PF_DEDUCTION.doubleValue()))
            .andExpect(jsonPath("$.taxDeduction").value(DEFAULT_TAX_DEDUCTION.doubleValue()))
            .andExpect(jsonPath("$.welfareFundDeduction").value(DEFAULT_WELFARE_FUND_DEDUCTION.doubleValue()))
            .andExpect(jsonPath("$.mobileBillDeduction").value(DEFAULT_MOBILE_BILL_DEDUCTION.doubleValue()))
            .andExpect(jsonPath("$.otherDeduction").value(DEFAULT_OTHER_DEDUCTION.doubleValue()))
            .andExpect(jsonPath("$.totalDeduction").value(DEFAULT_TOTAL_DEDUCTION.doubleValue()))
            .andExpect(jsonPath("$.netPay").value(DEFAULT_NET_PAY.doubleValue()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.pfContribution").value(DEFAULT_PF_CONTRIBUTION.doubleValue()))
            .andExpect(jsonPath("$.gfContribution").value(DEFAULT_GF_CONTRIBUTION.doubleValue()))
            .andExpect(jsonPath("$.provisionForFestivalBonus").value(DEFAULT_PROVISION_FOR_FESTIVAL_BONUS.doubleValue()))
            .andExpect(jsonPath("$.provisionForLeaveEncashment").value(DEFAULT_PROVISION_FOR_LEAVE_ENCASHMENT.doubleValue()))
            .andExpect(jsonPath("$.isFinalized").value(DEFAULT_IS_FINALIZED.booleanValue()))
            .andExpect(jsonPath("$.isDispatched").value(DEFAULT_IS_DISPATCHED.booleanValue()))
            .andExpect(jsonPath("$.entertainment").value(DEFAULT_ENTERTAINMENT.doubleValue()))
            .andExpect(jsonPath("$.utility").value(DEFAULT_UTILITY.doubleValue()))
            .andExpect(jsonPath("$.otherAddition").value(DEFAULT_OTHER_ADDITION.doubleValue()))
            .andExpect(jsonPath("$.salaryAdjustment").value(DEFAULT_SALARY_ADJUSTMENT.doubleValue()))
            .andExpect(jsonPath("$.providentFundArrear").value(DEFAULT_PROVIDENT_FUND_ARREAR.doubleValue()))
            .andExpect(jsonPath("$.allowance01").value(DEFAULT_ALLOWANCE_01.doubleValue()))
            .andExpect(jsonPath("$.allowance02").value(DEFAULT_ALLOWANCE_02.doubleValue()))
            .andExpect(jsonPath("$.allowance03").value(DEFAULT_ALLOWANCE_03.doubleValue()))
            .andExpect(jsonPath("$.allowance04").value(DEFAULT_ALLOWANCE_04.doubleValue()))
            .andExpect(jsonPath("$.allowance05").value(DEFAULT_ALLOWANCE_05.doubleValue()))
            .andExpect(jsonPath("$.allowance06").value(DEFAULT_ALLOWANCE_06.doubleValue()))
            .andExpect(jsonPath("$.provisionForProjectBonus").value(DEFAULT_PROVISION_FOR_PROJECT_BONUS.doubleValue()))
            .andExpect(jsonPath("$.isHold").value(DEFAULT_IS_HOLD.booleanValue()))
            .andExpect(jsonPath("$.attendanceRegularisationStartDate").value(DEFAULT_ATTENDANCE_REGULARISATION_START_DATE.toString()))
            .andExpect(jsonPath("$.attendanceRegularisationEndDate").value(DEFAULT_ATTENDANCE_REGULARISATION_END_DATE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.isVisibleToEmployee").value(DEFAULT_IS_VISIBLE_TO_EMPLOYEE.booleanValue()))
            .andExpect(jsonPath("$.pfArrear").value(DEFAULT_PF_ARREAR.doubleValue()))
            .andExpect(jsonPath("$.taxCalculationSnapshot").value(DEFAULT_TAX_CALCULATION_SNAPSHOT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeSalary() throws Exception {
        // Get the employeeSalary
        restEmployeeSalaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeSalary() throws Exception {
        // Initialize the database
        employeeSalaryRepository.saveAndFlush(employeeSalary);

        int databaseSizeBeforeUpdate = employeeSalaryRepository.findAll().size();

        // Update the employeeSalary
        EmployeeSalary updatedEmployeeSalary = employeeSalaryRepository.findById(employeeSalary.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeSalary are not directly saved in db
        em.detach(updatedEmployeeSalary);
        updatedEmployeeSalary
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .salaryGenerationDate(UPDATED_SALARY_GENERATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .refPin(UPDATED_REF_PIN)
            .pin(UPDATED_PIN)
            .joiningDate(UPDATED_JOINING_DATE)
            .confirmationDate(UPDATED_CONFIRMATION_DATE)
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .unit(UPDATED_UNIT)
            .department(UPDATED_DEPARTMENT)
            .mainGrossSalary(UPDATED_MAIN_GROSS_SALARY)
            .mainGrossBasicSalary(UPDATED_MAIN_GROSS_BASIC_SALARY)
            .mainGrossHouseRent(UPDATED_MAIN_GROSS_HOUSE_RENT)
            .mainGrossMedicalAllowance(UPDATED_MAIN_GROSS_MEDICAL_ALLOWANCE)
            .mainGrossConveyanceAllowance(UPDATED_MAIN_GROSS_CONVEYANCE_ALLOWANCE)
            .absentDays(UPDATED_ABSENT_DAYS)
            .fractionDays(UPDATED_FRACTION_DAYS)
            .payableGrossSalary(UPDATED_PAYABLE_GROSS_SALARY)
            .payableGrossBasicSalary(UPDATED_PAYABLE_GROSS_BASIC_SALARY)
            .payableGrossHouseRent(UPDATED_PAYABLE_GROSS_HOUSE_RENT)
            .payableGrossMedicalAllowance(UPDATED_PAYABLE_GROSS_MEDICAL_ALLOWANCE)
            .payableGrossConveyanceAllowance(UPDATED_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE)
            .arrearSalary(UPDATED_ARREAR_SALARY)
            .pfDeduction(UPDATED_PF_DEDUCTION)
            .taxDeduction(UPDATED_TAX_DEDUCTION)
            .welfareFundDeduction(UPDATED_WELFARE_FUND_DEDUCTION)
            .mobileBillDeduction(UPDATED_MOBILE_BILL_DEDUCTION)
            .otherDeduction(UPDATED_OTHER_DEDUCTION)
            .totalDeduction(UPDATED_TOTAL_DEDUCTION)
            .netPay(UPDATED_NET_PAY)
            .remarks(UPDATED_REMARKS)
            .pfContribution(UPDATED_PF_CONTRIBUTION)
            .gfContribution(UPDATED_GF_CONTRIBUTION)
            .provisionForFestivalBonus(UPDATED_PROVISION_FOR_FESTIVAL_BONUS)
            .provisionForLeaveEncashment(UPDATED_PROVISION_FOR_LEAVE_ENCASHMENT)
            .isFinalized(UPDATED_IS_FINALIZED)
            .isDispatched(UPDATED_IS_DISPATCHED)
            .entertainment(UPDATED_ENTERTAINMENT)
            .utility(UPDATED_UTILITY)
            .otherAddition(UPDATED_OTHER_ADDITION)
            .salaryAdjustment(UPDATED_SALARY_ADJUSTMENT)
            .providentFundArrear(UPDATED_PROVIDENT_FUND_ARREAR)
            .allowance01(UPDATED_ALLOWANCE_01)
            .allowance02(UPDATED_ALLOWANCE_02)
            .allowance03(UPDATED_ALLOWANCE_03)
            .allowance04(UPDATED_ALLOWANCE_04)
            .allowance05(UPDATED_ALLOWANCE_05)
            .allowance06(UPDATED_ALLOWANCE_06)
            .provisionForProjectBonus(UPDATED_PROVISION_FOR_PROJECT_BONUS)
            .isHold(UPDATED_IS_HOLD)
            .attendanceRegularisationStartDate(UPDATED_ATTENDANCE_REGULARISATION_START_DATE)
            .attendanceRegularisationEndDate(UPDATED_ATTENDANCE_REGULARISATION_END_DATE)
            .title(UPDATED_TITLE)
            .isVisibleToEmployee(UPDATED_IS_VISIBLE_TO_EMPLOYEE)
            .pfArrear(UPDATED_PF_ARREAR)
            .taxCalculationSnapshot(UPDATED_TAX_CALCULATION_SNAPSHOT);
        EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryMapper.toDto(updatedEmployeeSalary);

        restEmployeeSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeSalaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeSalary in the database
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAll();
        assertThat(employeeSalaryList).hasSize(databaseSizeBeforeUpdate);
        EmployeeSalary testEmployeeSalary = employeeSalaryList.get(employeeSalaryList.size() - 1);
        assertThat(testEmployeeSalary.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testEmployeeSalary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testEmployeeSalary.getSalaryGenerationDate()).isEqualTo(UPDATED_SALARY_GENERATION_DATE);
        assertThat(testEmployeeSalary.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmployeeSalary.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeeSalary.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testEmployeeSalary.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmployeeSalary.getRefPin()).isEqualTo(UPDATED_REF_PIN);
        assertThat(testEmployeeSalary.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testEmployeeSalary.getJoiningDate()).isEqualTo(UPDATED_JOINING_DATE);
        assertThat(testEmployeeSalary.getConfirmationDate()).isEqualTo(UPDATED_CONFIRMATION_DATE);
        assertThat(testEmployeeSalary.getEmployeeCategory()).isEqualTo(UPDATED_EMPLOYEE_CATEGORY);
        assertThat(testEmployeeSalary.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testEmployeeSalary.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testEmployeeSalary.getMainGrossSalary()).isEqualTo(UPDATED_MAIN_GROSS_SALARY);
        assertThat(testEmployeeSalary.getMainGrossBasicSalary()).isEqualTo(UPDATED_MAIN_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalary.getMainGrossHouseRent()).isEqualTo(UPDATED_MAIN_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalary.getMainGrossMedicalAllowance()).isEqualTo(UPDATED_MAIN_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalary.getMainGrossConveyanceAllowance()).isEqualTo(UPDATED_MAIN_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalary.getAbsentDays()).isEqualTo(UPDATED_ABSENT_DAYS);
        assertThat(testEmployeeSalary.getFractionDays()).isEqualTo(UPDATED_FRACTION_DAYS);
        assertThat(testEmployeeSalary.getPayableGrossSalary()).isEqualTo(UPDATED_PAYABLE_GROSS_SALARY);
        assertThat(testEmployeeSalary.getPayableGrossBasicSalary()).isEqualTo(UPDATED_PAYABLE_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalary.getPayableGrossHouseRent()).isEqualTo(UPDATED_PAYABLE_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalary.getPayableGrossMedicalAllowance()).isEqualTo(UPDATED_PAYABLE_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalary.getPayableGrossConveyanceAllowance()).isEqualTo(UPDATED_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalary.getArrearSalary()).isEqualTo(UPDATED_ARREAR_SALARY);
        assertThat(testEmployeeSalary.getPfDeduction()).isEqualTo(UPDATED_PF_DEDUCTION);
        assertThat(testEmployeeSalary.getTaxDeduction()).isEqualTo(UPDATED_TAX_DEDUCTION);
        assertThat(testEmployeeSalary.getWelfareFundDeduction()).isEqualTo(UPDATED_WELFARE_FUND_DEDUCTION);
        assertThat(testEmployeeSalary.getMobileBillDeduction()).isEqualTo(UPDATED_MOBILE_BILL_DEDUCTION);
        assertThat(testEmployeeSalary.getOtherDeduction()).isEqualTo(UPDATED_OTHER_DEDUCTION);
        assertThat(testEmployeeSalary.getTotalDeduction()).isEqualTo(UPDATED_TOTAL_DEDUCTION);
        assertThat(testEmployeeSalary.getNetPay()).isEqualTo(UPDATED_NET_PAY);
        assertThat(testEmployeeSalary.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testEmployeeSalary.getPfContribution()).isEqualTo(UPDATED_PF_CONTRIBUTION);
        assertThat(testEmployeeSalary.getGfContribution()).isEqualTo(UPDATED_GF_CONTRIBUTION);
        assertThat(testEmployeeSalary.getProvisionForFestivalBonus()).isEqualTo(UPDATED_PROVISION_FOR_FESTIVAL_BONUS);
        assertThat(testEmployeeSalary.getProvisionForLeaveEncashment()).isEqualTo(UPDATED_PROVISION_FOR_LEAVE_ENCASHMENT);
        assertThat(testEmployeeSalary.getIsFinalized()).isEqualTo(UPDATED_IS_FINALIZED);
        assertThat(testEmployeeSalary.getIsDispatched()).isEqualTo(UPDATED_IS_DISPATCHED);
        assertThat(testEmployeeSalary.getEntertainment()).isEqualTo(UPDATED_ENTERTAINMENT);
        assertThat(testEmployeeSalary.getUtility()).isEqualTo(UPDATED_UTILITY);
        assertThat(testEmployeeSalary.getOtherAddition()).isEqualTo(UPDATED_OTHER_ADDITION);
        assertThat(testEmployeeSalary.getSalaryAdjustment()).isEqualTo(UPDATED_SALARY_ADJUSTMENT);
        assertThat(testEmployeeSalary.getProvidentFundArrear()).isEqualTo(UPDATED_PROVIDENT_FUND_ARREAR);
        assertThat(testEmployeeSalary.getAllowance01()).isEqualTo(UPDATED_ALLOWANCE_01);
        assertThat(testEmployeeSalary.getAllowance02()).isEqualTo(UPDATED_ALLOWANCE_02);
        assertThat(testEmployeeSalary.getAllowance03()).isEqualTo(UPDATED_ALLOWANCE_03);
        assertThat(testEmployeeSalary.getAllowance04()).isEqualTo(UPDATED_ALLOWANCE_04);
        assertThat(testEmployeeSalary.getAllowance05()).isEqualTo(UPDATED_ALLOWANCE_05);
        assertThat(testEmployeeSalary.getAllowance06()).isEqualTo(UPDATED_ALLOWANCE_06);
        assertThat(testEmployeeSalary.getProvisionForProjectBonus()).isEqualTo(UPDATED_PROVISION_FOR_PROJECT_BONUS);
        assertThat(testEmployeeSalary.getIsHold()).isEqualTo(UPDATED_IS_HOLD);
        assertThat(testEmployeeSalary.getAttendanceRegularisationStartDate()).isEqualTo(UPDATED_ATTENDANCE_REGULARISATION_START_DATE);
        assertThat(testEmployeeSalary.getAttendanceRegularisationEndDate()).isEqualTo(UPDATED_ATTENDANCE_REGULARISATION_END_DATE);
        assertThat(testEmployeeSalary.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEmployeeSalary.getIsVisibleToEmployee()).isEqualTo(UPDATED_IS_VISIBLE_TO_EMPLOYEE);
        assertThat(testEmployeeSalary.getPfArrear()).isEqualTo(UPDATED_PF_ARREAR);
        assertThat(testEmployeeSalary.getTaxCalculationSnapshot()).isEqualTo(UPDATED_TAX_CALCULATION_SNAPSHOT);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeSalary() throws Exception {
        int databaseSizeBeforeUpdate = employeeSalaryRepository.findAll().size();
        employeeSalary.setId(count.incrementAndGet());

        // Create the EmployeeSalary
        EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryMapper.toDto(employeeSalary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeSalaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSalary in the database
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAll();
        assertThat(employeeSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeSalary() throws Exception {
        int databaseSizeBeforeUpdate = employeeSalaryRepository.findAll().size();
        employeeSalary.setId(count.incrementAndGet());

        // Create the EmployeeSalary
        EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryMapper.toDto(employeeSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSalary in the database
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAll();
        assertThat(employeeSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeSalary() throws Exception {
        int databaseSizeBeforeUpdate = employeeSalaryRepository.findAll().size();
        employeeSalary.setId(count.incrementAndGet());

        // Create the EmployeeSalary
        EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryMapper.toDto(employeeSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeSalaryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeSalaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeSalary in the database
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAll();
        assertThat(employeeSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeSalaryWithPatch() throws Exception {
        // Initialize the database
        employeeSalaryRepository.saveAndFlush(employeeSalary);

        int databaseSizeBeforeUpdate = employeeSalaryRepository.findAll().size();

        // Update the employeeSalary using partial update
        EmployeeSalary partialUpdatedEmployeeSalary = new EmployeeSalary();
        partialUpdatedEmployeeSalary.setId(employeeSalary.getId());

        partialUpdatedEmployeeSalary
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .salaryGenerationDate(UPDATED_SALARY_GENERATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .pin(UPDATED_PIN)
            .confirmationDate(UPDATED_CONFIRMATION_DATE)
            .mainGrossSalary(UPDATED_MAIN_GROSS_SALARY)
            .mainGrossHouseRent(UPDATED_MAIN_GROSS_HOUSE_RENT)
            .mainGrossMedicalAllowance(UPDATED_MAIN_GROSS_MEDICAL_ALLOWANCE)
            .fractionDays(UPDATED_FRACTION_DAYS)
            .payableGrossSalary(UPDATED_PAYABLE_GROSS_SALARY)
            .payableGrossHouseRent(UPDATED_PAYABLE_GROSS_HOUSE_RENT)
            .payableGrossMedicalAllowance(UPDATED_PAYABLE_GROSS_MEDICAL_ALLOWANCE)
            .payableGrossConveyanceAllowance(UPDATED_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE)
            .otherDeduction(UPDATED_OTHER_DEDUCTION)
            .totalDeduction(UPDATED_TOTAL_DEDUCTION)
            .pfContribution(UPDATED_PF_CONTRIBUTION)
            .utility(UPDATED_UTILITY)
            .allowance02(UPDATED_ALLOWANCE_02)
            .allowance04(UPDATED_ALLOWANCE_04)
            .provisionForProjectBonus(UPDATED_PROVISION_FOR_PROJECT_BONUS)
            .attendanceRegularisationEndDate(UPDATED_ATTENDANCE_REGULARISATION_END_DATE)
            .title(UPDATED_TITLE)
            .taxCalculationSnapshot(UPDATED_TAX_CALCULATION_SNAPSHOT);

        restEmployeeSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeSalary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeSalary))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeSalary in the database
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAll();
        assertThat(employeeSalaryList).hasSize(databaseSizeBeforeUpdate);
        EmployeeSalary testEmployeeSalary = employeeSalaryList.get(employeeSalaryList.size() - 1);
        assertThat(testEmployeeSalary.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testEmployeeSalary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testEmployeeSalary.getSalaryGenerationDate()).isEqualTo(UPDATED_SALARY_GENERATION_DATE);
        assertThat(testEmployeeSalary.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmployeeSalary.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEmployeeSalary.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testEmployeeSalary.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testEmployeeSalary.getRefPin()).isEqualTo(DEFAULT_REF_PIN);
        assertThat(testEmployeeSalary.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testEmployeeSalary.getJoiningDate()).isEqualTo(DEFAULT_JOINING_DATE);
        assertThat(testEmployeeSalary.getConfirmationDate()).isEqualTo(UPDATED_CONFIRMATION_DATE);
        assertThat(testEmployeeSalary.getEmployeeCategory()).isEqualTo(DEFAULT_EMPLOYEE_CATEGORY);
        assertThat(testEmployeeSalary.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testEmployeeSalary.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testEmployeeSalary.getMainGrossSalary()).isEqualTo(UPDATED_MAIN_GROSS_SALARY);
        assertThat(testEmployeeSalary.getMainGrossBasicSalary()).isEqualTo(DEFAULT_MAIN_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalary.getMainGrossHouseRent()).isEqualTo(UPDATED_MAIN_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalary.getMainGrossMedicalAllowance()).isEqualTo(UPDATED_MAIN_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalary.getMainGrossConveyanceAllowance()).isEqualTo(DEFAULT_MAIN_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalary.getAbsentDays()).isEqualTo(DEFAULT_ABSENT_DAYS);
        assertThat(testEmployeeSalary.getFractionDays()).isEqualTo(UPDATED_FRACTION_DAYS);
        assertThat(testEmployeeSalary.getPayableGrossSalary()).isEqualTo(UPDATED_PAYABLE_GROSS_SALARY);
        assertThat(testEmployeeSalary.getPayableGrossBasicSalary()).isEqualTo(DEFAULT_PAYABLE_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalary.getPayableGrossHouseRent()).isEqualTo(UPDATED_PAYABLE_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalary.getPayableGrossMedicalAllowance()).isEqualTo(UPDATED_PAYABLE_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalary.getPayableGrossConveyanceAllowance()).isEqualTo(UPDATED_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalary.getArrearSalary()).isEqualTo(DEFAULT_ARREAR_SALARY);
        assertThat(testEmployeeSalary.getPfDeduction()).isEqualTo(DEFAULT_PF_DEDUCTION);
        assertThat(testEmployeeSalary.getTaxDeduction()).isEqualTo(DEFAULT_TAX_DEDUCTION);
        assertThat(testEmployeeSalary.getWelfareFundDeduction()).isEqualTo(DEFAULT_WELFARE_FUND_DEDUCTION);
        assertThat(testEmployeeSalary.getMobileBillDeduction()).isEqualTo(DEFAULT_MOBILE_BILL_DEDUCTION);
        assertThat(testEmployeeSalary.getOtherDeduction()).isEqualTo(UPDATED_OTHER_DEDUCTION);
        assertThat(testEmployeeSalary.getTotalDeduction()).isEqualTo(UPDATED_TOTAL_DEDUCTION);
        assertThat(testEmployeeSalary.getNetPay()).isEqualTo(DEFAULT_NET_PAY);
        assertThat(testEmployeeSalary.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testEmployeeSalary.getPfContribution()).isEqualTo(UPDATED_PF_CONTRIBUTION);
        assertThat(testEmployeeSalary.getGfContribution()).isEqualTo(DEFAULT_GF_CONTRIBUTION);
        assertThat(testEmployeeSalary.getProvisionForFestivalBonus()).isEqualTo(DEFAULT_PROVISION_FOR_FESTIVAL_BONUS);
        assertThat(testEmployeeSalary.getProvisionForLeaveEncashment()).isEqualTo(DEFAULT_PROVISION_FOR_LEAVE_ENCASHMENT);
        assertThat(testEmployeeSalary.getIsFinalized()).isEqualTo(DEFAULT_IS_FINALIZED);
        assertThat(testEmployeeSalary.getIsDispatched()).isEqualTo(DEFAULT_IS_DISPATCHED);
        assertThat(testEmployeeSalary.getEntertainment()).isEqualTo(DEFAULT_ENTERTAINMENT);
        assertThat(testEmployeeSalary.getUtility()).isEqualTo(UPDATED_UTILITY);
        assertThat(testEmployeeSalary.getOtherAddition()).isEqualTo(DEFAULT_OTHER_ADDITION);
        assertThat(testEmployeeSalary.getSalaryAdjustment()).isEqualTo(DEFAULT_SALARY_ADJUSTMENT);
        assertThat(testEmployeeSalary.getProvidentFundArrear()).isEqualTo(DEFAULT_PROVIDENT_FUND_ARREAR);
        assertThat(testEmployeeSalary.getAllowance01()).isEqualTo(DEFAULT_ALLOWANCE_01);
        assertThat(testEmployeeSalary.getAllowance02()).isEqualTo(UPDATED_ALLOWANCE_02);
        assertThat(testEmployeeSalary.getAllowance03()).isEqualTo(DEFAULT_ALLOWANCE_03);
        assertThat(testEmployeeSalary.getAllowance04()).isEqualTo(UPDATED_ALLOWANCE_04);
        assertThat(testEmployeeSalary.getAllowance05()).isEqualTo(DEFAULT_ALLOWANCE_05);
        assertThat(testEmployeeSalary.getAllowance06()).isEqualTo(DEFAULT_ALLOWANCE_06);
        assertThat(testEmployeeSalary.getProvisionForProjectBonus()).isEqualTo(UPDATED_PROVISION_FOR_PROJECT_BONUS);
        assertThat(testEmployeeSalary.getIsHold()).isEqualTo(DEFAULT_IS_HOLD);
        assertThat(testEmployeeSalary.getAttendanceRegularisationStartDate()).isEqualTo(DEFAULT_ATTENDANCE_REGULARISATION_START_DATE);
        assertThat(testEmployeeSalary.getAttendanceRegularisationEndDate()).isEqualTo(UPDATED_ATTENDANCE_REGULARISATION_END_DATE);
        assertThat(testEmployeeSalary.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEmployeeSalary.getIsVisibleToEmployee()).isEqualTo(DEFAULT_IS_VISIBLE_TO_EMPLOYEE);
        assertThat(testEmployeeSalary.getPfArrear()).isEqualTo(DEFAULT_PF_ARREAR);
        assertThat(testEmployeeSalary.getTaxCalculationSnapshot()).isEqualTo(UPDATED_TAX_CALCULATION_SNAPSHOT);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeSalaryWithPatch() throws Exception {
        // Initialize the database
        employeeSalaryRepository.saveAndFlush(employeeSalary);

        int databaseSizeBeforeUpdate = employeeSalaryRepository.findAll().size();

        // Update the employeeSalary using partial update
        EmployeeSalary partialUpdatedEmployeeSalary = new EmployeeSalary();
        partialUpdatedEmployeeSalary.setId(employeeSalary.getId());

        partialUpdatedEmployeeSalary
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .salaryGenerationDate(UPDATED_SALARY_GENERATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .refPin(UPDATED_REF_PIN)
            .pin(UPDATED_PIN)
            .joiningDate(UPDATED_JOINING_DATE)
            .confirmationDate(UPDATED_CONFIRMATION_DATE)
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .unit(UPDATED_UNIT)
            .department(UPDATED_DEPARTMENT)
            .mainGrossSalary(UPDATED_MAIN_GROSS_SALARY)
            .mainGrossBasicSalary(UPDATED_MAIN_GROSS_BASIC_SALARY)
            .mainGrossHouseRent(UPDATED_MAIN_GROSS_HOUSE_RENT)
            .mainGrossMedicalAllowance(UPDATED_MAIN_GROSS_MEDICAL_ALLOWANCE)
            .mainGrossConveyanceAllowance(UPDATED_MAIN_GROSS_CONVEYANCE_ALLOWANCE)
            .absentDays(UPDATED_ABSENT_DAYS)
            .fractionDays(UPDATED_FRACTION_DAYS)
            .payableGrossSalary(UPDATED_PAYABLE_GROSS_SALARY)
            .payableGrossBasicSalary(UPDATED_PAYABLE_GROSS_BASIC_SALARY)
            .payableGrossHouseRent(UPDATED_PAYABLE_GROSS_HOUSE_RENT)
            .payableGrossMedicalAllowance(UPDATED_PAYABLE_GROSS_MEDICAL_ALLOWANCE)
            .payableGrossConveyanceAllowance(UPDATED_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE)
            .arrearSalary(UPDATED_ARREAR_SALARY)
            .pfDeduction(UPDATED_PF_DEDUCTION)
            .taxDeduction(UPDATED_TAX_DEDUCTION)
            .welfareFundDeduction(UPDATED_WELFARE_FUND_DEDUCTION)
            .mobileBillDeduction(UPDATED_MOBILE_BILL_DEDUCTION)
            .otherDeduction(UPDATED_OTHER_DEDUCTION)
            .totalDeduction(UPDATED_TOTAL_DEDUCTION)
            .netPay(UPDATED_NET_PAY)
            .remarks(UPDATED_REMARKS)
            .pfContribution(UPDATED_PF_CONTRIBUTION)
            .gfContribution(UPDATED_GF_CONTRIBUTION)
            .provisionForFestivalBonus(UPDATED_PROVISION_FOR_FESTIVAL_BONUS)
            .provisionForLeaveEncashment(UPDATED_PROVISION_FOR_LEAVE_ENCASHMENT)
            .isFinalized(UPDATED_IS_FINALIZED)
            .isDispatched(UPDATED_IS_DISPATCHED)
            .entertainment(UPDATED_ENTERTAINMENT)
            .utility(UPDATED_UTILITY)
            .otherAddition(UPDATED_OTHER_ADDITION)
            .salaryAdjustment(UPDATED_SALARY_ADJUSTMENT)
            .providentFundArrear(UPDATED_PROVIDENT_FUND_ARREAR)
            .allowance01(UPDATED_ALLOWANCE_01)
            .allowance02(UPDATED_ALLOWANCE_02)
            .allowance03(UPDATED_ALLOWANCE_03)
            .allowance04(UPDATED_ALLOWANCE_04)
            .allowance05(UPDATED_ALLOWANCE_05)
            .allowance06(UPDATED_ALLOWANCE_06)
            .provisionForProjectBonus(UPDATED_PROVISION_FOR_PROJECT_BONUS)
            .isHold(UPDATED_IS_HOLD)
            .attendanceRegularisationStartDate(UPDATED_ATTENDANCE_REGULARISATION_START_DATE)
            .attendanceRegularisationEndDate(UPDATED_ATTENDANCE_REGULARISATION_END_DATE)
            .title(UPDATED_TITLE)
            .isVisibleToEmployee(UPDATED_IS_VISIBLE_TO_EMPLOYEE)
            .pfArrear(UPDATED_PF_ARREAR)
            .taxCalculationSnapshot(UPDATED_TAX_CALCULATION_SNAPSHOT);

        restEmployeeSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeSalary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeSalary))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeSalary in the database
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAll();
        assertThat(employeeSalaryList).hasSize(databaseSizeBeforeUpdate);
        EmployeeSalary testEmployeeSalary = employeeSalaryList.get(employeeSalaryList.size() - 1);
        assertThat(testEmployeeSalary.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testEmployeeSalary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testEmployeeSalary.getSalaryGenerationDate()).isEqualTo(UPDATED_SALARY_GENERATION_DATE);
        assertThat(testEmployeeSalary.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmployeeSalary.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeeSalary.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testEmployeeSalary.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmployeeSalary.getRefPin()).isEqualTo(UPDATED_REF_PIN);
        assertThat(testEmployeeSalary.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testEmployeeSalary.getJoiningDate()).isEqualTo(UPDATED_JOINING_DATE);
        assertThat(testEmployeeSalary.getConfirmationDate()).isEqualTo(UPDATED_CONFIRMATION_DATE);
        assertThat(testEmployeeSalary.getEmployeeCategory()).isEqualTo(UPDATED_EMPLOYEE_CATEGORY);
        assertThat(testEmployeeSalary.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testEmployeeSalary.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testEmployeeSalary.getMainGrossSalary()).isEqualTo(UPDATED_MAIN_GROSS_SALARY);
        assertThat(testEmployeeSalary.getMainGrossBasicSalary()).isEqualTo(UPDATED_MAIN_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalary.getMainGrossHouseRent()).isEqualTo(UPDATED_MAIN_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalary.getMainGrossMedicalAllowance()).isEqualTo(UPDATED_MAIN_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalary.getMainGrossConveyanceAllowance()).isEqualTo(UPDATED_MAIN_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalary.getAbsentDays()).isEqualTo(UPDATED_ABSENT_DAYS);
        assertThat(testEmployeeSalary.getFractionDays()).isEqualTo(UPDATED_FRACTION_DAYS);
        assertThat(testEmployeeSalary.getPayableGrossSalary()).isEqualTo(UPDATED_PAYABLE_GROSS_SALARY);
        assertThat(testEmployeeSalary.getPayableGrossBasicSalary()).isEqualTo(UPDATED_PAYABLE_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalary.getPayableGrossHouseRent()).isEqualTo(UPDATED_PAYABLE_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalary.getPayableGrossMedicalAllowance()).isEqualTo(UPDATED_PAYABLE_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalary.getPayableGrossConveyanceAllowance()).isEqualTo(UPDATED_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalary.getArrearSalary()).isEqualTo(UPDATED_ARREAR_SALARY);
        assertThat(testEmployeeSalary.getPfDeduction()).isEqualTo(UPDATED_PF_DEDUCTION);
        assertThat(testEmployeeSalary.getTaxDeduction()).isEqualTo(UPDATED_TAX_DEDUCTION);
        assertThat(testEmployeeSalary.getWelfareFundDeduction()).isEqualTo(UPDATED_WELFARE_FUND_DEDUCTION);
        assertThat(testEmployeeSalary.getMobileBillDeduction()).isEqualTo(UPDATED_MOBILE_BILL_DEDUCTION);
        assertThat(testEmployeeSalary.getOtherDeduction()).isEqualTo(UPDATED_OTHER_DEDUCTION);
        assertThat(testEmployeeSalary.getTotalDeduction()).isEqualTo(UPDATED_TOTAL_DEDUCTION);
        assertThat(testEmployeeSalary.getNetPay()).isEqualTo(UPDATED_NET_PAY);
        assertThat(testEmployeeSalary.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testEmployeeSalary.getPfContribution()).isEqualTo(UPDATED_PF_CONTRIBUTION);
        assertThat(testEmployeeSalary.getGfContribution()).isEqualTo(UPDATED_GF_CONTRIBUTION);
        assertThat(testEmployeeSalary.getProvisionForFestivalBonus()).isEqualTo(UPDATED_PROVISION_FOR_FESTIVAL_BONUS);
        assertThat(testEmployeeSalary.getProvisionForLeaveEncashment()).isEqualTo(UPDATED_PROVISION_FOR_LEAVE_ENCASHMENT);
        assertThat(testEmployeeSalary.getIsFinalized()).isEqualTo(UPDATED_IS_FINALIZED);
        assertThat(testEmployeeSalary.getIsDispatched()).isEqualTo(UPDATED_IS_DISPATCHED);
        assertThat(testEmployeeSalary.getEntertainment()).isEqualTo(UPDATED_ENTERTAINMENT);
        assertThat(testEmployeeSalary.getUtility()).isEqualTo(UPDATED_UTILITY);
        assertThat(testEmployeeSalary.getOtherAddition()).isEqualTo(UPDATED_OTHER_ADDITION);
        assertThat(testEmployeeSalary.getSalaryAdjustment()).isEqualTo(UPDATED_SALARY_ADJUSTMENT);
        assertThat(testEmployeeSalary.getProvidentFundArrear()).isEqualTo(UPDATED_PROVIDENT_FUND_ARREAR);
        assertThat(testEmployeeSalary.getAllowance01()).isEqualTo(UPDATED_ALLOWANCE_01);
        assertThat(testEmployeeSalary.getAllowance02()).isEqualTo(UPDATED_ALLOWANCE_02);
        assertThat(testEmployeeSalary.getAllowance03()).isEqualTo(UPDATED_ALLOWANCE_03);
        assertThat(testEmployeeSalary.getAllowance04()).isEqualTo(UPDATED_ALLOWANCE_04);
        assertThat(testEmployeeSalary.getAllowance05()).isEqualTo(UPDATED_ALLOWANCE_05);
        assertThat(testEmployeeSalary.getAllowance06()).isEqualTo(UPDATED_ALLOWANCE_06);
        assertThat(testEmployeeSalary.getProvisionForProjectBonus()).isEqualTo(UPDATED_PROVISION_FOR_PROJECT_BONUS);
        assertThat(testEmployeeSalary.getIsHold()).isEqualTo(UPDATED_IS_HOLD);
        assertThat(testEmployeeSalary.getAttendanceRegularisationStartDate()).isEqualTo(UPDATED_ATTENDANCE_REGULARISATION_START_DATE);
        assertThat(testEmployeeSalary.getAttendanceRegularisationEndDate()).isEqualTo(UPDATED_ATTENDANCE_REGULARISATION_END_DATE);
        assertThat(testEmployeeSalary.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEmployeeSalary.getIsVisibleToEmployee()).isEqualTo(UPDATED_IS_VISIBLE_TO_EMPLOYEE);
        assertThat(testEmployeeSalary.getPfArrear()).isEqualTo(UPDATED_PF_ARREAR);
        assertThat(testEmployeeSalary.getTaxCalculationSnapshot()).isEqualTo(UPDATED_TAX_CALCULATION_SNAPSHOT);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeSalary() throws Exception {
        int databaseSizeBeforeUpdate = employeeSalaryRepository.findAll().size();
        employeeSalary.setId(count.incrementAndGet());

        // Create the EmployeeSalary
        EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryMapper.toDto(employeeSalary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeSalaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSalary in the database
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAll();
        assertThat(employeeSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeSalary() throws Exception {
        int databaseSizeBeforeUpdate = employeeSalaryRepository.findAll().size();
        employeeSalary.setId(count.incrementAndGet());

        // Create the EmployeeSalary
        EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryMapper.toDto(employeeSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSalary in the database
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAll();
        assertThat(employeeSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeSalary() throws Exception {
        int databaseSizeBeforeUpdate = employeeSalaryRepository.findAll().size();
        employeeSalary.setId(count.incrementAndGet());

        // Create the EmployeeSalary
        EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryMapper.toDto(employeeSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeSalary in the database
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAll();
        assertThat(employeeSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeSalary() throws Exception {
        // Initialize the database
        employeeSalaryRepository.saveAndFlush(employeeSalary);

        int databaseSizeBeforeDelete = employeeSalaryRepository.findAll().size();

        // Delete the employeeSalary
        restEmployeeSalaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeSalary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAll();
        assertThat(employeeSalaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
