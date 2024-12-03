package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.LeaveBalance;
import com.bits.hr.domain.enumeration.LeaveAmountType;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.repository.LeaveBalanceRepository;
import com.bits.hr.service.dto.LeaveBalanceDTO;
import com.bits.hr.service.mapper.LeaveBalanceMapper;
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
 * Integration tests for the {@link LeaveBalanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LeaveBalanceResourceIT {

    private static final LeaveType DEFAULT_LEAVE_TYPE = LeaveType.MENTIONABLE_ANNUAL_LEAVE;
    private static final LeaveType UPDATED_LEAVE_TYPE = LeaveType.MENTIONABLE_CASUAL_LEAVE;

    private static final Integer DEFAULT_OPENING_BALANCE = 1;
    private static final Integer UPDATED_OPENING_BALANCE = 2;

    private static final Integer DEFAULT_CLOSING_BALANCE = 1;
    private static final Integer UPDATED_CLOSING_BALANCE = 2;

    private static final Integer DEFAULT_CONSUMED_DURING_YEAR = 1;
    private static final Integer UPDATED_CONSUMED_DURING_YEAR = 2;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    private static final LeaveAmountType DEFAULT_LEAVE_AMOUNT_TYPE = LeaveAmountType.Day;
    private static final LeaveAmountType UPDATED_LEAVE_AMOUNT_TYPE = LeaveAmountType.Year;

    private static final String ENTITY_API_URL = "/api/leave-balances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private LeaveBalanceMapper leaveBalanceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaveBalanceMockMvc;

    private LeaveBalance leaveBalance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveBalance createEntity(EntityManager em) {
        LeaveBalance leaveBalance = new LeaveBalance()
            .leaveType(DEFAULT_LEAVE_TYPE)
            .openingBalance(DEFAULT_OPENING_BALANCE)
            .closingBalance(DEFAULT_CLOSING_BALANCE)
            .consumedDuringYear(DEFAULT_CONSUMED_DURING_YEAR)
            .year(DEFAULT_YEAR)
            .amount(DEFAULT_AMOUNT)
            .leaveAmountType(DEFAULT_LEAVE_AMOUNT_TYPE);
        return leaveBalance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveBalance createUpdatedEntity(EntityManager em) {
        LeaveBalance leaveBalance = new LeaveBalance()
            .leaveType(UPDATED_LEAVE_TYPE)
            .openingBalance(UPDATED_OPENING_BALANCE)
            .closingBalance(UPDATED_CLOSING_BALANCE)
            .consumedDuringYear(UPDATED_CONSUMED_DURING_YEAR)
            .year(UPDATED_YEAR)
            .amount(UPDATED_AMOUNT)
            .leaveAmountType(UPDATED_LEAVE_AMOUNT_TYPE);
        return leaveBalance;
    }

    @BeforeEach
    public void initTest() {
        leaveBalance = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaveBalance() throws Exception {
        int databaseSizeBeforeCreate = leaveBalanceRepository.findAll().size();
        // Create the LeaveBalance
        LeaveBalanceDTO leaveBalanceDTO = leaveBalanceMapper.toDto(leaveBalance);
        restLeaveBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveBalanceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaveBalance in the database
        List<LeaveBalance> leaveBalanceList = leaveBalanceRepository.findAll();
        assertThat(leaveBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        LeaveBalance testLeaveBalance = leaveBalanceList.get(leaveBalanceList.size() - 1);
        assertThat(testLeaveBalance.getLeaveType()).isEqualTo(DEFAULT_LEAVE_TYPE);
        assertThat(testLeaveBalance.getOpeningBalance()).isEqualTo(DEFAULT_OPENING_BALANCE);
        assertThat(testLeaveBalance.getClosingBalance()).isEqualTo(DEFAULT_CLOSING_BALANCE);
        assertThat(testLeaveBalance.getConsumedDuringYear()).isEqualTo(DEFAULT_CONSUMED_DURING_YEAR);
        assertThat(testLeaveBalance.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testLeaveBalance.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testLeaveBalance.getLeaveAmountType()).isEqualTo(DEFAULT_LEAVE_AMOUNT_TYPE);
    }

    @Test
    @Transactional
    void createLeaveBalanceWithExistingId() throws Exception {
        // Create the LeaveBalance with an existing ID
        leaveBalance.setId(1L);
        LeaveBalanceDTO leaveBalanceDTO = leaveBalanceMapper.toDto(leaveBalance);

        int databaseSizeBeforeCreate = leaveBalanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveBalance in the database
        List<LeaveBalance> leaveBalanceList = leaveBalanceRepository.findAll();
        assertThat(leaveBalanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLeaveBalances() throws Exception {
        // Initialize the database
        leaveBalanceRepository.saveAndFlush(leaveBalance);

        // Get all the leaveBalanceList
        restLeaveBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaveType").value(hasItem(DEFAULT_LEAVE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].openingBalance").value(hasItem(DEFAULT_OPENING_BALANCE)))
            .andExpect(jsonPath("$.[*].closingBalance").value(hasItem(DEFAULT_CLOSING_BALANCE)))
            .andExpect(jsonPath("$.[*].consumedDuringYear").value(hasItem(DEFAULT_CONSUMED_DURING_YEAR)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].leaveAmountType").value(hasItem(DEFAULT_LEAVE_AMOUNT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getLeaveBalance() throws Exception {
        // Initialize the database
        leaveBalanceRepository.saveAndFlush(leaveBalance);

        // Get the leaveBalance
        restLeaveBalanceMockMvc
            .perform(get(ENTITY_API_URL_ID, leaveBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaveBalance.getId().intValue()))
            .andExpect(jsonPath("$.leaveType").value(DEFAULT_LEAVE_TYPE.toString()))
            .andExpect(jsonPath("$.openingBalance").value(DEFAULT_OPENING_BALANCE))
            .andExpect(jsonPath("$.closingBalance").value(DEFAULT_CLOSING_BALANCE))
            .andExpect(jsonPath("$.consumedDuringYear").value(DEFAULT_CONSUMED_DURING_YEAR))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.leaveAmountType").value(DEFAULT_LEAVE_AMOUNT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLeaveBalance() throws Exception {
        // Get the leaveBalance
        restLeaveBalanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLeaveBalance() throws Exception {
        // Initialize the database
        leaveBalanceRepository.saveAndFlush(leaveBalance);

        int databaseSizeBeforeUpdate = leaveBalanceRepository.findAll().size();

        // Update the leaveBalance
        LeaveBalance updatedLeaveBalance = leaveBalanceRepository.findById(leaveBalance.getId()).get();
        // Disconnect from session so that the updates on updatedLeaveBalance are not directly saved in db
        em.detach(updatedLeaveBalance);
        updatedLeaveBalance
            .leaveType(UPDATED_LEAVE_TYPE)
            .openingBalance(UPDATED_OPENING_BALANCE)
            .closingBalance(UPDATED_CLOSING_BALANCE)
            .consumedDuringYear(UPDATED_CONSUMED_DURING_YEAR)
            .year(UPDATED_YEAR)
            .amount(UPDATED_AMOUNT)
            .leaveAmountType(UPDATED_LEAVE_AMOUNT_TYPE);
        LeaveBalanceDTO leaveBalanceDTO = leaveBalanceMapper.toDto(updatedLeaveBalance);

        restLeaveBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaveBalanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveBalanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaveBalance in the database
        List<LeaveBalance> leaveBalanceList = leaveBalanceRepository.findAll();
        assertThat(leaveBalanceList).hasSize(databaseSizeBeforeUpdate);
        LeaveBalance testLeaveBalance = leaveBalanceList.get(leaveBalanceList.size() - 1);
        assertThat(testLeaveBalance.getLeaveType()).isEqualTo(UPDATED_LEAVE_TYPE);
        assertThat(testLeaveBalance.getOpeningBalance()).isEqualTo(UPDATED_OPENING_BALANCE);
        assertThat(testLeaveBalance.getClosingBalance()).isEqualTo(UPDATED_CLOSING_BALANCE);
        assertThat(testLeaveBalance.getConsumedDuringYear()).isEqualTo(UPDATED_CONSUMED_DURING_YEAR);
        assertThat(testLeaveBalance.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testLeaveBalance.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testLeaveBalance.getLeaveAmountType()).isEqualTo(UPDATED_LEAVE_AMOUNT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingLeaveBalance() throws Exception {
        int databaseSizeBeforeUpdate = leaveBalanceRepository.findAll().size();
        leaveBalance.setId(count.incrementAndGet());

        // Create the LeaveBalance
        LeaveBalanceDTO leaveBalanceDTO = leaveBalanceMapper.toDto(leaveBalance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaveBalanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveBalance in the database
        List<LeaveBalance> leaveBalanceList = leaveBalanceRepository.findAll();
        assertThat(leaveBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaveBalance() throws Exception {
        int databaseSizeBeforeUpdate = leaveBalanceRepository.findAll().size();
        leaveBalance.setId(count.incrementAndGet());

        // Create the LeaveBalance
        LeaveBalanceDTO leaveBalanceDTO = leaveBalanceMapper.toDto(leaveBalance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveBalance in the database
        List<LeaveBalance> leaveBalanceList = leaveBalanceRepository.findAll();
        assertThat(leaveBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaveBalance() throws Exception {
        int databaseSizeBeforeUpdate = leaveBalanceRepository.findAll().size();
        leaveBalance.setId(count.incrementAndGet());

        // Create the LeaveBalance
        LeaveBalanceDTO leaveBalanceDTO = leaveBalanceMapper.toDto(leaveBalance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveBalanceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveBalanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaveBalance in the database
        List<LeaveBalance> leaveBalanceList = leaveBalanceRepository.findAll();
        assertThat(leaveBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLeaveBalanceWithPatch() throws Exception {
        // Initialize the database
        leaveBalanceRepository.saveAndFlush(leaveBalance);

        int databaseSizeBeforeUpdate = leaveBalanceRepository.findAll().size();

        // Update the leaveBalance using partial update
        LeaveBalance partialUpdatedLeaveBalance = new LeaveBalance();
        partialUpdatedLeaveBalance.setId(leaveBalance.getId());

        partialUpdatedLeaveBalance.consumedDuringYear(UPDATED_CONSUMED_DURING_YEAR);

        restLeaveBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaveBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaveBalance))
            )
            .andExpect(status().isOk());

        // Validate the LeaveBalance in the database
        List<LeaveBalance> leaveBalanceList = leaveBalanceRepository.findAll();
        assertThat(leaveBalanceList).hasSize(databaseSizeBeforeUpdate);
        LeaveBalance testLeaveBalance = leaveBalanceList.get(leaveBalanceList.size() - 1);
        assertThat(testLeaveBalance.getLeaveType()).isEqualTo(DEFAULT_LEAVE_TYPE);
        assertThat(testLeaveBalance.getOpeningBalance()).isEqualTo(DEFAULT_OPENING_BALANCE);
        assertThat(testLeaveBalance.getClosingBalance()).isEqualTo(DEFAULT_CLOSING_BALANCE);
        assertThat(testLeaveBalance.getConsumedDuringYear()).isEqualTo(UPDATED_CONSUMED_DURING_YEAR);
        assertThat(testLeaveBalance.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testLeaveBalance.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testLeaveBalance.getLeaveAmountType()).isEqualTo(DEFAULT_LEAVE_AMOUNT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateLeaveBalanceWithPatch() throws Exception {
        // Initialize the database
        leaveBalanceRepository.saveAndFlush(leaveBalance);

        int databaseSizeBeforeUpdate = leaveBalanceRepository.findAll().size();

        // Update the leaveBalance using partial update
        LeaveBalance partialUpdatedLeaveBalance = new LeaveBalance();
        partialUpdatedLeaveBalance.setId(leaveBalance.getId());

        partialUpdatedLeaveBalance
            .leaveType(UPDATED_LEAVE_TYPE)
            .openingBalance(UPDATED_OPENING_BALANCE)
            .closingBalance(UPDATED_CLOSING_BALANCE)
            .consumedDuringYear(UPDATED_CONSUMED_DURING_YEAR)
            .year(UPDATED_YEAR)
            .amount(UPDATED_AMOUNT)
            .leaveAmountType(UPDATED_LEAVE_AMOUNT_TYPE);

        restLeaveBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaveBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaveBalance))
            )
            .andExpect(status().isOk());

        // Validate the LeaveBalance in the database
        List<LeaveBalance> leaveBalanceList = leaveBalanceRepository.findAll();
        assertThat(leaveBalanceList).hasSize(databaseSizeBeforeUpdate);
        LeaveBalance testLeaveBalance = leaveBalanceList.get(leaveBalanceList.size() - 1);
        assertThat(testLeaveBalance.getLeaveType()).isEqualTo(UPDATED_LEAVE_TYPE);
        assertThat(testLeaveBalance.getOpeningBalance()).isEqualTo(UPDATED_OPENING_BALANCE);
        assertThat(testLeaveBalance.getClosingBalance()).isEqualTo(UPDATED_CLOSING_BALANCE);
        assertThat(testLeaveBalance.getConsumedDuringYear()).isEqualTo(UPDATED_CONSUMED_DURING_YEAR);
        assertThat(testLeaveBalance.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testLeaveBalance.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testLeaveBalance.getLeaveAmountType()).isEqualTo(UPDATED_LEAVE_AMOUNT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingLeaveBalance() throws Exception {
        int databaseSizeBeforeUpdate = leaveBalanceRepository.findAll().size();
        leaveBalance.setId(count.incrementAndGet());

        // Create the LeaveBalance
        LeaveBalanceDTO leaveBalanceDTO = leaveBalanceMapper.toDto(leaveBalance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaveBalanceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveBalance in the database
        List<LeaveBalance> leaveBalanceList = leaveBalanceRepository.findAll();
        assertThat(leaveBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaveBalance() throws Exception {
        int databaseSizeBeforeUpdate = leaveBalanceRepository.findAll().size();
        leaveBalance.setId(count.incrementAndGet());

        // Create the LeaveBalance
        LeaveBalanceDTO leaveBalanceDTO = leaveBalanceMapper.toDto(leaveBalance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveBalance in the database
        List<LeaveBalance> leaveBalanceList = leaveBalanceRepository.findAll();
        assertThat(leaveBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaveBalance() throws Exception {
        int databaseSizeBeforeUpdate = leaveBalanceRepository.findAll().size();
        leaveBalance.setId(count.incrementAndGet());

        // Create the LeaveBalance
        LeaveBalanceDTO leaveBalanceDTO = leaveBalanceMapper.toDto(leaveBalance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveBalanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaveBalance in the database
        List<LeaveBalance> leaveBalanceList = leaveBalanceRepository.findAll();
        assertThat(leaveBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLeaveBalance() throws Exception {
        // Initialize the database
        leaveBalanceRepository.saveAndFlush(leaveBalance);

        int databaseSizeBeforeDelete = leaveBalanceRepository.findAll().size();

        // Delete the leaveBalance
        restLeaveBalanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaveBalance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaveBalance> leaveBalanceList = leaveBalanceRepository.findAll();
        assertThat(leaveBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
