package com.bits.hr.repository;

import com.bits.hr.domain.WorkingExperience;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the WorkingExperience entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkingExperienceRepository extends JpaRepository<WorkingExperience, Long> {
    @Query(value = "select model from WorkingExperience model where model.employee.id = :employeeId")
    List<WorkingExperience> findAllByEmployeeId(long employeeId);
}
