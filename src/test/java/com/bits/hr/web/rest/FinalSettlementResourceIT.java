package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.FinalSettlement;
import com.bits.hr.repository.FinalSettlementRepository;
import com.bits.hr.service.FinalSettlementService;
import com.bits.hr.service.dto.FinalSettlementDTO;
import com.bits.hr.service.mapper.FinalSettlementMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link FinalSettlementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FinalSettlementResourceIT {

    private static final LocalDate DEFAULT_DATE_OF_RESIGNATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_RESIGNATION = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_NOTICE_PERIOD = 1;
    private static final Integer UPDATED_NOTICE_PERIOD = 2;

    private static final LocalDate DEFAULT_LAST_WORKING_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_WORKING_DAY = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_OF_RELEASE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_RELEASE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SERVICE_TENURE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_TENURE = "BBBBBBBBBB";

    private static final Double DEFAULT_M_BASIC = 1D;
    private static final Double UPDATED_M_BASIC = 2D;

    private static final Double DEFAULT_M_HOUSE_RENT = 1D;
    private static final Double UPDATED_M_HOUSE_RENT = 2D;

    private static final Double DEFAULT_M_MEDICAL = 1D;
    private static final Double UPDATED_M_MEDICAL = 2D;

    private static final Double DEFAULT_M_CONVEYANCE = 1D;
    private static final Double UPDATED_M_CONVEYANCE = 2D;

    private static final Double DEFAULT_SALARY_PAYABLE = 1D;
    private static final Double UPDATED_SALARY_PAYABLE = 2D;

    private static final String DEFAULT_SALARY_PAYABLE_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_SALARY_PAYABLE_REMARKS = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_DAYS_FOR_LEAVE_ENCASHMENT = 1D;
    private static final Double UPDATED_TOTAL_DAYS_FOR_LEAVE_ENCASHMENT = 2D;

    private static final Double DEFAULT_TOTAL_LEAVE_ENCASHMENT = 1D;
    private static final Double UPDATED_TOTAL_LEAVE_ENCASHMENT = 2D;

    private static final Double DEFAULT_MOBILE_BILL_IN_CASH = 1D;
    private static final Double UPDATED_MOBILE_BILL_IN_CASH = 2D;

    private static final String DEFAULT_ALLOWANCE_01_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALLOWANCE_01_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_ALLOWANCE_01_AMOUNT = 1D;
    private static final Double UPDATED_ALLOWANCE_01_AMOUNT = 2D;

    private static final String DEFAULT_ALLOWANCE_01_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_ALLOWANCE_01_REMARKS = "BBBBBBBBBB";

    private static final String DEFAULT_ALLOWANCE_02_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALLOWANCE_02_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_ALLOWANCE_02_AMOUNT = 1D;
    private static final Double UPDATED_ALLOWANCE_02_AMOUNT = 2D;

    private static final String DEFAULT_ALLOWANCE_02_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_ALLOWANCE_02_REMARKS = "BBBBBBBBBB";

    private static final String DEFAULT_ALLOWANCE_03_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALLOWANCE_03_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_ALLOWANCE_03_AMOUNT = 1D;
    private static final Double UPDATED_ALLOWANCE_03_AMOUNT = 2D;

    private static final String DEFAULT_ALLOWANCE_03_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_ALLOWANCE_03_REMARKS = "BBBBBBBBBB";

    private static final String DEFAULT_ALLOWANCE_04_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALLOWANCE_04_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_ALLOWANCE_04_AMOUNT = 1D;
    private static final Double UPDATED_ALLOWANCE_04_AMOUNT = 2D;

    private static final String DEFAULT_ALLOWANCE_04_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_ALLOWANCE_04_REMARKS = "BBBBBBBBBB";

    private static final Double DEFAULT_DEDUCTION_NOTICE_PAY = 1D;
    private static final Double UPDATED_DEDUCTION_NOTICE_PAY = 2D;

    private static final Double DEFAULT_DEDUCTION_PF = 1D;
    private static final Double UPDATED_DEDUCTION_PF = 2D;

    private static final Double DEFAULT_DEDUCTION_HAF = 1D;
    private static final Double UPDATED_DEDUCTION_HAF = 2D;

    private static final Double DEFAULT_DEDUCTION_EXCESS_CELL_BILL = 1D;
    private static final Double UPDATED_DEDUCTION_EXCESS_CELL_BILL = 2D;

    private static final Double DEFAULT_DEDUCTION_ABSENT_DAYS_ADJUSTMENT = 1D;
    private static final Double UPDATED_DEDUCTION_ABSENT_DAYS_ADJUSTMENT = 2D;

    private static final Double DEFAULT_TOTAL_SALARY_PAYABLE = 1D;
    private static final Double UPDATED_TOTAL_SALARY_PAYABLE = 2D;

    private static final Double DEFAULT_DEDUCTION_ANNUAL_INCOME_TAX = 1D;
    private static final Double UPDATED_DEDUCTION_ANNUAL_INCOME_TAX = 2D;

    private static final Double DEFAULT_NET_SALARY_PAYABLE = 1D;
    private static final Double UPDATED_NET_SALARY_PAYABLE = 2D;

    private static final Double DEFAULT_TOTAL_PAYABLE_PF = 1D;
    private static final Double UPDATED_TOTAL_PAYABLE_PF = 2D;

    private static final Double DEFAULT_TOTAL_PAYABLE_GF = 1D;
    private static final Double UPDATED_TOTAL_PAYABLE_GF = 2D;

    private static final Double DEFAULT_TOTAL_FINAL_SETTLEMENT_AMOUNT = 1D;
    private static final Double UPDATED_TOTAL_FINAL_SETTLEMENT_AMOUNT = 2D;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_DEDUCTION_NOTICE_PAY_DAYS = 1;
    private static final Integer UPDATED_DEDUCTION_NOTICE_PAY_DAYS = 2;

    private static final Integer DEFAULT_DEDUCTION_ABSENT_DAYS_ADJUSTMENT_DAYS = 1;
    private static final Integer UPDATED_DEDUCTION_ABSENT_DAYS_ADJUSTMENT_DAYS = 2;

    private static final Double DEFAULT_DEDUCTION_OTHER = 1D;
    private static final Double UPDATED_DEDUCTION_OTHER = 2D;

    private static final Double DEFAULT_TOTAL_SALARY = 1D;
    private static final Double UPDATED_TOTAL_SALARY = 2D;

    private static final Double DEFAULT_TOTAL_GROSS_SALARY = 1D;
    private static final Double UPDATED_TOTAL_GROSS_SALARY = 2D;

    private static final Double DEFAULT_TOTAL_DEDUCTION = 1D;
    private static final Double UPDATED_TOTAL_DEDUCTION = 2D;

    private static final LocalDate DEFAULT_FINAL_SETTLEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FINAL_SETTLEMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_FINALIZED = false;
    private static final Boolean UPDATED_IS_FINALIZED = true;

    private static final Integer DEFAULT_SALARY_NUM_OF_MONTH = 1;
    private static final Integer UPDATED_SALARY_NUM_OF_MONTH = 2;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/final-settlements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FinalSettlementRepository finalSettlementRepository;

    @Mock
    private FinalSettlementRepository finalSettlementRepositoryMock;

    @Autowired
    private FinalSettlementMapper finalSettlementMapper;

    @Mock
    private FinalSettlementService finalSettlementServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFinalSettlementMockMvc;

    private FinalSettlement finalSettlement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinalSettlement createEntity(EntityManager em) {
        FinalSettlement finalSettlement = new FinalSettlement()
            .dateOfResignation(DEFAULT_DATE_OF_RESIGNATION)
            .noticePeriod(DEFAULT_NOTICE_PERIOD)
            .lastWorkingDay(DEFAULT_LAST_WORKING_DAY)
            .dateOfRelease(DEFAULT_DATE_OF_RELEASE)
            .serviceTenure(DEFAULT_SERVICE_TENURE)
            .mBasic(DEFAULT_M_BASIC)
            .mHouseRent(DEFAULT_M_HOUSE_RENT)
            .mMedical(DEFAULT_M_MEDICAL)
            .mConveyance(DEFAULT_M_CONVEYANCE)
            .salaryPayable(DEFAULT_SALARY_PAYABLE)
            .salaryPayableRemarks(DEFAULT_SALARY_PAYABLE_REMARKS)
            .totalDaysForLeaveEncashment(DEFAULT_TOTAL_DAYS_FOR_LEAVE_ENCASHMENT)
            .totalLeaveEncashment(DEFAULT_TOTAL_LEAVE_ENCASHMENT)
            .mobileBillInCash(DEFAULT_MOBILE_BILL_IN_CASH)
            .allowance01Name(DEFAULT_ALLOWANCE_01_NAME)
            .allowance01Amount(DEFAULT_ALLOWANCE_01_AMOUNT)
            .allowance01Remarks(DEFAULT_ALLOWANCE_01_REMARKS)
            .allowance02Name(DEFAULT_ALLOWANCE_02_NAME)
            .allowance02Amount(DEFAULT_ALLOWANCE_02_AMOUNT)
            .allowance02Remarks(DEFAULT_ALLOWANCE_02_REMARKS)
            .allowance03Name(DEFAULT_ALLOWANCE_03_NAME)
            .allowance03Amount(DEFAULT_ALLOWANCE_03_AMOUNT)
            .allowance03Remarks(DEFAULT_ALLOWANCE_03_REMARKS)
            .allowance04Name(DEFAULT_ALLOWANCE_04_NAME)
            .allowance04Amount(DEFAULT_ALLOWANCE_04_AMOUNT)
            .allowance04Remarks(DEFAULT_ALLOWANCE_04_REMARKS)
            .deductionNoticePay(DEFAULT_DEDUCTION_NOTICE_PAY)
            .deductionPf(DEFAULT_DEDUCTION_PF)
            .deductionHaf(DEFAULT_DEDUCTION_HAF)
            .deductionExcessCellBill(DEFAULT_DEDUCTION_EXCESS_CELL_BILL)
            .deductionAbsentDaysAdjustment(DEFAULT_DEDUCTION_ABSENT_DAYS_ADJUSTMENT)
            .totalSalaryPayable(DEFAULT_TOTAL_SALARY_PAYABLE)
            .deductionAnnualIncomeTax(DEFAULT_DEDUCTION_ANNUAL_INCOME_TAX)
            .netSalaryPayable(DEFAULT_NET_SALARY_PAYABLE)
            .totalPayablePf(DEFAULT_TOTAL_PAYABLE_PF)
            .totalPayableGf(DEFAULT_TOTAL_PAYABLE_GF)
            .totalFinalSettlementAmount(DEFAULT_TOTAL_FINAL_SETTLEMENT_AMOUNT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deductionNoticePayDays(DEFAULT_DEDUCTION_NOTICE_PAY_DAYS)
            .deductionAbsentDaysAdjustmentDays(DEFAULT_DEDUCTION_ABSENT_DAYS_ADJUSTMENT_DAYS)
            .deductionOther(DEFAULT_DEDUCTION_OTHER)
            .totalSalary(DEFAULT_TOTAL_SALARY)
            .totalGrossSalary(DEFAULT_TOTAL_GROSS_SALARY)
            .totalDeduction(DEFAULT_TOTAL_DEDUCTION)
            .finalSettlementDate(DEFAULT_FINAL_SETTLEMENT_DATE)
            .isFinalized(DEFAULT_IS_FINALIZED)
            .salaryNumOfMonth(DEFAULT_SALARY_NUM_OF_MONTH)
            .remarks(DEFAULT_REMARKS);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        finalSettlement.setEmployee(employee);
        return finalSettlement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinalSettlement createUpdatedEntity(EntityManager em) {
        FinalSettlement finalSettlement = new FinalSettlement()
            .dateOfResignation(UPDATED_DATE_OF_RESIGNATION)
            .noticePeriod(UPDATED_NOTICE_PERIOD)
            .lastWorkingDay(UPDATED_LAST_WORKING_DAY)
            .dateOfRelease(UPDATED_DATE_OF_RELEASE)
            .serviceTenure(UPDATED_SERVICE_TENURE)
            .mBasic(UPDATED_M_BASIC)
            .mHouseRent(UPDATED_M_HOUSE_RENT)
            .mMedical(UPDATED_M_MEDICAL)
            .mConveyance(UPDATED_M_CONVEYANCE)
            .salaryPayable(UPDATED_SALARY_PAYABLE)
            .salaryPayableRemarks(UPDATED_SALARY_PAYABLE_REMARKS)
            .totalDaysForLeaveEncashment(UPDATED_TOTAL_DAYS_FOR_LEAVE_ENCASHMENT)
            .totalLeaveEncashment(UPDATED_TOTAL_LEAVE_ENCASHMENT)
            .mobileBillInCash(UPDATED_MOBILE_BILL_IN_CASH)
            .allowance01Name(UPDATED_ALLOWANCE_01_NAME)
            .allowance01Amount(UPDATED_ALLOWANCE_01_AMOUNT)
            .allowance01Remarks(UPDATED_ALLOWANCE_01_REMARKS)
            .allowance02Name(UPDATED_ALLOWANCE_02_NAME)
            .allowance02Amount(UPDATED_ALLOWANCE_02_AMOUNT)
            .allowance02Remarks(UPDATED_ALLOWANCE_02_REMARKS)
            .allowance03Name(UPDATED_ALLOWANCE_03_NAME)
            .allowance03Amount(UPDATED_ALLOWANCE_03_AMOUNT)
            .allowance03Remarks(UPDATED_ALLOWANCE_03_REMARKS)
            .allowance04Name(UPDATED_ALLOWANCE_04_NAME)
            .allowance04Amount(UPDATED_ALLOWANCE_04_AMOUNT)
            .allowance04Remarks(UPDATED_ALLOWANCE_04_REMARKS)
            .deductionNoticePay(UPDATED_DEDUCTION_NOTICE_PAY)
            .deductionPf(UPDATED_DEDUCTION_PF)
            .deductionHaf(UPDATED_DEDUCTION_HAF)
            .deductionExcessCellBill(UPDATED_DEDUCTION_EXCESS_CELL_BILL)
            .deductionAbsentDaysAdjustment(UPDATED_DEDUCTION_ABSENT_DAYS_ADJUSTMENT)
            .totalSalaryPayable(UPDATED_TOTAL_SALARY_PAYABLE)
            .deductionAnnualIncomeTax(UPDATED_DEDUCTION_ANNUAL_INCOME_TAX)
            .netSalaryPayable(UPDATED_NET_SALARY_PAYABLE)
            .totalPayablePf(UPDATED_TOTAL_PAYABLE_PF)
            .totalPayableGf(UPDATED_TOTAL_PAYABLE_GF)
            .totalFinalSettlementAmount(UPDATED_TOTAL_FINAL_SETTLEMENT_AMOUNT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deductionNoticePayDays(UPDATED_DEDUCTION_NOTICE_PAY_DAYS)
            .deductionAbsentDaysAdjustmentDays(UPDATED_DEDUCTION_ABSENT_DAYS_ADJUSTMENT_DAYS)
            .deductionOther(UPDATED_DEDUCTION_OTHER)
            .totalSalary(UPDATED_TOTAL_SALARY)
            .totalGrossSalary(UPDATED_TOTAL_GROSS_SALARY)
            .totalDeduction(UPDATED_TOTAL_DEDUCTION)
            .finalSettlementDate(UPDATED_FINAL_SETTLEMENT_DATE)
            .isFinalized(UPDATED_IS_FINALIZED)
            .salaryNumOfMonth(UPDATED_SALARY_NUM_OF_MONTH)
            .remarks(UPDATED_REMARKS);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        finalSettlement.setEmployee(employee);
        return finalSettlement;
    }

    @BeforeEach
    public void initTest() {
        finalSettlement = createEntity(em);
    }

    @Test
    @Transactional
    void createFinalSettlement() throws Exception {
        int databaseSizeBeforeCreate = finalSettlementRepository.findAll().size();
        // Create the FinalSettlement
        FinalSettlementDTO finalSettlementDTO = finalSettlementMapper.toDto(finalSettlement);
        restFinalSettlementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(finalSettlementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FinalSettlement in the database
        List<FinalSettlement> finalSettlementList = finalSettlementRepository.findAll();
        assertThat(finalSettlementList).hasSize(databaseSizeBeforeCreate + 1);
        FinalSettlement testFinalSettlement = finalSettlementList.get(finalSettlementList.size() - 1);
        assertThat(testFinalSettlement.getDateOfResignation()).isEqualTo(DEFAULT_DATE_OF_RESIGNATION);
        assertThat(testFinalSettlement.getNoticePeriod()).isEqualTo(DEFAULT_NOTICE_PERIOD);
        assertThat(testFinalSettlement.getLastWorkingDay()).isEqualTo(DEFAULT_LAST_WORKING_DAY);
        assertThat(testFinalSettlement.getDateOfRelease()).isEqualTo(DEFAULT_DATE_OF_RELEASE);
        assertThat(testFinalSettlement.getServiceTenure()).isEqualTo(DEFAULT_SERVICE_TENURE);
        assertThat(testFinalSettlement.getmBasic()).isEqualTo(DEFAULT_M_BASIC);
        assertThat(testFinalSettlement.getmHouseRent()).isEqualTo(DEFAULT_M_HOUSE_RENT);
        assertThat(testFinalSettlement.getmMedical()).isEqualTo(DEFAULT_M_MEDICAL);
        assertThat(testFinalSettlement.getmConveyance()).isEqualTo(DEFAULT_M_CONVEYANCE);
        assertThat(testFinalSettlement.getSalaryPayable()).isEqualTo(DEFAULT_SALARY_PAYABLE);
        assertThat(testFinalSettlement.getSalaryPayableRemarks()).isEqualTo(DEFAULT_SALARY_PAYABLE_REMARKS);
        assertThat(testFinalSettlement.getTotalDaysForLeaveEncashment()).isEqualTo(DEFAULT_TOTAL_DAYS_FOR_LEAVE_ENCASHMENT);
        assertThat(testFinalSettlement.getTotalLeaveEncashment()).isEqualTo(DEFAULT_TOTAL_LEAVE_ENCASHMENT);
        assertThat(testFinalSettlement.getMobileBillInCash()).isEqualTo(DEFAULT_MOBILE_BILL_IN_CASH);
        assertThat(testFinalSettlement.getAllowance01Name()).isEqualTo(DEFAULT_ALLOWANCE_01_NAME);
        assertThat(testFinalSettlement.getAllowance01Amount()).isEqualTo(DEFAULT_ALLOWANCE_01_AMOUNT);
        assertThat(testFinalSettlement.getAllowance01Remarks()).isEqualTo(DEFAULT_ALLOWANCE_01_REMARKS);
        assertThat(testFinalSettlement.getAllowance02Name()).isEqualTo(DEFAULT_ALLOWANCE_02_NAME);
        assertThat(testFinalSettlement.getAllowance02Amount()).isEqualTo(DEFAULT_ALLOWANCE_02_AMOUNT);
        assertThat(testFinalSettlement.getAllowance02Remarks()).isEqualTo(DEFAULT_ALLOWANCE_02_REMARKS);
        assertThat(testFinalSettlement.getAllowance03Name()).isEqualTo(DEFAULT_ALLOWANCE_03_NAME);
        assertThat(testFinalSettlement.getAllowance03Amount()).isEqualTo(DEFAULT_ALLOWANCE_03_AMOUNT);
        assertThat(testFinalSettlement.getAllowance03Remarks()).isEqualTo(DEFAULT_ALLOWANCE_03_REMARKS);
        assertThat(testFinalSettlement.getAllowance04Name()).isEqualTo(DEFAULT_ALLOWANCE_04_NAME);
        assertThat(testFinalSettlement.getAllowance04Amount()).isEqualTo(DEFAULT_ALLOWANCE_04_AMOUNT);
        assertThat(testFinalSettlement.getAllowance04Remarks()).isEqualTo(DEFAULT_ALLOWANCE_04_REMARKS);
        assertThat(testFinalSettlement.getDeductionNoticePay()).isEqualTo(DEFAULT_DEDUCTION_NOTICE_PAY);
        assertThat(testFinalSettlement.getDeductionPf()).isEqualTo(DEFAULT_DEDUCTION_PF);
        assertThat(testFinalSettlement.getDeductionHaf()).isEqualTo(DEFAULT_DEDUCTION_HAF);
        assertThat(testFinalSettlement.getDeductionExcessCellBill()).isEqualTo(DEFAULT_DEDUCTION_EXCESS_CELL_BILL);
        assertThat(testFinalSettlement.getDeductionAbsentDaysAdjustment()).isEqualTo(DEFAULT_DEDUCTION_ABSENT_DAYS_ADJUSTMENT);
        assertThat(testFinalSettlement.getTotalSalaryPayable()).isEqualTo(DEFAULT_TOTAL_SALARY_PAYABLE);
        assertThat(testFinalSettlement.getDeductionAnnualIncomeTax()).isEqualTo(DEFAULT_DEDUCTION_ANNUAL_INCOME_TAX);
        assertThat(testFinalSettlement.getNetSalaryPayable()).isEqualTo(DEFAULT_NET_SALARY_PAYABLE);
        assertThat(testFinalSettlement.getTotalPayablePf()).isEqualTo(DEFAULT_TOTAL_PAYABLE_PF);
        assertThat(testFinalSettlement.getTotalPayableGf()).isEqualTo(DEFAULT_TOTAL_PAYABLE_GF);
        assertThat(testFinalSettlement.getTotalFinalSettlementAmount()).isEqualTo(DEFAULT_TOTAL_FINAL_SETTLEMENT_AMOUNT);
        assertThat(testFinalSettlement.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testFinalSettlement.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testFinalSettlement.getDeductionNoticePayDays()).isEqualTo(DEFAULT_DEDUCTION_NOTICE_PAY_DAYS);
        assertThat(testFinalSettlement.getDeductionAbsentDaysAdjustmentDays()).isEqualTo(DEFAULT_DEDUCTION_ABSENT_DAYS_ADJUSTMENT_DAYS);
        assertThat(testFinalSettlement.getDeductionOther()).isEqualTo(DEFAULT_DEDUCTION_OTHER);
        assertThat(testFinalSettlement.getTotalSalary()).isEqualTo(DEFAULT_TOTAL_SALARY);
        assertThat(testFinalSettlement.getTotalGrossSalary()).isEqualTo(DEFAULT_TOTAL_GROSS_SALARY);
        assertThat(testFinalSettlement.getTotalDeduction()).isEqualTo(DEFAULT_TOTAL_DEDUCTION);
        assertThat(testFinalSettlement.getFinalSettlementDate()).isEqualTo(DEFAULT_FINAL_SETTLEMENT_DATE);
        assertThat(testFinalSettlement.getIsFinalized()).isEqualTo(DEFAULT_IS_FINALIZED);
        assertThat(testFinalSettlement.getSalaryNumOfMonth()).isEqualTo(DEFAULT_SALARY_NUM_OF_MONTH);
        assertThat(testFinalSettlement.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void createFinalSettlementWithExistingId() throws Exception {
        // Create the FinalSettlement with an existing ID
        finalSettlement.setId(1L);
        FinalSettlementDTO finalSettlementDTO = finalSettlementMapper.toDto(finalSettlement);

        int databaseSizeBeforeCreate = finalSettlementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinalSettlementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(finalSettlementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinalSettlement in the database
        List<FinalSettlement> finalSettlementList = finalSettlementRepository.findAll();
        assertThat(finalSettlementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFinalSettlements() throws Exception {
        // Initialize the database
        finalSettlementRepository.saveAndFlush(finalSettlement);

        // Get all the finalSettlementList
        restFinalSettlementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finalSettlement.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfResignation").value(hasItem(DEFAULT_DATE_OF_RESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].noticePeriod").value(hasItem(DEFAULT_NOTICE_PERIOD)))
            .andExpect(jsonPath("$.[*].lastWorkingDay").value(hasItem(DEFAULT_LAST_WORKING_DAY.toString())))
            .andExpect(jsonPath("$.[*].dateOfRelease").value(hasItem(DEFAULT_DATE_OF_RELEASE.toString())))
            .andExpect(jsonPath("$.[*].serviceTenure").value(hasItem(DEFAULT_SERVICE_TENURE)))
            .andExpect(jsonPath("$.[*].mBasic").value(hasItem(DEFAULT_M_BASIC.doubleValue())))
            .andExpect(jsonPath("$.[*].mHouseRent").value(hasItem(DEFAULT_M_HOUSE_RENT.doubleValue())))
            .andExpect(jsonPath("$.[*].mMedical").value(hasItem(DEFAULT_M_MEDICAL.doubleValue())))
            .andExpect(jsonPath("$.[*].mConveyance").value(hasItem(DEFAULT_M_CONVEYANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].salaryPayable").value(hasItem(DEFAULT_SALARY_PAYABLE.doubleValue())))
            .andExpect(jsonPath("$.[*].salaryPayableRemarks").value(hasItem(DEFAULT_SALARY_PAYABLE_REMARKS)))
            .andExpect(jsonPath("$.[*].totalDaysForLeaveEncashment").value(hasItem(DEFAULT_TOTAL_DAYS_FOR_LEAVE_ENCASHMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalLeaveEncashment").value(hasItem(DEFAULT_TOTAL_LEAVE_ENCASHMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].mobileBillInCash").value(hasItem(DEFAULT_MOBILE_BILL_IN_CASH.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance01Name").value(hasItem(DEFAULT_ALLOWANCE_01_NAME)))
            .andExpect(jsonPath("$.[*].allowance01Amount").value(hasItem(DEFAULT_ALLOWANCE_01_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance01Remarks").value(hasItem(DEFAULT_ALLOWANCE_01_REMARKS)))
            .andExpect(jsonPath("$.[*].allowance02Name").value(hasItem(DEFAULT_ALLOWANCE_02_NAME)))
            .andExpect(jsonPath("$.[*].allowance02Amount").value(hasItem(DEFAULT_ALLOWANCE_02_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance02Remarks").value(hasItem(DEFAULT_ALLOWANCE_02_REMARKS)))
            .andExpect(jsonPath("$.[*].allowance03Name").value(hasItem(DEFAULT_ALLOWANCE_03_NAME)))
            .andExpect(jsonPath("$.[*].allowance03Amount").value(hasItem(DEFAULT_ALLOWANCE_03_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance03Remarks").value(hasItem(DEFAULT_ALLOWANCE_03_REMARKS)))
            .andExpect(jsonPath("$.[*].allowance04Name").value(hasItem(DEFAULT_ALLOWANCE_04_NAME)))
            .andExpect(jsonPath("$.[*].allowance04Amount").value(hasItem(DEFAULT_ALLOWANCE_04_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].allowance04Remarks").value(hasItem(DEFAULT_ALLOWANCE_04_REMARKS)))
            .andExpect(jsonPath("$.[*].deductionNoticePay").value(hasItem(DEFAULT_DEDUCTION_NOTICE_PAY.doubleValue())))
            .andExpect(jsonPath("$.[*].deductionPf").value(hasItem(DEFAULT_DEDUCTION_PF.doubleValue())))
            .andExpect(jsonPath("$.[*].deductionHaf").value(hasItem(DEFAULT_DEDUCTION_HAF.doubleValue())))
            .andExpect(jsonPath("$.[*].deductionExcessCellBill").value(hasItem(DEFAULT_DEDUCTION_EXCESS_CELL_BILL.doubleValue())))
            .andExpect(
                jsonPath("$.[*].deductionAbsentDaysAdjustment").value(hasItem(DEFAULT_DEDUCTION_ABSENT_DAYS_ADJUSTMENT.doubleValue()))
            )
            .andExpect(jsonPath("$.[*].totalSalaryPayable").value(hasItem(DEFAULT_TOTAL_SALARY_PAYABLE.doubleValue())))
            .andExpect(jsonPath("$.[*].deductionAnnualIncomeTax").value(hasItem(DEFAULT_DEDUCTION_ANNUAL_INCOME_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].netSalaryPayable").value(hasItem(DEFAULT_NET_SALARY_PAYABLE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalPayablePf").value(hasItem(DEFAULT_TOTAL_PAYABLE_PF.doubleValue())))
            .andExpect(jsonPath("$.[*].totalPayableGf").value(hasItem(DEFAULT_TOTAL_PAYABLE_GF.doubleValue())))
            .andExpect(jsonPath("$.[*].totalFinalSettlementAmount").value(hasItem(DEFAULT_TOTAL_FINAL_SETTLEMENT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].deductionNoticePayDays").value(hasItem(DEFAULT_DEDUCTION_NOTICE_PAY_DAYS)))
            .andExpect(jsonPath("$.[*].deductionAbsentDaysAdjustmentDays").value(hasItem(DEFAULT_DEDUCTION_ABSENT_DAYS_ADJUSTMENT_DAYS)))
            .andExpect(jsonPath("$.[*].deductionOther").value(hasItem(DEFAULT_DEDUCTION_OTHER.doubleValue())))
            .andExpect(jsonPath("$.[*].totalSalary").value(hasItem(DEFAULT_TOTAL_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].totalGrossSalary").value(hasItem(DEFAULT_TOTAL_GROSS_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].totalDeduction").value(hasItem(DEFAULT_TOTAL_DEDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].finalSettlementDate").value(hasItem(DEFAULT_FINAL_SETTLEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isFinalized").value(hasItem(DEFAULT_IS_FINALIZED.booleanValue())))
            .andExpect(jsonPath("$.[*].salaryNumOfMonth").value(hasItem(DEFAULT_SALARY_NUM_OF_MONTH)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFinalSettlementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(finalSettlementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFinalSettlementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(finalSettlementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFinalSettlementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(finalSettlementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFinalSettlementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(finalSettlementRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFinalSettlement() throws Exception {
        // Initialize the database
        finalSettlementRepository.saveAndFlush(finalSettlement);

        // Get the finalSettlement
        restFinalSettlementMockMvc
            .perform(get(ENTITY_API_URL_ID, finalSettlement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(finalSettlement.getId().intValue()))
            .andExpect(jsonPath("$.dateOfResignation").value(DEFAULT_DATE_OF_RESIGNATION.toString()))
            .andExpect(jsonPath("$.noticePeriod").value(DEFAULT_NOTICE_PERIOD))
            .andExpect(jsonPath("$.lastWorkingDay").value(DEFAULT_LAST_WORKING_DAY.toString()))
            .andExpect(jsonPath("$.dateOfRelease").value(DEFAULT_DATE_OF_RELEASE.toString()))
            .andExpect(jsonPath("$.serviceTenure").value(DEFAULT_SERVICE_TENURE))
            .andExpect(jsonPath("$.mBasic").value(DEFAULT_M_BASIC.doubleValue()))
            .andExpect(jsonPath("$.mHouseRent").value(DEFAULT_M_HOUSE_RENT.doubleValue()))
            .andExpect(jsonPath("$.mMedical").value(DEFAULT_M_MEDICAL.doubleValue()))
            .andExpect(jsonPath("$.mConveyance").value(DEFAULT_M_CONVEYANCE.doubleValue()))
            .andExpect(jsonPath("$.salaryPayable").value(DEFAULT_SALARY_PAYABLE.doubleValue()))
            .andExpect(jsonPath("$.salaryPayableRemarks").value(DEFAULT_SALARY_PAYABLE_REMARKS))
            .andExpect(jsonPath("$.totalDaysForLeaveEncashment").value(DEFAULT_TOTAL_DAYS_FOR_LEAVE_ENCASHMENT.doubleValue()))
            .andExpect(jsonPath("$.totalLeaveEncashment").value(DEFAULT_TOTAL_LEAVE_ENCASHMENT.doubleValue()))
            .andExpect(jsonPath("$.mobileBillInCash").value(DEFAULT_MOBILE_BILL_IN_CASH.doubleValue()))
            .andExpect(jsonPath("$.allowance01Name").value(DEFAULT_ALLOWANCE_01_NAME))
            .andExpect(jsonPath("$.allowance01Amount").value(DEFAULT_ALLOWANCE_01_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.allowance01Remarks").value(DEFAULT_ALLOWANCE_01_REMARKS))
            .andExpect(jsonPath("$.allowance02Name").value(DEFAULT_ALLOWANCE_02_NAME))
            .andExpect(jsonPath("$.allowance02Amount").value(DEFAULT_ALLOWANCE_02_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.allowance02Remarks").value(DEFAULT_ALLOWANCE_02_REMARKS))
            .andExpect(jsonPath("$.allowance03Name").value(DEFAULT_ALLOWANCE_03_NAME))
            .andExpect(jsonPath("$.allowance03Amount").value(DEFAULT_ALLOWANCE_03_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.allowance03Remarks").value(DEFAULT_ALLOWANCE_03_REMARKS))
            .andExpect(jsonPath("$.allowance04Name").value(DEFAULT_ALLOWANCE_04_NAME))
            .andExpect(jsonPath("$.allowance04Amount").value(DEFAULT_ALLOWANCE_04_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.allowance04Remarks").value(DEFAULT_ALLOWANCE_04_REMARKS))
            .andExpect(jsonPath("$.deductionNoticePay").value(DEFAULT_DEDUCTION_NOTICE_PAY.doubleValue()))
            .andExpect(jsonPath("$.deductionPf").value(DEFAULT_DEDUCTION_PF.doubleValue()))
            .andExpect(jsonPath("$.deductionHaf").value(DEFAULT_DEDUCTION_HAF.doubleValue()))
            .andExpect(jsonPath("$.deductionExcessCellBill").value(DEFAULT_DEDUCTION_EXCESS_CELL_BILL.doubleValue()))
            .andExpect(jsonPath("$.deductionAbsentDaysAdjustment").value(DEFAULT_DEDUCTION_ABSENT_DAYS_ADJUSTMENT.doubleValue()))
            .andExpect(jsonPath("$.totalSalaryPayable").value(DEFAULT_TOTAL_SALARY_PAYABLE.doubleValue()))
            .andExpect(jsonPath("$.deductionAnnualIncomeTax").value(DEFAULT_DEDUCTION_ANNUAL_INCOME_TAX.doubleValue()))
            .andExpect(jsonPath("$.netSalaryPayable").value(DEFAULT_NET_SALARY_PAYABLE.doubleValue()))
            .andExpect(jsonPath("$.totalPayablePf").value(DEFAULT_TOTAL_PAYABLE_PF.doubleValue()))
            .andExpect(jsonPath("$.totalPayableGf").value(DEFAULT_TOTAL_PAYABLE_GF.doubleValue()))
            .andExpect(jsonPath("$.totalFinalSettlementAmount").value(DEFAULT_TOTAL_FINAL_SETTLEMENT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.deductionNoticePayDays").value(DEFAULT_DEDUCTION_NOTICE_PAY_DAYS))
            .andExpect(jsonPath("$.deductionAbsentDaysAdjustmentDays").value(DEFAULT_DEDUCTION_ABSENT_DAYS_ADJUSTMENT_DAYS))
            .andExpect(jsonPath("$.deductionOther").value(DEFAULT_DEDUCTION_OTHER.doubleValue()))
            .andExpect(jsonPath("$.totalSalary").value(DEFAULT_TOTAL_SALARY.doubleValue()))
            .andExpect(jsonPath("$.totalGrossSalary").value(DEFAULT_TOTAL_GROSS_SALARY.doubleValue()))
            .andExpect(jsonPath("$.totalDeduction").value(DEFAULT_TOTAL_DEDUCTION.doubleValue()))
            .andExpect(jsonPath("$.finalSettlementDate").value(DEFAULT_FINAL_SETTLEMENT_DATE.toString()))
            .andExpect(jsonPath("$.isFinalized").value(DEFAULT_IS_FINALIZED.booleanValue()))
            .andExpect(jsonPath("$.salaryNumOfMonth").value(DEFAULT_SALARY_NUM_OF_MONTH))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getNonExistingFinalSettlement() throws Exception {
        // Get the finalSettlement
        restFinalSettlementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFinalSettlement() throws Exception {
        // Initialize the database
        finalSettlementRepository.saveAndFlush(finalSettlement);

        int databaseSizeBeforeUpdate = finalSettlementRepository.findAll().size();

        // Update the finalSettlement
        FinalSettlement updatedFinalSettlement = finalSettlementRepository.findById(finalSettlement.getId()).get();
        // Disconnect from session so that the updates on updatedFinalSettlement are not directly saved in db
        em.detach(updatedFinalSettlement);
        updatedFinalSettlement
            .dateOfResignation(UPDATED_DATE_OF_RESIGNATION)
            .noticePeriod(UPDATED_NOTICE_PERIOD)
            .lastWorkingDay(UPDATED_LAST_WORKING_DAY)
            .dateOfRelease(UPDATED_DATE_OF_RELEASE)
            .serviceTenure(UPDATED_SERVICE_TENURE)
            .mBasic(UPDATED_M_BASIC)
            .mHouseRent(UPDATED_M_HOUSE_RENT)
            .mMedical(UPDATED_M_MEDICAL)
            .mConveyance(UPDATED_M_CONVEYANCE)
            .salaryPayable(UPDATED_SALARY_PAYABLE)
            .salaryPayableRemarks(UPDATED_SALARY_PAYABLE_REMARKS)
            .totalDaysForLeaveEncashment(UPDATED_TOTAL_DAYS_FOR_LEAVE_ENCASHMENT)
            .totalLeaveEncashment(UPDATED_TOTAL_LEAVE_ENCASHMENT)
            .mobileBillInCash(UPDATED_MOBILE_BILL_IN_CASH)
            .allowance01Name(UPDATED_ALLOWANCE_01_NAME)
            .allowance01Amount(UPDATED_ALLOWANCE_01_AMOUNT)
            .allowance01Remarks(UPDATED_ALLOWANCE_01_REMARKS)
            .allowance02Name(UPDATED_ALLOWANCE_02_NAME)
            .allowance02Amount(UPDATED_ALLOWANCE_02_AMOUNT)
            .allowance02Remarks(UPDATED_ALLOWANCE_02_REMARKS)
            .allowance03Name(UPDATED_ALLOWANCE_03_NAME)
            .allowance03Amount(UPDATED_ALLOWANCE_03_AMOUNT)
            .allowance03Remarks(UPDATED_ALLOWANCE_03_REMARKS)
            .allowance04Name(UPDATED_ALLOWANCE_04_NAME)
            .allowance04Amount(UPDATED_ALLOWANCE_04_AMOUNT)
            .allowance04Remarks(UPDATED_ALLOWANCE_04_REMARKS)
            .deductionNoticePay(UPDATED_DEDUCTION_NOTICE_PAY)
            .deductionPf(UPDATED_DEDUCTION_PF)
            .deductionHaf(UPDATED_DEDUCTION_HAF)
            .deductionExcessCellBill(UPDATED_DEDUCTION_EXCESS_CELL_BILL)
            .deductionAbsentDaysAdjustment(UPDATED_DEDUCTION_ABSENT_DAYS_ADJUSTMENT)
            .totalSalaryPayable(UPDATED_TOTAL_SALARY_PAYABLE)
            .deductionAnnualIncomeTax(UPDATED_DEDUCTION_ANNUAL_INCOME_TAX)
            .netSalaryPayable(UPDATED_NET_SALARY_PAYABLE)
            .totalPayablePf(UPDATED_TOTAL_PAYABLE_PF)
            .totalPayableGf(UPDATED_TOTAL_PAYABLE_GF)
            .totalFinalSettlementAmount(UPDATED_TOTAL_FINAL_SETTLEMENT_AMOUNT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deductionNoticePayDays(UPDATED_DEDUCTION_NOTICE_PAY_DAYS)
            .deductionAbsentDaysAdjustmentDays(UPDATED_DEDUCTION_ABSENT_DAYS_ADJUSTMENT_DAYS)
            .deductionOther(UPDATED_DEDUCTION_OTHER)
            .totalSalary(UPDATED_TOTAL_SALARY)
            .totalGrossSalary(UPDATED_TOTAL_GROSS_SALARY)
            .totalDeduction(UPDATED_TOTAL_DEDUCTION)
            .finalSettlementDate(UPDATED_FINAL_SETTLEMENT_DATE)
            .isFinalized(UPDATED_IS_FINALIZED)
            .salaryNumOfMonth(UPDATED_SALARY_NUM_OF_MONTH)
            .remarks(UPDATED_REMARKS);
        FinalSettlementDTO finalSettlementDTO = finalSettlementMapper.toDto(updatedFinalSettlement);

        restFinalSettlementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, finalSettlementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(finalSettlementDTO))
            )
            .andExpect(status().isOk());

        // Validate the FinalSettlement in the database
        List<FinalSettlement> finalSettlementList = finalSettlementRepository.findAll();
        assertThat(finalSettlementList).hasSize(databaseSizeBeforeUpdate);
        FinalSettlement testFinalSettlement = finalSettlementList.get(finalSettlementList.size() - 1);
        assertThat(testFinalSettlement.getDateOfResignation()).isEqualTo(UPDATED_DATE_OF_RESIGNATION);
        assertThat(testFinalSettlement.getNoticePeriod()).isEqualTo(UPDATED_NOTICE_PERIOD);
        assertThat(testFinalSettlement.getLastWorkingDay()).isEqualTo(UPDATED_LAST_WORKING_DAY);
        assertThat(testFinalSettlement.getDateOfRelease()).isEqualTo(UPDATED_DATE_OF_RELEASE);
        assertThat(testFinalSettlement.getServiceTenure()).isEqualTo(UPDATED_SERVICE_TENURE);
        assertThat(testFinalSettlement.getmBasic()).isEqualTo(UPDATED_M_BASIC);
        assertThat(testFinalSettlement.getmHouseRent()).isEqualTo(UPDATED_M_HOUSE_RENT);
        assertThat(testFinalSettlement.getmMedical()).isEqualTo(UPDATED_M_MEDICAL);
        assertThat(testFinalSettlement.getmConveyance()).isEqualTo(UPDATED_M_CONVEYANCE);
        assertThat(testFinalSettlement.getSalaryPayable()).isEqualTo(UPDATED_SALARY_PAYABLE);
        assertThat(testFinalSettlement.getSalaryPayableRemarks()).isEqualTo(UPDATED_SALARY_PAYABLE_REMARKS);
        assertThat(testFinalSettlement.getTotalDaysForLeaveEncashment()).isEqualTo(UPDATED_TOTAL_DAYS_FOR_LEAVE_ENCASHMENT);
        assertThat(testFinalSettlement.getTotalLeaveEncashment()).isEqualTo(UPDATED_TOTAL_LEAVE_ENCASHMENT);
        assertThat(testFinalSettlement.getMobileBillInCash()).isEqualTo(UPDATED_MOBILE_BILL_IN_CASH);
        assertThat(testFinalSettlement.getAllowance01Name()).isEqualTo(UPDATED_ALLOWANCE_01_NAME);
        assertThat(testFinalSettlement.getAllowance01Amount()).isEqualTo(UPDATED_ALLOWANCE_01_AMOUNT);
        assertThat(testFinalSettlement.getAllowance01Remarks()).isEqualTo(UPDATED_ALLOWANCE_01_REMARKS);
        assertThat(testFinalSettlement.getAllowance02Name()).isEqualTo(UPDATED_ALLOWANCE_02_NAME);
        assertThat(testFinalSettlement.getAllowance02Amount()).isEqualTo(UPDATED_ALLOWANCE_02_AMOUNT);
        assertThat(testFinalSettlement.getAllowance02Remarks()).isEqualTo(UPDATED_ALLOWANCE_02_REMARKS);
        assertThat(testFinalSettlement.getAllowance03Name()).isEqualTo(UPDATED_ALLOWANCE_03_NAME);
        assertThat(testFinalSettlement.getAllowance03Amount()).isEqualTo(UPDATED_ALLOWANCE_03_AMOUNT);
        assertThat(testFinalSettlement.getAllowance03Remarks()).isEqualTo(UPDATED_ALLOWANCE_03_REMARKS);
        assertThat(testFinalSettlement.getAllowance04Name()).isEqualTo(UPDATED_ALLOWANCE_04_NAME);
        assertThat(testFinalSettlement.getAllowance04Amount()).isEqualTo(UPDATED_ALLOWANCE_04_AMOUNT);
        assertThat(testFinalSettlement.getAllowance04Remarks()).isEqualTo(UPDATED_ALLOWANCE_04_REMARKS);
        assertThat(testFinalSettlement.getDeductionNoticePay()).isEqualTo(UPDATED_DEDUCTION_NOTICE_PAY);
        assertThat(testFinalSettlement.getDeductionPf()).isEqualTo(UPDATED_DEDUCTION_PF);
        assertThat(testFinalSettlement.getDeductionHaf()).isEqualTo(UPDATED_DEDUCTION_HAF);
        assertThat(testFinalSettlement.getDeductionExcessCellBill()).isEqualTo(UPDATED_DEDUCTION_EXCESS_CELL_BILL);
        assertThat(testFinalSettlement.getDeductionAbsentDaysAdjustment()).isEqualTo(UPDATED_DEDUCTION_ABSENT_DAYS_ADJUSTMENT);
        assertThat(testFinalSettlement.getTotalSalaryPayable()).isEqualTo(UPDATED_TOTAL_SALARY_PAYABLE);
        assertThat(testFinalSettlement.getDeductionAnnualIncomeTax()).isEqualTo(UPDATED_DEDUCTION_ANNUAL_INCOME_TAX);
        assertThat(testFinalSettlement.getNetSalaryPayable()).isEqualTo(UPDATED_NET_SALARY_PAYABLE);
        assertThat(testFinalSettlement.getTotalPayablePf()).isEqualTo(UPDATED_TOTAL_PAYABLE_PF);
        assertThat(testFinalSettlement.getTotalPayableGf()).isEqualTo(UPDATED_TOTAL_PAYABLE_GF);
        assertThat(testFinalSettlement.getTotalFinalSettlementAmount()).isEqualTo(UPDATED_TOTAL_FINAL_SETTLEMENT_AMOUNT);
        assertThat(testFinalSettlement.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFinalSettlement.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testFinalSettlement.getDeductionNoticePayDays()).isEqualTo(UPDATED_DEDUCTION_NOTICE_PAY_DAYS);
        assertThat(testFinalSettlement.getDeductionAbsentDaysAdjustmentDays()).isEqualTo(UPDATED_DEDUCTION_ABSENT_DAYS_ADJUSTMENT_DAYS);
        assertThat(testFinalSettlement.getDeductionOther()).isEqualTo(UPDATED_DEDUCTION_OTHER);
        assertThat(testFinalSettlement.getTotalSalary()).isEqualTo(UPDATED_TOTAL_SALARY);
        assertThat(testFinalSettlement.getTotalGrossSalary()).isEqualTo(UPDATED_TOTAL_GROSS_SALARY);
        assertThat(testFinalSettlement.getTotalDeduction()).isEqualTo(UPDATED_TOTAL_DEDUCTION);
        assertThat(testFinalSettlement.getFinalSettlementDate()).isEqualTo(UPDATED_FINAL_SETTLEMENT_DATE);
        assertThat(testFinalSettlement.getIsFinalized()).isEqualTo(UPDATED_IS_FINALIZED);
        assertThat(testFinalSettlement.getSalaryNumOfMonth()).isEqualTo(UPDATED_SALARY_NUM_OF_MONTH);
        assertThat(testFinalSettlement.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void putNonExistingFinalSettlement() throws Exception {
        int databaseSizeBeforeUpdate = finalSettlementRepository.findAll().size();
        finalSettlement.setId(count.incrementAndGet());

        // Create the FinalSettlement
        FinalSettlementDTO finalSettlementDTO = finalSettlementMapper.toDto(finalSettlement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinalSettlementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, finalSettlementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(finalSettlementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinalSettlement in the database
        List<FinalSettlement> finalSettlementList = finalSettlementRepository.findAll();
        assertThat(finalSettlementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFinalSettlement() throws Exception {
        int databaseSizeBeforeUpdate = finalSettlementRepository.findAll().size();
        finalSettlement.setId(count.incrementAndGet());

        // Create the FinalSettlement
        FinalSettlementDTO finalSettlementDTO = finalSettlementMapper.toDto(finalSettlement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinalSettlementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(finalSettlementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinalSettlement in the database
        List<FinalSettlement> finalSettlementList = finalSettlementRepository.findAll();
        assertThat(finalSettlementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFinalSettlement() throws Exception {
        int databaseSizeBeforeUpdate = finalSettlementRepository.findAll().size();
        finalSettlement.setId(count.incrementAndGet());

        // Create the FinalSettlement
        FinalSettlementDTO finalSettlementDTO = finalSettlementMapper.toDto(finalSettlement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinalSettlementMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(finalSettlementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinalSettlement in the database
        List<FinalSettlement> finalSettlementList = finalSettlementRepository.findAll();
        assertThat(finalSettlementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFinalSettlementWithPatch() throws Exception {
        // Initialize the database
        finalSettlementRepository.saveAndFlush(finalSettlement);

        int databaseSizeBeforeUpdate = finalSettlementRepository.findAll().size();

        // Update the finalSettlement using partial update
        FinalSettlement partialUpdatedFinalSettlement = new FinalSettlement();
        partialUpdatedFinalSettlement.setId(finalSettlement.getId());

        partialUpdatedFinalSettlement
            .dateOfResignation(UPDATED_DATE_OF_RESIGNATION)
            .noticePeriod(UPDATED_NOTICE_PERIOD)
            .serviceTenure(UPDATED_SERVICE_TENURE)
            .mBasic(UPDATED_M_BASIC)
            .mMedical(UPDATED_M_MEDICAL)
            .mConveyance(UPDATED_M_CONVEYANCE)
            .salaryPayableRemarks(UPDATED_SALARY_PAYABLE_REMARKS)
            .mobileBillInCash(UPDATED_MOBILE_BILL_IN_CASH)
            .allowance01Name(UPDATED_ALLOWANCE_01_NAME)
            .allowance01Amount(UPDATED_ALLOWANCE_01_AMOUNT)
            .allowance02Name(UPDATED_ALLOWANCE_02_NAME)
            .allowance02Remarks(UPDATED_ALLOWANCE_02_REMARKS)
            .allowance03Name(UPDATED_ALLOWANCE_03_NAME)
            .allowance04Name(UPDATED_ALLOWANCE_04_NAME)
            .allowance04Amount(UPDATED_ALLOWANCE_04_AMOUNT)
            .deductionPf(UPDATED_DEDUCTION_PF)
            .deductionHaf(UPDATED_DEDUCTION_HAF)
            .totalSalaryPayable(UPDATED_TOTAL_SALARY_PAYABLE)
            .deductionAnnualIncomeTax(UPDATED_DEDUCTION_ANNUAL_INCOME_TAX)
            .totalFinalSettlementAmount(UPDATED_TOTAL_FINAL_SETTLEMENT_AMOUNT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deductionNoticePayDays(UPDATED_DEDUCTION_NOTICE_PAY_DAYS)
            .deductionAbsentDaysAdjustmentDays(UPDATED_DEDUCTION_ABSENT_DAYS_ADJUSTMENT_DAYS)
            .deductionOther(UPDATED_DEDUCTION_OTHER)
            .totalGrossSalary(UPDATED_TOTAL_GROSS_SALARY)
            .finalSettlementDate(UPDATED_FINAL_SETTLEMENT_DATE)
            .remarks(UPDATED_REMARKS);

        restFinalSettlementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinalSettlement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFinalSettlement))
            )
            .andExpect(status().isOk());

        // Validate the FinalSettlement in the database
        List<FinalSettlement> finalSettlementList = finalSettlementRepository.findAll();
        assertThat(finalSettlementList).hasSize(databaseSizeBeforeUpdate);
        FinalSettlement testFinalSettlement = finalSettlementList.get(finalSettlementList.size() - 1);
        assertThat(testFinalSettlement.getDateOfResignation()).isEqualTo(UPDATED_DATE_OF_RESIGNATION);
        assertThat(testFinalSettlement.getNoticePeriod()).isEqualTo(UPDATED_NOTICE_PERIOD);
        assertThat(testFinalSettlement.getLastWorkingDay()).isEqualTo(DEFAULT_LAST_WORKING_DAY);
        assertThat(testFinalSettlement.getDateOfRelease()).isEqualTo(DEFAULT_DATE_OF_RELEASE);
        assertThat(testFinalSettlement.getServiceTenure()).isEqualTo(UPDATED_SERVICE_TENURE);
        assertThat(testFinalSettlement.getmBasic()).isEqualTo(UPDATED_M_BASIC);
        assertThat(testFinalSettlement.getmHouseRent()).isEqualTo(DEFAULT_M_HOUSE_RENT);
        assertThat(testFinalSettlement.getmMedical()).isEqualTo(UPDATED_M_MEDICAL);
        assertThat(testFinalSettlement.getmConveyance()).isEqualTo(UPDATED_M_CONVEYANCE);
        assertThat(testFinalSettlement.getSalaryPayable()).isEqualTo(DEFAULT_SALARY_PAYABLE);
        assertThat(testFinalSettlement.getSalaryPayableRemarks()).isEqualTo(UPDATED_SALARY_PAYABLE_REMARKS);
        assertThat(testFinalSettlement.getTotalDaysForLeaveEncashment()).isEqualTo(DEFAULT_TOTAL_DAYS_FOR_LEAVE_ENCASHMENT);
        assertThat(testFinalSettlement.getTotalLeaveEncashment()).isEqualTo(DEFAULT_TOTAL_LEAVE_ENCASHMENT);
        assertThat(testFinalSettlement.getMobileBillInCash()).isEqualTo(UPDATED_MOBILE_BILL_IN_CASH);
        assertThat(testFinalSettlement.getAllowance01Name()).isEqualTo(UPDATED_ALLOWANCE_01_NAME);
        assertThat(testFinalSettlement.getAllowance01Amount()).isEqualTo(UPDATED_ALLOWANCE_01_AMOUNT);
        assertThat(testFinalSettlement.getAllowance01Remarks()).isEqualTo(DEFAULT_ALLOWANCE_01_REMARKS);
        assertThat(testFinalSettlement.getAllowance02Name()).isEqualTo(UPDATED_ALLOWANCE_02_NAME);
        assertThat(testFinalSettlement.getAllowance02Amount()).isEqualTo(DEFAULT_ALLOWANCE_02_AMOUNT);
        assertThat(testFinalSettlement.getAllowance02Remarks()).isEqualTo(UPDATED_ALLOWANCE_02_REMARKS);
        assertThat(testFinalSettlement.getAllowance03Name()).isEqualTo(UPDATED_ALLOWANCE_03_NAME);
        assertThat(testFinalSettlement.getAllowance03Amount()).isEqualTo(DEFAULT_ALLOWANCE_03_AMOUNT);
        assertThat(testFinalSettlement.getAllowance03Remarks()).isEqualTo(DEFAULT_ALLOWANCE_03_REMARKS);
        assertThat(testFinalSettlement.getAllowance04Name()).isEqualTo(UPDATED_ALLOWANCE_04_NAME);
        assertThat(testFinalSettlement.getAllowance04Amount()).isEqualTo(UPDATED_ALLOWANCE_04_AMOUNT);
        assertThat(testFinalSettlement.getAllowance04Remarks()).isEqualTo(DEFAULT_ALLOWANCE_04_REMARKS);
        assertThat(testFinalSettlement.getDeductionNoticePay()).isEqualTo(DEFAULT_DEDUCTION_NOTICE_PAY);
        assertThat(testFinalSettlement.getDeductionPf()).isEqualTo(UPDATED_DEDUCTION_PF);
        assertThat(testFinalSettlement.getDeductionHaf()).isEqualTo(UPDATED_DEDUCTION_HAF);
        assertThat(testFinalSettlement.getDeductionExcessCellBill()).isEqualTo(DEFAULT_DEDUCTION_EXCESS_CELL_BILL);
        assertThat(testFinalSettlement.getDeductionAbsentDaysAdjustment()).isEqualTo(DEFAULT_DEDUCTION_ABSENT_DAYS_ADJUSTMENT);
        assertThat(testFinalSettlement.getTotalSalaryPayable()).isEqualTo(UPDATED_TOTAL_SALARY_PAYABLE);
        assertThat(testFinalSettlement.getDeductionAnnualIncomeTax()).isEqualTo(UPDATED_DEDUCTION_ANNUAL_INCOME_TAX);
        assertThat(testFinalSettlement.getNetSalaryPayable()).isEqualTo(DEFAULT_NET_SALARY_PAYABLE);
        assertThat(testFinalSettlement.getTotalPayablePf()).isEqualTo(DEFAULT_TOTAL_PAYABLE_PF);
        assertThat(testFinalSettlement.getTotalPayableGf()).isEqualTo(DEFAULT_TOTAL_PAYABLE_GF);
        assertThat(testFinalSettlement.getTotalFinalSettlementAmount()).isEqualTo(UPDATED_TOTAL_FINAL_SETTLEMENT_AMOUNT);
        assertThat(testFinalSettlement.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFinalSettlement.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testFinalSettlement.getDeductionNoticePayDays()).isEqualTo(UPDATED_DEDUCTION_NOTICE_PAY_DAYS);
        assertThat(testFinalSettlement.getDeductionAbsentDaysAdjustmentDays()).isEqualTo(UPDATED_DEDUCTION_ABSENT_DAYS_ADJUSTMENT_DAYS);
        assertThat(testFinalSettlement.getDeductionOther()).isEqualTo(UPDATED_DEDUCTION_OTHER);
        assertThat(testFinalSettlement.getTotalSalary()).isEqualTo(DEFAULT_TOTAL_SALARY);
        assertThat(testFinalSettlement.getTotalGrossSalary()).isEqualTo(UPDATED_TOTAL_GROSS_SALARY);
        assertThat(testFinalSettlement.getTotalDeduction()).isEqualTo(DEFAULT_TOTAL_DEDUCTION);
        assertThat(testFinalSettlement.getFinalSettlementDate()).isEqualTo(UPDATED_FINAL_SETTLEMENT_DATE);
        assertThat(testFinalSettlement.getIsFinalized()).isEqualTo(DEFAULT_IS_FINALIZED);
        assertThat(testFinalSettlement.getSalaryNumOfMonth()).isEqualTo(DEFAULT_SALARY_NUM_OF_MONTH);
        assertThat(testFinalSettlement.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdateFinalSettlementWithPatch() throws Exception {
        // Initialize the database
        finalSettlementRepository.saveAndFlush(finalSettlement);

        int databaseSizeBeforeUpdate = finalSettlementRepository.findAll().size();

        // Update the finalSettlement using partial update
        FinalSettlement partialUpdatedFinalSettlement = new FinalSettlement();
        partialUpdatedFinalSettlement.setId(finalSettlement.getId());

        partialUpdatedFinalSettlement
            .dateOfResignation(UPDATED_DATE_OF_RESIGNATION)
            .noticePeriod(UPDATED_NOTICE_PERIOD)
            .lastWorkingDay(UPDATED_LAST_WORKING_DAY)
            .dateOfRelease(UPDATED_DATE_OF_RELEASE)
            .serviceTenure(UPDATED_SERVICE_TENURE)
            .mBasic(UPDATED_M_BASIC)
            .mHouseRent(UPDATED_M_HOUSE_RENT)
            .mMedical(UPDATED_M_MEDICAL)
            .mConveyance(UPDATED_M_CONVEYANCE)
            .salaryPayable(UPDATED_SALARY_PAYABLE)
            .salaryPayableRemarks(UPDATED_SALARY_PAYABLE_REMARKS)
            .totalDaysForLeaveEncashment(UPDATED_TOTAL_DAYS_FOR_LEAVE_ENCASHMENT)
            .totalLeaveEncashment(UPDATED_TOTAL_LEAVE_ENCASHMENT)
            .mobileBillInCash(UPDATED_MOBILE_BILL_IN_CASH)
            .allowance01Name(UPDATED_ALLOWANCE_01_NAME)
            .allowance01Amount(UPDATED_ALLOWANCE_01_AMOUNT)
            .allowance01Remarks(UPDATED_ALLOWANCE_01_REMARKS)
            .allowance02Name(UPDATED_ALLOWANCE_02_NAME)
            .allowance02Amount(UPDATED_ALLOWANCE_02_AMOUNT)
            .allowance02Remarks(UPDATED_ALLOWANCE_02_REMARKS)
            .allowance03Name(UPDATED_ALLOWANCE_03_NAME)
            .allowance03Amount(UPDATED_ALLOWANCE_03_AMOUNT)
            .allowance03Remarks(UPDATED_ALLOWANCE_03_REMARKS)
            .allowance04Name(UPDATED_ALLOWANCE_04_NAME)
            .allowance04Amount(UPDATED_ALLOWANCE_04_AMOUNT)
            .allowance04Remarks(UPDATED_ALLOWANCE_04_REMARKS)
            .deductionNoticePay(UPDATED_DEDUCTION_NOTICE_PAY)
            .deductionPf(UPDATED_DEDUCTION_PF)
            .deductionHaf(UPDATED_DEDUCTION_HAF)
            .deductionExcessCellBill(UPDATED_DEDUCTION_EXCESS_CELL_BILL)
            .deductionAbsentDaysAdjustment(UPDATED_DEDUCTION_ABSENT_DAYS_ADJUSTMENT)
            .totalSalaryPayable(UPDATED_TOTAL_SALARY_PAYABLE)
            .deductionAnnualIncomeTax(UPDATED_DEDUCTION_ANNUAL_INCOME_TAX)
            .netSalaryPayable(UPDATED_NET_SALARY_PAYABLE)
            .totalPayablePf(UPDATED_TOTAL_PAYABLE_PF)
            .totalPayableGf(UPDATED_TOTAL_PAYABLE_GF)
            .totalFinalSettlementAmount(UPDATED_TOTAL_FINAL_SETTLEMENT_AMOUNT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deductionNoticePayDays(UPDATED_DEDUCTION_NOTICE_PAY_DAYS)
            .deductionAbsentDaysAdjustmentDays(UPDATED_DEDUCTION_ABSENT_DAYS_ADJUSTMENT_DAYS)
            .deductionOther(UPDATED_DEDUCTION_OTHER)
            .totalSalary(UPDATED_TOTAL_SALARY)
            .totalGrossSalary(UPDATED_TOTAL_GROSS_SALARY)
            .totalDeduction(UPDATED_TOTAL_DEDUCTION)
            .finalSettlementDate(UPDATED_FINAL_SETTLEMENT_DATE)
            .isFinalized(UPDATED_IS_FINALIZED)
            .salaryNumOfMonth(UPDATED_SALARY_NUM_OF_MONTH)
            .remarks(UPDATED_REMARKS);

        restFinalSettlementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinalSettlement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFinalSettlement))
            )
            .andExpect(status().isOk());

        // Validate the FinalSettlement in the database
        List<FinalSettlement> finalSettlementList = finalSettlementRepository.findAll();
        assertThat(finalSettlementList).hasSize(databaseSizeBeforeUpdate);
        FinalSettlement testFinalSettlement = finalSettlementList.get(finalSettlementList.size() - 1);
        assertThat(testFinalSettlement.getDateOfResignation()).isEqualTo(UPDATED_DATE_OF_RESIGNATION);
        assertThat(testFinalSettlement.getNoticePeriod()).isEqualTo(UPDATED_NOTICE_PERIOD);
        assertThat(testFinalSettlement.getLastWorkingDay()).isEqualTo(UPDATED_LAST_WORKING_DAY);
        assertThat(testFinalSettlement.getDateOfRelease()).isEqualTo(UPDATED_DATE_OF_RELEASE);
        assertThat(testFinalSettlement.getServiceTenure()).isEqualTo(UPDATED_SERVICE_TENURE);
        assertThat(testFinalSettlement.getmBasic()).isEqualTo(UPDATED_M_BASIC);
        assertThat(testFinalSettlement.getmHouseRent()).isEqualTo(UPDATED_M_HOUSE_RENT);
        assertThat(testFinalSettlement.getmMedical()).isEqualTo(UPDATED_M_MEDICAL);
        assertThat(testFinalSettlement.getmConveyance()).isEqualTo(UPDATED_M_CONVEYANCE);
        assertThat(testFinalSettlement.getSalaryPayable()).isEqualTo(UPDATED_SALARY_PAYABLE);
        assertThat(testFinalSettlement.getSalaryPayableRemarks()).isEqualTo(UPDATED_SALARY_PAYABLE_REMARKS);
        assertThat(testFinalSettlement.getTotalDaysForLeaveEncashment()).isEqualTo(UPDATED_TOTAL_DAYS_FOR_LEAVE_ENCASHMENT);
        assertThat(testFinalSettlement.getTotalLeaveEncashment()).isEqualTo(UPDATED_TOTAL_LEAVE_ENCASHMENT);
        assertThat(testFinalSettlement.getMobileBillInCash()).isEqualTo(UPDATED_MOBILE_BILL_IN_CASH);
        assertThat(testFinalSettlement.getAllowance01Name()).isEqualTo(UPDATED_ALLOWANCE_01_NAME);
        assertThat(testFinalSettlement.getAllowance01Amount()).isEqualTo(UPDATED_ALLOWANCE_01_AMOUNT);
        assertThat(testFinalSettlement.getAllowance01Remarks()).isEqualTo(UPDATED_ALLOWANCE_01_REMARKS);
        assertThat(testFinalSettlement.getAllowance02Name()).isEqualTo(UPDATED_ALLOWANCE_02_NAME);
        assertThat(testFinalSettlement.getAllowance02Amount()).isEqualTo(UPDATED_ALLOWANCE_02_AMOUNT);
        assertThat(testFinalSettlement.getAllowance02Remarks()).isEqualTo(UPDATED_ALLOWANCE_02_REMARKS);
        assertThat(testFinalSettlement.getAllowance03Name()).isEqualTo(UPDATED_ALLOWANCE_03_NAME);
        assertThat(testFinalSettlement.getAllowance03Amount()).isEqualTo(UPDATED_ALLOWANCE_03_AMOUNT);
        assertThat(testFinalSettlement.getAllowance03Remarks()).isEqualTo(UPDATED_ALLOWANCE_03_REMARKS);
        assertThat(testFinalSettlement.getAllowance04Name()).isEqualTo(UPDATED_ALLOWANCE_04_NAME);
        assertThat(testFinalSettlement.getAllowance04Amount()).isEqualTo(UPDATED_ALLOWANCE_04_AMOUNT);
        assertThat(testFinalSettlement.getAllowance04Remarks()).isEqualTo(UPDATED_ALLOWANCE_04_REMARKS);
        assertThat(testFinalSettlement.getDeductionNoticePay()).isEqualTo(UPDATED_DEDUCTION_NOTICE_PAY);
        assertThat(testFinalSettlement.getDeductionPf()).isEqualTo(UPDATED_DEDUCTION_PF);
        assertThat(testFinalSettlement.getDeductionHaf()).isEqualTo(UPDATED_DEDUCTION_HAF);
        assertThat(testFinalSettlement.getDeductionExcessCellBill()).isEqualTo(UPDATED_DEDUCTION_EXCESS_CELL_BILL);
        assertThat(testFinalSettlement.getDeductionAbsentDaysAdjustment()).isEqualTo(UPDATED_DEDUCTION_ABSENT_DAYS_ADJUSTMENT);
        assertThat(testFinalSettlement.getTotalSalaryPayable()).isEqualTo(UPDATED_TOTAL_SALARY_PAYABLE);
        assertThat(testFinalSettlement.getDeductionAnnualIncomeTax()).isEqualTo(UPDATED_DEDUCTION_ANNUAL_INCOME_TAX);
        assertThat(testFinalSettlement.getNetSalaryPayable()).isEqualTo(UPDATED_NET_SALARY_PAYABLE);
        assertThat(testFinalSettlement.getTotalPayablePf()).isEqualTo(UPDATED_TOTAL_PAYABLE_PF);
        assertThat(testFinalSettlement.getTotalPayableGf()).isEqualTo(UPDATED_TOTAL_PAYABLE_GF);
        assertThat(testFinalSettlement.getTotalFinalSettlementAmount()).isEqualTo(UPDATED_TOTAL_FINAL_SETTLEMENT_AMOUNT);
        assertThat(testFinalSettlement.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFinalSettlement.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testFinalSettlement.getDeductionNoticePayDays()).isEqualTo(UPDATED_DEDUCTION_NOTICE_PAY_DAYS);
        assertThat(testFinalSettlement.getDeductionAbsentDaysAdjustmentDays()).isEqualTo(UPDATED_DEDUCTION_ABSENT_DAYS_ADJUSTMENT_DAYS);
        assertThat(testFinalSettlement.getDeductionOther()).isEqualTo(UPDATED_DEDUCTION_OTHER);
        assertThat(testFinalSettlement.getTotalSalary()).isEqualTo(UPDATED_TOTAL_SALARY);
        assertThat(testFinalSettlement.getTotalGrossSalary()).isEqualTo(UPDATED_TOTAL_GROSS_SALARY);
        assertThat(testFinalSettlement.getTotalDeduction()).isEqualTo(UPDATED_TOTAL_DEDUCTION);
        assertThat(testFinalSettlement.getFinalSettlementDate()).isEqualTo(UPDATED_FINAL_SETTLEMENT_DATE);
        assertThat(testFinalSettlement.getIsFinalized()).isEqualTo(UPDATED_IS_FINALIZED);
        assertThat(testFinalSettlement.getSalaryNumOfMonth()).isEqualTo(UPDATED_SALARY_NUM_OF_MONTH);
        assertThat(testFinalSettlement.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingFinalSettlement() throws Exception {
        int databaseSizeBeforeUpdate = finalSettlementRepository.findAll().size();
        finalSettlement.setId(count.incrementAndGet());

        // Create the FinalSettlement
        FinalSettlementDTO finalSettlementDTO = finalSettlementMapper.toDto(finalSettlement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinalSettlementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, finalSettlementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(finalSettlementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinalSettlement in the database
        List<FinalSettlement> finalSettlementList = finalSettlementRepository.findAll();
        assertThat(finalSettlementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFinalSettlement() throws Exception {
        int databaseSizeBeforeUpdate = finalSettlementRepository.findAll().size();
        finalSettlement.setId(count.incrementAndGet());

        // Create the FinalSettlement
        FinalSettlementDTO finalSettlementDTO = finalSettlementMapper.toDto(finalSettlement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinalSettlementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(finalSettlementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinalSettlement in the database
        List<FinalSettlement> finalSettlementList = finalSettlementRepository.findAll();
        assertThat(finalSettlementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFinalSettlement() throws Exception {
        int databaseSizeBeforeUpdate = finalSettlementRepository.findAll().size();
        finalSettlement.setId(count.incrementAndGet());

        // Create the FinalSettlement
        FinalSettlementDTO finalSettlementDTO = finalSettlementMapper.toDto(finalSettlement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinalSettlementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(finalSettlementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinalSettlement in the database
        List<FinalSettlement> finalSettlementList = finalSettlementRepository.findAll();
        assertThat(finalSettlementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFinalSettlement() throws Exception {
        // Initialize the database
        finalSettlementRepository.saveAndFlush(finalSettlement);

        int databaseSizeBeforeDelete = finalSettlementRepository.findAll().size();

        // Delete the finalSettlement
        restFinalSettlementMockMvc
            .perform(delete(ENTITY_API_URL_ID, finalSettlement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FinalSettlement> finalSettlementList = finalSettlementRepository.findAll();
        assertThat(finalSettlementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
