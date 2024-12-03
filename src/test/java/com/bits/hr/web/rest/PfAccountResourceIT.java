package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.enumeration.PfAccountStatus;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.service.dto.PfAccountDTO;
import com.bits.hr.service.mapper.PfAccountMapper;
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
 * Integration tests for the {@link PfAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PfAccountResourceIT {

    private static final String DEFAULT_PF_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PF_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_MEMBERSHIP_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MEMBERSHIP_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MEMBERSHIP_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MEMBERSHIP_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final PfAccountStatus DEFAULT_STATUS = PfAccountStatus.ACTIVE;
    private static final PfAccountStatus UPDATED_STATUS = PfAccountStatus.INACTIVE;

    private static final String DEFAULT_DESIGNATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACC_HOLDER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACC_HOLDER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PIN = "AAAAAAAAAA";
    private static final String UPDATED_PIN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_JOINING = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_JOINING = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_OF_CONFIRMATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_CONFIRMATION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/pf-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PfAccountRepository pfAccountRepository;

    @Autowired
    private PfAccountMapper pfAccountMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPfAccountMockMvc;

    private PfAccount pfAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PfAccount createEntity(EntityManager em) {
        PfAccount pfAccount = new PfAccount()
            .pfCode(DEFAULT_PF_CODE)
            .membershipStartDate(DEFAULT_MEMBERSHIP_START_DATE)
            .membershipEndDate(DEFAULT_MEMBERSHIP_END_DATE)
            .status(DEFAULT_STATUS)
            .designationName(DEFAULT_DESIGNATION_NAME)
            .departmentName(DEFAULT_DEPARTMENT_NAME)
            .unitName(DEFAULT_UNIT_NAME)
            .accHolderName(DEFAULT_ACC_HOLDER_NAME)
            .pin(DEFAULT_PIN)
            .dateOfJoining(DEFAULT_DATE_OF_JOINING)
            .dateOfConfirmation(DEFAULT_DATE_OF_CONFIRMATION);
        return pfAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PfAccount createUpdatedEntity(EntityManager em) {
        PfAccount pfAccount = new PfAccount()
            .pfCode(UPDATED_PF_CODE)
            .membershipStartDate(UPDATED_MEMBERSHIP_START_DATE)
            .membershipEndDate(UPDATED_MEMBERSHIP_END_DATE)
            .status(UPDATED_STATUS)
            .designationName(UPDATED_DESIGNATION_NAME)
            .departmentName(UPDATED_DEPARTMENT_NAME)
            .unitName(UPDATED_UNIT_NAME)
            .accHolderName(UPDATED_ACC_HOLDER_NAME)
            .pin(UPDATED_PIN)
            .dateOfJoining(UPDATED_DATE_OF_JOINING)
            .dateOfConfirmation(UPDATED_DATE_OF_CONFIRMATION);
        return pfAccount;
    }

    @BeforeEach
    public void initTest() {
        pfAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createPfAccount() throws Exception {
        int databaseSizeBeforeCreate = pfAccountRepository.findAll().size();
        // Create the PfAccount
        PfAccountDTO pfAccountDTO = pfAccountMapper.toDto(pfAccount);
        restPfAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the PfAccount in the database
        List<PfAccount> pfAccountList = pfAccountRepository.findAll();
        assertThat(pfAccountList).hasSize(databaseSizeBeforeCreate + 1);
        PfAccount testPfAccount = pfAccountList.get(pfAccountList.size() - 1);
        assertThat(testPfAccount.getPfCode()).isEqualTo(DEFAULT_PF_CODE);
        assertThat(testPfAccount.getMembershipStartDate()).isEqualTo(DEFAULT_MEMBERSHIP_START_DATE);
        assertThat(testPfAccount.getMembershipEndDate()).isEqualTo(DEFAULT_MEMBERSHIP_END_DATE);
        assertThat(testPfAccount.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPfAccount.getDesignationName()).isEqualTo(DEFAULT_DESIGNATION_NAME);
        assertThat(testPfAccount.getDepartmentName()).isEqualTo(DEFAULT_DEPARTMENT_NAME);
        assertThat(testPfAccount.getUnitName()).isEqualTo(DEFAULT_UNIT_NAME);
        assertThat(testPfAccount.getAccHolderName()).isEqualTo(DEFAULT_ACC_HOLDER_NAME);
        assertThat(testPfAccount.getPin()).isEqualTo(DEFAULT_PIN);
        assertThat(testPfAccount.getDateOfJoining()).isEqualTo(DEFAULT_DATE_OF_JOINING);
        assertThat(testPfAccount.getDateOfConfirmation()).isEqualTo(DEFAULT_DATE_OF_CONFIRMATION);
    }

    @Test
    @Transactional
    void createPfAccountWithExistingId() throws Exception {
        // Create the PfAccount with an existing ID
        pfAccount.setId(1L);
        PfAccountDTO pfAccountDTO = pfAccountMapper.toDto(pfAccount);

        int databaseSizeBeforeCreate = pfAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPfAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PfAccount in the database
        List<PfAccount> pfAccountList = pfAccountRepository.findAll();
        assertThat(pfAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPfAccounts() throws Exception {
        // Initialize the database
        pfAccountRepository.saveAndFlush(pfAccount);

        // Get all the pfAccountList
        restPfAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pfAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].pfCode").value(hasItem(DEFAULT_PF_CODE)))
            .andExpect(jsonPath("$.[*].membershipStartDate").value(hasItem(DEFAULT_MEMBERSHIP_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].membershipEndDate").value(hasItem(DEFAULT_MEMBERSHIP_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].designationName").value(hasItem(DEFAULT_DESIGNATION_NAME)))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].unitName").value(hasItem(DEFAULT_UNIT_NAME)))
            .andExpect(jsonPath("$.[*].accHolderName").value(hasItem(DEFAULT_ACC_HOLDER_NAME)))
            .andExpect(jsonPath("$.[*].pin").value(hasItem(DEFAULT_PIN)))
            .andExpect(jsonPath("$.[*].dateOfJoining").value(hasItem(DEFAULT_DATE_OF_JOINING.toString())))
            .andExpect(jsonPath("$.[*].dateOfConfirmation").value(hasItem(DEFAULT_DATE_OF_CONFIRMATION.toString())));
    }

    @Test
    @Transactional
    void getPfAccount() throws Exception {
        // Initialize the database
        pfAccountRepository.saveAndFlush(pfAccount);

        // Get the pfAccount
        restPfAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, pfAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pfAccount.getId().intValue()))
            .andExpect(jsonPath("$.pfCode").value(DEFAULT_PF_CODE))
            .andExpect(jsonPath("$.membershipStartDate").value(DEFAULT_MEMBERSHIP_START_DATE.toString()))
            .andExpect(jsonPath("$.membershipEndDate").value(DEFAULT_MEMBERSHIP_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.designationName").value(DEFAULT_DESIGNATION_NAME))
            .andExpect(jsonPath("$.departmentName").value(DEFAULT_DEPARTMENT_NAME))
            .andExpect(jsonPath("$.unitName").value(DEFAULT_UNIT_NAME))
            .andExpect(jsonPath("$.accHolderName").value(DEFAULT_ACC_HOLDER_NAME))
            .andExpect(jsonPath("$.pin").value(DEFAULT_PIN))
            .andExpect(jsonPath("$.dateOfJoining").value(DEFAULT_DATE_OF_JOINING.toString()))
            .andExpect(jsonPath("$.dateOfConfirmation").value(DEFAULT_DATE_OF_CONFIRMATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPfAccount() throws Exception {
        // Get the pfAccount
        restPfAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPfAccount() throws Exception {
        // Initialize the database
        pfAccountRepository.saveAndFlush(pfAccount);

        int databaseSizeBeforeUpdate = pfAccountRepository.findAll().size();

        // Update the pfAccount
        PfAccount updatedPfAccount = pfAccountRepository.findById(pfAccount.getId()).get();
        // Disconnect from session so that the updates on updatedPfAccount are not directly saved in db
        em.detach(updatedPfAccount);
        updatedPfAccount
            .pfCode(UPDATED_PF_CODE)
            .membershipStartDate(UPDATED_MEMBERSHIP_START_DATE)
            .membershipEndDate(UPDATED_MEMBERSHIP_END_DATE)
            .status(UPDATED_STATUS)
            .designationName(UPDATED_DESIGNATION_NAME)
            .departmentName(UPDATED_DEPARTMENT_NAME)
            .unitName(UPDATED_UNIT_NAME)
            .accHolderName(UPDATED_ACC_HOLDER_NAME)
            .pin(UPDATED_PIN)
            .dateOfJoining(UPDATED_DATE_OF_JOINING)
            .dateOfConfirmation(UPDATED_DATE_OF_CONFIRMATION);
        PfAccountDTO pfAccountDTO = pfAccountMapper.toDto(updatedPfAccount);

        restPfAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pfAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the PfAccount in the database
        List<PfAccount> pfAccountList = pfAccountRepository.findAll();
        assertThat(pfAccountList).hasSize(databaseSizeBeforeUpdate);
        PfAccount testPfAccount = pfAccountList.get(pfAccountList.size() - 1);
        assertThat(testPfAccount.getPfCode()).isEqualTo(UPDATED_PF_CODE);
        assertThat(testPfAccount.getMembershipStartDate()).isEqualTo(UPDATED_MEMBERSHIP_START_DATE);
        assertThat(testPfAccount.getMembershipEndDate()).isEqualTo(UPDATED_MEMBERSHIP_END_DATE);
        assertThat(testPfAccount.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPfAccount.getDesignationName()).isEqualTo(UPDATED_DESIGNATION_NAME);
        assertThat(testPfAccount.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
        assertThat(testPfAccount.getUnitName()).isEqualTo(UPDATED_UNIT_NAME);
        assertThat(testPfAccount.getAccHolderName()).isEqualTo(UPDATED_ACC_HOLDER_NAME);
        assertThat(testPfAccount.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testPfAccount.getDateOfJoining()).isEqualTo(UPDATED_DATE_OF_JOINING);
        assertThat(testPfAccount.getDateOfConfirmation()).isEqualTo(UPDATED_DATE_OF_CONFIRMATION);
    }

    @Test
    @Transactional
    void putNonExistingPfAccount() throws Exception {
        int databaseSizeBeforeUpdate = pfAccountRepository.findAll().size();
        pfAccount.setId(count.incrementAndGet());

        // Create the PfAccount
        PfAccountDTO pfAccountDTO = pfAccountMapper.toDto(pfAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPfAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pfAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfAccount in the database
        List<PfAccount> pfAccountList = pfAccountRepository.findAll();
        assertThat(pfAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPfAccount() throws Exception {
        int databaseSizeBeforeUpdate = pfAccountRepository.findAll().size();
        pfAccount.setId(count.incrementAndGet());

        // Create the PfAccount
        PfAccountDTO pfAccountDTO = pfAccountMapper.toDto(pfAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfAccount in the database
        List<PfAccount> pfAccountList = pfAccountRepository.findAll();
        assertThat(pfAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPfAccount() throws Exception {
        int databaseSizeBeforeUpdate = pfAccountRepository.findAll().size();
        pfAccount.setId(count.incrementAndGet());

        // Create the PfAccount
        PfAccountDTO pfAccountDTO = pfAccountMapper.toDto(pfAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfAccountDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PfAccount in the database
        List<PfAccount> pfAccountList = pfAccountRepository.findAll();
        assertThat(pfAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePfAccountWithPatch() throws Exception {
        // Initialize the database
        pfAccountRepository.saveAndFlush(pfAccount);

        int databaseSizeBeforeUpdate = pfAccountRepository.findAll().size();

        // Update the pfAccount using partial update
        PfAccount partialUpdatedPfAccount = new PfAccount();
        partialUpdatedPfAccount.setId(pfAccount.getId());

        partialUpdatedPfAccount
            .pfCode(UPDATED_PF_CODE)
            .membershipStartDate(UPDATED_MEMBERSHIP_START_DATE)
            .status(UPDATED_STATUS)
            .designationName(UPDATED_DESIGNATION_NAME)
            .departmentName(UPDATED_DEPARTMENT_NAME)
            .unitName(UPDATED_UNIT_NAME)
            .pin(UPDATED_PIN)
            .dateOfConfirmation(UPDATED_DATE_OF_CONFIRMATION);

        restPfAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPfAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPfAccount))
            )
            .andExpect(status().isOk());

        // Validate the PfAccount in the database
        List<PfAccount> pfAccountList = pfAccountRepository.findAll();
        assertThat(pfAccountList).hasSize(databaseSizeBeforeUpdate);
        PfAccount testPfAccount = pfAccountList.get(pfAccountList.size() - 1);
        assertThat(testPfAccount.getPfCode()).isEqualTo(UPDATED_PF_CODE);
        assertThat(testPfAccount.getMembershipStartDate()).isEqualTo(UPDATED_MEMBERSHIP_START_DATE);
        assertThat(testPfAccount.getMembershipEndDate()).isEqualTo(DEFAULT_MEMBERSHIP_END_DATE);
        assertThat(testPfAccount.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPfAccount.getDesignationName()).isEqualTo(UPDATED_DESIGNATION_NAME);
        assertThat(testPfAccount.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
        assertThat(testPfAccount.getUnitName()).isEqualTo(UPDATED_UNIT_NAME);
        assertThat(testPfAccount.getAccHolderName()).isEqualTo(DEFAULT_ACC_HOLDER_NAME);
        assertThat(testPfAccount.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testPfAccount.getDateOfJoining()).isEqualTo(DEFAULT_DATE_OF_JOINING);
        assertThat(testPfAccount.getDateOfConfirmation()).isEqualTo(UPDATED_DATE_OF_CONFIRMATION);
    }

    @Test
    @Transactional
    void fullUpdatePfAccountWithPatch() throws Exception {
        // Initialize the database
        pfAccountRepository.saveAndFlush(pfAccount);

        int databaseSizeBeforeUpdate = pfAccountRepository.findAll().size();

        // Update the pfAccount using partial update
        PfAccount partialUpdatedPfAccount = new PfAccount();
        partialUpdatedPfAccount.setId(pfAccount.getId());

        partialUpdatedPfAccount
            .pfCode(UPDATED_PF_CODE)
            .membershipStartDate(UPDATED_MEMBERSHIP_START_DATE)
            .membershipEndDate(UPDATED_MEMBERSHIP_END_DATE)
            .status(UPDATED_STATUS)
            .designationName(UPDATED_DESIGNATION_NAME)
            .departmentName(UPDATED_DEPARTMENT_NAME)
            .unitName(UPDATED_UNIT_NAME)
            .accHolderName(UPDATED_ACC_HOLDER_NAME)
            .pin(UPDATED_PIN)
            .dateOfJoining(UPDATED_DATE_OF_JOINING)
            .dateOfConfirmation(UPDATED_DATE_OF_CONFIRMATION);

        restPfAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPfAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPfAccount))
            )
            .andExpect(status().isOk());

        // Validate the PfAccount in the database
        List<PfAccount> pfAccountList = pfAccountRepository.findAll();
        assertThat(pfAccountList).hasSize(databaseSizeBeforeUpdate);
        PfAccount testPfAccount = pfAccountList.get(pfAccountList.size() - 1);
        assertThat(testPfAccount.getPfCode()).isEqualTo(UPDATED_PF_CODE);
        assertThat(testPfAccount.getMembershipStartDate()).isEqualTo(UPDATED_MEMBERSHIP_START_DATE);
        assertThat(testPfAccount.getMembershipEndDate()).isEqualTo(UPDATED_MEMBERSHIP_END_DATE);
        assertThat(testPfAccount.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPfAccount.getDesignationName()).isEqualTo(UPDATED_DESIGNATION_NAME);
        assertThat(testPfAccount.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
        assertThat(testPfAccount.getUnitName()).isEqualTo(UPDATED_UNIT_NAME);
        assertThat(testPfAccount.getAccHolderName()).isEqualTo(UPDATED_ACC_HOLDER_NAME);
        assertThat(testPfAccount.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testPfAccount.getDateOfJoining()).isEqualTo(UPDATED_DATE_OF_JOINING);
        assertThat(testPfAccount.getDateOfConfirmation()).isEqualTo(UPDATED_DATE_OF_CONFIRMATION);
    }

    @Test
    @Transactional
    void patchNonExistingPfAccount() throws Exception {
        int databaseSizeBeforeUpdate = pfAccountRepository.findAll().size();
        pfAccount.setId(count.incrementAndGet());

        // Create the PfAccount
        PfAccountDTO pfAccountDTO = pfAccountMapper.toDto(pfAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPfAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pfAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfAccount in the database
        List<PfAccount> pfAccountList = pfAccountRepository.findAll();
        assertThat(pfAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPfAccount() throws Exception {
        int databaseSizeBeforeUpdate = pfAccountRepository.findAll().size();
        pfAccount.setId(count.incrementAndGet());

        // Create the PfAccount
        PfAccountDTO pfAccountDTO = pfAccountMapper.toDto(pfAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfAccount in the database
        List<PfAccount> pfAccountList = pfAccountRepository.findAll();
        assertThat(pfAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPfAccount() throws Exception {
        int databaseSizeBeforeUpdate = pfAccountRepository.findAll().size();
        pfAccount.setId(count.incrementAndGet());

        // Create the PfAccount
        PfAccountDTO pfAccountDTO = pfAccountMapper.toDto(pfAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfAccountMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pfAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PfAccount in the database
        List<PfAccount> pfAccountList = pfAccountRepository.findAll();
        assertThat(pfAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePfAccount() throws Exception {
        // Initialize the database
        pfAccountRepository.saveAndFlush(pfAccount);

        int databaseSizeBeforeDelete = pfAccountRepository.findAll().size();

        // Delete the pfAccount
        restPfAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, pfAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PfAccount> pfAccountList = pfAccountRepository.findAll();
        assertThat(pfAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
