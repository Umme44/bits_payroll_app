package com.bits.hr.repository;

import com.bits.hr.domain.PfLoanApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PfLoanApplication entity.
 */
@Repository
public interface PfLoanApplicationFormRepository extends JpaRepository<PfLoanApplication, Long> {
    Page<PfLoanApplication> findAllByPfAccountPin(Pageable pageable, String pin);
}
