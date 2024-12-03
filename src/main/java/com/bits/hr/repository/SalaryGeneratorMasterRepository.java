package com.bits.hr.repository;

import com.bits.hr.domain.SalaryGeneratorMaster;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * get()
 * Spring Data  repository for the SalaryGeneratorMaster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalaryGeneratorMasterRepository extends JpaRepository<SalaryGeneratorMaster, Long> {
    List<SalaryGeneratorMaster> findAllByOrderByYearDescMonthDesc();

    Optional<SalaryGeneratorMaster> findSalaryGeneratorMastersByYearAndMonth(String year, String month);

    @Query(value = "select u from SalaryGeneratorMaster u where u.isFinalized is null")
    List<SalaryGeneratorMaster> getWhereIsFinalizedNUll();

    @Query(value = "select u from SalaryGeneratorMaster u where u.isGenerated=true AND u.isFinalized = true")
    List<SalaryGeneratorMaster> getAllGeneratedAndFinalized();

    @Query(value = "select u from SalaryGeneratorMaster u where u.isGenerated=true AND u.isFinalized = false")
    List<SalaryGeneratorMaster> getAllGeneratedAndNotFinalized();
}
