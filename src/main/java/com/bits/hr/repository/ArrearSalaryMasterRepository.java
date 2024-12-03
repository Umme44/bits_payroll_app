package com.bits.hr.repository;

import com.bits.hr.domain.ArrearSalaryMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ArrearSalaryMaster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArrearSalaryMasterRepository extends JpaRepository<ArrearSalaryMaster, Long> {
    @Query(value = "select model from ArrearSalaryMaster model where model.isDeleted = false ")
    Page<ArrearSalaryMaster> findAllActive(Pageable pageable);
}
