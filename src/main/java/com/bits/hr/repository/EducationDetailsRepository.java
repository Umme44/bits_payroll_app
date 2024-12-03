package com.bits.hr.repository;

import com.bits.hr.domain.EducationDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EducationDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EducationDetailsRepository extends JpaRepository<EducationDetails, Long> {
    @Query(value = "select model from EducationDetails model where model.employee.id = :employeeId")
    List<EducationDetails> findAllByEmployeeId(long employeeId);
}
