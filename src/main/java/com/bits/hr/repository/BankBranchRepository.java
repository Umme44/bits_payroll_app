package com.bits.hr.repository;

import com.bits.hr.domain.BankBranch;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the BankBranch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankBranchRepository extends JpaRepository<BankBranch, Long> {
    Optional<BankBranch> findBankBranchByBranchName(String bankName);
}
