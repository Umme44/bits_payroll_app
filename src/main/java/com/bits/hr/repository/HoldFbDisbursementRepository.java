package com.bits.hr.repository;

import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.domain.HoldFbDisbursement;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the HoldFbDisbursement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HoldFbDisbursementRepository extends JpaRepository<HoldFbDisbursement, Long> {
    @Query(
        "select holdFbDisbursement from HoldFbDisbursement holdFbDisbursement where holdFbDisbursement.disbursedBy.login = ?#{principal.username}"
    )
    List<HoldFbDisbursement> findByDisbursedByIsCurrentUser();

    @Query(
        "select model from HoldFbDisbursement model " +
        "where model.festivalBonusDetail.employee.pin like %:searchText% " +
        "OR lower(model.festivalBonusDetail.employee.fullName) LIKE lower(CONCAT('%',:searchText,'%'))"
    )
    Page<HoldFbDisbursement> findAllDisbursedFB(String searchText, Pageable pageable);

    @Modifying
    @Query("delete from HoldFbDisbursement model where model.festivalBonusDetail=:festivalBonusDetails")
    void deleteAllByFestivalBonusDetails(FestivalBonusDetails festivalBonusDetails);
}
