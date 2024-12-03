package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.EmployeeSalaryTempData;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.EmployeeSalaryTempDataRepository;
import com.bits.hr.service.dto.EmployeeSalaryTempDataDTO;
import com.bits.hr.service.mapper.EmployeeSalaryTempDataMapper;
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
 * Integration tests for the {@link EmployeeSalaryTempDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeSalaryTempDataResourceIT {

    private static final Month DEFAULT_MONTH = Month.JANUARY;
    private static final Month UPDATED_MONTH = Month.FEBRUARY;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

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

    private static final Double DEFAULT_PROVISHION_FOR_PROJECT_BONUS = 1D;
    private static final Double UPDATED_PROVISHION_FOR_PROJECT_BONUS = 2D;

    private static final Double DEFAULT_LIVING_ALLOWANCE = 1D;
    private static final Double UPDATED_LIVING_ALLOWANCE = 2D;

    private static final Double DEFAULT_OTHER_ADDITION = 1D;
    private static final Double UPDATED_OTHER_ADDITION = 2D;

    private static final Double DEFAULT_SALARY_ADJUSTMENT = 1D;
    private static final Double UPDATED_SALARY_ADJUSTMENT = 2D;

    private static final Double DEFAULT_PROVIDENT_FUND_ARREAR = 1D;
    private static final Double UPDATED_PROVIDENT_FUND_ARREAR = 2D;

    private static final Double DEFAULT_ENTERTAINMENT = 1D;
    private static final Double UPDATED_ENTERTAINMENT = 2D;

    private static final Double DEFAULT_UTILITY = 1D;
    private static final Double UPDATED_UTILITY = 2D;

    private static final String ENTITY_API_URL = "/api/employee-salary-temp-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeSalaryTempDataRepository employeeSalaryTempDataRepository;

    @Autowired
    private EmployeeSalaryTempDataMapper employeeSalaryTempDataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeSalaryTempDataMockMvc;

    private EmployeeSalaryTempData employeeSalaryTempData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeSalaryTempData createEntity(EntityManager em) {
        EmployeeSalaryTempData employeeSalaryTempData = new EmployeeSalaryTempData()
            .month(DEFAULT_MONTH)
            .year(DEFAULT_YEAR)
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
            .provishionForProjectBonus(DEFAULT_PROVISHION_FOR_PROJECT_BONUS)
            .livingAllowance(DEFAULT_LIVING_ALLOWANCE)
            .otherAddition(DEFAULT_OTHER_ADDITION)
            .salaryAdjustment(DEFAULT_SALARY_ADJUSTMENT)
            .providentFundArrear(DEFAULT_PROVIDENT_FUND_ARREAR)
            .entertainment(DEFAULT_ENTERTAINMENT)
            .utility(DEFAULT_UTILITY);
        return employeeSalaryTempData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeSalaryTempData createUpdatedEntity(EntityManager em) {
        EmployeeSalaryTempData employeeSalaryTempData = new EmployeeSalaryTempData()
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
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
            .provishionForProjectBonus(UPDATED_PROVISHION_FOR_PROJECT_BONUS)
            .livingAllowance(UPDATED_LIVING_ALLOWANCE)
            .otherAddition(UPDATED_OTHER_ADDITION)
            .salaryAdjustment(UPDATED_SALARY_ADJUSTMENT)
            .providentFundArrear(UPDATED_PROVIDENT_FUND_ARREAR)
            .entertainment(UPDATED_ENTERTAINMENT)
            .utility(UPDATED_UTILITY);
        return employeeSalaryTempData;
    }

    @BeforeEach
    public void initTest() {
        employeeSalaryTempData = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeeSalaryTempData() throws Exception {
        int databaseSizeBeforeCreate = employeeSalaryTempDataRepository.findAll().size();
        // Create the EmployeeSalaryTempData
        EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO = employeeSalaryTempDataMapper.toDto(employeeSalaryTempData);
        restEmployeeSalaryTempDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryTempDataDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EmployeeSalaryTempData in the database
        List<EmployeeSalaryTempData> employeeSalaryTempDataList = employeeSalaryTempDataRepository.findAll();
        assertThat(employeeSalaryTempDataList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeSalaryTempData testEmployeeSalaryTempData = employeeSalaryTempDataList.get(employeeSalaryTempDataList.size() - 1);
        assertThat(testEmployeeSalaryTempData.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testEmployeeSalaryTempData.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testEmployeeSalaryTempData.getMainGrossSalary()).isEqualTo(DEFAULT_MAIN_GROSS_SALARY);
        assertThat(testEmployeeSalaryTempData.getMainGrossBasicSalary()).isEqualTo(DEFAULT_MAIN_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalaryTempData.getMainGrossHouseRent()).isEqualTo(DEFAULT_MAIN_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalaryTempData.getMainGrossMedicalAllowance()).isEqualTo(DEFAULT_MAIN_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getMainGrossConveyanceAllowance()).isEqualTo(DEFAULT_MAIN_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getAbsentDays()).isEqualTo(DEFAULT_ABSENT_DAYS);
        assertThat(testEmployeeSalaryTempData.getFractionDays()).isEqualTo(DEFAULT_FRACTION_DAYS);
        assertThat(testEmployeeSalaryTempData.getPayableGrossSalary()).isEqualTo(DEFAULT_PAYABLE_GROSS_SALARY);
        assertThat(testEmployeeSalaryTempData.getPayableGrossBasicSalary()).isEqualTo(DEFAULT_PAYABLE_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalaryTempData.getPayableGrossHouseRent()).isEqualTo(DEFAULT_PAYABLE_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalaryTempData.getPayableGrossMedicalAllowance()).isEqualTo(DEFAULT_PAYABLE_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getPayableGrossConveyanceAllowance()).isEqualTo(DEFAULT_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getArrearSalary()).isEqualTo(DEFAULT_ARREAR_SALARY);
        assertThat(testEmployeeSalaryTempData.getPfDeduction()).isEqualTo(DEFAULT_PF_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getTaxDeduction()).isEqualTo(DEFAULT_TAX_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getWelfareFundDeduction()).isEqualTo(DEFAULT_WELFARE_FUND_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getMobileBillDeduction()).isEqualTo(DEFAULT_MOBILE_BILL_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getOtherDeduction()).isEqualTo(DEFAULT_OTHER_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getTotalDeduction()).isEqualTo(DEFAULT_TOTAL_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getNetPay()).isEqualTo(DEFAULT_NET_PAY);
        assertThat(testEmployeeSalaryTempData.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testEmployeeSalaryTempData.getPfContribution()).isEqualTo(DEFAULT_PF_CONTRIBUTION);
        assertThat(testEmployeeSalaryTempData.getGfContribution()).isEqualTo(DEFAULT_GF_CONTRIBUTION);
        assertThat(testEmployeeSalaryTempData.getProvisionForFestivalBonus()).isEqualTo(DEFAULT_PROVISION_FOR_FESTIVAL_BONUS);
        assertThat(testEmployeeSalaryTempData.getProvisionForLeaveEncashment()).isEqualTo(DEFAULT_PROVISION_FOR_LEAVE_ENCASHMENT);
        assertThat(testEmployeeSalaryTempData.getProvishionForProjectBonus()).isEqualTo(DEFAULT_PROVISHION_FOR_PROJECT_BONUS);
        assertThat(testEmployeeSalaryTempData.getLivingAllowance()).isEqualTo(DEFAULT_LIVING_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getOtherAddition()).isEqualTo(DEFAULT_OTHER_ADDITION);
        assertThat(testEmployeeSalaryTempData.getSalaryAdjustment()).isEqualTo(DEFAULT_SALARY_ADJUSTMENT);
        assertThat(testEmployeeSalaryTempData.getProvidentFundArrear()).isEqualTo(DEFAULT_PROVIDENT_FUND_ARREAR);
        assertThat(testEmployeeSalaryTempData.getEntertainment()).isEqualTo(DEFAULT_ENTERTAINMENT);
        assertThat(testEmployeeSalaryTempData.getUtility()).isEqualTo(DEFAULT_UTILITY);
    }

    @Test
    @Transactional
    void createEmployeeSalaryTempDataWithExistingId() throws Exception {
        // Create the EmployeeSalaryTempData with an existing ID
        employeeSalaryTempData.setId(1L);
        EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO = employeeSalaryTempDataMapper.toDto(employeeSalaryTempData);

        int databaseSizeBeforeCreate = employeeSalaryTempDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeSalaryTempDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryTempDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSalaryTempData in the database
        List<EmployeeSalaryTempData> employeeSalaryTempDataList = employeeSalaryTempDataRepository.findAll();
        assertThat(employeeSalaryTempDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployeeSalaryTempData() throws Exception {
        // Initialize the database
        employeeSalaryTempDataRepository.saveAndFlush(employeeSalaryTempData);

        // Get all the employeeSalaryTempDataList
        restEmployeeSalaryTempDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeSalaryTempData.getId().intValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
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
            .andExpect(jsonPath("$.[*].provishionForProjectBonus").value(hasItem(DEFAULT_PROVISHION_FOR_PROJECT_BONUS.doubleValue())))
            .andExpect(jsonPath("$.[*].livingAllowance").value(hasItem(DEFAULT_LIVING_ALLOWANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].otherAddition").value(hasItem(DEFAULT_OTHER_ADDITION.doubleValue())))
            .andExpect(jsonPath("$.[*].salaryAdjustment").value(hasItem(DEFAULT_SALARY_ADJUSTMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].providentFundArrear").value(hasItem(DEFAULT_PROVIDENT_FUND_ARREAR.doubleValue())))
            .andExpect(jsonPath("$.[*].entertainment").value(hasItem(DEFAULT_ENTERTAINMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].utility").value(hasItem(DEFAULT_UTILITY.doubleValue())));
    }

    @Test
    @Transactional
    void getEmployeeSalaryTempData() throws Exception {
        // Initialize the database
        employeeSalaryTempDataRepository.saveAndFlush(employeeSalaryTempData);

        // Get the employeeSalaryTempData
        restEmployeeSalaryTempDataMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeSalaryTempData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeSalaryTempData.getId().intValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
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
            .andExpect(jsonPath("$.provishionForProjectBonus").value(DEFAULT_PROVISHION_FOR_PROJECT_BONUS.doubleValue()))
            .andExpect(jsonPath("$.livingAllowance").value(DEFAULT_LIVING_ALLOWANCE.doubleValue()))
            .andExpect(jsonPath("$.otherAddition").value(DEFAULT_OTHER_ADDITION.doubleValue()))
            .andExpect(jsonPath("$.salaryAdjustment").value(DEFAULT_SALARY_ADJUSTMENT.doubleValue()))
            .andExpect(jsonPath("$.providentFundArrear").value(DEFAULT_PROVIDENT_FUND_ARREAR.doubleValue()))
            .andExpect(jsonPath("$.entertainment").value(DEFAULT_ENTERTAINMENT.doubleValue()))
            .andExpect(jsonPath("$.utility").value(DEFAULT_UTILITY.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeSalaryTempData() throws Exception {
        // Get the employeeSalaryTempData
        restEmployeeSalaryTempDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeSalaryTempData() throws Exception {
        // Initialize the database
        employeeSalaryTempDataRepository.saveAndFlush(employeeSalaryTempData);

        int databaseSizeBeforeUpdate = employeeSalaryTempDataRepository.findAll().size();

        // Update the employeeSalaryTempData
        EmployeeSalaryTempData updatedEmployeeSalaryTempData = employeeSalaryTempDataRepository
            .findById(employeeSalaryTempData.getId())
            .get();
        // Disconnect from session so that the updates on updatedEmployeeSalaryTempData are not directly saved in db
        em.detach(updatedEmployeeSalaryTempData);
        updatedEmployeeSalaryTempData
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
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
            .provishionForProjectBonus(UPDATED_PROVISHION_FOR_PROJECT_BONUS)
            .livingAllowance(UPDATED_LIVING_ALLOWANCE)
            .otherAddition(UPDATED_OTHER_ADDITION)
            .salaryAdjustment(UPDATED_SALARY_ADJUSTMENT)
            .providentFundArrear(UPDATED_PROVIDENT_FUND_ARREAR)
            .entertainment(UPDATED_ENTERTAINMENT)
            .utility(UPDATED_UTILITY);
        EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO = employeeSalaryTempDataMapper.toDto(updatedEmployeeSalaryTempData);

        restEmployeeSalaryTempDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeSalaryTempDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryTempDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeSalaryTempData in the database
        List<EmployeeSalaryTempData> employeeSalaryTempDataList = employeeSalaryTempDataRepository.findAll();
        assertThat(employeeSalaryTempDataList).hasSize(databaseSizeBeforeUpdate);
        EmployeeSalaryTempData testEmployeeSalaryTempData = employeeSalaryTempDataList.get(employeeSalaryTempDataList.size() - 1);
        assertThat(testEmployeeSalaryTempData.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testEmployeeSalaryTempData.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testEmployeeSalaryTempData.getMainGrossSalary()).isEqualTo(UPDATED_MAIN_GROSS_SALARY);
        assertThat(testEmployeeSalaryTempData.getMainGrossBasicSalary()).isEqualTo(UPDATED_MAIN_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalaryTempData.getMainGrossHouseRent()).isEqualTo(UPDATED_MAIN_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalaryTempData.getMainGrossMedicalAllowance()).isEqualTo(UPDATED_MAIN_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getMainGrossConveyanceAllowance()).isEqualTo(UPDATED_MAIN_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getAbsentDays()).isEqualTo(UPDATED_ABSENT_DAYS);
        assertThat(testEmployeeSalaryTempData.getFractionDays()).isEqualTo(UPDATED_FRACTION_DAYS);
        assertThat(testEmployeeSalaryTempData.getPayableGrossSalary()).isEqualTo(UPDATED_PAYABLE_GROSS_SALARY);
        assertThat(testEmployeeSalaryTempData.getPayableGrossBasicSalary()).isEqualTo(UPDATED_PAYABLE_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalaryTempData.getPayableGrossHouseRent()).isEqualTo(UPDATED_PAYABLE_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalaryTempData.getPayableGrossMedicalAllowance()).isEqualTo(UPDATED_PAYABLE_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getPayableGrossConveyanceAllowance()).isEqualTo(UPDATED_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getArrearSalary()).isEqualTo(UPDATED_ARREAR_SALARY);
        assertThat(testEmployeeSalaryTempData.getPfDeduction()).isEqualTo(UPDATED_PF_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getTaxDeduction()).isEqualTo(UPDATED_TAX_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getWelfareFundDeduction()).isEqualTo(UPDATED_WELFARE_FUND_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getMobileBillDeduction()).isEqualTo(UPDATED_MOBILE_BILL_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getOtherDeduction()).isEqualTo(UPDATED_OTHER_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getTotalDeduction()).isEqualTo(UPDATED_TOTAL_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getNetPay()).isEqualTo(UPDATED_NET_PAY);
        assertThat(testEmployeeSalaryTempData.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testEmployeeSalaryTempData.getPfContribution()).isEqualTo(UPDATED_PF_CONTRIBUTION);
        assertThat(testEmployeeSalaryTempData.getGfContribution()).isEqualTo(UPDATED_GF_CONTRIBUTION);
        assertThat(testEmployeeSalaryTempData.getProvisionForFestivalBonus()).isEqualTo(UPDATED_PROVISION_FOR_FESTIVAL_BONUS);
        assertThat(testEmployeeSalaryTempData.getProvisionForLeaveEncashment()).isEqualTo(UPDATED_PROVISION_FOR_LEAVE_ENCASHMENT);
        assertThat(testEmployeeSalaryTempData.getProvishionForProjectBonus()).isEqualTo(UPDATED_PROVISHION_FOR_PROJECT_BONUS);
        assertThat(testEmployeeSalaryTempData.getLivingAllowance()).isEqualTo(UPDATED_LIVING_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getOtherAddition()).isEqualTo(UPDATED_OTHER_ADDITION);
        assertThat(testEmployeeSalaryTempData.getSalaryAdjustment()).isEqualTo(UPDATED_SALARY_ADJUSTMENT);
        assertThat(testEmployeeSalaryTempData.getProvidentFundArrear()).isEqualTo(UPDATED_PROVIDENT_FUND_ARREAR);
        assertThat(testEmployeeSalaryTempData.getEntertainment()).isEqualTo(UPDATED_ENTERTAINMENT);
        assertThat(testEmployeeSalaryTempData.getUtility()).isEqualTo(UPDATED_UTILITY);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeSalaryTempData() throws Exception {
        int databaseSizeBeforeUpdate = employeeSalaryTempDataRepository.findAll().size();
        employeeSalaryTempData.setId(count.incrementAndGet());

        // Create the EmployeeSalaryTempData
        EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO = employeeSalaryTempDataMapper.toDto(employeeSalaryTempData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeSalaryTempDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeSalaryTempDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryTempDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSalaryTempData in the database
        List<EmployeeSalaryTempData> employeeSalaryTempDataList = employeeSalaryTempDataRepository.findAll();
        assertThat(employeeSalaryTempDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeSalaryTempData() throws Exception {
        int databaseSizeBeforeUpdate = employeeSalaryTempDataRepository.findAll().size();
        employeeSalaryTempData.setId(count.incrementAndGet());

        // Create the EmployeeSalaryTempData
        EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO = employeeSalaryTempDataMapper.toDto(employeeSalaryTempData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeSalaryTempDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryTempDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSalaryTempData in the database
        List<EmployeeSalaryTempData> employeeSalaryTempDataList = employeeSalaryTempDataRepository.findAll();
        assertThat(employeeSalaryTempDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeSalaryTempData() throws Exception {
        int databaseSizeBeforeUpdate = employeeSalaryTempDataRepository.findAll().size();
        employeeSalaryTempData.setId(count.incrementAndGet());

        // Create the EmployeeSalaryTempData
        EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO = employeeSalaryTempDataMapper.toDto(employeeSalaryTempData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeSalaryTempDataMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryTempDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeSalaryTempData in the database
        List<EmployeeSalaryTempData> employeeSalaryTempDataList = employeeSalaryTempDataRepository.findAll();
        assertThat(employeeSalaryTempDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeSalaryTempDataWithPatch() throws Exception {
        // Initialize the database
        employeeSalaryTempDataRepository.saveAndFlush(employeeSalaryTempData);

        int databaseSizeBeforeUpdate = employeeSalaryTempDataRepository.findAll().size();

        // Update the employeeSalaryTempData using partial update
        EmployeeSalaryTempData partialUpdatedEmployeeSalaryTempData = new EmployeeSalaryTempData();
        partialUpdatedEmployeeSalaryTempData.setId(employeeSalaryTempData.getId());

        partialUpdatedEmployeeSalaryTempData
            .year(UPDATED_YEAR)
            .mainGrossSalary(UPDATED_MAIN_GROSS_SALARY)
            .mainGrossHouseRent(UPDATED_MAIN_GROSS_HOUSE_RENT)
            .mainGrossMedicalAllowance(UPDATED_MAIN_GROSS_MEDICAL_ALLOWANCE)
            .mainGrossConveyanceAllowance(UPDATED_MAIN_GROSS_CONVEYANCE_ALLOWANCE)
            .fractionDays(UPDATED_FRACTION_DAYS)
            .payableGrossHouseRent(UPDATED_PAYABLE_GROSS_HOUSE_RENT)
            .pfDeduction(UPDATED_PF_DEDUCTION)
            .taxDeduction(UPDATED_TAX_DEDUCTION)
            .gfContribution(UPDATED_GF_CONTRIBUTION)
            .provisionForFestivalBonus(UPDATED_PROVISION_FOR_FESTIVAL_BONUS)
            .provisionForLeaveEncashment(UPDATED_PROVISION_FOR_LEAVE_ENCASHMENT)
            .provishionForProjectBonus(UPDATED_PROVISHION_FOR_PROJECT_BONUS)
            .livingAllowance(UPDATED_LIVING_ALLOWANCE)
            .salaryAdjustment(UPDATED_SALARY_ADJUSTMENT);

        restEmployeeSalaryTempDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeSalaryTempData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeSalaryTempData))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeSalaryTempData in the database
        List<EmployeeSalaryTempData> employeeSalaryTempDataList = employeeSalaryTempDataRepository.findAll();
        assertThat(employeeSalaryTempDataList).hasSize(databaseSizeBeforeUpdate);
        EmployeeSalaryTempData testEmployeeSalaryTempData = employeeSalaryTempDataList.get(employeeSalaryTempDataList.size() - 1);
        assertThat(testEmployeeSalaryTempData.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testEmployeeSalaryTempData.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testEmployeeSalaryTempData.getMainGrossSalary()).isEqualTo(UPDATED_MAIN_GROSS_SALARY);
        assertThat(testEmployeeSalaryTempData.getMainGrossBasicSalary()).isEqualTo(DEFAULT_MAIN_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalaryTempData.getMainGrossHouseRent()).isEqualTo(UPDATED_MAIN_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalaryTempData.getMainGrossMedicalAllowance()).isEqualTo(UPDATED_MAIN_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getMainGrossConveyanceAllowance()).isEqualTo(UPDATED_MAIN_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getAbsentDays()).isEqualTo(DEFAULT_ABSENT_DAYS);
        assertThat(testEmployeeSalaryTempData.getFractionDays()).isEqualTo(UPDATED_FRACTION_DAYS);
        assertThat(testEmployeeSalaryTempData.getPayableGrossSalary()).isEqualTo(DEFAULT_PAYABLE_GROSS_SALARY);
        assertThat(testEmployeeSalaryTempData.getPayableGrossBasicSalary()).isEqualTo(DEFAULT_PAYABLE_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalaryTempData.getPayableGrossHouseRent()).isEqualTo(UPDATED_PAYABLE_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalaryTempData.getPayableGrossMedicalAllowance()).isEqualTo(DEFAULT_PAYABLE_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getPayableGrossConveyanceAllowance()).isEqualTo(DEFAULT_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getArrearSalary()).isEqualTo(DEFAULT_ARREAR_SALARY);
        assertThat(testEmployeeSalaryTempData.getPfDeduction()).isEqualTo(UPDATED_PF_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getTaxDeduction()).isEqualTo(UPDATED_TAX_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getWelfareFundDeduction()).isEqualTo(DEFAULT_WELFARE_FUND_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getMobileBillDeduction()).isEqualTo(DEFAULT_MOBILE_BILL_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getOtherDeduction()).isEqualTo(DEFAULT_OTHER_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getTotalDeduction()).isEqualTo(DEFAULT_TOTAL_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getNetPay()).isEqualTo(DEFAULT_NET_PAY);
        assertThat(testEmployeeSalaryTempData.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testEmployeeSalaryTempData.getPfContribution()).isEqualTo(DEFAULT_PF_CONTRIBUTION);
        assertThat(testEmployeeSalaryTempData.getGfContribution()).isEqualTo(UPDATED_GF_CONTRIBUTION);
        assertThat(testEmployeeSalaryTempData.getProvisionForFestivalBonus()).isEqualTo(UPDATED_PROVISION_FOR_FESTIVAL_BONUS);
        assertThat(testEmployeeSalaryTempData.getProvisionForLeaveEncashment()).isEqualTo(UPDATED_PROVISION_FOR_LEAVE_ENCASHMENT);
        assertThat(testEmployeeSalaryTempData.getProvishionForProjectBonus()).isEqualTo(UPDATED_PROVISHION_FOR_PROJECT_BONUS);
        assertThat(testEmployeeSalaryTempData.getLivingAllowance()).isEqualTo(UPDATED_LIVING_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getOtherAddition()).isEqualTo(DEFAULT_OTHER_ADDITION);
        assertThat(testEmployeeSalaryTempData.getSalaryAdjustment()).isEqualTo(UPDATED_SALARY_ADJUSTMENT);
        assertThat(testEmployeeSalaryTempData.getProvidentFundArrear()).isEqualTo(DEFAULT_PROVIDENT_FUND_ARREAR);
        assertThat(testEmployeeSalaryTempData.getEntertainment()).isEqualTo(DEFAULT_ENTERTAINMENT);
        assertThat(testEmployeeSalaryTempData.getUtility()).isEqualTo(DEFAULT_UTILITY);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeSalaryTempDataWithPatch() throws Exception {
        // Initialize the database
        employeeSalaryTempDataRepository.saveAndFlush(employeeSalaryTempData);

        int databaseSizeBeforeUpdate = employeeSalaryTempDataRepository.findAll().size();

        // Update the employeeSalaryTempData using partial update
        EmployeeSalaryTempData partialUpdatedEmployeeSalaryTempData = new EmployeeSalaryTempData();
        partialUpdatedEmployeeSalaryTempData.setId(employeeSalaryTempData.getId());

        partialUpdatedEmployeeSalaryTempData
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
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
            .provishionForProjectBonus(UPDATED_PROVISHION_FOR_PROJECT_BONUS)
            .livingAllowance(UPDATED_LIVING_ALLOWANCE)
            .otherAddition(UPDATED_OTHER_ADDITION)
            .salaryAdjustment(UPDATED_SALARY_ADJUSTMENT)
            .providentFundArrear(UPDATED_PROVIDENT_FUND_ARREAR)
            .entertainment(UPDATED_ENTERTAINMENT)
            .utility(UPDATED_UTILITY);

        restEmployeeSalaryTempDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeSalaryTempData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeSalaryTempData))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeSalaryTempData in the database
        List<EmployeeSalaryTempData> employeeSalaryTempDataList = employeeSalaryTempDataRepository.findAll();
        assertThat(employeeSalaryTempDataList).hasSize(databaseSizeBeforeUpdate);
        EmployeeSalaryTempData testEmployeeSalaryTempData = employeeSalaryTempDataList.get(employeeSalaryTempDataList.size() - 1);
        assertThat(testEmployeeSalaryTempData.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testEmployeeSalaryTempData.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testEmployeeSalaryTempData.getMainGrossSalary()).isEqualTo(UPDATED_MAIN_GROSS_SALARY);
        assertThat(testEmployeeSalaryTempData.getMainGrossBasicSalary()).isEqualTo(UPDATED_MAIN_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalaryTempData.getMainGrossHouseRent()).isEqualTo(UPDATED_MAIN_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalaryTempData.getMainGrossMedicalAllowance()).isEqualTo(UPDATED_MAIN_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getMainGrossConveyanceAllowance()).isEqualTo(UPDATED_MAIN_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getAbsentDays()).isEqualTo(UPDATED_ABSENT_DAYS);
        assertThat(testEmployeeSalaryTempData.getFractionDays()).isEqualTo(UPDATED_FRACTION_DAYS);
        assertThat(testEmployeeSalaryTempData.getPayableGrossSalary()).isEqualTo(UPDATED_PAYABLE_GROSS_SALARY);
        assertThat(testEmployeeSalaryTempData.getPayableGrossBasicSalary()).isEqualTo(UPDATED_PAYABLE_GROSS_BASIC_SALARY);
        assertThat(testEmployeeSalaryTempData.getPayableGrossHouseRent()).isEqualTo(UPDATED_PAYABLE_GROSS_HOUSE_RENT);
        assertThat(testEmployeeSalaryTempData.getPayableGrossMedicalAllowance()).isEqualTo(UPDATED_PAYABLE_GROSS_MEDICAL_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getPayableGrossConveyanceAllowance()).isEqualTo(UPDATED_PAYABLE_GROSS_CONVEYANCE_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getArrearSalary()).isEqualTo(UPDATED_ARREAR_SALARY);
        assertThat(testEmployeeSalaryTempData.getPfDeduction()).isEqualTo(UPDATED_PF_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getTaxDeduction()).isEqualTo(UPDATED_TAX_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getWelfareFundDeduction()).isEqualTo(UPDATED_WELFARE_FUND_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getMobileBillDeduction()).isEqualTo(UPDATED_MOBILE_BILL_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getOtherDeduction()).isEqualTo(UPDATED_OTHER_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getTotalDeduction()).isEqualTo(UPDATED_TOTAL_DEDUCTION);
        assertThat(testEmployeeSalaryTempData.getNetPay()).isEqualTo(UPDATED_NET_PAY);
        assertThat(testEmployeeSalaryTempData.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testEmployeeSalaryTempData.getPfContribution()).isEqualTo(UPDATED_PF_CONTRIBUTION);
        assertThat(testEmployeeSalaryTempData.getGfContribution()).isEqualTo(UPDATED_GF_CONTRIBUTION);
        assertThat(testEmployeeSalaryTempData.getProvisionForFestivalBonus()).isEqualTo(UPDATED_PROVISION_FOR_FESTIVAL_BONUS);
        assertThat(testEmployeeSalaryTempData.getProvisionForLeaveEncashment()).isEqualTo(UPDATED_PROVISION_FOR_LEAVE_ENCASHMENT);
        assertThat(testEmployeeSalaryTempData.getProvishionForProjectBonus()).isEqualTo(UPDATED_PROVISHION_FOR_PROJECT_BONUS);
        assertThat(testEmployeeSalaryTempData.getLivingAllowance()).isEqualTo(UPDATED_LIVING_ALLOWANCE);
        assertThat(testEmployeeSalaryTempData.getOtherAddition()).isEqualTo(UPDATED_OTHER_ADDITION);
        assertThat(testEmployeeSalaryTempData.getSalaryAdjustment()).isEqualTo(UPDATED_SALARY_ADJUSTMENT);
        assertThat(testEmployeeSalaryTempData.getProvidentFundArrear()).isEqualTo(UPDATED_PROVIDENT_FUND_ARREAR);
        assertThat(testEmployeeSalaryTempData.getEntertainment()).isEqualTo(UPDATED_ENTERTAINMENT);
        assertThat(testEmployeeSalaryTempData.getUtility()).isEqualTo(UPDATED_UTILITY);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeSalaryTempData() throws Exception {
        int databaseSizeBeforeUpdate = employeeSalaryTempDataRepository.findAll().size();
        employeeSalaryTempData.setId(count.incrementAndGet());

        // Create the EmployeeSalaryTempData
        EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO = employeeSalaryTempDataMapper.toDto(employeeSalaryTempData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeSalaryTempDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeSalaryTempDataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryTempDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSalaryTempData in the database
        List<EmployeeSalaryTempData> employeeSalaryTempDataList = employeeSalaryTempDataRepository.findAll();
        assertThat(employeeSalaryTempDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeSalaryTempData() throws Exception {
        int databaseSizeBeforeUpdate = employeeSalaryTempDataRepository.findAll().size();
        employeeSalaryTempData.setId(count.incrementAndGet());

        // Create the EmployeeSalaryTempData
        EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO = employeeSalaryTempDataMapper.toDto(employeeSalaryTempData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeSalaryTempDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryTempDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeSalaryTempData in the database
        List<EmployeeSalaryTempData> employeeSalaryTempDataList = employeeSalaryTempDataRepository.findAll();
        assertThat(employeeSalaryTempDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeSalaryTempData() throws Exception {
        int databaseSizeBeforeUpdate = employeeSalaryTempDataRepository.findAll().size();
        employeeSalaryTempData.setId(count.incrementAndGet());

        // Create the EmployeeSalaryTempData
        EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO = employeeSalaryTempDataMapper.toDto(employeeSalaryTempData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeSalaryTempDataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeSalaryTempDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeSalaryTempData in the database
        List<EmployeeSalaryTempData> employeeSalaryTempDataList = employeeSalaryTempDataRepository.findAll();
        assertThat(employeeSalaryTempDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeSalaryTempData() throws Exception {
        // Initialize the database
        employeeSalaryTempDataRepository.saveAndFlush(employeeSalaryTempData);

        int databaseSizeBeforeDelete = employeeSalaryTempDataRepository.findAll().size();

        // Delete the employeeSalaryTempData
        restEmployeeSalaryTempDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeSalaryTempData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeSalaryTempData> employeeSalaryTempDataList = employeeSalaryTempDataRepository.findAll();
        assertThat(employeeSalaryTempDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
