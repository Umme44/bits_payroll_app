package com.bits.hr.repository;

import com.bits.hr.domain.ArrearSalaryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ArrearSalaryItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArrearSalaryItemRepository extends JpaRepository<ArrearSalaryItem, Long> {
    @Query(
        value = "select model from ArrearSalaryItem model " +
        "where model.arrearSalaryMaster.id = :arrearsSalaryMasterId " +
        "AND model.isDeleted = false"
    )
    Page<ArrearSalaryItem> findByArrearsSalaryMaster(long arrearsSalaryMasterId, Pageable pageable);
}
