package com.bits.hr.repository;

import com.bits.hr.domain.PfLoanApplication;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PfLoanApplication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PfLoanApplicationRepository extends JpaRepository<PfLoanApplication, Long> {
    @Query("select model from PfLoanApplication model where model.id in :ids and model.isApproved<>true and model.isRejected <> true ")
    List<PfLoanApplication> getPendingPfApplicationByIds(List<Long> ids);

    @Query(
        "select model from PfLoanApplication model where model.status='PENDING' and model.isApproved = false and model.isRejected = false"
    )
    List<PfLoanApplication> getPendingLoanApplication();
}
