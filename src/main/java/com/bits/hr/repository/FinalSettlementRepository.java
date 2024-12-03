package com.bits.hr.repository;

import com.bits.hr.domain.FinalSettlement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FinalSettlement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinalSettlementRepository extends JpaRepository<FinalSettlement, Long> {
    @Query("select finalSettlement from FinalSettlement finalSettlement where finalSettlement.createdBy.login = ?#{principal.username}")
    List<FinalSettlement> findByCreatedByIsCurrentUser();

    @Query("select finalSettlement from FinalSettlement finalSettlement where finalSettlement.updatedBy.login = ?#{principal.username}")
    List<FinalSettlement> findByUpdatedByIsCurrentUser();

    @Query(value = "select model from FinalSettlement model where model.employee.id= :employeeId")
    List<FinalSettlement> findFinalSettlementByEmployeeId(long employeeId);
}
