package com.bits.hr.repository;

import com.bits.hr.domain.References;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the References entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReferencesRepository extends JpaRepository<References, Long> {
    @Query(value = "select model from References model where model.employee.id = :employeeId")
    List<References> findAllByEmployee(long employeeId);
}
